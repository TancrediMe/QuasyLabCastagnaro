package it.unicam.quasylab.gpmonitorgui.guireadyextensions;

import it.unicam.quasylab.gpmonitor.monitor.Filter;
import javafx.scene.control.TreeItem;

/**
 * Estensione di un {@link Filter} adattato per la grafica.
 * In questa estensione è possibile ottenere un albero di {@link TreeItem<String>} che
 * rappresenta la struttura ad albero a partire dal {@link Filter} corrente.
 */
public interface GuiReadyFilter extends Filter{

    /**
     * Restituisce l'albero di {@link TreeItem<String>} a partire da questo {@link Filter}.
     */
    default TreeItem<String> getTree() {
        TreeItem<String> root = getInfoAsTreeNode();
        root.getChildren().add(getSubTreeNodes());
        root.setExpanded(true);
        return root;
    }

    /**
     * Restituisce un {@link TreeItem<String>} che contenente tutte le informazioni di questo {@link Filter}
     */
    TreeItem<String> getInfoAsTreeNode();

    /**
     * Restituisce un {@link TreeItem<String>} chiamato "Sub-Filters" che ha come figli i {@link TreeItem<String>} recuoerati da tutti i figli del {@link Filter} corrente grazie al loro metodo {@code getTree()}
     * a patto che siano anch'essi istanziati come {@link GuiReadyFilter}.
     */
    default TreeItem<String> getSubTreeNodes(){
        if(getSubFilters()!=null&&!getSubFilters().isEmpty()){
            TreeItem<String> subNodes = new TreeItem<>("Sub-Filters ("+getSubFilters().size()+")");
            subNodes.setExpanded(true);
            for(Filter f:getSubFilters()){
                if(f instanceof GuiReadyFilter)
                {
                    TreeItem<String> subnode=((GuiReadyFilter) f).getTree();
                    subnode.setExpanded(true);
                    subNodes.getChildren().add(subnode);
                }
            }
            return subNodes;
        }
        return null;
    }
}
