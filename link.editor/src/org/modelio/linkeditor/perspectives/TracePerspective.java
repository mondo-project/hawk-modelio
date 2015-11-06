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
                                    

package org.modelio.linkeditor.perspectives;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

@objid ("1b535dcc-5e33-11e2-b81d-002564c97630")
public class TracePerspective implements IPerspectiveFactory {
    @objid ("1b535dcd-5e33-11e2-b81d-002564c97630")
    private static final String Trace1 = "TracePerspective.1";

    @objid ("1b535dcf-5e33-11e2-b81d-002564c97630")
    private static final String Trace2 = "TracePerspective.2";

    @objid ("1b535dd1-5e33-11e2-b81d-002564c97630")
    private static final String Trace3 = "TracePerspective.3";

    @objid ("1b535dd3-5e33-11e2-b81d-002564c97630")
    private static final String Trace4 = "TracePerspective.4";

    @objid ("1b535dd5-5e33-11e2-b81d-002564c97630")
    private static final String Trace5 = "TracePerspective.5";

    @objid ("1b535dd7-5e33-11e2-b81d-002564c97630")
    @Override
    public void createInitialLayout(final IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        
        IFolderLayout scope1 = layout.createFolder(Trace1, IPageLayout.LEFT, 0f, IPageLayout.ID_EDITOR_AREA);
        scope1.addView("org.modelio.linkeditor.view.LinkEditorViewID");        
        
        IFolderLayout scope2 = layout.createFolder(Trace2, IPageLayout.LEFT, 1f/5f, Trace1);
        scope2.addView("com.modeliosoft.modelio.explorer.UmlNavigatorContentID");
        
        IFolderLayout scope3 = layout.createFolder(Trace3, IPageLayout.RIGHT, 3f/4f, Trace1);
        // Check if the scope plugin is deployed to open its view 
        if (Platform.getBundle("com.modeliosoft.modelio.scope") != null) {
            scope3.addView("com.modeliosoft.modelio.scope.ScopeNavigatorContentID");
        }
        
        scope3.addPlaceholder("com.modeliosoft.modelio.mda.MdaExplorerContentID");
        scope3.addPlaceholder("com.modeliosoft.modelio.diagram.browser.BrowserViewID");
        
        IFolderLayout scope4 = layout.createFolder(Trace4, IPageLayout.BOTTOM, 4f/5f, Trace1);
        scope4.addView("com.modeliosoft.modelio.edition.notes.NotesViewID");        
        scope4.addPlaceholder("com.modeliosoft.modelio.diagram.symbol.SymbolViewID");
        scope4.addPlaceholder("org.eclipse.ui.views.ContentOutline");
        
        IFolderLayout scope5 = layout.createFolder(Trace5, IPageLayout.RIGHT, 1f/2f, Trace4);
        scope5.addView("com.modeliosoft.modelio.edition.ModelPropertyViewID");
        scope5.addPlaceholder("com.modeliosoft.modelio.edition.MdacPropertyViewID:*");
        scope5.addPlaceholder("com.modeliosoft.modelio.audit.AuditViewID");
    }

}
