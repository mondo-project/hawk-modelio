package org.modelio.diagram.creation.wizard.diagramcreation.treeview;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * ContentProvider for the diagram creation contributors tree viewer in the diagram creation wizard dialog.
 * @author xzhang
 */
@objid ("6384e101-a6e8-47a1-a962-953e60508053")
public class ContributorTreeContentProvider implements ITreeContentProvider {
    @objid ("c5556084-0e7f-4187-904d-dc491fafa262")
    private boolean showInvalid;

    @objid ("73c680cb-d874-42ac-b978-d92330d08788")
    private ContributorCategoryModel model;

    @objid ("ee9b5c4e-0f7f-412b-9ed9-a1b424736612")
    private ModelElement context;

    @objid ("7d440f2a-cfa0-4d8c-bcdf-64d9a2c33a8e")
    public ContributorTreeContentProvider(ModelElement context, boolean showInvalidDiagram) {
        this.context = context;
        this.showInvalid = showInvalidDiagram;
    }

    @objid ("527b1dc3-a015-4604-8a79-04a632b3dbb3")
    @Override
    public void dispose() {
        //Nothing to do
    }

    @objid ("c27ef485-0975-44b3-bf97-e60303266428")
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.model = (ContributorCategoryModel) newInput;
    }

    @objid ("635b2cf2-39e8-476c-8595-caaa8c9e55dc")
    @Override
    public Object[] getElements(Object inputElement) {
        List<DiagramCategory> categories = new ArrayList<>();
        if (this.showInvalid) {
            categories = this.model.getCategories();
        } else {
            // In the not show invalid mode, doesn't display the category without valid contributors
            for (DiagramCategory category : this.model.getCategories()) {
                if (getValidContributors(category.getContributors()).size() > 0) {
                    categories.add(category);
                }
            }
        }
        return categories.toArray();
    }

    @objid ("ba603fc4-bbf5-4f55-9d22-be81947bc646")
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof DiagramCategory) {
            DiagramCategory category = (DiagramCategory) parentElement;
            List<IDiagramWizardContributor> children = new ArrayList<>();
            if (this.showInvalid) {
                children = category.getContributors();
            } else {
                // In the not show invalid mode, display only the valid contributors
                children = getValidContributors(category.getContributors());
            }
            if (children.size() > 0) return children.toArray();
        }
        return null;
    }

    @objid ("f1086bc6-40e3-40cf-b430-34b6da4b69d8")
    @Override
    public Object getParent(Object element) {
        return null;
    }

    @objid ("9c79078f-ccd5-4f78-948a-eb01450bac74")
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof DiagramCategory) {
            return true;
        }
        return false;
    }

    @objid ("5f6140a5-8176-4f71-8fa4-785aa0d5dcee")
    public void setShowInvalid(boolean showInvalid) {
        this.showInvalid = showInvalid;
    }

    /**
     * Get the valid contributors for the current context from the given list of contributors
     * @param contributors @return
     */
    @objid ("940cab91-6861-403c-95a7-2d5e2fc8a651")
    private List<IDiagramWizardContributor> getValidContributors(List<IDiagramWizardContributor> contributors) {
        List<IDiagramWizardContributor> result = new ArrayList<>();
        for(IDiagramWizardContributor contributor :  contributors){
            if(this.context != null){
                if(contributor.accept(this.context)){
                    result.add(contributor);
                }
            }
        }
        return result;
    }

}
