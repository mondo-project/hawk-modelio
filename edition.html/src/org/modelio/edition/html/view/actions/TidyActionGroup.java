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
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;

/**
 * Runs JTidy on the HTML
 * 
 * @author Jeff Hardy
 * @since 1.2
 */
@objid ("c1728878-9a43-4fe0-ad0e-3d333b50ad71")
public class TidyActionGroup extends RichTextAction {
    @objid ("60283360-0774-4d3b-bd97-1eb92f9b946e")
     TidyAction tidyActionDefault;

    @objid ("f651c878-4f4c-44d0-80d7-830259c94b9f")
     TidyAction tidyActionCleanMS;

    @objid ("d661913e-2621-47ee-a980-51846c944164")
     TidyAction tidyActionCleanWord2000;

    /**
     * @param richText the rich text
     */
    @objid ("26b72b54-5bd9-4e75-bcdd-73d87f207102")
    public TidyActionGroup(IRichText richText) {
        super(richText, IAction.AS_DROP_DOWN_MENU);
        
        setImageDescriptor(HtmlTextImages.IMG_DESC_TIDY);
        //        setDisabledImageDescriptor(RichTextImages.DISABLED_IMG_DESC_PASTE);
        setToolTipText(HtmlTextResources.tidy_clean_toolTipText);
        setText(HtmlTextResources.tidy_clean_text);
        
        createActions();
        setMenuCreator(this.menuCreator);
    }

    @objid ("ec4daf01-4448-4617-b95e-dd963765cfc6")
    private void createActions() {
        this.tidyActionDefault = new TidyAction(this.mRichText, true, false, false);
        this.tidyActionDefault.setText(HtmlTextResources.tidy_clean_text);
        this.tidyActionDefault.setToolTipText(HtmlTextResources.tidy_clean_toolTipText);
        
        this.tidyActionCleanMS = new TidyAction(this.mRichText, true, true, false);
        this.tidyActionCleanMS.setText(HtmlTextResources.tidy_cleaner_text);
        this.tidyActionCleanMS.setToolTipText(HtmlTextResources.tidy_cleaner_toolTipText);
        
        this.tidyActionCleanWord2000 = new TidyAction(this.mRichText, true, true, true);
        this.tidyActionCleanWord2000.setText(HtmlTextResources.tidy_cleanest_text);
        this.tidyActionCleanWord2000.setToolTipText(HtmlTextResources.tidy_cleanest_toolTipText);
        addActionToMenu(this.tidyActionDefault);
        addActionToMenu(this.tidyActionCleanMS);
        addActionToMenu(this.tidyActionCleanWord2000);
    }

    @objid ("c277148d-cb52-481f-ac1e-d27739bdd6d5")
    @Override
    public void execute(IRichText richText) {
        // TODO Auto-generated method stub
        
        // call normal clean
        this.tidyActionDefault.execute(richText);
    }

    @objid ("bb0503da-dcea-4527-820f-924476624014")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

}
