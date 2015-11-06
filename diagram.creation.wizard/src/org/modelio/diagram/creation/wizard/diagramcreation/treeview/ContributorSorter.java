package org.modelio.diagram.creation.wizard.diagramcreation.treeview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * ViewerSorter for the diagram creation contributors list.
 * The valid contributors will be displayed first and then the invalid ones.
 * And they are also sorted by names.
 * @author xzhang
 */
@objid ("ab3402e9-c2e1-46ca-8428-fbe161dc6bb2")
public class ContributorSorter extends ViewerSorter {
    @objid ("301e3c46-3449-442d-8d4d-5a137126ff1b")
    private ModelElement context;

    @objid ("bb6a521e-570f-47bb-84b1-93cb916f75d9")
    public ContributorSorter(ModelElement context) {
        this.context = context;
    }

    @objid ("95037c9b-170a-4830-b0de-18c5ba5eea95")
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        // Category UML is always display as the first
        if (e1 instanceof DiagramCategory) {
            if (((DiagramCategory) e1).getType().equals("UML")) {
                return -1;
            }
        }
        if (e2 instanceof DiagramCategory) {
            if (((DiagramCategory) e2).getType().equals("UML")) {
                return 1;
            }
        }
        if (e1 instanceof DiagramCategory && e2 instanceof DiagramCategory) {
            return ((DiagramCategory) e1).getName().compareToIgnoreCase(((DiagramCategory) e2).getName());
        }
        if (e1 instanceof IDiagramWizardContributor && e2 instanceof IDiagramWizardContributor) {
            IDiagramWizardContributor contributor1 = (IDiagramWizardContributor) e1;
            IDiagramWizardContributor contributor2 = (IDiagramWizardContributor) e2;
            // Valid contributors are always the first 
            if (contributor1.accept(this.context) && !contributor2.accept(this.context)) {
                return -1;
            } else if (!contributor1.accept(this.context) && contributor2.accept(this.context)) {
                return 1;
            }
            // Sort by contributor names
            return contributor1.getLabel().compareToIgnoreCase(contributor2.getLabel());
        }
        return 0;
    }

}
