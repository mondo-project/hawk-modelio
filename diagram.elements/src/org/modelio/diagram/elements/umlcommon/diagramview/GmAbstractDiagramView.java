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
                                    

package org.modelio.diagram.elements.umlcommon.diagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.umlcommon.diagramheader.GmDiagramHeader;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@link AbstractDiagram} view in the diagram.
 * 
 * @author cmarin
 */
@objid ("81486234-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmAbstractDiagramView extends GmCompositeNode {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("81486238-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("8148623b-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("81486236-1dec-11e2-8cad-001ec947c8cc")
    private AbstractDiagram obDiagram;

    @objid ("81486237-1dec-11e2-8cad-001ec947c8cc")
    private GmAbstractDiagram viewedDiagram;

    /**
     * For deserialization only.
     */
    @objid ("8148623d-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractDiagramView() {
        super();
    }

    /**
     * Creates a diagram model.
     * @param diagram The diagram owning this diagram view
     * @param el The represented diagram.
     * @param ref The represented diagram reference.
     */
    @objid ("81486240-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractDiagramView(final GmAbstractDiagram diagram, AbstractDiagram el, MRef ref) {
        super(diagram, ref);
        this.obDiagram = el;
        
        final GmDiagramHeader header = new GmDiagramHeader(diagram, ref);
        // header.setLayoutData(BorderLayout.TOP);
        addChild(header);
    }

    @objid ("81486247-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.viewedDiagram != null) {
            this.viewedDiagram.dispose();
        }
    }

    @objid ("8148624a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    @objid ("81486252-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    @objid ("81486258-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void delete() {
        super.delete();
        if (this.viewedDiagram != null) {
            this.viewedDiagram.dispose();
            this.viewedDiagram = null;
        }
    }

    @objid ("814ac445-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        return null;
    }

    @objid ("814ac44d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("814ac452-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public AbstractDiagram getRepresentedElement() {
        return this.obDiagram;
    }

    /**
     * Get the viewed diagram model.
     * <p>
     * Loads the diagram if not already done.
     * @return the viewed diagram model.
     */
    @objid ("814ac457-1dec-11e2-8cad-001ec947c8cc")
    public GmAbstractDiagram getViewedDiagram() {
        if (this.obDiagram != null && this.viewedDiagram == null) {
            this.viewedDiagram = loadViewedDiagram();
        }
        return this.viewedDiagram;
    }

    @objid ("814ac45c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void obElementResolved(final MObject ev) {
        super.obElementResolved(ev);
    }

    @objid ("814ac461-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmAbstractDiagramView." + MINOR_VERSION_PROPERTY);
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

    @objid ("814ac465-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void refreshFromObModel() {
        // Nothing to do
    }

    /**
     * Load the diagram to view.
     * @return the viewed diagram.
     */
    @objid ("814ac468-1dec-11e2-8cad-001ec947c8cc")
    protected abstract GmAbstractDiagram loadViewedDiagram();

    @objid ("814ac46b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmAbstractDiagramView." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("814ac46f-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
        
        this.obDiagram = (AbstractDiagram) resolveRef(this.getRepresentedRef());
    }

    @objid ("814ac472-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
