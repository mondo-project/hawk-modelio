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
                                    

package org.modelio.app.core.inputpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Assert;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.advanced.impl.AdvancedFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.impl.BasicFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

@objid ("b9b73977-2fd8-11e2-a79f-bc305ba4815c")
public class InputPartService implements IInputPartService {
    @objid ("b9b73978-2fd8-11e2-a79f-bc305ba4815c")
    @Inject
    private MApplication application;

    @objid ("b9bbfc44-2fd8-11e2-a79f-bc305ba4815c")
    @Inject
    @Optional
    private MWindow workbenchWindow;

    @objid ("b9bbfc33-2fd8-11e2-a79f-bc305ba4815c")
    @Override
    public MInputPart createInputPart(String id) {
        MPartDescriptor descriptor = findDescriptor(id);
        return createInputPart(descriptor);
    }

    @objid ("b9bbfc39-2fd8-11e2-a79f-bc305ba4815c")
    @Override
    public MPlaceholder createSharedInputPart(String id, String inputURI) {
        MWindow sharedWindow = getModelioWindow();
        // Do we already have the part to share?
        MInputPart sharedPart = null;
        
        // check for existing parts if necessary
        
        for (MUIElement element : sharedWindow.getSharedElements()) {
            if (element.getElementId().equals(id) && element instanceof MInputPart) {
                MInputPart part = (MInputPart) element;       
                if(part.getInputURI() != null && part.getInputURI().equals(inputURI)){
                    sharedPart = part; 
                    break;
                }   
            }
        }
        
        if (sharedPart == null) {
            MPartDescriptor descriptor = findDescriptor(id);
            sharedPart = createInputPart(descriptor);
            if (sharedPart == null) {
                return null;
            }
            sharedPart.setInputURI(inputURI);
        
            sharedWindow.getSharedElements().add(sharedPart);
        }
        return createSharedPart(sharedPart);
    }

    @objid ("b9bbfc3f-2fd8-11e2-a79f-bc305ba4815c")
    private static MPlaceholder createSharedPart(MInputPart sharedPart) {
        // Create and return a reference to the shared part
        MPlaceholder sharedPartRef = AdvancedFactoryImpl.eINSTANCE.createPlaceholder();
        sharedPartRef.setElementId(sharedPart.getElementId());
        sharedPartRef.setRef(sharedPart);
        return sharedPartRef;
    }

    @objid ("b9bbfc45-2fd8-11e2-a79f-bc305ba4815c")
    private MWindow getModelioWindow() {
        if (this.workbenchWindow != null)
            return this.workbenchWindow;
        if (this.application.getSelectedElement() != null)
            return this.application.getSelectedElement();
        List<MWindow> windows = this.application.getChildren();
        if (windows.size() != 0)
            return windows.get(0);
        return null;
    }

    @objid ("b9bbfc49-2fd8-11e2-a79f-bc305ba4815c")
    private MPartDescriptor findDescriptor(String id) {
        for (MPartDescriptor descriptor : this.application.getDescriptors()) {
            if (descriptor.getElementId().equals(id)) {
                return descriptor;
            }
        }
        return null;
    }

    @objid ("b9bbfc4e-2fd8-11e2-a79f-bc305ba4815c")
    private static MInputPart createInputPart(MPartDescriptor descriptor) {
        if (descriptor == null) {
            return null;
        }
        
        MInputPart part = BasicFactoryImpl.eINSTANCE.createInputPart();
        part.setElementId(descriptor.getElementId());
        part.getMenus().addAll(EcoreUtil.copyAll(descriptor.getMenus()));
        if (descriptor.getToolbar() != null) {
            part.setToolbar((MToolBar) EcoreUtil.copy((EObject) descriptor.getToolbar()));
        }
        part.setContributorURI(descriptor.getContributorURI());
        part.setCloseable(descriptor.isCloseable());
        part.setContributionURI(descriptor.getContributionURI());
        part.setLabel(descriptor.getLabel());
        part.setIconURI(descriptor.getIconURI());
        part.setTooltip(descriptor.getTooltip());
        part.getHandlers().addAll(EcoreUtil.copyAll(descriptor.getHandlers()));
        part.getTags().addAll(descriptor.getTags());
        part.getBindingContexts().addAll(descriptor.getBindingContexts());
        return part;
    }

    @objid ("793755c0-3334-11e2-95fe-001ec947c8cc")
    @Override
    public MPart showInputPart(String id, String inputURI, PartState partState) {
        Assert.isNotNull(id);
        Assert.isNotNull(partState);
        
        MPart part = getInputPart(id, inputURI);
        if (part == null) {
            MPartDescriptor descriptor = findDescriptor(id);
            part = createInputPart(descriptor);
            if (part == null) {
                return null;
            }
            ((MInputPart)part).setInputURI(inputURI);
        }
        return getPartService().showPart(part, partState);
    }

    @objid ("303322d8-3ff6-4aac-b978-9da3bd64327e")
    @Override
    public void hideInputPart(MPart part) {
        //Force to show the part before hidding it (actually the part cannot be hidden if it is not visible in the current perspective)
        getPartService().showPart(part, PartState.VISIBLE); 
        getPartService().hidePart(part, true);
    }

    @objid ("659a254f-d1f7-4e51-a121-a46fcbd1a50b")
    @Override
    public Collection<? extends MPart> getInputParts(String id) {
        List<MPart> mParts = new ArrayList<>();
        for (MPart part : getPartService().getParts()) {
            if (part.getElementId().equals(id)) {
                mParts.add(part);
            }
        }
        return mParts;
    }

    @objid ("f27c9c88-d9e4-4d06-8b63-3a7ac4c45507")
    @Override
    public MPart getInputPart(String id, String inputURI) {
        for (MPart inputPart : getPartService().getInputParts(inputURI)) {
            if (id.equals(inputPart.getElementId())) {
                return inputPart;
            }
        }
        return null;
    }

    /**
     * Get the EPartService from the Modelio window.
     * <p>
     * Warning: do not inject the EPartService, it could be broken if a modal dialog is running.
     * </p>
     * @return the current EPartService.
     */
    @objid ("0529ae92-b7e1-4f6a-bed8-ac29948786e8")
    private EPartService getPartService() {
        return getModelioWindow().getContext().get(EPartService.class);
    }

}
