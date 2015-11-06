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
                                    

package org.modelio.audit.view.providers.byelement;

import java.text.SimpleDateFormat;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.modelio.audit.engine.core.IAuditEntry;
import org.modelio.audit.view.model.AuditElementModel;
import org.modelio.core.ui.images.ElementImageService;

@objid ("9a1ac88b-a495-48a7-b342-b2b3c8fd7099")
public class NumberedElementLabelProvier extends StyledCellLabelProvider {
    @objid ("5d2dca81-a823-4314-bb18-65b9779c6719")
    private final SimpleDateFormat formatter = new SimpleDateFormat("H:mm:ss");

    @objid ("0672105d-d785-47f9-9763-cb7c3598917d")
    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        StyledString text = new StyledString();
        
        if (element instanceof AuditElementModel) {
            AuditElementModel entry = (AuditElementModel) element;
            cell.setImage(ElementImageService.getIcon(entry.element));
            text.append(entry.element.getName());
            text.append(" (" + entry.entrys.size() + ")",StyledString.COUNTER_STYLER);       
        }else if(element instanceof IAuditEntry){
            IAuditEntry entry = (IAuditEntry) element;
            text.append(this.formatter.format(entry.getTimestamp()));
        }
        cell.setText(text.toString());
        cell.setStyleRanges(text.getStyleRanges());
        super.update(cell);
    }

}
