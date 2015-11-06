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
                                    

package org.modelio.diagram.editor.composite.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.composite.editor.CompositeDiagramConfigurer;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.CompositeStructureDiagram;

/**
 * Processor registering the composite diagram configurer.
 */
@objid ("d2b1d20e-5bd8-11e2-9e33-00137282c51b")
public class CompositeProcessor {
    @objid ("dace7245-5bd8-11e2-9e33-00137282c51b")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for Composite diagram
                configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(CompositeStructureDiagram.class).getName(), null, new CompositeDiagramConfigurer());
    }

}
