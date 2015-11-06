package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnCollaboration;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("b14cde30-787a-4fa2-b4b1-7616a2c34fe1")
public class BpmnProcessCollaborationDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("53c16dfc-36b2-43a4-86f4-bce9a605086b")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        BpmnProcess process = null;
        
        if (diagramContext instanceof BpmnProcess)
            process = (BpmnProcess) diagramContext;
        else {
            process = createBpmnProcess(modelFactory, diagramContext);
        }
        BpmnProcessCollaborationDiagram diagram = createBPMNDiagram(modelFactory, process, diagramName);
        if (diagram != null) {
            putNoteContent(diagram, "description", diagramDescription);
        }
        return diagram;
    }

    @objid ("a8f12f09-86be-42e5-90eb-749f968060ac")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        List<MClass> allowedMetaclasses = new ArrayList<>();
        allowedMetaclasses.add(Metamodel.getMClass(Package.class));
        allowedMetaclasses.add(Metamodel.getMClass(Class.class));
        allowedMetaclasses.add(Metamodel.getMClass(Interface.class));
        allowedMetaclasses.add(Metamodel.getMClass(Signal.class));
        allowedMetaclasses.add(Metamodel.getMClass(Actor.class));
        allowedMetaclasses.add(Metamodel.getMClass(Component.class));
        allowedMetaclasses.add(Metamodel.getMClass(Node.class));
        allowedMetaclasses.add(Metamodel.getMClass(UseCase.class));
        allowedMetaclasses.add(Metamodel.getMClass(Collaboration.class));
        allowedMetaclasses.add(Metamodel.getMClass(Operation.class));
        allowedMetaclasses.add(Metamodel.getMClass(BpmnProcess.class));
        allowedMetaclasses.add(Metamodel.getMClass(BpmnBehavior.class));
        return allowedMetaclasses;
    }

    @objid ("372b54e6-7f7f-444b-bdfb-46cf90a9d3d0")
    private BpmnProcessCollaborationDiagram createBPMNDiagram(final IModelFactory modelFactory, final BpmnProcess owner, final String diagramName) {
        BpmnProcessCollaborationDiagram diagram = modelFactory.createBpmnProcessCollaborationDiagram();
        diagram.setOrigin(owner);
        if (diagramName.equals(this.getLabel())) {                
            setElementDefaultName(diagram);
        } else {
            diagram.setName(diagramName);
        }
        return diagram;
    }

    @objid ("a43e7693-3d83-4021-8808-5368602dfe68")
    private BpmnProcess createBpmnProcess(final IModelFactory modelFactory, final ModelElement diagramContext) {
        BpmnBehavior behavior = null;
        BpmnProcess process = null;
        BpmnCollaboration collaboration = null;
        
        if (diagramContext instanceof BpmnBehavior) {
            behavior = (BpmnBehavior) diagramContext;
        } else if (diagramContext instanceof NameSpace) {
            behavior = modelFactory.createBpmnBehavior();
            behavior.setOwner((NameSpace) diagramContext);
            setElementDefaultName(behavior);
        } else if (diagramContext instanceof Operation) {
            behavior = modelFactory.createBpmnBehavior();
            behavior.setOwnerOperation((Operation) diagramContext);
            setElementDefaultName(behavior);
        } else if (diagramContext instanceof TemplateParameter) {
            behavior = modelFactory.createBpmnBehavior();
            behavior.setOwnerTemplateParameter((TemplateParameter) diagramContext);
            setElementDefaultName(behavior);
        }
        
        if (behavior != null) {
            process = modelFactory.createBpmnProcess();
            process.setOwner(behavior);
            setElementDefaultName(process);
        
            if (behavior.getRootElement(BpmnCollaboration.class).size() == 0) {
                collaboration = modelFactory.createBpmnCollaboration();
                collaboration.setOwner(behavior);
                setElementDefaultName(collaboration);
            }
        }
        return process;
    }

    @objid ("c5e549da-417a-49eb-afe2-12377314e81e")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.BpmnProcessCollaboration.Name");
    }

    @objid ("02a01669-bd24-4f94-a0f0-ecbe6566fd4e")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(BpmnProcessCollaborationDiagram.class));
    }

    @objid ("70490cdf-2713-4dac-9ace-dd3615ae157e")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.BpmnProcessCollaboration.Information");
    }

    @objid ("8b31bbfb-25c7-41a5-be72-e38fda9107df")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.BpmnProcessCollaboration.Details");
    }

}
