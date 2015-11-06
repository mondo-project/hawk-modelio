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
                                    

//------------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.epfcommon.ui.util;

import java.net.MalformedURLException;
import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.epfcommon.utils.FileUtil;
import org.osgi.framework.Bundle;

/**
 * Utility class for retrieving data from the clipboard.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("98578a2b-4c48-4c59-9d7f-b8f96503c02b")
public class ClipboardUtil {
    @objid ("b3c76168-90e3-42da-85bd-7ef704eab63b")
    private static final String SOURCE_URL = "SourceURL:"; // $NON-NLS-1$

    /**
     * The current clipboard.
     */
    @objid ("9618694a-bd71-494a-ad8c-7a5d4138160a")
    private static Clipboard clipboard;

    @objid ("35fcb2be-e936-48cf-be91-c2c9c6de8cb8")
    private static Transfer htmlTransfer = null;

    /**
     * Gets the HTML source URL from the current clipboard.
     * @return the HTML source URL or <code>null</code>
     */
    @objid ("de9d159a-89cd-4a4a-b6fd-4f2a1b7622d6")
    public static String getHTMLSourceURL() {
        if (htmlTransfer == null) {
            return null;
        }
        
        Clipboard lClipboard = new Clipboard(Display.getCurrent());
        String sourceURL = null;
        try {
            String htmlContent = (String) lClipboard.getContents(htmlTransfer);
            if (htmlContent != null && htmlContent.length() > 0) {
                int index = htmlContent.indexOf(SOURCE_URL);
                if (index > 0) {
                    sourceURL = htmlContent.substring(index
                            + SOURCE_URL.length());
                    sourceURL = sourceURL.substring(0, sourceURL
                            .indexOf(FileUtil.LINE_SEP));
                }
            }
            if (sourceURL != null && sourceURL.indexOf("\\") != -1) { //$NON-NLS-1$
                // IE provides sourceURL in form "file://C:\foo\bar.htm"
                // but when the hrefs are resolved, files look like "file:///C:/foo/bar.htm"
                URL url = new URL(sourceURL);
                sourceURL = url.toExternalForm();
                if (sourceURL.startsWith("file://") && !sourceURL.startsWith("file:///")) { //$NON-NLS-1$ //$NON-NLS-2$
                    // need to add a third / so rte.js can match the sourceURL to hrefs
                    sourceURL = "file:///" + sourceURL.substring(7); //$NON-NLS-1$
                }
            }
            return sourceURL;
        } catch (MalformedURLException urlEx) {
            return sourceURL;
        } finally {
            lClipboard.dispose();
        }
    }

    /**
     * Copy the string to the clipboard.
     * @param string the text to copy.
     */
    @objid ("2c05f87f-02e9-405f-b328-c88f6eb81a7b")
    public static void copyTextHTMLToClipboard(String string) {
        if (clipboard != null)
            clipboard.dispose();
        clipboard = new Clipboard(null);
        clipboard.setContents(new Object[] { string, string },
                new Transfer[] { TextTransfer.getInstance(), HTMLTransfer.getInstance() });
        if (clipboard != null)
            clipboard.dispose();
    }


static {
        if (SWT.getPlatform().equals("win32")) { //$NON-NLS-1$
            try {
                Bundle bundle = Platform
                        .getBundle("org.eclipse.epf.common.win32.win32.x86"); //$NON-NLS-1$
                Class<?> c = bundle
                        .loadClass("org.eclipse.epf.common.win32.Win32HTMLTransfer"); //$NON-NLS-1$
                if (c != null) {
                    htmlTransfer = (Transfer) c.newInstance();
                }
            } catch (Exception e) {
                htmlTransfer = null;
            }
        }
    }
}
