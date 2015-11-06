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
                                    

package org.modelio.diagram.api.services;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.dg.IDiagramElementsLayer;
import org.modelio.api.diagram.dg.IDiagramLayer;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.model.IGmPath;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This class represents a DiagramGraphic of the 'link' kind.
 */
@objid ("df567469-24b7-4970-aff4-e17a2d721dc8")
public abstract class DiagramLink extends DiagramAbstractLink {
    @objid ("b2af7b8d-b460-428e-afba-351dc7e0adc3")
    private final IGmLink gmLink;

    /**
     * Creates a diagram link.
     * @param diagramHandle The diagram manipulation class.
     * @param gmLink The gm link represented by this class.
     */
    @objid ("8f8c8cd7-50d2-4d76-aab8-ecb706ec5465")
    public DiagramLink(DiagramHandle diagramHandle, IGmLink gmLink) {
        super(diagramHandle);
        this.gmLink = gmLink;
    }

    /**
     * @return the source DiagramGraphic of the current Link.
     */
    @objid ("08af9ed9-21aa-47ca-9075-d3ec79d3d097")
    @Override
    public IDiagramGraphic getFrom() {
        final IGmLinkable from = this.gmLink.getFrom();
        IDiagramGraphic ret = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, from);
        
        if (ret == null &&
                from instanceof GmNodeModel &&
                ((GmNodeModel) from).getParent() instanceof GmPortContainer) {
            ret = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle,
                                                            ((GmNodeModel) from).getParent());
        }
        return ret;
    }

    /**
     * @return the destination DiagramNode of the current Link.
     */
    @objid ("c84c3c0e-4d5d-4705-bca1-01b2463952cd")
    @Override
    public IDiagramGraphic getTo() {
        final IGmLinkable to = this.gmLink.getTo();
        IDiagramGraphic ret = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, to);
        
        if (ret == null &&
                to instanceof GmNodeModel &&
                ((GmNodeModel) to).getParent() instanceof GmPortContainer) {
            ret = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle,
                                                            ((GmNodeModel) to).getParent());
        }
        return ret;
    }

    @objid ("84da6f23-19cb-4ea3-8684-d99086ef2ab4")
    @Override
    public MObject getElement() {
        return this.gmLink.getRelatedElement();
    }

    @objid ("7c9dfb37-51fc-4319-b1c4-ea66d27fe531")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.gmLink == null) ? 0 : this.gmLink.hashCode());
        return result;
    }

    @objid ("b7ba14f2-6014-4057-8d93-17ad5e110a43")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DiagramLink)) {
            return false;
        }
        final DiagramLink other = (DiagramLink) obj;
        if (this.gmLink == null) {
            if (other.gmLink != null) {
                return false;
            }
        } else if (!this.gmLink.equals(other.gmLink)) {
            return false;
        }
        return true;
    }

    @objid ("66cdb72f-c571-4387-8acb-71148e219cdf")
    @Override
    public String getTextColor() {
        final StyleKey styleKey = this.gmLink.getStyleKey(MetaKey.TEXTCOLOR);
        if (styleKey == null) {
            return null;
        }
        final Color color = this.gmLink.getStyle().getProperty(styleKey);
        return StyleKeyTypeConverter.convertToString(styleKey, color);
    }

    @objid ("b6130dbd-4813-4fb8-89a0-18a23339caaa")
    @Override
    public void setFont(final String value) {
        final StyleKey styleKey = this.gmLink.getStyleKey(MetaKey.FONT);
        if (styleKey == null) {
            return;
        }
        this.gmLink.getStyle()
        .setProperty(styleKey, StyleKeyTypeConverter.convertFromString(styleKey, value));
    }

    /**
     * Return the links that are starting (ie outgoing links) from this node.
     * @return A list of links in any case, possibly an empty one. Never returns null
     */
    @objid ("31b051d0-60de-4e90-96c5-2cd43816b021")
    @Override
    public List<IDiagramLink> getFromLinks() {
        final List<IDiagramLink> links = new ArrayList<>();
        for (final IGmLink link : this.gmLink.getStartingLinks()) {
            IDiagramLink diagramLink = DGFactory.getInstance().getDiagramLink(this.diagramHandle, link);
            if (diagramLink != null) {
                links.add(diagramLink);
            }
        }
        return links;
    }

    /**
     * Return the links that are ending (ie incoming links) at this node.
     * @return A list of links in any case, possibly an empty one. Never returns null
     */
    @objid ("71d2d2f0-0043-4476-8455-261ebf6fc10e")
    @Override
    public List<IDiagramLink> getToLinks() {
        final List<IDiagramLink> links = new ArrayList<>();
        for (final IGmLink link : this.gmLink.getEndingLinks()) {
            IDiagramLink diagramLink = DGFactory.getInstance().getDiagramLink(this.diagramHandle, link);
            if (diagramLink != null) {
                links.add(diagramLink);
            }
        }
        return links;
    }

    /**
     * Return the name of this link.
     * @return the link name
     */
    @objid ("5a77b0d0-5e96-43e2-879c-0fe4701f7a4c")
    @Override
    public String getName() {
        return this.gmLink.getRelatedElement() != null ? this.gmLink.getRelatedElement().getName()
                : this.gmLink.getGhostLabel();
    }

    @objid ("82e6fac1-0d2b-42f7-b495-054138cd4529")
    @Override
    public MObject getHyperLink() {
        return null;
    }

    @objid ("d87d76cc-63e8-4120-a91b-07590511da12")
    @Override
    public void setHyperLink(MObject obj) {
        // ignore
    }

    @objid ("c93a808f-b02f-4d6c-8ffd-2615a82e8e63")
    @Override
    public IGmObject getModel() {
        return this.gmLink;
    }

    @objid ("b1bc484a-cb35-4dbd-9918-c28696bb0432")
    @Override
    protected IGmPath getModelPath() {
        return this.gmLink.getPath();
    }

    @objid ("499edf69-7165-4a61-9c84-e87dd161fd33")
    @Override
    public IDiagramLayer getLayer() {
        // All element links currently belong to the main layer
        return this.diagramHandle.getDiagramNode().getElementsLayer(IDiagramElementsLayer.MAIN);
    }

    @objid ("58ced370-52b2-40a0-b785-07e3e574756d")
    @Override
    public void moveToLayer(IDiagramLayer newLayer) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Model element links belong to the elements layer.");
    }

}
