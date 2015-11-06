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
                                    

package org.modelio.diagram.elements.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagramStyleKeys;
import org.modelio.diagram.elements.drawings.ellipse.GmEllipseStyleKeys;
import org.modelio.diagram.elements.drawings.line.GmLineStyleKeys;
import org.modelio.diagram.elements.drawings.rectangle.GmRectangleStyleKeys;
import org.modelio.diagram.elements.drawings.text.GmTextStyleKeys;
import org.modelio.diagram.elements.umlcommon.abstraction.GmAbstraction;
import org.modelio.diagram.elements.umlcommon.constraint.GmConstraintStyleKeys;
import org.modelio.diagram.elements.umlcommon.dependency.GmDependency;
import org.modelio.diagram.elements.umlcommon.elementRealization.GmElementRealization;
import org.modelio.diagram.elements.umlcommon.externdocument.GmExternDocumentStyleKeys;
import org.modelio.diagram.elements.umlcommon.namespaceuse.GmNamespaceUseStyleKeys;
import org.modelio.diagram.elements.umlcommon.note.GmNoteStyleKeys;
import org.modelio.diagram.elements.umlcommon.usage.GmUsage;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the diagram.elements StyleKeys and initializing the factory.settings.
 */
@objid ("ec74c92a-58d3-11e2-be0b-002564c97630")
public class DiagramElementsProcessor {
    @objid ("f73b9f98-58d3-11e2-be0b-002564c97630")
    @Execute
    private void execute() {
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("f73b9f9b-58d3-11e2-be0b-002564c97630")
    private void declareFactorySettings() {
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramElements.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

    @objid ("f73b9f9d-58d3-11e2-be0b-002564c97630")
    private void declareStyleProviders() {
        // Abstract Diagram
        final FactoryStyle factory = FactoryStyle.getInstance();
        factory.declareProvider(GmAbstractDiagramStyleKeys.class);
        
        // Constraint
        factory.declareProvider(GmConstraintStyleKeys.class);
        
        // Dependency
        factory.declareProvider(GmDependency.styleKeyProvider);
        factory.declareProvider(GmAbstraction.styleKeyProvider);
        factory.declareProvider(GmElementRealization.styleKeyProvider);
        factory.declareProvider(GmUsage.styleKeyProvider);
        
        // MObject import
        factory.declareProvider(GmExternDocumentStyleKeys.class);
        
        // NamespaceUse
        factory.declareProvider(GmNamespaceUseStyleKeys.class);
        
        // Note
        factory.declareProvider(GmNoteStyleKeys.class);
        
        // Drawings
        factory.declareProvider(GmRectangleStyleKeys.class);
        factory.declareProvider(GmRectangleStyleKeys.Label.class);
        factory.declareProvider(GmEllipseStyleKeys.class);
        factory.declareProvider(GmEllipseStyleKeys.Label.class);
        factory.declareProvider(GmTextStyleKeys.class);
        factory.declareProvider(GmLineStyleKeys.class);
        factory.declareProvider(GmLineStyleKeys.SourceDeco.class);
        factory.declareProvider(GmLineStyleKeys.TargetDeco.class);
        //factory.declareProvider(GmLineStyleKeys.Label.class);
    }

}
