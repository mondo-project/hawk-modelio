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
                                    

package org.modelio.diagram.editor.statik.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.editor.statik.elements.StaticDiagramEditPartFactory;
import org.modelio.diagram.editor.statik.elements.StaticDiagramGmLinkFactory;
import org.modelio.diagram.editor.statik.elements.StaticDiagramGmNodeFactory;
import org.modelio.diagram.editor.statik.elements.activity.GmActivityImageStyleKeys;
import org.modelio.diagram.editor.statik.elements.activity.GmActivitySimpleStyleKeys;
import org.modelio.diagram.editor.statik.elements.activity.GmActivityStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.association.GmAssocStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.associationclass.ClassAssociationLinkStyleKeys;
import org.modelio.diagram.editor.statik.elements.bindinglink.BindingLinkStyleKeys;
import org.modelio.diagram.editor.statik.elements.bpmnbehavior.GmBpmnBehaviorImageStyleKeys;
import org.modelio.diagram.editor.statik.elements.bpmnbehavior.GmBpmnBehaviorSimpleStyleKeys;
import org.modelio.diagram.editor.statik.elements.bpmnbehavior.GmBpmnBehaviorStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.bpmnprocess.GmBpmnProcessImageStyleKeys;
import org.modelio.diagram.editor.statik.elements.bpmnprocess.GmBpmnProcessSimpleStyleKeys;
import org.modelio.diagram.editor.statik.elements.bpmnprocess.GmBpmnProcessStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.clazz.GmClassStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.collab.CollaborationStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.collabuse.CollaborationUseStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.collabuselink.CollabUseLinkStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.communicationinteraction.GmCommunicationInteractionImageStyleKeys;
import org.modelio.diagram.editor.statik.elements.communicationinteraction.GmCommunicationInteractionSimpleStyleKeys;
import org.modelio.diagram.editor.statik.elements.communicationinteraction.GmCommunicationInteractionStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.component.ComponentStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.componentRealization.GmComponentRealization;
import org.modelio.diagram.editor.statik.elements.connector.ConnectorLinkStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.datatype.DataTypeStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.elementimport.GmElementImportStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.enumeration.EnumStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.generalization.GmGeneralizationStyleKeys;
import org.modelio.diagram.editor.statik.elements.informationflowlink.InformationFlowLinkStyleKeys;
import org.modelio.diagram.editor.statik.elements.informationitem.InformationItemStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.instance.GmInstanceStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.instancelink.InstanceLinkStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.interaction.GmInteractionImageStyleKeys;
import org.modelio.diagram.editor.statik.elements.interaction.GmInteractionSimpleStyleKeys;
import org.modelio.diagram.editor.statik.elements.interaction.GmInteractionStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.interfaze.InterfaceStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.naryassoc.NAssocStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.naryconnector.NConnectorStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.narylink.NLinkStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.packageimport.PackageImportStyleKeys;
import org.modelio.diagram.editor.statik.elements.packagemerge.PackageMergeStyleKeys;
import org.modelio.diagram.editor.statik.elements.packaze.GmPackageStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.ports.GmPortStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.providedinterface.ProvidedInterfaceStyleKeys;
import org.modelio.diagram.editor.statik.elements.raisedexception.RaisedExceptionStyleKeys;
import org.modelio.diagram.editor.statik.elements.realization.GmInterfaceRealizationStyleKeys;
import org.modelio.diagram.editor.statik.elements.requiredinterface.RequiredInterfaceStyleKeys;
import org.modelio.diagram.editor.statik.elements.signal.GmSignalStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.statemachine.GmStateMachineImageStyleKeys;
import org.modelio.diagram.editor.statik.elements.statemachine.GmStateMachineSimpleStyleKeys;
import org.modelio.diagram.editor.statik.elements.statemachine.GmStateMachineStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.staticdiagram.GmStaticDiagramStyleKeys;
import org.modelio.diagram.editor.statik.elements.staticdiagramview.StaticDiagramViewStructuredStyleKeys;
import org.modelio.diagram.editor.statik.elements.substitution.GmSubstitution;
import org.modelio.diagram.editor.statik.elements.templatebinding.TemplateBindingStructuredStyleKeys;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the bpmn diagram configurer.
 */
@objid ("d8540744-5a76-11e2-9e33-00137282c51b")
public class StaticProcessor {
    @objid ("65828377-5bd5-11e2-9e33-00137282c51b")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for State diagram
        
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(ClassDiagram.class).getName(), null, new ClassDiagramConfigurer());
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(StaticDiagram.class).getName(), null, new StaticDiagramConfigurer());
        
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("6582837c-5bd5-11e2-9e33-00137282c51b")
    private void declareFactories() {
        // Register our edit part factory
        // ---------------------------------
        ModelioEditPartFactory.getInstance().registerFactory(StaticDiagramEditPartFactory.class.getName(),
                StaticDiagramEditPartFactory.getInstance());
        
        // Register gm link factory 
        GmLinkFactory.getInstance().registerFactory(StaticDiagramGmLinkFactory.class.getName(),
                StaticDiagramGmLinkFactory.getInstance());
        
        // Register gm node factory
        GmNodeFactory.getInstance().registerFactory(StaticDiagramGmNodeFactory.class.getName(),
                StaticDiagramGmNodeFactory.getInstance());
    }

    @objid ("6584e5ca-5bd5-11e2-9e33-00137282c51b")
    private void declareStyleProviders() {
        final FactoryStyle factory = FactoryStyle.getInstance();
        
        // Declare the StyleKey providers
        // ------------------------------
        
        // Association
        factory.declareProvider(GmAssocStructuredStyleKeys.class);
        factory.declareProvider(GmAssocStructuredStyleKeys.InfoFlows.class);
        factory.declareProvider(NAssocStructuredStyleKeys.class);
        factory.declareProvider(NAssocStructuredStyleKeys.InfoFlows.class);
        
        // Binding
        factory.declareProvider(BindingLinkStyleKeys.class);
        
        // Class
        factory.declareProvider(GmClassStructuredStyleKeys.class);
        factory.declareProvider(GmClassStructuredStyleKeys.Attribute.class);
        factory.declareProvider(GmClassStructuredStyleKeys.Operation.class);
        factory.declareProvider(GmClassStructuredStyleKeys.Inner.class);
        factory.declareProvider(GmClassStructuredStyleKeys.InternalStructure.class);
        
        // Class association
        factory.declareProvider(ClassAssociationLinkStyleKeys.class);
        
        // Collaboration
        factory.declareProvider(CollaborationStructuredStyleKeys.class);
        factory.declareProvider(CollaborationStructuredStyleKeys.Inner.class);
        factory.declareProvider(CollaborationStructuredStyleKeys.InternalStructure.class);
        
        // CollaborationUse
        factory.declareProvider(CollaborationUseStructuredStyleKeys.class);
        
        factory.declareProvider(CollabUseLinkStructuredStyleKeys.class);
        factory.declareProvider(CollabUseLinkStructuredStyleKeys.BindingsGroup.class);
        
        // Component
        factory.declareProvider(ComponentStructuredStyleKeys.class);
        factory.declareProvider(ComponentStructuredStyleKeys.Attribute.class);
        factory.declareProvider(ComponentStructuredStyleKeys.Operation.class);
        factory.declareProvider(ComponentStructuredStyleKeys.Inner.class);
        factory.declareProvider(ComponentStructuredStyleKeys.InternalStructure.class);
        
        // Component realization
        factory.declareProvider(GmComponentRealization.styleKeyProvider);
        
        // Connector
        factory.declareProvider(ConnectorLinkStructuredStyleKeys.class);
        factory.declareProvider(ConnectorLinkStructuredStyleKeys.InfoFlows.class);
        factory.declareProvider(NConnectorStructuredStyleKeys.class);
        factory.declareProvider(NConnectorStructuredStyleKeys.InfoFlows.class);
        
        // Datatype
        factory.declareProvider(DataTypeStructuredStyleKeys.class);
        factory.declareProvider(DataTypeStructuredStyleKeys.Attribute.class);
        factory.declareProvider(DataTypeStructuredStyleKeys.InternalStructure.class);
        factory.declareProvider(DataTypeStructuredStyleKeys.Operation.class);
        
        // Diagram
        factory.declareProvider(GmStaticDiagramStyleKeys.class);
        
        factory.declareProvider(StaticDiagramViewStructuredStyleKeys.class);
        
        // Enumeration
        factory.declareProvider(EnumStructuredStyleKeys.class);
        factory.declareProvider(EnumStructuredStyleKeys.Attribute.class);
        factory.declareProvider(EnumStructuredStyleKeys.Inner.class);
        factory.declareProvider(EnumStructuredStyleKeys.Litteral.class);
        factory.declareProvider(EnumStructuredStyleKeys.Operation.class);
        
        // MObject import
        factory.declareProvider(GmElementImportStructuredStyleKeys.class);
        
        
        
        // Package import
        factory.declareProvider(PackageImportStyleKeys.class);
        
        // Generalization
        factory.declareProvider(GmGeneralizationStyleKeys.class);
        
        // Information flow
        factory.declareProvider(InformationFlowLinkStyleKeys.class);
        factory.declareProvider(InformationFlowLinkStyleKeys.Items.class);
        
        // Information item
        factory.declareProvider(InformationItemStructuredStyleKeys.class);
        //factory.declareProvider(InformationItemStructuredStyleKeys.Represented.class);
        
        // Instance
        factory.declareProvider(GmInstanceStructuredStyleKeys.class);
        factory.declareProvider(GmInstanceStructuredStyleKeys.Slot.class);
        factory.declareProvider(GmInstanceStructuredStyleKeys.InternalStructure.class);
        
        // Interface
        factory.declareProvider(InterfaceStructuredStyleKeys.class);
        factory.declareProvider(InterfaceStructuredStyleKeys.Attribute.class);
        factory.declareProvider(InterfaceStructuredStyleKeys.Operation.class);
        factory.declareProvider(InterfaceStructuredStyleKeys.Inner.class);
        factory.declareProvider(InterfaceStructuredStyleKeys.InternalStructure.class);
        
        // InterfaceRealization
        factory.declareProvider(GmInterfaceRealizationStyleKeys.class);
        
        // Link
        factory.declareProvider(InstanceLinkStructuredStyleKeys.class);
        factory.declareProvider(InstanceLinkStructuredStyleKeys.InfoFlows.class);
        
        factory.declareProvider(NLinkStructuredStyleKeys.class);
        factory.declareProvider(NLinkStructuredStyleKeys.InfoFlows.class);
        
        // Package
        factory.declareProvider(GmPackageStructuredStyleKeys.class);
        
        // Package merge
        factory.declareProvider(PackageMergeStyleKeys.class);
        
        // Port
        factory.declareProvider(GmPortStructuredStyleKeys.class);
        
        // Provided interface
        factory.declareProvider(ProvidedInterfaceStyleKeys.class);
        
        // Raised exception
        factory.declareProvider(RaisedExceptionStyleKeys.class);
        
        // Required interface
        factory.declareProvider(RequiredInterfaceStyleKeys.class);
        
        // Signal
        factory.declareProvider(GmSignalStructuredStyleKeys.class);
        factory.declareProvider(GmSignalStructuredStyleKeys.Attribute.class);
        factory.declareProvider(GmSignalStructuredStyleKeys.Operation.class);
        factory.declareProvider(GmSignalStructuredStyleKeys.Inner.class);
        factory.declareProvider(GmSignalStructuredStyleKeys.InternalStructure.class);
        
        // Substitution
        factory.declareProvider(GmSubstitution.styleKeyProvider);
        
        // Template binding
        factory.declareProvider(TemplateBindingStructuredStyleKeys.class);
        
        // Activity
        factory.declareProvider(GmActivityStructuredStyleKeys.class);
        factory.declareProvider(GmActivityImageStyleKeys.class);
        factory.declareProvider(GmActivitySimpleStyleKeys.class);
        
        // BpmnBehavior
        factory.declareProvider(GmBpmnBehaviorStructuredStyleKeys.class);
        factory.declareProvider(GmBpmnBehaviorImageStyleKeys.class);
        factory.declareProvider(GmBpmnBehaviorSimpleStyleKeys.class);
        
        // BpmnProcess
        factory.declareProvider(GmBpmnProcessStructuredStyleKeys.class);
        factory.declareProvider(GmBpmnProcessImageStyleKeys.class);
        factory.declareProvider(GmBpmnProcessSimpleStyleKeys.class);
        
        // Interaction
        factory.declareProvider(GmInteractionStructuredStyleKeys.class);
        factory.declareProvider(GmInteractionImageStyleKeys.class);
        factory.declareProvider(GmInteractionSimpleStyleKeys.class);
        
        // CommunicationInteraction
        factory.declareProvider(GmCommunicationInteractionStructuredStyleKeys.class);
        factory.declareProvider(GmCommunicationInteractionImageStyleKeys.class);
        factory.declareProvider(GmCommunicationInteractionSimpleStyleKeys.class);
        
        // StateMachine
        factory.declareProvider(GmStateMachineStructuredStyleKeys.class);
        factory.declareProvider(GmStateMachineImageStyleKeys.class);
        factory.declareProvider(GmStateMachineSimpleStyleKeys.class);
    }

    @objid ("6584e5cc-5bd5-11e2-9e33-00137282c51b")
    private void declareFactorySettings() {
        // Load the default values. Do it only here, after key providers registration.
        BundleContext bundle = DiagramEditorStatik.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        StyleLoader loader = new StyleLoader();
        loader.load(url);
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
