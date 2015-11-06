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
                                    

package org.modelio.core.ui.images;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ConnectionPointReference;
import org.modelio.metamodel.uml.behavior.stateMachineModel.KindOfStateMachine;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Service to get an icon or an image for a model object.
 */
@objid ("004d5b20-5f0b-100d-835d-001ec947cd2a")
public class ElementImageService {
    @objid ("00691036-a3a1-100e-835d-001ec947cd2a")
    public static Image getIcon(MObject element) {
        // Only model elements can have stereotypes
        Image icon = element instanceof ModelElement ? getDisplayedStereotypeIcon((ModelElement) element) : null;
        if (icon != null) {
            return icon;
        }
        return MetamodelImageService.getIcon(element.getMClass(), getFlavor(element));
    }

    @objid ("006939f8-a3a1-100e-835d-001ec947cd2a")
    public static Image getImage(MObject element) {
        // Only model elements can have stereotypes
        Image icon = element instanceof ModelElement ? getDisplayedStereotypeImage((ModelElement) element) : null;
        if (icon != null) {
            return icon;
        }
        return MetamodelImageService.getImage(element.getMClass(), getFlavor(element));
    }

    /**
     * Returns the Stereotype to use as the object icon.
     * Choice is based on the current point of view which gives the highest
     * priority to the stereotypes of the module providing the current point of
     * view.
     * Same applies to getImage
     */
    @objid ("006959ec-a3a1-100e-835d-001ec947cd2a")
    private static Image getDisplayedStereotypeIcon(ModelElement modelElement) {
        for (Stereotype stereotype : modelElement.getExtension()) {
            if (stereotype.isValid() && stereotype.getIcon() != null && !stereotype.getIcon().isEmpty()) {
                ModuleComponent owner = getModuleOwner(stereotype);
                if (owner != null)
                    return ModuleI18NService.getIcon(owner, stereotype);
            }
        }
        return null;
    }

    @objid ("00699790-a3a1-100e-835d-001ec947cd2a")
    private static Image getDisplayedStereotypeImage(ModelElement modelElement) {
        for (Stereotype stereotype : modelElement.getExtension()) {
            if (stereotype.isValid() && stereotype.getImage() != null && !stereotype.getImage().isEmpty()) {
                ModuleComponent owner = getModuleOwner(stereotype);
                if (owner != null)
                    return ModuleI18NService.getImage(owner, stereotype);
            }
        }
        return null;
    }

    @objid ("0069b8b0-a3a1-100e-835d-001ec947cd2a")
    private static String getFlavor(MObject element) {
        return (String) element.accept(new ImageFlavorBuilder());
    }

    @objid ("849d195f-91f8-4a46-898c-b9ee4dac3567")
    private static ModuleComponent getModuleOwner(Stereotype stereotype) {
        Profile profile = stereotype.getOwner();
        if (profile == null) {
            CoreUi.LOG.warning(stereotype+" is orphan, it has no profile.");
            return null;
        }
        
        ModuleComponent owner = profile.getOwnerModule();
        if (owner == null)
            CoreUi.LOG.warning(profile+" is orphan, it has no module.");
        return owner;
    }

    /**
     * Visitor that computes an image key for a given {@link IElement}.
     */
    @objid ("8bfed586-1770-11e2-aa0d-002564c97630")
    protected static class ImageFlavorBuilder extends DefaultModelVisitor {
        @objid ("8c00d163-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            return null;
        }

        @objid ("8c00d169-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitParameter(Parameter theParameter) {
            if (theParameter.getReturned() != null) {
                return "return";
            } else if (theParameter.getComposed() != null) {
                return "io";
            } else {
                return null;
            }
        }

        @objid ("8c00f877-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitBehaviorParameter(BehaviorParameter theBehaviorParameter) {
            final Parameter theParameter = theBehaviorParameter.getMapped();
            if (theParameter != null) {
                return this.visitParameter(theParameter);
            } else {
                return this.visitElement(theBehaviorParameter);
            }
        }

        @objid ("8c011f86-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitStateMachine(StateMachine theStateMachine) {
            if (theStateMachine.getKind() == KindOfStateMachine.PROTOCOL) {
                return "protocol";
            } else {
                return this.visitBehavior(theStateMachine);
            }
        }

        @objid ("8c011f8b-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitAssociationEnd(AssociationEnd theAssociationEnd) {
            switch (theAssociationEnd.getAggregation()) {
                case KINDISAGGREGATION:
                    return "aggregation";
                case KINDISCOMPOSITION:
                    return "composition";
                case KINDISASSOCIATION:
                default:
                    return null;
            
            }
        }

        @objid ("8c01469a-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitExpansionNode(ExpansionNode theExpansionNode) {
            if (theExpansionNode.getRegionAsOutput() == null) {
                // Input expansion node
                return "inputelement";
            } else {
                // Output expansion node
                return "outputelement";
            }
        }

        @objid ("8c016da8-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitInformationFlow(InformationFlow theInformationFlow) {
            if (theInformationFlow.getRealizingActivityEdge().size() == 1 ||
                theInformationFlow.getRealizingFeature().size() == 1 ||
                theInformationFlow.getRealizingCommunicationMessage().size() == 1 ||
                theInformationFlow.getRealizingLink().size() == 1 ||
                theInformationFlow.getRealizingMessage().size() == 1) {
                // Input expansion node
                return "realizedinformationflow";
            } else {
                // Output expansion node
                return null;
            }
        }

        @objid ("8c016dad-1770-11e2-aa0d-002564c97630")
        @Override
        public Object visitConnectionPointReference(final ConnectionPointReference theConnectionPointReference) {
            if (theConnectionPointReference.getEntry() != null) {
                return "entry";
            } else if (theConnectionPointReference.getExit() != null) {
                return "exit";
            }
            return super.visitConnectionPointReference(theConnectionPointReference);
        }

    }

}
