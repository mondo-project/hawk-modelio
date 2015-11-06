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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.epfcommon.utils.XMLUtil;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.RichTextEditor;
import org.modelio.edition.html.view.dialogs.FindReplaceDialog;

/**
 * Finds and replaces text in a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("e18c6487-ae80-49b8-9ecd-089ca6e35a3d")
public class FindReplaceAction extends RichTextAction {
    /**
     * Finds text sub action.
     */
    @objid ("cde3ecf3-bd3b-4c4b-9dce-647bfbd9376c")
    public static final int FIND_TEXT = 1;

    /**
     * Replaces text sub action.
     */
    @objid ("2e87a62f-693b-4745-878b-15a20c20eae1")
    public static final int REPLACE_TEXT = 2;

    /**
     * Replaces and finds text sub action.
     */
    @objid ("e090cbf7-9a6c-47e0-b92e-4955b31a0b86")
    public static final int REPLACE_FIND_TEXT = 3;

    /**
     * Replaces all sub action.
     */
    @objid ("027e0757-881c-4d8c-a826-9eb09aa2620e")
    public static final int REPLACE_ALL_TEXT = 4;

    /**
     * Find match in a forward direction.
     */
    @objid ("6f07534b-15e7-44ae-b199-482fe6c98784")
    public static final int FORWARD_MATCH = 1;

    /**
     * Find match in a backward direction.
     */
    @objid ("e195436c-5162-4662-ae1c-f7ed461a7b6b")
    public static final int BACKWARD_MATCH = -1;

    /**
     * Whole word match.
     */
    @objid ("aed72bca-549c-41f4-8269-52a923ff778b")
    public static final int WHOLE_WORD_MATCH = 2;

    /**
     * Case sensitive match.
     */
    @objid ("5dcb08fa-a28b-4816-98a7-fcb6b1356dc3")
    public static final int CASE_SENSITIVE_MATCH = 4;

// Encoded single quote.
    @objid ("d5dc7769-c342-4826-af40-a211104ccaa9")
    protected static final String ENCODED_SINGLE_QUOTE = "%sq%"; // $NON-NLS-1$

    @objid ("7f2b19fc-5d7c-4922-963b-f4a0f56f3381")
    protected boolean foundMatch = false;

    @objid ("431a72b4-4e5d-4f77-9cc9-e9a45c71ab6c")
    protected IRichText richText;

    @objid ("38b1d654-980d-4a84-bf3b-95b82a783aa1")
    protected StyledText styledText;

// the dialog instance
    @objid ("2d910b4a-bcc6-4d29-8bc5-4ce4643ba57a")
    protected FindReplaceDialog dialog;

    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("85e5246b-160e-49b4-94bd-b7ca07ba470d")
    public FindReplaceAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_FIND_REPLACE);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_FIND_REPLACE);
        setToolTipText(HtmlTextResources.findReplaceAction_toolTipText);
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in readonly mode.
     */
    @objid ("881f29b0-f54b-4612-aece-1b5f90bb2580")
    @Override
    public boolean disableInReadOnlyMode() {
        return false;
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("e9b36aa7-402e-4938-8fcc-c7348eaf5ca1")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("5e6241dc-8ec8-4deb-acff-f36282ed7b2d")
    @Override
    public void execute(IRichText richText) {
        if (this.richText == null)
            this.richText = richText;
        if (this.richText != null) {
            try {
                if (this.dialog != null) {
                    this.dialog.setFindOnly(!this.richText.getEditable());
                    this.dialog.open();
                } else {
                    this.dialog = new FindReplaceDialog(Display
                            .getCurrent().getActiveShell(), this, !this.richText
                            .getEditable());
                    this.dialog.open();
                }
            } catch (Exception e) {
                HtmlTextPlugin.getDefault().getLogger().error(e);
            }
        }
    }

    /**
     * Returns <code>true</code> if a match is found.
     * @return <code>true</code> if a match is found.
     */
    @objid ("32996e25-bc1c-4fdc-90c4-b47dbe58b3b9")
    public boolean getFoundMatch() {
        return this.foundMatch;
    }

    /**
     * Executes the action.
     * @param subAction the sub action to execute
     * @param findText the find text
     * @param replaceText the replace text
     * @param matchDir the match direction; the value can either be
     * <code>FIND_FORWARD</code> or <code>FIND_BACKWARD</code>.
     * @param matchOptions the match options
     */
    @objid ("9e35f24c-6508-418c-b1f3-9c10533f3e78")
    public void run(int subAction, String findText, String replaceText, int matchDir, int matchOptions) {
        this.styledText = null;
        if (this.richText instanceof RichTextEditor
                && ((RichTextEditor) this.richText).isHTMLTabSelected()) {
            this.styledText = ((RichTextEditor) this.richText).getSourceEdit();
        }
        if (this.styledText == null) {
            if (findText.indexOf("'") != -1) { //$NON-NLS-1$
                findText = findText.replaceAll("'", ENCODED_SINGLE_QUOTE); //$NON-NLS-1$
            }
            if (replaceText.indexOf("'") != -1) { //$NON-NLS-1$
                replaceText = replaceText.replaceAll("'", ENCODED_SINGLE_QUOTE); //$NON-NLS-1$
            }
        }
        try {
            this.foundMatch = false;
            int status = 0;
            switch (subAction) {
            case FIND_TEXT:
                status = findText(findText, matchDir, matchOptions);
                break;
            case REPLACE_TEXT:
                status = replaceText(replaceText, matchDir, matchOptions);
                break;
            case REPLACE_FIND_TEXT:
                status = replaceFindText(findText, replaceText, matchDir,
                        matchOptions);
                break;
            case REPLACE_ALL_TEXT:
                replaceAll(findText, replaceText, matchOptions);
                break;
            default:
                throw new IllegalArgumentException("subAction="+subAction);
            }
            if (status > 0)
                this.foundMatch = true;
        } catch (Exception e) {
            HtmlTextPlugin.LOG.error(e);
        }
    }

    /**
     * Escapes the given text.
     * @param text text to be escaped
     */
    @objid ("b217a60c-a427-4eb0-858e-f70f00ec9f51")
    protected static String escape(String text) {
        if (text == null || text.length() == 0)
            return ""; //$NON-NLS-1$
        StringBuffer sb = new StringBuffer();
        int textSize = text.length();
        for (int i = 0; i < textSize; i++) {
            char ch = text.charAt(i);
            switch (ch) {
            case '<':
                sb.append(XMLUtil.XML_LT);
                break;
            case '>':
                sb.append(XMLUtil.XML_GT);
                break;
            case '&':
                sb.append(XMLUtil.XML_AMP);
                break;
            default:
                sb.append(ch);
                break;
            }
        }
        return sb.toString();
    }

    @objid ("281b0d0a-cd30-4392-a9c7-2842348e50b4")
    protected int findText(String findText, int matchDir, int matchOptions) {
        int status = 0;
        if (this.styledText != null) {
            status = styledTextFindTextAndSelect(findText, matchDir,
                    matchOptions);
        } else {
            status = this.richText
                    .executeCommand(IRichTextCommands.FIND_TEXT, new String[] {
                            findText, "" + matchDir, "" + matchOptions }); //$NON-NLS-1$ //$NON-NLS-2$                
        }
        return status;
    }

    @objid ("5e31497b-71bb-4168-900b-a3f4a0583a9f")
    protected int replaceText(String replaceText, int matchDir, int matchOptions) {
        int status = 0;
        if (this.styledText != null) {
            status = styledTextReplaceTextAndSelect(replaceText);
        } else {
            status = this.richText.executeCommand(IRichTextCommands.REPLACE_TEXT,
                    new String[] { replaceText,
                            "" + matchDir, "" + matchOptions }); //$NON-NLS-1$ //$NON-NLS-2$                    
        }
        return status;
    }

    @objid ("27c9fd4a-eb84-45af-8b94-eadfb5ee0cb4")
    protected int replaceFindText(String findText, String replaceText, int matchDir, int matchOptions) {
        int status = 0;
        if (this.styledText != null) {
            styledTextReplaceTextAndSelect(replaceText);
            status = styledTextFindTextAndSelect(findText, matchDir,
                    matchOptions);
        } else {
            this.richText.executeCommand(IRichTextCommands.REPLACE_TEXT, new String[] {
                    replaceText, "" + matchDir, "" + matchOptions }); //$NON-NLS-1$ //$NON-NLS-2$
            status = this.richText
                    .executeCommand(IRichTextCommands.FIND_TEXT, new String[] {
                            findText, "" + matchDir, "" + matchOptions }); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return status;
    }

    @objid ("4e032cc7-ab00-4eab-bb9e-8a97e64a9ead")
    protected void replaceAll(String findText, String replaceText, int matchOptions) {
        if (this.styledText != null) {
            styledTextReplaceAll(findText, replaceText, matchOptions);
        } else {
            this.richText.executeCommand(IRichTextCommands.REPLACE_ALL_TEXT,
                    new String[] { escape(findText), escape(replaceText),
                            "" + matchOptions }); //$NON-NLS-1$
        }
    }

    @objid ("53d5b171-68d8-4a15-ad63-fbdbed74d4ae")
    protected int styledTextFindTextAndSelect(String findText, int matchDir, int matchOptions) {
        Point selectionOffset = this.styledText.getSelectionRange();
        int firstSelectedOffset = selectionOffset.x;
        int lastSelectedOffset = selectionOffset.x + selectionOffset.y - 1;
        String htmlText = this.styledText.getText();
        int indexOfMatch = -1;
        if ((matchOptions & CASE_SENSITIVE_MATCH) == 0) {
            // TODO: use toUpperCase(Locale) once library has locale attribute
            htmlText = htmlText.toUpperCase();
            findText = findText.toUpperCase();
        }
        do {
            if (indexOfMatch != -1) {
                lastSelectedOffset = indexOfMatch + 1;
                firstSelectedOffset = indexOfMatch - 1;
            }
            if (matchDir == FORWARD_MATCH) {
                indexOfMatch = htmlText.indexOf(findText,
                        lastSelectedOffset + 1);
            } else {
                indexOfMatch = htmlText.lastIndexOf(findText,
                        firstSelectedOffset - 1);
            }
        } while (indexOfMatch != -1
                && ((matchOptions & WHOLE_WORD_MATCH) == WHOLE_WORD_MATCH)
                && isPartOfWord(htmlText, indexOfMatch, findText.length()));
        if (indexOfMatch != -1) {
            this.styledText.setSelectionRange(indexOfMatch, findText.length());
            this.styledText.showSelection();
        } else {
            String selectedText = this.styledText.getSelectionText();
            if ((matchOptions & CASE_SENSITIVE_MATCH) == 0) {
                selectedText = selectedText.toUpperCase();
            }
            if (selectedText.equals(findText)) {
                indexOfMatch = this.styledText.getSelectionRange().x;
            }
        }
        return indexOfMatch;
    }

    @objid ("11c610da-f598-495f-ba74-df6ae79cbb42")
    protected int styledTextReplaceTextAndSelect(String replaceText) {
        Point selectionOffset = this.styledText.getSelectionRange();
        this.styledText.replaceTextRange(selectionOffset.x, selectionOffset.y,
                replaceText);
        this.styledText.setSelectionRange(selectionOffset.x, replaceText.length());
        return 1;
    }

    @objid ("46eab359-2441-4b8e-89ec-518573da411e")
    protected void styledTextReplaceAll(String findText, String replaceText, int matchOptions) {
        this.styledText.setSelectionRange(0, 0);
        while (styledTextFindTextAndSelect(findText, FORWARD_MATCH,
                matchOptions) != -1) {
            styledTextReplaceTextAndSelect(replaceText);
        }
    }

    @objid ("e3116441-6650-4768-9bc9-f65a07578892")
    protected boolean isWordChar(char c) {
        if (Character.isLetterOrDigit(c))
            return true;
        return false;
    }

    @objid ("443bcc14-9f4d-478c-9e84-fa6902ac2bdb")
    protected boolean isPartOfWord(String text, int index, int length) {
        if (index > 0)
            if (isWordChar(text.charAt(index - 1)))
                return true;
        if (text.length() >= index + length)
            if (isWordChar(text.charAt(index + length)))
                return true;
        return false;
    }

    @objid ("b778fe1e-18b4-4bcc-b455-5e6fa7a530a9")
    public IRichText getRichText() {
        return this.richText;
    }

    @objid ("3c9d2d46-e8c0-4e3d-8eed-c3b18c36907c")
    public void setRichText(IRichText richText) {
        this.richText = richText;
    }

    @objid ("0a2fc27f-420d-43bf-8f67-d6a511c2dee9")
    public void dispose() {
        if (this.dialog != null) {
            this.dialog.close();
            this.dialog = null;
        }
    }

}
