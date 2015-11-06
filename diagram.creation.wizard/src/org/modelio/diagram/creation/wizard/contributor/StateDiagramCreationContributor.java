package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
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
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("3988a614-bd95-4ad5-8577-f1a7a9ded361")
public class StateDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("d1ff3ed3-cfa1-4ae6-ba4b-084942419f44")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        StateMachineDiagram stateDiagram = null;
        
        // Create the StateMachine:
        final StateMachine stateMachine;
        
        if (diagramContext instanceof StateMachine) {
            stateMachine = (StateMachine) diagramContext;
        } else if (diagramContext instanceof Operation) {
            stateMachine = modelFactory.createStateMachine();
            stateMachine.setOwnerOperation((Operation) diagramContext);
            setElementDefaultName(stateMachine);
        } else {
            stateMachine = modelFactory.createStateMachine();
            stateMachine.setOwner((NameSpace) diagramContext);
            setElementDefaultName(stateMachine);
        }
        
        // Create the state machine diagram:
        stateDiagram = createStateDiagram(modelFactory, diagramName, stateMachine);
        
        if (stateDiagram != null) {
            if (diagramName.equals(this.getLabel())) {                
                setElementDefaultName(stateDiagram);
            } else {
                stateDiagram.setName(diagramName);
            }
            putNoteContent(stateDiagram, "description", diagramDescription);
        }
        return stateDiagram;
    }

    @objid ("739b7eba-6884-405f-9f9b-bdb05fc365df")
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
        allowedMetaclasses.add(Metamodel.getMClass(StateMachine.class));
        return allowedMetaclasses;
    }

    @objid ("0ca58be9-9018-4877-90ba-c29162407b60")
    private StateMachineDiagram createStateDiagram(final IModelFactory modelFactory, final String diagramName, final ModelElement diagramContext) {
        // Create the Sequence diagram
        StateMachineDiagram stateDiagram = modelFactory.createStateMachineDiagram(diagramName,
                diagramContext,
                null);
        return stateDiagram;
    }

    @objid ("cb5992b6-b7c5-4863-baf1-5082b20cffc0")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.State.Name");
    }

    @objid ("5a59a23d-5500-4601-bc7f-d38e1b4f48c1")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(StateMachineDiagram.class));
    }

    @objid ("8c0cea20-ffac-4431-8347-d9c524cda1ba")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.State.Information");
    }

    @objid ("f568091f-c6de-49cb-8608-b59a96ddb47a")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.State.Details");
    }

}
