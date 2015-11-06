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
                                    

package org.modelio.diagram.editor.sequence.elements.sequencediagramview;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.sequence.editor.SequenceDiagramEditorInput;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.umlcommon.diagramview.GmAbstractDiagramView;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@link IStaticDiagram} view in the diagram.
 * 
 * @author cmarin
 */
@objid ("d98cdd3a-55b6-11e2-877f-002564c97630")
public class GmSequenceDiagramView extends GmAbstractDiagramView {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("d98cdd44-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("d98cdd47-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("d98cdd3e-55b6-11e2-877f-002564c97630")
     static final SequenceDiagramViewStructuredStyleKeys STRUCTURED_KEYS = new SequenceDiagramViewStructuredStyleKeys();

    @objid ("fc383315-3c11-4905-a6d0-71f3d138ce3b")
    private static final SequenceDiagramViewSimpleStyleKeys SIMPLE_KEYS = new SequenceDiagramViewSimpleStyleKeys();

    @objid ("9d77d8a3-bbff-411a-ac64-ea1417b414fe")
    private static final SequenceDiagramViewImageStyleKeys IMAGE_KEYS = new SequenceDiagramViewImageStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("d98cdd49-55b6-11e2-877f-002564c97630")
    public GmSequenceDiagramView() {
        super();
    }

    /**
     * Creates a diagram model.
     * @param diagram The diagram owning this diagram view
     * @param el The represented diagram.
     * @param ref The represented diagram reference.
     */
    @objid ("d98cdd4c-55b6-11e2-877f-002564c97630")
    public GmSequenceDiagramView(final GmAbstractDiagram diagram, SequenceDiagram el, MRef ref) {
        super(diagram, el, ref);
    }

    @objid ("d98e63e2-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("d98e63e9-55b6-11e2-877f-002564c97630")
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

    @objid ("d98e63f2-55b6-11e2-877f-002564c97630")
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

    @objid ("d98e63fa-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram loadViewedDiagram() {
        SequenceDiagramEditorInput input = new SequenceDiagramEditorInput(getDiagram().getModelManager(), getRepresentedElement());
        return input.getGmDiagram();
    }

    @objid ("d98e6401-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmSequenceDiagramView." + MINOR_VERSION_PROPERTY);
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

    @objid ("d98e6407-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmSequenceDiagramView." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("d98e640d-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("d98e6412-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
