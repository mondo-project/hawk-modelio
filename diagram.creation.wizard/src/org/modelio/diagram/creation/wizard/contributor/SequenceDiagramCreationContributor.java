package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
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

@objid ("79d8d35b-64bc-42db-854c-6b7c35780f67")
public class SequenceDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("12f39b07-a757-4ee0-9bae-73262df4b563")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        SequenceDiagram diagram = null;
        
        // Unless the parent element is already an Interaction, create the Interaction:
        Interaction interaction = null;
        if (diagramContext instanceof Interaction) {            
            interaction = (Interaction) diagramContext;
            diagram = smartCreateForNameSpace(modelFactory, interaction, diagramName);
        } else {
            interaction = modelFactory.createInteraction();            
            // Create the diagram, depending on parentElement, carry out the "smart" creation job
            if ((diagramContext instanceof Classifier) && !(diagramContext instanceof UseCase)) {
                interaction.setOwner((Classifier) diagramContext);
                setElementDefaultName(interaction);
                diagram = smartCreateForClassifier(modelFactory,
                        interaction,
                        (Classifier) diagramContext,
                        diagramName);
            } else if (diagramContext instanceof Operation) {
                interaction.setOwnerOperation((Operation) diagramContext);
                setElementDefaultName(interaction);
                diagram = smartCreateForOperation(modelFactory,
                        interaction,
                        (Operation) diagramContext,
                        diagramName);
            } else {
                interaction.setOwner((NameSpace) diagramContext);
                setElementDefaultName(interaction);
                diagram = smartCreateForNameSpace(modelFactory, interaction, diagramName);
            }
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

    @objid ("33bd6c71-5d66-430b-913c-2d23c6886d5c")
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
        allowedMetaclasses.add(Metamodel.getMClass(Interaction.class));
        return allowedMetaclasses;
    }

    @objid ("63f7df8b-7e7e-4873-8630-53a76c37912d")
    private SequenceDiagram createSequenceDiagram(final IModelFactory modelFactory, final String diagramName, final ModelElement diagramContext) {
        SequenceDiagram diagram = modelFactory.createSequenceDiagram();
        diagram.setName(diagramName);
        diagram.setOrigin(diagramContext);
        return diagram;
    }

    @objid ("1239d95e-2618-479d-8bc7-78a0faf08ab8")
    private SequenceDiagram smartCreateForClassifier(final IModelFactory modelFactory, final Interaction interaction, final Classifier parentClassifier, final String diagramName) {
        // Update model with effective context
        Interaction diagramContext = interaction;
        
        // create the sequence diagram
        SequenceDiagram diagram = createSequenceDiagram(modelFactory, diagramName, diagramContext);
        if (diagram != null) {
            // Create the locals Collaboration
            Collaboration locals = checkLocalCollaboration(modelFactory, interaction);
            if (locals != null) {
                // Create this pointer, create the instance:
                BindableInstance instance = modelFactory.createBindableInstance();
                if (instance != null) {
                    locals.getDeclared().add(instance);
                    instance.setName("this");
                    instance.setBase(parentClassifier);
        
                    // Create the corresponding InstanceNode:
                    Lifeline lifeline = modelFactory.createLifeline();
                    if (lifeline != null) {
                        interaction.getOwnedLine().add(lifeline);
                        lifeline.setName("this");
                        lifeline.setRepresented(instance);
                    }
                }
            }
        }
        return diagram;
    }

    @objid ("a264afa5-2b1a-4954-844c-b34df3a866ea")
    private SequenceDiagram smartCreateForNameSpace(final IModelFactory modelFactory, final Interaction interaction, final String diagramName) {
        // Update model with effective context
        Interaction diagramContext = interaction;
        
        // create the sequence diagram
        SequenceDiagram diagram = createSequenceDiagram(modelFactory, diagramName, diagramContext);
        if (diagram != null) {
            // Create the locals Collaboration:
            checkLocalCollaboration(modelFactory, interaction);
        }
        return diagram;
    }

    @objid ("a1ac8559-f314-49de-8373-b9fe33a6cd5e")
    private SequenceDiagram smartCreateForOperation(final IModelFactory modelFactory, final Interaction interaction, final Operation parentOperation, final String diagramName) {
        // Update model with effective context
        Interaction diagramContext = interaction;
        
        // create the sequence diagram
        SequenceDiagram diagram = createSequenceDiagram(modelFactory, diagramName, diagramContext);
        if (diagram != null) {
        
            // Create the locals Collaboration:
            Collaboration locals = checkLocalCollaboration(modelFactory, interaction);
            if (locals != null) {
                // Create the 'this' instance:
                BindableInstance instance = modelFactory.createBindableInstance();
        
                if (instance != null) {
                    locals.getDeclared().add(instance);
                    instance.setName("this");
                    instance.setBase(parentOperation.getOwner());
        
                    // Create the corresponding InstanceNode:
                    Lifeline lifeline = modelFactory.createLifeline();
                    if (lifeline != null) {
                        interaction.getOwnedLine().add(lifeline);
                        lifeline.setName("this");
                        lifeline.setRepresented(instance);
                    }
                }
            }
        }
        return diagram;
    }

    @objid ("75552bd2-a6e9-4327-86ad-319fac17dbf4")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Sequence.Name");
    }

    @objid ("1505abf0-1641-4bb7-a249-ab12604413ec")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(SequenceDiagram.class));
    }

    @objid ("ac91be33-f780-4054-afaa-2215114374e9")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Sequence.Information");
    }

    @objid ("e3242862-3cf8-4bcf-8eef-298e48b902e7")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Sequence.Details");
    }

    @objid ("cc092d46-1a87-471a-8bc6-595a4470a621")
    private Collaboration checkLocalCollaboration(final IModelFactory modelFactory, final Interaction interaction) {
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

}
