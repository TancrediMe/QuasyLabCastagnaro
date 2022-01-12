package it.unicam.quasylab.gpmonitorgui;

import it.unicam.quasylab.gpmonitorgui.guireadyextensions.GuiReadyFilter;
import it.unicam.quasylab.gpmonitorgui.guireadyextensions.GuiReadyTimedFilter;
import it.unicam.quasylab.gpmonitor.monitor.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * In questa classe sono definiti i metodi da eseguire in base agli eventi delle finestre video della GUI.
 */
public class Controller {
    @FXML
    ChoiceBox<String> FilterAggregationRuleField;
    @FXML
    ChoiceBox<String> FilterRuleField;
    @FXML
    public TextArea LogBox;
    @FXML
    public TextField ImportField;
    @FXML
    public TextField TypeField;
    @FXML
    public TextField ParametersField;
    @FXML
    public TextField ExportField;
    @FXML
    public TextField DevNameField;
    @FXML
    public TextField NetworkIDField;
    @FXML
    public TextField FilterValueField;
    @FXML
    public TextField FilterIDField;
    @FXML
    public ChoiceBox<String> FilterDeviceField;
    @FXML
    public TextField FilterParentIDField;
    @FXML
    public TextField StartTimeField;
    @FXML
    public TextField StopTimeField;
    @FXML
    public TextField FormatField;
    @FXML
    public TreeView<String> DevicesVew;
    @FXML
    public TreeView<String> FiltersView;
    private static int readerStatus=0;
    public static Stage configImportStage, exportDataStage, setNetworkStage, addDeviceStage, addFilterStage;

    /**
     * Click sul pulsante Import COnfig
     */
    public void importConfig(ActionEvent actionEvent) throws IOException {
        
        if(Application.check())
            return;
        configImportStage = new Stage();
        Parent root= FXMLLoader.load(new URL("https","deltasoftware.it","/Unicam/tesi/LoadFromFile.fxml"));
        configImportStage.setTitle("Import Configuration");
        configImportStage.setScene(new Scene(root));
        configImportStage.show();
    }

    /**
     * Click sul pulsante Export Data
     */
    public void exportData(ActionEvent actionEvent) throws IOException {
        
        if(!Application.check())
            return;
        exportDataStage = new Stage();
        Parent root= FXMLLoader.load(new URL("https","deltasoftware.it","/Unicam/tesi/ExportToFile.fxml"));
        exportDataStage.setTitle("Export Data");
        exportDataStage.setScene(new Scene(root));
        exportDataStage.show();
    }

    /**
     * Click sul pulsante Start
     */
    public void startReading(ActionEvent actionEvent) {
        
        if(!Application.check())
        {
            LogBox.setText("Initialize the Network!");
            return;
        }
        if(Application.getDevices().isEmpty())
        {
            LogBox.setText("Set at least 1 device!");
            return;
        }
        else
            LogBox.setText("");

        if(readerStatus==0){
            Application.startReadingThread();
            readerStatus=1;
        }
        else{
            LogBox.setText("Can't start Network!\n Please, restart this application");
        }


    }

    /**
     * Click sul pulsante Stop
     * @param actionEvent
     */
    public void stopReading(ActionEvent actionEvent) {
        
        if (Application.check())
            Application.stopReadingThread();
    }

    /**
     * Click sul pulsante Set Network
     */
    public void setNetwork(ActionEvent actionEvent) throws IOException {
        
        if(Application.check())
            return;
        setNetworkStage = new Stage();
        Parent root= FXMLLoader.load(new URL("https","deltasoftware.it","/Unicam/tesi/SetNetworkForm.fxml"));
        setNetworkStage.setTitle("Set Network");
        setNetworkStage.setScene(new Scene(root));
        setNetworkStage.show();
    }

    /**
     * Click sul pulsante Add Device
     */
    public void addDevice(ActionEvent actionEvent) throws IOException {
        
        if(!Application.check())
            return;
        addDeviceStage = new Stage();
        Parent root= FXMLLoader.load(new URL("https","deltasoftware.it","/Unicam/tesi/AddDeviceForm.fxml"));
        addDeviceStage.setTitle("Add Device");
        addDeviceStage.setScene(new Scene(root));
        addDeviceStage.show();
    }

    /**
     * Click sul pulsante Remove Device
     */
    public void removeDevice(ActionEvent actionEvent) {
        
        if(!Application.check())
            return;
        Collection<Device> removeList=new HashSet<>();
        for(TreeItem<String> t: DevicesVew.getSelectionModel().getSelectedItems()) {
            for (Device d : Objects.requireNonNull(Application.getDevices()))
                if (d.getName().equals(t.getValue().replace("Name: ", "")))
                    removeList.add(d);
        }
        for(Device d:removeList)
            Application.remove(d);
        refresh(actionEvent);
    }

    /**
     * Click sul pulsante Remove Filter
     */
    public void removeFilter(ActionEvent actionEvent) {
        
        if(!Application.check())
            return;
        for(TreeItem<String>t : FiltersView.getSelectionModel().getSelectedItems()){
            Application.remove(t.getValue());
        }
        refresh(actionEvent);
    }

    /**
     * Click sul pulsante Add Filter
     */
    public void addFilter(ActionEvent actionEvent) throws IOException {
        
        if(!Application.check())
            return;
        addFilterStage = new Stage();
        Parent root= FXMLLoader.load(new URL("https","deltasoftware.it","/Unicam/tesi/AddFilterForm.fxml"));
        addFilterStage.setTitle("Add Filter");
        addFilterStage.setScene(new Scene(root));
        addFilterStage.show();
    }

    /**
     * Click sul pulsante Cancel nella finestra di esportazione
     */
    public void cancelOperationExportData(ActionEvent actionEvent) {
        
        exportDataStage.close();
    }

    /**
     * Click sul pulsante Cancel nella finestra di importazione
     */
    public void cancelOperationImportConfig(ActionEvent actionEvent) {
        
        configImportStage.close();
    }

    /**
     * Click sul pulsante OK nella finestra di impostazione della rete
     */
    public void initializeNetwork(ActionEvent actionEvent) {
        
        if(!Application.check())
        {
            Application.init(TypeField.getText(),ParametersField.getText().split(","));
            setNetworkStage.close();
        }
    }

    /**
     * Click sul pulsante Cancel nella finestra di impostazione della rete
     * @param actionEvent
     */
    public void cancelOperationNetworkAdd(ActionEvent actionEvent) {
        
        setNetworkStage.close();
    }

    /**
     * Click sul pulsante OK nella finestra di esportazione
     * @param actionEvent
     */
    public void dataExport(ActionEvent actionEvent) {
        
        if(Application.check())
        {
            Application.export(ExportField.getText());
            exportDataStage.close();
        }
    }

    /**
     * Click sul pulsante OK nella finestra di importazione
     */
    public void configImport(ActionEvent actionEvent) throws IOException {
        
       if(!Application.check())
       {
           Application.init(ImportField.getText());
           configImportStage.close();
       }
    }

    /**
     * Click sul pulsante Refresh ALL
     */
    public void refresh(ActionEvent actionEvent) {
        if(!Application.check())
            return;
        DevicesVew.setRoot(null);
        FiltersView.setRoot(null);
        FiltersView.refresh();
        DevicesVew.refresh();
        DevicesVew.setShowRoot(true);
        if(Application.getDevices()!=null&&!Application.getDevices().isEmpty()){
        TreeItem<String> rootDevices= new TreeItem<>("All Devices");
        DevicesVew.setShowRoot(true);
        DevicesVew.setRoot(rootDevices);

        ArrayList<TreeItem<String>> devices = new ArrayList<>();

        for (Device d : Objects.requireNonNull(Application.getDevices()))
        {
            TreeItem<String> devname=(new TreeItem<>("Name: "+d.getName()));
            TreeItem<String> devNetId=new TreeItem<>("NetworkID: "+d.getNetworkID());
            devname.getChildren().add(devNetId);
            devices.add(devname);
            devname.setExpanded(true);
        }
        rootDevices.getChildren().addAll(devices);
        rootDevices.setExpanded(true);
        }
        FiltersView.setShowRoot(true);
        if(Application.getFilters()!=null&& Application.getFilters() instanceof GuiReadyFilter)
        FiltersView.setRoot(((GuiReadyFilter) Application.getFilters()).getTree());

        while (Application.getReceivedPackets().peek()!=null){
            Packet p = Application.getReceivedPackets().poll();
            LogBox.setText(p.getId()+" sends: "+Arrays.toString(p.getValue())+"\n"+LogBox.getText());
        }
    }

    /**
     * Click sul pulsante OK nella finestra Add Device
     */
    public void addDeviceOK(ActionEvent actionEvent) {
        
        if(Application.check())
        {
            Application.add(new DefaultDevice(DevNameField.getText(),NetworkIDField.getText()));
            addDeviceStage.close();
        }

    }

    /**
     * Click sul pulsante Cancel nella finestra Add Device
     */
    public void cancelOperationAddDevice(ActionEvent actionEvent) {
        
        addDeviceStage.close();
    }

    /**
     * Click sul pulsante OK nella finestra Add Filter
     */
    public void addFilterOK(ActionEvent actionEvent) {
        
        if(!Application.check())
            return;
        FilterAggregationRule agg=null;
        FilterRule r=null;
        switch(FilterAggregationRuleField.getSelectionModel().getSelectedItem())
        {
            case "AND":agg=FilterAggregationRule.AND; break;
            case "OR":agg=FilterAggregationRule.OR; break;
        }
        switch(FilterRuleField.getSelectionModel().getSelectedItem()){
            case "HIGHER":r=FilterRule.HIGHER;break;
            case "LOWER":r=FilterRule.LOWER;break;
            case "EQUALS":r=FilterRule.EQUALS;break;
        }

        String[] split= FilterValueField.getText().split(",");
        double[] value= new double[split.length];
        for(int i=0;i< value.length;i++)
            value[i]=Double.parseDouble(split[i]);
        Device dev=null;
        for(Device d: Objects.requireNonNull(Application.getDevices()))
            if(d.getName().equals(FilterDeviceField.getSelectionModel().getSelectedItem()))
                dev=d;
            if(r!=null&&agg!=null&&dev!=null)
            {
                TimeInterval timeInterval=null;
                try{
                    timeInterval=new DefaultTimeInterval(dateToLong(StartTimeField.getText(),FormatField.getText()),dateToLong(StopTimeField.getText(),FormatField.getText()));
                }catch (Exception ignored){}
                Application.add(new GuiReadyTimedFilter(FilterIDField.getText(),agg,r,value,dev,timeInterval), FilterParentIDField.getText());
            }
        addFilterStage.close();
    }

    /**
     * Click sul pulsante Cancel nella finestra Add Filter
     */
    public void cancelOperationAddFilter(ActionEvent actionEvent) {
        addFilterStage.close();
    }

    /**
     * Movimento del mouse nella finestra Add Filter
     * @param mouseEvent
     */
    public void loadProperties(MouseEvent mouseEvent) {
        
     if(FilterRuleField.getItems().isEmpty() && FilterAggregationRuleField.getItems().isEmpty())
     {
         for(Device d: Objects.requireNonNull(Application.getDevices()))
             FilterDeviceField.getItems().add(d.getName());
         FilterRuleField.getItems().add("HIGHER");
         FilterRuleField.getItems().add("LOWER");
         FilterRuleField.getItems().add("EQUALS");
         FilterAggregationRuleField.getItems().add("AND");
         FilterAggregationRuleField.getItems().add("OR");
     }
    }

    /**
     * Converte una data dal formato stringa al formato UnixTimestamp
     * @param date data da convertire
     * @param format formato della data
     * @return Unix Timestamp
     * @throws ParseException Se il formato non viene riconosciuto o la data non rispetta il formato
     */
    private static long dateToLong(String date, String format) throws ParseException {
        GregorianCalendar cal= new GregorianCalendar();
        cal.setTime(new SimpleDateFormat(format).parse(date));
        return cal.getTimeInMillis();
    }

}
