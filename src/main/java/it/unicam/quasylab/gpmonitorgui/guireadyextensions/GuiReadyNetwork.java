package it.unicam.quasylab.gpmonitorgui.guireadyextensions;

import it.unicam.quasylab.gpmonitor.monitor.Network;
import it.unicam.quasylab.gpmonitor.monitor.Packet;

import java.util.Queue;

/**
 * Gli oggetti di questa interfaccia sono Network che possono interagire con l'interfaccia grafica
 * I valori vengono aggiunti, anziché ad una, a 2 code: la prima è la solita coda di ricezione dell'interfaccia Network
 * e la seconda è quella da cui l'interfaccia grafica legge i valori ricevuti.
 */
public interface GuiReadyNetwork extends Network {
    /**
     * Restituisce la coda parallela dei valori ricevuti per l'interfaccia grafica.
     * @return
     */
    Queue<Packet> getReceived();
}
