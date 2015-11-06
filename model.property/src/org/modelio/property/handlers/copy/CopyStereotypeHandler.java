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
                                    

package org.modelio.property.handlers.copy;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.copy.PasteElementObject.PasteType;
import org.modelio.core.ui.copy.PasteElementObject;
import org.modelio.core.ui.copy.PasteElementTransfer;
import org.modelio.core.ui.copy.TransferItem;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.PropertyView;
import org.modelio.property.ui.ModelPropertyPanelProvider;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Copy selected stereotype(s) into the clipboard.
 */
@objid ("0141f15d-1e3e-45e1-8c36-87a4c8a5763e")
public class CopyStereotypeHandler {
    @objid ("4b85a669-1ec9-4ae6-b05b-65ed7c8b4e06")
     boolean ctrlFlag;

    @objid ("c9c105a1-38d4-4c0a-8d95-b18f95f096a7")
    @Inject
    protected IProjectService projectService;

    /**
     * @param part the current active part.
     * @return true if the handler can be executed.
     */
    @objid ("a27b6710-f9db-45f9-9457-05e1233f37a3")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        if (!(part.getObject() instanceof PropertyView)) {
            return false;
        }
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        if (propertyPanel == null) {
            return false;
        }
        Element parentElement = propertyPanel.getInput();
        if (parentElement == null) {
            return false;
        }
        
        List<ModelElement> selectedElements = propertyPanel.getSelectedTypeItems();
        if (selectedElements.isEmpty()) return false;
        for (ModelElement element : selectedElements) {            
            if (!(element instanceof Stereotype)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Copy the currently selected stereotype(s).
     * @param part the current active part.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("9a5daa31-ab46-421f-af1a-ac68ed8071c7")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        
        List<ModelElement> selectedElements = propertyPanel.getSelectedTypeItems();
        
        PasteElementObject toCopy = new PasteElementObject(PasteType.COPY);
        
        for (MObject element : selectedElements) {
            toCopy.addTransferedItems(new TransferItem(element, propertyPanel.getInput()));
        }
        
        Clipboard clipboard= new Clipboard(currentDisplay);
        clipboard.setContents(new Object[] { toCopy }, new Transfer[] { PasteElementTransfer.getInstance() });
    }

}
