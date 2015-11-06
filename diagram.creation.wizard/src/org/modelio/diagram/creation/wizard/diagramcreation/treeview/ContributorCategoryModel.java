package org.modelio.diagram.creation.wizard.diagramcreation.treeview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.ContributorCategory;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;

/**
 * ContributorCategoryModel
 * It creates the collection of {@link DiagramCategory}.
 * @author xzhang
 */
@objid ("1e6dc0be-42ce-4aa6-a6c4-707ddde19c79")
public class ContributorCategoryModel {
    @objid ("6ecfdc39-96b1-49fb-9960-d89518d70792")
    private List<DiagramCategory> categories;

    @objid ("d151760b-f7b5-4c6b-accf-971100c3416b")
    public ContributorCategoryModel(Map<ContributorCategory, List<IDiagramWizardContributor>> contributorsMap) {
        super();
        this.categories = createCategories(contributorsMap);
    }

    @objid ("3a322dc8-4d3d-4531-8376-3e57387ffa17")
    public List<DiagramCategory> createCategories(Map<ContributorCategory, List<IDiagramWizardContributor>> contributorsMap) {
        this.categories = new ArrayList<>();
        for (Map.Entry<ContributorCategory, List<IDiagramWizardContributor>> entry : contributorsMap.entrySet()) {
            DiagramCategory diagramCategory = new DiagramCategory(entry.getKey(), entry.getValue());
            this.categories.add(diagramCategory);
        }
        return this.categories;
    }

    @objid ("5e880ce9-37fd-4bee-ae83-20455b0e3beb")
    public List<DiagramCategory> getCategories() {
        return this.categories;
    }

    @objid ("b534b8f2-cbc8-4573-af00-86209ff21105")
    public DiagramCategory getCategoryOfContributor(IDiagramWizardContributor contributor) {
        for (DiagramCategory category : getCategories()) {
            if (category.getContributors().contains(contributor)) 
                return category;
        }
        return null;
    }

}
