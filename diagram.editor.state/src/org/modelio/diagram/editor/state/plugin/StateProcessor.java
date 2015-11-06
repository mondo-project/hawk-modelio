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
                                    

package org.modelio.diagram.editor.state.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.state.editor.StateDiagramConfigurer;
import org.modelio.diagram.editor.state.elements.StateDiagramEditPartFactory;
import org.modelio.diagram.editor.state.elements.choice.GmChoiceStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.connectionpoint.GmConnectionPointStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.deephistory.GmDeepHistoryStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.entry.GmEntryStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.exit.GmExitStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.finalstate.GmFinalStateStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.fork.GmForkStateStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.gmfactory.StateGmLinkFactory;
import org.modelio.diagram.editor.state.elements.gmfactory.StateGmNodeFactory;
import org.modelio.diagram.editor.state.elements.initialstate.GmInitialStateStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.internaltransition.GmInternalTransitionStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.join.GmJoinStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.junction.GmJunctionStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.region.GmRegionStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.shallowhistory.GmShallowHistoryStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.state.GmStateStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.statediagram.GmStateDiagramStyleKeys;
import org.modelio.diagram.editor.state.elements.statediagramview.StateDiagramViewStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.terminal.GmTerminalStructuredStyleKeys;
import org.modelio.diagram.editor.state.elements.transition.GmTransitionStyleKeys;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the state diagram configurer.
 */
@objid ("d3d1b4e7-addf-427c-9d43-990994e7ac10")
public class StateProcessor {
    @objid ("9f90dd9c-5fc1-45ff-a32f-9f78b869f8e3")
    @Execute
    private void execute(ToolRegistry toolregistry, IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for State diagram
        
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(StateMachineDiagram.class).getName(), null, new StateDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("7dbd2e8d-7785-41a6-bab2-21a2ee36ad43")
    private void declareFactories() {
        // Register our edit part factory
        ModelioEditPartFactory.getInstance().registerFactory(StateDiagramEditPartFactory.class.getName(),
                                                             StateDiagramEditPartFactory.getInstance());
        
        // register the State diagram GmLink factory extension
        GmLinkFactory.getInstance().registerFactory(StateGmLinkFactory.class.getName(),
                                                    StateGmLinkFactory.getInstance());
        
        // register the State diagram GmLink factory extension
        GmNodeFactory.getInstance().registerFactory(StateGmNodeFactory.class.getName(),
                                                    StateGmNodeFactory.getInstance());
    }

    @objid ("ca1f656f-0421-48bf-bb03-d12780242178")
    private void declareStyleProviders() {
        FactoryStyle.getInstance().declareProvider(GmStateDiagramStyleKeys.class);
               
               FactoryStyle.getInstance().declareProvider(StateDiagramViewStructuredStyleKeys.class);
               
               FactoryStyle.getInstance().declareProvider(GmStateStructuredStyleKeys.class);
               // region
               FactoryStyle.getInstance().declareProvider(GmRegionStructuredStyleKeys.class);
               // internal transition
               FactoryStyle.getInstance().declareProvider(GmInternalTransitionStructuredStyleKeys.class);
               // Initial state
               FactoryStyle.getInstance().declareProvider(GmInitialStateStructuredStyleKeys.class);
               // Final state
               FactoryStyle.getInstance().declareProvider(GmFinalStateStructuredStyleKeys.class);
               // choice state
               FactoryStyle.getInstance().declareProvider(GmChoiceStructuredStyleKeys.class);
               // terminal state
               FactoryStyle.getInstance().declareProvider(GmTerminalStructuredStyleKeys.class);
               // exit state
               FactoryStyle.getInstance().declareProvider(GmExitStructuredStyleKeys.class);
               // entry state
               FactoryStyle.getInstance().declareProvider(GmEntryStructuredStyleKeys.class);
               // connection point
               FactoryStyle.getInstance().declareProvider(GmConnectionPointStructuredStyleKeys.class);
               // connection point
               FactoryStyle.getInstance().declareProvider(GmJunctionStructuredStyleKeys.class);
               // shallow history
               FactoryStyle.getInstance().declareProvider(GmShallowHistoryStructuredStyleKeys.class);
               // deep history
               FactoryStyle.getInstance().declareProvider(GmDeepHistoryStructuredStyleKeys.class);
               // fork
               FactoryStyle.getInstance().declareProvider(GmForkStateStructuredStyleKeys.class);
               // join
               FactoryStyle.getInstance().declareProvider(GmJoinStructuredStyleKeys.class);
               // transition
               FactoryStyle.getInstance().declareProvider(GmTransitionStyleKeys.class);
    }

    @objid ("1ae9e4b8-fe5f-4182-886d-e2d0437c833f")
    private void declareFactorySettings() {
        // Load the default values. Do it only here, after key providers registration.
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorState.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
