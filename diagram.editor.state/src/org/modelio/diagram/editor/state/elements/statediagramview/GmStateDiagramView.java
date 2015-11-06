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
                                    

package org.modelio.diagram.editor.state.elements.statediagramview;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.state.editor.StateDiagramEditorInput;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.umlcommon.diagramview.GmAbstractDiagramView;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@link StateMachineDiagram} view in the diagram.
 * 
 * @author cmarin
 */
@objid ("f5964b1a-55b6-11e2-877f-002564c97630")
public class GmStateDiagramView extends GmAbstractDiagramView {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("f597d1c0-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("f5995859-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("fe6c2e04-5a5b-11e2-9e33-00137282c51b")
     static final StateDiagramViewStructuredStyleKeys STRUCTURED_KEYS = new StateDiagramViewStructuredStyleKeys();

    @objid ("fe6c2e06-5a5b-11e2-9e33-00137282c51b")
    private static final StateDiagramViewSimpleStyleKeys SIMPLE_KEYS = new StateDiagramViewSimpleStyleKeys();

    @objid ("fe6e905f-5a5b-11e2-9e33-00137282c51b")
    private static final StateDiagramViewImageStyleKeys IMAGE_KEYS = new StateDiagramViewImageStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("f599585b-55b6-11e2-877f-002564c97630")
    public GmStateDiagramView() {
        super();
    }

    /**
     * Creates a diagram model.
     * @param diagram The diagram owning this diagram view
     * @param el The represented diagram.
     * @param ref The represented diagram reference.
     */
    @objid ("f599585e-55b6-11e2-877f-002564c97630")
    public GmStateDiagramView(final GmAbstractDiagram diagram, StateMachineDiagram el, MRef ref) {
        super(diagram, el, ref);
    }

    @objid ("f599586b-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("f5995872-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        StyleKey ret = STRUCTURED_KEYS.getStyleKey(metakey);
        if (ret != null)
            return ret;
        
        ret = SIMPLE_KEYS.getStyleKey(metakey);
        if (ret != null)
            return ret;
        
        ret = IMAGE_KEYS.getStyleKey(metakey);
        return ret;
    }

    @objid ("f599587c-55b6-11e2-877f-002564c97630")
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

    @objid ("f5995884-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram loadViewedDiagram() {
        StateDiagramEditorInput input = new StateDiagramEditorInput(getDiagram().getModelManager(), getRepresentedElement());
        return input.getGmDiagram();
    }

    @objid ("f599588b-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmStateDiagramView." + MINOR_VERSION_PROPERTY);
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

    @objid ("f5995891-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmStateDiagramView." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("f5995897-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("f599589c-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
