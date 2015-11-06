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
package org.modelio.edition.html.view.actions;

import java.io.File;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.epfcommon.ui.util.ClipboardUtil;
import org.modelio.edition.html.epfcommon.utils.FileUtil;
import org.modelio.edition.html.epfcommon.utils.NetUtil;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.RichTextEditor;
import org.modelio.log.writers.PluginLogger;

/**
 * Pastes text from the clipboard onto a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("34bff51c-384f-462e-9c49-0b50cd67fdcf")
public class PasteAction extends RichTextAction {
    @objid ("6b4aff9a-2a43-42cb-b6b6-88254807000b")
    private static final String SOURCE_URL = "SourceURL:"; // $NON-NLS-1$

    @objid ("42a5dfca-fa8b-4746-ba23-86cc63793c23")
    private static final String HTM_EXT = ".htm"; // $NON-NLS-1$

    @objid ("4c09fd45-c0eb-4dff-81b9-e5698a724a6e")
    private static final String HTML_EXT = ".html"; // $NON-NLS-1$

    @objid ("a1a88cc3-0b07-40c1-9451-e77ec21ff903")
    protected static String sourceURLStr = ""; // $NON-NLS-1$

    @objid ("194f9be6-0d82-41cd-8c63-b6afb9a44101")
    private static String RESOURCES = "resources"; // $NON-NLS-1$

    @objid ("85903c25-4bbf-447e-b950-d3312a61f305")
    private static final Pattern HREF_REFERENCES = Pattern
			.compile(
					"href\\s*=\\s*\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // $NON-NLS-1$

    @objid ("9dc79c8f-d29c-4d4b-8787-863cb4d8f4db")
    protected static final Pattern p_image_ref = Pattern
			.compile(
					"(<(img|iframe).*?src\\s*=\\s*\")(.*?)(\")", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); // $NON-NLS-1$

    @objid ("76aae44c-3618-4cad-8d7b-98b4a68eae09")
    private PluginLogger logger;

    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("31cb2b95-0c73-49cc-8e8f-48163296879e")
    public PasteAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_PASTE);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_PASTE);
        setToolTipText(HtmlTextResources.pasteAction_toolTipText);
        this.logger = HtmlTextPlugin.getDefault().getLogger();
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("aa0aeb40-b33d-4d32-8589-6614862d49da")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("ecb6e2e4-b10d-4052-8a88-75d22a6ea657")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            copyLinkResources(richText);
            if (richText instanceof RichTextEditor
                    && ((RichTextEditor) richText).isHTMLTabSelected()) {
                StyledText styledText = ((RichTextEditor) richText)
                        .getSourceEdit();
                styledText.paste();
            } else {
                richText.executeCommand(IRichTextCommands.PASTE, sourceURLStr);
            }
        }
    }

    /**
     * Copies the link resources.
     * @param richText a rich text control
     */
    @objid ("cc58f7cb-8530-422e-a575-1c6a6f6fbbdc")
    protected void copyLinkResources(IRichText richText) {
        try {
            sourceURLStr = ClipboardUtil.getHTMLSourceURL();
            Clipboard clipboard = new Clipboard(Display.getCurrent());
            String html = (String) clipboard.getContents(HTMLTransfer
                    .getInstance());
            if (html != null && html.length() > 0) {
                String basePath = richText.getBasePath();
                URL sourceURL = null;
                if (sourceURLStr == null || sourceURLStr.length() == 0
                        || sourceURLStr.equals("about:blank")) { //$NON-NLS-1$
                    sourceURL = richText.getCopyURL();
                } else {
                    sourceURL = new URL(sourceURLStr);
                }
        
                Matcher matcher = HREF_REFERENCES.matcher(html);
                while (matcher.find()) {
                    String href = NetUtil.decodeURL(matcher.group(1));
                    try {
                        URL hrefURL = new URL(sourceURL, href);
                        String scheme = hrefURL.getProtocol();
                        if (scheme != null
                                && scheme
                                        .equalsIgnoreCase(NetUtil.FILE_SCHEME)) {
                            String url = hrefURL.getPath();
                            File srcFile = new File(NetUtil.decodeURL(url));
                            File tgtFile = null;
                            File tgtDir = null;
                            if (href.startsWith("#") || sourceURL.sameFile(hrefURL)) { //$NON-NLS-1$
                                continue;
                            } else if (href.startsWith(RESOURCES) || href.startsWith("./" + RESOURCES)) { //$NON-NLS-1$
                                tgtFile = new File(basePath, href);
                                tgtDir = tgtFile.getParentFile();
                            } else {
                                String resPath = getSubdirectoryOfResources(href);
                                tgtFile = new File(basePath + RESOURCES, resPath);
                                tgtDir = tgtFile;
                            }
                            tgtDir.mkdirs();
                            FileUtil.copyFile(srcFile, tgtDir);
                        }
                    } catch (Exception e) {
                        this.logger.warning(e);
                    }
                }
        
                matcher = p_image_ref.matcher(html);
                while (matcher.find()) {
                    String src = NetUtil.decodeURL(matcher.group(3));
                    try {
                        URL srcURL = new URL(sourceURL, src);
                        String scheme = srcURL.getProtocol();
                        if (scheme != null
                                && scheme.equalsIgnoreCase(NetUtil.FILE_SCHEME)) {
                            File srcFile = null;
                            String authority = srcURL.getAuthority();
                            if (authority != null) {
                                srcFile = new File(NetUtil.decodeURL(authority + srcURL.getPath()));
                            } else {
                                srcFile = new File(NetUtil.decodeURL(srcURL.getPath()));
                            }
                            File tgtFile = null;
                            File tgtDir = null;
                            if (src.startsWith(RESOURCES) || src.startsWith("./" + RESOURCES)) { //$NON-NLS-1$
                                tgtFile = new File(basePath, src);
                                tgtDir = tgtFile.getParentFile();
                            } else {
                                String resPath = getSubdirectoryOfResources(src);
                                tgtFile = new File(basePath + RESOURCES, resPath);
                                tgtDir = tgtFile;
                            }
                            tgtDir.mkdirs();
                            FileUtil.copyFile(srcFile, tgtDir);
                        }
                    } catch (Exception e) {
                        this.logger.warning(e);
                    }
                }
            }
        } catch (Exception e) {
            this.logger.error(e);
        }
    }

    /**
     * Parses the given HTML content from the clipboard and returns the source
     * URL.
     * @param htmlContent the HTML content from the clipboard
     * @return the source URL or <code>null</code>
     */
    @objid ("f12b87c9-3b0d-4bfb-a507-78e899a69b7a")
    protected String getSourceURL(String htmlContent) {
        String sourceURL = null;
        int sourceURLIndex = htmlContent.indexOf(SOURCE_URL);
        if (sourceURLIndex > 0) {
            sourceURL = htmlContent.substring(sourceURLIndex
                    + SOURCE_URL.length());
            sourceURL = sourceURL.substring(0, sourceURL
                    .indexOf(FileUtil.LINE_SEP));
            if (sourceURL.toLowerCase().endsWith(HTM_EXT)
                    || sourceURL.toLowerCase().endsWith(HTML_EXT)) {
                sourceURL = sourceURL.substring(0, sourceURL.indexOf(FileUtil
                        .getFileName(sourceURL)) - 1);
                sourceURL = sourceURL.replace('\\', '/');
            }
            sourceURL = FileUtil.appendSeparator(sourceURL, "/"); //$NON-NLS-1$
        }
        return sourceURL;
    }

    @objid ("dc261db9-ff40-490b-b22f-5544a67dd86d")
    protected String getSubdirectoryOfResources(String path) {
        String result = ""; //$NON-NLS-1$
        int res_idx = path.indexOf(RESOURCES);
        if (res_idx != -1) {
            ArrayDeque<String> stack = new ArrayDeque<>();
            File relative = new File(path).getParentFile();
            while (!relative.getName().equals(RESOURCES)) {
                stack.push(relative.getName());
                relative = relative.getParentFile();
            }
            while (!stack.isEmpty()) {
                result = result + stack.pop() + File.separator;
            }
        }
        return result;
    }

}
