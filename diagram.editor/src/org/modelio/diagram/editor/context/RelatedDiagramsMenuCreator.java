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
                                    

package org.modelio.diagram.editor.context;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.diagram.editor.plugin.DiagramEditor;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("95357df5-2744-4db4-80b5-2b6ab4372fa8")
public class RelatedDiagramsMenuCreator {
    @objid ("1e4df3dd-6b6a-4407-a61c-b233f2e20f40")
    @Inject
    protected MApplication application;

    @objid ("7d2a7cc3-0159-4dae-872d-2c2d24daac1a")
    @AboutToShow
    public void aboutToShow(List<MMenuElement> items) {
        // Get current selection                   
        final ModelElement selectedElement = getSelectedElement();
        
        if (selectedElement != null) {
            // Get diagrams to display
            final List<AbstractDiagram> relatedDiagrams = getRelatedDiagrams(selectedElement);
            if (relatedDiagrams.size() == 0) {
                // No diagrams to relate to
                return;
            }
            
            // Add the related diagrams menu
            items.add(createMenu(relatedDiagrams));
        }
    }

    @objid ("fb7d1e18-ca47-4080-ba87-7bbd74288700")
    public MMenu createMenu(final List<AbstractDiagram> relatedDiagrams) {
        final String contributorId = "platform:/plugin/" + DiagramEditor.PLUGIN_ID;
        
        // create a new menu
        MMenu elementCreationMenu = MMenuFactory.INSTANCE.createMenu();
        elementCreationMenu.setLabel(DiagramEditor.I18N.getString("RelatedDiagramsMenu.label"));
        elementCreationMenu.setIconURI(contributorId + "/icons/relateddiagram.png");
        
        // make the menu visible
        elementCreationMenu.setEnabled(true);
        elementCreationMenu.setToBeRendered(true);
        elementCreationMenu.setVisible(true);
        
        // bound the menu to the contributing plugin 
        elementCreationMenu.setContributorURI(contributorId);
        
        // add related diagram items
        elementCreationMenu.getChildren().addAll(createDiagramItems(relatedDiagrams, contributorId));
        return elementCreationMenu;
    }

    @objid ("ba85750a-0458-4469-9c32-1a3edb84116c")
    public List<AbstractDiagram> getRelatedDiagrams(final ModelElement currentModelElement) {
        final List<AbstractDiagram> relatedDiagrams = new ArrayList<>();
        for (final Dependency dependency : currentModelElement.getDependsOnDependency()) {
            if (dependency.isStereotyped("ModelerModule", "related_diagram")) {
                final ModelElement relatedElement = dependency.getDependsOn();
                if (relatedElement instanceof AbstractDiagram && ! relatedDiagrams.contains(relatedElement)) {
                    relatedDiagrams.add((AbstractDiagram) relatedElement);
                }
            }
        }
        return relatedDiagrams;
    }

    @objid ("c97151d6-21b1-41f1-b067-10a84eb289fe")
    private List<MMenuElement> createDiagramItems(List<AbstractDiagram> relatedDiagrams, String contributorId) {
        List<MMenuElement> ret = new ArrayList<>();
        for (AbstractDiagram diagram : relatedDiagrams) {
            ret.add(createDiagramItems(diagram, contributorId));
        }
        return ret;
    }

    @objid ("ced07e16-3c49-427e-a131-7ff7707c5674")
    private MMenuElement createDiagramItems(AbstractDiagram diagram, String contributorId) {
        // create a new handled item
        MHandledMenuItem relatedDiagramItem = MMenuFactory.INSTANCE.createHandledMenuItem();
        relatedDiagramItem.setLabel(diagram.getName());
        relatedDiagramItem.setIconURI("platform:/plugin/org.modelio.core.ui/icons/mmimages/" + diagram.getMClass().getName().toLowerCase() + ".png");
        
        // make the menu visible
        relatedDiagramItem.setEnabled(true);
        relatedDiagramItem.setToBeRendered(true);
        relatedDiagramItem.setVisible(true);
        
        // bound the menu to the contributing plugin 
        relatedDiagramItem.setContributorURI(contributorId);
        
        // set the command
        MCommand command = getCommand("org.modelio.app.ui.command.openrelateddiagram");
        relatedDiagramItem.setCommand(command);
        
        // add the opened diagram as parameter
        MParameter p = MCommandsFactory.INSTANCE.createParameter();
        p.setName("org.modelio.app.ui.command.parameter.related_diagram");
        p.setValue(diagram.getUuid().toString());
        relatedDiagramItem.getParameters().add(p);
        return relatedDiagramItem;
    }

    @objid ("51aba02e-59b7-4b5e-9779-e59a5dade8f9")
    private MCommand getCommand(String commandId) {
        for (MCommand c : this.application.getCommands()) {
            if (commandId.equals(c.getElementId())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get the currently selected element, or <code>null</code> if the selection size is not equal to one.
     * @return the selected element.
     */
    @objid ("3c32cacb-f1eb-4709-acea-c4959a571faf")
    protected ModelElement getSelectedElement() {
        // Get the active selection from the application, to avoid context-related issues when opening the same diagram several times...
        IStructuredSelection selection = (IStructuredSelection) this.application.getContext().get(IServiceConstants.ACTIVE_SELECTION);
        if (selection.size() != 1) {
            return null;
        }
        
        final Object obj = selection.getFirstElement();
        if (obj instanceof ModelElement) {
            return (ModelElement) obj;
        } else if (obj instanceof IAdaptable) {
            IAdaptable adaptable = (IAdaptable) obj;
            return (ModelElement) adaptable.getAdapter(ModelElement.class);
        }
        return null;
    }

}
