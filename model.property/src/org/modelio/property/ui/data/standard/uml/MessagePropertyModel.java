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
                                    

package org.modelio.property.ui.data.standard.uml;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.modelelement.ModelElementListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageEnd;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Generalization;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.InterfaceRealization;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.ProvidedInterface;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>Message</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Message</i> metaclass.
 */
@objid ("8f489134-c068-11e1-8c0a-002564c97630")
public class MessagePropertyModel extends AbstractPropertyModel<Message> {
    /**
     * Properties to display for <i>Message</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a782690d-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Message", "Name", "Invoked", "Argument", "SignalSignature"};

    @objid ("8f48913f-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f489140-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f489141-c068-11e1-8c0a-002564c97630")
    private SingleElementType defaultInvokedType = null;

    @objid ("8f489142-c068-11e1-8c0a-002564c97630")
    private SingleElementType signalSignatureType = null;

    @objid ("15f8abb7-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>Message</i> data model from an <i>Message</i>.
     */
    @objid ("8f489143-c068-11e1-8c0a-002564c97630")
    public MessagePropertyModel(Message theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.defaultInvokedType = new SingleElementType(true, Operation.class, session);
        this.signalSignatureType = new SingleElementType(true, Signal.class, session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f489149-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f48914e-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return MessagePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f489153-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return MessagePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getInvoked();
            case 3:
                return this.theEditedElement.getArgument();
            case 4:
                return this.theEditedElement.getSignalSignature();
            default:
                return null;
            }
        default:
            return null;
        }
    }

    /**
     * Return the type of the element displayed at the specified row and column.
     * <p>
     * This type will be used to choose an editor and a renderer for each cell
     * of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8f489159-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
            case 0: // Header
                return this.labelStringType;
            case 1:
                return this.stringType;
            case 2:
                return getInvokedOperationType();
            case 3:
                return this.stringType;
            case 4:
                return this.signalSignatureType;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("8f48915f-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
            case 0:
                return; // Header cannot be modified
            case 1:
                this.theEditedElement.setName((String) value);
                break;
            case 2:
                this.theEditedElement.setInvoked((Operation) value);
                break;
            case 3:
                this.theEditedElement.setArgument((String) value);
                break;
            case 4:
                this.theEditedElement.setSignalSignature((Signal) value);
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    @objid ("8f489165-c068-11e1-8c0a-002564c97630")
    private IPropertyType getInvokedOperationType() {
        MessageEnd message = this.theEditedElement.getReceiveEvent();
        
        if (message == null) {
            return this.defaultInvokedType;
        }
        
        EList<Lifeline> lifelines = message.getCovered();
        if (lifelines.size() != 1) {
            return this.defaultInvokedType;
        }
        
        Lifeline lifeline = lifelines.get(0);
        if (lifeline == null) {
            return this.defaultInvokedType;
        }
        
        Instance instance = lifeline.getRepresented();
        if (instance == null) {
            return this.defaultInvokedType;
        }
        
        NameSpace ns = instance.getBase();
        
        if (!(ns instanceof Classifier)) {
            return this.defaultInvokedType;
        }
        
        Classifier classifier = (Classifier)ns;
        
        List<ModelElement> availableOperations = getAvailableOperations(classifier);
        
        ModelElementListType type = new ModelElementListType(true, Operation.class, availableOperations, CoreSession.getSession(this.theEditedElement));
        return type;
    }

    @objid ("8f489169-c068-11e1-8c0a-002564c97630")
    private List<ModelElement> getAvailableOperations(Classifier classifier) {
        List<ModelElement> operationsList = new ArrayList<>(); 
        
        if (classifier != null) {
            // Get operations from the classifier itself:
            for (Operation op : classifier.getOwnedOperation()) {
                operationsList.add(op);
            }
        
            // Get operations from parents:
            for (Generalization generalization : classifier.getParent()) {
                NameSpace parentNameSpace = generalization.getSuperType();
                if (parentNameSpace instanceof Classifier) {
                    operationsList.addAll(getAvailableOperations((Classifier)parentNameSpace));
                }
            }
        
            // Get operations from realized interfaces:
            for (InterfaceRealization realization : classifier.getRealized()) {
                Interface parentNameSpace = realization.getImplemented();
                operationsList.addAll(getAvailableOperations(parentNameSpace));
            }
        
            // Get operations from provided interfaces:
            for (BindableInstance bindableInstance : classifier.getInternalStructure()) {
                NameSpace biNs = bindableInstance.getBase();
                if (biNs instanceof Classifier && !biNs.equals(classifier)) {
                    operationsList.addAll(getAvailableOperations((Classifier)biNs));
                }
                if (bindableInstance instanceof Port) {
                    Port port = (Port)bindableInstance;
                    EList<ProvidedInterface> providedInterfaces = port.getProvided();
                    for (ProvidedInterface providedInterface : providedInterfaces) {
                        EList<Interface> interfaces = providedInterface.getProvidedElement();
                        for (Interface itf : interfaces) {
                            operationsList.addAll(getAvailableOperations(itf));
                        }
                    }
                }
            }
        }
        return operationsList;
    }

}
