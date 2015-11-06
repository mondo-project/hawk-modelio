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
                                    

package org.modelio.diagram.editor.object.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.object.editor.ObjectDiagramConfigurer;
import org.modelio.diagram.editor.object.elements.ObjectDiagramEditPartFactory;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.ObjectDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the bpmn diagram configurer.
 */
@objid ("113cadae-5a45-11e2-9e33-00137282c51b")
public class ObjectProcessor {
    @objid ("194affc9-5a45-11e2-9e33-00137282c51b")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for State diagram
        
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(ObjectDiagram.class).getName(), null, new ObjectDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("194affce-5a45-11e2-9e33-00137282c51b")
    private void declareFactories() {
        // Register our edit part factory
           ModelioEditPartFactory.getInstance().registerFactory(ObjectDiagramEditPartFactory.class.getName(),
                                                                ObjectDiagramEditPartFactory.getInstance());
    }

    @objid ("194affd2-5a45-11e2-9e33-00137282c51b")
    private void declareFactorySettings() {
        // Load the default values. Do it only here, after key providers registration.
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorObject.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
