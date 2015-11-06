/*
 * Copyright 2013 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */  
                                    

package org.modelio.model.browser.views.treeview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.gproject.data.project.FragmentType;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.BusinessRuleContainer;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.GenericAnalystContainer;
import org.modelio.metamodel.analyst.GenericAnalystElement;
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.analyst.GoalContainer;
import org.modelio.metamodel.analyst.PropertyContainer;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.activities.BpmnCallActivity;
import org.modelio.metamodel.bpmn.activities.BpmnComplexBehaviorDefinition;
import org.modelio.metamodel.bpmn.activities.BpmnMultiInstanceLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnTask;
import org.modelio.metamodel.bpmn.bpmnService.BpmnEndPoint;
import org.modelio.metamodel.bpmn.bpmnService.BpmnInterface;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.events.BpmnCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.events.BpmnThrowEvent;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnGateway;
import org.modelio.metamodel.bpmn.objects.BpmnItemAwareElement;
import org.modelio.metamodel.bpmn.objects.BpmnItemDefinition;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnCollaboration;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.resources.BpmnResource;
import org.modelio.metamodel.bpmn.resources.BpmnResourceRole;
import org.modelio.metamodel.bpmn.rootElements.BpmnArtifact;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptCallEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptChangeEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptTimeEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallOperationAction;
import org.modelio.metamodel.uml.behavior.activityModel.CentralBufferNode;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.ConditionalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ControlFlow;
import org.modelio.metamodel.uml.behavior.activityModel.DataStoreNode;
import org.modelio.metamodel.uml.behavior.activityModel.DecisionMergeNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionRegion;
import org.modelio.metamodel.uml.behavior.activityModel.FlowFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ForkJoinNode;
import org.modelio.metamodel.uml.behavior.activityModel.InitialNode;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.InterruptibleActivityRegion;
import org.modelio.metamodel.uml.behavior.activityModel.LoopNode;
import org.modelio.metamodel.uml.behavior.activityModel.MessageFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectFlow;
import org.modelio.metamodel.uml.behavior.activityModel.OpaqueAction;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.metamodel.uml.behavior.activityModel.SendSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.StructuredActivityNode;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.OpaqueBehavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Region;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateVertex;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.infrastructure.matrix.MatrixDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IModelChangeSupport;
import org.modelio.vcore.session.api.model.change.IStatusChangeEvent;
import org.modelio.vcore.session.api.model.change.IStatusChangeListener;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

/**
 * Default content provider for the model browser.
 * <p>
 * When setting a GProject or an ICoreSession as input, it registers a model change listener refreshing the viewer.
 * </p>
 * <p>
 * The {@link #getElements(Object)} method returns the tree roots, from the "local roots" if defined, or from the GProject itself.
 * </p>
 */
@objid ("001a0cb6-dd16-1fab-b27f-001ec947cd2a")
class BrowserContentProvider implements IModelChangeListener, IStatusChangeListener, ITreeContentProvider {
    @objid ("34f7a7d2-835e-4ce2-84a8-ba4fdd130c7c")
    private boolean showMdaModel = true;

    @objid ("ff36377b-9fb0-4b83-8951-1fb16435876c")
    private boolean showProjects = true;

    @objid ("ed6adba5-bd14-49eb-9a03-214653ee8b34")
    private boolean showAnalystModel = true;

    @objid ("eece2eec-d3d3-4fdd-a843-b854fe957d88")
     volatile boolean isEditorActive = false;

    @objid ("4c01ca83-d9b3-4add-a1f1-cc43ac424d68")
    private static ModelBrowserVisitor visitor = new ModelBrowserVisitor();

    @objid ("b3ebb255-adb0-4f9a-801d-2e245b31f21f")
    private volatile ViewRefresher viewRefresher;

    @objid ("97aaacfb-d8fb-4fd5-8b6e-fa9cc2cec994")
    private GProject openedProject;

    @objid ("aa020c33-b0a1-484f-a267-124f1dc2aaa1")
    private List<Object> localRoots = new ArrayList<>();

    @objid ("f0a0adaa-e5c1-4228-a62a-16f1cb8429e5")
    private Viewer viewer;

    @objid ("58fc2cc8-bb7a-4f71-b79b-3ef3dc4cd878")
    @Override
    public void inputChanged(final Viewer currentViewer, final Object oldInput, final Object newInput) {
        this.viewer = currentViewer;
        
        // Unregister model change listener on the old input
        if (oldInput != null && oldInput instanceof GProject) {
            ICoreSession session = ((GProject) oldInput).getSession();
            if (session != null) {
                session.getModelChangeSupport().removeModelChangeListener(this);
                session.getModelChangeSupport().removeStatusChangeListener(this);
            }
        } else if (oldInput != null && oldInput instanceof ICoreSession) {
            ICoreSession session = (ICoreSession) oldInput;
            session.getModelChangeSupport().removeModelChangeListener(this);
            session.getModelChangeSupport().removeStatusChangeListener(this);
        }
        
        // Register model change listener on the new input
        if (newInput != null && newInput instanceof GProject) {
            this.openedProject = (GProject) newInput;
        
            IModelChangeSupport changeSupport = ((GProject) newInput).getSession().getModelChangeSupport();
            changeSupport.addModelChangeListener(this);
            changeSupport.addStatusChangeListener(this);
        } else if (newInput != null && newInput instanceof ICoreSession) {
            this.openedProject = null;
        
            IModelChangeSupport changeSupport = ((ICoreSession) newInput).getModelChangeSupport();
            changeSupport.addModelChangeListener(this);
            changeSupport.addStatusChangeListener(this);
        } else {
            this.openedProject = null;
        }
    }

    @objid ("a1b68b72-805e-4178-9036-92739784b671")
    @Override
    public void dispose() {
        // Nothing to do.
    }

    @objid ("0ade0bbe-2c18-463f-a215-0027baa332e7")
    @Override
    public Object[] getElements(final Object parent) {
        // If any local roots are declared, return them
        if (this.localRoots != null && this.localRoots.size() > 0) {
            return this.localRoots.toArray();
        }
        
        // No local root, get all fragments
        if (parent != null && parent instanceof GProject) {
            return getFragments((GProject) parent).toArray();                
        }
        
        // Nothing to return yet
        return new Object[0];
    }

    @objid ("20da7dc0-bc98-4a7c-86ae-6a1e0b74943f")
    @Override
    public Object getParent(final Object child) {
        if (child instanceof IProjectFragment) {
            return this.viewer.getInput();
        } else if (child instanceof BpmnFlowElement && ((BpmnFlowElement) child).getLane().size() > 0) {
            return ((BpmnFlowElement) child).getLane().get(0);
        } else if (child instanceof BpmnBoundaryEvent && ((BpmnBoundaryEvent) child).getAttachedToRef() != null) {
            return ((BpmnBoundaryEvent) child).getAttachedToRef();
        } else if (child instanceof LinkContainer) {
            return ((LinkContainer)child).getElement();
        } else if (child instanceof ArchiveContainer) {
            return ((ArchiveContainer)child).getElement();
        } else if (child instanceof MObject) {
            SmObjectImpl element = (SmObjectImpl) child;
            SmObjectImpl owner = element.getCompositionOwner();
            if (owner != null) {
                if (! this.showProjects && isProject(owner)) {
                    return getParent(owner);
                } else {
                    // look for right container
                    if (visitor.getArchivedChildren(owner).contains(element))
                            return new ArchiveContainer(owner, -1);
                    if (visitor.getLinkChildren(owner).contains(element))
                        return new LinkContainer(owner, -1);
                    
                    return owner;
                }
            } else if (this.openedProject != null) {
                return this.openedProject.getFragment(element);
            }
        }
        return null;
    }

    @objid ("7b52bff5-96e4-435b-9c8a-1f5547d4f12a")
    @Override
    public Object[] getChildren(final Object parent) {
        if (parent instanceof IProjectFragment) {
            IProjectFragment fragment = (IProjectFragment) parent;
            return getFragmentRoots(fragment).toArray();
        } else if (parent instanceof MObject) {
            return visitor.getChildren((MObject) parent, true, true).toArray();
        } else if (parent instanceof LinkContainer) {
            return visitor.getLinks((LinkContainer) parent);
        } else if (parent instanceof ArchiveContainer) {
            return visitor.getArchivedVersions((ArchiveContainer) parent);
        }
        return new Object[0];
    }

    @objid ("3be7ab25-36d6-44e6-8493-f179516df4da")
    @Override
    public boolean hasChildren(final Object parent) {
        if (parent instanceof IProjectFragment) {
            IProjectFragment fragment = (IProjectFragment) parent;
            return !getFragmentRoots(fragment).isEmpty();
        } else if (parent instanceof MObject) {
            return visitor.hasChildren((MObject) parent, true, true);
        }
        return true;
    }

    @objid ("feca0f54-e66d-4c6e-9846-14006730c436")
    @Override
    public void modelChanged(final IModelChangeEvent event) {
        scheduleRefresh();
    }

    @objid ("2f458ffd-68ce-4717-b170-1520af1b8b77")
    public List<Object> getLocalRoots() {
        return this.localRoots;
    }

    @objid ("af68056b-fa2e-49e7-b700-10d30878ff9d")
    public void setLocalRoots(List<Object> localRoots) {
        this.localRoots = localRoots;
        if (this.viewer != null && !this.viewer.getControl().isDisposed()) {
            this.viewer.refresh();
        }
    }

    @objid ("dc85e8c3-5bcb-4c60-bcfa-73f06a077f3e")
    public synchronized void doRefreshViewer() {
        if (this.viewer != null && !this.viewer.getControl().isDisposed() && !this.isEditorActive) {
            this.viewer.refresh();
        }
        this.viewRefresher = null;
    }

    @objid ("d50727aa-2e6b-4dae-9c5c-d5ca0d558d92")
    private synchronized void scheduleRefresh() {
        if (this.viewRefresher == null) {
            this.viewRefresher = new ViewRefresher();
            this.viewer.getControl().getDisplay().asyncExec(this.viewRefresher);
        }
    }

    @objid ("9e6c3fff-b1b6-4d8e-8653-350e6bcd1c3e")
    @Override
    public void statusChanged(IStatusChangeEvent event) {
        scheduleRefresh();
    }

    @objid ("52b87d3b-cfbe-427f-a444-4a21417f6374")
    private List<Object> getFragmentRoots(IProjectFragment fragment) {
        List<Object> ret = new ArrayList<>();
        
        for (MObject root : fragment.getRoots()) {
            if (root instanceof AnalystProject) {
                if (this.showAnalystModel) {
                    ret.add(root);
                }
            } else if (root instanceof Project) {
                if (this.showProjects) {
                    ret.add(root);
                } else {
                    // Take children of those projects... 
                    ret.addAll(Arrays.asList(getChildren(root)));
                }
            } else if (root instanceof ModuleComponent) {
                if (this.showMdaModel) ret.add(root);
            } else {                    
                ret.add(root);
            }
        }
        return ret;
    }

    @objid ("2795e64c-28ef-43f2-b82a-eea53b2f0694")
    private List<IProjectFragment> getFragments(GProject project) {
        List<IProjectFragment> fragments = new ArrayList<>();
        for (IProjectFragment iProjectFragment : project.getFragments()) {
            if (!isShowMdaModel()) {
                // Ignore MDA fragments
                if (iProjectFragment.getType() == FragmentType.MDA) {
                    continue;
                } else if (iProjectFragment.getType() == FragmentType.RAMC) {
                    boolean ignore = false;
                    for (MObject root : iProjectFragment.getRoots()) {
                        // Ignore fragments containing module components
                        if (root instanceof ModuleComponent) {
                            ignore = true;
                        }
                    }
                    if (ignore) {
                        continue;
                    }
                }
            }
            fragments.add(iProjectFragment);
        }
        Collections.sort(fragments, new FragmentComparator());
        return fragments;
    }

    @objid ("58f56eac-0069-4f1f-945f-564885dffcb1")
    public boolean isShowMdaModel() {
        return this.showMdaModel;
    }

    @objid ("345701fe-e1b5-40d4-aaa3-97b89b4069a9")
    public void setShowMdaModel(boolean showMdaModel) {
        this.showMdaModel = showMdaModel;
    }

    @objid ("2bde233e-e49b-4045-89b2-9def9436c902")
    public boolean isShowProjects() {
        return this.showProjects;
    }

    @objid ("5027eed8-af31-4e43-a910-280bd2c3f0ce")
    public void setShowProjects(boolean showProjects) {
        this.showProjects = showProjects;
    }

    @objid ("2fa495ed-4490-44d0-ba8f-a97f019863c5")
    private boolean isProject(MObject element) {
        return element instanceof Project || element instanceof AnalystProject;
    }

    @objid ("7fbd3379-fbdf-4605-81dd-f75acf09e585")
    public boolean isShowAnalystModel() {
        return this.showAnalystModel;
    }

    @objid ("ec940e1a-8b17-48be-a572-715c333e1210")
    public void setShowAnalystModel(boolean showAnalystModel) {
        this.showAnalystModel  = showAnalystModel;
    }

    /**
     * Visitor used to get tree node children.
     * <p>
     * <h3>Implementation note:</h3>
     * If {@link #findLinksChildren} or {@link #findArchivedVersionChildren} is <code>true</code>,
     * the implementation should use {@link #addResults(List)} so that children tree nodes
     * are linked to the container. <br/>
     * This should help the tree view not shrinking sometimes.
     * <p>
     * In the other case {@link #addResults(List)} should be called, to avoid creating adapters
     * for nothing.
     */
    @objid ("440712f4-8e83-4fbe-ada0-31de148452e4")
    private static class ModelBrowserVisitor extends DefaultModelVisitor {
        @objid ("ef200fd5-b87a-4110-a65c-7f9dcd21607d")
        private boolean findUmlChildren;

        @objid ("5a481f83-4d8d-4271-97f0-5b080878f0fd")
        private boolean findScopeChildren;

        @objid ("c22eb469-c6e9-410a-b6ff-af3d49a65124")
        private boolean findLinksChildren;

        @objid ("1535fe78-e9dd-4395-b6bb-5205d8055aab")
        private boolean findArchivedVersionChildren;

        @objid ("73644ebd-ff3b-4bab-8af0-aa35e37d0421")
        private ArrayList<Object> result;

        @objid ("e4477a8f-d66d-4a24-9a30-eb87bb7b5c0e")
        public ModelBrowserVisitor() {
            // nothing to do
        }

        @objid ("d45f3143-2662-4cbf-8701-c1df3efcc9a6")
        public List<Object> getChildren(final MObject parent, boolean findUml, boolean findScope) {
            List<Object> umlChildren = getChildren(parent, findUml, findScope, false, false);
            List<Object> auxChildren = getLinkChildren(parent);
            List<Object> archiveChildren = getArchivedChildren(parent);
            
            List<Object> ret = new ArrayList<>(umlChildren.size() + 2);
            ret.addAll(umlChildren);
            
            if (! auxChildren.isEmpty())
                ret.add(new LinkContainer(parent, auxChildren.size()));
            
            if (! archiveChildren.isEmpty())
                ret.add(new ArchiveContainer(parent, archiveChildren.size()));
            return ret;
        }

        @objid ("0cbc32a5-acc0-4c92-9643-b72c893433d5")
        public boolean hasChildren(final MObject parent, boolean findUml, boolean findScope) {
            // TODO: optimize?
            return getChildren(parent, findUml, findScope).size() > 0;
        }

        /**
         * Get the children to display into the given element.
         * @param parent the element where children will be looked for
         * @return The children to display when expanding the tree node.
         */
        @objid ("89370d4c-0e5d-4b5c-aa1a-3c4b5404848c")
        private List<Object> getChildren(MObject parent, boolean findUml, boolean findScope, boolean findLinks, boolean findArchivedVersion) {
            this.findUmlChildren = findUml;
            this.findScopeChildren = findScope;
            this.findLinksChildren = findLinks;
            this.findArchivedVersionChildren = findArchivedVersion; 
            this.result = new ArrayList<>();
            parent.accept(this);
            return this.result;
        }

        @objid ("b3164446-3f7b-4307-95e5-a511a60586dc")
        public Object[] getLinks(LinkContainer parent) {
            return getChildren(parent.getElement(), false, false, true, false).toArray();
        }

        @objid ("f93d8eed-28fe-44ff-b1ca-819d07abe7bb")
        @Override
        public Object visitDictionary(Dictionary theDictionary) {
            if (this.findScopeChildren) {
                // Dictionary
                for (Dictionary dictionary : theDictionary.getOwnedDictionary()) {
                    addResult(dictionary);
                }
            
                // Term
                for (Term term : theDictionary.getOwnedTerm()) {
                    addResult(term);
                }
            }
            return super.visitDictionary(theDictionary);
        }

        @objid ("648d0e0c-0589-42b0-82ab-96f75c0b3058")
        @Override
        public Object visitPropertyContainer(PropertyContainer thePropertyContainer) {
            List<ModelElement> ramcElement = new ArrayList<>();
            List<ModelElement> element = new ArrayList<>();
            
            if (this.findScopeChildren) {
                // PropertySet
                for (PropertyTableDefinition propertySet : thePropertyContainer.getDefinedTable()) {
                    if (propertySet != null) {
                        MStatus status = propertySet.getStatus();
                        if (status.isModifiable())
                            element.add(propertySet);
                        else
                            ramcElement.add(propertySet);
                    }
                }
            
            
                for (ModelElement el : element) {
                    addResult(el);
                }
            
                for (ModelElement el : ramcElement) {
                    addResult(el);
                }
            
                // PropertyType
                addResults(thePropertyContainer.getDefinedType());
            }
            return super.visitPropertyContainer(thePropertyContainer);
        }

        @objid ("f53ee63e-b18e-4a6d-a35e-4ff6c882c49b")
        @Override
        public Object visitPropertyTableDefinition(PropertyTableDefinition thePropertySet) {
            if (this.findScopeChildren) {
                // Property
                for (PropertyDefinition property : thePropertySet.getOwned()) {
                    addResult(property);
                }
            }
            return super.visitPropertyTableDefinition(thePropertySet);
        }

        @objid ("42d27894-7f7e-435d-b288-782c3d98d5bf")
        @Override
        public Object visitRequirementContainer(RequirementContainer theRequirementContainer) {
            if (this.findScopeChildren) {
                // RequirementContainer
                for (RequirementContainer requirementContainer : theRequirementContainer.getOwnedContainer()) {
                    addResult(requirementContainer);
                }
            
                // Requirement
                for (Requirement requirement : theRequirementContainer.getOwnedRequirement()) {
                    addResult(requirement);
                }
            }
            return super.visitRequirementContainer(theRequirementContainer);
        }

        @objid ("72548cc4-61ff-4ceb-9551-583d66385b1d")
        @Override
        public Object visitActivity(Activity theActivity) {
            if (this.findUmlChildren) {
                // ActivityPartition
                addResults(theActivity.getOwnedGroup(ActivityPartition.class));
            
                // InteruptibleActivityRegion
                addResults(theActivity.getOwnedGroup(InterruptibleActivityRegion.class));
            
                // LoopNode
                addResults(theActivity.getOwnedNode(LoopNode.class));
            
                // ConditionalNode
                addResults(theActivity.getOwnedNode(ConditionalNode.class));
            
                // StructuredActivityNode
                final List<StructuredActivityNode> structuredActivityNode = theActivity.getOwnedNode(StructuredActivityNode.class);
                for (StructuredActivityNode node: structuredActivityNode) {
                    if (node instanceof ConditionalNode || node instanceof LoopNode || node instanceof ExpansionRegion) {
                        continue;
                    }
                    addResult(node);
                }
            
                // OpaqueAction
                addResults(theActivity.getOwnedNode(OpaqueAction.class));
            
                // AcceptSignalAction
                addResults(theActivity.getOwnedNode(AcceptSignalAction.class));
            
                // SendSignalAction
                addResults(theActivity.getOwnedNode(SendSignalAction.class));
            
                // AcceptCallEventAction
                addResults(theActivity.getOwnedNode(AcceptCallEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theActivity.getOwnedNode(AcceptTimeEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theActivity.getOwnedNode(AcceptChangeEventAction.class));
            
                // CallOperationAction
                addResults(theActivity.getOwnedNode(CallOperationAction.class));
            
                // CallBehaviorAction
                addResults(theActivity.getOwnedNode(CallBehaviorAction.class));
            
                // ForkJoinNode
                addResults(theActivity.getOwnedNode(ForkJoinNode.class));
            
                // DecisionMergeNode
                addResults(theActivity.getOwnedNode(DecisionMergeNode.class));
            
                // InitialNode
                addResults(theActivity.getOwnedNode(InitialNode.class));
            
                // FlowFinalNode
                addResults(theActivity.getOwnedNode(FlowFinalNode.class));
            
                // ActivityFinalNode
                addResults(theActivity.getOwnedNode(ActivityFinalNode.class));
            
                // InstanceNode
                addResults(theActivity.getOwnedNode(InstanceNode.class));
            
                // CentralBufferNode
                final List<CentralBufferNode> centralBufferNodes = theActivity.getOwnedNode(CentralBufferNode.class);
                for (CentralBufferNode node: centralBufferNodes) {
                    if (!(node instanceof DataStoreNode)) {                        
                        addResult(node);
                    }
                }
            
                // DataStoreNode
                addResults(theActivity.getOwnedNode(DataStoreNode.class));
            
                // ActivityParameterNode
                addResults(theActivity.getOwnedNode(ActivityParameterNode.class));
            
                // InputPin
                addResults(theActivity.getOwnedNode(InputPin.class));
            
                // OutputPin
                addResults(theActivity.getOwnedNode(OutputPin.class));
            
                // ExpansionRegion
                addResults(theActivity.getOwnedNode(ExpansionRegion.class));
            
            }
            return super.visitActivity(theActivity);
        }

        @objid ("eaf46395-c253-45fb-b57b-7d4f133f70b7")
        @Override
        public Object visitActivityAction(ActivityAction theActivityAction) {
            if (this.findUmlChildren) {
                // InputPin
                addResults(theActivityAction.getInput());
            
                // OutputPin
                addResults(theActivityAction.getOutput());
            
                // ExceptionHandler
                addResults(theActivityAction.getHandler());
            
            }
            return super.visitActivityAction(theActivityAction);
        }

        @objid ("63b8f579-8534-4542-adcc-965dc8e66701")
        @Override
        public Object visitActivityNode(ActivityNode theActivityNode) {
            if (this.findUmlChildren) {
                // ObjectFlow
                addResults(theActivityNode.getOutgoing(ObjectFlow.class));
            
                // MessageFlow
                addResults(theActivityNode.getOutgoing(MessageFlow.class));
            
                // ControlFlow
                addResults(theActivityNode.getOutgoing(ControlFlow.class));
            
            }
            return super.visitActivityNode(theActivityNode);
        }

        @objid ("e93f33a1-4758-43ea-980e-7c756536d3ab")
        @Override
        public Object visitActivityPartition(ActivityPartition theActivityPartition) {
            if (this.findUmlChildren) {
                // ControlFlow
                addResults(theActivityPartition.getSubPartition());
            
                // LoopNode
                addResults(theActivityPartition.getContainedNode(LoopNode.class));
            
                // ConditionalNode
                addResults(theActivityPartition.getContainedNode(ConditionalNode.class));
            
                // StructuredActivityNode
                final List<StructuredActivityNode> structuredActivityNodes = theActivityPartition.getContainedNode(StructuredActivityNode.class);
                for (StructuredActivityNode node: structuredActivityNodes) {
                    if (!(node instanceof ConditionalNode) && !(node instanceof LoopNode)) {                        
                        addResult(node);
                    }
                }
            
                // OpaqueAction
                addResults(theActivityPartition.getContainedNode(OpaqueAction.class));
            
                // AcceptSignalAction
                addResults(theActivityPartition.getContainedNode(AcceptSignalAction.class));
            
                // SendSignalAction
                addResults(theActivityPartition.getContainedNode(SendSignalAction.class));
            
                // AcceptCallEventAction
                addResults(theActivityPartition.getContainedNode(AcceptCallEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theActivityPartition.getContainedNode(AcceptTimeEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theActivityPartition.getContainedNode(AcceptChangeEventAction.class));
            
                // CallOperationAction
                addResults(theActivityPartition.getContainedNode(CallOperationAction.class));
            
                // CallBehaviorAction
                addResults(theActivityPartition.getContainedNode(CallBehaviorAction.class));
            
                // ForkJoinNode
                addResults(theActivityPartition.getContainedNode(ForkJoinNode.class));
            
                // DecisionMergeNode
                addResults(theActivityPartition.getContainedNode(DecisionMergeNode.class));
            
                // InitialNode
                addResults(theActivityPartition.getContainedNode(InitialNode.class));
            
                // FlowFinalNode
                addResults(theActivityPartition.getContainedNode(FlowFinalNode.class));
            
                // ActivityFinalNode
                addResults(theActivityPartition.getContainedNode(ActivityFinalNode.class));
            
                // InstanceNode
                addResults(theActivityPartition.getContainedNode(InstanceNode.class));
            
                // CentralBufferNode
                final List<CentralBufferNode> centralBufferNodes = theActivityPartition.getContainedNode(CentralBufferNode.class);
                for (CentralBufferNode node: centralBufferNodes) {
                    if (!(node instanceof DataStoreNode)) {                        
                        addResult(node);
                    }
                }
            
                // DataStoreNode
                addResults(theActivityPartition.getContainedNode(DataStoreNode.class));
            
                // ActivityParameterNode
                addResults(theActivityPartition.getContainedNode(ActivityParameterNode.class));
            
                // InputPin
                addResults(theActivityPartition.getContainedNode(InputPin.class));
            
                // OutputPin
                addResults(theActivityPartition.getContainedNode(OutputPin.class));
            
                // MessageFlow
                addResults(theActivityPartition.getOutgoing());
            }
            return super.visitActivityPartition(theActivityPartition);
        }

        @objid ("4f9a4bed-96be-4e06-a8fc-cc71372d89d0")
        @Override
        public Object visitAnalystProject(AnalystProject theAnalystProject) {
            if (this.findScopeChildren) {
                // Requirements
                addResults(theAnalystProject.getRequirementRoot());
            
                // Business Rules
                addResults(theAnalystProject.getBusinessRuleRoot());
            
                // Goals
                addResults(theAnalystProject.getGoalRoot());
                
                // Generic items
                addResults(theAnalystProject.getGenericRoot());
            
                // Dictionaries
                addResults(theAnalystProject.getDictionaryRoot());
            
                // PropertySet
                final PropertyContainer propertyRoot = theAnalystProject.getPropertyRoot();
                if (propertyRoot != null) {
                    addResult(propertyRoot);
                }
            }
            return super.visitAnalystProject(theAnalystProject);
        }

        @objid ("06abab5b-709a-4f53-84c9-bd945605e8f6")
        @Override
        public Object visitArtifact(Artifact theArtifact) {
            if (this.findUmlChildren) {
                // Manifestation
                addResults(theArtifact.getUtilized());
            }
            return super.visitArtifact(theArtifact);
        }

        @objid ("55599373-f7be-4662-90bb-72ee35f50f40")
        @Override
        public Object visitAssociationEnd(AssociationEnd theAssociationEnd) {
            if (this.findUmlChildren) {
                addResults(theAssociationEnd.getQualifier());
            
                addResult(theAssociationEnd.getAssociation());
            }
            return super.visitAssociationEnd(theAssociationEnd);
        }

        @objid ("9cfa11bf-26e7-4554-87ad-5b9e8e4249be")
        @Override
        public Object visitBehavior(Behavior theBehavior) {
            if (this.findUmlChildren) {
                // BehaviorParameter
                addResults(theBehavior.getParameter());
            
                // Event
                addResults(theBehavior.getEComponent());
            
                // Collaboration
                addResults(theBehavior.getOwnedCollaboration());
            }
            return super.visitBehavior(theBehavior);
        }

        @objid ("f914aadd-4bad-4fff-98d2-bc9b9809abfe")
        @Override
        public Object visitClassifier(Classifier theClassifier) {
            if (this.findUmlChildren) {
                // Attribute
                addResults(theClassifier.getOwnedAttribute());
            
                // AssociationEnd
                for (final AssociationEnd end : theClassifier.getOwnedEnd()) {
                    if (end.isNavigable() || (end.getOpposite() != null && !end.getOpposite().isNavigable())) {
                        addResult(end);
                    }
                }
            
                // NaryAssociationEnd
                addResults(theClassifier.getOwnedNaryEnd());
            
                // Operation
                addResults(theClassifier.getOwnedOperation());
            
                // Port
                addResults(theClassifier.getInternalStructure(Port.class));
            
                // Part
                final List<BindableInstance> bindableInstances = theClassifier.getInternalStructure(BindableInstance.class);
                for (BindableInstance instance : bindableInstances) {
                    if (!(instance instanceof Port)) {                        
                        addResult(instance);
                    }
                }
            }
            
            if (this.findLinksChildren) {
                // Substitutions
                addResults(theClassifier.getSubstitued());
                // Component realizations 
                addResults(theClassifier.getRealizedComponent());
            }
            return super.visitClassifier(theClassifier);
        }

        @objid ("07f16feb-c663-4930-a34e-7ef17cc6e188")
        @Override
        public Object visitClause(Clause theClause) {
            if (this.findUmlChildren) {
                // LoopNode
                addResults(theClause.getBody(LoopNode.class));
            
                // ConditionalNode
                addResults(theClause.getBody(ConditionalNode.class));
            
                // StructuredActivityNode
                addResults(theClause.getBody(StructuredActivityNode.class));
            
                // OpaqueAction
                addResults(theClause.getBody(OpaqueAction.class));
            
                // AcceptSignalAction
                addResults(theClause.getBody(AcceptSignalAction.class));
            
                // SendSignalAction
                addResults(theClause.getBody(SendSignalAction.class));
            
                // AcceptCallEventAction
                addResults(theClause.getBody(AcceptCallEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theClause.getBody(AcceptTimeEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theClause.getBody(AcceptChangeEventAction.class));
            
                // CallOperationAction
                addResults(theClause.getBody(CallOperationAction.class));
            
                // CallBehaviorAction
                addResults(theClause.getBody(CallBehaviorAction.class));
            
                // ForkJoinNode
                addResults(theClause.getBody(ForkJoinNode.class));
            
                // DecisionMergeNode
                addResults(theClause.getBody(DecisionMergeNode.class));
            
                // InitialNode
                addResults(theClause.getBody(InitialNode.class));
            
                // FlowFinalNode
                addResults(theClause.getBody(FlowFinalNode.class));
            
                // ActivityFinalNode
                addResults(theClause.getBody(ActivityFinalNode.class));
            
                // InstanceNode
                addResults(theClause.getBody(InstanceNode.class));
            
                // CentralBufferNode
                addResults(theClause.getBody(CentralBufferNode.class));
            
                // DataStoreNode
                addResults(theClause.getBody(DataStoreNode.class));
            
                // ActivityParameterNode
                addResults(theClause.getBody(ActivityParameterNode.class));
            
                // InputPin
                addResults(theClause.getBody(InputPin.class));
            
                // OutputPin
                addResults(theClause.getBody(OutputPin.class));
            
            }
            return super.visitClause(theClause);
        }

        @objid ("bef8b506-1c91-48d9-8130-05a6760bb244")
        @Override
        public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
            if (this.findUmlChildren) {
                // Binding
                addResults(theCollaborationUse.getRoleBinding());
            }
            return super.visitCollaborationUse(theCollaborationUse);
        }

        @objid ("1a585c72-21e5-4a73-9473-12fe2d585569")
        @Override
        public Object visitCommunicationInteraction(CommunicationInteraction theCommunicationInteraction) {
            if (this.findUmlChildren) {
                // CommunicationNode
                addResults(theCommunicationInteraction.getOwned());
            }
            return super.visitCommunicationInteraction(theCommunicationInteraction);
        }

        @objid ("321bf70f-4c42-4543-930e-0b44de84ef42")
        @Override
        public Object visitCommunicationNode(CommunicationNode theCommunicationNode) {
            if (this.findUmlChildren) {
                // CommunicationChannel
                addResults(theCommunicationNode.getStarted());
            }
            return super.visitCommunicationNode(theCommunicationNode);
        }

        @objid ("2e3c73e2-3236-4998-9721-b9d815b54638")
        @Override
        public Object visitConditionalNode(ConditionalNode theConditionalNode) {
            if (this.findUmlChildren) {
                // Clause
                addResults(theConditionalNode.getOwnedClause());
            }
            return super.visitConditionalNode(theConditionalNode);
        }

        @objid ("25bf8331-5f38-46a7-815e-6c0f0db3016f")
        @Override
        public Object visitEnumeration(Enumeration theEnumeration) {
            if (this.findUmlChildren) {
                addResults(theEnumeration.getValue());
            }
            return super.visitEnumeration(theEnumeration);
        }

        @objid ("e8337077-0386-4d77-b9c8-1195a36d7fb9")
        @Override
        public Object visitInstance(Instance theInstance) {
            if (this.findUmlChildren) {
                // Port
                addResults(theInstance.getPart(Port.class));
            
                // Part
                final List<BindableInstance> bindableInstances = theInstance.getPart(BindableInstance.class);
                for (BindableInstance instance: bindableInstances) {
                    if (!(instance instanceof Port)) {                        
                        addResult(instance);
                    }
                }
            
                // AttributeLink
                addResults(theInstance.getSlot());
            
                // ConnectorEnd
                addResults(theInstance.getOwnedEnd(ConnectorEnd.class));
            
                // LinkEnd
                addResults(theInstance.getOwnedEnd(LinkEnd.class));
            
                // NaryLinkEnd
                addResults(theInstance.getOwnedNaryEnd());
            }
            return super.visitInstance(theInstance);
        }

        @objid ("160b8a74-004f-412b-96c1-d056c270f372")
        @Override
        public Object visitModelElement(ModelElement theModelElement) {
            if (this.findLinksChildren) {
                // Usage
                addResults(theModelElement.getDependsOnDependency(Usage.class));
            
                // Dependency
                addResults(theModelElement.getDependsOnDependency(Dependency.class));
                
            }
            if (this.findUmlChildren) {
                addResults(theModelElement.getProduct());
                
                // Matrix
                addResults(theModelElement.getMatrix());
            }
            return super.visitModelElement(theModelElement);
        }

        @objid ("b38e7c62-21dd-468a-9255-474f4c9dd8ff")
        @Override
        public Object visitModelTree(ModelTree theModelTree) {
            if (this.findUmlChildren) {
                // Template parameter
                addResults(theModelTree.getOwnedElement(TemplateParameter.class));
            
                // Package
                addResults(theModelTree.getOwnedElement(org.modelio.metamodel.uml.statik.Package.class));
            
                // Interface
                addResults(theModelTree.getOwnedElement(Interface.class));
            
                // Class
                addResults(theModelTree.getOwnedElement(org.modelio.metamodel.uml.statik.Class.class));
            
                // Actor
                addResults(theModelTree.getOwnedElement(Actor.class));
            
                // UseCase
                addResults(theModelTree.getOwnedElement(UseCase.class));
            
                // Signal
                addResults(theModelTree.getOwnedElement(Signal.class));
            
                // Node
                addResults(theModelTree.getOwnedElement(Node.class));
            
                // Component
                this.result.removeAll(theModelTree.getOwnedElement(Component.class));
                addResults(theModelTree.getOwnedElement(Component.class));
            
                // Artifact
                addResults(theModelTree.getOwnedElement(Artifact.class));
            
                // Collaboration
                addResults(theModelTree.getOwnedElement(Collaboration.class));
            
                // DataType
                addResults(theModelTree.getOwnedElement(DataType.class));
            
                // Enumeration
                addResults(theModelTree.getOwnedElement(Enumeration.class));
            
                // Enumeration
                addResults(theModelTree.getOwnedElement(InformationItem.class));
            }
            return super.visitModelTree(theModelTree);
        }

        @objid ("5d143330-a373-4574-9b58-9f51cf2650e8")
        @Override
        public Object visitNameSpace(NameSpace theNameSpace) {
            if (this.findLinksChildren) {
                // ElementImport
                addResults(theNameSpace.getOwnedImport());
            
                // PackageImport
                addResults(theNameSpace.getOwnedPackageImport());
            }
            
            if (this.findUmlChildren) {
                // Generalization
                addResults(theNameSpace.getParent());
            
                // InterfaceRealization
                addResults(theNameSpace.getRealized());
            
                // TemplateParameter
                addResults(theNameSpace.getTemplate());
            
                // TemplateBinding
                addResults(theNameSpace.getTemplateInstanciation());
            
                // Instance
                addResults(theNameSpace.getDeclared(Instance.class));
            
                // Port
                addResults(theNameSpace.getDeclared(Port.class));
            
                // Part
                addResults(theNameSpace.getDeclared(BindableInstance.class));
            
                // CollaborationUse
                addResults(theNameSpace.getOwnedCollaborationUse());
            
                // Activity
                addResults(theNameSpace.getOwnedBehavior(Activity.class));
            
                // OpaqueBehavior
                addResults(theNameSpace.getOwnedBehavior(OpaqueBehavior.class));
            
                // Interaction
                addResults(theNameSpace.getOwnedBehavior(Interaction.class));
            
                // CommunicationInteraction
                addResults(theNameSpace.getOwnedBehavior(CommunicationInteraction.class));
            
                // StateMachine
                addResults(theNameSpace.getOwnedBehavior(StateMachine.class));
            
                // BPMN behavior
                addResults(theNameSpace.getOwnedBehavior(BpmnBehavior.class));
            
                // DataFlow
                addResults(theNameSpace.getOwnedDataFlow());
            
                // InformationFlow
                addResults(theNameSpace.getOwnedInformationFlow());
            }
            return super.visitNameSpace(theNameSpace);
        }

        @objid ("79762b35-f155-4b76-a228-b9ef33a10f08")
        @Override
        public Object visitOperation(Operation theOperation) {
            if (this.findLinksChildren) {
                // ElementImport
                addResults(theOperation.getOwnedImport());
            
                // PackageImport
                addResults(theOperation.getOwnedPackageImport());
            }
            
            if (this.findUmlChildren) {
                // ReturnParameter
                final Parameter returnParameter = theOperation.getReturn();
                if (returnParameter != null) {
                    addResult(returnParameter);
                }
            
                // IOParameter
                addResults(theOperation.getIO());
            
                // RaisedException
                addResults(theOperation.getThrown());
            
                // TemplateParameter
                addResults(theOperation.getTemplate());
            
                // TemplateBinding
                addResults(theOperation.getTemplateInstanciation());
            
                // Collaboration
                addResults(theOperation.getExample());
            
                // CollaborationUse
                addResults(theOperation.getOwnedCollaborationUse());
            
                // Activity
                addResults(theOperation.getOwnedBehavior(Activity.class));
            
                // BPMN
                addResults(theOperation.getOwnedBehavior(BpmnBehavior.class));
            
                // OpaqueBehavior
                addResults(theOperation.getOwnedBehavior(OpaqueBehavior.class));
            
                // Interaction
                addResults(theOperation.getOwnedBehavior(Interaction.class));
            
                // CommunicationInteraction
                addResults(theOperation.getOwnedBehavior(CommunicationInteraction.class));
            
                // StateMachine
                addResults(theOperation.getOwnedBehavior(StateMachine.class));
            
            }
            return super.visitOperation(theOperation);
        }

        @objid ("4ae2db19-cb4e-4cf8-be18-07a7710d62c4")
        @Override
        public Object visitPackage(org.modelio.metamodel.uml.statik.Package thePackage) {
            if (this.findLinksChildren) {
                // PackageMerge
                addResults(thePackage.getMerge());
            }
            return super.visitPackage(thePackage);
        }

        @objid ("e7e4addd-5961-4266-8215-06536884c35f")
        @Override
        public Object visitPort(Port thePort) {
            if (this.findUmlChildren) {
                // ProvidedInterface
                addResults(thePort.getProvided());
            
                // RequiredInterface
                addResults(thePort.getRequired());
            }
            return super.visitPort(thePort);
        }

        @objid ("86183b28-09a6-403e-8b03-c9b2a4e5ad9b")
        @Override
        public Object visitProject(Project theProject) {
            if (this.findUmlChildren) {
                final org.modelio.metamodel.uml.statik.Package root = theProject.getModel();
                if (root != null) {
                    addResult(root);
                }
            }
            return super.visitProject(theProject);
        }

        @objid ("a8db4fa4-8d86-4142-b8e6-a0c4140155f9")
        @Override
        public Object visitRegion(Region theRegion) {
            if (this.findUmlChildren) {
                addResults(theRegion.getSub());
            }
            return super.visitRegion(theRegion);
        }

        @objid ("cb4e4418-efbf-47e8-b15b-1864745dd733")
        @Override
        public Object visitState(State theState) {
            if (this.findUmlChildren) {
                // Region
                addResults(theState.getOwnedRegion());
            
                // Entry and Exit Point
                addResults(theState.getEntryPoint());
                addResults(theState.getExitPoint());
            
                // InternalTransition
                addResults(theState.getInternal());
            
                // ConnectionPointReference
                addResults(theState.getConnection());
            }
            return super.visitState(theState);
        }

        @objid ("54d6f053-8b54-4fb4-bc64-7a42645de941")
        @Override
        public Object visitStateMachine(StateMachine theStateMachine) {
            if (this.findUmlChildren) {
            
                // Entry and Exit point are directly on the StateMachine
                addResults(theStateMachine.getEntryPoint());
                addResults(theStateMachine.getExitPoint());
            
                // the visible children of the StateMachine are the children of its
                // Top State
                final Region theRegion = theStateMachine.getTop();
                addResults(theRegion.getSub());
            
            }
            return super.visitStateMachine(theStateMachine);
        }

        @objid ("8cd265e1-e576-4cae-804f-eef47ca5a866")
        @Override
        public Object visitStateVertex(StateVertex theStateVertex) {
            if (this.findUmlChildren) {
                // InternalTransition
                addResults(theStateVertex.getOutGoing(Transition.class));
            
                // InternalTransition
                addResults(theStateVertex.getOutGoing(InternalTransition.class));
            }
            return super.visitStateVertex(theStateVertex);
        }

        @objid ("ee5d24c5-dbd8-4884-9c62-dbbcb86c92ca")
        @Override
        public Object visitStructuredActivityNode(StructuredActivityNode theStructuredActivityNode) {
            if (this.findUmlChildren) {
                // LoopNode
                addResults(theStructuredActivityNode.getBody(LoopNode.class));
            
                // ConditionalNode
                addResults(theStructuredActivityNode.getBody(ConditionalNode.class));
            
                // StructuredActivityNode
                addResults(theStructuredActivityNode.getBody(StructuredActivityNode.class));
            
                // OpaqueAction
                addResults(theStructuredActivityNode.getBody(OpaqueAction.class));
            
                // AcceptSignalAction
                addResults(theStructuredActivityNode.getBody(AcceptSignalAction.class));
            
                // SendSignalAction
                addResults(theStructuredActivityNode.getBody(SendSignalAction.class));
            
                // AcceptCallEventAction
                addResults(theStructuredActivityNode.getBody(AcceptCallEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theStructuredActivityNode.getBody(AcceptTimeEventAction.class));
            
                // AcceptTimeEventAction
                addResults(theStructuredActivityNode.getBody(AcceptChangeEventAction.class));
            
                // CallOperationAction
                addResults(theStructuredActivityNode.getBody(CallOperationAction.class));
            
                // CallBehaviorAction
                addResults(theStructuredActivityNode.getBody(CallBehaviorAction.class));
            
                // ForkJoinNode
                addResults(theStructuredActivityNode.getBody(ForkJoinNode.class));
            
                // DecisionMergeNode
                addResults(theStructuredActivityNode.getBody(DecisionMergeNode.class));
            
                // nitialNode
                addResults(theStructuredActivityNode.getBody(InitialNode.class));
            
                // FlowFinalNode
                addResults(theStructuredActivityNode.getBody(FlowFinalNode.class));
            
                // ActivityFinalNode
                addResults(theStructuredActivityNode.getBody(ActivityFinalNode.class));
            
                // InstanceNode
                addResults(theStructuredActivityNode.getBody(InstanceNode.class));
            
                // CentralBufferNode
                addResults(theStructuredActivityNode.getBody(CentralBufferNode.class));
            
                // DataStoreNode
                addResults(theStructuredActivityNode.getBody(DataStoreNode.class));
            
                // ActivityParameterNode
                addResults(theStructuredActivityNode.getBody(ActivityParameterNode.class));
            
                // InputPin
                addResults(theStructuredActivityNode.getBody(InputPin.class));
            
                // OutputPin
                addResults(theStructuredActivityNode.getBody(OutputPin.class));
            
                // ExpansionNode
                addResults(theStructuredActivityNode.getBody(ExpansionNode.class));
            
            }
            return super.visitStructuredActivityNode(theStructuredActivityNode);
        }

        @objid ("17efe1be-cf74-4645-a4e4-f52de0e0da3e")
        @Override
        public Object visitTemplateBinding(TemplateBinding theTemplateBinding) {
            if (this.findUmlChildren) {
                // TemlplateParameterSubstitution
                addResults(theTemplateBinding.getParameterSubstitution());
            }
            return super.visitTemplateBinding(theTemplateBinding);
        }

        @objid ("7b619e7f-9c85-40bc-b0da-63251338725c")
        @Override
        public Object visitTemplateParameter(TemplateParameter theTemplateParameter) {
            if (this.findUmlChildren) {
                // TemlplateParameterSubstitution
                final ModelElement modelElement = theTemplateParameter.getOwnedParameterElement();
                if (modelElement != null) {
                    addResult(modelElement);
                }
            }
            return super.visitTemplateParameter(theTemplateParameter);
        }

        @objid ("56d220c5-a059-4587-8626-d153f1872055")
        @Override
        public Object visitUseCase(UseCase theUseCase) {
            if (this.findUmlChildren) {
                // ExtensionPoint
                addResults(theUseCase.getOwnedExtension());
            
                addResults(theUseCase.getUsed());
            }
            return super.visitUseCase(theUseCase);
        }

        @objid ("6154d82c-1ff4-4b5a-837c-e9d101e860ce")
        @Override
        public Object visitExpansionRegion(ExpansionRegion theExpansionRegion) {
            if (this.findUmlChildren) {
                addResults(theExpansionRegion.getInputElement());
                addResults(theExpansionRegion.getOutputElement());
            }
            return super.visitExpansionRegion(theExpansionRegion);
        }

        @objid ("d29ee007-3fee-4bae-a399-5a9e50988740")
        @Override
        public Object visitBpmnBehavior(final BpmnBehavior theBpmnBehavior) {
            if (this.findUmlChildren) {
            
                // BpmnProcess
                addResults(theBpmnBehavior.getRootElement(BpmnProcess.class));
            
                // BpmnCollaboration
                addResults(theBpmnBehavior.getRootElement(BpmnCollaboration.class));
            
                // BpmnMessage
                addResults(theBpmnBehavior.getRootElement(BpmnMessage.class));
            
                // BpmntemDefinition
                addResults(theBpmnBehavior.getRootElement(BpmnItemDefinition.class));
            
                // BpmnInterface
                addResults(theBpmnBehavior.getRootElement(BpmnInterface.class));
            
                // BpmnEndPoint
                addResults(theBpmnBehavior.getRootElement(BpmnEndPoint.class));
            
                // BpmnResource
                addResults(theBpmnBehavior.getRootElement(BpmnResource.class));
            }
            return super.visitBpmnBehavior(theBpmnBehavior);
        }

        @objid ("e151ac92-0dc4-4de1-9aec-320d0ef243de")
        @Override
        public Object visitBpmnProcess(final BpmnProcess theBpmnProcess) {
            if (this.findUmlChildren) {
            
                // LaneSet
                for (final BpmnLaneSet laneset : theBpmnProcess.getLaneSet()) {
                    addResults(laneset.getLane());
                }
            
                // SubProcess
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnSubProcess.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnCallActivity
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnCallActivity.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnStartEvent
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnStartEvent.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnCallActivity
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnTask.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnBpmnGateway
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnGateway.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnntermediateCatchEven
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnIntermediateCatchEvent.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // // BpmnBoundaryEvent
                // for (IBpmnFlowElement element :
                // theBpmnProcess.getFlowElement(IBpmnBoundaryEvent.class)) {
                // if (element.getLane().size() == 0) {
                // addResult(element);
                // }
                // }
            
                // BpmnThrowEvent
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnThrowEvent.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnItemAwareElemen
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnItemAwareElement.class)) {
                    if (element.getLane().size() == 0) {
                        addResult(element);
                    }
                }
            
                // BpmnArtifact
                addResults(theBpmnProcess.getArtifact(BpmnArtifact.class));
            
                // BpmnResourceRole
                addResults(theBpmnProcess.getResource());
            }
            
            if (this.findLinksChildren) {
                // SequenceFlow
                for (final BpmnFlowElement element : theBpmnProcess.getFlowElement(BpmnSequenceFlow.class)) {
                    if (element.getLane().isEmpty()) {
                        addResult(element);
                    }
                }
            }
            return super.visitBpmnProcess(theBpmnProcess);
        }

        @objid ("a2ed02d0-b764-477a-8e43-06e06eb77f4d")
        @Override
        public Object visitBpmnCollaboration(final BpmnCollaboration theBpmnCollaboration) {
            if (this.findUmlChildren) {
            
                // BpmnArtifact
                addResults(theBpmnCollaboration.getArtifact());
            
                // BpmnCollaboration
                addResults(theBpmnCollaboration.getParticipants());
            
            }
            
            if (this.findLinksChildren) {
                // MessageFlow
                addResults(theBpmnCollaboration.getMessageFlow());
            }
            return super.visitBpmnCollaboration(theBpmnCollaboration);
        }

        @objid ("aa5deb48-deb3-4f39-b3c1-ea846faec669")
        @Override
        public Object visitBpmnLane(final BpmnLane theBpmnLane) {
            if (this.findUmlChildren) {
            
                // BpmnLaneSet
                if (theBpmnLane.getChildLaneSet() != null) {
                    addResults(theBpmnLane.getChildLaneSet().getLane());
                }
            
                // SubProcess
                addResults(theBpmnLane.getFlowElementRef(BpmnSubProcess.class));
            
                // BpmnCallActivity
                addResults(theBpmnLane.getFlowElementRef(BpmnCallActivity.class));
            
                // BpmnStartEvent
                addResults(theBpmnLane.getFlowElementRef(BpmnStartEvent.class));
            
                // BpmnCallActivity
                addResults(theBpmnLane.getFlowElementRef(BpmnTask.class));
            
                // BpmnBpmnGateway
                addResults(theBpmnLane.getFlowElementRef(BpmnGateway.class));
            
                // BpmnIntermediateCatchEven
                addResults(theBpmnLane.getFlowElementRef(BpmnIntermediateCatchEvent.class));
            
                // BpmnBoundaryEvent
                addResults(theBpmnLane.getFlowElementRef(BpmnBoundaryEvent.class));
            
                // BpmnThrowEvent
                addResults(theBpmnLane.getFlowElementRef(BpmnThrowEvent.class));
            
                // BpmnItemAwareElemen
                addResults(theBpmnLane.getFlowElementRef(BpmnItemAwareElement.class));
            
            }
            
            if (this.findLinksChildren) {
                // SequenceFlow
                addResults(theBpmnLane.getFlowElementRef(BpmnSequenceFlow.class));
            }
            return super.visitBpmnLane(theBpmnLane);
        }

        @objid ("31ba2028-3fce-4ce4-b8ea-0f08d3ae6f41")
        @Override
        public Object visitBpmnActivity(final BpmnActivity theBpmnActivity) {
            if (this.findUmlChildren) {
                // BpmnDataInput
                addResults(theBpmnActivity.getInputSpecification());
            
                // BpmnDataOutput
                addResults(theBpmnActivity.getOutputSpecification());
            
                // BpmnLoopCharacteristics
                if (theBpmnActivity.getLoopCharacteristics() != null) {
                    addResult(theBpmnActivity.getLoopCharacteristics());
                }
            
                addResults(theBpmnActivity.getBoundaryEventRef());
            }
            
            if (this.findLinksChildren) {
                // BpmnDataAssoication
                addResults(theBpmnActivity.getDataInputAssociation());
            
                // BpmnDataDataAssoication
                addResults(theBpmnActivity.getDataOutputAssociation());
            }
            return super.visitBpmnActivity(theBpmnActivity);
        }

        @objid ("9e1917d0-7141-42b1-a35d-b82a1968c237")
        @Override
        public Object visitBpmnLaneSet(final BpmnLaneSet theBpmnLane) {
            if (this.findUmlChildren) {
                // BpmnLane
                addResults(theBpmnLane.getLane());
            }
            return super.visitBpmnLaneSet(theBpmnLane);
        }

        @objid ("cf4bb100-000c-4a4b-a9fe-d4cfdb231845")
        @Override
        public Object visitBpmnFlowNode(final BpmnFlowNode theBpmnFlowNode) {
            if (this.findUmlChildren) {
                // BpmnResourceRole
                addResults(theBpmnFlowNode.getResource());
            }
            return super.visitBpmnFlowNode(theBpmnFlowNode);
        }

        @objid ("911a6714-92c8-4bdc-b380-cdecf4d81771")
        @Override
        public Object visitBpmnComplexBehaviorDefinition(final BpmnComplexBehaviorDefinition theBpmnComplexBehaviorDefinition) {
            if (this.findUmlChildren) {
                // BpmnImplicitThrowEvent
                if (theBpmnComplexBehaviorDefinition.getEvent() != null) {
                    addResult(theBpmnComplexBehaviorDefinition.getEvent());
                }
            }
            return super.visitBpmnComplexBehaviorDefinition(theBpmnComplexBehaviorDefinition);
        }

        @objid ("0028edb9-7013-4f26-9f42-1f84fac8d472")
        @Override
        public Object visitBpmnMultiInstanceLoopCharacteristics(final BpmnMultiInstanceLoopCharacteristics theBpmnMultiInstanceLoopCharacteristics) {
            if (this.findUmlChildren) {
                // BpmnDataInput
                if (theBpmnMultiInstanceLoopCharacteristics.getLoopDataInput() != null) {
                    addResult(theBpmnMultiInstanceLoopCharacteristics.getLoopDataInput());
                }
            
                // BpmnDataOutput
                if (theBpmnMultiInstanceLoopCharacteristics.getLoopDataOutputRef() != null) {
                    addResult(theBpmnMultiInstanceLoopCharacteristics.getLoopDataOutputRef());
                }
            
                // BpmnComplexBehaviorDefinition
                addResults(theBpmnMultiInstanceLoopCharacteristics.getComplexBehaviorDefinition());
            }
            return super.visitBpmnMultiInstanceLoopCharacteristics(theBpmnMultiInstanceLoopCharacteristics);
        }

        @objid ("8b23526d-f78e-4ace-8cf3-5cbbf9b1836f")
        @Override
        public Object visitBpmnSubProcess(final BpmnSubProcess theBpmnSubProcess) {
            if (this.findUmlChildren) {
                // SubProcess
                addResults(theBpmnSubProcess.getFlowElement(BpmnSubProcess.class));
            
                // BpmnCallActivity
                addResults(theBpmnSubProcess.getFlowElement(BpmnCallActivity.class));
            
                // BpmnStartEvent
                addResults(theBpmnSubProcess.getFlowElement(BpmnStartEvent.class));
            
                // BpmnCallActivity
                addResults(theBpmnSubProcess.getFlowElement(BpmnTask.class));
            
                // BpmnBpmnGateway
                addResults(theBpmnSubProcess.getFlowElement(BpmnGateway.class));
            
                // BpmnIntermediateCatchEven
                addResults(theBpmnSubProcess.getFlowElement(BpmnIntermediateCatchEvent.class));
            
                // BpmnBoundaryEvent
                // addResults(theBpmnSubProcess.getFlowElement(BpmnBoundaryEvent.class));
            
                // BpmnThrowEvent
                addResults(theBpmnSubProcess.getFlowElement(BpmnThrowEvent.class));
            
                // BpmnItemAwareElemen
                addResults(theBpmnSubProcess.getFlowElement(BpmnItemAwareElement.class));
            
            }
            
            if (this.findLinksChildren) {
                // SequenceFlow
                addResults(theBpmnSubProcess.getFlowElement(BpmnSequenceFlow.class));
            }
            return super.visitBpmnSubProcess(theBpmnSubProcess);
        }

        @objid ("266b1b86-0cc1-4933-829f-2282ea4946d9")
        @Override
        public Object visitBpmnCatchEvent(final BpmnCatchEvent theBpmnCatchEvent) {
            if (this.findUmlChildren) {
                // BpmnDataOutput
                if (theBpmnCatchEvent.getDataOutput() != null) {
                    addResult(theBpmnCatchEvent.getDataOutput());
                }
            }
            
            if (this.findLinksChildren) {
                // BpmnDataAssoication
                addResults(theBpmnCatchEvent.getDataOutputAssociation());
            }
            return super.visitBpmnCatchEvent(theBpmnCatchEvent);
        }

        @objid ("a4e9101d-ace8-4b60-b77f-4aee0b48621b")
        @Override
        public Object visitBpmnEvent(final BpmnEvent theBpmnEvent) {
            if (this.findUmlChildren) {
            
                // BpmnEventDefinitions
                addResults(theBpmnEvent.getEventDefinitions());
            }
            return super.visitBpmnEvent(theBpmnEvent);
        }

        @objid ("8d2c3562-96a5-4049-adea-ec8657a01839")
        @Override
        public Object visitBpmnThrowEvent(final BpmnThrowEvent theBpmnThrowEvent) {
            if (this.findUmlChildren) {
                // BpmnDataInput
                if (theBpmnThrowEvent.getDataInput() != null) {
                    addResult(theBpmnThrowEvent.getDataInput());
                }
            }
            if (this.findLinksChildren) {
                // BpmnDataAssociation
                addResults(theBpmnThrowEvent.getDataInputAssociation());
            }
            return super.visitBpmnThrowEvent(theBpmnThrowEvent);
        }

        @objid ("556ae953-a75e-4b99-b012-57422c317f4c")
        @Override
        public Object visitBpmnSequenceFlow(final BpmnSequenceFlow theBpmnSequenceFlow) {
            if (this.findLinksChildren) {
                // BpmnSequenceFlowDataAssociation
                addResults(theBpmnSequenceFlow.getConnector());
            }
            return super.visitBpmnSequenceFlow(theBpmnSequenceFlow);
        }

        @objid ("7c2eb9e0-8030-487d-b153-749a6fadea54")
        @Override
        public Object visitBpmnItemAwareElement(final BpmnItemAwareElement theBpmnItemAwareElement) {
            if (this.findUmlChildren) {
            
                // BpmnDataState
                if (theBpmnItemAwareElement.getDataState() != null) {
                    addResult(theBpmnItemAwareElement.getDataState());
                }
            }
            return super.visitBpmnItemAwareElement(theBpmnItemAwareElement);
        }

        @objid ("97196f54-7747-43e3-a05d-c9674b1faa70")
        @Override
        public Object visitBpmnResource(final BpmnResource theBpmnResource) {
            if (this.findUmlChildren) {
            
                // BpmnResourceParameter
                addResults(theBpmnResource.getParameter());
            }
            return super.visitBpmnResource(theBpmnResource);
        }

        @objid ("fa14b746-9658-4602-bc4d-6767e89cb40b")
        @Override
        public Object visitBpmnResourceRole(final BpmnResourceRole theBpmnResourceRole) {
            if (this.findUmlChildren) {
            
                // BpmnResourceParameterBinding
                addResults(theBpmnResourceRole.getResourceParameterBinding());
            }
            return super.visitBpmnResourceRole(theBpmnResourceRole);
        }

        @objid ("ed2ba046-ba01-4c31-8914-e8374d343400")
        @Override
        public Object visitBpmnInterface(final BpmnInterface theBpmnInterface) {
            if (this.findUmlChildren) {
            
                // BpmnOperation
                addResults(theBpmnInterface.getOperation());
            }
            return super.visitBpmnInterface(theBpmnInterface);
        }

        @objid ("8b8839c6-b070-45f9-a6da-632e332b1e4a")
        @Override
        public Object visitMetaclassReference(MetaclassReference theMetaclassReference) {
            if (this.findUmlChildren) {
                addResults(theMetaclassReference.getDefinedTagType());
            
                addResults(theMetaclassReference.getDefinedNoteType());
            
                addResults(theMetaclassReference.getDefinedExternDocumentType());
            }
            return super.visitMetaclassReference(theMetaclassReference);
        }

        @objid ("e3d0d39b-6667-4073-bee7-b8ea65e39d90")
        @Override
        public Object visitModuleComponent(ModuleComponent theModule) {
            if (this.findUmlChildren) {
                addResults(theModule.getOwnedProfile());
            
                addResults(theModule.getModuleParameter());
            
            }
            return super.visitModuleComponent(theModule);
        }

        @objid ("e9f04b91-7160-45b3-87d2-b5879b8f501c")
        @Override
        public Object visitProfile(Profile theProfile) {
            if (this.findUmlChildren) {
                addResults(theProfile.getOwnedReference());
            
                addResults(theProfile.getDefinedStereotype());
            
            }
            return super.visitProfile(theProfile);
        }

        @objid ("0988fdcc-739d-465b-a817-7bbe8f8d527c")
        @Override
        public Object visitStereotype(Stereotype theStereotype) {
            if (this.findUmlChildren) {
                addResults(theStereotype.getDefinedTagType());
            
                addResults(theStereotype.getDefinedNoteType());
            
                addResults(theStereotype.getDefinedExternDocumentType());
                
                addResult(theStereotype.getDefinedTable());
            }
            return super.visitStereotype(theStereotype);
        }

        @objid ("a25945e3-69b1-42b2-b675-480089d9286f")
        @Override
        public Object visitBusinessRuleContainer(BusinessRuleContainer theBusinessRuleContainer) {
            if (this.findScopeChildren) {
                // BusinessRuleContainer
                for (BusinessRuleContainer requirementContainer : theBusinessRuleContainer.getOwnedContainer()) {
                    addResult(requirementContainer);
                }
            
                // BusinessRule
                for (BusinessRule businessRule : theBusinessRuleContainer.getOwnedRule()) {
                    addResult(businessRule);
                }
            }
            return super.visitBusinessRuleContainer(theBusinessRuleContainer);
        }

        @objid ("9a672be7-a453-460b-9638-187c3d44f19a")
        @Override
        public Object visitGoalContainer(GoalContainer theGoalContainer) {
            if (this.findScopeChildren) {
                // GoalContainer
                for (GoalContainer requirementContainer : theGoalContainer.getOwnedContainer()) {
                    addResult(requirementContainer);
                }
            
                // Goal
                for (Goal goal : theGoalContainer.getOwnedGoal()) {
                    addResult(goal);
                }
            }
            return super.visitGoalContainer(theGoalContainer);
        }

        @objid ("94a09187-7dde-4a09-a735-9fc7355bd508")
        @Override
        public Object visitRequirement(Requirement theRequirement) {
            if (this.findScopeChildren) {
                // Requirement
                for (Requirement requirement : theRequirement.getSubRequirement()) {
                    addResult(requirement);
                }
            }
            
            // Old versions
            if (this.findArchivedVersionChildren) 
                addResults(theRequirement.getArchivedRequirementVersion());
            return super.visitRequirement(theRequirement);
        }

        @objid ("9a22969f-7720-4ae0-bf80-9b230477d7eb")
        @Override
        public Object visitBusinessRule(BusinessRule theBusinessRule) {
            if (this.findScopeChildren) {
                // BusinessRule
                for (BusinessRule businessRule : theBusinessRule.getSubRule()) {
                    addResult(businessRule);
                }
            }
            
            // Old versions
            if (this.findArchivedVersionChildren) 
                addResults(theBusinessRule.getArchivedRuleVersion());
            return super.visitBusinessRule(theBusinessRule);
        }

        @objid ("8bf3057b-42da-4665-a491-090f89c2a1e3")
        @Override
        public Object visitGoal(Goal theGoal) {
            if (this.findScopeChildren) {
                // Goal
                for (Goal goal : theGoal.getSubGoal()) {
                    addResult(goal);
                }
            }
            
            // Old versions
            if (this.findArchivedVersionChildren) 
                addResults(theGoal.getArchivedGoalVersion());
            return super.visitGoal(theGoal);
        }

        @objid ("082b29dd-54c0-4f8a-9529-4789b7629ba9")
        @Override
        public Object visitEnumeratedPropertyType(EnumeratedPropertyType theEnumeratedPropertyType) {
            if (this.findScopeChildren) {
                // PropertyEnumerationLitteral
                for (PropertyEnumerationLitteral litteral : theEnumeratedPropertyType.getLitteral()) {
                    addResult(litteral);
                }
            }
            return super.visitEnumeratedPropertyType(theEnumeratedPropertyType);
        }

        /**
         * Avoid having a duplicated element in the result, but preserves order unlike a Set.
         * @param elt the element to add.
         */
        @objid ("a3e8807f-29fa-485f-9a75-9a4d8d845c97")
        private void addResult(Object elt) {
            if (elt != null && !this.result.contains(elt)) {
                this.result.add(elt);
            }
        }

        /**
         * Avoid having duplicated elements in the result, but preserves order unlike a Set.
         * @param elts the element to add.
         */
        @objid ("7f29ac99-30e3-479e-a283-17ca3e951f9c")
        private void addResults(List<? extends MObject> elts) {
            for (MObject elt : elts) {
                addResult(elt);
            }
        }

        @objid ("176836c0-a540-474d-85af-33e0bb82a33f")
        @Override
        public Object visitNaryAssociationEnd(NaryAssociationEnd theNaryAssociationEnd) {
            if (this.findUmlChildren) {
                final NaryAssociation association = theNaryAssociationEnd.getNaryAssociation();
                if (association != null) {
                    final ClassAssociation linkToClass = association.getLinkToClass();
                    if (linkToClass != null) {
                        addResult(linkToClass);
                    }
                }
            }
            return super.visitNaryAssociationEnd(theNaryAssociationEnd);
        }

        @objid ("a9e188b0-8115-4103-84d9-480eb741ed4b")
        @Override
        public Object visitCommunicationChannel(CommunicationChannel theCommunicationChannel) {
            if (this.findUmlChildren) {
                // CommunicationMessages
                addResults(theCommunicationChannel.getStartToEndMessage());
                addResults(theCommunicationChannel.getEndToStartMessage());
            }
            return super.visitCommunicationChannel(theCommunicationChannel);
        }

        @objid ("953e9e84-9521-48e3-adad-0a054e6e6ddc")
        @Override
        public Object visitMatrixDefinition(MatrixDefinition theMatrixDefinition) {
            if (this.findUmlChildren) {
                addResult(theMatrixDefinition.getLinesDefinition());
                
                addResult(theMatrixDefinition.getColumnsDefinition());
                
                addResult(theMatrixDefinition.getDepthDefinition());
                
                addResult(theMatrixDefinition.getValuesDefinition());
            }
            return super.visitMatrixDefinition(theMatrixDefinition);
        }

        @objid ("041fe7fb-42b3-4960-a8c7-ff0f9073ace4")
        @Override
        public Object visitAssociation(Association theAssociation) {
            if (this.findUmlChildren) {
                addResult(theAssociation.getLinkToClass());
            }
            return super.visitAssociation(theAssociation);
        }

        @objid ("c0c0a678-8058-4b84-9bb8-55f0e7daa5ab")
        @Override
        public Object visitLinkEnd(LinkEnd theLinkEnd) {
            if (this.findUmlChildren) {
                addResult(theLinkEnd.getLink());
            }
            return super.visitLinkEnd(theLinkEnd);
        }

        @objid ("51de2b4e-b072-4a1b-96dc-f1992a43469a")
        @Override
        public Object visitGenericAnalystContainer(GenericAnalystContainer obj) {
            if (this.findScopeChildren) {
                // GenericAnalystContainer
                for (GenericAnalystContainer requirementContainer : obj.getOwnedContainer()) {
                    addResult(requirementContainer);
                }
            
                // GenericAnalystElement
                for (GenericAnalystElement requirement : obj.getOwnedElement()) {
                    addResult(requirement);
                }
            }
            return super.visitGenericAnalystContainer(obj);
        }

        @objid ("f2e996b1-4c4c-4348-8559-2b2969a43f13")
        @Override
        public Object visitGenericAnalystElement(GenericAnalystElement obj) {
            if (this.findScopeChildren) {
                // Sub GenericAnalystElement
                for (GenericAnalystElement requirement : obj.getSubElement()) {
                    addResult(requirement);
                }
            }    
            
            // Old versions
            if (this.findArchivedVersionChildren) 
                addResults(obj.getArchivedElementVersion());
            return super.visitGenericAnalystElement(obj);
        }

        @objid ("a95e03fb-764f-47d4-9e92-dc02a3784526")
        @Override
        public Object visitTerm(Term obj) {
            // Old versions
            if (this.findArchivedVersionChildren) 
                addResults(obj.getArchivedTermVersion());
            return super.visitTerm(obj);
        }

        @objid ("3f6e6d13-659c-4e8c-a751-c7f1133b1cab")
        public Object[] getArchivedVersions(ArchiveContainer parent) {
            return getChildren(parent.getElement(), false, false, false, true).toArray();
        }

        @objid ("b97e1abb-e2f0-40e3-a3a4-79c945f84027")
        List<Object> getArchivedChildren(final MObject parent) {
            return getChildren(parent, false, false, false, true);
        }

        @objid ("f7f23377-a5a8-4d32-9199-09963754f921")
        List<Object> getLinkChildren(final MObject parent) {
            return getChildren(parent, false, false, true, false);
        }

    }

    @objid ("3976546c-0369-4210-9227-2fcd043ed4e0")
    private class ViewRefresher implements Runnable {
        @objid ("a135a24b-5079-4418-80bd-260b1acfc3fe")
        public ViewRefresher() {
            // nothing
        }

        @objid ("069ad896-68a8-41d7-8c83-43ef69fdcda0")
        @Override
        public void run() {
            doRefreshViewer();
        }

    }

    @objid ("b780e61b-8b01-4bc0-925d-f2bdabdd4a0d")
    private static class FragmentComparator implements Comparator<IProjectFragment> {
        @objid ("2403e2ad-9b9f-4ce9-a792-acc866bbe802")
        public FragmentComparator() {
            // Empty constructor
        }

        @objid ("d4fc43be-60b9-42ce-92c6-20faf04f9813")
        @Override
        public int compare(IProjectFragment f1, IProjectFragment f2) {
            if (f1.getType() == f2.getType()) {
                return f1.getId().compareTo(f2.getId());
            } else {
                return getTypeWeight(f1.getType()) - getTypeWeight(f2.getType());
            }
        }

        @objid ("0e80d258-ce9c-43b3-936d-13bfdd6b2d18")
        private static int getTypeWeight(FragmentType type) {
            switch (type) {
            case EXML:
                return 0;
            case EXML_SVN:
                return 1;
            case RAMC:
                return 2;
            case EXML_URL:
                return 3;
            case MDA:
                return 4;
            default:
                return 99;
            }
        }

    }

}
