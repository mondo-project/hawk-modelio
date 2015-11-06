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
                                    

package org.modelio.diagram.elements.core.link;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.gproject.model.api.MTools;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command that moves the source of a GmLink to another location on the same node or another one.
 * 
 * @author cmarin
 */
@objid ("7feead19-1dec-11e2-8cad-001ec947c8cc")
public class DefaultReconnectSourceCommand extends Command {
    @objid ("7feead1d-1dec-11e2-8cad-001ec947c8cc")
    private final GmLink gmLink;

    @objid ("7feead1f-1dec-11e2-8cad-001ec947c8cc")
    private final IGmLinkable newSrcNode;

    @objid ("7feead21-1dec-11e2-8cad-001ec947c8cc")
    private Object anchorModel;

    /**
     * Create the command.
     * @param gmLink The link to move.
     * @param dest The new source node.
     */
    @objid ("7feead22-1dec-11e2-8cad-001ec947c8cc")
    public DefaultReconnectSourceCommand(GmLink gmLink, IGmLinkable dest) {
        this.gmLink = gmLink;
        this.newSrcNode = dest;
    }

    @objid ("7feead27-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canExecute() {
        // The diagram must be modifiable
        if (!MTools.getAuthTool().canModify(this.gmLink.getDiagram().getRelatedElement()))
            return false;
        
        // If the source changes, The old and new source and the link elements must be modifiable
        final IGmLinkable oldSrcNode = this.gmLink.getFrom();
        if (oldSrcNode == null) {
            return true;
        }
        if (this.newSrcNode.getRepresentedRef().equals(oldSrcNode.getRepresentedRef())) {
            return true;
        } else {
            // The old and new source and the link elements must be modifiable
            return isModifableElement(oldSrcNode) && isModifableElement(this.newSrcNode) && isModifableElement(this.gmLink);
        }
    }

    @objid ("7feead2b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        updateLinkSource();
        
        if (this.anchorModel != null) {
            this.gmLink.getPath().setSourceAnchor(this.anchorModel);
            this.gmLink.setLayoutData(new GmPath(this.gmLink.getPath()));
        }
    }

    /**
     * Set the model of the source anchor of the link.
     * @param anchorModel the model of the source anchor of the link
     */
    @objid ("7ff10f4b-1dec-11e2-8cad-001ec947c8cc")
    public void setAnchorModel(final Object anchorModel) {
        this.anchorModel = anchorModel;
    }

    @objid ("7ff10f50-1dec-11e2-8cad-001ec947c8cc")
    protected void updateLinkSource() {
        final IGmLinkable oldSourceNode = this.gmLink.getFrom();
        if (oldSourceNode != this.newSrcNode) {
            final MObject link = this.gmLink.getRelatedElement();
            final MObject newSource = this.newSrcNode.getRelatedElement();
            if (oldSourceNode != null) {
                final MObject oldSource = oldSourceNode.getRelatedElement();
                if (!newSource.equals(oldSource)) {
                    // Update Ob model
                    MTools.getModelTool().setSource(link, oldSource, newSource);
                }
        
                // Update gm model
                oldSourceNode.removeStartingLink(this.gmLink);
            } else {
                MTools.getModelTool().setSource(link, null, newSource);
            }
            this.newSrcNode.addStartingLink(this.gmLink);
        }
    }

    @objid ("7ff10f52-1dec-11e2-8cad-001ec947c8cc")
    private boolean isModifableElement(final IGmLinkable model) {
        MObject relatedElement = model.getRelatedElement();
        if (relatedElement != null && !relatedElement.isShell() && !relatedElement.isDeleted()) {
            return relatedElement.getStatus().isModifiable();
        }
        return true;
    }

}
