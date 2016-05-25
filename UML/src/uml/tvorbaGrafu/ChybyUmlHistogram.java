/**
 * 
 */
package uml.tvorbaGrafu;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import uml.Konstanty;
import uml.nacitani.VysledekValidaceUML;

/**
 * Vytvoří Histogram chyb pro studenta jehož osobní číslo obdrží v konstruktoru.
 * @author Jitka Fürbacherová
 * @version 1.0
 *
 */
public class ChybyUmlHistogram extends ApplicationFrame{
	private static final long serialVersionUID = 1L;
	/*mapa, kterou vytvořím pro kažého studenta, klíčem je id chyby a hodnotou číslo, 
	  které udává kolikrát tento student tuto chybu udělá */
	 private TreeMap<String, Integer> map = new TreeMap<String, Integer>();
	 private String titulek;
	
	 /**
	  * Zavolá tvorbu a naplnění mapy.
	  * Nastaví velikost plátna, na které je vykreslován graf.
	  * Ukládá vytvořené grafy na cílove místo
	  * @param title
	  * @param vysledkyUML
	  * @param idUlohy
	  * @param nazevSlozky
	  */
	public ChybyUmlHistogram(String title, List<VysledekValidaceUML> vysledkyUML,
			String idUlohy, File nazevSlozky){
		super(title);
		titulek = title;
	    VytvorMapu(vysledkyUML, idUlohy);
	    NaplnMapu(vysledkyUML, idUlohy);
	    
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        int width = 1550; 
        int height = 900;
        chartPanel.setPreferredSize(new Dimension(width,height));
        setContentPane(chartPanel);
        
        if(!nazevSlozky.exists()){
			nazevSlozky.mkdir();
		}
        
        File pieChart = new File( nazevSlozky + "/" + title + ".png"); 
        try{
			ChartUtilities.saveChartAsPNG(pieChart , chart , width , height);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * V této metodě vytvořím mapu danému studentovi, ve které bude klíčem id chyby a hodnotou počet, 
	 * kolikrát student tuto chybu udělal. Tento počet je při tvoření mapy nastaven na 0.
	 * @param vysledkyUML
	 * @param idulohy
	 * @return
	 */
	public TreeMap<String, Integer> VytvorMapu(List<VysledekValidaceUML> vysledkyUML, String idulohy){
		for(VysledekValidaceUML vys: vysledkyUML){
			if(vys.getIdUlohy().compareTo(idulohy)== 0){
				//vytvoří mapu, ve které bude hodnota 0
				map.put(vys.getChybaUML().substring(0, Konstanty.pocetZnakuChyby), 0);		
			}
		}
		return map;
			
	}
	
	/**
	 * V této metodě naplňuji mapu daného studenta. Procházím jednotlivé odevzdané
	 * soubory a když narazím na soubor daného studenta, zvýším patřičnou hodnotu
	 * v mapě odpovidající dané chybě.
	 * @param vysledkyUML
	 * @param idUlohy
	 * @return
	 */
	public Map<String, Integer> NaplnMapu(List<VysledekValidaceUML> vysledkyUML, String idUlohy){
	for(VysledekValidaceUML vys: vysledkyUML){
		if(vys.getIdUlohy().compareTo(idUlohy) == 0){
			map.put(vys.getChybaUML().substring(0, Konstanty.pocetZnakuChyby),
					map.get(vys.getChybaUML().substring(0, Konstanty.pocetZnakuChyby)) + 1);
		}
	}	
		return map;
	}
	
	
   /**
    * Vytvoří vzorek dat.
    * @return
    */
    private CategoryDataset createDataset(){
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        int pocet = map.size();
        for (String key: map.keySet()){
        	if((pocet < Konstanty.MAX_POCET) || (map.get(key) >= Konstanty.MIN_HODNOTA)){
        		result.addValue(map.get(key), "", key.substring(0, Konstanty.pocetZnakuChyby));
        	}
        }
        
        return result;
    }
    
    /**
     * Vytvoří graf dle parametrů.
     * @param dataset
     * @return
     */
    private JFreeChart createChart(final CategoryDataset dataset){

        final JFreeChart chart = ChartFactory.createStackedBarChart(
            titulek,				     // titulek chyby
            "Identifikátor chyby",       // domain axis label
            "Počet",                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // the plot orientation
            false,                       // legenda
            true,                        // tooltipy
            false                        // url
        );
       
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(renderer);
        plot.setFixedLegendItems(createLegendItems());
        CategoryAxis domainAxis = plot.getDomainAxis(); 
        // nastavi sklon popisku osy X
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        return chart;
        
    }

    /**
     * Vytvoří legendu
     * @return result
     */
    private LegendItemCollection createLegendItems(){
       LegendItemCollection result = new LegendItemCollection();
       LegendItem item1 = new LegendItem("Výskyt chyb", new Color(0x22, 0x22, 0xFF));
       result.add(item1);
       return result;
    }
}
