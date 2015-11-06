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
                                    

package org.modelio.diagram.editor.deployment.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.deployment.editor.DeploymentDiagramConfigurer;
import org.modelio.diagram.editor.deployment.elements.DeploymentDiagramEditPartFactory;
import org.modelio.diagram.editor.deployment.elements.DeploymentDiagramGmLinkFactory;
import org.modelio.diagram.editor.deployment.elements.DeploymentDiagramGmNodeFactory;
import org.modelio.diagram.editor.deployment.elements.artifact.GmArtifactStructuredStyleKeys;
import org.modelio.diagram.editor.deployment.elements.deploymentdiagram.GmDeploymentDiagramStyleKeys;
import org.modelio.diagram.editor.deployment.elements.manifestation.GmManifestationStyleKeys;
import org.modelio.diagram.editor.deployment.elements.node.GmNodeStructuredStyleKeys;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.DeploymentDiagram;
import org.osgi.framework.BundleContext;

/**
 * Processor registering the bpmn diagram configurer.
 */
@objid ("bebd9066-5bf2-11e2-a156-00137282c51b")
public class DeploymentProcessor {
    @objid ("ebf98df1-5bf2-11e2-a156-00137282c51b")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        // Register a diagram configurer for State diagram
        
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(DeploymentDiagram.class).getName(), null, new DeploymentDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("ec00b4ff-5bf2-11e2-a156-00137282c51b")
    private void declareFactories() {
        // Register our edit part factory
        ModelioEditPartFactory.getInstance()
                              .registerFactory(DeploymentDiagramEditPartFactory.class.getName(),
                                               DeploymentDiagramEditPartFactory.getInstance());
        
        // Register gm link factory 
        GmLinkFactory.getInstance().registerFactory(DeploymentDiagramGmLinkFactory.class.getName(),
                                                    DeploymentDiagramGmLinkFactory.getInstance());
        
        // Register gm node factory
        GmNodeFactory.getInstance().registerFactory(DeploymentDiagramGmNodeFactory.class.getName(),
                                                    DeploymentDiagramGmNodeFactory.getInstance());
    }

    @objid ("ec00b501-5bf2-11e2-a156-00137282c51b")
    private void declareStyleProviders() {
        // Artifact
           FactoryStyle.getInstance().declareProvider(GmArtifactStructuredStyleKeys.class);
           FactoryStyle.getInstance().declareProvider(GmArtifactStructuredStyleKeys.Attribute.class);
           FactoryStyle.getInstance().declareProvider(GmArtifactStructuredStyleKeys.Inner.class);
           FactoryStyle.getInstance().declareProvider(GmArtifactStructuredStyleKeys.InternalStructure.class);
           FactoryStyle.getInstance().declareProvider(GmArtifactStructuredStyleKeys.Operation.class);
           
           // Node
           FactoryStyle.getInstance().declareProvider(GmNodeStructuredStyleKeys.class);
           FactoryStyle.getInstance().declareProvider(GmNodeStructuredStyleKeys.Attribute.class);
           FactoryStyle.getInstance().declareProvider(GmNodeStructuredStyleKeys.Inner.class);
           FactoryStyle.getInstance().declareProvider(GmNodeStructuredStyleKeys.InternalStructure.class);
           FactoryStyle.getInstance().declareProvider(GmNodeStructuredStyleKeys.Operation.class);
           
           // Manifestation
           FactoryStyle.getInstance().declareProvider(GmManifestationStyleKeys.class);
           
           // Deployment Diagram
           FactoryStyle.getInstance().declareProvider(GmDeploymentDiagramStyleKeys.class);
    }

    @objid ("ec00b503-5bf2-11e2-a156-00137282c51b")
    private void declareFactorySettings() {
        // Load the default values. Do it only here, after key providers registration.
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorDeployment.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
