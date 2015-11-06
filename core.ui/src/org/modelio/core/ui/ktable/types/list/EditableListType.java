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
                                    

package org.modelio.core.ui.ktable.types.list;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import org.modelio.app.core.picking.IModelioPickingService;

/**
 * Defines a String list which can have new values added in its combo editor.
 */
@objid ("8deda4b2-c068-11e1-8c0a-002564c97630")
public class EditableListType extends ListType {
    @objid ("8deda4b3-c068-11e1-8c0a-002564c97630")
    public EditableListType(boolean acceptNullValue, List<String> values) {
        super(acceptNullValue, values);
    }

    @objid ("0a91cc5d-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        EditableComboCellEditor comboTextEditor = new EditableComboCellEditor();
        comboTextEditor.setItems(this.values.toArray(new String[this.values.size()]));
        return comboTextEditor;
    }

}
