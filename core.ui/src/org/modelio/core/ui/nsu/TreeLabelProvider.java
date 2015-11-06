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
                                    

package org.modelio.core.ui.nsu;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.metamodel.uml.infrastructure.Element;

@objid ("9961fc2f-ac84-46f6-a369-c173b17c680a")
public class TreeLabelProvider extends LabelProvider {
    @objid ("0d2a0e88-dbe6-49fc-aeee-74b8b93e0d25")
    @Override
    public String getText(final Object element) {
        return CauseLabelProvider.get((Element) element);
    }

    @objid ("0c41dfa2-7d2d-4864-943e-ccb3f3a014fc")
    @Override
    public Image getImage(final Object element) {
        return ElementImageService.getIcon((Element)element);
    }

}
