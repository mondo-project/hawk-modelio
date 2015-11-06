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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.elements.core.model.GmAbstractObject;
import org.modelio.diagram.elements.core.model.ImageServices;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.IImageableNode;
import org.modelio.diagram.styles.core.IStyle;

/**
 * EditPart handling an Image with an label underneath it.
 * 
 * @author fpoyer
 */
@objid ("7e890c33-1dec-11e2-8cad-001ec947c8cc")
public class LabelledImageEditPart extends ImageEditPart {
    @objid ("7e890c35-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        // Create the figure
        ImageFigure fig = (ImageFigure) super.createFigure();
        Figure container = new Figure();
        ToolbarLayout manager = new ToolbarLayout(false);
        manager.setStretchMinorAxis(false);
        manager.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        container.setLayoutManager(manager);
        container.add(fig, null, 0);
        // set style independent properties
        fig.setTextAlignment(PositionConstants.CENTER);
        fig.setTextPlacement(PositionConstants.SOUTH);
        
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // return the figure
        return container;
    }

    @objid ("7e890c3c-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getModelChildren() {
        if (getModel() instanceof GmCompositeNode) {
            return (List<Object>) (Object) ((GmCompositeNode) getModel()).getVisibleChildren();
        } else {
            return Collections.emptyList();
        }
    }

    @objid ("7e890c42-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (!(aFigure instanceof ImageFigure)) {
            // bypass the container figure.
            super.refreshFromStyle((IFigure) aFigure.getChildren().get(0), style);
        }
    }

    @objid ("7e890c49-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        IImageableNode inode = (IImageableNode) this.getModel();
        ImageFigure fig = (ImageFigure) this.getFigure().getChildren().get(0);
        Image image = inode.getImage();
        if (image == null) {
            // Use default image.
            image = ImageServices.getNoImageImage();
        }
        fig.setImage(image);
        getFigure().getParent().setConstraint(getFigure(), ((GmAbstractObject) inode).getLayoutData());
    }

    @objid ("7e890c4c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        if (index == 0) {
            // label
            IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
            getContentPane().add(child, null, 1);
        }
    }

}
