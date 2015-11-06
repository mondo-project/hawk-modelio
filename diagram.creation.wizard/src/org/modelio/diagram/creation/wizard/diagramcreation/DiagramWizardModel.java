package org.modelio.diagram.creation.wizard.diagramcreation;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("fc9aff98-780c-4955-af66-dc1d17406ef1")
public class DiagramWizardModel {
    @objid ("5c03561e-09d2-46d8-b526-129c1bc223da")
    private String name = "";

    @objid ("5028271e-b5e0-4e7b-a00d-a6619d36559a")
    private String description = "";

    @objid ("4ba33db8-3dd8-4365-af97-45d8c4a90ede")
    private boolean showInvalidDiagrams = false;

    @objid ("3abd44b1-9f25-4799-9a79-aaf5b8f52c70")
    private IDiagramWizardContributor selectedContributor = null;

    @objid ("ebeb72cb-e27c-450f-8cc6-4dcff16df918")
    private ModelElement context = null;

    @objid ("c7aa7897-ec4a-40f7-ad2b-66471d58ef8a")
    public String getName() {
        return this.name;
    }

    @objid ("d1c6f3c2-59fa-45f8-b8d8-f93cd4ecfcb0")
    public void setName(final String diagramName) {
        this.name = diagramName;
    }

    @objid ("58c486ab-c2a2-4a6e-981b-4d380451fc15")
    public String getDescription() {
        return this.description;
    }

    @objid ("d9d02d54-1b0a-4fbe-9f05-9fd9950c9420")
    public void setDescription(final String diagramDescription) {
        this.description = diagramDescription;
    }

    @objid ("c835f834-9968-4225-96f8-776f1402d886")
    public IDiagramWizardContributor getSelectedContributor() {
        return this.selectedContributor;
    }

    @objid ("4571141e-0fb7-40dd-b5c8-77f25d7fae2d")
    public void setSelectedContributor(final IDiagramWizardContributor selectedContributors) {
        this.selectedContributor = selectedContributors;
    }

    @objid ("47771c82-0af7-4bbe-a4e1-9e6158c295ca")
    public boolean isShowInvalidDiagram() {
        return this.showInvalidDiagrams;
    }

    @objid ("e757b9d4-7fd5-4506-b13a-62d58d434286")
    public void setShowInvalidDiagram(final boolean showInvalidDiagram) {
        this.showInvalidDiagrams = showInvalidDiagram;
    }

    @objid ("1b82b0ab-ef93-4f03-987e-114d6f594e1e")
    public ModelElement getContext() {
        return this.context;
    }

    @objid ("989d1188-997e-437b-acd5-bd5d57bda0ed")
    public void setContext(final ModelElement context) {
        this.context = context;
    }

}
