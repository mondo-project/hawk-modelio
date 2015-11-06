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
                                    

package org.modelio.property.ui.data.stereotype.body;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultDoubleDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultIntegerDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.convert.IDisplayConverter;
import org.eclipse.nebula.widgets.nattable.data.validate.DataValidator;
import org.eclipse.nebula.widgets.nattable.data.validate.ValidationFailedException;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.action.CellEditDragMode;
import org.eclipse.nebula.widgets.nattable.edit.action.KeyEditAction;
import org.eclipse.nebula.widgets.nattable.edit.action.MouseEditAction;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.TextCellEditor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ComboBoxPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.VerticalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.BodyCellEditorMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.CellPainterMouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.KeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.LetterOrDigitKeyEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.nattable.editors.DateDisplayConverter;
import org.modelio.core.ui.nattable.editors.DateEditor;
import org.modelio.core.ui.nattable.editors.ElementEditor;
import org.modelio.core.ui.nattable.editors.ImagePainter;
import org.modelio.core.ui.nattable.editors.ListDisplayConverter;
import org.modelio.core.ui.nattable.editors.ListEditor;
import org.modelio.core.ui.nattable.editors.TextIconConverter;
import org.modelio.core.ui.nattable.editors.TextIconPainter;
import org.modelio.core.ui.nattable.editors.TimeDisplayConverter;
import org.modelio.core.ui.nattable.editors.TimeEditor;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.stereotype.DefaultLabelProvider;
import org.modelio.property.ui.data.stereotype.data.TableDataModel;
import org.modelio.property.ui.data.stereotype.model.IPropertyModel2;
import org.modelio.ui.UIColor;

@objid ("cf2fe66e-3469-45ac-a8a2-b757b9b752a3")
public class BodyConfiguration extends AbstractRegistryConfiguration {
    @objid ("583060d8-d594-495c-846e-8792a6102722")
    protected static final String EDITABLE = "Cell.editable";

    @objid ("833f91bb-1141-4cea-b153-3b3d86e1c501")
    protected IConfigRegistry registry;

    @objid ("339db2de-2759-4ae3-b772-a88a352875d0")
    protected final IModelioPickingService pickingService;

    @objid ("6e7c2d06-fb7f-42ac-b3b2-cc222d457913")
    protected IProjectService projectService;

    @objid ("e401b782-3269-4a53-b636-8f2d6464bc41")
    protected TableDataModel dataModel;

    @objid ("d39fb4fc-8f25-45f5-8984-37229c2146c5")
    protected final DataLayer bodyDataLayer;

    @objid ("26f0843e-ee64-4b71-8c86-e5e9a0d46718")
    public BodyConfiguration(final DataLayer bodyDataLayer, TableDataModel dataModel, IProjectService projectService, IModelioPickingService pickingService) {
        this.bodyDataLayer = bodyDataLayer;
        this.projectService = projectService;
        this.pickingService = pickingService;
        this.dataModel = dataModel;
    }

    @objid ("4e1b75ac-a422-436c-98c9-fea78f39c6a7")
    @Override
    public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
        // F2 key enters edition
        uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.NONE, SWT.F2), new KeyEditAction());
        
        // SPACE key enters edition: this is especially useful for changing the
        // value for a checkbox
        uiBindingRegistry.registerKeyBinding(new KeyEventMatcher(SWT.NONE, SWT.SPACE), new KeyEditAction());
        
        // any letter or digit key enters edition (upper or lower case)
        uiBindingRegistry.registerKeyBinding(new LetterOrDigitKeyEventMatcher(), new KeyEditAction());
        uiBindingRegistry.registerKeyBinding(new LetterOrDigitKeyEventMatcher(SWT.SHIFT), new KeyEditAction());
        
        // Single click in a TextCellEditor editor enters edition
        uiBindingRegistry.registerSingleClickBinding(new BodyCellEditorMouseEventMatcher(TextCellEditor.class),
                new MouseEditAction());
        
        // Single click in a DateEditor editor enters edition
        uiBindingRegistry.registerSingleClickBinding(new BodyCellEditorMouseEventMatcher(DateEditor.class), new MouseEditAction());
        
        // Single click in a ElementEditor editor enters edition
        uiBindingRegistry.registerSingleClickBinding(new BodyCellEditorMouseEventMatcher(ElementEditor.class),
                new MouseEditAction());
        
        uiBindingRegistry.registerFirstSingleClickBinding(new CellPainterMouseEventMatcher(GridRegion.BODY,
                MouseEventMatcher.LEFT_BUTTON, CheckBoxPainter.class), new MouseEditAction());
        
        uiBindingRegistry.registerFirstMouseDragMode(new CellPainterMouseEventMatcher(GridRegion.BODY,
                MouseEventMatcher.LEFT_BUTTON, CheckBoxPainter.class), new CellEditDragMode());
    }

    @objid ("0beeb779-a802-475c-975c-d39aa6a5a319")
    @Override
    public void configureRegistry(IConfigRegistry configRegistry) {
        this.registry = configRegistry;
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE,
                DisplayMode.EDIT, EDITABLE);
        
        // Declare all standard types
        configureRegistryForSTRING(configRegistry);
        configureRegistryForBOOLEAN(configRegistry);
        configureRegistryForINTEGER(configRegistry);
        configureRegistryForUNSIGNED(configRegistry);
        configureRegistryForFLOAT(configRegistry);
        configureRegistryForIMAGE(configRegistry);
        
        configureRegistryForLIST(configRegistry);
        configureRegistryForDATE(configRegistry);
        configureRegistryForTIME(configRegistry);
        configureRegistryForELEMENT(configRegistry);
    }

    @objid ("b254ac7b-9625-4ebe-ae2c-ae10c7950c31")
    @Override
    public void configureLayer(ILayer layer) {
        final IPropertyModel2 editor = this.dataModel.getPropertyModel();
        
        // NOTE: Register the accumulator on the body data layer.
        // This ensures that the labels are bound to the column index and are unaffected by column order.
        final IConfigLabelAccumulator cellLabelAccumulator = new ColumnOverrideLabelAccumulator(this.bodyDataLayer) {
        
            @Override
            public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
                super.accumulateConfigLabels(configLabels, columnPosition, rowPosition);
        
                final Integer objectAtRow = BodyConfiguration.this.dataModel.getObjectAtRow(rowPosition);
                final Integer objectAtColumn = BodyConfiguration.this.dataModel.getObjectAtCol(columnPosition);
        
                final Class<?> type = editor.getValueTypeAt(1+objectAtRow, objectAtColumn);
                final List<String> possibleValues = editor.getPossibleValues(objectAtColumn, objectAtRow);
                if (possibleValues != null) { // Enum-like custom type
                    // standard type for rendering
                    String tag = getTypeTag(type);
                    configLabels.addLabel(tag);
        
                    // custom tag for edition
                    tag = getTypeTag(CustomEnum.class);
                    configLabels.addLabel(tag + ".edit");
        
                    // declare the custom type in the registry, just in case
                    configureRegistryForPOSSIBLEVALUES(BodyConfiguration.this.registry, possibleValues);
                } else if (type.isEnum()) { // Enum type
                    // enumeration specific configuration
                    String tag = getTypeTag(Enum.class, type.getName());
                    configLabels.addLabel(tag);
        
                    // custom tag for edition
                    configLabels.addLabel(tag + ".edit");
        
                    // declare the enum type in the registry, just in case
                    configureRegistryForENUMERATE(BodyConfiguration.this.registry, type);
                } else { // Standard type
                    String tag = getTypeTag(type);
                    configLabels.addLabel(tag);
        
                    // custom tag for edition
                    configLabels.addLabel(tag + ".edit");
                }
        
                if (editor.isEditable(objectAtRow, objectAtColumn)) {
                    configLabels.addLabel(EDITABLE);
                }
        
                // Define a specific style for this cell
                IPropertyStyle newStyle = new DefaultPropertyStyle();
                editor.updateStyle(objectAtColumn, objectAtRow, newStyle);
        
                // Background color
                final Color backgroundColor = newStyle.getBackgroundColor();
                if (backgroundColor != GUIHelper.COLOR_WHITE) {
                    final String label = "BACKGROUND" + backgroundColor.toString();
                    configLabels.addLabel(label);
        
                    // Define a background style with the appropriate color
                    Style style = new Style();
                    style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, backgroundColor);
        
                    BodyConfiguration.this.registry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.NORMAL, label);
                } else if (rowPosition %2 == 0) {
                    final String label = "BACKGROUND" + backgroundColor.toString();
                    configLabels.addLabel(label);
        
                    // Define a background style with the appropriate color
                    Style style = new Style();
                    style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, UIColor.TABLE_EVENROW_BG);
        
                    BodyConfiguration.this.registry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.NORMAL, label);
                }
        
                // Foreground color
                final Color foregroundColor = newStyle.getForegroundColor();
                if (foregroundColor != GUIHelper.COLOR_BLACK) {
                    final String label = "FOREGROUND" + foregroundColor.toString();
                    configLabels.addLabel(label);
        
                    // Define a foreground style with the appropriate color
                    Style style = new Style();
                    style.setAttributeValue(CellStyleAttributes.FOREGROUND_COLOR, foregroundColor);
        
                    BodyConfiguration.this.registry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, style, DisplayMode.NORMAL, label);
                }
            }
        
        };
        this.bodyDataLayer.setConfigLabelAccumulator(cellLabelAccumulator);
    }

    @objid ("1d2884c6-6c6b-4d5f-bd3e-c3593ca49ad7")
    protected void configureRegistryForENUMERATE(IConfigRegistry configRegistry, Class<?> enumeratedPropertyType) {
        final String tag = getTypeTag(Enum.class, enumeratedPropertyType.getName());
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new ComboBoxPainter(), DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new EnumerationLiteralDisplayConverter(
                enumeratedPropertyType), DisplayMode.NORMAL, tag);
        
        // Editor
        final ComboBoxCellEditor comboEditor = new ComboBoxCellEditor(Arrays.asList(enumeratedPropertyType.getEnumConstants()));
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboEditor, DisplayMode.NORMAL, tag + ".edit");
        
        // Validator
    }

    @objid ("681ede88-b615-4a57-8f60-dc2ab3ad2ba2")
    protected String getTypeTag(Class<?> baseType) {
        return getTypeTag(baseType, "");
    }

    @objid ("b551fc93-d646-4717-8a88-a72f5602e0b4")
    protected String getTypeTag(Class<?> baseType, String suffix) {
        return baseType.getName() + ".tag." + suffix;
    }

    @objid ("27755356-a85e-417a-9f01-80dd9908934a")
    private void configureRegistryForSTRING(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(String.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        
        // Editor
        final TextCellEditor textCellEditor = new TextCellEditor();
        textCellEditor.setDecorationPositionOverride(SWT.LEFT | SWT.TOP);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, textCellEditor, DisplayMode.NORMAL, tag + ".edit");
        
        // Validator
    }

    @objid ("bb8e4871-b5c8-4681-bc84-6116769b584f")
    private void configureRegistryForBOOLEAN(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Boolean.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new CheckBoxPainter(), DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultBooleanDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        final CheckBoxCellEditor checkboxEditor = new CheckBoxCellEditor();
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, checkboxEditor, DisplayMode.NORMAL, tag + ".edit");
        
        // Validator
    }

    @objid ("a77886f0-f3c4-48d0-9f7e-afd9cd289aab")
    private void configureRegistryForINTEGER(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Integer.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.RIGHT);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultIntegerDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        
        // Validator
        final DataValidator validator = new DataValidator() {
            @Override
            public boolean validate(int columnIndex, int rowIndex, Object newValue) {
                try {
                    Integer.decode(newValue.toString());
                    return true;
                } catch (final NumberFormatException e) {
                    throw new ValidationFailedException("Value must be an Integer");
                }
            }
        };
        configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR, validator, DisplayMode.EDIT, tag);
    }

    @objid ("c3450b30-456d-4356-ac27-24cd8b73a25c")
    private void configureRegistryForUNSIGNED(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Short.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.RIGHT);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultIntegerDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        
        // Validator
        final DataValidator validator = new DataValidator() {
            @Override
            public boolean validate(int columnIndex, int rowIndex, Object newValue) {
                try {
                    final Integer i = Integer.decode(newValue.toString());
                    if (i < 0) {
                        throw new ValidationFailedException("Value must be an positive");
                    }
                    return true;
                } catch (final NumberFormatException e) {
                    throw new ValidationFailedException("Value must be an Integer");
                }
            }
        };
        configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR, validator, DisplayMode.EDIT, tag);
    }

    @objid ("d0ecf8a9-bd1e-41d8-8bfe-c29ca4ce9032")
    private void configureRegistryForFLOAT(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Float.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.RIGHT);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DefaultDoubleDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        
        // Validator
        final DataValidator validator = new DataValidator() {
            @Override
            public boolean validate(int columnIndex, int rowIndex, Object newValue) {
                try {
                    Float.parseFloat(newValue.toString());
                    return true;
                } catch (final NumberFormatException e) {
                    throw new ValidationFailedException("Value must be an float");
                }
            }
        };
        configRegistry.registerConfigAttribute(EditConfigAttributes.DATA_VALIDATOR, validator, DisplayMode.EDIT, tag);
    }

    @objid ("49d7bea4-cde9-420e-8d1a-be9ecb4d14c7")
    private void configureRegistryForDATE(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Date.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new DateDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        final DateEditor editor = new DateEditor();
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, editor, DisplayMode.NORMAL, tag + ".edit");
        
        // Validator
    }

    @objid ("89355621-cefd-4147-9922-afb23708ca2e")
    private void configureRegistryForTIME(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Time.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new TimeDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        final TimeEditor editor = new TimeEditor();
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, editor, DisplayMode.EDIT, tag + ".edit");
        
        // Validator
    }

    @objid ("ed0f1693-de8b-4ec0-9aeb-85c35a834006")
    private void configureRegistryForELEMENT(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Element.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new TextIconPainter(true), DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new TextIconConverter(new DefaultLabelProvider(Element.class)), DisplayMode.NORMAL, tag);
        
        // Editor
        final ElementEditor editor = new ElementEditor(this.projectService.getSession(), this.pickingService);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, editor, DisplayMode.EDIT, tag + ".edit");
        
        // Validator
    }

    @objid ("60bb10a5-c429-42c2-b922-9033bb91e797")
    private void configureRegistryForIMAGE(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(Image.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_PAINTER, new ImagePainter(), DisplayMode.NORMAL, tag);
        
        // Display converter
        
        // Editor
        
        // Validator
    }

    @objid ("ba6d9853-42fc-40b2-bf83-e293418e10e5")
    protected void configureRegistryForPOSSIBLEVALUES(IConfigRegistry configRegistry, List<String> possibleValues) {
        final String tag = getTypeTag(CustomEnum.class);
        
        // Editor
        final ComboBoxCellEditor comboEditor = new ComboBoxCellEditor(possibleValues);
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, comboEditor, DisplayMode.NORMAL, tag + ".edit");
    }

    @objid ("35f8316e-59b6-4826-a334-684dfee90861")
    private void configureRegistryForLIST(IConfigRegistry configRegistry) {
        final String tag = getTypeTag(List.class);
        // Style and painter
        final Style cellStyle = new Style();
        cellStyle.setAttributeValue(CellStyleAttributes.HORIZONTAL_ALIGNMENT, HorizontalAlignmentEnum.CENTER);
        cellStyle.setAttributeValue(CellStyleAttributes.VERTICAL_ALIGNMENT, VerticalAlignmentEnum.TOP);
        configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle, DisplayMode.NORMAL, tag);
        
        // Display converter
        configRegistry.registerConfigAttribute(CellConfigAttributes.DISPLAY_CONVERTER, new ListDisplayConverter(),
                DisplayMode.NORMAL, tag);
        
        // Editor
        final ListEditor editor = new ListEditor();
        configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITOR, editor, DisplayMode.NORMAL, tag + ".edit");
        
        // Validator
    }

    @objid ("e619abff-a623-45e7-b084-2793e44d41a5")
    static class EnumerationLiteralDisplayConverter implements IDisplayConverter {
        @objid ("dd33707b-aa27-40a8-a3b0-92a11f12b55d")
        private final Class<?> enumType;

        @objid ("1b73393b-8727-4f31-b3e4-d00018faafe1")
        public EnumerationLiteralDisplayConverter(Class<?> enumType) {
            this.enumType = enumType;
        }

        @objid ("979b6993-58d7-44d6-86c4-9d460e6e8ce4")
        @Override
        public Object canonicalToDisplayValue(Object obj) {
            if (obj == null) {
                return "undefined";
            }
            
            final Enum<?> l = (Enum<?>) obj;
            return l.name();
        }

        @objid ("df759aae-2377-42d3-a1eb-19154f28e973")
        @Override
        public Object canonicalToDisplayValue(ILayerCell arg0, IConfigRegistry arg1, Object obj) {
            return canonicalToDisplayValue(obj);
        }

        @objid ("ecdc4e4f-0efc-4adf-92d0-66e536e4cad1")
        @Override
        public Object displayToCanonicalValue(Object displayedValue) {
            for (final Object l : this.enumType.getEnumConstants()) {
                if (l.toString().equals(displayedValue)) {
                    return l;
                }
            }
            return this.enumType.getEnumConstants()[0];
        }

        @objid ("93c2218a-a49d-4571-830a-8df56d172ac0")
        @Override
        public Object displayToCanonicalValue(ILayerCell arg0, IConfigRegistry arg1, Object displayedValue) {
            return displayToCanonicalValue(displayedValue);
        }

    }

    @objid ("5df26ef8-fad0-4c01-88ca-3ddc4ce45473")
    public enum CustomEnum {

// Empty enum, to indicate a dynamic type
    }

}
