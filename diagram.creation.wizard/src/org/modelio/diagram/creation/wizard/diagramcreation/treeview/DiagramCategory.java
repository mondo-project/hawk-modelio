package org.modelio.diagram.creation.wizard.diagramcreation.treeview;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.diagram.ContributorCategory;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;

/**
 * DiagramCategory
 * Composite with name, and a list of {@link IDiagramWizardContributor}
 * @author xzhang
 */
@objid ("a078b1be-09c4-4fba-8e7c-82d41c433039")
public class DiagramCategory {
    @objid ("9ce9d132-86bd-41fb-bb79-53f3f72e2470")
    private List<IDiagramWizardContributor> contributors = new ArrayList<>();

    @objid ("c106c51d-de04-492d-aceb-4c551201b37b")
    private ContributorCategory category;

    @objid ("738e2616-2783-42cd-909a-3554975497dc")
    public DiagramCategory(ContributorCategory contributorCategory, List<IDiagramWizardContributor> contributors) {
        this.category = contributorCategory;
        this.contributors = contributors;
    }

    @objid ("e270e213-d9ad-4ed8-a125-5d3448acf90c")
    public String getName() {
        return this.category.getLabel();
    }

    @objid ("b1c55f9c-cf6b-4de2-9ad2-398d2e58d4e9")
    public List<IDiagramWizardContributor> getContributors() {
        return this.contributors;
    }

    @objid ("6b5f7e09-20f4-4024-a1cb-9bdda951e638")
    public Image getIcon() {
        return this.category.getIcon();
    }

    @objid ("f56ad34c-8ef3-4cf1-bbeb-7a04b72170a5")
    public String getType() {
        return this.category.getType();
    }

}
