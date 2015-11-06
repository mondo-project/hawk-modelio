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
                                    

package org.modelio.audit.checker;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.audit.checker.actions.AuditSeverityAction;
import org.modelio.audit.checker.actions.DisableRuleHandlerAction;
import org.modelio.audit.checker.actions.SelectInExplorerAction;
import org.modelio.audit.checker.actions.ShowDetailsAction;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.service.IAuditService;
import org.modelio.audit.view.AuditPanelProvider;
import org.modelio.audit.view.providers.AuditProviderFactory.AuditViewMode;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("96eb884f-f002-4509-9546-ecebafdc1802")
public class CheckerView extends ModelioDialog {
    @objid ("b2078137-7704-40d3-8131-9c7236958f1a")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_interface_audit_view.html";

    @objid ("41fda318-77e3-4cbb-8aec-2682b8e60b6c")
     volatile boolean refreshPending = false;

    @objid ("ee65f8c0-a167-4569-928b-c5bf50fa2db5")
    private final AuditPanelProvider auditPanel;

    @objid ("8caa1334-044b-4bca-82f3-57d3fea29201")
    private CheckElementRunner checker;

    @objid ("bba42d4a-5086-4baf-9209-4f16f25560c8")
    private IAuditService auditService;

    @objid ("04a2cb18-1b8f-4991-9f66-3f6a735b148b")
    private List<MObject> checkedElements;

    @objid ("ca6923a2-472b-4fbb-978c-eeab71eabd47")
    private IProjectService projectService;

    @objid ("77163aef-6967-4458-bd23-67f917a28b48")
    private IModelioNavigationService navigationService;

    @objid ("ff1e9730-1830-4297-a794-d4ec80856790")
    private Thread checkerThread;

    @objid ("5dbd4b31-af13-47bb-b29d-73a2b93901b1")
     Label statusLabel;

    @objid ("3fe456c5-2511-454a-a2c5-4b7d23f1c1c4")
    private Button byTypeButton;

    @objid ("1d8b45e5-5404-4529-bac0-e9dc87b2fe65")
    private Button byRuleButton;

    @objid ("6ab7dbf9-7422-4440-b5d0-dad556b07625")
    private Button byElementButton;

    @objid ("723a2e63-b808-433b-b0c8-f1d20f627318")
    private Button byListButton;

    @objid ("02060f87-ee74-494d-8c22-b6a55f4f1f30")
    protected CheckerView(Shell parentShell, ICoreSession session, Object selection, IMModelServices modelService, IModelioNavigationService navigationService, MApplication application, EModelService emService, IAuditService auditService, IProjectService projectService, String jodId) {
        super(parentShell);
        
        this.checkedElements = getSelectedElements(selection);
        
        this.checker = new CheckElementRunner(auditService, selection, jodId);
        this.auditPanel = new AuditPanelProvider(session, modelService, navigationService, application, emService, jodId);
        this.auditService = auditService;
        this.projectService = projectService;
        this.navigationService = navigationService;
        setBlockOnOpen(false);
    }

    @objid ("b484dcf7-7e02-40ee-be38-9b221a65a6aa")
    @Override
    public Control createContentArea(Composite parent) {
        parent.setLayout(new GridLayout(1, false));
        
        Composite root = new Composite(parent, SWT.NONE);
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        root.setLayoutData(gd);
        root.setLayout(new GridLayout(6, false));
        
        // --------------------------------
        Composite comp = new Composite(root, SWT.BORDER);
        comp.setLayout(new RowLayout());
        
        Label elementLabel = new Label(comp, SWT.NONE);
        elementLabel.setText("Checked element(s): ");
        // gd = new GridData();
        // elementLabel.setLayoutData(gd);
        
        int card = this.checkedElements.size();
        Label elementIcon;
        Label elementText;
        switch (card) {
        
        case 0:
            break;
        case 1:
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(0).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(0).getName());
            break;
        case 2:
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(0).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(0).getName() + ", ");
        
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(1).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(1).getName());
            break;
        case 3:
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(0).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(0).getName() + ", ");
        
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(1).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(1).getName() + ", ");
        
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(2).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(2).getName());
            break;
        default:
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(0).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(0).getName() + ", ");
        
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(1).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(1).getName() + ", ");
        
            elementIcon = new Label(comp, SWT.NONE);
            elementIcon.setImage(MetamodelImageService.getIcon(this.checkedElements.get(2).getMClass()));
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(this.checkedElements.get(2).getName());
        
            elementText = new Label(comp, SWT.NONE);
            elementText.setText(", ... (" + (this.checkedElements.size()) + " elements)");
            break;
        }
        
        gd = new GridData();
        gd.horizontalSpan = 6;
        gd.horizontalAlignment = SWT.FILL;
        comp.setLayoutData(gd);
        
        // --------------------------------
        this.statusLabel = new Label(root, SWT.CENTER);
        gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalSpan = 2;
        this.statusLabel.setLayoutData(gd);
        
        this.byTypeButton = new Button(root, SWT.TOGGLE);
        this.byTypeButton.setImage(Audit.getImageDescriptor("icons/LayoutByType.png").createImage());
        this.byTypeButton.addMouseListener(new MouseListener() {
        
            @Override
            public void mouseUp(MouseEvent e) {
                CheckerView.this.auditPanel.setAuditViewMode(AuditViewMode.BYTYPE);
                CheckerView.this.byRuleButton.setSelection(false);
                CheckerView.this.byElementButton.setSelection(false);
                CheckerView.this.byListButton.setSelection(false);
            }
        
            @Override
            public void mouseDown(MouseEvent e) {
            }
        
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });
        this.byTypeButton.setSelection(true);
        
        this.byRuleButton = new Button(root, SWT.TOGGLE);
        this.byRuleButton.setImage(Audit.getImageDescriptor("icons/LayoutByRule.png").createImage());
        this.byRuleButton.addMouseListener(new MouseListener() {
        
            @Override
            public void mouseUp(MouseEvent e) {
                CheckerView.this.auditPanel.setAuditViewMode(AuditViewMode.BYRULE);
                CheckerView.this.byTypeButton.setSelection(false);
                CheckerView.this.byElementButton.setSelection(false);
                CheckerView.this.byListButton.setSelection(false);
            }
        
            @Override
            public void mouseDown(MouseEvent e) {
            }
        
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });
        
        this.byElementButton = new Button(root, SWT.TOGGLE);
        this.byElementButton.setImage(Audit.getImageDescriptor("icons/LayoutByElement.png").createImage());
        this.byElementButton.addMouseListener(new MouseListener() {
        
            @Override
            public void mouseUp(MouseEvent e) {
                CheckerView.this.auditPanel.setAuditViewMode(AuditViewMode.BYELEMENT);
                CheckerView.this.byTypeButton.setSelection(false);
                CheckerView.this.byRuleButton.setSelection(false);
                CheckerView.this.byListButton.setSelection(false);
            }
        
            @Override
            public void mouseDown(MouseEvent e) {
            }
        
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });
        this.byListButton = new Button(root, SWT.TOGGLE);
        this.byListButton.setImage(Audit.getImageDescriptor("icons/LayoutFlat.png").createImage());
        this.byListButton.addMouseListener(new MouseListener() {
        
            @Override
            public void mouseUp(MouseEvent e) {
                CheckerView.this.auditPanel.setAuditViewMode(AuditViewMode.FLAT);
                CheckerView.this.byTypeButton.setSelection(false);
                CheckerView.this.byRuleButton.setSelection(false);
                CheckerView.this.byElementButton.setSelection(false);
            }
        
            @Override
            public void mouseDown(MouseEvent e) {
            }
        
            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });
        this.auditPanel.createPanel(root);
        gd = new GridData();
        gd.horizontalSpan = 6;
        gd.horizontalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessVerticalSpace = true;
        this.auditPanel.getPanel().setLayoutData(gd);
        
        this.auditPanel.setInput(this.auditService.getAuditEngine());
        
        this.checker.setView(this);
        this.checkerThread = new Thread(this.checker);
        this.checkerThread.setPriority(Thread.MIN_PRIORITY);
        this.checkerThread.setName("CHECKER");
        this.checkerThread.start();
        initContextMenu();
        return this.auditPanel.getPanel();
    }

    @objid ("9ad02847-2a84-465f-ac9f-47e1e2c762e0")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @objid ("511c844f-45a0-4750-9d9b-63ec8abed84f")
    @Override
    public void init() {
        getShell().setText(Audit.I18N.getString("Audit.CheckerView.DialogTitle"));
        setTitle(Audit.I18N.getString("Audit.CheckerView.DialogTitle"));
        setMessage(Audit.I18N.getString("Audit.CheckerView.DialogMessage"));
        this.getShell().setSize(800, 400);
        this.getShell().setMinimumSize(300, 300);
    }

    @objid ("63a3a266-4063-45a7-b816-3310e63d25b5")
    @Override
    public boolean close() {
        this.checkerThread.interrupt();
        this.auditPanel.dispose();
        return super.close();
    }

    @objid ("9f139001-78db-4aef-967b-23998fd56d4c")
    public void refresh() {
        if (!this.refreshPending && !CheckerView.this.statusLabel.isDisposed()) {
            this.refreshPending = true;    
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (!CheckerView.this.statusLabel.isDisposed()) {
                        CheckerView.this.statusLabel.setText(CheckerView.this.checker.getStatus());
                    }
                    CheckerView.this.refreshPending = false;
                }
            });
        }
    }

    @objid ("9d183d37-76f3-4dbe-b645-2291bb194daf")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("08af30b1-eb5c-48d3-8951-1b073f8ae369")
    private List<MObject> getSelectedElements(Object selection) {
        ArrayList<MObject> selectedElements = new ArrayList<>();
        
        if (selection instanceof MObject) {
            selectedElements.add((MObject) selection);
        } else if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() >= 1) {
            Object[] elements = ((IStructuredSelection) selection).toArray();
            for (Object element : elements) {
                if (element instanceof MObject) {
                    selectedElements.add((MObject) element);
                } else if (element instanceof IAdaptable) {
                    final MObject adapter = (MObject) ((IAdaptable) element).getAdapter(MObject.class);
                    if (adapter != null) {
                        selectedElements.add(adapter);
                    }
                }
            }
        }
        return selectedElements;
    }

    @objid ("5aa3ff89-5b8f-42fc-a96b-14c9b14f5e74")
    public void refreshContent() {
        checker.setView(this);
        checkerThread = new Thread(checker);
        checkerThread.setPriority(Thread.MIN_PRIORITY);
        checkerThread.setName("CHECKER");
        checkerThread.start();
    }

    @objid ("416e764e-7153-4d39-bdaf-d320e8f52fb9")
    private void initContextMenu() {
        // initalize the context menu
        MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                manager.add(new SelectInExplorerAction(navigationService,  auditPanel.getTreeViewer()));
                manager.add(new ShowDetailsAction(projectService,navigationService,  auditPanel.getTreeViewer()));
                manager.add(new AuditSeverityAction("AuditAdvice",auditService,CheckerView.this));
                manager.add(new AuditSeverityAction("AuditWarning",auditService,CheckerView.this));
                manager.add(new AuditSeverityAction("AuditError",auditService,CheckerView.this));
                manager.add(new DisableRuleHandlerAction(auditService, CheckerView.this));            
            }
        });
        Menu menu = menuMgr.createContextMenu(this.auditPanel.getTreeViewer().getTree());
        this.auditPanel.getTreeViewer().getTree().setMenu(menu);
    }

    @objid ("d0213c43-d025-4147-8d56-59e9ac747268")
    public Tree getAuditTree() {
        return auditPanel.getTreeViewer().getTree();
    }

}
