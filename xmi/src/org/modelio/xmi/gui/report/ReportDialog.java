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
                                    

package org.modelio.xmi.gui.report;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.modelio.api.app.navigation.INavigationService;
import org.modelio.api.ui.ModelioDialog;
import org.modelio.api.ui.UIColor;
import org.modelio.xmi.gui.report.ReportModel.ElementMessage;
import org.modelio.xmi.plugin.Xmi;

/**
 * Dialog box used for the generation report.
 */
@objid ("8a07ec2e-d98c-4c20-b70c-be9b3af9d491")
class ReportDialog extends ModelioDialog {
    @objid ("a62b65d3-6c64-4c7e-b84c-3cc733d0824d")
    private ReportModel model;

    @objid ("89a54956-dcbb-4cd4-b6d8-b162f5abd7d4")
     INavigationService navigationService;

    @objid ("09f88e6b-09d4-4efa-ac04-deb9f82f5338")
    private Image warningImage;

    @objid ("95303b66-cb52-4637-9b1a-d58af1826340")
    private Image errorImage;

    @objid ("bc8a6d64-3b0e-46fc-9587-1a8cad51b25c")
     Table table;

    @objid ("c7c8d7be-f6bd-47fb-99ef-b51cdbd648e3")
    private Image infoImage;

    @objid ("ba27d8cc-e3a0-4eae-9b5a-b84f6c82390c")
     Text descriptionText;

    @objid ("4320b9da-9b24-4f43-a607-4ef8aa345f07")
    public ReportDialog(Shell parentShell, final INavigationService navigationService) {
        super (parentShell);
        setShellStyle (SWT.DIALOG_TRIM | getDefaultOrientation ());
        this.navigationService = navigationService;
    }

    @objid ("00856ef6-e513-4f86-8045-b3b24d9e54ab")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton (parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @objid ("b6e6dc8a-9020-4a2c-a6d0-907846478361")
    @Override
    public Control createContentArea(Composite parent) {
        this.warningImage = JFaceResources.getImage ("dialog_messasge_warning_image"); //$NON-NLS-1$
        this.errorImage = JFaceResources.getImage ("dialog_message_error_image"); //$NON-NLS-1$
        this.infoImage = JFaceResources.getImage ("dialog_messasge_info_image"); //$NON-NLS-1$
        
        Point s = getInitialSize ();
        
        
        this.table = new Table (parent, SWT.MULTI | SWT.BORDER |
                SWT.FULL_SELECTION);
        GridData data = new GridData (SWT.FILL, SWT.FILL, true, true);
        this.table.setLayoutData (data);
        this.table.setLinesVisible (true);
        
        TableColumn column = new TableColumn (this.table, SWT.NONE);
        column.setText ("Message");
        
        updateViewFromModel ();
        
        column.setWidth (s.x);
        
        this.table.addMouseListener (new MouseListener () {
            @Override
            public void mouseDoubleClick (MouseEvent e) {
                TableItem item = ReportDialog.this.table.getItem (new Point (e.x, e.y));
                if (item != null) {
                    if (item.getData () instanceof ElementMessage) {
                        ElementMessage theElement = (ElementMessage) item.getData ();
        
                        String desc = theElement.description;
                        ReportDialog.this.descriptionText.setText (desc);
        
                        // On a double clic, select the element
                        if (theElement.element != null && theElement.element.isValid ()) {
                            ReportDialog.this.navigationService.fireNavigate (theElement.element);
                        }
                    }
                }
            }
        
            @Override
            public void mouseDown (MouseEvent e) {
                // Nothing to do
            }
        
            @Override
            public void mouseUp (MouseEvent e) {
                TableItem item = ReportDialog.this.table.getItem (new Point (e.x, e.y));
                if (item != null) {
                    if (item.getData () instanceof ElementMessage) {
                        ElementMessage theElement = (ElementMessage) item.getData ();
        
                        String desc = theElement.description;
                        ReportDialog.this.descriptionText.setText (desc);
        
                        // On a CTRL + clic, select the element
                        if ((e.stateMask & SWT.CTRL) != 0) {
                            if (theElement.element != null && theElement.element.isValid ()) {
                                ReportDialog.this.navigationService.fireNavigate (theElement.element);
                            }
                        }
                    }
                }
            }
        });
        
        this.descriptionText = new Text (parent, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
        GridData data_description = new GridData (SWT.FILL, SWT.FILL, true, false);
        data_description.heightHint = 50;
        this.descriptionText.setLayoutData (data_description);
        this.descriptionText.setBackground (UIColor.TEXT_READONLY_BG);
        return parent;
    }

    @objid ("3a854dbd-3471-451f-a466-f9348b96599e")
    private void updateViewFromModel() {
        if (this.table != null) {
            this.table.removeAll ();
        
            if (this.model != null) {
                for (ElementMessage error : this.model.getErrors ()) {
                    TableItem item = new TableItem (this.table, SWT.NONE);
                    item.setImage (0, this.errorImage);
                    item.setText (0, error.message);
                    item.setData (error);
                }
        
                for (ElementMessage warning : this.model.getWarnings ()) {
                    TableItem item = new TableItem (this.table, SWT.NONE);
                    item.setImage (0, this.warningImage);
                    item.setText (0, warning.message);
                    item.setData (warning);
                }
        
                for (ElementMessage info : this.model.getInfos ()) {
                    TableItem item = new TableItem (this.table, SWT.NONE);
                    item.setImage (0, this.infoImage);
                    item.setText (0, info.message);
                    item.setData (info);
                }
            }
        
            this.table.getColumn (0).pack ();
        }
    }

    @objid ("e6907d4d-2738-4d18-8fd5-9795de19ef6f")
    public void setModel(ReportModel model) {
        this.model = model;
        updateViewFromModel ();
    }

    @objid ("35a20a5c-4e7d-43e0-b09c-e212c1ce145d")
    public boolean isDisposed() {
        Shell s = getShell ();
        return s == null || s.isDisposed ();
    }

    @objid ("47e0ee66-3634-4709-abdb-eddbbc3c3522")
    @Override
    public void init() {
        Shell shell = getShell ();
        
        // Put the messages in the banner area
        setLogoImage (null);
        shell.setText (Xmi.I18N.getString ("Gui.Export.ReportDialogTitle")); 
        setTitle (Xmi.I18N.getString ("Gui.Export.ReportDialogTitle")); 
        setMessage (Xmi.I18N.getString ("Gui.Export.ReportDialogMessage"));
    }

    @objid ("a4ea5946-ef41-47b0-aef3-2ff504ac05d8")
    @Override
    protected Point getInitialSize() {
        Point p = super.getInitialSize();
        p.x = (int) Math.floor(p.x*1.35);
        return p;
    }

}
