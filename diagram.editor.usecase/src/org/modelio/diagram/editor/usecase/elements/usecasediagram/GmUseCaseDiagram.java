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
                                    

package org.modelio.diagram.editor.usecase.elements.usecasediagram;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.usecase.elements.system.GmSystem;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCase;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.NamedStyle;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("5e82a51a-55b7-11e2-877f-002564c97630")
public class GmUseCaseDiagram extends GmAbstractDiagram {
    @objid ("5e82a524-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("5e82a527-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("7b7a61bb-5eff-11e2-b9cc-001ec947c8cc")
    private StaticDiagram element;

    @objid ("7b7a61bc-5eff-11e2-b9cc-001ec947c8cc")
    private static final GmUseCaseDiagramStyleKeys STYLEKEYS = new GmUseCaseDiagramStyleKeys();

    @objid ("7b7a61be-5eff-11e2-b9cc-001ec947c8cc")
    private GmSystem system;

    @objid ("5e82a529-55b7-11e2-877f-002564c97630")
    public GmUseCaseDiagram(ModelManager manager, StaticDiagram theStateDiagram, MRef diagramRef) {
        super(manager, diagramRef);
        this.element = theStateDiagram;
        
        NamedStyle namedStyle = DiagramStyles.getStyleManager().getStyle(DiagramStyles.USECASE_STYLE_NAME);
        if (namedStyle != null) {
            this.getStyle().setCascadedStyle(namedStyle);
        }
    }

    @objid ("5e82a535-55b7-11e2-877f-002564c97630")
    public GmUseCaseDiagram() {
        // empty constructor for the serialization
    }

    @objid ("5e82a538-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return (Actor.class.isAssignableFrom(type)) || (UseCase.class.isAssignableFrom(type)) || (InformationItem.class.isAssignableFrom(type));
    }

    @objid ("5e82a540-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return true;
    }

    @objid ("5e842bbd-55b7-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        if (canCreate(metaclass))
            return this;
        //else
        return null;
    }

    @objid ("5e842bc7-55b7-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("5e842bce-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STYLEKEYS.getStyleKey(metakey);
    }

    @objid ("5e842bd8-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STYLEKEYS.getStyleKeys();
    }

    @objid ("5e842be1-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmUseCaseDiagram." + MINOR_VERSION_PROPERTY);
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

    @objid ("5e842be7-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.element;
    }

    @objid ("5e842bee-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("5e842bf5-55b7-11e2-877f-002564c97630")
    public GmSystem getSystem() {
        if (this.system == null) {
            this.system = new GmSystem(this, getRepresentedRef());
            addChild(getSystem());
        }
        return this.system;
    }

    @objid ("5e842bfa-55b7-11e2-877f-002564c97630")
    @Override
    public void addChild(final GmNodeModel child) {
        if (child instanceof GmUseCase &&
            ((Boolean) getStyle().getProperty(GmUseCaseDiagramStyleKeys.SHOW_SYSTEM)).booleanValue()) {
            MObject theUseCase = child.getRepresentedElement();
            GmSystem s = getSystem();
            if (theUseCase != null &&
                theUseCase.getCompositionOwner().equals(s.getRepresentedElement())) {
                s.addChild(child);
                return;
            }
        }
        // Child is not a Use case, 
        // or System is set "not visible" by the style, 
        // or use case doesn't belong to system's element
        super.addChild(child);
    }

    @objid ("5e85b25d-55b7-11e2-877f-002564c97630")
    @Override
    public void removeChild(final GmNodeModel child) {
        super.removeChild(child);
        if (child == this.system) {
            this.system = null;
        }
    }

    @objid ("5e85b264-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        super.styleChanged(changedStyle);
        
        if (getSystem() != null) {
            fireChildVisibilityChanged(getSystem());
        }
    }

    @objid ("5e85b26b-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        if (property == GmUseCaseDiagramStyleKeys.SHOW_SYSTEM) {
            if (Boolean.TRUE.equals(newValue)) {
                if (getSystem() != null) {
                    fireChildVisibilityChanged(getSystem());
                }
                super.styleChanged(property, newValue);
            } else {
                super.styleChanged(property, newValue);
                if (getSystem() != null) {
                    fireChildVisibilityChanged(getSystem());
                }
            }
        } else {
            super.styleChanged(property, newValue);
        }
    }

    @objid ("5e85b274-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmUseCaseDiagram." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("5e85b27a-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (StaticDiagram) resolveRef(this.getRepresentedRef());
        for (GmNodeModel child : getChildren()) {
            if (child instanceof GmSystem) {
                this.system = (GmSystem)child;
            }
        }
    }

    @objid ("5e85b27f-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
