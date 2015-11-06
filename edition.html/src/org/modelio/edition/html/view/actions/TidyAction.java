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

import java.io.UnsupportedEncodingException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.modelio.edition.html.epfcommon.html.DefaultHTMLFormatter;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.RichTextEditor;

/**
 * Runs JTidy on the HTML
 * 
 * @author Jeff Hardy
 * @since 1.2
 */
@objid ("0d84dc58-4fe9-4589-bca3-60dcd82854ce")
public class TidyAction extends RichTextAction {
    /**
     * The HTML source formatter.
     */
    @objid ("62f95d14-55df-408b-8c95-b4457cc0a872")
    protected DefaultHTMLFormatter htmlFormatter;

    /**
     * Creates a new instance.
     * @param richText the rich text
     * @param forceOutput <i>true</i> to force Tidy to output something event if HTML is not valid.
     * @param makeBare <i>true</i> to strip MS Word tags
     * @param word2000 <i>true</i> to strip Word 2000 tags
     */
    @objid ("0e81f84a-2ebb-47d6-9f99-6eba9bcd4297")
    public TidyAction(IRichText richText, boolean forceOutput, boolean makeBare, boolean word2000) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_TIDY);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_TIDY);
        
        if (this.htmlFormatter == null) {
            this.htmlFormatter = new DefaultHTMLFormatter();
            this.htmlFormatter.setForceOutput(forceOutput);
            this.htmlFormatter.setIndent(true);
            this.htmlFormatter.setMakeBare(makeBare);
            this.htmlFormatter.setWord2000(word2000);
        }
    }

    @objid ("9cc42d1f-415d-410c-8dda-2c0d66d1899b")
    @Override
    public void execute(IRichText richText) {
        // get current text
        String html = richText.getText();
        
        try {
            html = this.htmlFormatter.formatHTML(html);
        } catch (UnsupportedEncodingException e) {
            HtmlTextPlugin.LOG.warning(e);
        }
        
        // set text
        richText.setText(html);
        richText.checkModify();
    }

    @objid ("29090c14-5253-4dc2-be1f-9dbe27368cac")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

}
