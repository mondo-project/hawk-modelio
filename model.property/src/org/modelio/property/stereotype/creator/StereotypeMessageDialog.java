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
                                    

/**
 * 
 */
package org.modelio.property.stereotype.creator;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Same as {@link MessageDialog} with the following changes:
 * <ul>
 * <li>the dialog is resizeable,
 * <li>the message is selectable and can be copied in the clipboard,
 * <li>a scroll bar displays if the message text is not completely visible.
 */
@objid ("84df72b0-dc5e-4650-a2a7-b4086833150c")
public class StereotypeMessageDialog extends MessageDialog {
    @objid ("9792b1bf-6955-4ca7-9642-888e0ca6b88e")
    private Text messageText;

    /**
     * Create a message dialog. Note that the dialog will have no visual representation (no widgets) until it is told to
     * open.
     * <p>
     * The labels of the buttons to appear in the button bar are supplied in this constructor as an array. The
     * <code>open</code> method will return the index of the label in this array corresponding to the button that was
     * pressed to close the dialog. If the dialog was dismissed without pressing a button (ESC, etc.) then -1 is
     * returned. Note that the <code>open</code> method blocks.
     * </p>
     * @param parentShell the parent shell
     * @param dialogTitle the dialog title, or <code>null</code> if none
     * @param dialogTitleImage the dialog title image, or <code>null</code> if none
     * @param dialogMessage the dialog message
     * @param dialogImageType one of the following values:
     * <ul>
     * <li><code>MessageDialog.NONE</code> for a dialog with no image</li>
     * <li><code>MessageDialog.ERROR</code> for a dialog with an error image</li>
     * <li><code>MessageDialog.INFORMATION</code> for a dialog with an information image</li>
     * <li><code>MessageDialog.QUESTION </code> for a dialog with a question image</li>
     * <li><code>MessageDialog.WARNING</code> for a dialog with a warning image</li>
     * </ul>
     * @param dialogButtonLabels an array of labels for the buttons in the button bar
     * @param defaultIndex the index in the button label array of the default button
     */
    @objid ("766313a4-f2c8-4368-985c-350d8ca13435")
    public StereotypeMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType,
                dialogButtonLabels, defaultIndex);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /**
     * (non-Javadoc)
     * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
     */
    @objid ("f22cff2a-fe94-43fd-97e8-cd217ef48e96")
    @Override
    protected Point getInitialSize() {
        return new Point(600, 250);
    }

    /**
     * Create the area the message will be shown in.
     * <p>
     * The parent composite is assumed to use GridLayout as its layout manager, since the parent is typically the
     * composite created in {@link Dialog#createDialogArea}.
     * </p>
     * @param composite The composite to parent from.
     * @return Control
     */
    @objid ("c6741ba2-684e-4eb1-a18c-9e8af1c9d11d")
    @Override
    protected Control createMessageArea(Composite composite) {
        // create composite
        // create image
        Image image = getImage();
        if (image != null) {
            this.imageLabel = new Label(composite, SWT.NULL);
            image.setBackground(this.imageLabel.getBackground());
            this.imageLabel.setImage(image);
            GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING)
                    .applyTo(this.imageLabel);
        }
        
        // create message
        if (this.message != null) {
            ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.H_SCROLL |
                                                                                   SWT.V_SCROLL);
            // scrolledComposite will enlarge the Text if resized
            scrolledComposite.setExpandVertical(true);
            scrolledComposite.setExpandHorizontal(true);
        
            // Set scrolledComposite minimum size
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            gd.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
            gd.heightHint = 80;
            scrolledComposite.setLayoutData(gd);
        
            // Create the Text
            this.messageText = new Text(scrolledComposite, SWT.MULTI /*| SWT.WRAP*/);
            this.messageText.setText(this.message);
            this.messageText.setEditable(false);
            this.messageText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
            scrolledComposite.setContent(this.messageText);
        
            // Compute Text size
            final Point point = this.messageText.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
            this.messageText.setSize(point);
        
            // Set size where scroll bars will show
            scrolledComposite.setMinSize(new Point(point.x, point.y));
        
            // Constraint scrolledComposite width to be at least the Text width
            if (point.x > gd.widthHint)
                gd.widthHint = point.x;
        
            // Constraint scrolledComposite width to be at most half of the screen
            final int monitorWidth = this.getShell().getDisplay().getPrimaryMonitor().getBounds().width;
            if (gd.widthHint > monitorWidth / 2)
                gd.widthHint = monitorWidth / 2;
        
            // Constraint scrolledComposite height to be the Text height
            gd.heightHint = point.y;
        
            // Constraint scrolledComposite height to be at most 200
            if (gd.heightHint > 200)
                gd.heightHint = 200;
        
        }
        
        this.messageLabel = null;
        return composite;
    }

    @objid ("d09cc3f9-d0e5-4775-9d33-8ba3c566fb9e")
    @Override
    protected Control createDialogArea(Composite parent) {
        return createMessageArea(parent);
    }

    @objid ("5864680b-d5af-4127-83e5-2f7c71e0baae")
    @Override
    protected Control createButtonBar(Composite parent) {
        Control ret = super.createButtonBar(parent);
        
        GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.CENTER).span(2, 1)
                .applyTo(ret);
        return ret;
    }

    /**
     * Convenience method to open a simple confirm (OK/Cancel) dialog.
     * @param parent the parent shell of the dialog, or <code>null</code> if none
     * @param title the dialog's title, or <code>null</code> if none
     * @param message the message
     * @return <code>true</code> if the user presses the OK button, <code>false</code> otherwise
     */
    @objid ("9bbd53ec-3afe-48d1-a74c-81dd5e66e81c")
    public static boolean openConfirm(Shell parent, String title, String message) {
        StereotypeMessageDialog dialog = new StereotypeMessageDialog(parent, title, null, // accept
                                                                     // the
                                                                     // default
                                                                     // window
                                                                     // icon
                                                                     message,
                                                                     QUESTION,
                                                                     new String[] {
                                                                             IDialogConstants.OK_LABEL,
                                                                             IDialogConstants.CANCEL_LABEL },
                                                                     0); // OK
        // is
        // the
        // default
        return dialog.open() == 0;
    }

    /**
     * Convenience method to open a standard error dialog.
     * @param parent the parent shell of the dialog, or <code>null</code> if none
     * @param title the dialog's title, or <code>null</code> if none
     * @param message the message
     */
    @objid ("d5365357-c967-409c-a4dc-9204176c2b7c")
    public static void openError(Shell parent, String title, String message) {
        StereotypeMessageDialog dialog = new StereotypeMessageDialog(parent, title, null, // accept the default window icon
                                                                     message,
                                                                     ERROR,
                                                                     new String[] { IDialogConstants.OK_LABEL },
                                                                     0); // ok is the default
        dialog.open();
        return;
    }

    /**
     * Convenience method to open a modeless standard information dialog.
     * @param parent the parent shell of the dialog, or <code>null</code> if none
     * @param title the dialog's title, or <code>null</code> if none
     * @param message the message
     */
    @objid ("119d7ece-5740-4be7-a7cc-ec09024e815f")
    public static void openModelessInformation(Shell parent, String title, String message) {
        StereotypeMessageDialog dialog = new StereotypeMessageDialog(parent, // parent
                                                                     title, // dialog title
                                                                     null, // accept the default window icon
                                                                     message, // message
                                                                     INFORMATION,// dialog type
                                                                     new String[] { IDialogConstants.OK_LABEL }, // button labels 
                                                                     0 // ok is the default
        );
        
        dialog.setBlockOnOpen(false);
        
        // Hack the shell style so that the dialog is not modal.
        int style = dialog.getShellStyle();
        style &= ~(SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL);
        dialog.setShellStyle(style);
        
        dialog.open();
        return;
    }

    /**
     * Convenience method to open a standard information dialog.
     * @param parent the parent shell of the dialog, or <code>null</code> if none
     * @param title the dialog's title, or <code>null</code> if none
     * @param message the message
     */
    @objid ("bd0df126-b593-4853-a036-986c551e8cde")
    public static void openInformation(Shell parent, String title, String message) {
        StereotypeMessageDialog dialog = new StereotypeMessageDialog(parent, // parent
                                                                     title, // dialog title
                                                                     null, // accept the default window icon
                                                                     message, // message
                                                                     INFORMATION,// dialog type
                                                                     new String[] { IDialogConstants.OK_LABEL }, // button labels 
                                                                     0 // ok is the default
        );
        dialog.open();
        return;
    }

    /**
     * Convenience method to open a simple Yes/No question dialog.
     * @param parent the parent shell of the dialog, or <code>null</code> if none
     * @param title the dialog's title, or <code>null</code> if none
     * @param message the message
     * @return <code>true</code> if the user presses the OK button, <code>false</code> otherwise
     */
    @objid ("913f919e-fb11-4017-9e0c-13e52f65119a")
    public static boolean openQuestion(Shell parent, String title, String message) {
        StereotypeMessageDialog dialog = new StereotypeMessageDialog(parent, title, null, // accept
                                                                     // the
                                                                     // default
                                                                     // window
                                                                     // icon
                                                                     message,
                                                                     QUESTION,
                                                                     new String[] {
                                                                             IDialogConstants.YES_LABEL,
                                                                             IDialogConstants.NO_LABEL },
                                                                     0); // yes is
        // the
        // default
        return dialog.open() == 0;
    }

    /**
     * Convenience method to open a standard warning dialog.
     * @param parent the parent shell of the dialog, or <code>null</code> if none
     * @param title the dialog's title, or <code>null</code> if none
     * @param message the message
     */
    @objid ("97ff0d38-51f1-4e4a-845a-fef0a3409d3f")
    public static void openWarning(Shell parent, String title, String message) {
        StereotypeMessageDialog dialog = new StereotypeMessageDialog(parent, title, null, // accept the default window icon
                                                                     message,
                                                                     WARNING,
                                                                     new String[] { IDialogConstants.OK_LABEL },
                                                                     0); // ok is the default
        dialog.open();
        return;
    }

}
