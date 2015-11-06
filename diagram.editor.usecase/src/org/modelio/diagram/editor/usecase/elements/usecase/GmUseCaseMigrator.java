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
                                    

package org.modelio.diagram.editor.usecase.elements.usecase;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.usecase.elements.usecase.v0._GmUseCase;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.persistence.IPersistent;
import org.modelio.diagram.persistence.IPersistentMigrator;
import org.modelio.diagram.styles.core.StyleKey;

@objid ("5e5f8cc1-55b7-11e2-877f-002564c97630")
public class GmUseCaseMigrator implements IPersistentMigrator {
    @objid ("5e5f8cc5-55b7-11e2-877f-002564c97630")
    @Override
    public IPersistent createInstanceOfMajorVersion(final int majorVersionToInstantiate) {
        switch (majorVersionToInstantiate) {
            case 0: {
                return new _GmUseCase();
            }
            default: {
                return null;
            }
        }
    }

    @objid ("5e5f8cce-55b7-11e2-877f-002564c97630")
    @Override
    public IPersistent migrate(final IPersistent instanceToMigrate) {
        if (instanceToMigrate.getMajorVersion() == 0) {
            return migrateFromV0(instanceToMigrate);
        }
        return null;
    }

    @objid ("5e5f8cda-55b7-11e2-877f-002564c97630")
    private IPersistent migrateFromV0(final IPersistent instanceToMigrate) {
        _GmUseCase oldUseCase = (_GmUseCase) instanceToMigrate;
        
        GmUseCase newUseCase = new GmUseCase(oldUseCase);
        
        newUseCase.setLayoutData(oldUseCase.getLayoutData());
        
        newUseCase.setRoleInComposition(oldUseCase.getRoleInComposition());
        
        GmUseCasePrimaryNode newPrimaryNode = newUseCase.getMainNode();
        for (IGmLink link : oldUseCase.getStartingLinks()) {
            oldUseCase.removeStartingLink(link);
            newPrimaryNode.addStartingLink(link);
        }
        for (IGmLink link : oldUseCase.getEndingLinks()) {
            oldUseCase.removeEndingLink(link);
            newPrimaryNode.addEndingLink(link);
        }
        
        newUseCase.getStyle().setCascadedStyle(oldUseCase.getStyle().getCascadedStyle());
        for (StyleKey key : oldUseCase.getStyle().getLocalKeys()) {
            newUseCase.getStyle().setProperty(key, oldUseCase.getStyle().getProperty(key));
        }
        
        oldUseCase.delete();
        return newUseCase;
    }

}
