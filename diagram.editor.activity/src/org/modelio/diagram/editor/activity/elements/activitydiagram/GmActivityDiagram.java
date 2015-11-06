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
                                    

package org.modelio.diagram.editor.activity.elements.activitydiagram;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CentralBufferNode;
import org.modelio.metamodel.uml.behavior.activityModel.ControlNode;
import org.modelio.metamodel.uml.behavior.activityModel.DataStoreNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExceptionHandler;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.InterruptibleActivityRegion;
import org.modelio.metamodel.uml.behavior.activityModel.Pin;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the Gm of a ActivityDiagram.
 */
@objid ("2991e69e-55b6-11e2-877f-002564c97630")
public class GmActivityDiagram extends GmAbstractDiagram {
    @objid ("29936cfb-55b6-11e2-877f-002564c97630")
    private ActivityDiagram obDiagram;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("29936cfe-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("29936d01-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("2f75ac9c-58a2-11e2-9574-002564c97630")
    private static GmActivityDiagramStyleKeys STYLEKEYS = new GmActivityDiagramStyleKeys();

    /**
     * Default constructor.
     * @param manager the manager needed make the link between the Ob and Gm models.
     * @param theActivityDiagram the diagram itself.
     * @param diagramRef a reference to the diagram.
     */
    @objid ("29936d03-55b6-11e2-877f-002564c97630")
    public GmActivityDiagram(ModelManager manager, ActivityDiagram theActivityDiagram, MRef diagramRef) {
        super(manager, diagramRef);
        this.obDiagram = theActivityDiagram;
    }

    /**
     * Empty constructor needed for the (de-)serialization.
     */
    @objid ("29936d0f-55b6-11e2-877f-002564c97630")
    public GmActivityDiagram() {
        // empty constructor for the serialization
    }

    @objid ("29936d12-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return acceptCreateMetaclass(type);
    }

    @objid ("29936d1a-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return el != null && acceptUnmaskMetaclass(el.getClass());
    }

    @objid ("29936d22-55b6-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        if (acceptCreateMetaclass(metaclass))
            return this;
        // else
        return null;
    }

    @objid ("29936d2c-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("29936d33-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STYLEKEYS.getStyleKey(metakey);
    }

    @objid ("2994f39d-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STYLEKEYS.getStyleKeys();
    }

    @objid ("2994f3a6-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmActivityDiagram." + MINOR_VERSION_PROPERTY);
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
    @objid ("2994f3ac-55b6-11e2-877f-002564c97630")
    private boolean acceptCreateMetaclass(Class<? extends MObject> metaclass) {
        return (ActivityPartition.class.isAssignableFrom(metaclass) ||
                ActivityAction.class.isAssignableFrom(metaclass) ||
                ControlNode.class.isAssignableFrom(metaclass) ||
                ActivityParameterNode.class.isAssignableFrom(metaclass) ||
                CentralBufferNode.class.isAssignableFrom(metaclass) ||
                DataStoreNode.class.isAssignableFrom(metaclass) ||
                InstanceNode.class.isAssignableFrom(metaclass) ||
                InterruptibleActivityRegion.class.isAssignableFrom(metaclass) ||
                ActivityEdge.class.isAssignableFrom(metaclass) ||
                ExceptionHandler.class.isAssignableFrom(metaclass) ||
                Dependency.class.isAssignableFrom(metaclass) ||
                ExternDocument.class.isAssignableFrom(metaclass) ||
                Note.class.isAssignableFrom(metaclass) ||
                Constraint.class.isAssignableFrom(metaclass));
    }

    @objid ("2994f3b4-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.obDiagram;
    }

    @objid ("2994f3bb-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    /**
     * Returns true if the given metaclass is supported.
     * @param metaclass
     * @return true if the given metaclass is supported.
     */
    @objid ("2994f3c2-55b6-11e2-877f-002564c97630")
    private boolean acceptUnmaskMetaclass(final Class<? extends MObject> metaclass) {
        return acceptCreateMetaclass(metaclass) ||
               Pin.class.isAssignableFrom(metaclass) ||
               ExpansionNode.class.isAssignableFrom(metaclass);
    }

    @objid ("2994f3cb-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmActivityDiagram." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("2994f3d1-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.obDiagram = (ActivityDiagram) resolveRef(this.getRepresentedRef());
    }

    @objid ("2994f3d6-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
