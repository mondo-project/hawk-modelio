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
                                    

package org.modelio.diagram.elements.common.image;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.ImageServices;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.node.IImageableNode;
import org.modelio.diagram.elements.core.policies.SimpleModeDeferringCreateNodePolicy;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;

/**
 * Default edit part for handling a node in Image representation mode. An Image only. If a Label is needed, use
 * {@link LabelledImageEditPart} instead.
 * 
 * @author fpoyer
 */
@objid ("7e844780-1dec-11e2-8cad-001ec947c8cc")
public class ImageEditPart extends GmNodeEditPart {
    @objid ("7e844782-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (!switchRepresentationMode()) {
            super.refreshFromStyle(aFigure, style);
        }
    }

    @objid ("7e844789-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new SimpleModeDeferringCreateNodePolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("7e84478c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        IImageableNode inode = (IImageableNode) this.getModel();
        ImageFigure fig = (ImageFigure) this.getFigure();
        Image image = inode.getImage();
        if (image == null) {
            // Use default image
            image = ImageServices.getNoImageImage();
        }
        fig.setImage(image);
        fig.getParent().setConstraint(fig, ((GmNodeModel) inode).getLayoutData());
    }

    @objid ("7e84478f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // Create the figure
        ImageFigure fig = new ImageFigure();
        
        // set style independent properties
        //fig.setPreferredSize(48, 60);
        
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // Initialise image.
        IImageableNode inode = (IImageableNode) this.getModel();
        Image image = inode.getImage();
        if (image == null) {
            // No image provided by model, use default image.
            image = ImageServices.getNoImageImage();
        }
        fig.setImage(image);
        
        // return the figure
        return fig;
    }

    @objid ("7e86a9dd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<Object> getModelChildren() {
        return Collections.emptyList();
    }

}
