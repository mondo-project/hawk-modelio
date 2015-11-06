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
                                    

package org.modelio.audit.checker;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.audit.engine.core.AuditRunnerStatus;
import org.modelio.audit.engine.core.IAuditMonitor;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.service.IAuditService;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("37082079-8112-4acb-ac8e-7daa6c603af8")
public class CheckElementRunner implements Runnable {
    @objid ("c33453f5-bc1a-4214-95e5-f863a079caab")
    private String jobId;

    @objid ("4003bb3f-78d8-4ba2-9a9e-1879e0938ced")
    private String status;

    @objid ("c2cbede5-ce65-4fd2-8aa0-c797163dff52")
    private int counter = 0;

    @objid ("4dfe4e75-b8da-44cc-9bf0-e436e1bbb473")
    private List<MObject> selectedElements;

    @objid ("b27728e5-5cc1-46ca-999c-1522713b4930")
    private CheckerView checkerView;

    @objid ("7e53fe70-13db-41d6-bfec-b74e9e604e9c")
    private IAuditService auditService;

    @objid ("474c30b6-2255-4455-a6ec-f7a043135244")
    public CheckElementRunner(IAuditService auditService, Object selection, String jobId) {
        this.selectedElements = new ArrayList<>();
        this.auditService = auditService;
        this.jobId = jobId;
        
        if (selection instanceof MObject) {
            this.selectedElements.add((MObject) selection);
        } else if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() >= 1) {
            Object[] elements = ((IStructuredSelection) selection).toArray();
            for (Object element : elements) {
                if (element instanceof MObject) {
                    this.selectedElements.add((MObject) element);
                } else if (element instanceof IAdaptable) {
                    final MObject adapter = (MObject) ((IAdaptable) element).getAdapter(MObject.class);
                    if (adapter != null) {
                        this.selectedElements.add(adapter);
                    }
                }
            }
        }
    }

    @objid ("86f5739b-1ebf-45c5-aa59-a4a5b549ff6e")
    @Override
    public void run() {
        this.status = Audit.I18N.getMessage("Audit.Checker.Status.PostingChecks", " ");
        this.checkerView.refresh();
        
        List<MObject> elements = new ArrayList<>();
        for (MObject e : this.selectedElements) {
            if (e.isValid() && !e.getStatus().isRamc())
                collectElements(elements, e);
        }
        // now post checks to the audit system
        this.status = Audit.I18N.getString("Audit.Checker.Status.RunningChecks");
        this.checkerView.refresh();
        postChecksToAuditEngine(elements);
        
        while (this.auditService.getAuditEngine().getAuditRunner().status == AuditRunnerStatus.PROCESSING) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                ; // ignore
            }
        }
        this.status = "";
        this.checkerView.refresh();
    }

    @objid ("d6cbc385-b5a5-4174-9ea5-692beef499cc")
    private void collectElements(List<MObject> collectedElements, MObject element) {
        this.status = Audit.I18N.getMessage("Audit.Checker.Status.PostingChecks", Integer.toString(++this.counter));
        // this.checkedElement = element;
        this.checkerView.refresh();
        // this.auditService.checkElement(element, this.jobId);
        collectedElements.add(element);
        for (MObject child : element.getCompositionChildren()) {
            collectElements(collectedElements, child);
        }
        if (collectedElements.size() > 10000)
            postChecksToAuditEngine(collectedElements);
    }

    @objid ("db5cd80c-bb03-49b4-8026-44f6e6048d57")
    public String getStatus() {
        return this.status;
    }

    @objid ("1ab41864-0cf4-4e6f-a3ac-97e97c424939")
    public void setView(CheckerView checkerView) {
        this.checkerView = checkerView;
    }

    @objid ("4d65cc95-9617-4993-93d6-90aa91a5cb47")
    private void postChecksToAuditEngine(List<MObject> elements) {
        for (MObject e : elements)
            this.auditService.checkElement(e, this.jobId);
        elements.clear();
    }

}
