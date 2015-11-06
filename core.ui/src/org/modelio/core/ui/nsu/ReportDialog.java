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
                                    

package org.modelio.core.ui.nsu;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.modelio.Modelio;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.impl.CoreSession;

@objid ("71a1b0f7-a5cc-4603-847b-d47e49cb8316")
public class ReportDialog extends ModelioDialog {
    @objid ("ded108c2-9773-48cc-ae05-d7eadfb85610")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Index.html";

    @objid ("86b9ebed-d84c-4c82-9f2c-ca33711e20e1")
    private TreeViewer tree;

    @objid ("44f5b98f-fb5f-4e62-ad8b-91d53eb2a2fc")
    private NamespaceUse input;

    @objid ("4455f5a5-8496-4de4-8489-af300b4cb153")
    private Group grp;

    @objid ("c4808896-8102-4e26-9a54-f396865dc320")
    private IModel iModel;

    @objid ("25aeeeed-603e-4b5c-b9b0-7ebeaa9caf16")
    private ModelChangeListener modelChangeListener;

    @objid ("aeb158bd-eaca-4d80-b4f9-8dca4d419fa9")
    private IModelioNavigationService navigationService;

    @objid ("e462cd21-0d86-4d78-8790-647ec082b7f4")
    public ReportDialog(final Shell parentShell) {
        this(parentShell, null);
    }

    @objid ("a28b5c5d-9b3e-4716-87b5-0252a7ad5053")
    @Override
    public void addButtonsInButtonBar(final Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @objid ("bd3c62f5-9b28-4606-87b6-2d275a974de1")
    @Override
    public Control createContentArea(final Composite composite) {
        Composite parent = new Composite(composite, SWT.NONE);
        parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        final FillLayout fillLayout = new FillLayout();
        fillLayout.marginHeight = 5;
        fillLayout.marginWidth = 1;
        parent.setLayout(fillLayout);
        
        this.grp = new Group(parent, 0);
        this.grp.setText("");
        this.grp.setFont(CoreFontRegistry.getModifiedFont(composite.getFont(), SWT.BOLD));
        this.grp.setLayout(new FormLayout());
        
        FormData data1 = new FormData();
        data1.top = new FormAttachment(0, 5);
        data1.left = new FormAttachment(0, 5);
        data1.right = new FormAttachment(100, -5);
        
        FormData data2 = new FormData();
        data2.top = new FormAttachment(0, 10);
        data2.left = new FormAttachment(0, 5);
        data2.right = new FormAttachment(100, -5);
        data2.bottom = new FormAttachment(100, -5);
        
        this.tree = new TreeViewer(this.grp, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        this.tree.setContentProvider(new TreeContentProvider());
        this.tree.setLabelProvider(new TreeLabelProvider());
        this.tree.getControl().setLayoutData(data2);
        
        updateViewFromModel();
        
        this.tree.addPostSelectionChangedListener(new ISelectionChangedListener() {
        
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Element theElement = getSelectedEl(event);
                if (theElement != null && theElement.isValid()) {
                    if (navigationService != null)
                        navigationService.fireNavigate(theElement);
                }
            }
        
            private Element getSelectedEl(SelectionChangedEvent event) {
                ISelection sel = event.getSelection();
                if (sel instanceof IStructuredSelection) {
                    Object object = ((IStructuredSelection) sel).getFirstElement();
                    if (object instanceof NamespaceUse)
                        return ((NamespaceUse) object).getUser();
                    if (object instanceof Element)
                        return (Element) object;
                }
                return null;
            }
        });
        return parent;
    }

    @objid ("8f98c531-f842-42fa-b6d6-b62408cfee1d")
    private void updateViewFromModel() {
        if (this.grp != null)
            this.grp.setText(" " + getDescription() + "  ");
        
        if (this.tree != null) {
            this.tree.setInput(this.input);
            this.tree.expandToLevel(2);
        }
    }

    @objid ("89410150-6770-44e9-905e-725b7f9682ed")
    public void setModel(final NamespaceUse use) {
        this.input = use;
        
        ICoreSession session = CoreSession.getSession(use);
        
        if (this.modelChangeListener != null)
            this.modelChangeListener.unregister();
        
        this.modelChangeListener = new ModelChangeListener(session, this);
        updateViewFromModel();
    }

    @objid ("89b4755a-56eb-432e-97ae-609a9940ab3d")
    public boolean isDisposed() {
        Shell s = getShell();
        return s == null || s.isDisposed();
    }

    @objid ("5340876a-28ef-4495-860c-974100cfd6c0")
    @Override
    public void init() {
        Shell shell = getShell();
        
        // Put the messages in the banner area
        setLogoImage(null);
        shell.setText(CoreUi.I18N.getMessage("causeanalyzer.shelltext")); //$NON-NLS-1$
        setTitle(CoreUi.I18N.getMessage("causeanalyzer.title")); //$NON-NLS-1$
        setMessage(CoreUi.I18N.getMessage("causeanalyzer.message")); //$NON-NLS-1$
    }

    @objid ("a245fb93-41a9-4923-b4a6-4da8c33dbd59")
    private String getDescription() {
        if (this.input != null) {
            return CoreUi.I18N.getMessage("causeanalyzer.main_label", this.input.getUser().getName(), this.input.getUsed()
                    .getName()); //$NON-NLS-1$
        }
        return "";
    }

    @objid ("1eaeb994-3450-4930-8a48-9a93d7121738")
    @Override
    public boolean close() {
        if (this.modelChangeListener != null) {
            this.modelChangeListener.unregister();
        }
        return super.close();
    }

    @objid ("2f51d739-1c8a-46bb-971a-74fc05e88ba9")
    public ReportDialog(final Shell parentShell, IModelioNavigationService navigationService) {
        super(parentShell);
        this.navigationService = navigationService;
    }

    @objid ("267a77be-4ff2-4a53-a86e-f803b03c1d76")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("efb29fad-520c-4d77-8446-b7464d125512")
    private static class ModelChangeListener implements IModelChangeListener {
        @objid ("e740644b-b119-477c-a7cb-df45bdb3c7cc")
        private ReportDialog dlg;

        @objid ("c3698c3a-a580-4b30-954b-c9c1bbde5111")
        private ICoreSession session;

        @objid ("7ac995da-b879-464d-a513-bce13a2e8005")
        public ModelChangeListener(ICoreSession session, ReportDialog dlg) {
            this.session = session;
            this.dlg = dlg;
            session.getModelChangeSupport().addModelChangeListener(this);
        }

        @objid ("affe0e78-7ccc-4e10-ba76-19f319a48c27")
        @Override
        public void modelChanged(IModelChangeEvent event) {
            if (!this.dlg.isDisposed()) {
                this.dlg.updateViewFromModel();
            }
        }

        @objid ("8a086654-48a0-407f-aa5e-849b629b05e2")
        public void unregister() {
            this.session.getModelChangeSupport().removeModelChangeListener(this);
            this.session = null;
        }

    }

}
