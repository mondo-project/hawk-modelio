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
                                    

package org.modelio.diagram.editor.statik.elements.staticdiagramview;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.editor.StaticDiagramEditorInput;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.umlcommon.diagramview.GmAbstractDiagramView;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@link StaticDiagram} view in the diagram.
 * 
 * @author cmarin
 */
@objid ("36ce75da-55b7-11e2-877f-002564c97630")
public class GmStaticDiagramView extends GmAbstractDiagramView {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("36ce75e4-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("36ce75e7-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("64fa9d66-5bd5-11e2-9e33-00137282c51b")
     static final StaticDiagramViewStructuredStyleKeys STRUCTURED_KEYS = new StaticDiagramViewStructuredStyleKeys();

    @objid ("64fa9d68-5bd5-11e2-9e33-00137282c51b")
    private static final StaticDiagramViewSimpleStyleKeys SIMPLE_KEYS = new StaticDiagramViewSimpleStyleKeys();

    @objid ("64fa9d6a-5bd5-11e2-9e33-00137282c51b")
    private static final StaticDiagramViewImageStyleKeys IMAGE_KEYS = new StaticDiagramViewImageStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("36ce75e9-55b7-11e2-877f-002564c97630")
    public GmStaticDiagramView() {
        super();
    }

    /**
     * Creates a diagram model.
     * @param diagram The diagram owning this diagram view
     * @param el The represented diagram.
     * @param ref The represented diagram reference.
     */
    @objid ("36ce75ec-55b7-11e2-877f-002564c97630")
    public GmStaticDiagramView(final GmAbstractDiagram diagram, StaticDiagram el, MRef ref) {
        super(diagram, el, ref);
    }

    @objid ("36ce75f9-55b7-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("36ce7600-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        switch (getRepresentationMode()) {
        case IMAGE:
            return IMAGE_KEYS.getStyleKey(metakey);
        case SIMPLE:
            return SIMPLE_KEYS.getStyleKey(metakey);
        case STRUCTURED:
            return STRUCTURED_KEYS.getStyleKey(metakey);
        default:
            return null;
        }
    }

    @objid ("36cffc7c-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        switch (getRepresentationMode()) {
        case IMAGE:
            return IMAGE_KEYS.getStyleKeys();
        case SIMPLE:
            return SIMPLE_KEYS.getStyleKeys();
        case STRUCTURED:
            return STRUCTURED_KEYS.getStyleKeys();
        default:
            return Collections.emptyList();
        }
    }

    @objid ("36cffc84-55b7-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram loadViewedDiagram() {
        StaticDiagramEditorInput input = new StaticDiagramEditorInput(getDiagram().getModelManager(), getRepresentedElement());
        return input.getGmDiagram();
    }

    @objid ("36cffc8b-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmStaticDiagramView." + MINOR_VERSION_PROPERTY);
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

    @objid ("36cffc91-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmStaticDiagramView." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("36cffc97-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("36cffc9c-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
