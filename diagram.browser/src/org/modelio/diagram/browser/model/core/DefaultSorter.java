package org.modelio.diagram.browser.model.core;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.uml.infrastructure.Element;

@objid ("0034ee6e-0d4f-10c6-842f-001ec947cd2a")
public class DefaultSorter extends ViewerSorter {
    /**
     * Sorting rules:
     * 
     * <li>by adapter type: DiagramSet > VirtualFolder > Element > DiagramRef<br>
     * 
     * <li>alphabetic ordering when adapter types are equal (for diagrams the string key which is used is made of the metaclass +
     * the name in order to sort diagrams by type)
     */
    @objid ("0034f756-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        int e1TypeWeight = getTypeWeight(e1);
        int e2TypeWeight = getTypeWeight(e2);
        
        if (e1TypeWeight == e2TypeWeight) {
            return super.compare(viewer, getSortingKey(e1), getSortingKey(e2));
        } else {
            return super.compare(viewer, e1TypeWeight, e2TypeWeight);
        }
    }

    @objid ("00352c26-0d4f-10c6-842f-001ec947cd2a")
    private String getSortingKey(Object o) {
        if (o instanceof DiagramSet) {
            return ((DiagramSet) o).getName();
        }
        if (o instanceof VirtualFolder) {
            return ((VirtualFolder) o).getName();
        }
        if (o instanceof Element) {
            return ((Element) o).getName();
        }
        if (o instanceof DiagramRef) {
            return ((DiagramRef) o).getReferencedDiagram().getName();
        }
        return "";
    }

    @objid ("00354a4e-0d4f-10c6-842f-001ec947cd2a")
    private int getTypeWeight(Object o) {
        if (o instanceof DiagramSet) {
            return 1;
        }
        if (o instanceof VirtualFolder) {
            return 2;
        }
        if (o instanceof Element) {
            return 3;
        }
        if (o instanceof DiagramRef) {
            return 4;
        }
        return 0;
    }

}
