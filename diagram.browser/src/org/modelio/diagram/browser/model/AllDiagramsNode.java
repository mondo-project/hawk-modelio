package org.modelio.diagram.browser.model;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.diagram.browser.model.core.VirtualFolder;
import org.modelio.diagram.browser.plugin.DiagramBrowser;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.IModel;

@objid ("0047bcce-0d4f-10c6-842f-001ec947cd2a")
public class AllDiagramsNode extends VirtualFolder {
    @objid ("007bd9fa-1f1a-10c7-842f-001ec947cd2a")
    private final GProject project;

    @objid ("0047e6ae-0d4f-10c6-842f-001ec947cd2a")
    public AllDiagramsNode(GProject session, Project project) {
        super(project);
        this.project = session;
        setName(DiagramBrowser.I18N.getString("AllDiagrams"));
    }

    @objid ("00481674-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public Object[] getChildren(Object parentElement) {
        List<Element> diagrams = new ArrayList<>();
        
        for (AbstractDiagram diag : this.project.getSession().getModel().findByClass(AbstractDiagram.class, IModel.NODELETED)) {
            if(!diag.isShell())
                diagrams.add(diag);
        }
        return diagrams.toArray();
    }

    @objid ("00484b80-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public Object[] getElements(Object inputElement) {
        return null;
    }

    @objid ("00487f38-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public Object getParent(Object element) {
        return null;
    }

    @objid ("0048a8b4-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public boolean hasChildren(Object element) {
        return true;
    }

    @objid ("0048d1c2-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void dispose() {
        // Nothing to dispose
    }

    @objid ("0048ea40-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void inputChanged(Viewer viewer, Object oldIn, Object newIn) {
        // Nothing to do
    }

}
