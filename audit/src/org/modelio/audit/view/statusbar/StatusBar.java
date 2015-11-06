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
                                    

package org.modelio.audit.view.statusbar;

import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.modelio.audit.engine.core.AuditRunnerStatus;
import org.modelio.audit.engine.core.IAuditDiagnostic;
import org.modelio.audit.plugin.Audit;

/**
 * StatusBar controller.
 * <p>
 * Merges in one class the status bar SWT component and its controller.
 * <p>
 * TODO : Images are not disposed with the status bar.
 */
@objid ("a68875a0-fb29-41bb-b23f-a5f67438eba9")
public class StatusBar {
    @objid ("f084b7d8-0df4-417e-9f36-6476c9b0e7f8")
    private String processingStatusTooltip;

    @objid ("4f6daa1f-992b-4868-be37-df91abca00f1")
    private String suspendedStatusTooltip;

    @objid ("3fddfbf1-ca91-4d9b-87b7-53d9a48e6eaa")
    private String idleStatusTooltip;

    @objid ("bdcf8cba-d172-4193-a690-b2ec3481ce9e")
    private AuditRunnerStatus lastRefreshedstatus = null;

    @objid ("ba0a9fb8-9f69-41f0-a235-3b6f9546458a")
    private int lastRefreshedCount;

    @objid ("b2145247-16ea-4328-ab04-f5d6bcca659a")
    private String nErrorsSuffix;

    @objid ("221536af-64c6-4127-9ec7-6f699f8dd704")
    private String nWarningsSuffix;

    @objid ("60711bf5-9dbe-48df-ba6c-5a27e4b96339")
    private String nAdvicesSuffix;

    @objid ("4d79dca0-6847-408a-858b-aa1b124588ac")
    private final Label queueSizeLabel;

    @objid ("b0ae407c-0b2e-440d-a042-d1d890b15ebd")
    private final Label runnerStatusLabel;

    @objid ("f160ead6-f563-4ac0-b384-1f9e21ebd978")
    private Image processingStatusIcon;

    @objid ("0d541b96-eec4-483e-9e38-177982da243b")
    private Image suspendedStatusIcon;

    @objid ("bd428326-221a-4a19-9b04-0126ed75a4bc")
    private Image idleStatusIcon;

    @objid ("9b3c8307-7fba-4849-ab0d-6fea7a3c7c7a")
    private final Composite container;

    @objid ("09107f7c-64a1-478f-a360-931cd0d17016")
    private final Label auditSummaryLabel;

    @objid ("56cdbed4-19d9-4718-9b9c-6c65273a46e7")
    private StringBuilder summaryMessage = new StringBuilder();

    @objid ("cfa7443f-7f0d-4f3e-8e1b-7c7fed168602")
    private Color errorColor;

    @objid ("12a7fd53-5d8c-4833-b06e-1fe87d6ae43c")
    private Color warningColor;

    @objid ("2c8f5237-965e-4d17-b30f-a0f8a9aaa8b7")
    private Color adviceColor;

    /**
     * Initialize the status bar.
     * @param parent The parent composite.
     * @param style not used.
     */
    @objid ("860b222d-5fd2-4138-bd16-d12002c5d836")
    public StatusBar(Composite parent, int style) {
        allocateResources();
        
        this.container = new Composite(parent, SWT.NONE);
        
        final GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.horizontalSpacing = 0;
        this.container.setLayout(layout);
        
        this.auditSummaryLabel = new Label(this.container, SWT.BORDER );
        this.auditSummaryLabel.setText("...");
        GridData rd = new GridData();
        rd.horizontalAlignment = SWT.FILL;
        rd.grabExcessHorizontalSpace = true;
        this.auditSummaryLabel.setAlignment(SWT.CENTER);
        this.auditSummaryLabel.setLayoutData(rd);
        
        // Queue size label
        this.queueSizeLabel = new Label(this.container, SWT.BORDER | SWT.RIGHT_TO_LEFT);
        this.queueSizeLabel.setText("0");
        rd = new GridData(80, SWT.DEFAULT);
        this.queueSizeLabel.setLayoutData(rd);
        
        // Runner status 'LED-like' display
        this.runnerStatusLabel = new Label(this.container, SWT.NONE);
        this.runnerStatusLabel.setImage(this.idleStatusIcon);
        rd = new GridData();
        rd.horizontalAlignment = SWT.CENTER;
        this.runnerStatusLabel.setLayoutData(rd);
        
        // TODO : This method shouldn't have to be called here:
        // Adding this status bar as listener should fire its status(...) method.
        // updateFromRunnerStatus(Audit.getInstance().auditEngine.getRunningStatus(), 0);
        doRefreshStatus(AuditRunnerStatus.IDLE, 0);
    }

    /**
     * Get the status bar SWT Composite.
     * <p>
     * The returned Composite is a child of the composite given to the constructor.
     * @return the status bar composite.
     */
    @objid ("024af934-42cc-49e8-b83a-422f58a36662")
    public Composite getComposite() {
        return this.container;
    }

    /**
     * Update the status bar from last audit diagnostic. MUST BE CALLED from the UI Thread
     * @param auditDiagnostic the last audit diagnostic.
     */
    @objid ("8c4a37ba-abe8-4a2f-ac2f-cdd90b385018")
    public void doRefreshDiagnostic(final IAuditDiagnostic auditDiagnostic) {
        int nErrors = auditDiagnostic.getErrorCount();
        int nWarnings = auditDiagnostic.getWarningCount();
        int nTips = auditDiagnostic.getTipCount();
        
        if ((nErrors + nWarnings + nTips) != this.lastRefreshedCount) {
            this.summaryMessage.setLength(0);
            this.summaryMessage.append(auditDiagnostic.getErrorCount());
            this.summaryMessage.append(this.nErrorsSuffix);
            this.summaryMessage.append(auditDiagnostic.getWarningCount());
            this.summaryMessage.append(this.nWarningsSuffix);
            this.summaryMessage.append(auditDiagnostic.getTipCount());
            this.summaryMessage.append(this.nAdvicesSuffix);
        
            this.auditSummaryLabel.setText(this.summaryMessage.toString());
        
            if (nErrors > 0) {
                this.auditSummaryLabel.setForeground(this.errorColor);
            } else if (nWarnings > 0) {
                this.auditSummaryLabel.setForeground(this.warningColor);
            } else {
                this.auditSummaryLabel.setForeground(this.adviceColor);
            }
            this.lastRefreshedCount = nErrors + nWarnings + nTips;
        }
    }

    /**
     * Update the status view from the given parameters. MUST BE CALLED from the UI Thread
     * @param status The audit status
     * @param queueSize The queue size
     */
    @objid ("f3b2425b-7220-431c-9cb0-ad1715864e5d")
    public void doRefreshStatus(final AuditRunnerStatus status, final int queueSize) {
        this.queueSizeLabel.setText(Integer.toString(queueSize));
        
        if (status != this.lastRefreshedstatus) {
            switch (status) {
            case IDLE:
                this.runnerStatusLabel.setImage(this.idleStatusIcon);
                this.runnerStatusLabel.setToolTipText(this.idleStatusTooltip);
                break;
            case PROCESSING:
                this.runnerStatusLabel.setImage(this.processingStatusIcon);
                this.runnerStatusLabel.setToolTipText(this.processingStatusTooltip);
                break;
            case SUSPENDED:
                this.runnerStatusLabel.setImage(this.suspendedStatusIcon);
                this.runnerStatusLabel.setToolTipText(this.suspendedStatusTooltip);
                break;
            case TERMINATED:
                this.runnerStatusLabel.setImage(null);
                this.runnerStatusLabel.setToolTipText(Audit.I18N.getString("Audit.Status.terminated"));
                break;
            default:
                break;
            }
        
            this.lastRefreshedstatus = status;
        }
    }

    @objid ("1ed3255c-1647-4be2-9b9c-9749a0d96813")
    private void allocateResources() {
        this.processingStatusIcon = createImageDescriptor("icons/processing.png").createImage();
        this.processingStatusTooltip = Audit.I18N.getString("Audit.Status.processing");
        this.suspendedStatusIcon = createImageDescriptor("icons/suspended.png").createImage();
        this.suspendedStatusTooltip = Audit.I18N.getString("Audit.Status.suspended");
        this.idleStatusIcon = createImageDescriptor("icons/idle.png").createImage();
        this.idleStatusTooltip = Audit.I18N.getString("Audit.Status.terminated");
        
        this.nErrorsSuffix = " " + Audit.I18N.getMessage("Audit.Status.NbErrorsSuffix") + ", ";
        this.nWarningsSuffix = " " + Audit.I18N.getMessage("Audit.Status.NbWarningsSuffix") + ", ";
        this.nAdvicesSuffix = " " + Audit.I18N.getMessage("Audit.Status.NbAdvicesSuffix");
        
        this.errorColor = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
        this.warningColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
        this.adviceColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
    }

    /**
     * Create an image descriptor from a path relative to the Audit plugin.
     * @param path a relative path.
     * @return the image descriptor.
     */
    @objid ("7d3f3c6e-5867-45d4-aad1-5ea7ba3047b0")
    private ImageDescriptor createImageDescriptor(final String path) {
        URL bitmapUrl = FileLocator.find(Platform.getBundle(Audit.PLUGIN_ID), new Path(path), null);
        return ImageDescriptor.createFromURL(bitmapUrl);
    }

}
