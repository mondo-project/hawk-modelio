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
                                    

package org.modelio.diagram.editor.communication.elements.communicationnode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.communication.elements.communicationnode.v0._GmCommunicationNode;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.persistence.IPersistent;
import org.modelio.diagram.persistence.IPersistentMigrator;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Migrator class for GmCommunicationNode.
 * 
 * @author fpoyer
 */
@objid ("7a5a712f-55b6-11e2-877f-002564c97630")
public class GmCommunicationNodeMigrator implements IPersistentMigrator {
    @objid ("7a5a7133-55b6-11e2-877f-002564c97630")
    @Override
    public IPersistent createInstanceOfMajorVersion(final int majorVersionToInstantiate) {
        switch (majorVersionToInstantiate) {
            case 0: {
                return new _GmCommunicationNode();
            }
            default: {
                return null;
            }
        }
    }

    @objid ("7a5a713b-55b6-11e2-877f-002564c97630")
    @Override
    public IPersistent migrate(final IPersistent instanceToMigrate) {
        if (instanceToMigrate.getMajorVersion() == 0) {
            return migrateFromV0(instanceToMigrate);
        }
        return null;
    }

    @objid ("7a5a7146-55b6-11e2-877f-002564c97630")
    private IPersistent migrateFromV0(final IPersistent instanceToMigrate) {
        _GmCommunicationNode oldNode = (_GmCommunicationNode) instanceToMigrate;
        
        GmCommunicationNode newNode = new GmCommunicationNode(oldNode);
        
        newNode.setLayoutData(oldNode.getLayoutData());
        
        newNode.setRoleInComposition(oldNode.getRoleInComposition());
        
        GmCommunicationNodePrimaryNode newPrimaryNode = newNode.getMainNode();
        for (IGmLink link : oldNode.getStartingLinks()) {
            oldNode.removeStartingLink(link);
            newPrimaryNode.addStartingLink(link);
        }
        for (IGmLink link : oldNode.getEndingLinks()) {
            oldNode.removeEndingLink(link);
            newPrimaryNode.addEndingLink(link);
        }
        
        newNode.getStyle().setCascadedStyle(oldNode.getStyle().getCascadedStyle());
        for (StyleKey key : oldNode.getStyle().getLocalKeys()) {
            newNode.getStyle().setProperty(key, oldNode.getStyle().getProperty(key));
        }
        
        oldNode.delete();
        return newNode;
    }

}
