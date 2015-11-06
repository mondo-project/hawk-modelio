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
                                    

package org.modelio.diagram.elements.common.abstractdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.persistence.ExtReferenceResolver;
import org.modelio.diagram.elements.persistence.InstanceFactory;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.persistence.XmlDiagramReader;
import org.modelio.diagram.persistence.XmlDiagramWriter;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Service class that load and save a {@link GmAbstractDiagram} from its {@link AbstractDiagram} saved data.
 * 
 * @author cmarin
 */
@objid ("7e143931-1dec-11e2-8cad-001ec947c8cc")
public class DiagramPersistence {
    /**
     * Load a {@link GmAbstractDiagram} from its {@link AbstractDiagram} saved data.
     * <p>
     * Clear the diagram before (re)loading it.
     * @param diagram the diagram to reload.
     */
    @objid ("7e143933-1dec-11e2-8cad-001ec947c8cc")
    public static void loadDiagram(GmAbstractDiagram diagram) {
        final MRef diagramRef = diagram.getRepresentedRef();
        final AbstractDiagram obDiagram = (AbstractDiagram) diagram.getModelManager().resolveRef(diagramRef);
        
        if (obDiagram == null) {
            throw new IllegalArgumentException("{" + diagramRef.uuid + "} " + diagramRef.mc
                    + " not found, it may have been deleted.");
        }
        
        final String data = obDiagram.getUiData();
        
        diagram.clear();
        
        if (data != null && !data.isEmpty()) {
            final IDiagramReader reader = new XmlDiagramReader(new InstanceFactory(), new ExtReferenceResolver());
            reader.readDiagram(data, diagram);
        }
        
        diagram.updateLastSaveDate();
        
        diagram.refreshAllFromObModel();
        diagram.refreshFromObModel();
    }

    /**
     * Save the {@link GmAbstractDiagram} content in the {@link AbstractDiagram}.
     * <p>
     * A transaction must already be open.
     * @param diagram The diagram to save.
     */
    @objid ("7e143939-1dec-11e2-8cad-001ec947c8cc")
    public static void saveDiagram(GmAbstractDiagram diagram) {
        final IDiagramWriter writer = new XmlDiagramWriter();
        writer.save(diagram);
        
        final AbstractDiagram obDiagram = (AbstractDiagram) (diagram.getRelatedElement());
        if (!obDiagram.isShell() && !obDiagram.isDeleted()) {
            obDiagram.setUiData(writer.getOutput());
            obDiagram.setUiDataVersion(obDiagram.getUiDataVersion() + 1);
            diagram.updateLastSaveDate();
        }
    }

}
