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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnnodefooter;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents the classifier header.
 * <p>
 * Contains for the moment the class name.<br>
 * Will contain in the future:<br>
 * - its visibility <br>
 * - tagged values <br>
 * - &lt;&lt;stereotypes names>> <br>
 * - a stereotype icons bar <br>
 */
@objid ("61748a67-55b6-11e2-877f-002564c97630")
public class GmBpmnNodeFooter extends GmCompositeNode {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("61748a6b-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("61748a6e-55b6-11e2-877f-002564c97630")
    private Boolean isEmptySubProcess;

    @objid ("61748a70-55b6-11e2-877f-002564c97630")
    private Boolean isLoop;

    @objid ("61748a72-55b6-11e2-877f-002564c97630")
    private Boolean isParallel;

    @objid ("617610fa-55b6-11e2-877f-002564c97630")
    private Boolean isSequential;

    @objid ("617610fc-55b6-11e2-877f-002564c97630")
    private Boolean isAdHoc;

    @objid ("617610fe-55b6-11e2-877f-002564c97630")
    private Boolean isCompensation;

    @objid ("61761100-55b6-11e2-877f-002564c97630")
    private Boolean isNonEmptySubProcess;

    @objid ("61761102-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Creates a classifier header
     * @param diagram the owning diagram.
     * @param relatedRef reference
     */
    @objid ("61761104-55b6-11e2-877f-002564c97630")
    public GmBpmnNodeFooter(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
        this.isEmptySubProcess = false;
        this.isNonEmptySubProcess = false;
        this.isLoop = false;
        this.isParallel = false;
        this.isSequential = false;
        this.isAdHoc = false;
        this.isCompensation = false;
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("6176110d-55b6-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (parent != null && getParent() != parent) {
            getStyle().setCascadedStyle(parent.getStyle());
        }
        
        super.setParent(parent);
    }

    /**
     * Nothing can be unmasked here.
     */
    @objid ("61761114-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    /**
     * Nothing can be created here.
     */
    @objid ("6176111d-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    /**
     * Delegates to the parent.
     */
    @objid ("61761126-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (getParent() == null)
            return null;
        return getParent().getStyleKey(metakey);
    }

    /**
     * The header does not have own style keys.
     */
    @objid ("61761131-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    /**
     * Delegates to the parent.
     */
    @objid ("6177979a-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        if (getParent() == null)
            return RepresentationMode.STRUCTURED;
        return getParent().getRepresentationMode();
    }

    /**
     * Empty c'tor for deserialisation.
     */
    @objid ("617797a2-55b6-11e2-877f-002564c97630")
    public GmBpmnNodeFooter() {
        // empty constructor for the serialization
    }

    @objid ("617797a5-55b6-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        return null;
    }

    @objid ("617797af-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnNodeFooter." + MINOR_VERSION_PROPERTY);
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

    @objid ("617797b5-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        out.writeProperty("EmptySubProcess", isEmptySubProcess());
        out.writeProperty("NonEmptySubProcess", isNonEmptySubProcess());
        out.writeProperty("Loop", isLoop());
        out.writeProperty("Parallel", isParallel());
        out.writeProperty("Sequential", isSequential());
        out.writeProperty("AdHoc", isAdHoc());
        out.writeProperty("Compensation", isCompensation());
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnNodeFooter." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("617797bb-55b6-11e2-877f-002564c97630")
    public Boolean isEmptySubProcess() {
        return this.isEmptySubProcess;
    }

    @objid ("617797c0-55b6-11e2-877f-002564c97630")
    public void setEmptySubProcess(Boolean isSubProcess) {
        this.isEmptySubProcess = isSubProcess;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isSubProcess);
    }

    @objid ("617797c4-55b6-11e2-877f-002564c97630")
    public Boolean isLoop() {
        return this.isLoop;
    }

    @objid ("617797c9-55b6-11e2-877f-002564c97630")
    public void setLoop(Boolean isLoop) {
        this.isLoop = isLoop;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isLoop);
    }

    @objid ("617797cd-55b6-11e2-877f-002564c97630")
    public Boolean isParallel() {
        return this.isParallel;
    }

    @objid ("617797d2-55b6-11e2-877f-002564c97630")
    public void setParallel(Boolean isParallel) {
        this.isParallel = isParallel;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isParallel);
    }

    @objid ("61791e39-55b6-11e2-877f-002564c97630")
    public Boolean isSequential() {
        return this.isSequential;
    }

    @objid ("61791e3e-55b6-11e2-877f-002564c97630")
    public void setSequential(Boolean isSequential) {
        this.isSequential = isSequential;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isSequential);
    }

    @objid ("61791e42-55b6-11e2-877f-002564c97630")
    public Boolean isAdHoc() {
        return this.isAdHoc;
    }

    @objid ("61791e47-55b6-11e2-877f-002564c97630")
    public void setAdHoc(Boolean isAdHoc) {
        this.isAdHoc = isAdHoc;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isAdHoc);
    }

    @objid ("61791e4b-55b6-11e2-877f-002564c97630")
    public Boolean isCompensation() {
        return this.isCompensation;
    }

    @objid ("61791e50-55b6-11e2-877f-002564c97630")
    public void setCompensation(Boolean isCompensation) {
        this.isCompensation = isCompensation;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isCompensation);
    }

    @objid ("61791e54-55b6-11e2-877f-002564c97630")
    public Boolean isNonEmptySubProcess() {
        return this.isNonEmptySubProcess;
    }

    @objid ("61791e59-55b6-11e2-877f-002564c97630")
    public void setNonEmptySubProcess(final Boolean isSubProcess) {
        this.isNonEmptySubProcess = isSubProcess;
        firePropertyChange(PROPERTY_LAYOUTDATA, null, isSubProcess);
    }

    @objid ("61791e5e-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        setEmptySubProcess((Boolean) in.readProperty("EmptySubProcess"));
        setNonEmptySubProcess((Boolean) in.readProperty("NonEmptySubProcess"));
        setLoop((Boolean) in.readProperty("Loop"));
        setParallel((Boolean) in.readProperty("Parallel"));
        setSequential((Boolean) in.readProperty("Sequential"));
        setAdHoc((Boolean) in.readProperty("AdHoc"));
        setCompensation((Boolean) in.readProperty("Compensation"));
    }

    @objid ("61791e63-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
