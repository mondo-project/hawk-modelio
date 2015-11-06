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
                                    

package org.modelio.diagram.symbol.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.IStyleChangeListener;
import org.modelio.diagram.styles.core.NamedStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.manager.StyleManager;
import org.modelio.diagram.styles.plugin.DiagramStyles;

/**
 * Control contribution the displays a style selection combo.
 */
@objid ("ac4fa34f-55b7-11e2-877f-002564c97630")
public class StyleSelector implements IStyleChangeListener, PropertyChangeListener {
    @objid ("ac4fa350-55b7-11e2-877f-002564c97630")
    protected static final String DIAGRAM_SETTINGS_STYLE = "diagram settings";

    /**
     * The combo identifier.
     */
    @objid ("ac4fa352-55b7-11e2-877f-002564c97630")
    public static final String ID = "org.modelio.diagram.symbol.StyleSelectorID";

    @objid ("ac4fa354-55b7-11e2-877f-002564c97630")
    private IGmObject gmObject;

    @objid ("ac4fa357-55b7-11e2-877f-002564c97630")
    private Combo combo;

    /**
     * Initialize the combo.
     */
    @objid ("ac4fa358-55b7-11e2-877f-002564c97630")
    public StyleSelector() {
    }

    /**
     * Apply the style selected in the combo to the given style.
     * Setting the style to null means:<ul>
     * <li> setting the cascaded style of the gmObject to the style of the owning diagram (unless the gmObject is the diagram itself)
     * <li> cleaning all local properties</ul>
     * @param style the style to apply.
     */
    @objid ("ac5129ba-55b7-11e2-877f-002564c97630")
    public void changeStyle(IStyle style) {
        StyleEditor se = new StyleEditor(this.gmObject);
        
        if (style != null) {
            se.setCascadedStyle(style);
        } else {
            // setting the style to null means:
            // - setting the cascaded style of the gmObject to the style of the owning diagram (unless the gmObject is the diagram itself)
            // - cleaning all local properties
            if (this.gmObject != this.gmObject.getDiagram()) {
                IStyle diagramStyle = this.gmObject.getDiagram().getStyle();
                se.setCascadedStyle(diagramStyle);
                se.reset();
            }
        
        }
        
        // new StyleEditor(selectedGraphic).reset();
        // final NamedStyle selectedStyle = (NamedStyle) ComboContributionItem.this.styleChooserCombo.getData(text);
        // new StyleEditor(selectedGraphic).setCascadedStyle(selectedStyle);
    }

    /**
     * Creates the SWT combo.
     * @param parent the parent composite
     * @param sel the current selection
     * @return the created SWT combo
     */
    @objid ("ac4fa35a-55b7-11e2-877f-002564c97630")
    @PostConstruct
    public Control createControl(Composite parent, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection sel) {
        this.combo = new Combo(parent, SWT.READ_ONLY);
        this.combo.addFocusListener(new StyleComboFocusListener(this,
                                                                this.combo));
        this.combo.addSelectionListener(new StyleComboSelectionAdapter(this,
                                                                       this.combo));
        populateCombo();
        
        if (sel != null)
            update(sel);
        return this.combo;
    }

    @objid ("c6125645-59aa-11e2-892a-001ec947ccaf")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_DELETE)) {
           // The Gm displayed in the Symbol view has been deleted, reset the selection.
           setSelection(null);
        }
    }

    @objid ("c6125649-59aa-11e2-892a-001ec947ccaf")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        if (this.gmObject == null)
            return;
        
        setSelection(this.gmObject);
    }

    @objid ("c612564e-59aa-11e2-892a-001ec947ccaf")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        if (this.gmObject == null)
            return;
        
        setSelection(this.gmObject);
    }

    /**
     * Called when the selection changes in the workbench.<br>
     * Its responsibility is to set the NotesView's current element.
     * @param sel the current modelio selection.
     */
    @objid ("ac5129bf-55b7-11e2-877f-002564c97630")
    @Inject
    @Optional
    void update(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection sel) {
        // we ignore the selection when one of the following conditions is matched:
        // - it is 'null'
        //TODO - we are pinned
        //TODO - the selection is our own selection
        //TODO - the selection is same selection
        // - the combo is not yet initialized
        if (sel == null || this.combo==null) 
            return;
        
        
        // currently the selection must be unique (one element) and it must be a GmObject
        final List<?> selectedElements = sel.toList();
        if (selectedElements.size() == 1 && selectedElements.get(0) instanceof IAdaptable) {
            final IAdaptable selectedAdapter = (IAdaptable) selectedElements.get(0);
            final IGmObject selGmObject = (IGmObject) selectedAdapter.getAdapter(IGmObject.class);
            setSelection(selGmObject);
        } else {
            setSelection(null);
        }
    }

    @objid ("ac5129c4-55b7-11e2-877f-002564c97630")
    protected void populateCombo() {
        // Each time the focus is gained, update the style list 
        // (as this list may change at anytime depending on the style manager activity). 
        // The style manager has currently no available fire/listening capacities.
        this.combo.removeAll();
        this.combo.add(DIAGRAM_SETTINGS_STYLE, 0);
        
        // add the styles  
        StyleManager sm = DiagramStyles.getStyleManager();
        for (String label : sm.getAvailableStyles()) {
            this.combo.add(label);
        }
    }

    /**
     * Update the combo from the given graphic model.
     * @param aGmObject a graphic model.
     */
    @objid ("c6125655-59aa-11e2-892a-001ec947ccaf")
    private void setSelection(IGmObject aGmObject) {
        if (aGmObject == null) {
            this.combo.removeAll();
            this.combo.setEnabled(false);
            return;
        } else {
            this.populateCombo();
        
            this.gmObject = aGmObject;
        
            // The gmObject style may be based either on its owning diagram style or on any other style
            IStyle diagramStyle = this.gmObject.getDiagram().getStyle();
            IStyle gmObjectBaseStyle = this.gmObject.getStyle().getCascadedStyle();
            if (gmObjectBaseStyle == diagramStyle) {
                this.combo.setText(DIAGRAM_SETTINGS_STYLE);
            } else if (gmObjectBaseStyle instanceof NamedStyle) {
                this.combo.setText(((NamedStyle) gmObjectBaseStyle).getName());
            } else
                this.combo.select(0);
        
            // Can't change style if the diagram is read only
            boolean isEditable = aGmObject.isEditable();
            this.combo.setEnabled(isEditable);
        
            this.combo.pack();
        }
    }

    @objid ("ac5129c6-55b7-11e2-877f-002564c97630")
    private static class StyleComboSelectionAdapter extends SelectionAdapter {
        @objid ("ac5129c7-55b7-11e2-877f-002564c97630")
        private StyleSelector styleSelector;

        @objid ("ac5129c8-55b7-11e2-877f-002564c97630")
        private Combo combo;

        @objid ("ac5129c9-55b7-11e2-877f-002564c97630")
        public StyleComboSelectionAdapter(StyleSelector styleSelector, Combo combo) {
            this.styleSelector = styleSelector;
            this.combo = combo;
        }

        @objid ("ac5129cd-55b7-11e2-877f-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            final String text = this.combo.getText();
            if (text.equals(DIAGRAM_SETTINGS_STYLE)) {
                this.styleSelector.changeStyle(null);
            } else {
                IStyle newStyle = DiagramStyles.getStyleManager()
                                               .getStyle(text);
                this.styleSelector.changeStyle(newStyle);
            }
        }

    }

    @objid ("ac5129d1-55b7-11e2-877f-002564c97630")
    private static class StyleComboFocusListener implements FocusListener {
        @objid ("ac5129d2-55b7-11e2-877f-002564c97630")
        private StyleSelector styleSelector;

        @objid ("ac5129d3-55b7-11e2-877f-002564c97630")
        private Combo combo;

        @objid ("ac5129d4-55b7-11e2-877f-002564c97630")
        public StyleComboFocusListener(StyleSelector styleSelector, Combo combo) {
            this.styleSelector = styleSelector;
            this.combo = combo;
        }

        @objid ("ac5129d8-55b7-11e2-877f-002564c97630")
        @Override
        public void focusGained(FocusEvent e) {
            this.styleSelector.populateCombo();
        }

        @objid ("ac5129dc-55b7-11e2-877f-002564c97630")
        @Override
        public void focusLost(FocusEvent e) {
            //NOOP
        }

    }

}
