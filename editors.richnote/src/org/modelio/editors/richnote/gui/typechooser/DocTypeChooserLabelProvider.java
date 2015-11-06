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
                                    

package org.modelio.editors.richnote.gui.typechooser;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.editors.richnote.plugin.EditorsRichNote;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("a3cb7538-b1f7-4732-a51f-75adf160af91")
class DocTypeChooserLabelProvider extends LabelProvider {
    @objid ("7517f40a-6c59-426d-b61b-2359b2ac1133")
    public DocTypeChooserLabelProvider() {
    }

    @objid ("eb6134dc-e852-4cd7-8d86-061dc74ee8bf")
    @Override
    public Image getImage(Object element) {
        if (element instanceof AdapterModule) {
            // it is a Module
            AdapterModule adapter = (AdapterModule)element;
            ModuleComponent module = adapter.getMdac();
            return getModuleImage(module);
        } else if (element instanceof AdapterRichNoteType) {
            // It is a rich note type
            ExternDocumentType docType = ((AdapterRichNoteType)element).getDocType();
        
            return getDocumentTypeImage(docType);
        } else if (element instanceof AdapterStereotype) {
            // It is a stereotype
            AdapterStereotype stereotypeAdapter = (AdapterStereotype)element;
            Stereotype stereotype = stereotypeAdapter.getStereotype();
            
            Image image = ModuleI18NService.getIcon(getModuleOwner(stereotype), stereotype);
        
            if (image == null) {
                image = MetamodelImageService.getIcon(stereotype.getMClass());
            }
        
            return image;
        }
        return null;
    }

    @objid ("b8b83347-97b4-4a9b-a36c-ce2dd1fc961d")
    @Override
    public String getText(Object element) {
        if (element instanceof AdapterModule) {
            AdapterModule adapter = (AdapterModule) element;
            
            return ModuleI18NService.getLabel(adapter.getMdac());
        } else if (element instanceof AdapterRichNoteType) {
            StringBuffer noteTypeLabel = new StringBuffer();
            AdapterRichNoteType adapter = (AdapterRichNoteType) element;
        
            String label = ModuleI18NService.getLabel(adapter.getDocType());
            if (!label.isEmpty()) {
                noteTypeLabel.append(label);
            } else {
                noteTypeLabel.append(adapter.getDocType().getName());
            }
        
            return noteTypeLabel.toString();
        } else if (element instanceof AdapterStereotype) {
            StringBuffer noteTypeLabel = new StringBuffer();
            AdapterStereotype adapter = (AdapterStereotype) element;
        
            noteTypeLabel.append("<<");
            String label = ModuleI18NService.getLabel(adapter.getStereotype());
            if (!"".equals(label)) {
                noteTypeLabel.append(label);
            } else {
                noteTypeLabel.append(adapter.getStereotype().getName());
            }
            noteTypeLabel.append(">>");
            
            return noteTypeLabel.toString();
        } else {
            return element.toString();
        }
    }

    @objid ("2703ad18-26d0-4050-9b5f-e2a7916a55e4")
    private Image getModuleImage(ModuleComponent moduleModel) {
        Image moduleImage = ModuleI18NService.getModuleImage(moduleModel);
        
        if (moduleImage != null)
            return moduleImage;
        else
            return MetamodelImageService.getIcon(moduleModel.getMClass());
    }

    /**
     * Get the icon the the rich note type.
     * @param docType the rich note type
     */
    @objid ("9885814f-a09e-4000-ae07-cf2e1893dd8a")
    private Image getDocumentTypeImage(ExternDocumentType docType) {
        // TODO missing service to ask the module for the rich note type image.
        // return ModuleI18NService.getIcon(getModuleOwner(docType), docType);
        return MetamodelImageService.getIcon(docType.getMClass());
    }

    @objid ("ed4d5834-dfb1-414e-a8ce-3ffa36e0de6b")
    private static ModuleComponent getModuleOwner(MObject stereotype) {
        MObject parent = stereotype.getCompositionOwner();
        while (parent != null && ! (parent instanceof ModuleComponent))
            parent = parent.getCompositionOwner();
        
        if (parent == null) {
            EditorsRichNote.LOG.warning(stereotype+" is directly or indirectly orphan.");
        }
        return (ModuleComponent) parent;
    }

}
