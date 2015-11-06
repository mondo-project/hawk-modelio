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
                                    

package org.modelio.property.ui.data.stereotype;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.core.ui.nattable.editors.TextIconDisplayAdapter;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("412263de-bc7b-40c1-bf46-2ab30b6a9a9d")
public class DefaultLabelProvider extends LabelProvider {
    @objid ("eb1234ba-65c8-46b5-b875-0b62bee94eb6")
    private Class<?> type;

    @objid ("974c3d72-b707-47cb-9faf-fc8defb6919f")
    public DefaultLabelProvider(Class<?> type) {
        this.type = type;
    }

    @objid ("92f42245-252b-46f6-8ea9-1ce4b9a12f41")
    @Override
    public Image getImage(Object element) {
        return (element instanceof MObject) ? ElementImageService.getIcon((MObject) element) : null;
    }

    @objid ("b5447ec9-9f95-4673-b15e-1ec2d239f151")
    @Override
    public String getText(Object element) {
        return TextIconDisplayAdapter.convertToString(this.type, element);
    }

}
