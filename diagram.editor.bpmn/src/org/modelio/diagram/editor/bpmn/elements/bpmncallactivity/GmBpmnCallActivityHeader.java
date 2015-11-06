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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmncallactivity;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.editor.bpmn.editor.BpmnSharedImages;
import org.modelio.diagram.editor.bpmn.elements.bpmnnodeheader.GmBpmnNodeHeader;
import org.modelio.diagram.editor.bpmn.plugin.DiagramEditorBpmn;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.metamodel.bpmn.activities.BpmnBusinessRuleTask;
import org.modelio.metamodel.bpmn.activities.BpmnCallActivity;
import org.modelio.metamodel.bpmn.activities.BpmnManualTask;
import org.modelio.metamodel.bpmn.activities.BpmnReceiveTask;
import org.modelio.metamodel.bpmn.activities.BpmnScriptTask;
import org.modelio.metamodel.bpmn.activities.BpmnSendTask;
import org.modelio.metamodel.bpmn.activities.BpmnServiceTask;
import org.modelio.metamodel.bpmn.activities.BpmnTask;
import org.modelio.metamodel.bpmn.activities.BpmnUserTask;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents the classifier header.
 * <p>
 * Contains for the moment the class name.<br>
 * Will contain in the future:<br>
 * - its visibility <br>
 * - tagged values <br>
 * - &lt;&lt;stereotypes names>> <br>
 * - a stereotype icons bar <br>
 */
@objid ("609a5714-55b6-11e2-877f-002564c97630")
public class GmBpmnCallActivityHeader extends GmBpmnNodeHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("609a5716-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("609a5719-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("609a571b-55b6-11e2-877f-002564c97630")
    public GmBpmnCallActivityHeader(final GmAbstractDiagram diagram, final MRef relatedRef, final boolean showIcone) {
        super(diagram, relatedRef, showIcone);
    }

    @objid ("609a5727-55b6-11e2-877f-002564c97630")
    @Override
    protected String computeMainLabel() {
        String mlabel = null;
        String reference = null;
        
        if (getRelatedElement() != null && !getRelatedElement().getName().equals("")) {
            mlabel = getRelatedElement().getName();
        }
        
        BpmnCallActivity element = (BpmnCallActivity) getRelatedElement();
        if (element.getCalledBehavior() != null) {
            reference = element.getCalledBehavior().getName();
        } else if (element.getCalledGlobalTask() != null) {
            reference = element.getCalledGlobalTask().getName();
        } else if (element.getCalledOperation() != null) {
            reference = element.getCalledOperation().getName();
        } else if (element.getCalledProcess() != null) {
            reference = element.getCalledProcess().getName();
        }
        
        StringBuilder s = new StringBuilder();
        
        String basename = getDiagram().getModelManager().getModelServices().getElementNamer().getBaseName(element.getMClass());
        if (mlabel != null && !mlabel.equals("")) {
            if (!mlabel.startsWith(basename) || reference == null) {
                s.append(mlabel);
        
                if (reference != null) {
                    s.append(":");
                }
            }
        
        }
        if (reference != null)
            s.append(reference);
        return s.toString();
    }

    @objid ("609a572c-55b6-11e2-877f-002564c97630")
    public List<Image> getRepresentedIcon() {
        List<Image> res = new ArrayList<>();
        Boolean showrepresented = getStyle().getProperty(GmBpmnCallActivityStructuredStyleKeys.SHOWREPRESENTED);
        BpmnCallActivity element = (BpmnCallActivity) getRelatedElement();
        if (showrepresented) {
        
            if (element.getCalledBehavior() != null) {
                res.add(ElementImageService.getIcon(element.getCalledBehavior()));
            } else if (element.getCalledGlobalTask() != null) {
                res.add(ElementImageService.getIcon(element.getCalledGlobalTask()));
            } else if (element.getCalledOperation() != null) {
                res.add(ElementImageService.getIcon(element.getCalledOperation()));
            } else if (element.getCalledProcess() != null) {
                res.add(ElementImageService.getIcon(element.getCalledProcess()));
            }
        } else {
        
            if (element.getCalledBehavior() != null) {
                res.add(ElementImageService.getIcon(element.getCalledBehavior()));
            } else if (element.getCalledGlobalTask() != null) {
                BpmnTask task = element.getCalledGlobalTask();
                if (task instanceof BpmnSendTask) {
                    res.add(DiagramEditorBpmn.getImageRegistry()
                            .getImage(BpmnSharedImages.SENDTASKHEADER));
                } else if (task instanceof BpmnReceiveTask) {
                    res.add(DiagramEditorBpmn
                            .getImageRegistry()
                            .getImage(BpmnSharedImages.RECEIVETASKHEADER));
                } else if (task instanceof BpmnServiceTask) {
                    res.add(DiagramEditorBpmn
                            .getImageRegistry()
                            .getImage(BpmnSharedImages.SERVICETASKHEADER));
                } else if (task instanceof BpmnUserTask) {
                    res.add(DiagramEditorBpmn
                            .getImageRegistry()
                            .getImage(BpmnSharedImages.USERTASKHEADER));
                } else if (task instanceof BpmnManualTask) {
                    res.add(DiagramEditorBpmn
                            .getImageRegistry()
                            .getImage(BpmnSharedImages.MANUALTASKHEADER));
                } else if (task instanceof BpmnScriptTask) {
                    res.add(DiagramEditorBpmn
                            .getImageRegistry()
                            .getImage(BpmnSharedImages.SCRIPTTASKHEADER));
                } else if (task instanceof BpmnBusinessRuleTask) {
                    res.add(DiagramEditorBpmn
                            .getImageRegistry()
                            .getImage(BpmnSharedImages.BUSINESSRULETASKHEADER));
                }
            } else if (element.getCalledOperation() != null) {
                res.add(ElementImageService.getIcon(element.getCalledOperation()));
            }
        }
        return res;
    }

    /**
     * Empty c'torfor deserialization.
     */
    @objid ("609a5732-55b6-11e2-877f-002564c97630")
    public GmBpmnCallActivityHeader() {
        // empty constructor for the serialization
    }

    @objid ("609a5735-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnCallActivityHeader." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0(in);
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 0
            read_0(in);
            break;
        }
        }
    }

    @objid ("609a573b-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnCallActivityHeader." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("609bdd99-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("609bdd9e-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
