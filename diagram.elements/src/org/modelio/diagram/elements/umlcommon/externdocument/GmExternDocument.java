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
                                    

package org.modelio.diagram.elements.umlcommon.externdocument;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
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
import org.modelio.editors.richnote.api.RichNoteFormatRegistry;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphical model for a {@link ExternDocument}.
 */
@objid ("81544dee-1dec-11e2-8cad-001ec947c8cc")
public class GmExternDocument extends GmSimpleNode {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("81544df3-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("81544df6-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("81544df0-1dec-11e2-8cad-001ec947c8cc")
    private ExternDocument document;

    @objid ("81544df1-1dec-11e2-8cad-001ec947c8cc")
     static final GmExternDocumentStyleKeys KEYS = new GmExternDocumentStyleKeys();

    /**
     * Constructor to use only for deserialization.
     */
    @objid ("81544df8-1dec-11e2-8cad-001ec947c8cc")
    public GmExternDocument() {
    }

    /**
     * Creates a GmExternDocument.
     * @param diagram The diagram owning the node
     * @param document The represented note element
     * @param ref The represented note reference
     */
    @objid ("8156b005-1dec-11e2-8cad-001ec947c8cc")
    public GmExternDocument(final GmAbstractDiagram diagram, final ExternDocument document, final MRef ref) {
        super(diagram, ref);
        this.document = document;
    }

    /**
     * @return the external document name.
     */
    @objid ("8156b00e-1dec-11e2-8cad-001ec947c8cc")
    public String getName() {
        if (this.document != null) {
            return this.document.getName();
        } else {
            return "?";
        }
    }

    /**
     * @return the external document content.
     */
    @objid ("8156b012-1dec-11e2-8cad-001ec947c8cc")
    public String getContents() {
        if (this.document != null) {
            return this.document.getAbstract();
        } else {
            return "?";
        }
    }

    @objid ("8156b016-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public IEditableText getEditableText() {
        if (this.document == null)
            return null;
        return new IEditableText() {
            @Override
            public String getText() {
        return GmExternDocument.this.getRepresentedElement().getName();
                    }
        
                    @Override
                    public void setText(String text) {
        GmExternDocument.this.getRepresentedElement().setName(text);
                    }
                };
    }

    @objid ("8156b01b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ExternDocument getRepresentedElement() {
        return this.document;
    }

    @objid ("8156b020-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("8156b025-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public StyleKey getStyleKey(final MetaKey metakey) {
        return KEYS.getStyleKey(metakey);
    }

    @objid ("8156b02c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<StyleKey> getStyleKeys() {
        return KEYS.getStyleKeys();
    }

    /**
     * Get the extern document type label.
     * @return the note type label.
     */
    @objid ("8156b033-1dec-11e2-8cad-001ec947c8cc")
    public String getType() {
        if (this.document == null)
            return "?";
        
        final ExternDocumentType model = this.document.getType();
        if (model == null)
            return "<none>";
        
        String label = ModuleI18NService.getLabel(model);
        return label.isEmpty() ? model.getName() : label;
    }

    /**
     * Get the extern document type label.
     * @return the note type label.
     */
    @objid ("8156b038-1dec-11e2-8cad-001ec947c8cc")
    public Image getMimeType() {
        return RichNoteFormatRegistry.getInstance().getFormat(getRepresentedElement()).getIcon();
    }

    @objid ("8156b03d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(final IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmExternDocument." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0(in);
            break;
        }
        default: {
            assert (false) : readVersion +" version number not covered!";
            // reading as last handled version: 0
            read_0(in);
            break;
        }
        }
    }

    @objid ("8156b042-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void refreshFromObModel() {
        if (this.document != null) {
            firePropertyChange(PROPERTY_LABEL, null, this.document.getAbstract());
        }
    }

    @objid ("8156b045-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("8156b04a-1dec-11e2-8cad-001ec947c8cc")
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

    @objid ("8156b04f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmExternDocument." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("8159125f-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(final IDiagramReader in) {
        super.read(in);
        this.document = (ExternDocument) resolveRef(this.getRepresentedRef());
    }

    @objid ("81591263-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
