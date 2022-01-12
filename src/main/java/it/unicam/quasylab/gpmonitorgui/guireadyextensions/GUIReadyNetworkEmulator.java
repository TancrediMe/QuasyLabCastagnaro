package it.unicam.quasylab.gpmonitorgui.guireadyextensions;

import it.unicam.quasylab.gpmonitor.monitor.Packet;
import it.unicam.quasylab.gpmonitor.sensornetworks.NetworkEmulator;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Estensione del {@link NetworkEmulator} per permettere anche all'interfaccia grafica di leggere i valori inviati dalla rete.
 */
public class GUIReadyNetworkEmulator extends NetworkEmulator implements GuiReadyNetwork{
    Queue<Packet> received=new LinkedList<>();
    @Override
    public Packet receive() {
        Packet e = super.receive();
        received.add(e);
        return e;
    }

    /**
     * Restituisce la coda dei pacchetti ricevuti dalla rete.
     */
    public Queue<Packet> getReceived(){
        return received;
    }
}
