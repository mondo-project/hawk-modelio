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
                                    

package org.modelio.diagram.editor.statik.elements.packaze;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.editor.statik.elements.namespacinglink.GmCompositionLink;
import org.modelio.diagram.editor.statik.elements.namespacinglink.redraw.RedrawCompositionLinkEditPolicy;
import org.modelio.diagram.elements.common.header.GmModelElementHeader;
import org.modelio.diagram.elements.common.simple.NonSelectableSimpleEditPart;
import org.modelio.diagram.elements.common.simple.SimpleFigure;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;

/**
 * Overloading of the {@link NonSelectableSimpleEditPart} to handle satellite children transfer back into body when
 * leaving simple mode.
 * 
 * @author fpoyer
 */
@objid ("3629aa91-55b7-11e2-877f-002564c97630")
public class PackageSimpleEditPart extends NonSelectableSimpleEditPart {
    @objid ("3629aa95-55b7-11e2-877f-002564c97630")
     static final int DEFAULT_HEIGHT = 50;

    @objid ("3629aa97-55b7-11e2-877f-002564c97630")
     static final int DEFAULT_WIDTH = 100;

    @objid ("3629aa99-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
        GmPackagePrimaryNode model = (GmPackagePrimaryNode) getModel();
        if (model.getRepresentationMode() == RepresentationMode.STRUCTURED) {
            // Start by cleaning all children that might have been created by the auto-unmask behaviors.            
            for (GmNodeModel mbodyChild : model.getBody().getChildren()) {
                mbodyChild.delete();
            }
            // Delete composition links.
            List<IGmLink> linksToDelete = new ArrayList<>();
            for (IGmLink link : model.getStartingLinks()) {
                if (link instanceof GmCompositionLink) {
                    linksToDelete.add(link);
                }
            }
            for (IGmLink link : linksToDelete) {
                link.delete();
            }
            // new representation mode is not SIMPLE, put back body content into body BEFORE the switch.
            for (GmNodeModel child : model.getParentNode().getChildren(GmPackage.BODY_CONTENT_AS_SATELLITE)) {
                model.getParentNode().removeChild(child);
                child.setRoleInComposition("");
                model.getBody().addChild(child);   
            }
            
        }
        super.refreshFromStyle(aFigure, style);
        if (model.getRepresentationMode() != RepresentationMode.SIMPLE) {
            GmPackage gmPackage = (GmPackage) model.getParentNode();
            GmCompositeNode ancestor = gmPackage.getParentNode();
            int index = ancestor.getChildIndex(gmPackage);
            // This will "delete" the current edit part.
            ancestor.removeChild(gmPackage);
            
            // This will invoke the ModelioEditPartFactory that will
            // create another edit part.
            ancestor.addChild(gmPackage, index);
            return;
        }
    }

    @objid ("3629aaa2-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // create the figure
           final PackageSimpleFigure aFigure = new PackageSimpleFigure();
           
           // set style independent properties
           final Dimension d = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
           aFigure.setPreferredSize(d);
           aFigure.setMinimumSize(d);
           
           // set style dependent properties
           refreshFromStyle(aFigure, getModelStyle());
           
           // return the figure
        return aFigure;
    }

    @objid ("362b30fa-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        // Add specific policy to handle requests to redraw composition links.
        installEditPolicy("RedrawCompositionLinkEditPolicy", new RedrawCompositionLinkEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimpleModeOwnedElementCreationEditPolicy());
    }

    @objid ("362b30fd-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        final GmPackagePrimaryNode model = (GmPackagePrimaryNode) this.getModel();
        final SimpleFigure aFigure = (SimpleFigure) this.getFigure();
        
        aFigure.getParent().setConstraint(aFigure, model.getLayoutData());
        GmModelElementHeader gmModelElementHeader = (GmModelElementHeader)model.getFirstChild("header");
        if (gmModelElementHeader != null) {
            gmModelElementHeader.obElementsUpdated();
            aFigure.setLabel(gmModelElementHeader.getMainLabel());
        }
    }

}
