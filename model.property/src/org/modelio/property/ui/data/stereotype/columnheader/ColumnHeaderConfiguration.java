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
                                    

package org.modelio.property.ui.data.stereotype.columnheader;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.BeveledBorderDecorator;
import org.eclipse.nebula.widgets.nattable.resize.action.AutoResizeColumnAction;
import org.eclipse.nebula.widgets.nattable.resize.action.ColumnResizeCursorAction;
import org.eclipse.nebula.widgets.nattable.resize.event.ColumnResizeEventMatcher;
import org.eclipse.nebula.widgets.nattable.resize.mode.ColumnResizeDragMode;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.ui.action.ClearCursorAction;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.modelio.core.ui.nattable.editors.TextIconConverter;
import org.modelio.core.ui.nattable.editors.TextIconPainter;
import org.modelio.property.ui.data.stereotype.data.TableDataModel;

/**
 * Configuration for the column headers of property table editor. The configuration is dynamically computed from the properties of
 * the propertyModel being displayed in the editor.
 * 
 * Supported types and labels:
 * 
 * HSTRING = horizontal text, non-editable <br/>
 */
@objid ("a74be5c4-9e39-46f9-90d3-81a532a9506c")
public class ColumnHeaderConfiguration implements IConfiguration {
    @objid ("0d6557ed-e8b3-423e-a049-0f97abcad808")
    public static final String HSTRING = "HSTRING";

    @objid ("fa7435ee-8f95-42ac-9274-ccbbe38dc69e")
    public static final String VSTRING = "VSTRING";

    @objid ("568b14c3-2c3f-44fc-98ec-5f1ba09c6a2b")
    public static final String CUSTOM = "CUSTOM";

    @objid ("56a5fd01-ca39-42bb-ba3a-bee4f5bab41e")
    protected TableDataModel dataModel;

    @objid ("ddde72fd-403e-4327-85d1-a421ef122ed3")
    private final DataLayer columnHeaderDataLayer;

    @objid ("23dff76c-a167-4b83-90ee-90ef4b46562b")
    public ColumnHeaderConfiguration(TableDataModel dataModel, final DataLayer columnHeaderDataLayer) {
        this.dataModel = dataModel;
        this.columnHeaderDataLayer = columnHeaderDataLayer;
    }

    @objid ("6b64dd2b-9890-4df9-9ae8-d76700a4bfdc")
    @Override
    public void configureLayer(ILayer layer) {
        ColumnOverrideLabelAccumulator acc = (ColumnOverrideLabelAccumulator) this.columnHeaderDataLayer
                .getConfigLabelAccumulator();
        
        if (acc == null) {
            this.columnHeaderDataLayer.setConfigLabelAccumulator(new ColumnOverrideLabelAccumulator(this.columnHeaderDataLayer));
            acc = (ColumnOverrideLabelAccumulator) this.columnHeaderDataLayer.getConfigLabelAccumulator();
        }
        
        // Fill columns from data model
        int col = 0;
        for (@SuppressWarnings("unused")
        final Object colObject : this.dataModel.getColumns()) {
            acc.registerColumnOverridesOnTop(col, HSTRING, CUSTOM);
            col++;
        }
    }

    @objid ("785af08c-146a-4b39-b0b4-dd045ef68bb3")
    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        configureRegistryForHSTRING(configRegistry);
        configureLabelProvider(configRegistry);
    }

    @objid ("298842a4-144f-4574-8d49-2f47bc696a8a")
    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        // Mouse move - Show col resize cursor
        uiBindingRegistry.registerFirstMouseMoveBinding(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 0),
                new ColumnResizeCursorAction());
        
        // Mouse move - Clear cursor
        uiBindingRegistry.registerMouseMoveBinding(new MouseEventMatcher(), new ClearCursorAction());
        
        // Column resize
        uiBindingRegistry.registerFirstMouseDragMode(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 1),
                new ColumnResizeDragMode());
        uiBindingRegistry.registerDoubleClickBinding(new ColumnResizeEventMatcher(SWT.NONE, GridRegion.COLUMN_HEADER, 1),
                new AutoResizeColumnAction());
    }

    @objid ("a9ebda4c-03ab-49af-8d88-0ac5057ef908")
    private void configureRegistryForHSTRING(IConfigRegistry configRegistry) {
        // Cell Painter
        final ICellPainter horizontalTextPainter = new BeveledBorderDecorator(new TextIconPainter());
        
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, horizontalTextPainter, DisplayMode.NORMAL,
                HSTRING);
        
        // Cell style
        final Style hCellStyle = new Style();
        hCellStyle.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_WIDGET_BACKGROUND);
        hCellStyle.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, GUIHelper.COLOR_WIDGET_FOREGROUND);
        hCellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_BACKGROUND_COLOR, GUIHelper.COLOR_WHITE);
        hCellStyle.setAttributeValue(CellStyleAttributes.GRADIENT_FOREGROUND_COLOR, GUIHelper.getColor(136, 212, 215));
        hCellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        hCellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.MIDDLE);
        hCellStyle.setAttributeValue(CellStyleAttributes.BORDER_STYLE, null);
        hCellStyle.setAttributeValue(CellStyleAttributes.FONT, GUIHelper.getFont(new FontData("Verdana", 10, SWT.NORMAL)));
        
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, hCellStyle, DisplayMode.NORMAL, HSTRING);
        
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.NEVER_EDITABLE,
                DisplayMode.EDIT, HSTRING);
    }

    @objid ("a4309b7b-06f5-4288-bd09-894d11868bfb")
    private void configureRegistryForCUSTOM(IConfigRegistry configRegistry, ILabelProvider type) {
        final String tag = CUSTOM;
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new TextIconConverter(type),
                DisplayMode.NORMAL, tag);
    }

    @objid ("8ed91482-6e57-4f8c-aa63-d952269aa287")
    public void configureLabelProvider(IConfigRegistry configRegistry) {
        // Register configurations for the columns
        configureRegistryForCUSTOM(configRegistry, new LabelProvider() {
            @Override
            public String getText(Object element) {
                return (String) ColumnHeaderConfiguration.this.dataModel.getValueAt(0, (Integer) element);
            }
        });
    }

}
