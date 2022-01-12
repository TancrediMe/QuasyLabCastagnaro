package it.unicam.quasylab.gpmonitorgui;

import it.unicam.quasylab.gpmonitor.exporter.XMLExporter;
import it.unicam.quasylab.gpmonitorgui.guireadyextensions.GUIReadyNetworkEmulator;
import it.unicam.quasylab.gpmonitorgui.guireadyextensions.GuiReadyDefaultXMLParser;
import it.unicam.quasylab.gpmonitor.monitor.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Queue;

/**
 * Questa classe stabilisce una comunicazione tra GPMonitorGui e GPMonitor funzionando da ponte tra i due.
 * Riceve comandi da GPMonitorGui tramite il {@link Controller} e d esegue le opportune operazioni su un {@link DataCollector}
 */
public  class Application {
    private static DataCollector collector;
    private static GUIReadyNetworkEmulator emu=new GUIReadyNetworkEmulator();

    /**
     * Inizializza il {@link DataCollector} creando una nuova{@link Network}
     * @param type tipo della {@link Network}
     * @param Networkparameters parametri di inizializzazione della {@link Network}
     */
    public static void init(String type,String ... Networkparameters) {
        if(Networkparameters.length>=2 && Double.parseDouble(Networkparameters[0])<=Double.parseDouble(Networkparameters[1]))
            collector=new DefaultDataCollector(emu, new DefaultDataLog(), Networkparameters);
    }

    /**
     * Inizializza il {@link DataCollector} leggendo la configurazione da un file
     * @param filename percorso del file nel disco fisso.
     * @throws IOException se la lettura del file non riesce
     */
    public static void init(String filename) throws IOException {
        collector=new GuiReadyDefaultXMLParser(filename,emu).parse();
    }

    /**
     * Restituisce la coda dei {@link Packet} ricevuti dalla rete di sensori.
     */
    public static Queue<Packet> getReceivedPackets(){
        return emu.getReceived();
    }

    /**
     * Restituisce i {@link Device} memorizzati
     */
    public static Collection<Device> getDevices(){
        if(collector==null) return null;
        return collector.getDevices();
    }

    /**
     * Restituisce la struttura ad albero dei {@link Filter} memorizzati partendo dal root.
     */
    public static Filter getFilters(){
        return collector.getDataLog().getFilter();
    }

    /**
     * Restituisce {@code true} se è stata eseguita la procedura di inizializzazione {@code init()}.
     * {@code false} altrimenti.
     */
    public static boolean check(){
        return collector!=null;
    }

    /**
     * Avvia la lettura dalla rete
     */
    public static void startReadingThread(){
        collector.startListening();
    }

    /**
     * Ferma la lettura dalla rete
     */
    public static void stopReadingThread(){
        collector.stopListening();
    }

    /**
     * Esporta i dati raccolti in un file
     * @param filename percorso del file
     */
    public static void export (String filename){
        new XMLExporter(collector).export(filename);
    }

    /**
     * Aggiunge un {@link Device} al {@link DataCollector}
     * @param d {@link Device} da aggiungere
     */
    public static void add(Device d){
        collector.add(d);
    }

    /**
     * Rimuove un {@link Device} al {@link DataCollector}
     * @param d {@link Device} da rimuovere
     */
    public static void remove(Device d){
        collector.remove(d);
    }

    /**
     * Rimuove un {@link Filter} al {@link DataCollector} in base al suo ID
     * @param filterID ID del {@link Filter} da rimuovere
     */
    public static void remove(String filterID){
            System.out.println(getFilters().find(filterID)+","+filterID);
        if(getFilters()!=null) {
            if (filterID.equals(getFilters().getID()))
                collector.getDataLog().setFilter(null);
            else
                getFilters().remove(getFilters().find(filterID));
        }
    }

    /**
     * Aggiunge un {@link Filter} al {@link DataCollector} come figlio di un altro {@link Filter}
     * @param f {@link Filter} da aggiungere
     * @param parentID ID del {@link Filter} padre di f
     */
    public static void add(Filter f, String parentID){

        if(collector.getDataLog().getFilter()==null)
            collector.getDataLog().setFilter(f);
        else
            collector.getDataLog().getFilter().find(parentID).add(f);
    }
}
