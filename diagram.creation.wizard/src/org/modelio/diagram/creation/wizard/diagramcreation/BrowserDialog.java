package org.modelio.diagram.creation.wizard.diagramcreation;

import java.net.MalformedURLException;
import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.diagram.creation.wizard.plugin.DiagramCreationWizard;

/**
 * This class implements the dialog box that display the audit entry details
 */
@objid ("46cd5bed-10a9-4f62-b28d-859484663c49")
public class BrowserDialog extends ModelioDialog {
    @objid ("8d99e0fc-0928-4c09-9829-3401f7920c54")
    protected String docUrl;

    @objid ("d1c3e750-e8cd-4fb4-9e75-69a115ab26c4")
    protected Browser browser;

    /**
     * Create an BrowserDialog instance.
     * @param parentShell The parent shell.
     * @param docUrl The documentation url to display.
     */
    @objid ("5c20e64a-2420-4554-abdc-b74e317b1261")
    public BrowserDialog(final Shell parentShell, final String docUrl) {
        super(parentShell);
        this.docUrl = docUrl;
    }

    /**
     * Add buttons to the buttons bar in the bottom of the dialog.
     * <p>
     * Here we just need to have a "close" button.
     * @param parent the parent composite of the dialog.
     */
    @objid ("8a3c199d-ad1a-4b61-b914-33e8be406f29")
    @Override
    public void addButtonsInButtonBar(final Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * This is the main method that is called to construct the GUI content of the box.
     * @param parent the parent composite of the dialog.
     */
    @objid ("c96c1ee7-4725-430e-91c7-ddae5aeba1c5")
    @Override
    public Control createContentArea(final Composite parent) {
        this.browser = new Browser(parent, SWT.BORDER);
        this.browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.browser.setMenu(new Menu(this.browser));
        this.browser.setJavascriptEnabled(false);
        
        this.browser.addLocationListener(new LocationAdapter() {
            @Override
            public void changing(LocationEvent event)
            {
        
                String newLocation = event.location;
                try {
                    URL url = new URL(newLocation);
                    if (url.getQuery() != null)
                        BrowserDialog.this.browser.setUrl(BrowserDialog.this.docUrl);
                } catch (MalformedURLException e) {
                    DiagramCreationWizard.LOG.error(e);
                }
        
            }
        });
        
        this.browser.setUrl(this.docUrl);
        return this.browser;
    }

    /**
     * Init is called when the dialog box is opened.
     */
    @objid ("562f04ec-9642-4369-bc86-0820972dbbab")
    @Override
    public void init() {
        setLogoImage(null);
        // Put the messages in the banner area
        getShell().setText(DiagramCreationWizard.I18N.getString("BrowserDialog.DialogTitle"));
        setTitle(DiagramCreationWizard.I18N.getString("BrowserDialog.DialogTitle"));
        //setMessage(Messages.getString("BrowserDialog.DialogMessage"));
        this.getShell().setSize(600, 550);
        this.getShell().setMinimumSize(600, 550);
    }

}
