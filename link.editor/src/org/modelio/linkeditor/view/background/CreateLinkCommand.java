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
                                    

package org.modelio.linkeditor.view.background;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.api.MTools;
import org.modelio.linkeditor.view.LinkEditorView;
import org.modelio.linkeditor.view.background.typeselection.TypeSelectionModel;
import org.modelio.linkeditor.view.background.typeselection.TypeSelectionPopup;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command used by the DropEditPolicy to create a link.
 * 
 * @author fpoyer
 */
@objid ("1b8ee04d-5e33-11e2-b81d-002564c97630")
public class CreateLinkCommand extends Command {
    @objid ("1b8ee052-5e33-11e2-b81d-002564c97630")
    private final MObject from;

    @objid ("1b8ee053-5e33-11e2-b81d-002564c97630")
    private final MObject to;

    @objid ("bd5bd20f-806d-48d9-a266-a267681100e1")
    private final List<Object> newLinkTypes;

    @objid ("c7084642-49cd-4e19-b243-54eb56ec8376")
    private static Object linkTypeToUse = null;

    /**
     * C'tor.
     * @param from the origin
     * @param to the destination
     * @param newLinkTypes the type(s) of the link to create, can be any mix of {@link AssociationEnd}.class, {@link ElementImport} .class,
     * {@link Generalization}.class, {@link Dependency}.class and Stereotype(s) that can be applied to a dependency;
     */
    @objid ("e6088f98-5efd-11e2-a8be-00137282c51b")
    public CreateLinkCommand(final MObject from, final MObject to, final List<Object> newLinkTypes) {
        this.from = from;
        this.to = to;
        this.newLinkTypes = newLinkTypes;
        CreateLinkCommand.linkTypeToUse = null;
    }

    @objid ("e6088fa3-5efd-11e2-a8be-00137282c51b")
    @Override
    public boolean canExecute() {
        boolean isFromModifiable = this.from.getStatus().isModifiable();
        boolean canLink = true;
        if (this.newLinkTypes.size() == 0) {
            canLink = true;
        } else if (this.newLinkTypes.size() == 1) {
            Object newLinkType = this.newLinkTypes.get(0);
            if (newLinkType instanceof Class) {
                if (AssociationEnd.class.isAssignableFrom((Class<?>) newLinkType)) {
                    canLink = MTools.getLinkTool().canLink(Metamodel.getMClass(AssociationEnd.class), this.from, this.to);
                } else if (Dependency.class.isAssignableFrom((Class<?>) newLinkType)) {
                    canLink = MTools.getLinkTool().canLink(Metamodel.getMClass(Dependency.class), this.from, this.to);
                } else if (ElementImport.class.isAssignableFrom((Class<?>) newLinkType)) {
                    canLink = MTools.getLinkTool().canLink(Metamodel.getMClass(ElementImport.class), this.from, this.to);
                } else if (Generalization.class.isAssignableFrom((Class<?>) newLinkType)) {
                    canLink = MTools.getLinkTool().canLink(Metamodel.getMClass(Generalization.class), this.from, this.to);
                } else if (InterfaceRealization.class.isAssignableFrom((Class<?>) newLinkType)) {
                    canLink = MTools.getLinkTool().canLink(Metamodel.getMClass(InterfaceRealization.class), this.from, this.to);
                }
            } else if (newLinkType instanceof Stereotype) {
                canLink = MTools.getLinkTool().canLink(((Stereotype) newLinkType), Metamodel.getMClass(Dependency.class),
                        this.from, this.to);
            }
        }
        // else {
        // // Several possible link types, unable to decide before user
        // // makes an active choice... in doubt, accept.
        // canLink = true;
        // }
        return isFromModifiable && canLink;
    }

    @objid ("e60af1f3-5efd-11e2-a8be-00137282c51b")
    @Override
    public void execute() {
        // TODO model service shouldn't be accessed in a static way :(
        final IMModelServices modelServices = LinkEditorView.modelServices;
        IModelFactory factory = modelServices.getModelFactory();
        IElementNamer namer = modelServices.getElementNamer();
        
        if (this.newLinkTypes.size() == 0) {
            if (CreateLinkCommand.linkTypeToUse == null) {
                // No usable link type, abort.
                return;
            }
            // else {
            // // Nothing to do here, just use previously set link type.
            // }
        } else if (this.newLinkTypes.size() == 1) {
            // Simplest case, just set the type of link to use.
            CreateLinkCommand.linkTypeToUse = this.newLinkTypes.get(0);
        
        } else {
            // Open disambiguation popup.
            TypeSelectionModel model = new TypeSelectionModel(this.newLinkTypes);
            TypeSelectionPopup popup = new TypeSelectionPopup(Display.getDefault().getActiveShell(), model);
            popup.setBlockOnOpen(true);
            if (popup.open() == IDialogConstants.OK_ID) {
                // Set type of link to use then proceed.
                CreateLinkCommand.linkTypeToUse = model.getSelectedType();
            } else {
                // User cancelled.
                CreateLinkCommand.linkTypeToUse = null;
            }
        }
        MObject newElement = null;
        if (CreateLinkCommand.linkTypeToUse instanceof Class<?>) {
            if (AssociationEnd.class.isAssignableFrom((Class<?>) CreateLinkCommand.linkTypeToUse)) {
                newElement = factory.createAssociation((Classifier) this.from, (Classifier) this.to, "");
                for (AssociationEnd end : ((Association) newElement).getEnd()) {
                    if (end.isNavigable()) {
                        end.setName(namer.getUniqueName(end));
                    }
                }
            } else if (ElementImport.class.isAssignableFrom((Class<?>) CreateLinkCommand.linkTypeToUse)) {
                if (this.from instanceof NameSpace) {
                    newElement = factory.createElementImport((NameSpace) this.from, (NameSpace) this.to);
                } else {
                    newElement = factory.createElementImport((Operation) this.from, (NameSpace) this.to);
                }
            } else if (Generalization.class.isAssignableFrom((Class<?>) CreateLinkCommand.linkTypeToUse)) {
                newElement = factory.createGeneralization((NameSpace) this.from, (NameSpace) this.to);
            } else if (InterfaceRealization.class.isAssignableFrom((Class<?>) CreateLinkCommand.linkTypeToUse)) {
                newElement = factory.createInterfaceRealization((NameSpace) this.from, (Interface) this.to);
            } else if (Dependency.class.isAssignableFrom((Class<?>) CreateLinkCommand.linkTypeToUse)) {
                Dependency dependency = factory.createDependency();
                dependency.setImpacted((ModelElement) this.from);
                dependency.setDependsOn((ModelElement) this.to);
                newElement = dependency;
            }
        
        } else if (CreateLinkCommand.linkTypeToUse instanceof Stereotype) {
            Dependency dependency = factory.createDependency();
            dependency.setImpacted((ModelElement) this.from);
            dependency.setDependsOn((ModelElement) this.to);
            dependency.getExtension().add((Stereotype) CreateLinkCommand.linkTypeToUse);
            newElement = dependency;
        }
        if (newElement != null) {
            newElement.setName(namer.getUniqueName(newElement));
        }
    }

}
