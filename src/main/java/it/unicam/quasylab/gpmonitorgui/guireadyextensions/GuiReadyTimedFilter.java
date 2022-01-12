package it.unicam.quasylab.gpmonitorgui.guireadyextensions;

import it.unicam.quasylab.gpmonitor.monitor.*;
import javafx.scene.control.TreeItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Estensione di {@link TimedFilter} secondo il contratto stabilito da {@link GuiReadyFilter}.
 */
public class GuiReadyTimedFilter extends TimedFilter implements GuiReadyFilter{
    public GuiReadyTimedFilter(String ID, FilterAggregationRule aggregationRule, FilterRule filterRule, double[] filtervalue, Device filterDevice, TimeInterval interval) {
        super(ID, aggregationRule, filterRule, filtervalue, filterDevice, interval);

    }

    @Override
    public TreeItem<String> getInfoAsTreeNode() {
        TreeItem<String> root = new TreeItem<>( getID());
        root.getChildren().add(new TreeItem<>("Value: "+ Arrays.toString(getValue())));
        root.getChildren().add(new TreeItem<>("Device: "+getDevice().getName()));
        root.getChildren().add(new TreeItem<>("Rule: "+getRule().name()));
        root.getChildren().add(new TreeItem<>("Aggregation Rule: "+getFilterAggregationRule().name()));
        if(getTimeInterval()!=null){
            TreeItem<String> timeInterval = new TreeItem<>("Time Interval");
            timeInterval.getChildren().add(new TreeItem<>("Start: "+longToDate(getTimeInterval().getstart())));
            timeInterval.getChildren().add(new TreeItem<>("Stop: "+longToDate(getTimeInterval().getStop())));
            root.getChildren().add(timeInterval);
            timeInterval.setExpanded(true);
        }
        return root;
    }

    private String longToDate(long value){
        GregorianCalendar time= new GregorianCalendar();
        time.setTimeInMillis(value);
        String result = time.get(Calendar.DAY_OF_MONTH)+"/"+
                time.get(Calendar.MONTH)+"/"+
                time.get(Calendar.YEAR)+" "+
                time.get(Calendar.HOUR_OF_DAY)+":"+
                time.get(Calendar.MINUTE)+":"+
                time.get(Calendar.SECOND)+","+
                time.get(Calendar.MILLISECOND);
        return result;
    }
}
