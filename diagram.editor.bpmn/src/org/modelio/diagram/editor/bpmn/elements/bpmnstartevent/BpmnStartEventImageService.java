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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnstartevent;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;

@objid ("61b7ae06-55b6-11e2-877f-002564c97630")
public class BpmnStartEventImageService {
    @objid ("61b7ae07-55b6-11e2-877f-002564c97630")
    private String image_ref = null;

    @objid ("61b7ae08-55b6-11e2-877f-002564c97630")
    public static String getImageRef(GmModel gm_model) {
        BpmnStartEvent start_event = (BpmnStartEvent) gm_model.getRelatedElement();
        if (start_event != null && start_event.isValid()) {
            List<BpmnEventDefinition> definitions = start_event.getEventDefinitions();
        
            if (start_event.isIsInterrupting()) {
                if (definitions.size() == 1) {
                    BpmnEventDefinition definition = definitions.get(0);
                    return Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_" + definition.getMClass().getName();
                } else if (definitions.size() > 1) {
                    return Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_Multiple";
                }
            } else {
                if (definitions.size() == 1) {
                    BpmnEventDefinition definition = definitions.get(0);
                    return Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + definition.getMClass().getName();
                } else if (definitions.size() > 1) {
                    return Metamodel.getMClass(BpmnStartEvent.class).getName() + "_Multiple";
                }
            }
        }
        return Metamodel.getMClass(BpmnStartEvent.class).getName();
    }

    @objid ("61b7ae0f-55b6-11e2-877f-002564c97630")
    public boolean hasImageChange(GmModel model) {
        String new_ref = getImageRef(model);
        if (this.image_ref == null) {
            this.image_ref = new_ref;
            return false;
        } else {
            if (new_ref.equals(this.image_ref)) {
                return false;
            } else {
                this.image_ref = new_ref;
                return true;
            }
        }
    }

}
