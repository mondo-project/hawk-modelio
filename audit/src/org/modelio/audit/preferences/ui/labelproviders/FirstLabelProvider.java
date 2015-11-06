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
                                    

package org.modelio.audit.preferences.ui.labelproviders;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.modelio.audit.preferences.model.AuditCategory;
import org.modelio.audit.preferences.model.AuditRule;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.ui.UIColor;

/**
 * Provide checkbox label
 */
@objid ("b8dc1e05-d39c-4dd8-a7a8-9e6111a6ebac")
public class FirstLabelProvider extends ColumnLabelProvider {
    @objid ("e88598d5-5d34-440c-8174-43d9873c29bc")
    private static final Color DISABLED_COLOR = new Color(Display.getCurrent(),  160,160,160);

    @objid ("764c2137-717a-411f-b3f9-1df257b7782c")
    public FirstLabelProvider() {
    }

    @objid ("acc54876-ef32-4fe1-8b7b-07695e6e4d20")
    @Override
    public Image getImage(Object element) {
        if(element instanceof AuditCategory){
            switch (((AuditCategory)element).getName()) {
            case "Activity":
                return MetamodelImageService.getIcon(Metamodel.getMClass(Activity.class));
            case "Analyst":
                return MetamodelImageService.getIcon(Metamodel.getMClass(RequirementContainer.class));
            case "BPMN":
                return MetamodelImageService.getIcon(Metamodel.getMClass(BpmnBehavior.class));
            case "Behaviour":
                return MetamodelImageService.getIcon(Metamodel.getMClass(Behavior.class));
            case "Sequence":
                return MetamodelImageService.getIcon(Metamodel.getMClass(Interaction.class));
            case "State":
                return MetamodelImageService.getIcon(Metamodel.getMClass(StateMachine.class));
            case "Static":
                return MetamodelImageService.getIcon(Metamodel.getMClass(org.modelio.metamodel.uml.statik.Class.class));
            case "UseCase":
                return MetamodelImageService.getIcon(Metamodel.getMClass(UseCase.class));
            case "Others":
                // No icon yet...
            default:
                break;
        
            }
        }
        return null;
    }

    @objid ("d7d79e6e-457d-4353-9ba4-ffac659b1ce5")
    @Override
    public void update(ViewerCell cell) {
        super.update(cell);
    }

    @objid ("2cbfe214-5671-4d49-8a6f-117bfcba188a")
    @Override
    public String getText(Object element) {
        if(element instanceof AuditCategory){
            return ((AuditCategory)element).getName();
        }else if(element instanceof AuditRule){
            return ((AuditRule) element).ruleId;
        }
        return "";
    }

    @objid ("d47dbca6-2f1c-4ce9-8639-5dc62b781afc")
    @Override
    public Color getForeground(Object element) {
        if(element instanceof AuditRule){
            if(((AuditRule)element).enabled){
                return UIColor.BLACK;
            }  
            
           return DISABLED_COLOR;
        }
        return super.getForeground(element);
    }

}
