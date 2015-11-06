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
                                    

package org.modelio.diagram.editor.sequence.elements.interactionoperand;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPart;
import org.modelio.diagram.elements.common.portcontainer.PortContainerLayout;

/**
 * EditPart for InteractionOperand. Handles specificity of gates.
 * 
 * @author fpoyer
 */
@objid ("d906966c-55b6-11e2-877f-002564c97630")
public class InteractionOperandEditPart extends PortContainerEditPart {
    @objid ("d9069670-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // Override the layout to provide a version that doesn't shrink primary node if preferred bounds are not given...
        IFigure fig = super.createFigure();
        fig.setLayoutManager(new PortContainerLayout() {
            @Override
            public void layout(IFigure parent) {
                Point offset = getOrigin(parent);
                IFigure child;
                for (Object childObj : parent.getChildren()) {
                    child = (IFigure) childObj;
                    Object childConstraint = getConstraint(child);
                    if (child.equals(this.getMainNodeFigure())) {
                        // Main node _ALWAYS_ uses all available space.
                        childConstraint = parent.getBounds().getTranslated(offset.getNegated());
                    }
                    if (childConstraint == null) {
                        // no constraint for this child yet, ignore it completely
                        continue;
                    }
                    Rectangle bounds;
                    if (childConstraint instanceof Rectangle)
                        bounds = (Rectangle) childConstraint;
                    else
                        bounds = getCorrectedRectangle(parent, child, (PortConstraint) childConstraint);
        
                    if (bounds.width == -1 || bounds.height == -1) {
                        Dimension childPreferredSize = child.getPreferredSize(bounds.width, bounds.height);
                        bounds = bounds.getCopy();
                        if (bounds.width == -1)
                            bounds.width = childPreferredSize.width;
                        if (bounds.height == -1)
                            bounds.height = childPreferredSize.height;
                    }
                    bounds = bounds.getTranslated(offset);
                    child.setBounds(bounds);
                }
            }
        });
        return fig;
    }

}
