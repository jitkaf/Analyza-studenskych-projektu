package bp.agregovanegrafy;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import knihovna.Barva;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import bp.Konstanty;
import bp.nactenidat.VysledekValidaceSouboru;


/**
 * Tato třída slouží pro vytvoření agregovaného grafu Stav odevzdání domácích úloh. 
 * Na ose X jsou domácí úlohy a na ose Y je  počet jejich odevzdání. Žlutě jsou znázorněny
 * neúspěšné pokusy, červeně úspěšné opožděné a zeleně úspěšné včasné.
 * @author Jitka Fürbacherová
 * @version 1.0
 */
public class GrafStavOdevzdaniDomacichUloh extends ApplicationFrame{
	private static final long serialVersionUID = 1L;
		/** <idUlohy, počet neúspěšných odevzdání dané úlohy> */
	static TreeMap<String, Integer> neuspesnePokusy = new TreeMap<String, Integer>();
		/** <idUlohy, počet úpěšných včasných odevzdání dané úlohy> */
	static TreeMap<String, Integer> uspesneVcasne = new TreeMap<String, Integer>();
		/** <idUlohy, počet úpěšných opožděných odevzdání dané úlohy> */
	static TreeMap<String, Integer> uspesneOpozdene = new TreeMap<String, Integer>();
	private String titulek;
    private int max = 0; //největší počet odevzdání v jeden den
    
    /**
     * V tomto konstruktoru je dle parametrů nastaven titulek a výstupní adresář. 
	 * Dále jsou volány metody pro zpracování předaných výsledků validací.
     * @param title - titulek grafu
     * @param rok - aktuální rok, pro který je graf tvořen
     * @param vysledky - výsledky validací, které budou v této třídě zpracovávány
     * @param jenUspesne - přepínač určující, zda zpracovávám jen úspěšné validace nebo i pokusy
     * @param vystupniAdresar - kořenový adresář, do kterého mají být grafy tvořeny
     */
	public GrafStavOdevzdaniDomacichUloh(final String title, int rok,
			ArrayList<VysledekValidaceSouboru> vysledky, boolean jenUspesne, String vystupniAdresar){
		super(title);
		titulek =  title;
	    VytvorMapu(vysledky);
	    NaplnMapu(vysledky, jenUspesne);
        final CategoryDataset dataset = createDataset(jenUspesne);
        final JFreeChart chart = createChart(dataset,jenUspesne);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(Konstanty.SIRKA_A, Konstanty.VYSKA_A));
        setContentPane(chartPanel);
        uloz(rok, chart, jenUspesne, vystupniAdresar);
	}


	/**
	 * V této metodě je vygenerován graf a uložen.
	 * @param rok
	 * @param chart
	 * @param jenUspesne
	 * @param vystupniAdresar
	 */
	private void uloz(int rok, JFreeChart chart, boolean jenUspesne, String vystupniAdresar){
		File nazevSlozky = new File (vystupniAdresar);
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
        nazevSlozky = new File (vystupniAdresar + "/" + rok);
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
       
       	nazevSlozky = new File (vystupniAdresar + "/"+ rok + Konstanty.GRAF_STAV_ODEVZDANI_DOMACICH_ULOH_SLOZKA);
   		if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
   		String nazev = "";
   		if(jenUspesne){
   			nazev = Konstanty.GRAF_STAV_DOMACICH_ULOH_OK_SOUBOR + rok;
   		}
   		else{
   			nazev = Konstanty.GRAF_STAV_DOMACICH_ULOH_VSE_SOUBOR + rok;
   		}
        File pieChart = new File(nazevSlozky  + nazev + Konstanty.KONCOVKA); 
        try{
			ChartUtilities.saveChartAsPNG(pieChart, chart, Konstanty.SIRKA_A, Konstanty.VYSKA_A);
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * V této metodě jsou naplněny mapy neuspesnePokusy, uspesneVcasne a uspesneOpozdene. 
	 * Klíčem těchto map je idUlohy, hodnotou počet jejich odevzdání (včasného/opožděného 
	 * úspěšného nebo neúspěšného). O tom zda je odevzdání úspěšné a včasné rozhodují 
	 * atributy ve VysledekValidaceSouboru, který je uložen ve vysledky. 
	 * @param vysledky
	 * @param jenUspesne
	 */
	private void NaplnMapu(List<VysledekValidaceSouboru> vysledky, boolean jenUspesne){
		for(VysledekValidaceSouboru vys: vysledky){
			if((vys.getIdUlohy()!= null) && (vys.getDatumZacatku()!= null)){
				if((jenUspesne != Konstanty.JEN_USPESNE) && (vys.getVysledek().getPopisVysledku().compareTo("OK") != 0)){
					pridejDoMap(vys, neuspesnePokusy);
				}
				
				if((vys.getVysledek().getPopisVysledku().compareTo("OK") == 0) && (vys.getOdevzdazdanoVcas())){
					pridejDoMap(vys, uspesneVcasne);
				}
				else if((vys.getVysledek().getPopisVysledku().compareTo("OK") == 0) && (!vys.getOdevzdazdanoVcas())){
					pridejDoMap(vys, uspesneOpozdene);
				}
			}	
		}
	}
	
	/**
	 * Připočte do požadované mapy zpracovávaný výsledek validace.
	 * @param vys - zpracovávaný výsledek validace
	 * @param map - mapa, klíčem je idUlohy a hodnotou počet odevzdání
	 * vyhovující požadavkům (ok, včas..)
	 */
	private void pridejDoMap(VysledekValidaceSouboru vys, TreeMap<String, Integer> map){
		map.put(vys.getIdUlohy(), map.get(vys.getIdUlohy()) + 1);
	}


	/**
	 * Vytvoří mapy neuspesnePokusy, neuspesneVcasne a uspesneOpozdene, klíčem těchto
	 * map jsou id úloh a hodnotou je vždy 0. Hodnota reprezentuje počet načtených
	 * úloh daného idUlohy. 
	 * @param vysledky
	 */
	private void VytvorMapu(List<VysledekValidaceSouboru> vysledky){
		for(String idUlohy: Konstanty.ID_ULOH){
			neuspesnePokusy.put(idUlohy, 0);
			uspesneVcasne.put(idUlohy, 0);
			uspesneOpozdene.put(idUlohy, 0);
		}
	}	
	
	/**
	 * Nastaví dataset.
	 * @param jenUspesne
	 * @return
	 */
	private CategoryDataset createDataset(boolean jenUspesne){
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		for(String key: neuspesnePokusy.keySet()){
			if (jenUspesne != Konstanty.JEN_USPESNE){
				result.addValue(neuspesnePokusy.get(key), Konstanty.LEGENDA_NEUSPESNE, "" + (key));
			}
			result.addValue(uspesneVcasne.get(key), Konstanty.LEGENDA_USPESNE_VCAS, "" + (key));
			result.addValue(uspesneOpozdene.get(key), Konstanty.LEGENDA_USPESNE_POZDE, "" + (key));
			int pocOdevzdani = neuspesnePokusy.get(key) + uspesneVcasne.get(key) + uspesneOpozdene.get(key);
			if(pocOdevzdani > max){
				max = pocOdevzdani;
			}
		}
	    
		return result;
	}
	
	/**
	 * Nastaví parametry grafu.
	 * @param dataset
	 * @return
	 */
	private JFreeChart createChart(final CategoryDataset dataset, boolean jenUspesne){
        final JFreeChart chart = ChartFactory.createStackedBarChart(
            titulek, 										// titulek grafu
            Konstanty.ODEVZDANI_DOMACICH_ULOH_POPIS_X,      // domain axis label
            Konstanty.POPIS_Y,                    			// range axis label
            dataset,                    			    	// data
            PlotOrientation.VERTICAL,   					// plot orientace
            true,                        					// legenda
            true,                        					// tooltipy
            false                   				  		// url
        );
       
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        KeyToGroupMap map = new KeyToGroupMap();

        renderer.setSeriesToGroupMap(map);
        renderer.setItemMargin(0.10);
        renderer.setDrawBarOutline(false);

        if(jenUspesne != Konstanty.JEN_USPESNE){
	        Paint p1 = new GradientPaint(0.0f, 0.0f, Barva.ZLATA.getColor(),
	                0.0f, 0.0f, Barva.ZLUTA.getColor());
	        renderer.setSeriesPaint(0, p1);
	        Paint p2 = new GradientPaint(0.0f, 0.0f, Barva.KHAKI.getColor(),
	                0.0f, 0.0f, Barva.ZELENA.getColor());
	        renderer.setSeriesPaint(1, p2);
	        Paint p3 = new GradientPaint(0.0f, 0.0f, Barva.CIHLOVA.getColor(),
	                0.0f, 0.0f, Barva.CERVENA.getColor());
	        renderer.setSeriesPaint(2, p3);
        }
        else{
	        Paint p1 = new GradientPaint(0.0f, 0.0f, Barva.KHAKI.getColor(),
	                0.0f, 0.0f, Barva.ZELENA.getColor());
	        renderer.setSeriesPaint(0, p1);
	        Paint p2 = new GradientPaint(0.0f, 0.0f, Barva.CIHLOVA.getColor(),
	                0.0f, 0.0f, Barva.CERVENA.getColor());
	        renderer.setSeriesPaint(1, p2);
        }
      
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(createLegendItems(jenUspesne));
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        if(max < Konstanty.MIN_POCET_POLOZEK){
        	range.setTickUnit(new NumberTickUnit(1));
        }
        
        return chart;
    }
	
	/**
	 * Nastaví legendu.
	 * @return
	 */
	private LegendItemCollection createLegendItems(boolean jenUspesne){
		LegendItemCollection result = new LegendItemCollection();
		if(!jenUspesne){
		LegendItem item1 = new LegendItem(Konstanty.LEGENDA_NEUSPESNE, "-", null, null,
				Plot.DEFAULT_LEGEND_ITEM_BOX, Barva.ZLUTA.getColor());
		result.add(item1);
		}
		LegendItem item2 = new LegendItem(Konstanty.LEGENDA_USPESNE_VCAS,
				"-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, Barva.ZELENA.getColor());
		LegendItem item3 = new LegendItem(Konstanty.LEGENDA_USPESNE_POZDE,
				"-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, Barva.CERVENA.getColor());
		result.add(item2);
		result.add(item3);
	  
		return result;
	}
}
