package it.unicam.quasylab.gpmonitorgui.guireadyextensions;

import it.unicam.quasylab.gpmonitor.maker.DefaultXMLParser;
import it.unicam.quasylab.gpmonitor.monitor.*;

import java.io.IOException;
import java.util.Map;

import static it.unicam.quasylab.gpmonitor.monitor.FilterAggregationRule.AND;
import static it.unicam.quasylab.gpmonitor.monitor.FilterAggregationRule.OR;

/**
 * Estensione del {@link DefaultXMLParser} per costruire elementi di gpmonitor visualizzabili nell'interfaccia grafica.
 */
public class GuiReadyDefaultXMLParser extends DefaultXMLParser {
    GUIReadyNetworkEmulator e;

    public GuiReadyDefaultXMLParser(String filename, GUIReadyNetworkEmulator emulator) throws IOException {
        super(filename);
        e = emulator;
    }


    @Override
    protected Network parseNetwork() {
        return e;
    }

    @Override
    protected Filter parseFilters() {

        String[] filtersBlocks = parseMultiTag("Filter", fileContent);
        for(String filterBlock:filtersBlocks) {
            String id = parseTAG("ID", filterBlock);
            String[] compositeValue=parseTAG("filterValue", filterBlock).split(",");
            double [] filterValue=new double[compositeValue.length];
            for(int i=0;i<compositeValue.length;i++)
                filterValue[i]=Double.parseDouble(compositeValue[i]);
            Device filterDevice=null;
            for(Device d : devices)
                if(d.getName().equals(parseTAG("FilterDevice", filterBlock)))
                    filterDevice=d;
            String father="";
            try{  father=parseTAG("father", filterBlock);   }catch (Exception e){}
            String aggregationRule = parseTAG("aggregationRule", filterBlock);
            FilterAggregationRule ar;
            switch(aggregationRule){
                case "AND":ar=AND;break;
                case "OR":ar=OR;break;
                default:throw new IllegalArgumentException("REGOLA FILTRO NON RICONOSCIUTA!");
            }
            String filterRule=parseTAG("filterRule", filterBlock);
            FilterRule r;
            switch (filterRule) {
                case "HIGHER":r=FilterRule.HIGHER;break;
                case "LOWER":r=FilterRule.LOWER;break;
                case "EQUALS":r= FilterRule.EQUALS;break;
                default:throw new IllegalArgumentException("REGOLA FILTRO NON RICONOSCIUTA!");
            }
            Filter f;
            if(filterBlock.contains("TimeInterval"))
            {
                String timeBlock= parseTAG("TimeInterval", filterBlock);
                String start= parseTAG("start", timeBlock);
                String end= parseTAG("end", timeBlock);
                TimeInterval time = new DefaultTimeInterval(start,end);
                f=new GuiReadyTimedFilter(id,ar,r,filterValue,filterDevice,time);
            }
            else
                f=new GuiReadyTimedFilter(id,ar,r,filterValue,filterDevice,null);

            filtersMap.put(f,father);
        }
        Filter root = null;
        for(Map.Entry<Filter,String> e:filtersMap.entrySet()) {
            if (e.getValue().equals(""))
                root = e.getKey();
        }
        for(Map.Entry<Filter,String> e:filtersMap.entrySet()){
            if(!e.getValue().equals(""))
                for(Map.Entry<Filter,String> t:filtersMap.entrySet()){
                    if(t.getKey().getID().equals(e.getValue())){
                        t.getKey().add(e.getKey());
                    }
                }
        }
        return root;
    }

}