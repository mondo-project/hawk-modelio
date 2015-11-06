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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

@objid ("945bdce3-3743-44f9-8a41-1803ef9a36ad")
public class TimeEditor extends AbstractCellEditor {
    @objid ("5a315bad-799b-4ad6-8f05-2c96804c9a89")
     CDateTime cdt;

    @objid ("5ced1efc-dbc2-4825-afe1-c592e57aa65e")
    @Override
    public Object getEditorValue() {
        return this.displayConverter.canonicalToDisplayValue(this.cdt.getSelection());
    }

    @objid ("d736e24b-8a66-44ee-9372-d14644395a31")
    @Override
    public void setEditorValue(Object value) {
        this.cdt.setSelection((Date) this.displayConverter.displayToCanonicalValue(value));
    }

    @objid ("1b23d4c6-c7f4-4621-82a2-6c4104bd4838")
    @Override
    public Control getEditorControl() {
        return this.cdt;
    }

    @objid ("66279a48-6e4d-4d29-b18a-ef304c1f1744")
    @Override
    public CDateTime createEditorControl(Composite parentComposite) {
        return new CDateTime(parentComposite, CDT.TIME_MEDIUM | CDT.TAB_FIELDS | CDT.TEXT_TRAIL | SWT.LEFT);
    }

    @objid ("ff09672f-040f-4989-82a5-a7ef9f26a897")
    @Override
    protected Control activateCell(Composite parentComposite, Object originalCanonicalValue) {
        this.cdt = createEditorControl(parentComposite);
        
        this.cdt.setSelection((originalCanonicalValue != null) ? (Date) originalCanonicalValue : new Date());
        return this.cdt;
    }

    @objid ("8d33bc80-81f1-47ba-9127-516948b5cb41")
    @Override
    public boolean openInline(IConfigRegistry registry, List<String> configLabels) {
        return super.openInline(registry, configLabels);
    }

    @objid ("b035bad1-953b-4c39-8513-1dbada2630eb")
    @Override
    public void close() {
        super.close();
        this.cdt = null;
    }

}
