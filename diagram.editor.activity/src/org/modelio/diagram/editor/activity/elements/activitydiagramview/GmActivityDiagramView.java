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
                                    

package org.modelio.diagram.editor.activity.elements.activitydiagramview;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.editor.ActivityDiagramEditorInput;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.umlcommon.diagramview.GmAbstractDiagramView;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@link IStaticDiagram} view in the diagram.
 * 
 * @author cmarin
 */
@objid ("299e1b6f-55b6-11e2-877f-002564c97630")
public class GmActivityDiagramView extends GmAbstractDiagramView {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("299e1b79-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("299e1b7c-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("299e1b73-55b6-11e2-877f-002564c97630")
     static final ActivityDiagramViewStructuredStyleKeys STRUCTURED_KEYS = new ActivityDiagramViewStructuredStyleKeys();

    @objid ("299e1b75-55b6-11e2-877f-002564c97630")
    private static final ActivityDiagramViewSimpleStyleKeys SIMPLE_KEYS = new ActivityDiagramViewSimpleStyleKeys();

    @objid ("299e1b77-55b6-11e2-877f-002564c97630")
    private static final ActivityDiagramViewImageStyleKeys IMAGE_KEYS = new ActivityDiagramViewImageStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("299e1b7e-55b6-11e2-877f-002564c97630")
    public GmActivityDiagramView() {
        super();
    }

    /**
     * Creates a diagram model.
     * @param diagram The diagram owning this diagram view
     * @param el The represented diagram.
     * @param ref The represented diagram reference.
     */
    @objid ("299e1b81-55b6-11e2-877f-002564c97630")
    public GmActivityDiagramView(final GmAbstractDiagram diagram, ActivityDiagram el, MRef ref) {
        super(diagram, el, ref);
    }

    @objid ("299e1b8e-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("299fa1fa-55b6-11e2-877f-002564c97630")
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

    @objid ("299fa204-55b6-11e2-877f-002564c97630")
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

    @objid ("299fa20c-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram loadViewedDiagram() {
        ActivityDiagramEditorInput input = new ActivityDiagramEditorInput(getDiagram().getModelManager(), getRepresentedElement());
        return input.getGmDiagram();
    }

    @objid ("299fa213-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmActivityDiagramView." + MINOR_VERSION_PROPERTY);
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

    @objid ("299fa219-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmActivityDiagramView." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("299fa21f-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("299fa224-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
