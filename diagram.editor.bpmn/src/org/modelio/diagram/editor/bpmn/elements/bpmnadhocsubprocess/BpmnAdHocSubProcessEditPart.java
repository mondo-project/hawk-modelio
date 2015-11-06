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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnadhocsubprocess;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodefooter.GmBpmnNodeFooter;
import org.modelio.diagram.editor.bpmn.elements.bpmnsubprocess.BpmnSubProcessLinkEditPolicy;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnSubProcessStructuredStyleKeys;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.figures.RoundedBoxFigure;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DeferringCreateNodePolicy;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * EditPart for an SubProcess Node.
 */
@objid ("607a4bda-55b6-11e2-877f-002564c97630")
public class BpmnAdHocSubProcessEditPart extends GmNodeEditPart {
    @objid ("607a4bde-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // create the figure
        RoundedBoxFigure fig = new RoundedBoxFigure();
        fig.setLayoutManager(new BorderLayout());
        
        // set style independent properties
        fig.setPreferredSize(100, 60);
        fig.setRadius(5);
        
        // Required for CallActivity reprsentation
        fig.setLineWidth(1);
        
        fig.setLinePattern(LinePattern.LINE_SOLID);
        
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // return the figure
        return fig;
    }

    @objid ("607a4be3-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DeferringCreateNodePolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new BpmnSubProcessLinkEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("607a4be6-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        GmBpmnAdHocSubProcessPrimaryNode calloperationModel = (GmBpmnAdHocSubProcessPrimaryNode) this.getModel();
        this.getFigure().getParent().setConstraint(this.getFigure(), calloperationModel.getLayoutData());
        
        if (calloperationModel.getRelatedElement().isTriggeredByEvent()) {
            ((RoundedBoxFigure) this.getFigure()).setLinePattern(LinePattern.LINE_DOT);
        } else {
            ((RoundedBoxFigure) this.getFigure()).setLinePattern(LinePattern.LINE_SOLID);
        }
    }

    @objid ("607a4be9-55b6-11e2-877f-002564c97630")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        if (((GmNodeModel) childEditPart.getModel()).getRoleInComposition().equals("HEADER"))
            this.getFigure().add(child, BorderLayout.TOP, index);
        if (((GmNodeModel) childEditPart.getModel()).getRoleInComposition().equals("BODY")) {
            this.getFigure().add(child, BorderLayout.CENTER, index);
        }
        if (((GmNodeModel) childEditPart.getModel()).getRoleInComposition().equals("FOOTER")) {
            this.getFigure().add(child, BorderLayout.BOTTOM, index);
        }
    }

    @objid ("607a4bee-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    @objid ("607a4bf3-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (aFigure instanceof RoundedBoxFigure) {
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
            }
        }
        
        GmBpmnAdHocSubProcessPrimaryNode model = (GmBpmnAdHocSubProcessPrimaryNode) this.getModel();
        Boolean showcontent = getModelStyle().getProperty(GmBpmnSubProcessStructuredStyleKeys.SHOWCONTENT);
        GmBpmnNodeFooter gmBpmnNodeFooter = (GmBpmnNodeFooter) model.getFirstChild("FOOTER");
        if (gmBpmnNodeFooter != null) {
            if (showcontent) {
                gmBpmnNodeFooter.setEmptySubProcess(false);
                gmBpmnNodeFooter.setNonEmptySubProcess(true);
            } else {
                gmBpmnNodeFooter.setEmptySubProcess(true);
                gmBpmnNodeFooter.setNonEmptySubProcess(false);
            }
        }
    }

    @objid ("607bd27e-55b6-11e2-877f-002564c97630")
    @Override
    protected void reorderChild(final EditPart child, final int index) {
        removeChildVisual(child);
        List children = getChildren();
        children.remove(child);
        children.add(index, child);
        addChildVisual(child, index);
    }

}
