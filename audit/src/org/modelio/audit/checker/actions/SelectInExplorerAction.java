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
                                    

package org.modelio.audit.checker.actions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.audit.engine.core.IAuditEntry;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.view.model.AuditElementModel;
import org.modelio.audit.view.model.AuditRuleModel;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("dba3fb61-f279-4762-9a73-7c3b424a441b")
public class SelectInExplorerAction extends Action {
    @objid ("589704b2-d626-4c7a-8453-f7ce00a4d2fc")
    private TreeViewer tree;

    @objid ("d50c56fa-68a0-4bdf-818b-b7ca1d2dd2b6")
    private IModelioNavigationService navigationService;

    @objid ("56f082dd-d319-48b7-a644-5f17d8ffc582")
    public SelectInExplorerAction(IModelioNavigationService navigationService, TreeViewer tree) {
        this.tree = tree;
        this.navigationService = navigationService;        
        this.setText(Audit.I18N.getString("Audit.CheckerView.Contextual.Select"));
        setImageDescriptor(Audit.getImageDescriptor("icons/select.png"));
    }

    @objid ("311ea849-b554-4e21-97fa-4d565a3f239e")
    @Override
    public void run() {
        TreeItem[] item = this.tree.getTree().getSelection();
        Object obj = item[0].getData();
        MObject element = null;
        if (obj instanceof IAuditEntry) {
            element = ((IAuditEntry) obj).getElement();
        } else if (obj instanceof AuditRuleModel) {
            element = ((AuditElementModel) obj).element;
        }
        
        if (element != null) {
            navigationService.fireNavigate(element);
        }
    }

    @objid ("c843b139-b3b4-45f0-b152-2da7f03e0a36")
    @Override
    public boolean isEnabled() {
        TreeItem[] item = this.tree.getTree().getSelection();
        Object obj = (Object) item[0].getData();
        return  obj instanceof IAuditEntry || obj instanceof AuditElementModel;
    }

}
