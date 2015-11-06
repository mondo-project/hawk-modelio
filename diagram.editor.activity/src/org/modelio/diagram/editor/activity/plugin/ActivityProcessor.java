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
                                    

package org.modelio.diagram.editor.activity.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.activity.elements.ActivityDiagramEditPartFactory;
import org.modelio.diagram.editor.activity.elements.acceptsignal.GmAcceptSignalStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.action.GmActionStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.activitydiagram.GmActivityDiagramStyleKeys;
import org.modelio.diagram.editor.activity.elements.activityfinal.GmActivityFinalStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.callbehavior.GmCallBehaviorStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.callevent.GmCallEventStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.calloperation.GmCallOperationStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.centralbuffer.GmCentralBufferStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.changeevent.GmChangeEventStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.clause.GmClauseStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.conditional.GmConditionalStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.controlflow.GmControlStyleKeys;
import org.modelio.diagram.editor.activity.elements.datastore.GmDataStoreStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.decisionmerge.GmDecisionMergeStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.exceptionhandler.GmExceptionHandlerStyleKeys;
import org.modelio.diagram.editor.activity.elements.expansionnode.GmExpansionNodeStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.expansionregion.GmExpansionRegionStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.flowfinal.GmFlowFinalStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.forkjoin.GmForkJoinStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.gmfactories.ActivityGmLinkFactory;
import org.modelio.diagram.editor.activity.elements.gmfactories.ActivityGmNodeFactory;
import org.modelio.diagram.editor.activity.elements.initial.GmInitialStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.inputpin.GmInputPinStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.interruptible.GmInterruptibleStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.loopnode.GmLoopNodeStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.objectflow.GmObjectFlowStyleKeys;
import org.modelio.diagram.editor.activity.elements.objectnode.GmObjectNodeStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.outputpin.GmOutputPinStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.partition.GmPartitionStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.sendsignal.GmSendSignalStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.structuredactivity.GmStructuredActivityStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.timeevent.GmTimeEventStructuredStyleKeys;
import org.modelio.diagram.editor.activity.elements.valuepin.GmValuePinStructuredStyleKeys;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the activity diagram configurer.
 */
@objid ("2b75e8b3-55b6-11e2-877f-002564c97630")
public class ActivityProcessor {
    @objid ("2ed7892c-58a7-11e2-9574-002564c97630")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(ActivityDiagram.class).getName(), null, new ActivityDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("2ed78931-58a7-11e2-9574-002564c97630")
    private void declareFactories() {
        ModelioEditPartFactory.getInstance().registerFactory(ActivityDiagramEditPartFactory.class.getName(),
                ActivityDiagramEditPartFactory.getInstance());
        
        GmLinkFactory.getInstance().registerFactory(ActivityGmLinkFactory.class.getName(),
                ActivityGmLinkFactory.getInstance());
        
        GmNodeFactory.getInstance().registerFactory(ActivityGmNodeFactory.class.getName(),
                ActivityGmNodeFactory.getInstance());
    }

    @objid ("2ed78933-58a7-11e2-9574-002564c97630")
    private void declareStyleProviders() {
        // Activity Diagram
        FactoryStyle.getInstance().declareProvider(GmActivityDiagramStyleKeys.class);
        // OpaqueAction
        FactoryStyle.getInstance().declareProvider(GmActionStructuredStyleKeys.class);
        // CallBehavior
        FactoryStyle.getInstance().declareProvider(GmCallBehaviorStructuredStyleKeys.class);
        // CallOperation
        FactoryStyle.getInstance().declareProvider(GmCallOperationStructuredStyleKeys.class);
        // ObjectNode
        FactoryStyle.getInstance().declareProvider(GmObjectNodeStructuredStyleKeys.class);
        // CentralBuffer
        FactoryStyle.getInstance().declareProvider(GmCentralBufferStructuredStyleKeys.class);
        // DataStore
        FactoryStyle.getInstance().declareProvider(GmLoopNodeStructuredStyleKeys.class);
        // Loop node
        FactoryStyle.getInstance().declareProvider(GmDataStoreStructuredStyleKeys.class);
        // Control flow
        FactoryStyle.getInstance().declareProvider(GmControlStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmControlStyleKeys.InfoFlows.class);
        // value pin
        FactoryStyle.getInstance().declareProvider(GmValuePinStructuredStyleKeys.class);
        // expansion node
        FactoryStyle.getInstance().declareProvider(GmExpansionNodeStructuredStyleKeys.class);
        // expansion region
        FactoryStyle.getInstance().declareProvider(GmExpansionRegionStructuredStyleKeys.class);
        // Exception handler
        FactoryStyle.getInstance().declareProvider(GmExceptionHandlerStyleKeys.class);
        // Object flow
        FactoryStyle.getInstance().declareProvider(GmObjectFlowStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmObjectFlowStyleKeys.InfoFlows.class);
        // Send signal action
        FactoryStyle.getInstance().declareProvider(GmSendSignalStructuredStyleKeys.class);
        // accept call event action
        FactoryStyle.getInstance().declareProvider(GmCallEventStructuredStyleKeys.class);
        // accept change event action
        FactoryStyle.getInstance().declareProvider(GmChangeEventStructuredStyleKeys.class);
        // accept signal action
        FactoryStyle.getInstance().declareProvider(GmAcceptSignalStructuredStyleKeys.class);
        // structured activity node
        FactoryStyle.getInstance().declareProvider(GmStructuredActivityStructuredStyleKeys.class);
        // conditional node
        FactoryStyle.getInstance().declareProvider(GmConditionalStructuredStyleKeys.class);
        // clause
        FactoryStyle.getInstance().declareProvider(GmClauseStructuredStyleKeys.class);
        // time event
        FactoryStyle.getInstance().declareProvider(GmTimeEventStructuredStyleKeys.class);
        // initial
        FactoryStyle.getInstance().declareProvider(GmInitialStructuredStyleKeys.class);
        // final
        FactoryStyle.getInstance().declareProvider(GmActivityFinalStructuredStyleKeys.class);
        // fork join
        FactoryStyle.getInstance().declareProvider(GmForkJoinStructuredStyleKeys.class);
        // flowfinal
        FactoryStyle.getInstance().declareProvider(GmFlowFinalStructuredStyleKeys.class);
        // decision merge
        FactoryStyle.getInstance().declareProvider(GmDecisionMergeStructuredStyleKeys.class);
        // Input pint
        FactoryStyle.getInstance().declareProvider(GmInputPinStructuredStyleKeys.class);
        // Output pin
        FactoryStyle.getInstance().declareProvider(GmOutputPinStructuredStyleKeys.class);
        // Partition
        FactoryStyle.getInstance().declareProvider(GmPartitionStructuredStyleKeys.class);
        // Interruptible region
        FactoryStyle.getInstance().declareProvider(GmInterruptibleStructuredStyleKeys.class);
    }

    @objid ("a4d67dc4-58d3-11e2-be0b-002564c97630")
    private void declareFactorySettings() {
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorActivity.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
