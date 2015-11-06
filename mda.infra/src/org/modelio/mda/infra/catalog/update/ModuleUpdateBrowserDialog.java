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
                                    

package org.modelio.mda.infra.catalog.update;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.module.catalog.FileModuleStore;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vbasic.net.UriPathAccess;

@objid ("3a97523c-6868-498c-baa8-a65e45c982ab")
public class ModuleUpdateBrowserDialog extends ModelioDialog {
    @objid ("fd9130cf-c6de-4959-8583-ebcb0c56ba49")
    protected FileModuleStore catalog;

    @objid ("8b755504-3c19-4493-92a1-351a02cc7773")
    protected List<ModuleUpdateDescriptor> modulesToUpdate;

    @objid ("c9bfda7f-175e-4d7a-8a3a-e98d985da5ac")
    protected static final Image CHECKED = MdaInfra.getImageDescriptor("icons/checked.gif").createImage();

    @objid ("eaa7858e-fdbd-41fc-8196-edfd98a92c1b")
    protected static final Image UNCHECKED = MdaInfra.getImageDescriptor("icons/unchecked.gif").createImage();

    @objid ("569f19e3-1625-4233-806e-28f4309d0076")
    protected Browser browser;

    @objid ("a4de78ab-9651-4da9-8930-2ad755775562")
    protected Button updateButton;

    @objid ("a03ca026-a6cd-4ec6-b83e-0809ab6883b7")
    protected IModelioProgressService progressService;

    @objid ("cc78bdff-9021-4b14-aa0a-f2826efbd913")
    public ModuleUpdateBrowserDialog(Shell parentShell, List<ModuleUpdateDescriptor> modulesToUpdate, FileModuleStore catalog, IModelioProgressService progressService) {
        super(parentShell);
        this.modulesToUpdate = modulesToUpdate;
        this.catalog = catalog;
        this.progressService = progressService;
    }

    @objid ("67e6c5f5-995f-4cb8-9b21-0337980af059")
    @Override
    public Control createContentArea(Composite parent) {
        Composite compo = new Composite(parent, SWT.NONE);
        GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        layoutData.minimumHeight = 150;
        compo.setLayoutData(layoutData);
        compo.setLayout(new GridLayout(2, false));
        
        Composite leftComposite = new Composite(compo, SWT.NONE);
        leftComposite.setLayout(new GridLayout(1, false));
        leftComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
        
        // Define the TableViewer
        final TableViewer moduleTableViewer = new TableViewer(leftComposite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
        final Table moduleTable = moduleTableViewer.getTable();
        moduleTable.setHeaderVisible(true);
        moduleTable.setLayoutData(new GridData(SWT.BEGINNING, SWT.FILL, false, true));
        
        // Create the columns 
        createColumns(moduleTableViewer);
        
        // Set the ContentProvider
        moduleTableViewer.setContentProvider(ArrayContentProvider.getInstance());
        
        moduleTableViewer.setInput(this.modulesToUpdate);
        
        for (int i = 0 ; i < moduleTable.getColumnCount(); i++) {
            TableColumn col = moduleTable.getColumn(i);
            col.pack();
            
            // The column with an icon isn't resized well after pack, we need to add the image width
            if (i == 4) {
                final int width = col.getWidth();
                col.setWidth(width + 50);
            }
        }
        
        moduleTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    if (structuredSelection.size() == 1) {
                        Object obj = structuredSelection.getFirstElement();
                        if (obj instanceof ModuleUpdateDescriptor) {                            
                            String url = ((ModuleUpdateDescriptor) obj).getLink();
                            ModuleUpdateBrowserDialog.this.browser.setUrl(url);
                        }
                    }
                }
            }
        });
        
        Composite buttonComposite = new Composite(leftComposite, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.END, SWT.END, false, false));
        buttonComposite.setLayout(new GridLayout(2, false));
        
        // Select all button
        Button selectAllButton = new Button(buttonComposite, SWT.PUSH);
        selectAllButton.setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.SelectAll"));
        selectAllButton.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (ModuleUpdateDescriptor moduleDescriptor : ModuleUpdateBrowserDialog.this.modulesToUpdate) {
                    moduleDescriptor.setToUpdate(true);
                }
                moduleTableViewer.setInput(ModuleUpdateBrowserDialog.this.modulesToUpdate);
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                //
            }
        });
        selectAllButton.setLayoutData(new GridData());
        
        // Unselect all button
        Button unselectAllButton = new Button(buttonComposite, SWT.PUSH);
        unselectAllButton.setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.UnselectAll"));
        unselectAllButton.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                for (ModuleUpdateDescriptor moduleDescriptor : ModuleUpdateBrowserDialog.this.modulesToUpdate) {
                    moduleDescriptor.setToUpdate(false);
                }
                moduleTableViewer.setInput(ModuleUpdateBrowserDialog.this.modulesToUpdate);
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                //
            }
        });
        unselectAllButton.setLayoutData(new GridData());
        
        this.browser = new Browser(compo, SWT.NONE);
        this.browser.setText("");
        
        final GridData browserLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browserLayoutData.widthHint = 300;
        this.browser.setLayoutData(browserLayoutData);
        
        moduleTableViewer.setSelection(new StructuredSelection(moduleTableViewer.getElementAt(0)));
        return null;
    }

    @objid ("7139fad5-4f56-4893-9d30-bb1e740d212f")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        this.updateButton = createButton(parent, OK, MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.Update"), true);
        this.updateButton.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent evt) {
                IRunnableWithProgress runnable = new IRunnableWithProgress() {
        
                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        List<ModuleUpdateDescriptor> selectedModules = new ArrayList<>();
                        for (ModuleUpdateDescriptor descriptor : ModuleUpdateBrowserDialog.this.modulesToUpdate) {
                            if (descriptor.isToUpdate()) {
                                selectedModules.add(descriptor);
                            }
                        }
                        int modulesToUpdateSum = selectedModules.size();
                        monitor.beginTask(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.UpdateProgressTitle"), modulesToUpdateSum*5);
                        for (int i=0; i<modulesToUpdateSum; i++) {
                            if (monitor.isCanceled()) {
                                break;  // if monitor is canceled
                            }
                            ModuleUpdateDescriptor desc = selectedModules.get(i);
                            //Keys {0}:counter {1}:sum of modules {2}:module file name
                            monitor.subTask(MdaInfra.I18N.getMessage("ModuleUpdateBrowserDialog.UpdateModulesProgressSubTask", String.valueOf(i+1), String.valueOf(modulesToUpdateSum), desc.getName()));                            
                            monitor.worked(1);
        
                            try (UriPathAccess pathAccess = new UriPathAccess(URIUtil.fromString(desc.getDownloadLink()), null)) {
                                final Path path = pathAccess.getPath();
                                monitor.worked(2);
                                // install in catalog
                                ModuleUpdateBrowserDialog.this.catalog.getModuleHandle(path, null);
                                monitor.worked(1);
                                pathAccess.close();
                            } catch (IOException | URISyntaxException e) {
                                MdaInfra.LOG.error("Unable to install module " + desc.getDownloadLink());
                                MdaInfra.LOG.debug(e);
                            }
                            monitor.worked(1);
                        }
                        monitor.done();
                    }                    
                };
                try {
                    ModuleUpdateBrowserDialog.this.progressService.run(true, true, runnable);
                } catch (InvocationTargetException | InterruptedException e) {
                    MdaInfra.LOG.debug(e);
                }          
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        });
        
        createButton(parent, CANCEL, MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.Close"), true);
    }

    @objid ("4a98a9b5-8949-4c2e-8a42-63a1dfa29c29")
    @Override
    public void init() {
        getShell().setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.ShellTitle")); //$NON-NLS-1$);
        setTitle(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.Title")); //$NON-NLS-1$
        setMessage(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.Message")); //$NON-NLS-1$
        
        // Position and resize dialog shell
        int width = 1100;
        int height = 800;
        
        Rectangle refBounds = this.getShell().getParent().getBounds();
        this.getShell().setMinimumSize(width, height);
        this.getShell().layout(true);
        
        this.getShell().setBounds(refBounds.x + (refBounds.width - width) / 2, refBounds.y + (refBounds.height - height) / 2,
                width, height);
    }

    @objid ("62d741c0-d6b5-490c-ad81-08ce978d89f9")
    private void createColumns(final TableViewer tableViewer) {
        TableViewerColumn nameCol = new TableViewerColumn(tableViewer, SWT.NONE);
        nameCol.getColumn().setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.column.ModuleName"));
        nameCol.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                ModuleUpdateDescriptor mud = (ModuleUpdateDescriptor) element;
                return mud.getName();
            }
            
            @Override
            public Image getImage(Object element) {
                return MdaInfra.getImageDescriptor("icons/module.png").createImage();
            }
        });
        
        TableViewerColumn oldVersionCol = new TableViewerColumn(tableViewer, SWT.NONE);
        oldVersionCol.getColumn().setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.column.CurrentVersion"));
        oldVersionCol.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                ModuleUpdateDescriptor mud = (ModuleUpdateDescriptor) element;
                if (mud.getCurrentVersion().equals("")) {
                    return MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.NotInstalled");
                }
                return mud.getCurrentVersion();
            }
        });
        
        TableViewerColumn newVersionCol = new TableViewerColumn(tableViewer, SWT.NONE);
        newVersionCol.getColumn().setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.column.NewVersion"));
        newVersionCol.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                ModuleUpdateDescriptor mud = (ModuleUpdateDescriptor) element;
                return mud.getNewVersion();
            }
        });      
        
        TableViewerColumn updateCol = new TableViewerColumn(tableViewer, SWT.NONE);
        updateCol.getColumn().setText(MdaInfra.I18N.getString("ModuleUpdateBrowserDialog.column.SelectInstall"));
        updateCol.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return "";
            }
        
            @Override
            public Image getImage(Object element) {
                if (((ModuleUpdateDescriptor) element).isToUpdate()) {
                    return CHECKED;
                } 
                return UNCHECKED;
            }
        });
        updateCol.setEditingSupport(new EditingSupport (tableViewer) {
            @Override
            protected Object getValue(Object element) {
                return ((ModuleUpdateDescriptor) element).isToUpdate();
            }
        
            @Override
            protected void setValue(Object element, Object value) {
                ((ModuleUpdateDescriptor) element).setToUpdate((Boolean) value);
                this.getViewer().refresh(element, true);
            }
        
            @Override
            protected CellEditor getCellEditor(Object element) {
                return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
            }
        
            @Override
            protected boolean canEdit(Object element) {
                return true;
            }
        });
    }

    @objid ("0644d8db-b0f6-40a2-93f6-19f5ff0dcf7a")
    @Override
    public boolean close() {
        return super.close();
    }

}
