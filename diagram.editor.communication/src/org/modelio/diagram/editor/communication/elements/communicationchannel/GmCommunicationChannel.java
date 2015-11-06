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
                                    

package org.modelio.diagram.editor.communication.elements.communicationchannel;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.communication.elements.communicationmessage.GmCommunicationInvertedMessageArrow;
import org.modelio.diagram.editor.communication.elements.communicationmessage.GmCommunicationInvertedMessageGroup;
import org.modelio.diagram.editor.communication.elements.communicationmessage.GmCommunicationSentMessageArrow;
import org.modelio.diagram.editor.communication.elements.communicationmessage.GmCommunicationSentMessageGroup;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.extensions.GmFractionalConnectionLocator;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link CommunicationChannel}.
 * 
 * @author chm
 */
@objid ("7a21fc08-55b6-11e2-877f-002564c97630")
public class GmCommunicationChannel extends GmLink {
    @objid ("7a21fc0c-55b6-11e2-877f-002564c97630")
    private CommunicationChannel theCommunicationChannel;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7a21fc11-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("7a21fc14-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("0580ebe1-599a-11e2-ae45-002564c97630")
    private static final GmCommunicationChannelStyleKeys STYLEKEYS = new GmCommunicationChannelStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("7a21fc16-55b6-11e2-877f-002564c97630")
    public GmCommunicationChannel() {
    }

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param communicationchannel The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("7a21fc19-55b6-11e2-877f-002564c97630")
    public GmCommunicationChannel(GmAbstractDiagram diagram, MObject communicationchannel, MRef ref) {
        super(diagram, ref);
        
        this.theCommunicationChannel = (CommunicationChannel) communicationchannel;
        
        addExtension(ExtensionLocation.MiddleSE, new GmDefaultFlatHeader(diagram, ref));
        
        GmFractionalConnectionLocator constraint;
        constraint = new GmFractionalConnectionLocator(0.25, -10, -25);
        addExtension(new GmCommunicationSentMessageGroup(diagram, ref), constraint);
        constraint = new GmFractionalConnectionLocator(0.30, 0, 40, false);
        addExtension(new GmCommunicationSentMessageArrow(diagram, ref), constraint);
        
        constraint = new GmFractionalConnectionLocator(0.75, 20, 25);
        addExtension(new GmCommunicationInvertedMessageGroup(diagram, ref), constraint);
        constraint = new GmFractionalConnectionLocator(0.70, 0, 40, true);
        addExtension(new GmCommunicationInvertedMessageArrow(diagram, ref), constraint);
    }

    @objid ("7a21fc25-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.theCommunicationChannel;
    }

    @objid ("7a21fc2c-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STYLEKEYS.getStyleKey(metakey);
    }

    @objid ("7a21fc36-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STYLEKEYS.getStyleKeys();
    }

    @objid ("7a21fc3f-55b6-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        
        this.theCommunicationChannel = (CommunicationChannel) resolveRef(getRepresentedRef());
    }

    @objid ("7a23829d-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.theCommunicationChannel.getStart();
    }

    @objid ("7a2382a4-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.theCommunicationChannel.getEnd();
    }

    @objid ("7a2382ab-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return CommunicationMessage.class.isAssignableFrom(type);
    }

    @objid ("7a2382b3-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("7a2382ba-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmCommunicationChannel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7a2382c0-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
