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
                                    

package org.modelio.diagram.editor.bpmn.elements.diagrams.subprocess;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.diagrams.GmBpmnDiagramStyleKeys;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnBaseElement;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the Gm of a BpmnProcessCollaborationDiagram.
 */
@objid ("61fddea1-55b6-11e2-877f-002564c97630")
public class GmBpmnSubProcessDiagram extends GmAbstractDiagram {
    @objid ("61fddea6-55b6-11e2-877f-002564c97630")
    private BpmnSubProcessDiagram obDiagram;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("61fddea9-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("61fddeac-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("61fddea5-55b6-11e2-877f-002564c97630")
    private static GmBpmnDiagramStyleKeys STYLEKEYS = new GmBpmnDiagramStyleKeys();

    /**
     * Default constructor.
     * @param manager the manager needed make the link between the Ob and Gm models.
     * @param diagram the diagram itself.
     * @param diagramRef a reference to the diagram.
     */
    @objid ("61fddeae-55b6-11e2-877f-002564c97630")
    public GmBpmnSubProcessDiagram(final ModelManager manager, final BpmnSubProcessDiagram diagram, final MRef diagramRef) {
        super(manager, diagramRef);
        this.obDiagram = diagram;
    }

    /**
     * Empty constructor needed for the (de-)serialization.
     */
    @objid ("61fddebd-55b6-11e2-877f-002564c97630")
    public GmBpmnSubProcessDiagram() {
        // empty constructor for the serialization
    }

    @objid ("61fddec0-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(final Class<? extends MObject> type) {
        return acceptMetaclass(type);
    }

    @objid ("61fddec9-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(final MObject el) {
        return el != null && acceptMetaclass(el.getClass());
    }

    @objid ("61fdded2-55b6-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(final Class<? extends MObject> metaclass) {
        if (acceptMetaclass(metaclass))
            return this;
        return null;
    }

    @objid ("61ff653b-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("61ff6542-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(final MetaKey metakey) {
        return STYLEKEYS.getStyleKey(metakey);
    }

    @objid ("61ff654d-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STYLEKEYS.getStyleKeys();
    }

    @objid ("61ff6556-55b6-11e2-877f-002564c97630")
    @Override
    public void read(final IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnSubProcessDiagram." + MINOR_VERSION_PROPERTY);
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

    /**
     * Returns true if the given metaclass is supported.
     * @param metaclass
     * @return true if the given metaclass is supported.
     */
    @objid ("61ff655d-55b6-11e2-877f-002564c97630")
    private boolean acceptMetaclass(final Class<? extends MObject> metaclass) {
        if (BpmnProcess.class.isAssignableFrom(metaclass)) {
            return false;
        }
        if (Dependency.class.isAssignableFrom(metaclass) ||
            AbstractDiagram.class.isAssignableFrom(metaclass) ||
            Constraint.class.isAssignableFrom(metaclass) ||
            ExternDocument.class.isAssignableFrom(metaclass) ||
            Note.class.isAssignableFrom(metaclass)) {
            return true;
        }
        return (BpmnBaseElement.class.isAssignableFrom(metaclass));
    }

    @objid ("61ff6566-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.obDiagram;
    }

    @objid ("61ff656d-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.obDiagram;
    }

    @objid ("61ff6574-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnSubProcessDiagram." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("61ff657a-55b6-11e2-877f-002564c97630")
    private void read_0(final IDiagramReader in) {
        super.read(in);
        this.obDiagram = (BpmnSubProcessDiagram) resolveRef(this.getRepresentedRef());
    }

    @objid ("6200ebdd-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
