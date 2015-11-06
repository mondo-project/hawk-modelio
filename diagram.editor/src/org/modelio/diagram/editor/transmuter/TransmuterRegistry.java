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
                                    

package org.modelio.diagram.editor.transmuter;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.activities.BpmnAdHocSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnBusinessRuleTask;
import org.modelio.metamodel.bpmn.activities.BpmnCallActivity;
import org.modelio.metamodel.bpmn.activities.BpmnManualTask;
import org.modelio.metamodel.bpmn.activities.BpmnReceiveTask;
import org.modelio.metamodel.bpmn.activities.BpmnScriptTask;
import org.modelio.metamodel.bpmn.activities.BpmnSendTask;
import org.modelio.metamodel.bpmn.activities.BpmnServiceTask;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnTask;
import org.modelio.metamodel.bpmn.activities.BpmnTransaction;
import org.modelio.metamodel.bpmn.activities.BpmnUserTask;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Registry dedicated to transmutation service of model elements
 */
@objid ("847ecf99-5c05-11e2-a156-00137282c51b")
public class TransmuterRegistry {
    @objid ("847ecf9b-5c05-11e2-a156-00137282c51b")
    protected static final TransmuterRegistry INSTANCE = new TransmuterRegistry();

    @objid ("847ecf9c-5c05-11e2-a156-00137282c51b")
    private HashMap<String, Class<? extends ITransmuter>> registry;

    /**
     * Singleton of the TransmuterRegistry class
     * @return Instance of TransmuterRegistry
     */
    @objid ("847ecfa2-5c05-11e2-a156-00137282c51b")
    public static TransmuterRegistry getInstance() {
        return INSTANCE;
    }

    @objid ("847ecfa7-5c05-11e2-a156-00137282c51b")
    private TransmuterRegistry() {
        this.registry = new HashMap<>();
        initRegistry();
    }

    /**
     * Test if a transmutation between two elements can be done
     * @param originmetaclass : Metaclass of the element to be transmuted
     * @param targetmetaclass : Target metaclass of transmutation
     * @return True if transmutation can be done
     */
    @objid ("847ecfa9-5c05-11e2-a156-00137282c51b")
    public boolean canTransmute(final Class<? extends MObject> originmetaclass, final Class<? extends MObject> targetmetaclass) {
        return this.registry.containsKey(getKey(originmetaclass, targetmetaclass));
    }

    /**
     * Test if a transmutation between two elements can be done
     * @param origine : element to be transmuted
     * @param targetmetaclass Target metaclass of transmutation
     * @return True if transmutation can be done
     */
    @objid ("848131f5-5c05-11e2-a156-00137282c51b")
    public boolean canTransmute(final MObject origine, final Class<? extends MObject> targetmetaclass) {
        return canTransmute(origine.getClass(), targetmetaclass);
    }

    /**
     * Returns the implementation class of a transmutation
     * @param element lement to be transmuted
     * @param metaclass Target metaclass of transmutation
     * @return implementation class of transmutation
     */
    @objid ("84813200-5c05-11e2-a156-00137282c51b")
    public ITransmuter getTransmuter(final MObject element, final String metaclass) {
        String key = getKey(element.getMClass().getName(), metaclass);
        Class<? extends ITransmuter> transmuter = this.registry.get(key);
        if(transmuter != null){
            try {
                Constructor<?> constructeur = transmuter.getConstructor(new Class[] { MObject.class, String.class });
                return (ITransmuter) constructeur.newInstance(new Object[] { element, metaclass });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @objid ("84813209-5c05-11e2-a156-00137282c51b")
    private String getKey(final Class<? extends MObject> origine, final Class<? extends MObject> transmuted) {
        return getKey(getMetaclassName(origine),getMetaclassName(transmuted));
    }

    @objid ("8483944d-5c05-11e2-a156-00137282c51b")
    private void initRegistry() {
        this.registry.put(getKey(BpmnTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnSendTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSendTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnReceiveTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnServiceTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnServiceTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnUserTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnUserTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnManualTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnManualTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnScriptTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnScriptTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnBusinessRuleTask.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnSubProcess.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnSubProcess.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnAdHocSubProcess.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnTransaction.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnTransaction.class, BpmnCallActivity.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnCallActivity.class, BpmnTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnSendTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnReceiveTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnServiceTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnUserTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnManualTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnScriptTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnBusinessRuleTask.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnAdHocSubProcess.class), BpmnActivityTransmuter.class);
        this.registry.put(getKey(BpmnCallActivity.class, BpmnTransaction.class), BpmnActivityTransmuter.class);
        
        this.registry.put(getKey(BpmnExclusiveGateway.class, BpmnInclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnExclusiveGateway.class, BpmnComplexGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnExclusiveGateway.class, BpmnParallelGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnExclusiveGateway.class, BpmnEventBasedGateway.class), BpmnGatewayTransmuter.class);
        
        this.registry.put(getKey(BpmnComplexGateway.class, BpmnExclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnComplexGateway.class, BpmnInclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnComplexGateway.class, BpmnParallelGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnComplexGateway.class, BpmnEventBasedGateway.class), BpmnGatewayTransmuter.class);
        
        this.registry.put(getKey(BpmnParallelGateway.class, BpmnInclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnParallelGateway.class, BpmnComplexGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnParallelGateway.class, BpmnExclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnParallelGateway.class, BpmnEventBasedGateway.class), BpmnGatewayTransmuter.class);
        
        this.registry.put(getKey(BpmnEventBasedGateway.class, BpmnInclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnEventBasedGateway.class, BpmnComplexGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnEventBasedGateway.class, BpmnParallelGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnEventBasedGateway.class, BpmnExclusiveGateway.class), BpmnGatewayTransmuter.class);
        
        this.registry.put(getKey(BpmnInclusiveGateway.class, BpmnExclusiveGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnInclusiveGateway.class, BpmnComplexGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnInclusiveGateway.class, BpmnParallelGateway.class), BpmnGatewayTransmuter.class);
        this.registry.put(getKey(BpmnInclusiveGateway.class, BpmnEventBasedGateway.class), BpmnGatewayTransmuter.class);
        
        this.registry.put(getKey(BpmnStartEvent.class, BpmnEndEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnStartEvent.class, BpmnIntermediateCatchEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnStartEvent.class, BpmnIntermediateThrowEvent.class), BpmnEventTransmuter.class);
        
        this.registry.put(getKey(BpmnEndEvent.class, BpmnStartEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnEndEvent.class, BpmnIntermediateCatchEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnEndEvent.class, BpmnIntermediateThrowEvent.class), BpmnEventTransmuter.class);
        
        this.registry.put(getKey(BpmnIntermediateCatchEvent.class, BpmnEndEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnIntermediateCatchEvent.class, BpmnStartEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnIntermediateCatchEvent.class, BpmnIntermediateThrowEvent.class), BpmnEventTransmuter.class);
        
        this.registry.put(getKey(BpmnIntermediateThrowEvent.class, BpmnEndEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnIntermediateThrowEvent.class, BpmnIntermediateCatchEvent.class), BpmnEventTransmuter.class);
        this.registry.put(getKey(BpmnIntermediateThrowEvent.class, BpmnStartEvent.class), BpmnEventTransmuter.class);
    }

    @objid ("8483944f-5c05-11e2-a156-00137282c51b")
    public boolean canTransmute(final String originmetaclass, final String targetmetaclass) {
        return this.registry.containsKey(getKey(originmetaclass, targetmetaclass));
    }

    /**
     * Returns the implementation class of a transmutation
     * @param element lement to be transmuted
     * @param metaclass Target metaclass of transmutation
     * @return implementation class of transmutation
     */
    @objid ("84839457-5c05-11e2-a156-00137282c51b")
    public ITransmuter getTransmuter(final MObject element, final Class<? extends MObject> metaclass) {
        return getTransmuter(element,getMetaclassName(metaclass));
    }

    @objid ("84839462-5c05-11e2-a156-00137282c51b")
    private String getKey(final String origine, final String transmuted) {
        String key = origine;
        key = key.concat("_");
        key = key.concat(transmuted);
        return key;
    }

    @objid ("8483946a-5c05-11e2-a156-00137282c51b")
    private String getMetaclassName(final Class<? extends MObject> metaclass) {
        return Metamodel.getMClass(metaclass).getName();
    }

}
