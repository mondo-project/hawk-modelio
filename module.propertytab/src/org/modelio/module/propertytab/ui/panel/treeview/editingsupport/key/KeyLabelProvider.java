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
                                    

package org.modelio.module.propertytab.ui.panel.treeview.editingsupport.key;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.TreeItem;
import org.modelio.module.propertytab.model.ModuleProperty;
import org.modelio.module.propertytab.ui.panel.treeview.ModulePropertyContentProvider.Category;
import org.modelio.ui.UIColor;

@objid ("c8951e50-1eba-11e2-9382-bc305ba4815c")
public class KeyLabelProvider extends StyledCellLabelProvider {
    @objid ("c8951e51-1eba-11e2-9382-bc305ba4815c")
    public KeyLabelProvider() {
    }

    @objid ("c8951e53-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void update(ViewerCell cell) {
        if(cell.getElement() instanceof ModuleProperty){
            ModuleProperty element = (ModuleProperty)cell.getElement();
            
            String label = element.getLabel();
                
            if(element.getCategory() != null){
                label = label.substring(label.indexOf("]") + 1);  
            }
            
            cell.setText(label); 
        }else if(cell.getElement() instanceof Category){
            Category element = (Category)cell.getElement();
            cell.setText(element.label); 
        }
        
        if (cell.getItem() instanceof TreeItem) {
            TreeItem item = (TreeItem) cell.getItem();
            if (item.getParent().indexOf(item) % 2 == 0) {
                cell.setBackground(UIColor.TABLE_EVENROW_BG);
            } else {
                cell.setBackground(UIColor.TABLE_ODDROW_BG);
            }
        }
        return;
    }

    @objid ("c8954562-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void dispose() {
        super.dispose();
    }

}
