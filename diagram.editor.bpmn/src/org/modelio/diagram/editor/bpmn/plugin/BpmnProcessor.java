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
                                    

package org.modelio.diagram.editor.bpmn.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.bpmn.editor.BpmnDiagramConfigurer;
import org.modelio.diagram.editor.bpmn.editor.BpmnSubProcessDiagramConfigurer;
import org.modelio.diagram.editor.bpmn.elements.BpmnEditPartFactory;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEventImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEventSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEventStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmncallactivity.GmBpmnCallActivityStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmndataassociation.GmBpmnDataAssociationStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataObjectStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.GmBpmnEndEventImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.GmBpmnEndEventSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnendevent.GmBpmnEndEventStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.GmBpmnIntermediateCatchEventImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.GmBpmnIntermediateCatchEventSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatecatchevent.GmBpmnIntermediateCatchEventStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.GmBpmnIntermediateThrowEventImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.GmBpmnIntermediateThrowEventSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnintermediatethrowevent.GmBpmnIntermediateThrowEventStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.GmBpmnLaneStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow.GmBpmnMessageFlowStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnreceivetask.GmBpmnReceiveTaskStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnsendtask.GmBpmnSendTaskStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow.GmBpmnSequenceFlowStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnservicetask.GmBpmnServiceTaskStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEventImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEventSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmnstartevent.GmBpmnStartEventStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.diagrams.GmBpmnDiagramStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.gmfactory.BpmnGmLinkFactory;
import org.modelio.diagram.editor.bpmn.elements.gmfactory.BpmnGmNodeFactory;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnGatewayImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnGatewaySimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnGatewayStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnSubProcessImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnSubProcessSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnSubProcessStructuredStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnTaskImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnTaskSimpleStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.style.GmBpmnTaskStructuredStyleKeys;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the bpmn diagram configurer.
 */
@objid ("a390671a-598e-11e2-ae45-002564c97630")
public class BpmnProcessor {
    @objid ("c618fbee-59a6-11e2-ae45-002564c97630")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for BPMN diagrams
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(BpmnProcessCollaborationDiagram.class).getName(), null, new BpmnDiagramConfigurer());
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(BpmnSubProcessDiagram.class).getName(), null, new BpmnSubProcessDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("c618fbf3-59a6-11e2-ae45-002564c97630")
    private void declareFactories() {
        // Register factories
        GmNodeFactory.getInstance().registerFactory(DiagramEditorBpmn.PLUGIN_ID, new BpmnGmNodeFactory());
        GmLinkFactory.getInstance().registerFactory(DiagramEditorBpmn.PLUGIN_ID, new BpmnGmLinkFactory());
        (ModelioEditPartFactory.getInstance()).registerFactory(DiagramEditorBpmn.PLUGIN_ID, new BpmnEditPartFactory());
    }

    @objid ("c618fbf5-59a6-11e2-ae45-002564c97630")
    private void declareStyleProviders() {
        FactoryStyle.getInstance().declareProvider(GmBpmnSequenceFlowStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnMessageFlowStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnTaskStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnTaskImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnTaskSimpleStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnSubProcessStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnSubProcessImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnSubProcessSimpleStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnDiagramStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnGatewayImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnGatewaySimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnGatewayStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnBoundaryEventStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnBoundaryEventSimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnBoundaryEventImageStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnEndEventImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnEndEventSimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnEndEventStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnStartEventImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnStartEventSimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnStartEventStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnIntermediateCatchEventImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnIntermediateCatchEventSimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnIntermediateCatchEventStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnIntermediateThrowEventImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnIntermediateThrowEventSimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnIntermediateThrowEventStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnDataObjectStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnDataSimpleStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnDataImageStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnDataAssociationStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnLaneStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnCallActivityStructuredStyleKeys.class);
        
        FactoryStyle.getInstance().declareProvider(GmBpmnSendTaskStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnServiceTaskStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmBpmnReceiveTaskStructuredStyleKeys.class);
    }

    @objid ("c618fbf7-59a6-11e2-ae45-002564c97630")
    private void declareFactorySettings() {
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorBpmn.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
