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
                                    

package org.modelio.core.ui.treetable.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * Notes about design: <br/>
 * Unfortunately, the jface standard ComboBoxCellEditor is not designed for being subclassed.</br>
 * As we need to specialize several of its features, we had to use a modified copy of the original ComboBoxCellEditor (ComboBoxCellEditor3)
 * 
 * @author pvlaemyn
 */
@objid ("6b348c04-1eba-11e2-9382-bc305ba4815c")
public class LabelsComboBoxCellEditor extends ComboBoxCellEditor2 {
    @objid ("87b4fea3-1eba-11e2-9382-bc305ba4815c")
    private List<String> types;

    @objid ("6b34b314-1eba-11e2-9382-bc305ba4815c")
    public LabelsComboBoxCellEditor(Composite parent, String[] labels, int style) {
        super(parent, labels, style);
        this.types = new ArrayList<>(Arrays.asList(labels));
        ((CCombo) this.getControl()).setEditable(false);
        setActivationStyle(ComboBoxCellEditor2.DROP_DOWN_ON_MOUSE_ACTIVATION);
    }

    @objid ("6b34da25-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected void doSetValue(Object value) {
        int index = this.types.indexOf(value);
        if(index == -1){
            index = 0;
        }
        super.doSetValue(new Integer(index));
    }

    @objid ("6b350132-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected Object doGetValue() {
        return this.types.get((Integer) super.doGetValue());
    }

}
