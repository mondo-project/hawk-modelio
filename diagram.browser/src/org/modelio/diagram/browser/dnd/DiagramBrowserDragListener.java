package org.modelio.diagram.browser.dnd;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.modelio.diagram.browser.model.core.DiagramRef;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.DiagramSet;

@objid ("6b017796-1ad8-48ba-987a-f2123a42fd5a")
public class DiagramBrowserDragListener implements DragSourceListener {
    @objid ("c0eb6878-8fda-472a-9230-614e967bc48f")
    public static final String NULL = "NULL";

    @objid ("7fc0bb59-70a8-466a-bd0c-b4cc39730974")
    public static final String ABSTRACT_DIAGRAM = "ABSTRACT_DIAGRAM";

    @objid ("65728688-9fb9-4811-bdf6-38099257342a")
    public static final String DIAGRAM_SET = "ABSTRACT_DIAGRAM";

    @objid ("17f93d8a-f166-4c7d-a5d5-34c413cbd911")
    private final TreeViewer viewer;

    @objid ("14c4dc81-12c5-49cd-9a46-15ed59341e0e")
    public DiagramBrowserDragListener(TreeViewer viewer) {
        this.viewer = viewer;
    }

    @objid ("f759c1a3-2650-4b51-a25c-3c166cb05444")
    @Override
    public void dragStart(DragSourceEvent event) {
        LocalSelectionTransfer.getTransfer().setSelection(this.viewer.getSelection());
    }

    @objid ("058e1a5f-bc24-41d9-9a7b-19ad599ce14a")
    @Override
    public void dragSetData(DragSourceEvent event) {
        if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType))           
            event.data=LocalSelectionTransfer.getTransfer().getSelection();
    }

    @objid ("ad527feb-3cfc-4fa6-b120-5b1e383b771c")
    @Override
    public void dragFinished(DragSourceEvent event) {
    }

}
