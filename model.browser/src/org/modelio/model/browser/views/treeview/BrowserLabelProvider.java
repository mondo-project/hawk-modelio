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

import java.net.URL;
import java.util.List;
import java.util.Stack;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.core.ui.images.ElementStyler;
import org.modelio.core.ui.images.FragmentImageService;
import org.modelio.core.ui.images.FragmentStyledLabelProvider;
import org.modelio.core.ui.images.IModelioElementLabelProvider;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptCallEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallOperationAction;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.activityModel.SendSignalAction;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Event;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageSort;
import org.modelio.metamodel.uml.behavior.stateMachineModel.AbstractPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateVertex;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.metamodel.uml.informationFlow.DataFlow;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Abstraction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.Substitution;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.ComponentRealization;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.ElementRealization;
import org.modelio.metamodel.uml.statik.Feature;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.PackageImport;
import org.modelio.metamodel.uml.statik.PackageMerge;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.PassingMode;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.metamodel.uml.statik.RaisedException;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.uml.statik.TemplateParameterSubstitution;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.model.browser.plugin.ModelBrowser;

/**
 * Browser tree label provider.
 */
@objid ("6948d6a1-d63b-11e1-9955-002564c97630")
public class BrowserLabelProvider extends LabelProvider implements IModelioElementLabelProvider {
    @objid ("1e70582e-d672-11e1-9955-002564c97630")
    private static final String IMAGES_PATH = "icons/";

    @objid ("7234e8b1-4540-11e2-aeb7-002564c97630")
    private boolean showVisibility = true;

    @objid ("c13d663c-d63b-11e1-9955-002564c97630")
     BrowserLabelService umlLabelService;

    @objid ("c9ecbf47-97a3-4b36-8d5e-da4650a0f278")
    private static final Image ARCHIVE_CONTAINER = loadImage("analyst_archives.png");

    @objid ("02e4e8a3-149e-446d-acfb-95c4922e76dd")
    private static final Image LINK_CONTAINER = loadImage("links.png");

    @objid ("ed4047da-504e-450b-8ff8-58ee18765a5e")
     static final Font normalFont = CoreFontRegistry.getFont(Display.getCurrent().getSystemFont().getFontData());

    @objid ("8508489f-7996-4cd7-ae4f-983d88315575")
     static final Font italicFont = CoreFontRegistry.getModifiedFont(normalFont, SWT.ITALIC);

    /**
     * Default c'tor.
     */
    @objid ("c13d663d-d63b-11e1-9955-002564c97630")
    public BrowserLabelProvider() {
        this.umlLabelService = new BrowserLabelService();
    }

    @objid ("c13d663f-d63b-11e1-9955-002564c97630")
    @Override
    public Image getImage(Object obj) {
        // null object special case
        if (obj == null) {
            return null;
        } else if (obj instanceof Element) {
            final Element element = (Element) obj;
            return ElementImageService.getIcon(element);
        } else if (obj instanceof IProjectFragment) {
            return FragmentImageService.getImage((IProjectFragment) obj);
        } else if (obj instanceof LinkContainer) {
            return LINK_CONTAINER;
        } else if (obj instanceof ArchiveContainer) {
            return ARCHIVE_CONTAINER;
        }
        
        // Should never happen
        assert (false);
        return null;
    }

    @objid ("8d6903fc-0043-4649-bca9-2d939542e439")
    @Override
    public StyledString getStyledText(Object obj) {
        if (obj == null) {
            return new StyledString("<null>", StyledString.createColorRegistryStyler("red", null));
        } else if (obj instanceof Element) {
            return this.umlLabelService.getLabel((Element) obj, this.showVisibility);
        } else if (obj instanceof IProjectFragment) {
            return FragmentStyledLabelProvider.getStyledText((IProjectFragment) obj);
        } else if (obj instanceof LinkContainer) {
            final int nbLinks = ((LinkContainer) obj).getNbLinks();
            return new StyledString(nbLinks <= 1
                    ? ModelBrowser.I18N.getMessage("LinkContainer.Single", nbLinks)
                            : ModelBrowser.I18N.getMessage("LinkContainer.Multi", nbLinks));
        
        } else if (obj instanceof ArchiveContainer) {
            final int nbVersions = ((ArchiveContainer) obj).getNbVersions();
            return new StyledString(nbVersions <= 1 
                    ? ModelBrowser.I18N.getMessage("ArchiveContainer.Single", nbVersions)
                            : ModelBrowser.I18N.getMessage("ArchiveContainer.Multi", nbVersions));
        }
        
        // Unknown object
        return new StyledString(obj.toString(), StyledString.createColorRegistryStyler("red", null));
    }

    /**
     * Returns the simple label of an object, without style additions.
     * Uses {@link BrowserLabelProvider#getStyledText(Object)}.
     */
    @objid ("7ffe017a-d90b-4845-ad9b-9bfd1a76b146")
    @Override
    public String getText(Object element) {
        return getStyledText(element).getString();
    }

    /**
     * @return <code>true</code> if feature visibility is shown.
     */
    @objid ("7234e8b4-4540-11e2-aeb7-002564c97630")
    public boolean isShowVisibility() {
        return this.showVisibility;
    }

    /**
     * Enable or disable display of element visibility.
     * @param showVisibility <code>true</code> to enable, <code>false</code> to disable.
     */
    @objid ("7234e8b8-4540-11e2-aeb7-002564c97630")
    public void setShowVisibility(boolean showVisibility) {
        this.showVisibility = showVisibility;
    }

    @objid ("0017ec42-e1fe-100f-85b1-001ec947cd2a")
    @Override
    public boolean showAsReference(Object object) {
        return false;
    }

    @objid ("1e2a276c-d672-11e1-9955-002564c97630")
    private static Image loadImage(String imageFileName) {
        ImageDescriptor desc = null;
        Image image = null;
        
        // Get the relative file name
        final StringBuilder path = new StringBuilder(IMAGES_PATH);
        path.append(imageFileName);
        
        final IPath imagePath = new Path(path.toString());
        final URL url = FileLocator.find(ModelBrowser.getInstance().getBundle(), imagePath, null);
        assert (url != null);
        
        if (url != null) {
            desc = ImageDescriptor.createFromURL(url);
            image = desc.createImage();
            assert (image != null);
        }
        return image;
    }

    /**
     * This class provide the label of the elements displayed in the UML explorer
     */
    @objid ("c13eecb7-d63b-11e1-9955-002564c97630")
    private static class BrowserLabelService extends DefaultModelVisitor {
        @objid ("72374a0f-4540-11e2-aeb7-002564c97630")
        private boolean showVisibility;

        /**
         * a stack used for recursive calls to {@link #getLabel(Element, boolean)}
         */
        @objid ("f94f1a9e-48bf-4431-928d-7e4cf676b564")
        private Stack<Element> elementStack;

        @objid ("0ce4f277-c7ca-41df-9f73-4d986be0c95f")
         StyledString elementStyledLabel;

        @objid ("d4a49a92-5199-4f9c-9f9b-b93c2a9a0ec3")
        public BrowserLabelService() {
            this(new Stack<Element>());
        }

        /**
         * Initialize the label service.
         * @param elementStack a stack to use for recursive calls to {@link #getLabel(Element, boolean)}
         */
        @objid ("c13eecbb-d63b-11e1-9955-002564c97630")
        public BrowserLabelService(Stack<Element> elementStack) {
            super();
            this.elementStyledLabel = new StyledString();
            this.elementStack = elementStack;
        }

        /**
         * Get the explorer label for the given element.
         * @param element The element to get symbol
         * @param mustShowVisibility Whether or not to show the visibility in the labels.
         * @return The element symbol.
         */
        @objid ("c13eecbe-d63b-11e1-9955-002564c97630")
        public StyledString getLabel(Element element, boolean mustShowVisibility) {
            // Guard agains't null elements
            if (element == null) {
                return new StyledString("<null>", ElementStyler.getStyler(element));
            }
            
            if (this.elementStack.contains(element)) {
                // loop detected, return the name...
                return new StyledString(element.getName());
            }
            
            // store the element for loop detection
            this.elementStack.push(element);
            
            try {
                // reset the returned value
                this.elementStyledLabel = new StyledString();
                this.showVisibility = mustShowVisibility;
            
                // call the visitor
                element.accept(this);
            
                // return the value
                return this.elementStyledLabel;
            } finally {
                this.elementStack.pop();
            }
        }

        @objid ("c13eecc4-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitAcceptCallEventAction(AcceptCallEventAction theAcceptCallEventAction) {
            final StyledString symbol = new StyledString();
            
            final String acceptCallEventActionName = theAcceptCallEventAction.getName();
            final Operation operation = theAcceptCallEventAction.getCalled();
            
            if (operation != null && (acceptCallEventActionName.equals("Unnamed") || acceptCallEventActionName.equals(""))) {
                symbol.append(operation.getName(), ElementStyler.getStyler(theAcceptCallEventAction, operation));
            } else {
                symbol.append(acceptCallEventActionName, ElementStyler.getStyler(theAcceptCallEventAction));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c13eecca-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitAcceptSignalAction(AcceptSignalAction theAcceptSignalAction) {
            final StyledString symbol = new StyledString();
            
            final String acceptSignalActionName = theAcceptSignalAction.getName();
            final List<Signal> signals = theAcceptSignalAction.getAccepted();
            Styler styler = ElementStyler.getStyler(theAcceptSignalAction);
            if (signals.size() > 0 && (acceptSignalActionName.equals("Unnamed") || acceptSignalActionName.equals(""))) {
                for (int i = 0; i < signals.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ", styler);
                    }
                    symbol.append(signals.get(i).getName(), ElementStyler.getStyler(theAcceptSignalAction, signals.get(i)));
                }
            } else {
                symbol.append(acceptSignalActionName, styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c13eecd0-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitActivityEdge(ActivityEdge theActivityEdge) {
            final StyledString symbol = new StyledString();
            
            final ActivityNode target = theActivityEdge.getTarget();
            
            if (target != null) {
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(target, this.showVisibility).toString(), ElementStyler.getStyler(theActivityEdge, target));
            } else {
                final String name = theActivityEdge.getName();
                symbol.append(name, ElementStyler.getStyler(theActivityEdge));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c13eecd6-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitActivityParameterNode(ActivityParameterNode theActivityParameterNode) {
            final StyledString symbol = new StyledString();
            
            // PassingMode passingMode =
            // theActivityParameterNode.getParameterPassing();
            Styler styler = ElementStyler.getStyler(theActivityParameterNode);
            final GeneralClass type = theActivityParameterNode.getType();
            
            final BehaviorParameter behaviorParameter = theActivityParameterNode.getRepresentedRealParameter();
            
            if (behaviorParameter != null) {
                symbol.append(behaviorParameter.getName(), ElementStyler.getStyler(theActivityParameterNode, behaviorParameter));
                symbol.append(" ");
            } else {
                symbol.append(theActivityParameterNode.getName(), styler);
                symbol.append(" ");
            }
            
            Parameter theParameter = null;
            
            if (behaviorParameter != null) {
                theParameter = behaviorParameter.getMapped();
            }
            
            if (theParameter != null && theParameter.getComposed() != null) {
                final PassingMode passingMode = theParameter.getParameterPassing();
            
                if (passingMode == PassingMode.IN) {
                    symbol.append("In", styler);
                }
                if (passingMode == PassingMode.OUT) {
                    symbol.append("Out", styler);
                }
                if (passingMode == PassingMode.INOUT) {
                    symbol.append("Inout", styler);
                }
            } else if (theParameter != null && theParameter.getReturned() != null) {
                symbol.append("Out", styler);
            }
            
            if (behaviorParameter != null) {
                symbol.append(": ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(theActivityParameterNode, type));
                } else {
                    symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c13eecdc-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitActivityPartition(ActivityPartition theActivityPartition) {
            final StyledString symbol = new StyledString();
            Styler styler = ElementStyler.getStyler(theActivityPartition);
            symbol.append(theActivityPartition.getName(), styler);
            
            final ModelElement represented = theActivityPartition.getRepresented();
            if (represented != null) {
                symbol.append(":", styler);
                Styler representedStyler = ElementStyler.getStyler(theActivityPartition, represented);
                if (represented instanceof ActivityPartition) {
                    symbol.append(represented.getName(), representedStyler);
                } else {
                    final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                    symbol.append(labelService.getLabel(represented, this.showVisibility).toString(), representedStyler);
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("d31fd921-5031-4c58-8fca-4b9392ba4efb")
        @Override
        public Object visitAnalystElement(AnalystElement obj) {
            final Styler styler = ElementStyler.getStyler(obj);
            final StringBuilder label = new StringBuilder (obj.getName());
            
            if (obj.getVersion() != 0) {
                label.append(" (v");
                label.append(obj.getVersion());
                label.append(")");
            }
            
            this.elementStyledLabel = new StyledString(label.toString(), styler);
            return null;
        }

        @objid ("c13eece2-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitAssociationEnd(AssociationEnd theAssociationEnd) {
            final StyledString symbol = new StyledString();
            Styler styler = ElementStyler.getStyler(theAssociationEnd);
            
            final VisibilityMode visibility = theAssociationEnd.getVisibility();
            symbol.append(getVisibilitySymbol(visibility), styler);
            
            if (theAssociationEnd.isIsDerived()) {
                symbol.append("/", styler);
            }
            
            final String associationEndName = theAssociationEnd.getName();
            if (associationEndName.isEmpty()) {
                symbol.append(ModelBrowser.I18N.getString("NoName"), styler);
            } else {
                symbol.append(theAssociationEnd.getName(), styler);
            }
            
            symbol.append(": ", styler);
            
            // The type
            final Classifier type = theAssociationEnd.getTarget();
            if (type != null) {
                symbol.append(type.getName(), ElementStyler.getStyler(theAssociationEnd, type));
            } else {
                symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
            }
            
            // The cardinality
            symbol.append(getAssociationEndMultiplicity(theAssociationEnd).toString(), styler);
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1407354-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitAttribute(Attribute theAttribute) {
            final StyledString symbol = new StyledString();
            Styler styler = ElementStyler.getStyler(theAttribute);
            final VisibilityMode visibility = theAttribute.getVisibility();
            symbol.append(getVisibilitySymbol(visibility), styler);
            
            if (theAttribute.isIsDerived()) {
                symbol.append("/", styler);
            }
            
            symbol.append(theAttribute.getName(), styler);
            
            final GeneralClass type = theAttribute.getType();
            
            symbol.append(" : ", styler);
            if (type != null) {
                symbol.append(type.getName(), ElementStyler.getStyler(theAttribute, type));
            } else {
                symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
            }
            
            symbol.append(getAttributeMultiplicity(theAttribute).toString(), styler);
            
            final String value = theAttribute.getValue();
            if (value != null && !value.equals("")) {
                symbol.append(" = ", styler);
                symbol.append(value, styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c140735a-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitAttributeLink(AttributeLink theAttributeLink) {
            final StyledString symbol = new StyledString();
            Styler styler = ElementStyler.getStyler(theAttributeLink);
            final Attribute base = theAttributeLink.getBase();
            
            if (base != null) {
                symbol.append(base.getName(), ElementStyler.getStyler(theAttributeLink, base));
            } else {
                symbol.append(theAttributeLink.getName(), styler);
            }
            
            final String value = theAttributeLink.getValue();
            
            if (!value.equals("")) {
                symbol.append(" = ", styler);
                symbol.append(value, styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1407360-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitBehaviorParameter(BehaviorParameter theBehaviorParameter) {
            final Parameter theParameter = theBehaviorParameter.getMapped();
            Styler styler = ElementStyler.getStyler(theBehaviorParameter);
            final StyledString symbol = new StyledString();
            
            final PassingMode passingMode = theBehaviorParameter.getParameterPassing();
            
            final GeneralClass type = theBehaviorParameter.getType();
            if (theParameter != null) {
                Styler paraStyler = ElementStyler.getStyler(theBehaviorParameter, theParameter);
                if (theParameter.getComposed() != null) {
                    symbol.append(theParameter.getName(), paraStyler);
                    symbol.append(" ", paraStyler);
            
                    if (passingMode == PassingMode.IN) {
                        symbol.append("In", paraStyler);
                    }
                    if (passingMode == PassingMode.OUT) {
                        symbol.append("Out", paraStyler);
                    }
                    if (passingMode == PassingMode.INOUT) {
                        symbol.append("Inout", paraStyler);
                    }
                } else {
                    symbol.append("Out", paraStyler);
                }
            } else {
                symbol.append(theBehaviorParameter.getName(), styler);
            }
            
            symbol.append(": ");
            if (type != null) {
                symbol.append(type.getName(), ElementStyler.getStyler(theBehaviorParameter, type));
            } else {
                symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1407366-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitBinding(Binding theBinding) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theBinding);
            
            final ModelElement role = theBinding.getRole();
            final ModelElement feature = theBinding.getRepresentedFeature();
            
            final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            if (role != null) {
                symbol.append(labelService.getLabel(role, this.showVisibility).toString(), ElementStyler.getStyler(theBinding, role));
            }
            symbol.append(" --> ", styler);
            if (feature != null) {
                symbol.append(labelService.getLabel(feature, this.showVisibility).toString(), ElementStyler.getStyler(theBinding, feature));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c140736c-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitCallBehaviorAction(CallBehaviorAction theCallBehaviorAction) {
            final StyledString symbol = new StyledString();
            
            final String callBehaviorActionName = theCallBehaviorAction.getName();
            final Behavior behavior = theCallBehaviorAction.getCalled();
            
            if (behavior != null && (callBehaviorActionName.equals("Unnamed") || callBehaviorActionName.equals(""))) {
                symbol.append(behavior.getName(), ElementStyler.getStyler(theCallBehaviorAction, behavior));
            } else {
                symbol.append(callBehaviorActionName, ElementStyler.getStyler(theCallBehaviorAction));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1407372-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitCallOperationAction(CallOperationAction theCallOperationAction) {
            final StyledString symbol = new StyledString();
            
            final String callOperationActionName = theCallOperationAction.getName();
            final Operation operation = theCallOperationAction.getCalled();
            
            if (operation != null && (callOperationActionName.equals("Unnamed") || callOperationActionName.equals(""))) {
                symbol.append(operation.getName(), ElementStyler.getStyler(theCallOperationAction, operation));
            } else {
                symbol.append(callOperationActionName, ElementStyler.getStyler(theCallOperationAction));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1407378-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitClassAssociation(ClassAssociation theClassAssociation) {
            final StyledString symbol = new StyledString();
            
            final Class theClass = theClassAssociation.getClassPart();
            
            if (theClass != null) {
                symbol.append(theClass.getName(), ElementStyler.getStyler(theClassAssociation, theClass));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c140737e-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitClause(Clause theClause) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theClause);
            symbol.append("[", styler);
            
            final String test = theClause.getTest();
            if (test.length() == 0) {
                symbol.append("Conditional clause", styler);
            } else {
                symbol.append(test, styler);
            }
            
            symbol.append("]", styler);
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c141f9f3-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitCollaborationUse(CollaborationUse theCollaborationUse) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theCollaborationUse);
            symbol.append(theCollaborationUse.getName(), styler);
            
            final Collaboration base = theCollaborationUse.getType();
            symbol.append(": ", styler);
            if (base != null) {
                symbol.append(base.getName(), ElementStyler.getStyler(theCollaborationUse, base));
            } else {
                symbol.append(ModelBrowser.I18N.getString("NoBase"), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c141f9f9-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitCommunicationMessage(CommunicationMessage theCommunicationMessage) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theCommunicationMessage);
            final String name = theCommunicationMessage.getName();
            final MessageSort messageSort = theCommunicationMessage.getSortOfMessage();
            
            if (name.equals("")) {
                symbol.append(ModelBrowser.I18N.getString(messageSort.name()), styler);
            } else {
                symbol.append(name, styler);
            }
            
            if (messageSort != MessageSort.SYNCCALL && messageSort != MessageSort.CREATEMESSAGE) {
                symbol.append("(", styler);
                symbol.append(theCommunicationMessage.getArgument(), styler);
                symbol.append(")", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c141fa00-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitCommunicationNode(CommunicationNode theCommunicationNode) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theCommunicationNode);
            final Instance instance = theCommunicationNode.getRepresented();
            
            if (instance != null) {
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(instance, this.showVisibility).toString(), ElementStyler.getStyler(theCommunicationNode, instance));
            } else {
                symbol.append(theCommunicationNode.getName(), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c141fa06-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitDataFlow(DataFlow theDataFlow) {
            final StyledString symbol = new StyledString();
            
            final Signal signal = theDataFlow.getSModel();
            
            if (signal != null) {
                symbol.append(signal.getName(), ElementStyler.getStyler(theDataFlow, signal));
            } else {
                symbol.append(theDataFlow.getName(), ElementStyler.getStyler(theDataFlow));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c141fa0c-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitDependency(Dependency theDependency) {
            final ModelElement destination = theDependency.getDependsOn();
            
            assert (Metamodel.getMClass(Dependency.class) == theDependency.getMClass());
            return visitDependencyLikeObject(theDependency, "depends on", destination);
        }

        @objid ("c1438092-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            this.elementStyledLabel = new StyledString(theElement.getClass().getSimpleName(), ElementStyler.getStyler(theElement));
            return null;
        }

        @objid ("c1438098-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitElementImport(ElementImport theElementImport) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theElementImport);
            final NameSpace importedNamespace = theElementImport.getImportedElement();
            
            if (importedNamespace != null) {
                final Styler importNamespaceStyler = ElementStyler.getStyler(theElementImport, importedNamespace);
                if (theElementImport.getVisibility() == VisibilityMode.PUBLIC) {
                    symbol.append("import ", styler);
                } else {
                    symbol.append("access ", styler);
                }
                symbol.append(importedNamespace.getName(), importNamespaceStyler);
            
                final ModelTree owner = importedNamespace.getOwner();
            
                appendFrom(symbol, theElementImport, owner, styler);
                
            } else {
                symbol.append("<No destination>", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c143809e-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitEvent(Event theEvent) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theEvent);
            final Operation operation = theEvent.getCalled();
            final Signal signal = theEvent.getModel();
            
            if (operation != null) {
                symbol.append(operation.getName(), ElementStyler.getStyler(theEvent, operation));
            } else if (signal != null) {
                symbol.append(signal.getName(), ElementStyler.getStyler(theEvent, signal));
            } else if (theEvent.getName().equals("")) {
                symbol.append(theEvent.getExpression(), styler);
            } else {
                symbol.append(theEvent.getName(), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c14380a4-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitExtensionPoint(ExtensionPoint theExtensionPoint) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theExtensionPoint);
            final VisibilityMode visibility = theExtensionPoint.getVisibility();
            symbol.append(getVisibilitySymbol(visibility), styler);
            
            symbol.append(theExtensionPoint.getName(), styler);
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c14380aa-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitFeature(Feature theFeature) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theFeature);
            final VisibilityMode visibility = theFeature.getVisibility();
            symbol.append(getVisibilitySymbol(visibility), styler);
            
            symbol.append(theFeature.getName(), styler);
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c14380b0-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitGeneralization(Generalization theGeneralization) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theGeneralization);
            symbol.append("is_a ", styler);
            
            final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            final NameSpace parent = theGeneralization.getSuperType();
            
            if (parent != null) {
                symbol.append(labelService.getLabel(parent, this.showVisibility).toString(), ElementStyler.getStyler(theGeneralization, parent));
            
                final ModelTree owner = parent.getOwner();
                appendFrom(symbol, theGeneralization, owner, styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c14380b6-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitInformationFlow(InformationFlow theInformationFlow) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theInformationFlow);
            final List<Classifier> classifiers = theInformationFlow.getConveyed();
            if (classifiers.size() > 0) {
                for (int i = 0; i < classifiers.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ", styler);
                    }
                    symbol.append(classifiers.get(i).getName(), ElementStyler.getStyler(theInformationFlow, classifiers.get(i)));
                }
            } else {
                symbol.append(theInformationFlow.getName(), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c14380bc-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitInstance(Instance theInstance) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theInstance);
            symbol.append(theInstance.getName(), styler);
            
            final NameSpace base = theInstance.getBase();
            symbol.append(": ", styler);
            if (base != null) {
                symbol.append(base.getName(), ElementStyler.getStyler(theInstance, base));
            } else {
                symbol.append(ModelBrowser.I18N.getString("NoBase"), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c14380c2-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitInstanceNode(InstanceNode theInstanceNode) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theInstanceNode);
            final Instance instance = theInstanceNode.getRepresented();
            final Attribute attribut = theInstanceNode.getRepresentedAttribute();
            final AssociationEnd assocEnd = theInstanceNode.getRepresentedRole();
            final BehaviorParameter behaviorParameter = theInstanceNode.getRepresentedRealParameter();
            
            final GeneralClass type = theInstanceNode.getType();
            
            if (type != null) {
                symbol.append(theInstanceNode.getName(), styler);
            
                symbol.append(": ", styler);
                symbol.append(type.getName(), ElementStyler.getStyler(theInstanceNode, type));
            } else if (instance != null) {
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(instance, this.showVisibility).toString(), ElementStyler.getStyler(theInstanceNode, instance));
            } else if (attribut != null) {
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(attribut, this.showVisibility).toString(), ElementStyler.getStyler(theInstanceNode, attribut));
            } else if (assocEnd != null) {
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(assocEnd, this.showVisibility).toString(), ElementStyler.getStyler(theInstanceNode, assocEnd));
            } else if (behaviorParameter != null) {
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(behaviorParameter, this.showVisibility).toString(), ElementStyler.getStyler(theInstanceNode, behaviorParameter));
            } else {
                symbol.append(theInstanceNode.getName(), styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1450731-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitInterfaceRealization(InterfaceRealization theInterfaceRealization) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theInterfaceRealization);
            
            // symbol.append("Realize ");
            
            final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            final Interface implemented = theInterfaceRealization.getImplemented();
            
            if (implemented != null) {
                symbol.append(labelService.getLabel(implemented, this.showVisibility).toString(), ElementStyler.getStyler(theInterfaceRealization, implemented));
            
                final ModelTree owner = implemented.getOwner();
                appendFrom(symbol, theInterfaceRealization, owner, styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1450737-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitInternalTransition(InternalTransition theInternalTransition) {
            super.visitInternalTransition(theInternalTransition);
            if (this.elementStyledLabel.equals("")) {
                this.elementStyledLabel = new StyledString("/", ElementStyler.getStyler(theInternalTransition));
            }
            return null;
        }

        @objid ("c145073d-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitLinkEnd(LinkEnd theLinkEnd) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theLinkEnd);
            
            final String linkEndName = theLinkEnd.getName();
            
            if (linkEndName.equals("")) {
                symbol.append(ModelBrowser.I18N.getString("NoName"), styler);
            } else {
                symbol.append(linkEndName, styler);
            }
            final Instance linked = theLinkEnd.getTarget();
            if (linked != null) {
                symbol.append("::", styler);
                symbol.append(linked.getName(), ElementStyler.getStyler(theLinkEnd, theLinkEnd.getTarget()));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1450744-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitManifestation(Manifestation theManifestation) {
            final StyledString symbol = new StyledString();
            
            final Styler styler = ElementStyler.getStyler(theManifestation);
            final ModelElement destination = theManifestation.getUtilizedElement();
            
            if (destination != null) {
                symbol.append("manifested ", styler);
                symbol.append(destination.getName(), ElementStyler.getStyler(theManifestation, destination));
            
                ModelTree owner = null;
            
                if (destination instanceof ModelTree) {
                    owner = ((ModelTree) destination).getOwner();
                }
            
                appendFrom(symbol, theManifestation, owner, styler);
            } else {
                symbol.append("<No destination>", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("e267f7ae-d7ec-11e1-80f0-001ec947ccaf")
        @Override
        public Object visitMetaclassReference(MetaclassReference theMataclassReference) {
            final String ref = theMataclassReference.getReferencedClassName();
            final Styler styler = ElementStyler.getStyler(theMataclassReference);
            if (ref != null) {
                this.elementStyledLabel = new StyledString(ref, styler);
            } else {
                this.elementStyledLabel = new StyledString("<null>", styler);
            }
            return null;
        }

        @objid ("c1468dcc-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitModelElement(ModelElement theModelElement) {
            this.elementStyledLabel = new StyledString(theModelElement.getName(), ElementStyler.getStyler(theModelElement));
            return null;
        }

        @objid ("c14b21ab-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitNameSpace(NameSpace theNameSpace) {
            final List<TemplateBinding> templateInstanciations = theNameSpace.getTemplateInstanciation();
            final Styler styler = ElementStyler.getStyler(theNameSpace);
            if (templateInstanciations.isEmpty()) {
                // The name space does not instantiate templates
                this.elementStyledLabel = new StyledString(theNameSpace.getName(), styler);
            } else {
                // The name space instantiates templates
            
                final StyledString symbol = new StyledString();
            
                // append name
                final String name = theNameSpace.getName();
                symbol.append(name, styler);
            
                // append template instantiations
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            
                if (!name.isEmpty()) {
                    symbol.append(": ", styler);
                }
            
                boolean first = true;
                for (final TemplateBinding b : templateInstanciations) {
                    Styler stylerTB = ElementStyler.getStyler(theNameSpace, b);
                    if (first) {
                        first = false;
                    } else {
                        symbol.append(", ", stylerTB);
                    }
                    symbol.append(labelService.getLabel(b, this.showVisibility).toString(), stylerTB);
                }
            
                this.elementStyledLabel = symbol;
            }
            return null;
        }

        @objid ("a36acd77-2c63-4597-99fe-b76eafb3f9dd")
        @Override
        public Object visitNaryAssociationEnd(NaryAssociationEnd theAssociationEnd) {
            final StyledString symbol = new StyledString();
            Styler styler = ElementStyler.getStyler(theAssociationEnd);
            
            final VisibilityMode visibility = theAssociationEnd.getVisibility();
            symbol.append(getVisibilitySymbol(visibility), styler);
            
            if (theAssociationEnd.isIsDerived()) {
                symbol.append("/", styler);
            }
            
            final String associationEndName = theAssociationEnd.getName();
            if (associationEndName.isEmpty()) {
                symbol.append(ModelBrowser.I18N.getString("NoName"), styler);
            } else {
                symbol.append(theAssociationEnd.getName(), styler);
            }
            
            symbol.append(": ", styler);
            
            // The type
            final NaryAssociation type = theAssociationEnd.getNaryAssociation();
            symbol.append(type.getName(), ElementStyler.getStyler(theAssociationEnd, type));
            
            // The cardinality
            symbol.append(getNaryAssociationEndMultiplicity(theAssociationEnd).toString(), styler);
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("47574be7-1d18-4e9f-9362-ae557fdfbc85")
        @Override
        public Object visitNaryLinkEnd(NaryLinkEnd theLinkEnd) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theLinkEnd);
            
            final String linkEndName = theLinkEnd.getName();
            
            if (linkEndName.equals("")) {
                symbol.append(ModelBrowser.I18N.getString("NoName"), styler);
            } else {
                symbol.append(linkEndName, styler);
            }
            final NaryLink linked = theLinkEnd.getNaryLink();
            symbol.append("::", styler);
            symbol.append(linked.getName(), ElementStyler.getStyler(theLinkEnd, linked));
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468dd2-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitObjectNode(ObjectNode theObjectNode) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theObjectNode);
            final BehaviorParameter parameter = theObjectNode.getRepresentedRealParameter();
            final Instance instance = theObjectNode.getRepresented();
            final Attribute attribute = theObjectNode.getRepresentedAttribute();
            final AssociationEnd role = theObjectNode.getRepresentedRole();
            
            final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            
            if (parameter != null) {
                symbol.append(labelService.getLabel(parameter, this.showVisibility).toString(), ElementStyler.getStyler(theObjectNode, parameter));
            } else if (instance != null) {
                symbol.append(labelService.getLabel(instance, this.showVisibility).toString(), ElementStyler.getStyler(theObjectNode, instance));
            } else if (attribute != null) {
                symbol.append(labelService.getLabel(attribute, this.showVisibility).toString(), ElementStyler.getStyler(theObjectNode, attribute));
            } else if (role != null) {
                symbol.append(labelService.getLabel(role, this.showVisibility).toString(), ElementStyler.getStyler(theObjectNode, role));
            } else {
                final GeneralClass type = theObjectNode.getType();
            
                symbol.append(theObjectNode.getName(), styler);
            
                final String upperbound = theObjectNode.getUpperBound();
                if (upperbound.length() > 0 && !upperbound.equals("1")) {
                    symbol.append("[", styler);
                    symbol.append(upperbound, styler);
                    symbol.append("]", styler);
                }
            
                if (type != null) {
                    symbol.append(" : ", styler);
                    symbol.append(type.getName(), ElementStyler.getStyler(theObjectNode, type));
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468dd8-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitOperation(Operation theOperation) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theOperation);
            final VisibilityMode visibility = theOperation.getVisibility();
            symbol.append(getVisibilitySymbol(visibility), styler);
            
            symbol.append(theOperation.getName(), styler);
            
            symbol.append("(", styler);
            final List<Parameter> parameters = theOperation.getIO();
            final int parameterNumber = parameters.size();
            
            for (int i = 0; i < parameterNumber; i++) {
                final Parameter para = parameters.get(i);
                symbol.append(getParameterSymbol(para, styler, theOperation));
                if (i < parameterNumber - 1) {
                    symbol.append(", ", styler);
                }
            }
            
            symbol.append(")", styler);
            
            final Parameter returnParameter = theOperation.getReturn();
            if (returnParameter != null) {
                final GeneralClass type = returnParameter.getType();
                symbol.append(":", styler);
                symbol.append(" ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(theOperation, type));
                } else {
                    symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
                }
                symbol.append(getParameterMultiplicity(returnParameter).toString(), ElementStyler.getStyler(theOperation, returnParameter));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468dde-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitPackageImport(PackageImport thePackageImport) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(thePackageImport);
            
            final NameSpace importedNamespace = thePackageImport.getImportedPackage();
            
            if (importedNamespace != null) {
                if (thePackageImport.getVisibility() == VisibilityMode.PUBLIC) {
                    symbol.append("import all ", styler);
                } else {
                    symbol.append("access all ", styler);
                }
                symbol.append(importedNamespace.getName(), ElementStyler.getStyler(thePackageImport, importedNamespace));
            
                final ModelTree owner = importedNamespace.getOwner();
                appendFrom(symbol, thePackageImport, owner, styler);
            } else {
                symbol.append("<No destination>", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468de4-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitPackageMerge(PackageMerge thePackageMerge) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(thePackageMerge);
            
            final Package mergedPackage = thePackageMerge.getMergedPackage();
            
            if (mergedPackage != null) {
                symbol.append("merge ", styler);
                symbol.append(mergedPackage.getName(), ElementStyler.getStyler(thePackageMerge, mergedPackage));
            
                final ModelTree owner = mergedPackage.getOwner();
                appendFrom(symbol, thePackageMerge, owner, styler);
            } else {
                symbol.append("<No destination>", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468dea-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitParameter(Parameter theParameter) {
            this.elementStyledLabel = getParameterSymbol(theParameter, ElementStyler.getStyler(theParameter), null);
            return null;
        }

        @objid ("c1468df0-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitPort(Port thePort) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(thePort);
            symbol.append(thePort.getName(), styler);
            
            final NameSpace type = getType(thePort);
            if (type != null) {
                symbol.append(" : ", styler);
                symbol.append(type.getName(), ElementStyler.getStyler(thePort, type));
            }
            
            // If the name is empty, build a list of provided and required
            // interfaces as a workaround
            if (symbol.length() == 0) {
                final List<RequiredInterface> requiredInterfaces = thePort.getRequired();
                if (requiredInterfaces.size() > 0) {
                    symbol.append("R = ", styler);
                    for (int i = 0; i < requiredInterfaces.size(); i++) {
                        if (i > 0) {
                            symbol.append(", ", styler);
                        }
                        RequiredInterface ri = requiredInterfaces.get(i);
                        final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                        symbol.append(labelService.getLabel(ri, this.showVisibility).toString(), ElementStyler.getStyler(thePort, ri));
                    }
                }
            
                final List<ProvidedInterface> providedInterfaces = thePort.getProvided();
                if (providedInterfaces.size() > 0) {
                    if (requiredInterfaces.size() > 0) {
                        symbol.append(", ", styler);
                    }
                    symbol.append("P = ");
                    for (int i = 0; i < providedInterfaces.size(); i++) {
                        if (i > 0) {
                            symbol.append(", ", styler);
                        }
                        ProvidedInterface pi = providedInterfaces.get(i);
                        final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                        symbol.append(labelService.getLabel(pi, this.showVisibility).toString(), ElementStyler.getStyler(thePort, pi));
                    }
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468df6-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitProvidedInterface(ProvidedInterface theProvidedInterface) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theProvidedInterface);
            final List<Interface> providedElements = theProvidedInterface.getProvidedElement();
            
            if (providedElements.size() > 0) {
                for (int i = 0; i < providedElements.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ", styler);
                    }
                    Interface pe = providedElements.get(i);
                    final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                    symbol.append(labelService.getLabel(pe, this.showVisibility).toString(), ElementStyler.getStyler(theProvidedInterface, pe));
                }
            } else {
                symbol.append("none", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1468dfc-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitRaisedException(RaisedException theRaisedException) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theRaisedException);
            symbol.append("throws ", styler);
            
            final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            final Classifier thrownType = theRaisedException.getThrownType();
            
            if (thrownType != null) {
                symbol.append(labelService.getLabel(thrownType, this.showVisibility).toString(), ElementStyler.getStyler(theRaisedException, thrownType));
            
                final ModelTree owner = thrownType.getOwner();
                appendFrom(symbol, theRaisedException, owner, styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c148146f-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitRequiredInterface(RequiredInterface theRequiredInterface) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theRequiredInterface);
            final List<Interface> requiredElements = theRequiredInterface.getRequiredElement();
            
            if (requiredElements.size() > 0) {
                for (int i = 0; i < requiredElements.size(); i++) {
                    if (i > 0) {
                        symbol.append(", ", styler);
                    }
                    Interface re = requiredElements.get(i);
                    final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                    symbol.append(labelService.getLabel(re, this.showVisibility).toString(), ElementStyler.getStyler(theRequiredInterface, re));
                }
            } else {
                symbol.append("none", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1481475-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitSendSignalAction(SendSignalAction theSendSignalAction) {
            final StyledString symbol = new StyledString();
            
            final String sendSignalActionName = theSendSignalAction.getName();
            final Signal signal = theSendSignalAction.getSent();
            
            if (signal != null && (sendSignalActionName.equals("Unnamed") || sendSignalActionName.equals(""))) {
                symbol.append(signal.getName(), ElementStyler.getStyler(theSendSignalAction, signal));
            } else {
                symbol.append(sendSignalActionName, ElementStyler.getStyler(theSendSignalAction));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c148147b-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitTemplateBinding(TemplateBinding theTemplateBinding) {
            final StyledString symbol = new StyledString();
            Styler styler = ElementStyler.getStyler(theTemplateBinding);
            final NameSpace namespace = theTemplateBinding.getInstanciatedTemplate();
            if (namespace != null) {
                symbol.append(namespace.getName(), ElementStyler.getStyler(theTemplateBinding, namespace));
            } else {
                final Operation operation = theTemplateBinding.getInstanciatedTemplateOperation();
                if (operation != null) {
                    symbol.append(operation.getName(), ElementStyler.getStyler(theTemplateBinding, operation));
                }
            }
            
            symbol.append(" <", styler);
            
            final List<TemplateParameterSubstitution> substitutions = theTemplateBinding.getParameterSubstitution();
            for (int i = 0; i < substitutions.size(); i++) {
                if (i != 0) {
                    symbol.append(", ", styler);
                }
                TemplateParameterSubstitution ts = substitutions.get(i);
                final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                symbol.append(labelService.getLabel(ts, this.showVisibility).toString(), ElementStyler.getStyler(theTemplateBinding, ts));
            }
            
            symbol.append(" >", styler);
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1481481-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitTemplateParameter(TemplateParameter theTemplateParameter) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theTemplateParameter);
            symbol.append(theTemplateParameter.getName(), styler);
            
            final ModelElement type = theTemplateParameter.getType();
            
            if (type != null) {
                final Styler typeStyler = ElementStyler.getStyler(theTemplateParameter, type);
                if (theTemplateParameter.isIsValueParameter()) {
                    symbol.append(":", styler);
            
                    final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                    symbol.append(labelService.getLabel(type, this.showVisibility).toString(), typeStyler);
            
                    symbol.append(" expression", styler);
                } else {
                    boolean isConstrained = false;
            
                    if (type instanceof NameSpace) {
                        final NameSpace nsType = (NameSpace) type;
                        isConstrained = nsType.getSpecialization().size() > 0;
                    }
            
                    if (isConstrained) {
                        symbol.append(" > ", styler);
            
                        final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                        symbol.append(labelService.getLabel(type, this.showVisibility).toString(), typeStyler);
                    } else if (type.getClass() != Class.class || type.getExtension().size() != 0) {
                        symbol.append(" : ", styler);
                        symbol.append(type.getClass().getSimpleName().substring(2), styler);
                    }
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1481487-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitTemplateParameterSubstitution(TemplateParameterSubstitution theTemplateParameterSubstitution) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theTemplateParameterSubstitution);
            final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
            
            final TemplateParameter templateParameter = theTemplateParameterSubstitution.getFormalParameter();
            if (templateParameter != null) {
                symbol.append(labelService.getLabel(templateParameter, this.showVisibility).toString(), ElementStyler.getStyler(theTemplateParameterSubstitution, templateParameter));
            }
            
            final String value = theTemplateParameterSubstitution.getValue();
            if (!value.isEmpty()) {
                symbol.append("=", styler);
                symbol.append(value, styler);
            } else {
                symbol.append("=", styler);
                final ModelElement actual = theTemplateParameterSubstitution.getActual();
                // Be careful if actual is equal to Owner TemplateBinding
                // because it will loop
                if (actual != null) {
                    Styler actualStyler = ElementStyler.getStyler(theTemplateParameterSubstitution, actual);
                    if (actual.equals(theTemplateParameterSubstitution.getOwner())) {
                        symbol.append(actual.getName(), actualStyler);
                        // Prevent infinite loop
                    } else {
                        symbol.append(labelService.getLabel(actual, this.showVisibility).toString(), actualStyler);
                    }
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c148148d-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitTransition(Transition theTransition) {
            final StyledString symbol = new StyledString();
            
            // IStateVertex sourceVertex = theTransition.getSource();
            final StateVertex targetVertex = theTransition.getTarget();
            final boolean withEvent = true;
            
            final Styler styler = ElementStyler.getStyler(theTransition);
            
            // Trigger
            final Event trigger = theTransition.getTrigger();
            if (trigger != null) {
                symbol.append(trigger.getName(), styler);
            } else {
                symbol.append(theTransition.getReceivedEvents(), styler);
            }
            
            // Guard condition
            final String condition = theTransition.getGuard();
            if (condition != null && !condition.equals("")) {
                symbol.append("[", styler);
                symbol.append(condition, styler);
                symbol.append("]", styler);
            }
            
            // Action
            final Operation operation = theTransition.getProcessed();
            if (operation != null) {
                symbol.append("/", styler);
                symbol.append(operation.getName(), styler);
                symbol.append("()", styler);
            } else {
                final String effect = theTransition.getEffect();
                if (effect.length() > 0) {
                    symbol.append("/", styler);
                    symbol.append(effect, styler);
                }
            }
            
            // SentEvent
            final Signal effects = theTransition.getEffects();
            if (effects != null && withEvent) {
                symbol.append("^", styler);
                symbol.append(effects.getName(), styler);
                symbol.append("()", styler);
            } else {
                final String sentEvents = theTransition.getSentEvents();
                if (sentEvents.length() > 0) {
                    symbol.append("^", styler);
                    symbol.append(sentEvents, styler);
                }
            }
            
            // postGard
            final String postCondition = theTransition.getPostCondition();
            if (postCondition != null && !postCondition.equals("")) {
                symbol.append("{", styler);
                symbol.append(postCondition, styler);
                symbol.append("}", styler);
            }
            
            if (symbol.length() == 0) {
                if (targetVertex instanceof AbstractPseudoState) {
                    final AbstractPseudoState target = ((AbstractPseudoState) targetVertex);
                    final BrowserLabelService labelService = new BrowserLabelService(this.elementStack);
                    symbol.append(labelService.getLabel(target, this.showVisibility).toString(), styler);
                } else {
                    symbol.append(theTransition.getName(), styler);
                    if (targetVertex != null) {
                        symbol.append("::", styler);
                        symbol.append(targetVertex.getName(), styler);
                    }
                }
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1481493-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitUsage(Usage theUsage) {
            final ModelElement destination = theUsage.getDependsOn();
            return visitDependencyLikeObject(theUsage, "uses", destination);
        }

        @objid ("c1499b34-d63b-11e1-9955-002564c97630")
        @Override
        public Object visitUseCaseDependency(UseCaseDependency theUseCaseDependency) {
            final StyledString symbol = new StyledString();
            final List<Stereotype> stereotypes = theUseCaseDependency.getExtension();
            
            if (stereotypes.size() > 0) {
                final Styler stylerStereotypes = ElementStyler.getStyler(theUseCaseDependency, stereotypes.get(0));
                symbol.append("<<", stylerStereotypes);
                symbol.append(stereotypes.get(0).getName(), stylerStereotypes);
                symbol.append(">>", stylerStereotypes);
            }
            
            final UseCase target = theUseCaseDependency.getTarget();
            
            if (target != null) {
                symbol.append(target.getName(), ElementStyler.getStyler(theUseCaseDependency, target));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("c1481499-d63b-11e1-9955-002564c97630")
        private static StringBuilder getAssociationEndMultiplicity(AssociationEnd theAssociationEnd) {
            final StringBuilder multiplicity = new StringBuilder();
            
            final String multiplicityMinStr = theAssociationEnd.getMultiplicityMin();
            final String multiplicityMaxStr = theAssociationEnd.getMultiplicityMax();
            String separator = "";
            
            if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
                multiplicity.append(" [");
            
                if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                    multiplicity.append(multiplicityMinStr);
                } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                    multiplicity.append("*");
                } else {
                    if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                        separator = "..";
                    }
            
                    multiplicity.append(multiplicityMinStr);
                    multiplicity.append(separator);
                    multiplicity.append(multiplicityMaxStr);
                }
                multiplicity.append("]");
            }
            return multiplicity;
        }

        @objid ("c1499b0d-d63b-11e1-9955-002564c97630")
        private static StringBuilder getAttributeMultiplicity(Attribute theAttribute) {
            final StringBuilder multiplicity = new StringBuilder();
            
            final String multiplicityMinStr = theAttribute.getMultiplicityMin();
            final String multiplicityMaxStr = theAttribute.getMultiplicityMax();
            String separator = "";
            
            if (multiplicityMinStr.equals("1") && multiplicityMaxStr.equals("1")) {
                return multiplicity;
            }
            
            if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
                multiplicity.append(" [");
            
                if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                    multiplicity.append(multiplicityMinStr);
                } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                    multiplicity.append("*");
                } else {
                    if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                        separator = "..";
                    }
            
                    multiplicity.append(multiplicityMinStr);
                    multiplicity.append(separator);
                    multiplicity.append(multiplicityMaxStr);
                }
                multiplicity.append("]");
            }
            return multiplicity;
        }

        @objid ("48eaf5dc-f40d-4707-9419-495caec4ed77")
        private static StringBuilder getDependencyVerb(ModelElement dep, String defaultVerb) {
            StringBuilder stringBuilder = new StringBuilder();
            if ( ! (dep.getExtension().isEmpty())) {
                for (Stereotype v : dep.getExtension()) {
                    stringBuilder.append(ModuleI18NService.getLabel(v));
            
                    stringBuilder.append(", ");
                }
                // remove last ", "
                stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length());
            } else {
                stringBuilder.append(defaultVerb);
                /*if (dep instanceof Usage)
                    stringBuilder.append("uses");
                else if (dep instanceof ElementRealization)
                    stringBuilder.append("realizes");
                else if (dep instanceof Abstraction)
                    stringBuilder.append("abstracts");
                else
                    stringBuilder.append("depends on");*/
            }
            return stringBuilder;
        }

        @objid ("0c96c88b-c64a-430f-9635-6894b6271d77")
        private static StringBuilder getNaryAssociationEndMultiplicity(NaryAssociationEnd theAssociationEnd) {
            final StringBuilder multiplicity = new StringBuilder();
            
            final String multiplicityMinStr = theAssociationEnd.getMultiplicityMin();
            final String multiplicityMaxStr = theAssociationEnd.getMultiplicityMax();
            String separator = "";
            
            if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
                multiplicity.append(" [");
            
                if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                    multiplicity.append(multiplicityMinStr);
                } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                    multiplicity.append("*");
                } else {
                    if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                        separator = "..";
                    }
            
                    multiplicity.append(multiplicityMinStr);
                    multiplicity.append(separator);
                    multiplicity.append(multiplicityMaxStr);
                }
                multiplicity.append("]");
            }
            return multiplicity;
        }

        @objid ("c1499b12-d63b-11e1-9955-002564c97630")
        private static StringBuilder getParameterMultiplicity(Parameter theParameter) {
            final StringBuilder multiplicity = new StringBuilder();
            
            final String multiplicityMinStr = theParameter.getMultiplicityMin();
            final String multiplicityMaxStr = theParameter.getMultiplicityMax();
            String separator = "";
            
            if (multiplicityMinStr.equals("1") && multiplicityMaxStr.equals("1")) {
                return multiplicity;
            }
            
            if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
                multiplicity.append(" [");
                if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                    multiplicity.append(multiplicityMinStr);
                } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                    multiplicity.append("*");
                } else {
                    if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                        separator = "..";
                    }
            
                    multiplicity.append(multiplicityMinStr);
                    multiplicity.append(separator);
                    multiplicity.append(multiplicityMaxStr);
                }
                multiplicity.append("]");
            }
            return multiplicity;
        }

        @objid ("c1499b17-d63b-11e1-9955-002564c97630")
        private static StyledString getParameterSymbol(Parameter theParameter, Styler styler, Operation fromOperation) {
            final StyledString symbol = new StyledString();
            
            final PassingMode passingMode = theParameter.getParameterPassing();
            
            final GeneralClass type = theParameter.getType();
            
            if (theParameter.getReturned() != null) {
                symbol.append("out", styler);
            
                symbol.append(" : ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(theParameter, type));
                } else {
                    symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
                }
            
                symbol.append(getParameterMultiplicity(theParameter).toString(), styler);
            } else if (theParameter.getComposed() != null) {
                symbol.append(theParameter.getName(), styler);
                symbol.append(" ", styler);
            
                if (passingMode == PassingMode.IN) {
                    symbol.append("in", styler);
                }
                if (passingMode == PassingMode.OUT) {
                    symbol.append("out", styler);
                }
                if (passingMode == PassingMode.INOUT) {
                    symbol.append("inout", styler);
                }
            
                symbol.append(" : ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(fromOperation==null?theParameter:fromOperation, type));
                } else {
                    symbol.append(ModelBrowser.I18N.getString("NoType"), styler);
                }
            
                symbol.append(getParameterMultiplicity(theParameter).toString(), styler);
            }
            return symbol;
        }

        @objid ("c1499b2a-d63b-11e1-9955-002564c97630")
        private static NameSpace getType(Port thePort) {
            final ModelElement represented = thePort.getRepresentedFeature();
            
            if (represented == null) {
                return thePort.getBase();
            } else if (hasTypeCycles(thePort)) {
                return null;
            } else if (represented instanceof Attribute) {
                return ((Attribute) represented).getType();
            } else if (represented instanceof AssociationEnd) {
                return ((AssociationEnd) represented).getTarget();
            } else if (represented instanceof Instance) {
                return ((Instance) represented).getBase();
            } else if (represented instanceof Parameter) {
                return ((Parameter) represented).getType();
            }
            return null;
        }

        @objid ("c14b21b1-d63b-11e1-9955-002564c97630")
        private String getVisibilitySymbol(VisibilityMode v) {
            if (this.showVisibility) {
                switch (v) {
                case PUBLIC:
                    return "+";
                case PROTECTED:
                    return "#";
                case PRIVATE:
                    return "-";
                case PACKAGEVISIBILITY:
                    return "\u2248";
                case VISIBILITYUNDEFINED:
                    return "";
                default:
                }
            }
            return "";
        }

        @objid ("c1499b2f-d63b-11e1-9955-002564c97630")
        private static boolean hasTypeCycles(Port thePort) {
            BindableInstance currentInstance = thePort;
            boolean hasCycle = false;
            
            while (currentInstance != null && (!hasCycle)) {
                final ModelElement currentRepresented = currentInstance.getRepresentedFeature();
                if (currentRepresented != null && currentRepresented instanceof BindableInstance) {
                    currentInstance = (BindableInstance) currentRepresented;
                    if (thePort.equals(currentInstance)) {
                        hasCycle = true;
                    }
                } else {
                    currentInstance = null;
                }
            
            }
            return hasCycle;
        }

        @objid ("2f696caa-b325-40d6-a497-60b09b4aa466")
        private Object visitDependencyLikeObject(ModelElement theDependency, String mmverb, ModelElement destination) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theDependency);
            final StringBuilder verb = getDependencyVerb(theDependency, mmverb).append(" ");
            
            if (destination != null) {
                symbol.append(verb.toString(), styler);
            
                StyledString destLabel = new BrowserLabelService(this.elementStack).getLabel(destination, false);
                symbol.append(destLabel.getString(), ElementStyler.getStyler(theDependency, destination));
            
                ModelTree owner = null;
            
                if (destination instanceof ModelTree) {
                    owner = ((ModelTree) destination).getOwner();
                    appendFrom(symbol, theDependency, owner, styler);
                }
            
            } else {
                symbol.append(verb.toString(), styler);
                symbol.append("<No destination>", styler);
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

        @objid ("db5b172f-07f0-4dc0-a537-da925c8eb6af")
        @Override
        public Object visitAbstraction(Abstraction obj) {
            final ModelElement destination = obj.getDependsOn();
            return visitDependencyLikeObject(obj, "abstracts", destination);
        }

        @objid ("00fbab75-dd87-48f2-8207-d6dd633682d6")
        @Override
        public Object visitElementRealization(ElementRealization obj) {
            final ModelElement destination = obj.getDependsOn();
            return visitDependencyLikeObject(obj, "realizes", destination);
        }

        @objid ("90ff28da-ddbc-4599-be23-d2c1c7eeb4e9")
        @Override
        public Object visitSubstitution(Substitution obj) {
            final ModelElement destination = obj.getContract();
            return visitDependencyLikeObject(obj, "substitute", destination);
        }

        @objid ("329f2653-96f2-4bbc-a084-b874cf2f6e65")
        @Override
        public Object visitComponentRealization(ComponentRealization obj) {
            final ModelElement destination = obj.getAbstraction();
            return visitDependencyLikeObject(obj, "realizes", destination);
        }

        /**
         * Append <code>"(from xxxx)"</code> to the symbol
         * @param symbol the symbol to modify
         * @param srcObj the source object, used to compute the style of <code>'xxxx'</code>
         * @param owner the object to display in <code>'xxxx'</code>
         * @param styler the style of <code>"(from "</code>
         */
        @objid ("7787d580-79b5-418a-8cee-5eee60f8e05c")
        private void appendFrom(final StyledString symbol, ModelElement srcObj, ModelTree owner, final Styler styler) {
            if (owner != null) {
                symbol.append(" (from ", styler);
                symbol.append(owner.getName(), ElementStyler.getStyler(srcObj, owner));
                symbol.append(")", styler);
            }
        }

        @objid ("ef2fd1be-7fd7-4f79-87fe-2473e5d23359")
        @Override
        public Object visitBpmnLane(BpmnLane obj) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(obj);
            
            ModelElement represented = obj.getPartitionElement();
            symbol.append(obj.getName(), styler);
            
            if (represented != null) {
                symbol.append(": ", styler);
                symbol.append(represented.getName(), ElementStyler.getStyler(obj, represented));
            }
            
            this.elementStyledLabel = symbol;
            return null;
        }

    }

}
