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
                                    

package org.modelio.diagram.editor.communication.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.communication.editor.CommunicationDiagramConfigurer;
import org.modelio.diagram.editor.communication.elements.CommunicationDiagramEditPartFactory;
import org.modelio.diagram.editor.communication.elements.CommunicationDiagramGmLinkFactory;
import org.modelio.diagram.editor.communication.elements.CommunicationDiagramGmNodeFactory;
import org.modelio.diagram.editor.communication.elements.communicationchannel.GmCommunicationChannelStyleKeys;
import org.modelio.diagram.editor.communication.elements.communicationdiagram.GmCommunicationDiagramStyleKeys;
import org.modelio.diagram.editor.communication.elements.communicationdiagramview.CommunicationDiagramViewStructuredStyleKeys;
import org.modelio.diagram.editor.communication.elements.communicationnode.GmCommunicationNodeStructuredStyleKeys;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the activity diagram configurer.
 */
@objid ("9e1fe2ba-598e-11e2-ae45-002564c97630")
public class CommunicationProcessor {
    @objid ("059657cb-599a-11e2-ae45-002564c97630")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for communication diagrams
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(CommunicationDiagram.class).getName(), null, new CommunicationDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("059657d0-599a-11e2-ae45-002564c97630")
    private void declareFactories() {
        // Register our edit part factory
        ModelioEditPartFactory.getInstance()
                              .registerFactory(CommunicationDiagramEditPartFactory.class.getName(),
                                               CommunicationDiagramEditPartFactory.getInstance());
        
        // Register gm link factory 
        GmLinkFactory.getInstance().registerFactory(CommunicationDiagramGmLinkFactory.class.getName(),
                                                    CommunicationDiagramGmLinkFactory.getInstance());
        
        // Register gm node factory
        GmNodeFactory.getInstance().registerFactory(CommunicationDiagramGmNodeFactory.class.getName(),
                                                    CommunicationDiagramGmNodeFactory.getInstance());
    }

    @objid ("059657d2-599a-11e2-ae45-002564c97630")
    private void declareStyleProviders() {
        // Communication Channel
        FactoryStyle.getInstance().declareProvider(GmCommunicationChannelStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmCommunicationChannelStyleKeys.InfoFlows.class);
        
        // Communication Diagram
        FactoryStyle.getInstance().declareProvider(GmCommunicationDiagramStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(CommunicationDiagramViewStructuredStyleKeys.class);
        
        // Communication node
        FactoryStyle.getInstance().declareProvider(GmCommunicationNodeStructuredStyleKeys.class);
    }

    @objid ("059657d4-599a-11e2-ae45-002564c97630")
    private void declareFactorySettings() {
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorCommunication.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
