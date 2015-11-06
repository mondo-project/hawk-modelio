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
                                    

package org.modelio.diagram.editor.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.inputpart.IInputPartService;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * A manager for the diagram editors. This class is responsible for handling the activation request on diagram, by either opening a
 * new diagram editor for said diagram if none already exists, or if on is found by bringing it to top so that it is visible.<br>
 */
@objid ("6670d446-33f7-11e2-95fe-001ec947c8cc")
public class DiagramEditorsManager {
    @objid ("98e02547-0d22-44f8-a91b-3b5ee448a64e")
    public Map<AbstractDiagram , MPart> editors = new HashMap<>();

    @objid ("083bd59f-afa8-4a9c-9017-d699c967c57e")
    private static DiagramEditorsManager instance;

    @objid ("6670d448-33f7-11e2-95fe-001ec947c8cc")
    @Inject
    @Optional
    void onEditElement(@EventTopic(ModelioEventTopics.EDIT_ELEMENT) MObject mObject, IDiagramConfigurerRegistry configurerRegistry, final IInputPartService inputPartService, final DiagramEditorsManager manager) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        
        // Only handle activation requests for diagrams.
        if (!(mObject instanceof AbstractDiagram) || !mObject.isValid()) {
            return;
        }
        final AbstractDiagram diagram = (AbstractDiagram) mObject;
        
        // Get the configurers associated with the metaclass/stereotypes.
        String metaclassName = diagram.getMClass().getName();
        List<String> stereotypes = new ArrayList<>();
        for (Stereotype stereo : diagram.getExtension()) {
            stereotypes.add(stereo.getName());
        }
        List<IDiagramConfigurer> configurers = configurerRegistry.getConfigurers(metaclassName, stereotypes);
        String editorId = null;
        for (IDiagramConfigurer configurer : configurers) {
            if (configurer.getContributionURI() != null && !configurer.getContributionURI().isEmpty()) {
                editorId = configurer.getContributionURI();
            }
        }
        if (editorId == null) {
            DiagramEditor.LOG.error("Unsupported diagram type:" + metaclassName);
            return;
        }
        
        final String finalEditorId = editorId;
        
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                // Request the opening (or re-activation/bring-to-top/get-focus) of a Diagram Editor
                String inputUri = diagram.getUuid().toString();
                MPart openedPart = inputPartService.showInputPart(finalEditorId, inputUri, PartState.ACTIVATE);
        
                String label = diagram.getName();
                openedPart.setLabel(label);
        
                // Set icon
                // Hack: we only have an Image, not an URL. We have to get the SWT widget (in an ugly way) and set it manually. 
                Object widget = openedPart.getWidget();
                try {
                    Method m = widget.getClass().getMethod("getParent");
                    if (m != null) {
                        Object parent = m.invoke(widget);
                        if (parent instanceof CTabFolder) {
                            CTabFolder tabFolder = (CTabFolder) parent;
                            for (CTabItem tabItem : tabFolder.getItems()) {
                                if (tabItem.getImage() == null && label.equals(tabItem.getText())) {
                                    tabItem.setImage(ElementImageService.getIcon(diagram));
                                }
                            }
                        }
                    }
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    // Implementation probably changed, image isn't set
                }
        
                manager.put(diagram, openedPart);
            }
        });
        return;
    }

    @objid ("6670d450-33f7-11e2-95fe-001ec947c8cc")
    @Execute
    static void execute(IEclipseContext context) {
        DiagramEditorsManager newInstance = DiagramEditorsManager.getInstance();
        ContextInjectionFactory.inject(newInstance, context);
        context.set(DiagramEditorsManager.class,newInstance);
        context.set(ToolRegistry.class, ContextInjectionFactory.make(ToolRegistry.class, context));
    }

    @objid ("025f7abb-22a8-4203-ab6d-f764ea1793cd")
    public void put(AbstractDiagram iDiagram, MPart editor) {
        if (!this.editors.containsKey(iDiagram)) {
            this.editors.put(iDiagram, editor);
        }
    }

    @objid ("bfd0f1c9-78cb-4a62-aa56-75873c350ba8")
    public void remove(AbstractDiagram iDiagram) {
        if (this.editors.containsKey(iDiagram)) {
            this.editors.remove(iDiagram);
        }
    }

    @objid ("74f65215-eb9a-4198-9b01-ea9db7632807")
    public MPart get(AbstractDiagram abstractDiagram) {
        return this.editors.get(abstractDiagram);
    }

    @objid ("4c4f57a5-0fba-41cf-857c-d896d7043d18")
    public void closeAll(IInputPartService inputPartService) {
        // Close all opened diagram editors.
        HashMap<AbstractDiagram, MPart> copy = new HashMap<>(this.editors);
        for (MPart editor : copy.values()) {            
            // close the editor.
            inputPartService.hideInputPart(editor);
        }
        this.editors.clear();
    }

    @objid ("fd3fc81c-a3a1-47d8-bc9a-4aa087dc3c52")
    @Inject
    @Optional
    void onProjectClosed(@SuppressWarnings("unused")
@Optional
@EventTopic(ModelioEventTopics.PROJECT_CLOSING) final GProject project, final IInputPartService inputPartService) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().syncExec(new Runnable() {
        
            @Override
            public void run() {
                // close all diagram editors when closing the project
                closeAll(inputPartService);
            }
        });
    }

    @objid ("d95de5b5-afad-444f-b4a2-fc29eb498b15")
    public static synchronized DiagramEditorsManager getInstance() {
        if(instance == null){
            instance = new DiagramEditorsManager();
        }
        return instance;
    }

}
