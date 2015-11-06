package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("95d779e3-1da3-4a4e-afa3-46058a83155a")
public class CommunicationDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("ea2be538-10ce-4046-8045-6b3e9b68bebe")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        CommunicationDiagram diagram = null;
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        // Unless the parent element is already a CommunicationInteraction, create the CommunicationInteraction:
        CommunicationInteraction interaction = null;
        Collaboration locals = null;
        if (diagramContext instanceof CommunicationInteraction) {
            interaction = (CommunicationInteraction) diagramContext;
            if (interaction.getOwnedCollaboration().size() > 0) {
                locals = interaction.getOwnedCollaboration().get(0);
            } else {
                this.checkLocalCollaboration(modelFactory, interaction);
            }
        } else if (diagramContext instanceof Operation) {
            // create a CommunicationInteraction and its 'locals' collaboration
            interaction = modelFactory.createCommunicationInteraction();
            locals = this.checkLocalCollaboration(modelFactory, interaction);
            interaction.setOwnerOperation((Operation) diagramContext);
            setElementDefaultName(interaction);
        } else {
            // create a CommunicationInteraction and its 'locals' collaboration
            interaction = modelFactory.createCommunicationInteraction();
            locals = this.checkLocalCollaboration(modelFactory, interaction);
            interaction.setOwner((NameSpace) diagramContext);
            setElementDefaultName(interaction);
        }
        
        // Create the diagram, depending on parentElement, carry out the "smart" creation job
        if ((diagramContext instanceof Classifier) && !(diagramContext instanceof UseCase)) {
            diagram = this.smartCreateForClassifier(modelFactory,
                    interaction,
                    locals,
                    (Classifier) diagramContext,
                    diagramName);
        } else if (diagramContext instanceof Operation) {
            diagram = this.smartCreateForOperation(modelFactory,
                    interaction,
                    locals,
                    (Operation) diagramContext,
                    diagramName);
        } else {
            diagram = this.smartCreateForNameSpace(modelFactory, interaction, diagramName);
        }
        
        if (diagram != null) {
            if (diagramName.equals(this.getLabel())) {                
                setElementDefaultName(diagram);
            } else {
                diagram.setName(diagramName);
            }
            putNoteContent(diagram, "description", diagramDescription);
        }
        return diagram;
    }

    @objid ("69399480-ccc2-4849-9aaa-c2b58aecff0c")
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
        allowedMetaclasses.add(Metamodel.getMClass(CommunicationInteraction.class));
        return allowedMetaclasses;
    }

    @objid ("ec6f8179-97e9-4890-81ea-70c8db31f2f3")
    private CommunicationDiagram createCommunicationDiagram(IModelFactory modelFactory, final CommunicationInteraction diagramContext, final String diagramName) {
        // Create the Communication diagram
        CommunicationDiagram diagram;
        diagram = modelFactory.createCommunicationDiagram(diagramName, diagramContext, null);
        diagramContext.getProduct().add(diagram);
        return diagram;
    }

    /**
     * Creating a Communication diagram under a classifier will:
     * <ul>
     * <li>create a 'locals' collaboration under the classifier</li>
     * </ul>
     * @param locals
     * @param diagramName
     */
    @objid ("804d0332-eee4-45cb-b0ce-78d71861032f")
    private CommunicationDiagram smartCreateForClassifier(final IModelFactory modelFactory, final CommunicationInteraction interaction, final Collaboration locals, final Classifier parentClassifier, final String diagramName) {
        // Update model with effective context
        CommunicationInteraction diagramContext = interaction;
        
        // create the communication diagram
        CommunicationDiagram diagram = this.createCommunicationDiagram(modelFactory,
                                                                        diagramContext,
                                                                        diagramName);
        if (diagram != null) {
            // Create this pointer, create the instance:
            BindableInstance instance = modelFactory.createBindableInstance();
            if (instance != null) {
                locals.getDeclared().add(instance);
                instance.setName("this");
                instance.setBase(parentClassifier);
        
                // Create the corresponding CommunicationNode:
                CommunicationNode node = modelFactory.createCommunicationNode();
                if (node != null) {
                    interaction.getOwned().add(node);
                    node.setName("this");
                    node.setRepresented(instance);
                }
            }
        }
        return diagram;
    }

    @objid ("c31ea4ac-86d5-4987-b4a2-c25a39eff319")
    private CommunicationDiagram smartCreateForNameSpace(final IModelFactory modelFactory, final CommunicationInteraction interaction, final String diagramName) {
        // Update model with effective context
        CommunicationInteraction diagramContext = interaction;
        
        // create the communication diagram
        return this.createCommunicationDiagram(modelFactory, diagramContext, diagramName);
    }

    @objid ("93d80e2e-086b-47ab-9a2d-c8444af05281")
    private CommunicationDiagram smartCreateForOperation(final IModelFactory modelFactory, final CommunicationInteraction interaction, final Collaboration locals, final Operation parentOperation, final String diagramName) {
        // Update model with effective context
        CommunicationInteraction diagramContext = interaction;
        
        // create the communication diagram
        CommunicationDiagram diagram = this.createCommunicationDiagram(modelFactory,
                                                                        diagramContext,
                                                                        diagramName);
        if (diagram != null) {
        
            // Create the 'this' instance:
            BindableInstance instance = modelFactory.createBindableInstance();
        
            if (instance != null) {
                locals.getDeclared().add(instance);
                instance.setName("this");
                instance.setBase(parentOperation.getOwner());
        
                // Create the corresponding CommunicationNode:
                CommunicationNode node = modelFactory.createCommunicationNode();
                if (node != null) {
                    interaction.getOwned().add(node);
                    node.setName("this");
                    node.setRepresented(instance);
                }
            }
        }
        return diagram;
    }

    @objid ("a362afd0-4299-4c99-b6c2-7f43221a261f")
    private Collaboration checkLocalCollaboration(final IModelFactory modelFactory, final CommunicationInteraction interaction) {
        Collaboration locals = null;
        // Look for an existing local Collaboration
        for (Collaboration collab : interaction.getOwnedCollaboration()) {
            locals = collab;
            break;
        }
        
        if (locals == null) {
            // Create the local Collaboration
            locals = modelFactory.createCollaboration();
            interaction.getOwnedCollaboration().add(locals);
            locals.setName("locals");
        }
        return locals;
    }

    @objid ("607477b0-0fbb-455c-b582-93bb16761084")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Communication.Name");
    }

    @objid ("b31f620d-db97-4b3c-b483-5e3cd601d585")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(CommunicationDiagram.class));
    }

    @objid ("ce2af409-eba2-4b8a-b1b2-76b8d88ff611")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Communication.Information");
    }

    @objid ("e5f2c211-f980-4842-93e4-1dcc89b86e21")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Communication.Details");
    }

}
