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
                                    

package org.modelio.audit.view.dialog.auditEntry;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * This class provide the labels and the images to display for each linked element that is present in an audit entry.
 */
@objid ("61421ab6-0045-4473-8b3c-034cc11e87e8")
public class LinkedElementLabelProvider extends LabelProvider {
    /**
     * The modeling session.
     */
    @objid ("b6b6b73e-577f-4c32-87bb-487a20c82d3e")
    private ICoreSession modelingSession = null;

    /**
     * @param modelingSession The modeling session.
     */
    @objid ("c2b086b9-e51a-4759-907f-86d74119fc86")
    public LinkedElementLabelProvider(ICoreSession modelingSession) {
        this.modelingSession = modelingSession;
    }

    /**
     * Return the image of the linked model element.
     */
    @objid ("894bb535-3582-4cca-8294-35a714433874")
    @Override
    public Image getImage(Object object) {
        if (object instanceof Element) {
            Element element = (Element) object;
            return ElementImageService.getIcon(element);
        } else {
            return null;
        }
    }

    /**
     * Return the label of the linked model element.
     */
    @objid ("1859c9fa-a72c-4380-a9db-95edced71b48")
    @Override
    public String getText(Object object) {
        if (object instanceof Element) {
            Element element = (Element) object;
            AuditElementLabelService elementLabelService = new AuditElementLabelService();
            return elementLabelService.getLabel(element);
        } else {
            return "";
        }
    }

}
