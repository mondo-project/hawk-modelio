package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("3e4b1657-cb8f-4925-a14b-6c02e8877ffd")
public class BpmnSubProcessDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("8e3fe408-1c52-443e-a4e2-4efdda11f7f9")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        BpmnSubProcessDiagram diagram = createBpmnSubProcessDiagram(modelFactory, diagramContext, diagramName);
        if (diagram != null) {
            putNoteContent(diagram, "description", diagramDescription);
        }
        return diagram;
    }

    @objid ("cae9e31e-b8c0-424b-b917-4380b119e75e")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        List<MClass> allowedMetaclasses = new ArrayList<>();
        allowedMetaclasses.add(Metamodel.getMClass(BpmnSubProcess.class));
        return allowedMetaclasses;
    }

    @objid ("6478ea12-7a14-4e5d-bd31-06f2e6ab92ae")
    private BpmnSubProcessDiagram createBpmnSubProcessDiagram(final IModelFactory modelFactory, final ModelElement diagramContext, final String diagramName) {
        BpmnSubProcessDiagram diagram = modelFactory.createBpmnSubProcessDiagram();
        diagram.setOrigin(diagramContext);
        if (diagramName.equals(this.getLabel())) {                
            setElementDefaultName(diagram);
        } else {
            diagram.setName(diagramName);
        }
        return diagram;
    }

    @objid ("1305e5bd-bdac-45dd-8295-07c6a5489674")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.BpmnSubProcess.Name");
    }

    @objid ("d4454991-3606-4c96-9613-8ffb4b949cc3")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(BpmnSubProcessDiagram.class));
    }

    @objid ("da2ad714-7390-4933-bd66-1bb012b6e148")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.BpmnSubProcess.Information");
    }

    @objid ("d90b0a09-66a2-4ec4-b681-26758fe74122")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.BpmnSubProcess.Details");
    }

}
