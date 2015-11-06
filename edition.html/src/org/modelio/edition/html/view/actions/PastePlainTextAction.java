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
                                    

package org.modelio.edition.html.view.actions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.epfcommon.utils.StrUtil;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.RichTextEditor;

/**
 * Paste as plain text.
 */
@objid ("38149e1f-081b-467b-b82c-e7b2418295ba")
public class PastePlainTextAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("d2355fbf-9669-4870-a66c-87599903a857")
    public PastePlainTextAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_PASTE_PLAIN_TEXT);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_PASTE_PLAIN_TEXT);
        setToolTipText(HtmlTextResources.pastePlainTextAction_toolTipText);
        setEnabled(true);
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("6639b6a5-5185-4858-a273-16b200c305df")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

    @objid ("7ff5c625-069e-49e4-9f09-d52fbc5fd3ae")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            // get text from clipboard
            Clipboard clipboard = new Clipboard(Display.getCurrent());
            String text = (String) clipboard.getContents(TextTransfer
                    .getInstance());
            if (text != null && text.length() > 0) {
                text = StrUtil.convertNewlinesToHTML(text);
                if (richText instanceof RichTextEditor) {
                    ((RichTextEditor)richText).addHTML(text);
                } else {
                    richText.executeCommand(IRichTextCommands.ADD_HTML, text);
                }
            }
        }
    }

}
