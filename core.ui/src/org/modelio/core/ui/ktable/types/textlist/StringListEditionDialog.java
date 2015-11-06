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
                                    

package org.modelio.core.ui.ktable.types.textlist;

import java.net.URL;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.plugin.CoreUi;
import org.osgi.framework.Bundle;

/**
 * This class displays the results of a search for model elements (see Searcher
 * class). The found elements are proposed to the end user in a list where he
 * can select the element of his choice. Each time the user selects a different
 * element in the list a NAVIGATION event is fired. Along with the results, the
 * dialog displays the current regular expression that produced the results.
 * This expression can be modified leading to an update of the displayed
 * results. This allows for search refinement when many elements have been
 * found.
 */
@objid ("8dcd99b0-c068-11e1-8c0a-002564c97630")
public class StringListEditionDialog extends ModelioDialog {
    @objid ("a5152245-c068-11e1-8c0a-002564c97630")
    private String title = "";

    @objid ("a517838e-c068-11e1-8c0a-002564c97630")
    private String message = "";

    @objid ("a5178392-c068-11e1-8c0a-002564c97630")
    private String detailedMessage = "";

    @objid ("cb016f07-269e-4413-9233-94af37110107")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Index.html";

    @objid ("35e7b037-0875-4ffa-a0f5-768452c2dd84")
    private List<String> initialContent;

    @objid ("a0f95fd8-a1d4-4a55-be42-4fa51bef5dd8")
    private int size;

    @objid ("8dcd99b8-c068-11e1-8c0a-002564c97630")
    private StringListCellEditor editor = null;

    @objid ("8dcd99bb-c068-11e1-8c0a-002564c97630")
    private IStringListValidator editionValidator = null;

    @objid ("23381fa8-6b4d-438c-a2ef-d5c9c6b16389")
    protected static StringListEditionDialog instance;

    @objid ("cf1efe9d-6c26-47d2-b671-908ecd58abd2")
    private StringListEditionComposite area;

    @objid ("8dcf2032-c068-11e1-8c0a-002564c97630")
    private StringListEditionDialog(Shell parentShell, String title, String message, int size, String detailedMessage, StringListCellEditor editor, List<String> initialContent, IStringListValidator editionValidator) {
        super(parentShell);
        this.title = title;
        this.message = message;
        this.size = size;
        this.detailedMessage = detailedMessage;
        this.editor = editor;
        this.editionValidator = editionValidator;
        this.initialContent = initialContent;
    }

    @objid ("8dcf2041-c068-11e1-8c0a-002564c97630")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @objid ("8dcf2045-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean close() {
        if (this.equals(instance)) { // should always be the case, could be an assert!
            instance = null;
        }
        return super.close();
    }

    @objid ("8dcf204a-c068-11e1-8c0a-002564c97630")
    @Override
    public Control createContentArea(Composite parent) {
        this.area = new StringListEditionComposite(parent, SWT.NONE, this.size);
        this.area.initContent(this.initialContent);
        
        getShell().pack();
        return this.area;
    }

    @objid ("8dd0a6d4-c068-11e1-8c0a-002564c97630")
    @Override
    public void init() {
        setLogoImage(null);
        
        getShell().setText(this.title);
        setTitle(this.title);
        setMessage(this.message);
        
        Point minSize = new Point(440,300);
        getShell().setMinimumSize(minSize);
    }

    @objid ("8dd0a6e1-c068-11e1-8c0a-002564c97630")
    @Override
    public int open() {
        return super.open();
    }

    @objid ("8dd0a6ed-c068-11e1-8c0a-002564c97630")
    @Override
    protected void cancelPressed() {
        this.editor.closeEditor(false);
        super.cancelPressed();
    }

    @objid ("8dd0a6f0-c068-11e1-8c0a-002564c97630")
    protected static Image getBundleImage(String relativePath) {
        Bundle imageBundle = Platform.getBundle(CoreUi.PLUGIN_ID);
        IPath bitmapPath = new Path(relativePath);
        URL bitmapUrl = FileLocator.find(imageBundle, bitmapPath, null);
        ImageDescriptor desc = ImageDescriptor.createFromURL(bitmapUrl);
        return desc.createImage();
    }

    @objid ("8dd0a6f5-c068-11e1-8c0a-002564c97630")
    @Override
    protected void okPressed() {
        List<String> values = this.area.getContent();
        this.editionValidator.validate(values);
        this.editor.closeEditor(true);
        super.okPressed();
    }

    @objid ("ea6da17a-15ca-46bb-8d09-8cde66ee1daa")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("ddd79991-ab0b-4f31-aaac-72d7c832f3b4")
    public static StringListEditionDialog getInstance(Shell parentShell, String title, String message, int size, String detailedMessage, StringListCellEditor editor, List<String> initialContent, IStringListValidator editionValidator) {
        if (parentShell == null)
            return null;
        
        if (instance != null) {
            return instance;
        }
        
        instance = new StringListEditionDialog(parentShell, title, message, size, detailedMessage, editor, initialContent, editionValidator);
        return instance;
    }

    @objid ("8c16dbfe-3584-4a1f-88d1-8369e5be5e18")
    public static void closeInstance() {
        if (instance != null) {
            Display.getDefault().asyncExec(new Runnable() {        
                @Override
                public void run() {
                    instance.close();
                }
            });
        }
    }

}
