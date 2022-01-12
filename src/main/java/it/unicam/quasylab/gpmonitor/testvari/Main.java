package it.unicam.quasylab.gpmonitor.testvari;


import it.unicam.quasylab.gpmonitor.exporter.XMLExporter;
import it.unicam.quasylab.gpmonitor.maker.DefaultXMLParser;
import it.unicam.quasylab.gpmonitor.monitor.DataCollector;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
        DataCollector c =new DefaultXMLParser("C:\\Users\\Public\\Monitor\\GroupProjectConfig.xml").parse();
        XMLExporter exporter = new XMLExporter(c);
        c.startListening();
        while(true){
            System.out.print("> ");
            if(new Scanner(System.in).nextLine().equals("export"))
                exporter.export("C:\\Users\\Public\\Monitor\\Log.xml");
            else if(new Scanner(System.in).nextLine().equals("x"))
                c.stopListening();
        }
    }
}
