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
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.model.GmAbstractObject;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.ProxyStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents the link between the note and the annoted element.
 * <p>
 * The annoted element is the source and the destination is a {@link GmNote}.
 * 
 * @author cmarin
 */
@objid ("8188c192-1dec-11e2-8cad-001ec947c8cc")
public class GmNoteLink extends GmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("8188c194-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("8188c197-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Constructor that must be used for deserialization only.
     */
    @objid ("8188c199-1dec-11e2-8cad-001ec947c8cc")
    public GmNoteLink() {
        // Nothing to do.
    }

    /**
     * Creates a new GmNoteLink
     * @param diagram The diagram containing the link.
     * @param relatedRef a reference to the represented Note.
     */
    @objid ("8188c19c-1dec-11e2-8cad-001ec947c8cc")
    public GmNoteLink(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
    }

    @objid ("8188c1a1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Note getRelatedElement() {
        if (getTo() != null) {
            return (Note) getTo().getRelatedElement();
        } else {
            return null;
        }
    }

    @objid ("8188c1a5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return GmNote.KEYS.getStyleKey(metakey);
    }

    @objid ("8188c1ab-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<StyleKey> getStyleKeys() {
        return GmNote.KEYS.getStyleKeys();
    }

    @objid ("8188c1b2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IStyle createStyle(GmAbstractDiagram aDiagram) {
        return new ProxyStyle(aDiagram.getStyle());
    }

    @objid ("818b23c5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getFromElement() {
        return this.getRelatedElement().getSubject();
    }

    @objid ("818b23ca-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getToElement() {
        return this.getRelatedElement();
    }

    /**
     * Updates the proxy style to point to the given node style.
     * @param ref the reference node, may be null.
     */
    @objid ("818b23cf-1dec-11e2-8cad-001ec947c8cc")
    private void refreshStyle(final GmAbstractObject ref) {
        // Modify the style
        if (ref != null)
            getStyle().setCascadedStyle(ref.getStyle());
        else
            getStyle().setCascadedStyle(getDiagram().getStyle());
    }

    @objid ("818b23d4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTo(final IGmLinkable to) {
        super.setTo(to);
        if (to instanceof GmAbstractObject)
            refreshStyle((GmAbstractObject) to);
    }

    @objid ("818b23d9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void readLink(final IDiagramReader in) {
        super.readLink(in);
        if (getTo() instanceof GmAbstractObject)
            refreshStyle((GmAbstractObject) getTo());
    }

    @objid ("818b23de-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNoteLink." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("818b23e2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
