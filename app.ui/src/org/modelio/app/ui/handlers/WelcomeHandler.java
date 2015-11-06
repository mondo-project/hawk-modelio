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
                                    

package org.modelio.app.ui.handlers;

import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.modelio.app.ui.persp.IModelioUiService;

/**
 * @author phv
 */
@objid ("377404ed-33e4-4678-ab58-7f3eecf6bc7d")
public class WelcomeHandler {
    @objid ("6c0bc7e0-56bf-45f6-bbfa-c289d1503e15")
    @Inject
    private EPartService partService;

    @objid ("4dc3dd4b-1163-471f-9d39-ba868013d567")
    @Execute
    public void execute(IModelioUiService pm) {
        pm.showWelcome(true);
        
        MPart part = partService.findPart("org.modelio.app.ui.welcome.part");
        
        if (part == null) {
            part = partService.showPart("org.modelio.app.ui.welcome.part", PartState.ACTIVATE);
        } else {
            partService.showPart(part, PartState.ACTIVATE);
        }
    }

    @objid ("05d849bb-eec4-4cb2-8844-978d40668122")
    @CanExecute
    boolean canExecute(IModelioUiService pm) {
        return true;
    }

}
