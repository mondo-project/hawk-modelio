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
                                    

package org.modelio.diagram.api.dg.drawings.common;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramDrawing;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.dg.IDiagramLayer;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.api.services.DiagramAbstractLink;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.model.IGmPath;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLayer;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLink;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLinkable;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class represents drawing links.
 */
@objid ("48149ebb-d343-45e5-ac56-65c5a61c7630")
public class DiagramDrawingLinkDG extends DiagramAbstractLink implements IDiagramDrawing {
    @objid ("b2ac457b-5b05-4c67-8d72-f58b209286da")
    private IGmDrawingLink gmLink;

    @objid ("5fd9c5fd-1ff2-42d0-b7ff-6d0d6b1d9877")
    @Override
    protected IGmPath getModelPath() {
        return this.gmLink.getPath();
    }

    @objid ("ad7a8b14-b164-4dc7-8c93-bfcba585447f")
    @Override
    public IDiagramGraphic getFrom() {
        final IGmDrawingLinkable from = this.gmLink.getFrom();
        
        if (from instanceof IGmDrawingLayer)
            return null;
        
        IDiagramGraphic ret = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, from);
        return ret;
    }

    @objid ("2a0d1cb9-a8e0-4d47-8e2b-19c4fb751dde")
    @Override
    public IDiagramGraphic getTo() {
        final IGmDrawingLinkable to = this.gmLink.getTo();
        
        if (to instanceof IGmDrawingLayer)
            return null;
        
        IDiagramGraphic ret = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, to);
        return ret;
    }

    @objid ("166aeea7-b980-4ce2-947f-ebb4a7eca277")
    @Override
    public MObject getElement() {
        return null;
    }

    @objid ("949f4d28-9f79-4eb9-bd6e-a24e6f0217c4")
    @Override
    public List<IDiagramLink> getFromLinks() {
        return Collections.emptyList();
    }

    @objid ("6b8a8a0d-5f90-4618-bf3e-dd3d8fd07352")
    @Override
    public List<IDiagramLink> getToLinks() {
        return Collections.emptyList();
    }

    @objid ("abebe093-1850-4279-a87c-f823425de974")
    @Override
    public IGmObject getModel() {
        return this.gmLink;
    }

    @objid ("3d689f0e-3f6d-44c2-8ab7-a786ade0d1dd")
    @Override
    public String getName() {
        return this.gmLink.toString();
    }

    /**
     * Creates a drawing link.
     * @param diagramHandle the diagram handle
     * @param gmLink the drawing link model
     */
    @objid ("f7f7aefc-a5f9-4c39-afbb-11c92b342bec")
    public DiagramDrawingLinkDG(DiagramHandle diagramHandle, IGmDrawingLink gmLink) {
        super(diagramHandle);
        this.gmLink = gmLink;
    }

    @objid ("28699ac5-2d6d-4d61-8d22-47bcfcecf30e")
    @Override
    public IDiagramLayer getLayer() {
        return DGFactory.getInstance().getDiagramLayer(this.diagramHandle, this.gmLink.getLayer());
    }

    @objid ("af4401a4-a94e-4928-acab-9af88ed400f0")
    @Override
    public void moveToLayer(IDiagramLayer newLayer) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Links belong to the source node layer.");
    }

}
