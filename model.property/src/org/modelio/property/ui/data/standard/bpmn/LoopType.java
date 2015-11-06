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
                                    

package org.modelio.property.ui.data.standard.bpmn;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.activities.BpmnLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnMultiInstanceLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnStandardLoopCharacteristics;
import org.modelio.vcore.smkernel.SmObjectImpl;

/**
 * Enum for Activity LoopType property
 */
@objid ("8e67b6cf-c068-11e1-8c0a-002564c97630")
public enum LoopType {
    None,
    Standard,
    MultiInstanceSequential,
    MultiInstanceParallel;

    /**
     * Get Loop Characteristics
     * @param definition
     * LoopCharacteristics
     * @return LoopType
     */
    @objid ("8e67b6d5-c068-11e1-8c0a-002564c97630")
    public static LoopType getType(BpmnActivity activity) {
        BpmnLoopCharacteristics definition = activity.getLoopCharacteristics();
        if (definition instanceof BpmnStandardLoopCharacteristics) {
            return Standard;
        } else if (definition instanceof BpmnMultiInstanceLoopCharacteristics) {
            BpmnMultiInstanceLoopCharacteristics multi = (BpmnMultiInstanceLoopCharacteristics) definition;
            if (multi.isIsSequencial()) {
                return MultiInstanceSequential;
            } else {
                return MultiInstanceParallel;
            }
        }
        return None;
    }

    /**
     * Update Loop Characteristics
     * @param type type
     * @param activity activity
     */
    @objid ("8e67b6dd-c068-11e1-8c0a-002564c97630")
    public static void setType(IMModelServices modelService, LoopType type, BpmnActivity activity) {
        // Delete the old loop type
        BpmnLoopCharacteristics ch = activity.getLoopCharacteristics();
        if (ch != null) {
            ((SmObjectImpl) ch).delete();
        }
        
        switch (type) {
        case Standard: 
            BpmnStandardLoopCharacteristics newStandardEvent = modelService.getModelFactory().createBpmnStandardLoopCharacteristics();
            newStandardEvent.setOwnerActivity(activity);
            break;
        case MultiInstanceParallel:
            BpmnMultiInstanceLoopCharacteristics newMultiInstanceParallelEvent = modelService.getModelFactory().createBpmnMultiInstanceLoopCharacteristics();
            newMultiInstanceParallelEvent.setOwnerActivity(activity);
            newMultiInstanceParallelEvent.setIsSequencial(false);
            break;
        case MultiInstanceSequential:
            BpmnMultiInstanceLoopCharacteristics newMultiInstanceSequential = modelService.getModelFactory().createBpmnMultiInstanceLoopCharacteristics();
            newMultiInstanceSequential.setOwnerActivity(activity);
            newMultiInstanceSequential.setIsSequencial(true);
            break;
        case None:
            // Nothing to do, the old type is already deleted
            break;
        default:
            break;
        }
    }

}
