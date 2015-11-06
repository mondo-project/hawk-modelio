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
                                    

package org.modelio.diagram.elements.umlcommon.externdocument;

import java.io.IOException;
import java.nio.file.FileSystemException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.linkednode.CreateLinkedNodeCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.plugin.DiagramElements;
import org.modelio.editors.richnote.api.RichNoteCreator;
import org.modelio.editors.richnote.gui.typechooser.PromptRichNoteDescriptorDlg;
import org.modelio.editors.richnote.gui.typechooser.RichNoteDescriptor;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * {@link GmNodeModel} creation command that:
 * <ul>
 * <li>creates and initialize the MObject if asked.
 * <li>creates the {@link GmNodeModel} and unmask it.
 * </ul>
 * according to the provided {@link ModelioCreationContext}.
 */
@objid ("814d269e-1dec-11e2-8cad-001ec947c8cc")
public class CreateExternDocumentCommand extends CreateLinkedNodeCommand {
    /**
     * @param context Modelio creation context
     */
    @objid ("814d26a0-1dec-11e2-8cad-001ec947c8cc")
    public CreateExternDocumentCommand(final ModelioCreationContext context) {
        super(context);
    }

    @objid ("814d26a5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected MObject createElement(final IModelFactory modelFactory, IElementNamer elementNamer) {
        Shell parentShell = Display.getDefault().getActiveShell();
        GmAbstractDiagram diagram = this.sourceNode.getDiagram();
        IMModelServices modelServices = diagram.getModelManager().getModelServices();
        
        PromptRichNoteDescriptorDlg dialog = new PromptRichNoteDescriptorDlg(parentShell,
                 new RichNoteDescriptor((ModelElement) this.parentElement),
                 modelServices);
        
        if (dialog.open() == Window.OK) {
             try {
                 RichNoteDescriptor descriptor = dialog.getRichNoteDescriptor();
                 ExternDocument newElement = modelFactory.createExternDocument(descriptor.getDocumentType(),
                         descriptor.getTargetElement(),
                         descriptor.getChosenMimeType());
        
                 // Some additional initializing steps might be needed.
                 newElement.setName(descriptor.getName());
                 newElement.setPath(descriptor.getPath());
                 newElement.setAbstract(descriptor.getAbstract());
                 RichNoteCreator.createRichNote(newElement);
        
                 return newElement;
             } catch (FileSystemException e) {
                 DiagramElements.LOG.error(e);
                 MessageDialog.openError(parentShell, "Error", FileUtils.getLocalizedMessage(e));
             } catch (IOException e) {
                 DiagramElements.LOG.error(e);
                 MessageDialog.openError(parentShell, "Error", e.getLocalizedMessage());
             }
         }
        return null;
    }

}
