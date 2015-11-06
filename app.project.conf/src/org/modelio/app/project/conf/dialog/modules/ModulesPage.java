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
                                    

package org.modelio.app.project.conf.dialog.modules;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IParameterModel;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.project.conf.dialog.ProjectModel;
import org.modelio.app.project.conf.dialog.modules.list.ModulesSection;
import org.modelio.app.project.conf.dialog.modules.parameters.ParameterSection;
import org.modelio.gproject.module.GModule;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.ui.UIColor;

@objid ("a73dfbfb-33f6-11e2-a514-002564c97630")
public class ModulesPage {
    @objid ("a73dfbfd-33f6-11e2-a514-002564c97630")
    protected ModulesSection modulesSection;

    @objid ("a73dfbfe-33f6-11e2-a514-002564c97630")
    protected ParameterSection parameterSection;

    @objid ("5ce78b08-2d71-4937-8c5e-6f8f5efefbea")
    protected Label descriptionLabel;

    @objid ("420779fc-4a9f-477e-b584-ca069eb5fa27")
    protected IEclipseContext applicationContext;

    @objid ("0d6c89e4-d2fc-4a2e-bdc7-294d37969c1f")
     ProjectModel projectAdapter;

    @objid ("a73dfbff-33f6-11e2-a514-002564c97630")
    public Composite createControls(FormToolkit toolkit, MApplication application, final Composite parent, ModelioEnv env) {
        this.applicationContext = application.getContext();
        Composite mainComposite = toolkit.createComposite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout());
        
        // The form
        ScrolledForm form = toolkit.createScrolledForm(mainComposite);
        form.getBody().setLayout(new TableWrapLayout());
        form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // Modules Section
        this.modulesSection = new ModulesSection(this.applicationContext, env);
        Section s1 = this.modulesSection.createControls(toolkit, form.getBody());
        s1.setLayoutData(new TableWrapData(TableWrapData.FILL));
        
        // Parameters Section
        this.parameterSection = new ParameterSection(this.applicationContext);
        final Section s3 = this.parameterSection.createControls(toolkit, form.getBody());
        s3.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
             
        // Parameter updater
        this.modulesSection.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    if (structuredSelection.size() == 0) {
                        ModulesPage.this.parameterSection.setInput(null);                       
                        // force layout of the new zone
                        boolean expanded = s3.isExpanded();
                        s3.setExpanded(!expanded);
                        s3.setExpanded(expanded);
                    } else if (structuredSelection.size() == 1) {
                        Object obj = structuredSelection.getFirstElement();                
                        if (obj instanceof GModule) {
                            GModule module = (GModule) obj;
                            ModulesPage.this.parameterSection.setInput(module);                            
                            // force layout of the new zone
                            boolean expanded = s3.isExpanded();
                            s3.setExpanded(!expanded);
                            s3.setExpanded(expanded);
                        }
                    }
                }
            }
        });
        
        this.descriptionLabel = toolkit.createLabel(mainComposite, "", SWT.WRAP | SWT.V_SCROLL);
        this.descriptionLabel.setForeground(UIColor.LABEL_TIP_FG);
        GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd2.heightHint = 60;
        this.descriptionLabel.setLayoutData(gd2);
        
        // Add description updater for each section
        addModuleSectionDescriptionUpdater();
        addParameterDescriptionSectionUpdater();
        return mainComposite;
    }

    @objid ("a73f82a3-33f6-11e2-a514-002564c97630")
    public void setInput(ProjectModel projectAdapter) {
        this.projectAdapter = projectAdapter;
        // update the different sections
        this.modulesSection.setInput(projectAdapter);
    }

    /**
     * Add module section updater
     * When selection change in the section, the description label text will be updated
     */
    @objid ("e235ec46-7cda-4e89-ba56-30f26d1ec351")
    private void addModuleSectionDescriptionUpdater() {
        this.modulesSection.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Reset description
                ModulesPage.this.descriptionLabel.setText(""); //$NON-NLS-1$
                
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    if (structuredSelection.size() == 1) {
                        Object obj = structuredSelection.getFirstElement();
                
                        if (obj instanceof GModule) {
                            IModuleService moduleService = ModulesPage.this.applicationContext.get(IModuleService.class);
                            
                            // Fill the module's description
                            GModule module = (GModule) obj;
                            
                            IModule iModule = moduleService.getIModule(module.getModuleElement());
                            if (iModule != null) { 
                                ModulesPage.this.descriptionLabel.setText(iModule.getDescription());
                            }
                        }
                    }
                }
            }
            
        });
    }

    /**
     * Add parameter section updater
     * When selection change in the section, the description label text will be updated
     */
    @objid ("abaf6b83-2a46-4049-adcb-ff38a18a35ed")
    private void addParameterDescriptionSectionUpdater() {
        this.parameterSection.addSelectionChangedListener(new ISelectionChangedListener() {
        
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Reset description
                ModulesPage.this.descriptionLabel.setText(""); //$NON-NLS-1$
                
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    if (structuredSelection.size() == 1) {
                        Object obj = structuredSelection.getFirstElement();
                
                        if (obj instanceof IParameterModel) {
                            // Fill the module's description
                            IParameterModel param = (IParameterModel) obj;
                            
                            ModulesPage.this.descriptionLabel.setText(param.getDescription());
                        }
                    }
                }
                
            }
            
        });
    }

    @objid ("367fb76d-b012-470c-9734-982b47947a4e")
    ProjectModel getProjectAdapter() {
        return this.projectAdapter;
    }

}
