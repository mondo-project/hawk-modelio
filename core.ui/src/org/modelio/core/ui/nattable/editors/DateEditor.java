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
                                    

package org.modelio.core.ui.nattable.editors;

import java.util.Date;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer.MoveDirectionEnum;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

@objid ("375f6520-2909-4c82-a6b1-586e13c777bb")
public class DateEditor extends AbstractCellEditor {
    @objid ("f98196fd-201e-41b2-b2de-f4a2e72bacec")
     CDateTime cdt;

    @objid ("f7243420-9e5b-4397-b038-b3f21875a6b4")
    @Override
    public Object getEditorValue() {
        return this.displayConverter.canonicalToDisplayValue(this.cdt.getSelection());
    }

    @objid ("f50cea2f-6745-4f3e-adfc-d8b956d4ec49")
    @Override
    public void setEditorValue(Object value) {
        this.cdt.setSelection((Date) this.displayConverter.displayToCanonicalValue(value));
    }

    @objid ("b1abf8e3-f866-4333-ba7c-be5eea6dc9e8")
    @Override
    public Control getEditorControl() {
        return this.cdt;
    }

    @objid ("49205602-eade-41d5-b126-2b6d2f7dcecd")
    @Override
    public CDateTime createEditorControl(Composite parentComposite) {
        return new CDateTime(parentComposite, CDT.DATE_MEDIUM | CDT.DROP_DOWN) {
            @Override
            protected void postClose(Shell popup) {
                super.postClose(popup);
                DateEditor.this.commit(MoveDirectionEnum.NONE, true);
            }
        };
    }

    @objid ("e2c40099-4c8a-45d0-a82e-6f2c328241c1")
    @Override
    protected Control activateCell(Composite parentComposite, Object originalCanonicalValue) {
        this.cdt = createEditorControl(parentComposite);
        this.cdt.setSelection((originalCanonicalValue != null) ? (Date) originalCanonicalValue : new Date());
        return this.cdt;
    }

    @objid ("7bc2e21d-94de-4053-8db8-77944819fd0b")
    @Override
    public boolean openInline(IConfigRegistry registry, List<String> configLabels) {
        return super.openInline(registry, configLabels);
    }

    @objid ("fe0c54a2-4d9b-431f-a128-e7b4b2984f13")
    @Override
    public void close() {
        super.close();
        this.cdt = null;
    }

}
