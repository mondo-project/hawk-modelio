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
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.figures.IOrientableShaper.Orientation;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmNoStyleSimpleNode;
import org.modelio.diagram.elements.core.node.IImageableNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the Gm of a ExpansionNode.
 */
@objid ("2a56bd0d-55b6-11e2-877f-002564c97630")
public class GmExpansionNodePrimaryNode extends GmNoStyleSimpleNode implements IImageableNode {
    @objid ("2a56bd13-55b6-11e2-877f-002564c97630")
    private MObject element = null;

    @objid ("2a56bd16-55b6-11e2-877f-002564c97630")
    private Orientation orientation = Orientation.WestEast;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("2a56bd19-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("2a56bd1c-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Default C'tor.
     * @param diagram the diagram in which this Gm is created.
     * @param el the expansion node represented.
     * @param ref a reference to the represented expansion node.
     */
    @objid ("2a56bd1e-55b6-11e2-877f-002564c97630")
    public GmExpansionNodePrimaryNode(GmAbstractDiagram diagram, ExpansionNode el, MRef ref) {
        super(diagram, ref);
        this.element = el;
    }

    /**
     * Empty c'tor for deserialisation.
     */
    @objid ("2a56bd2a-55b6-11e2-877f-002564c97630")
    public GmExpansionNodePrimaryNode() {
        // empty for the serialization
    }

    @objid ("2a56bd2d-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    @objid ("2a56bd35-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    @objid ("2a58439a-55b6-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        return new IEditableText() {
        
            @Override
            public String getText() {
        return getRelatedElement().getName();
                    }
        
                    @Override
                    public void setText(String text) {
        getRelatedElement().setName(text);
                    }
        
                };
    }

    /**
     * Get the stereotype image to display.
     * @return the stereotype image to display. Must not be <i>null</i>.
     */
    @objid ("2a5843a1-55b6-11e2-877f-002564c97630")
    @Override
    public Image getImage() {
        return ElementImageService.getImage(getRelatedElement());
    }

    /**
     * @return the current orientation of the node.
     */
    @objid ("2a5843a7-55b6-11e2-877f-002564c97630")
    public Orientation getOrientation() {
        return this.orientation;
    }

    @objid ("2a5843ae-55b6-11e2-877f-002564c97630")
    @Override
    public ExpansionNode getRelatedElement() {
        return (ExpansionNode) super.getRelatedElement();
    }

    @objid ("2a5843b5-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        final StyleKey repModeKey = GmExpansionNode.STRUCTURED_KEYS.getStyleKey(MetaKey.REPMODE);
        return getStyle().getProperty(repModeKey);
    }

    @objid ("2a5843bc-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.element;
    }

    /**
     * @return the text to show.
     */
    @objid ("2a5843c3-55b6-11e2-877f-002564c97630")
    public String getText() {
        return getRelatedElement().getName();
    }

    @objid ("2a5843c8-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmExpansionNodePrimaryNode." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0(in);
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 0
            read_0(in);
            break;
        }
        }
    }

    @objid ("2a5843ce-55b6-11e2-877f-002564c97630")
    @Override
    public void write(final IDiagramWriter out) {
        super.write(out);
        out.writeProperty("orientation", this.orientation);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmExpansionNodePrimaryNode." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("2a5843d5-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        if (getRelatedElement() != null) {
            firePropertyChange(PROPERTY_LABEL, null, getRelatedElement().getName());
        }
        // forcing visual refresh in case Image changed 
        firePropertyChange(PROPERTY_LAYOUTDATA, null, getLayoutData());
    }

    /**
     * Sets the orientation of the node.
     * @param orientation the new orientation.
     */
    @objid ("2a59ca3a-55b6-11e2-877f-002564c97630")
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    @objid ("2a59ca40-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = resolveRef(this.getRepresentedRef());
        this.orientation = (Orientation) in.readProperty("orientation");
    }

    @objid ("2a59ca45-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
