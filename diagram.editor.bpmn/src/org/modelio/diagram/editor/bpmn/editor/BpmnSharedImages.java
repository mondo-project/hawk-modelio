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
                                    

package org.modelio.diagram.editor.bpmn.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.modelio.diagram.editor.bpmn.plugin.DiagramEditorBpmn;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.events.BpmnCancelEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnCompensateEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnConditionalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnErrorEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEscalationEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnLinkEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnMessageEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnSignalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.events.BpmnTerminateEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnTimerEventDefinition;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.osgi.framework.Bundle;

/**
 * BPMN diagram shared images registry.
 */
@objid ("60743184-55b6-11e2-877f-002564c97630")
public class BpmnSharedImages implements ISharedImages {
    @objid ("60743186-55b6-11e2-877f-002564c97630")
    public static final String SUBPROCESS = "Static_SubProcess";

    @objid ("60743188-55b6-11e2-877f-002564c97630")
    public static final String LOOP = "Static_Loop";

    @objid ("6074318a-55b6-11e2-877f-002564c97630")
    public static final String SEQUENTIAL = "Static_Sequential";

    @objid ("6074318c-55b6-11e2-877f-002564c97630")
    public static final String PARALLEL = "Static_Parallel";

    @objid ("6074318e-55b6-11e2-877f-002564c97630")
    public static final String ADHOC = "Static_AdHoc";

    @objid ("60743190-55b6-11e2-877f-002564c97630")
    public static final String COMPENSATION = "Static_Compensation";

    @objid ("60743192-55b6-11e2-877f-002564c97630")
    public static final String MESSAGE = "Static_Message";

    @objid ("60743194-55b6-11e2-877f-002564c97630")
    public static final String MESSAGERETURN = "Static_MessageReturn";

    @objid ("60743196-55b6-11e2-877f-002564c97630")
    public static final String INPUT = "Static_Input";

    @objid ("60743198-55b6-11e2-877f-002564c97630")
    public static final String OUTPUT = "Static_Output";

    @objid ("6074319a-55b6-11e2-877f-002564c97630")
    public static final String STORE = "Static_Store";

    @objid ("6074319d-55b6-11e2-877f-002564c97630")
    public static final String SENDTASKHEADER = "Static_SendTask_Header";

    @objid ("6075b7fa-55b6-11e2-877f-002564c97630")
    public static final String RECEIVETASKHEADER = "Static_ReceiveTask_Header";

    @objid ("6075b7fc-55b6-11e2-877f-002564c97630")
    public static final String SERVICETASKHEADER = "Static_ServiceTask_Header";

    @objid ("6075b7fe-55b6-11e2-877f-002564c97630")
    public static final String USERTASKHEADER = "Static_UserTask_Header";

    @objid ("6075b800-55b6-11e2-877f-002564c97630")
    public static final String MANUALTASKHEADER = "Static_ManualTask_Header";

    @objid ("6075b802-55b6-11e2-877f-002564c97630")
    public static final String SCRIPTTASKHEADER = "Static_SvriptTask_Header";

    @objid ("6075b804-55b6-11e2-877f-002564c97630")
    public static final String BUSINESSRULETASKHEADER = "Static_BusinessRuleTask_Header";

    @objid ("6075b806-55b6-11e2-877f-002564c97630")
    public static String NONEMPTYSUBPROCESS = "Static_NonEmptySubProcess";

    @objid ("6074319c-55b6-11e2-877f-002564c97630")
    private ImageRegistry imageRegistry;

    @objid ("6075b807-55b6-11e2-877f-002564c97630")
    public BpmnSharedImages() {
        initializeImageRegistry();
    }

    @objid ("6075b809-55b6-11e2-877f-002564c97630")
    @Override
    public Image getImage(String key) {
        return this.imageRegistry.get(key);
    }

    @objid ("6075b80f-55b6-11e2-877f-002564c97630")
    @Override
    public ImageDescriptor getImageDescriptor(String key) {
        return this.imageRegistry.getDescriptor(key);
    }

    @objid ("6075b815-55b6-11e2-877f-002564c97630")
    public void declareImage(String key, ImageDescriptor descriptor) {
        this.imageRegistry.put(key, descriptor);
    }

    @objid ("6075b819-55b6-11e2-877f-002564c97630")
    private void initializeImageRegistry() {
        this.imageRegistry = new ImageRegistry(Display.getDefault());
        this.imageRegistry.put("MissingImage", ImageDescriptor.getMissingImageDescriptor());
        Bundle bundle = Platform.getBundle(DiagramEditorBpmn.PLUGIN_ID);
        
        ImageDescriptor image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                                               new Path("icons/bpmnexclusivegateway.png"),
                                                                               null));
        
        declareImage(Metamodel.getMClass(BpmnExclusiveGateway.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/bpmnparallelgateway.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnParallelGateway.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/bpmninclusivegateway.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnInclusiveGateway.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/bpmneventbasedgateway.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEventBasedGateway.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/bpmncomplexgateway.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnComplexGateway.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/startevent.png"), null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/endevent.png"), null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/bpmncomplexgateway.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnComplexGateway.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/intermediaryevent_tr.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnBoundaryEvent.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_intermediaryevent_tr.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnMessageEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_" + Metamodel.getMClass(BpmnMessageEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnMessageEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnMessageEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_ni_" +
                     Metamodel.getMClass(BpmnMessageEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnMessageEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/timer_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnTimerEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/timer_startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_" + Metamodel.getMClass(BpmnTimerEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/timer_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnTimerEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/timer_intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_ni_" +
                     Metamodel.getMClass(BpmnTimerEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/error_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnErrorEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/error_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnErrorEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/error_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnErrorEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/escalation_intermediaryevent_tr.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnEscalationEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/escalation_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnEscalationEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/escalation_intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_ni_" +
                     Metamodel.getMClass(BpmnEscalationEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/escalation_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnEscalationEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/escalation_startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_" + Metamodel.getMClass(BpmnEscalationEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/escalation_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnEscalationEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/cancel_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnCancelEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/cancel_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnCancelEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/compensation_intermediaryevent_th.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnCompensateEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/compensation_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnCompensateEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/compensation_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnCompensateEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/compensation_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnCompensateEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/conditional_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnConditionalEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/conditional_startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_" + Metamodel.getMClass(BpmnConditionalEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/conditional_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnConditionalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/conditional_intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_ni_" +
                     Metamodel.getMClass(BpmnConditionalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/link_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() + "_" + Metamodel.getMClass(BpmnLinkEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/link_intermediaryevent_tr.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName() + "_" + Metamodel.getMClass(BpmnLinkEventDefinition.class).getName(),
                     image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/signal_intermediaryevent_tr.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnSignalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/signal_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnSignalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/signal_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_" +
                     Metamodel.getMClass(BpmnSignalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/signal_intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() +
                     "_ni_" +
                     Metamodel.getMClass(BpmnSignalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/signal_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_" + Metamodel.getMClass(BpmnSignalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/signal_startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_" + Metamodel.getMClass(BpmnSignalEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/terminate_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_" + Metamodel.getMClass(BpmnTerminateEventDefinition.class).getName(), image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/multiple_startevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_Multiple", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/multiple_startevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnStartEvent.class).getName() + "_ni_Multiple", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/multiple_intermediaryevent_ca.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() + "_Multiple", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/multiple_intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() + "_ni_Multiple", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/intermediaryevent_ni.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName() + "_ni", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/multiple_intermediaryevent_tr.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName() + "_Multiple", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/multiple_endevent.png"),
                                                               null));
        declareImage(Metamodel.getMClass(BpmnEndEvent.class).getName() + "_Multiple", image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_subprocess.png"),
                                                               null));
        declareImage(SUBPROCESS, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_subprocess_content.png"),
                                                               null));
        declareImage(NONEMPTYSUBPROCESS, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_loop.png"),
                                                               null));
        declareImage(LOOP, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_parallel.png"),
                                                               null));
        declareImage(PARALLEL, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_sequential.png"),
                                                               null));
        declareImage(SEQUENTIAL, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_adhock.png"),
                                                               null));
        declareImage(ADHOC, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/activity_compensation.png"),
                                                               null));
        declareImage(COMPENSATION, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/message.png"), null));
        declareImage(MESSAGE, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/message_return.png"),
                                                               null));
        declareImage(MESSAGERETURN, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/input.png"), null));
        declareImage(INPUT, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/output.png"), null));
        declareImage(OUTPUT, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/store.png"), null));
        declareImage(STORE, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/receivetask_header.png"),
                                                               null));
        declareImage(RECEIVETASKHEADER, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/sendtask_header.png"),
                                                               null));
        declareImage(SENDTASKHEADER, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/servicetask_header.png"),
                                                               null));
        declareImage(SERVICETASKHEADER, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/usertask_header.png"),
                                                               null));
        declareImage(USERTASKHEADER, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/manualtask_header.png"),
                                                               null));
        declareImage(MANUALTASKHEADER, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/scripttask_header.png"),
                                                               null));
        declareImage(SCRIPTTASKHEADER, image);
        
        image = ImageDescriptor.createFromURL(FileLocator.find(bundle,
                                                               new Path("icons/businessruletask_header.png"),
                                                               null));
        declareImage(BUSINESSRULETASKHEADER, image);
    }

}
