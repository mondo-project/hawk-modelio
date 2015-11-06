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
                                    

package org.modelio.diagram.editor.activity.elements.expansionnode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.editor.activity.elements.figures.ExpansionNodeFigure;
import org.modelio.diagram.editor.activity.elements.policies.CreateFlowEditPolicy;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;

/**
 * EditPart for an ExpansionNode Node.
 * 
 * @author fpoyer
 */
@objid ("2a52291a-55b6-11e2-877f-002564c97630")
public class ExpansionNodeEditPart extends GmNodeEditPart {
    @objid ("2a52291e-55b6-11e2-877f-002564c97630")
     Label labelFigure;

    @objid ("2a52291f-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.NODE_ROLE, new CreateFlowEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("2a522922-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // create the figure
        ExpansionNodeFigure fig = new ExpansionNodeFigure();
        
        // set style independent properties
        fig.setPreferredSize(80, 15);
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // return the figure
        return fig;
    }

    @objid ("2a522927-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        GmExpansionNodePrimaryNode expansionnodeModel = (GmExpansionNodePrimaryNode) this.getModel();
        this.getFigure().getParent().setConstraint(this.getFigure(), expansionnodeModel.getLayoutData());
        refreshOrientation();
    }

    @objid ("2a52292a-55b6-11e2-877f-002564c97630")
    private void refreshOrientation() {
        this.getFigure().addLayoutListener(new PostLayoutUpdater(this));
        // Force invalidation to be sure that layout will be called (otherwise the listener might not be called or much too late).
        this.getFigure().invalidate();
    }

    @objid ("2a52292c-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    @objid ("2a522931-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (aFigure instanceof ExpansionNodeFigure) {
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
            }
        } else {
            super.refreshFromStyle(aFigure, style);
        }
    }

}
