/**
 * 
 */
package bp.agregovanegrafy;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import bp.Konstanty;
import bp.nactenidat.VysledekValidaceSouboru;

/**
 * Tato třída slouží pro vytvoření agregovaného grafu Denni doba. Na ose X je čas v hodinách,
 * na ose Y je počet odevzdání domácí úlohy v tuto hodinu.
 * @author Jitka Fürbacherová
 * @version 1.0
 *
 */
public class GrafDenniDoba extends ApplicationFrame{
	private static final long serialVersionUID = 1L;
		/** <hodina, počet odevzdání tuto hodinu> */
	static TreeMap<Integer, Integer> hodinaOdevzdani = new TreeMap<Integer, Integer>();
	private String titulek;
	private String idUlohy;
	private int max = 0; //největší počet odevzdání v jeden den
	
	/**
	 * V tomto konstruktoru je dle parametrů nastaven titulek, idUlohy a výstupní
	 * adresář. Dále jsou volány metody pro zpracování předaných výsledků validací.
	 * @param title - titulek grafu
	 * @param vysledky - výsledky validací, které budou v této třídě zpracovávány
	 * @param idUlohy - idUlohy, kterou zrovna zpracovávám, pokud je nastaveno na 
	 * hodnotu null, zpracuji všechny úlohy
	 * @param rok - aktuální rok, pro který je graf tvořen
	 * @param vystupniAdresar - kořenový adresář, do kterého budou grafy tvořeny
	 */
	public GrafDenniDoba(final String title, List<VysledekValidaceSouboru> vysledky, String idUlohy, 
			int rok, String vystupniAdresar ){
		super(title);
		titulek = title;
		this.idUlohy = idUlohy;
	    VytvorMapu(vysledky);
	    NaplnMapu(vysledky);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(Konstanty.SIRKA_A, Konstanty.VYSKA_A));
        setContentPane(chartPanel);
        uloz(rok, chart, vystupniAdresar);
	}
	
	/**
	 * V této metodě je vygenerován graf a uložen.
	 * @param rok
	 * @param chart
	 * @param vystupniAdresar
	 */
	private void uloz(int rok, JFreeChart chart, String vystupniAdresar){
		File nazevSlozky = new File (vystupniAdresar);
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
        
        nazevSlozky = new File (vystupniAdresar + "/" + rok);
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
        
      	nazevSlozky = new File (vystupniAdresar + "/" + rok + Konstanty.GRAF_DENNI_DOBA_VSE_SLOZKA);
		if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
		String id = "" + rok + "_";
		if(idUlohy == null){
			id = id + "celkovy";
		}
		else{
			id = id + idUlohy;
		}
        File pieChart = new File(nazevSlozky + Konstanty.GRAF_DENNI_DOBA_SOUBOR + id +  Konstanty.KONCOVKA); 
        try{
			ChartUtilities.saveChartAsPNG(pieChart, chart, Konstanty.SIRKA_A, Konstanty.VYSKA_A);
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * V této metodě jsou postupně procházeny veškeré předané výsledky validací
	 * a naplněna mapa hodinaOdevzdani, která udává kolik úloh bylo v daný čas odevzdáváno.
	 * @param vysledky
	 */
	private void NaplnMapu(List<VysledekValidaceSouboru> vysledky){
		for(VysledekValidaceSouboru vys: vysledky){
			if(vys.getIdUlohy()!= null){
				if (idUlohy == null){
					hodinaOdevzdani.put(vys.getCasValidace().getHour(), 
							hodinaOdevzdani.get(vys.getCasValidace().getHour()) + 1);
				}
				else if(vys.getIdUlohy().compareTo(idUlohy) == 0){
					hodinaOdevzdani.put(vys.getCasValidace().getHour(), 
							hodinaOdevzdani.get(vys.getCasValidace().getHour()) + 1);
				}
			}	
		}
	}
	
	/**
	 * Vytvoří mapu hodinaOdevzdani, klíčem jsou čísla od 0 do 23 a hodnotou je vždy 0.
	 * Do této mapy bude později ukládáno kolik odevzdání se v jednolivé hodiny uskutečnilo.
	 * @param vysledky
	 */
	private void VytvorMapu(List<VysledekValidaceSouboru> vysledky){
		hodinaOdevzdani.clear();
		for(int i = 0; i<24; i++){
			hodinaOdevzdani.put(i, 0);
		}
	}	
	
	/**
	 * Vytvoří dataset.
	 * @return
	 */
	private CategoryDataset createDataset(){
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		for(Integer key: hodinaOdevzdani.keySet()){
			result.addValue(hodinaOdevzdani.get(key), "", "" + (key));
			if(max < hodinaOdevzdani.get(key) ){
				max = hodinaOdevzdani.get(key);
			}
		}
	    
		return result;
	}
	
	/**
	 * Nastaví parametry grafu.
	 * @param dataset
	 * @return
	 */
	private JFreeChart createChart(final CategoryDataset dataset){
        final JFreeChart chart = ChartFactory.createStackedBarChart(
            titulek,  									// titulek grafu
            Konstanty.GRAF_DENNI_DOBA_POPIS_X,          // domain axis label
            Konstanty.POPIS_Y,                     		// range axis label
            dataset,                    				// data
            PlotOrientation.VERTICAL,   			    // plot orientace
            false,                      			    // legenda
            true,                    				    // tooltipy
            false                  					    // url
        );
       
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(renderer);
        if(max < Konstanty.MIN_POCET_POLOZEK){
        	NumberAxis range = (NumberAxis) plot.getRangeAxis();
        	range.setTickUnit(new NumberTickUnit(1));
        }
     
        return chart;
    }
	
	@Override
	public String toString(){
		return "GrafDenniDoba [titulek=" + titulek + ", idUlohy=" + idUlohy + "]";
	}
}
