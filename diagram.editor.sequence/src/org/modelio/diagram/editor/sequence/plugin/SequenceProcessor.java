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
                                    

package org.modelio.diagram.editor.sequence.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.editor.sequence.elements.SequenceDiagramEditPartFactory;
import org.modelio.diagram.editor.sequence.elements.SequenceDiagramGmLinkFactory;
import org.modelio.diagram.editor.sequence.elements.SequenceDiagramGmNodeFactory;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.GmCombinedFragmentStyleKeys;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.GmExecutionOccurenceSpecificationStyleKeys;
import org.modelio.diagram.editor.sequence.elements.executionspecification.GmExecutionSpecificationStructuredStyleKeys;
import org.modelio.diagram.editor.sequence.elements.gate.GmGateStructuredStyleKeys;
import org.modelio.diagram.editor.sequence.elements.interactionoperand.GmInteractionOperandStyleKeys;
import org.modelio.diagram.editor.sequence.elements.interactionuse.GmInteractionUseStyleKeys;
import org.modelio.diagram.editor.sequence.elements.interactionuse.gate.GmGateOnInteractionUseStructuredStyleKeys;
import org.modelio.diagram.editor.sequence.elements.lifeline.GmLifelineStructuredStyleKeys;
import org.modelio.diagram.editor.sequence.elements.message.GmMessageStyleKeys;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.GmSequenceDiagramStyleKeys;
import org.modelio.diagram.editor.sequence.elements.sequencediagramview.SequenceDiagramViewStructuredStyleKeys;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.GmStateInvariantStructuredStyleKeys;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.osgi.framework.BundleContext;

@objid ("ef3b46f8-4778-4fba-8be4-d8dc2c327182")
public class SequenceProcessor {
    @objid ("f7e41841-49ed-4ca6-ab5e-3fe6a93f0310")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(SequenceDiagram.class).getName(), null, new SequenceDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("74573ff1-5afb-411b-a94e-c33298fc8e3d")
    private void declareFactories() {
        ModelioEditPartFactory.getInstance().registerFactory(SequenceDiagramEditPartFactory.class.getName(),
                SequenceDiagramEditPartFactory.getInstance());
        
        GmLinkFactory.getInstance().registerFactory(SequenceDiagramGmLinkFactory.class.getName(),
                SequenceDiagramGmLinkFactory.getInstance());
        
        GmNodeFactory.getInstance().registerFactory(SequenceDiagramGmNodeFactory.class.getName(),
                SequenceDiagramGmNodeFactory.getInstance());
    }

    @objid ("c45215d1-ae9d-4a2e-84d7-7ba1105400ab")
    private void declareStyleProviders() {
        final FactoryStyle factory = FactoryStyle.getInstance();
                
                // Declare the StyleKey providers
                // ------------------------------
                
                // Sequence Diagram
                // ----------------
                factory.declareProvider(GmSequenceDiagramStyleKeys.class);
                
                factory.declareProvider(SequenceDiagramViewStructuredStyleKeys.class);
                
                // Message
                factory.declareProvider(GmMessageStyleKeys.class);
                factory.declareProvider(GmMessageStyleKeys.InfoFlows.class);
                
                // Lifeline
                factory.declareProvider(GmLifelineStructuredStyleKeys.class);
                
                // Execution
                factory.declareProvider(GmExecutionSpecificationStructuredStyleKeys.class);
                
                // ExecutionOccurenceSpecification
                factory.declareProvider(GmExecutionOccurenceSpecificationStyleKeys.class);
                
                // InteractionUse
                factory.declareProvider(GmInteractionUseStyleKeys.class);
                
                // GateOnInteractionUSe
                factory.declareProvider(GmGateOnInteractionUseStructuredStyleKeys.class);
                
                // Gate (on diagram background)
                factory.declareProvider(GmGateStructuredStyleKeys.class);
                
                // CombinedFragment
                factory.declareProvider(GmCombinedFragmentStyleKeys.class);
                
                // InteractionOperand
                factory.declareProvider(GmInteractionOperandStyleKeys.class);
                
                // StateInvariant
                factory.declareProvider(GmStateInvariantStructuredStyleKeys.class);
    }

    @objid ("b1725a5f-ec9e-4fe1-8a14-3e4af693f01f")
    private void declareFactorySettings() {
        // Load the default values.
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorSequence.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
