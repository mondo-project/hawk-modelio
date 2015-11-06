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
                                    

package org.modelio.diagram.elements.umlcommon.note;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.node.GmSimpleNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphical model for a {@link Note}.
 * 
 * @author phv
 */
@objid ("81819a59-1dec-11e2-8cad-001ec947c8cc")
public class GmNote extends GmSimpleNode {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("81819a5e-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("81865f0f-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("81819a5b-1dec-11e2-8cad-001ec947c8cc")
    private Note note;

    @objid ("81819a5c-1dec-11e2-8cad-001ec947c8cc")
     static final GmNoteStyleKeys KEYS = new GmNoteStyleKeys();

    /**
     * Constructor to use only for deserialization.
     */
    @objid ("81865f11-1dec-11e2-8cad-001ec947c8cc")
    public GmNote() {
    }

    /**
     * Creates a GmNote.
     * @param diagram The diagram owning the node
     * @param note The represented note element
     * @param ref The represented note reference
     */
    @objid ("81865f14-1dec-11e2-8cad-001ec947c8cc")
    public GmNote(GmAbstractDiagram diagram, Note note, MRef ref) {
        super(diagram, ref);
        this.note = note;
    }

    /**
     * @return the note content.
     */
    @objid ("81865f1a-1dec-11e2-8cad-001ec947c8cc")
    public String getContents() {
        if (this.note != null) {
            return this.note.getContent();
        } else {
            return "?";
        }
    }

    @objid ("81865f1e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public IEditableText getEditableText() {
        if (this.note == null)
            return null;
        return new IEditableText() {
            @Override
            public String getText() {
        return GmNote.this.getRepresentedElement().getContent();
                    }
                
                    @Override
                    public void setText(String text) {
        GmNote.this.getRepresentedElement().setContent(text);
                    }
                };
    }

    @objid ("81865f23-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Note getRepresentedElement() {
        return this.note;
    }

    @objid ("81865f28-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("81865f2d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return KEYS.getStyleKey(metakey);
    }

    @objid ("8188c169-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<StyleKey> getStyleKeys() {
        return KEYS.getStyleKeys();
    }

    /**
     * Get the note type label.
     * @return the note type label.
     */
    @objid ("8188c170-1dec-11e2-8cad-001ec947c8cc")
    public String getType() {
        if (this.note == null)
            return "?";
        
        final NoteType model = this.note.getModel();
        if (model == null)
            return "<none>";
        
        String label = ModuleI18NService.getLabel(model);
        return label.isEmpty() ? model.getName() : label;
    }

    @objid ("8188c175-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmNote." + MINOR_VERSION_PROPERTY);
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

    @objid ("8188c179-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void refreshFromObModel() {
        if (this.note != null) {
            firePropertyChange(PROPERTY_LABEL, null, this.note.getContent());
        }
    }

    @objid ("8188c17c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("8188c181-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void removeEndingLink(final IGmLink gmLink) {
        boolean selfDelete = false;
        if (getRelatedElement() != null && getRelatedElement().equals(gmLink.getRelatedElement())) {
            // the removed link represents the same element (the note) as this gm: delete self as well.
            selfDelete = true;
        }
        super.removeEndingLink(gmLink);
        if (selfDelete) {
            // the removed link represents the same element (the note) as this gm: delete self as well.
            delete();
        }
    }

    @objid ("8188c186-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNote." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("8188c18a-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.note = (Note) resolveRef(this.getRepresentedRef());
    }

    @objid ("8188c18d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
