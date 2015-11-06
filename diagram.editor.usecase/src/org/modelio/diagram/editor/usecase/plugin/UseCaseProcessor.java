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
                                    

package org.modelio.diagram.editor.usecase.plugin;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Execute;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.editor.usecase.elements.UseCaseDiagramEditPartFactory;
import org.modelio.diagram.editor.usecase.elements.UseCaseGmLinkFactory;
import org.modelio.diagram.editor.usecase.elements.UseCaseGmNodeFactory;
import org.modelio.diagram.editor.usecase.elements.actor.GmActorStructuredStyleKeys;
import org.modelio.diagram.editor.usecase.elements.system.GmSystemStyleKeys;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCaseStructuredStyleKeys;
import org.modelio.diagram.editor.usecase.elements.usecasedependency.GmUseCaseDependencyStyleKeys;
import org.modelio.diagram.editor.usecase.elements.usecasediagram.GmUseCaseDiagramStyleKeys;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.elements.gmfactory.GmLinkFactory;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.UseCaseDiagram;
import org.osgi.framework.BundleContext;

@objid ("7c1c400b-5eff-11e2-b9cc-001ec947c8cc")
public class UseCaseProcessor {
    @objid ("7c1c400d-5eff-11e2-b9cc-001ec947c8cc")
    @Execute
    private void execute(IDiagramConfigurerRegistry configurerRegistry) {
        configurerRegistry.registerDiagramConfigurer(Metamodel.getMClass(UseCaseDiagram.class).getName(), null, new UseCaseDiagramConfigurer());
        
        // Register our edit part factory
        declareFactories();
        
        // Declare our supported StyleKeys 
        declareStyleProviders();
        
        // Load the default values. Do it only here, after key providers registration.
        declareFactorySettings();
    }

    @objid ("7c1c4011-5eff-11e2-b9cc-001ec947c8cc")
    private void declareFactories() {
        ModelioEditPartFactory.getInstance().registerFactory(UseCaseDiagramEditPartFactory.class.getName(),
                UseCaseDiagramEditPartFactory.getInstance());
        
        GmLinkFactory.getInstance().registerFactory(UseCaseGmLinkFactory.class.getName(),
                UseCaseGmLinkFactory.getInstance());
        
        GmNodeFactory.getInstance().registerFactory(UseCaseGmNodeFactory.class.getName(),
                UseCaseGmNodeFactory.getInstance());
    }

    @objid ("7c1c4013-5eff-11e2-b9cc-001ec947c8cc")
    private void declareStyleProviders() {
        // Actor
        FactoryStyle.getInstance().declareProvider(GmActorStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmActorStructuredStyleKeys.Attribute.class);
        FactoryStyle.getInstance().declareProvider(GmActorStructuredStyleKeys.Operation.class);
        FactoryStyle.getInstance().declareProvider(GmActorStructuredStyleKeys.InternalStructure.class);
        
        // Use Case
        FactoryStyle.getInstance().declareProvider(GmUseCaseStructuredStyleKeys.class);
        FactoryStyle.getInstance().declareProvider(GmUseCaseStructuredStyleKeys.Attribute.class);
        FactoryStyle.getInstance().declareProvider(GmUseCaseStructuredStyleKeys.Operation.class);
        FactoryStyle.getInstance().declareProvider(GmUseCaseStructuredStyleKeys.ExtensionPoint.class);
        FactoryStyle.getInstance().declareProvider(GmUseCaseStructuredStyleKeys.Inner.class);
        FactoryStyle.getInstance().declareProvider(GmUseCaseStructuredStyleKeys.InternalStructure.class);
        
        // Use Case Dependency
        FactoryStyle.getInstance().declareProvider(GmUseCaseDependencyStyleKeys.class);
        
        // Use Case Diagram
        FactoryStyle.getInstance().declareProvider(GmUseCaseDiagramStyleKeys.class);
        
        // System
        FactoryStyle.getInstance().declareProvider(GmSystemStyleKeys.class);
    }

    @objid ("7c1c4015-5eff-11e2-b9cc-001ec947c8cc")
    private void declareFactorySettings() {
        // Load the default values.
        StyleLoader loader = new StyleLoader();
        BundleContext bundle = DiagramEditorUseCase.getContext();
        URL url = FileLocator.find(bundle.getBundle(), new Path("res/factory.settings"), null);
        
        loader.load(url);
        
        FactoryStyle.getInstance().injectDefaultValues(loader.getStyleProperties());
    }

}
