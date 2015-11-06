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
                                    

package org.modelio.app.project.core.services;

import java.beans.EventHandler;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.modelio.app.project.core.plugin.AppProjectCore;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.fragment.FragmentMigrationNeededException;

/**
 * Dialog box that asks for migration of a model fragment.
 * @author cmarin
 */
@objid ("6f434a7c-2449-4e33-802b-e14b8e646f26")
public class ConfirmMigrationDialog extends ModelioDialog {
    @objid ("324740b4-c079-4e5d-b26c-980c7401950f")
    private FragmentMigrationNeededException toMigrate;

    /**
     * @param parentShell a parent SWT shell
     * @param toMigrate the migration informations
     */
    @objid ("2da6a151-9a2f-4645-b3e2-747e533a77e2")
    public ConfirmMigrationDialog(Shell parentShell, FragmentMigrationNeededException toMigrate) {
        super(parentShell);
        assert (toMigrate != null);
        this.toMigrate = toMigrate;
    }

    @objid ("45f4f366-ee8e-40f8-a605-838a1345f0b4")
    @Override
    public void init() {
        getShell().setText(AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.title"));
        //setTitle(AppUi.I18N.getMessage("UntrustedServerDialog.title", this.uri.getHost()));
        setTitle(AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.header",
                this.toMigrate.getFragmentId(),
                this.toMigrate.getSummary()));
    }

    @objid ("922537cd-c58d-4649-84c7-00e8838ded10")
    @Override
    public Control createContentArea(Composite parent) {
        final FormToolkit tk = new FormToolkit(parent.getDisplay());
        parent.addDisposeListener( EventHandler.create(DisposeListener.class, tk, "dispose")); 
        
        ScrolledForm scrolledForm = tk.createScrolledForm(parent);
        GridDataFactory.defaultsFor(scrolledForm).align(SWT.FILL, SWT.FILL).grab(true, true).hint(600, 500).applyTo(scrolledForm);
        
        final Composite body = scrolledForm.getBody();
        TableWrapLayout bodyLayout = new TableWrapLayout();
        bodyLayout.verticalSpacing = 10;
        body.setLayout(bodyLayout);
        body.setBackground(tk.getColors().getBackground());
        
        {
            // | ExpandableComposite.TWISTIE
            Section headerArea = tk.createSection(body, ExpandableComposite.TITLE_BAR |  ExpandableComposite.EXPANDED);
            headerArea.setLayout(new TableWrapLayout());
            headerArea.setText(AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.summaryarea.title"));
            headerArea.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP));
        
            String headerText = AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.summaryarea.msg", 
                    this.toMigrate.getFragmentId(),
                    this.toMigrate.getSummary());
            
            Text label = tk.createText(headerArea, headerText, SWT.MULTI | SWT.WRAP);
            label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB));
            label.setEditable(false);
            
            headerArea.setClient(label);
            
        }
        
        if (this.toMigrate.getDetails() != null && ! this.toMigrate.getDetails().trim().isEmpty()) {
            Section area = tk.createSection(body, ExpandableComposite.TITLE_BAR |  ExpandableComposite.EXPANDED);
            // ExpandableComposite.TWISTIE
            area.setText(AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.detailarea.title", 
                    this.toMigrate.getFragmentId()));
            area.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB));
        
            Composite areaBody = tk.createComposite(area, SWT.WRAP);
            areaBody.setLayout(new TableWrapLayout());
            
            /*String headerText = AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.detailarea.msg", 
                    this.toMigrate.getFragmentId());
            tk.createLabel(areaBody, headerText, SWT.WRAP);*/
            
            FormText formText = tk.createFormText(areaBody, false);
            formText.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB));
            try {
                formText.setText(this.toMigrate.getDetails(), true, true);
            } catch (IllegalArgumentException e) {
                formText.dispose();
                //Label label = tk.createLabel(areaBody, this.toMigrate.getDetails(), SWT.WRAP);
                //label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB));
                
                Text label = tk.createText(areaBody, this.toMigrate.getDetails(), SWT.MULTI | SWT.WRAP);
                label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB));
                label.setEditable(false);
            }
            
            area.setClient(areaBody);
        }
        
        {
            Section area = tk.createSection(body, ExpandableComposite.TITLE_BAR |  ExpandableComposite.EXPANDED);
            area.setText(AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.conclusion.title", 
                    this.toMigrate.getFragmentId()));
            area.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.BOTTOM));
        
            String conclusion = AppProjectCore.I18N.getMessage("ConfirmMigrationDialog.conclusion.msg", 
                    this.toMigrate.getFragmentId());
            Text label = tk.createText(area, conclusion, SWT.MULTI | SWT.WRAP);
            label.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.BOTTOM));
            label.setEditable(false);
            
            area.setClient(label);
        }
        return scrolledForm;
    }

    @objid ("4fe0a1a3-c0df-43e1-8afe-8a1e234a0d56")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID,  AppProjectCore.I18N.getMessage(
                "ConfirmMigrationDialog.button.yes", this.toMigrate.getFragmentId()), false);
        createButton(parent, IDialogConstants.CANCEL_ID, AppProjectCore.I18N.getMessage(
                "ConfirmMigrationDialog.button.no", this.toMigrate.getFragmentId()), true);
    }

}
