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
                                    

package org.modelio.property.ui.data;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Element;

@objid ("e8796b6c-9ea4-46df-b915-e29eb7153aa4")
public class DataPanelInput {
    @objid ("4dd34e0a-d2f7-4525-acbc-f5b62c56c4ef")
    private boolean showHiddenAnnotations;

    @objid ("400d60de-bd12-46d4-9bd9-18210fa6a8ff")
    private IProjectService projectService;

    @objid ("7f746195-5147-4ef3-a9f5-b281e65a53ad")
    private IMModelServices modelService;

    @objid ("84dc4a7c-ec74-42a3-afbf-3bf9d665edb5")
    private IModelioPickingService pickingService;

    @objid ("94832d78-66d5-496d-9e8c-3d557a79395d")
    private IActivationService activationService;

    /**
     * Model element whose annotations are edited.
     */
    @objid ("06d07e77-8dbd-4de4-9d25-807affbeeec7")
    private Element typedElement;

    /**
     * Part of annotations displayed in the right part.
     */
    @objid ("11d03ad9-be31-4e39-8877-c0386f392f54")
    private Object typingElement;

    /**
     * @return the projectService
     */
    @objid ("89be548a-6f4b-4846-a0bd-99c64b02377b")
    public IProjectService getProjectService() {
        return this.projectService;
    }

    /**
     * @param projectService the projectService to set
     */
    @objid ("7aea9820-3e91-41e3-b11c-ec3833950508")
    public void setProjectService(IProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * @return the modelService
     */
    @objid ("ab7c35a4-f664-47b9-9a82-59b28e9b1213")
    public IMModelServices getModelService() {
        return this.modelService;
    }

    /**
     * @param modelService the modelService to set
     */
    @objid ("2e4e0fbb-6393-4d6f-b176-bf7801d24f48")
    public void setModelService(IMModelServices modelService) {
        this.modelService = modelService;
    }

    /**
     * @return the pickingService
     */
    @objid ("8d1e712d-f353-4752-86c2-e2cef13d759d")
    public IModelioPickingService getPickingService() {
        return this.pickingService;
    }

    /**
     * @param pickingService the pickingService to set
     */
    @objid ("7a19d103-8c6f-4a00-b52e-55e3ab41bd3b")
    public void setPickingService(IModelioPickingService pickingService) {
        this.pickingService = pickingService;
    }

    /**
     * @return the activationService
     */
    @objid ("5f5b637d-fef9-4749-b1c9-cfea3f463816")
    public IActivationService getActivationService() {
        return this.activationService;
    }

    /**
     * @param activationService the activationService to set
     */
    @objid ("5ec20e53-4aaa-43f0-afb8-64725a2e3a0f")
    public void setActivationService(IActivationService activationService) {
        this.activationService = activationService;
    }

    /**
     * @return the typedElement
     */
    @objid ("f3eeb270-f834-4530-a896-bbed51d50f26")
    public Element getTypedElement() {
        return this.typedElement;
    }

    /**
     * @param typedElement the typedElement to set
     */
    @objid ("d3452517-4265-429e-a3fc-ab6a1d9365ae")
    public void setTypedElement(Element typedElement) {
        this.typedElement = typedElement;
    }

    /**
     * @return the typingElement
     */
    @objid ("e36e61f2-f869-41bc-88c8-c7e973c699db")
    public Object getTypingElement() {
        return this.typingElement;
    }

    /**
     * @param typingElement the typingElement to set
     */
    @objid ("337a6591-1a27-4ed0-9c3e-5f0fb796d729")
    public void setTypingElement(Object typingElement) {
        this.typingElement = typingElement;
    }

    /**
     * @param projectService
     * @param modelService
     * @param pickingService
     * @param activationService
     * @param typedElement Model element whose annotations are edited.
     * @param typingElement Part of annotations displayed in the data panel.
     */
    @objid ("98d05ffe-672f-4bed-89c0-eeba3dff1de2")
    public DataPanelInput(IProjectService projectService, IMModelServices modelService, IModelioPickingService pickingService, IActivationService activationService, Element typedElement, Object typingElement, boolean showHiddenAnnotations) {
        super();
        this.projectService = projectService;
        this.modelService = modelService;
        this.pickingService = pickingService;
        this.activationService = activationService;
        this.typedElement = typedElement;
        this.typingElement = typingElement;
        this.setShowHiddenAnnotations(showHiddenAnnotations);
    }

    @objid ("9a011839-1861-424b-8af0-13b01fb66a80")
    public DataPanelInput(DataPanelInput other) {
        this.projectService = other.projectService;
        this.modelService = other.modelService;
        this.pickingService = other.pickingService;
        this.activationService = other.activationService;
        this.typedElement = other.typedElement;
        this.typingElement = other.typingElement;
        this.setShowHiddenAnnotations(other.isShowHiddenAnnotations());
    }

    @objid ("7b9f4391-163e-495b-b884-3230f1ac80e1")
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.activationService == null) ? 0 : this.activationService.hashCode());
        result = prime * result + ((this.modelService == null) ? 0 : this.modelService.hashCode());
        result = prime * result + ((this.pickingService == null) ? 0 : this.pickingService.hashCode());
        result = prime * result + ((this.projectService == null) ? 0 : this.projectService.hashCode());
        result = prime * result + ((this.typedElement == null) ? 0 : this.typedElement.hashCode());
        result = prime * result + ((this.typingElement == null) ? 0 : this.typingElement.hashCode());
        return result;
    }

    @objid ("9b3fe3de-ce43-41b0-bac7-fc23231c26e5")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataPanelInput other = (DataPanelInput) obj;
        if (this.activationService == null) {
            if (other.activationService != null)
                return false;
        } else if (!this.activationService.equals(other.activationService))
            return false;
        if (this.modelService == null) {
            if (other.modelService != null)
                return false;
        } else if (!this.modelService.equals(other.modelService))
            return false;
        if (this.pickingService == null) {
            if (other.pickingService != null)
                return false;
        } else if (!this.pickingService.equals(other.pickingService))
            return false;
        if (this.projectService == null) {
            if (other.projectService != null)
                return false;
        } else if (!this.projectService.equals(other.projectService))
            return false;
        if (this.typedElement == null) {
            if (other.typedElement != null)
                return false;
        } else if (!this.typedElement.equals(other.typedElement))
            return false;
        if (this.typingElement == null) {
            if (other.typingElement != null)
                return false;
        } else if (!this.typingElement.equals(other.typingElement))
            return false;
        return true;
    }

    @objid ("c165d075-44c6-485b-8a56-749107521552")
    public boolean isShowHiddenAnnotations() {
        return this.showHiddenAnnotations;
    }

    @objid ("64e76aed-5069-4952-85b5-8407337ba7fd")
    public void setShowHiddenAnnotations(boolean showHiddenAnnotations) {
        this.showHiddenAnnotations = showHiddenAnnotations;
    }

}
