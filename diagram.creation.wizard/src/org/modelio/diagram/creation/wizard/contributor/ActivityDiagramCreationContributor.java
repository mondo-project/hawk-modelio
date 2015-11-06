package org.modelio.diagram.creation.wizard.contributor;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
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
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("c28c9c8b-26df-40a1-a215-ec7a838f3b50")
public class ActivityDiagramCreationContributor extends AbstractDiagramCreationContributor {
    @objid ("0e69cc19-074b-48f1-802a-365268cbc72d")
    private ActivityDiagram smartCreateForNameSpace(final Activity activity, final String diagramName) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        ActivityDiagram diagram = createActivityDiagram(modelFactory, diagramName, activity);
        if (diagram != null) {
            checkLocalCollaboration(modelFactory, activity);
        }
        return diagram;
    }

    @objid ("f9a2c429-1bda-4044-b16b-3bf062ff2db5")
    private ActivityDiagram smartCreateForOperation(final Activity activity, final Operation parentOperation, final String diagramName) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        ActivityDiagram diagram = createActivityDiagram(modelFactory, diagramName, activity);
        if (diagram != null) {
            // Create the locals Collaboration
            Collaboration locals = checkLocalCollaboration(modelFactory, activity);
            if (locals != null) {
                // Create the instance
                BindableInstance instance = modelFactory.createBindableInstance();
                locals.getDeclared().add(instance);
                instance.setName("this");
                instance.setBase(parentOperation.getOwner());
        
                // Create the corresponding InstanceNode:
                InstanceNode instanceNode = modelFactory.createInstanceNode();
                if (instanceNode != null) {
                    activity.getOwnedNode().add(instanceNode);
                    instanceNode.setName("this");
                    instanceNode.setRepresented(instance);
                }
            }
        
            // Create Activity parameters:
            List<Parameter> paramsList = parentOperation.getIO();
            BehaviorParameter behaviorParameter = null;
            ActivityParameterNode parameterNode = null;
            String parameterName;
        
            // IOParameters...
            for (Parameter parameter : paramsList) {
                // Create the BehaviorParameter:
                behaviorParameter = modelFactory.createBehaviorParameter();
                activity.getParameter().add(behaviorParameter);
                parameterName = parameter.getName();
        
                if (behaviorParameter != null) {
                    behaviorParameter.setName(parameterName);
                    behaviorParameter.setMultiplicityMin(parameter.getMultiplicityMin());
                    behaviorParameter.setMultiplicityMax(parameter.getMultiplicityMax());
                    behaviorParameter.setParameterPassing(parameter.getParameterPassing());
                    behaviorParameter.setTypeConstraint(parameter.getTypeConstraint());
                    behaviorParameter.setDefaultValue(parameter.getDefaultValue());
                    behaviorParameter.setMapped(parameter);
                    behaviorParameter.setType(parameter.getType());
        
                    // Create the ParameterNode:
                    parameterNode = modelFactory.createActivityParameterNode();
        
                    if (parameterNode != null) {
                        activity.getOwnedNode().add(parameterNode);
                        parameterNode.setRepresentedRealParameter(behaviorParameter);
                        parameterNode.setName(parameterName);
                    }
                }
            }
        
            // return parameter
            Parameter returnParameter = parentOperation.getReturn();
        
            if (returnParameter != null) {
                // Create the BehaviorParameter:
                behaviorParameter = modelFactory.createBehaviorParameter();
                activity.getParameter().add(behaviorParameter);
                parameterName = returnParameter.getName();
        
                if (behaviorParameter != null) {
                    behaviorParameter.setName(parameterName);
                    behaviorParameter.setMultiplicityMin(returnParameter.getMultiplicityMin());
                    behaviorParameter.setMultiplicityMax(returnParameter.getMultiplicityMax());
                    behaviorParameter.setParameterPassing(returnParameter.getParameterPassing());
                    behaviorParameter.setTypeConstraint(returnParameter.getTypeConstraint());
                    behaviorParameter.setDefaultValue(returnParameter.getDefaultValue());
                    behaviorParameter.setMapped(returnParameter);
                    behaviorParameter.setType(returnParameter.getType());
        
                    // Create the ParameterNode:     
                    parameterNode = modelFactory.createActivityParameterNode();
                    activity.getOwnedNode().add(parameterNode);
                    if (parameterNode != null) {
                        parameterNode.setRepresentedRealParameter(behaviorParameter);
                        parameterNode.setName(parameterName);
                    }
                }
            }
        }
        return diagram;
    }

    @objid ("37b01bcc-3fc6-422a-9cda-6bf969cb0f82")
    private ActivityDiagram smartCreateForClassifier(final Activity activity, final NameSpace parentClassifier, final String diagramName) {
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        ActivityDiagram diagram = createActivityDiagram(modelFactory, diagramName, activity);
        if (diagram != null) {
            // Create the locals Collaboration
            Collaboration locals = checkLocalCollaboration(modelFactory, activity);
            if (locals != null) {
                // Create the instance:
                BindableInstance instance = modelFactory.createBindableInstance();
                locals.getDeclared().add(instance);
                instance.setName("this");
                instance.setBase(parentClassifier);
        
                // Create the corresponding InstanceNode:
                InstanceNode instanceNode = modelFactory.createInstanceNode();
                if (instanceNode != null) {
                    activity.getOwnedNode().add(instanceNode);
                    instanceNode.setName("this");
                    instanceNode.setRepresented(instance);
                }
            }
        }
        return diagram;
    }

    @objid ("a4c31f6c-379c-4422-bc14-597a7a1e4669")
    @Override
    public AbstractDiagram actionPerformed(final ModelElement diagramContext, final String diagramName, final String diagramDescription) {
        if (diagramContext == null && this.projectService == null || this.projectService.getSession() == null) {
            return null;
        }
        IModelFactory modelFactory = this.mmServices.getModelFactory();
        ActivityDiagram diagram = null;
        
        // Unless the parent element is already an Activity, create the Activity:
        Activity activity = null;
        if (diagramContext instanceof Activity) {
            activity = (Activity) diagramContext;
            diagram = smartCreateForNameSpace(activity, diagramName);
        } else {
            activity = modelFactory.createActivity();
            // Create the diagram, depending on parentElement, carry out the "smart" creation job
            if ((diagramContext instanceof Classifier) && !(diagramContext instanceof UseCase)) {
                activity.setOwner((Classifier) diagramContext);
                setElementDefaultName(activity);
                diagram = smartCreateForClassifier(activity, (Classifier) diagramContext, diagramName);
            } else if (diagramContext instanceof Operation) {
                activity.setOwnerOperation((Operation) diagramContext);
                setElementDefaultName(activity);
                diagram = smartCreateForOperation(activity, (Operation) diagramContext, diagramName);
            } else {
                activity.setOwner((NameSpace) diagramContext);
                setElementDefaultName(activity);
                diagram = smartCreateForNameSpace(activity, diagramName);
            }
        }        
        
        if (diagram != null) {
            if (diagramName.equals(this.getLabel())) {                
                setElementDefaultName(diagram);
            } else {
                diagram.setName(diagramName);
            }
            putNoteContent(diagram,"description", diagramDescription);
        }
        return diagram;
    }

    @objid ("a251bc7c-9395-4e1d-b440-1de941605f63")
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
        allowedMetaclasses.add(Metamodel.getMClass(Activity.class));
        return allowedMetaclasses;
    }

    @objid ("6bbe5555-01ad-4e5f-9145-6ecbf905c795")
    private ActivityDiagram createActivityDiagram(final IModelFactory modelFactory, final String diagramName, final Activity activity) {
        // Create the Activity diagram:
        ActivityDiagram diagram = modelFactory.createActivityDiagram(diagramName, activity);
        activity.getProduct().add(diagram);
        if (diagram != null) {
            diagram.setIsVertical(false);
        }
        return diagram;
    }

    @objid ("a5827623-b2ee-43e0-956c-3f7a4412bfbd")
    @Override
    public String getLabel() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Activity.Name");
    }

    @objid ("f4d2aed7-590d-4761-9667-cc709fad8a6e")
    @Override
    public Image getIcon() {
        return MetamodelImageService.getIcon(Metamodel.getMClass(ActivityDiagram.class));
    }

    @objid ("d086291b-41dc-4037-9c6b-72fa05c660d4")
    @Override
    public String getInformation() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Activity.Information");
    }

    @objid ("57ce73e0-ed21-4707-8221-a4d93979f8ce")
    @Override
    public String getDetails() {
        return DiagramCreationWizard.I18N.getString("CreationWizard.Activity.Details");
    }

    @objid ("ccdc993f-adde-41ba-8eb6-e8d64fda87d4")
    protected Collaboration checkLocalCollaboration(IModelFactory modelFactory, final Activity interaction) {
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
