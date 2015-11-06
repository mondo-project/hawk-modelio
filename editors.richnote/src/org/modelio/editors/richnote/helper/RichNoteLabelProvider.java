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
                                    

package org.modelio.editors.richnote.helper;

import java.nio.file.Path;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.editors.richnote.api.RichNoteFormat;
import org.modelio.editors.richnote.api.RichNoteFormatRegistry;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;

/**
 * Helper class to get the label and the icon of an {@link ExternDocument}.
 */
@objid ("a16dd783-ab5a-4516-918f-ed92ff83f273")
public class RichNoteLabelProvider {
    @objid ("d8aee85c-1451-4173-ba84-d22f908de772")
    private RichNoteLabelProvider() {
        // no instance
        throw new UnsupportedOperationException();
    }

    /**
     * Get the explorer icon of a external document.
     * <p>
     * The returned image is owned by a registry, may be used elsewhere and must <b>not</b> be disposed.
     * @param doc a document.
     * @return the icon.
     */
    @objid ("e5c8589a-eec4-4be3-8624-5a61c781331d")
    public static Image getIcon(final ExternDocument doc) {
        RichNoteFormat format = RichNoteFormatRegistry.getInstance().getDocumentFormatForMime(doc.getMimeType());
        return format != null ? format.getIcon() : null;
    }

    /**
     * Get the Windows explorer icon descriptor for a file.
     * <p>
     * Return <code>null</code> if the file has no registered extension or is a directory.
     * @param aFile a file.
     * @return the icon descriptor or <code>null</code>.
     */
    @objid ("3110b801-c154-4c5d-8634-9ed7682f89ca")
    public static ImageDescriptor getIcon(final Path aFile) {
        final String fname = aFile.getFileName().toString();
        final int idx = fname.lastIndexOf('.');
        
        if (idx != -1) {
            Program program = Program.findProgram(fname.substring(idx));
            if (program != null) {
                ImageData data = program.getImageData();
                if (data != null)
                    return ImageDescriptor.createFromImageData(data);
            }
        }
        return null;
    }

    /**
     * Get the label to display for an extern document.
     * @param document a document.
     * @return the label.
     */
    @objid ("f6d6a31e-97aa-49bd-b7ac-1d9f5921c8c5")
    public static String getLabel(final ExternDocument document) {
        String name = document.getName();
        
        final ExternDocumentType type = document.getType();
        if (type != null) {
            if (!name.isEmpty()) {
                name += " ";
            }
        
            final String label = ModuleI18NService.getLabel(type);
            if (!label.isEmpty()) {
                return name + "[" + label + "]";
            } else {
                return name + "[" + type.getName() + "]";
            }
        }
        return name;
    }

}
