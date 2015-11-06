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
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.StructuralFeature;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>InformationFlow</i> data model.
 * <p>
 * This class provides the list of properties for the <i>InformationFlow</i> metaclass.
 */
@objid ("8f2365b7-c068-11e1-8c0a-002564c97630")
public class InformationFlowPropertyModel extends AbstractPropertyModel<InformationFlow> {
    /**
     * Properties to display for <i>InformationFlow</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a73fc288-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"InformationFlow", "Name", "Conveyed", "InformationSource", "Owner", "InformationTarget", "Realizing"};

    @objid ("8f24ec26-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f24ec27-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f24ec28-c068-11e1-8c0a-002564c97630")
    private MultipleElementType conveyedType = null;

    @objid ("8f24ec29-c068-11e1-8c0a-002564c97630")
    private SingleElementType informationSourceType = null;

    @objid ("8f24ec2a-c068-11e1-8c0a-002564c97630")
    private SingleElementType ownerType = null;

    @objid ("8f24ec2b-c068-11e1-8c0a-002564c97630")
    private SingleElementType informationTargetType = null;

    @objid ("8f24ec2c-c068-11e1-8c0a-002564c97630")
    private SingleElementType realizingType = null;

    /**
     * Create a new <i>InformationFlow</i> data model from an <i>InformationFlow</i>.
     */
    @objid ("8f24ec2d-c068-11e1-8c0a-002564c97630")
    public InformationFlowPropertyModel(InformationFlow theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.conveyedType = new MultipleElementType(true, theEditedElement, "Conveyed", Classifier.class, model);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.informationSourceType = new SingleElementType(false, ModelElement.class, session);
        this.ownerType = new SingleElementType(true, NameSpace.class, session);
        this.informationTargetType = new SingleElementType(false, ModelElement.class, session);
        List<java.lang.Class<? extends MObject>> realizingTypes = new ArrayList<>(); 
        realizingTypes.add(ActivityEdge.class);
        realizingTypes.add(CommunicationMessage.class);
        realizingTypes.add(LinkEnd.class);
        realizingTypes.add(Message.class);
        realizingTypes.add(StructuralFeature.class);
        this.realizingType = new SingleElementType(true, realizingTypes);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f24ec33-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f24ec38-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return InformationFlowPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f24ec3d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return InformationFlowPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getConveyed();
                case 3:
                {
                    EList< ModelElement > sources = this.theEditedElement.getInformationSource();
                    if (  sources != null && sources.size() > 0 )
                        return sources.get(0);
                    // else
                    return null;
                }
                case 4:
                    return this.theEditedElement.getOwner();
                case 5:
                {
                    EList< ModelElement > targets = this.theEditedElement.getInformationTarget();
                    if (  targets != null && targets.size() > 0 )
                        return targets.get(0);
                    // else
                    return null;
                }
                case 6:
                    return getRealizing();
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
    @objid ("8f24ec43-c068-11e1-8c0a-002564c97630")
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
                    return this.conveyedType;
                case 3:
                    return this.informationSourceType;
                case 4:
                    return this.ownerType;
                case 5:
                    return this.informationTargetType;
                case 6:
                    return this.realizingType;
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
    @objid ("8f24ec49-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
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
                    for (Classifier s : this.theEditedElement.getConveyed())
                        this.theEditedElement.getConveyed().remove(s);
                    
                    List<Classifier> newcontent = (List<Classifier>)value;
                    for (Classifier s : newcontent)
                        this.theEditedElement.getConveyed().add(s);
                    break;
                case 3:
                    this.theEditedElement.getInformationSource().remove(this.theEditedElement.getInformationSource().get(0));
                    this.theEditedElement.getInformationSource().add((ModelElement) value);
                    break;
                case 4:
                    this.theEditedElement.setOwner((NameSpace) value);
                    break;
                case 5:
                    this.theEditedElement.getInformationTarget().remove(this.theEditedElement.getInformationSource().get(0));
                    this.theEditedElement.getInformationTarget().add((ModelElement) value);
                    break;
                case 6:
                    setRealizing(this.theEditedElement, value);
                    break;
                default:
                    return;
            }
              break;
        default:
            return;
        }
    }

    /**
     * Returns the element realizing by the given InformationFlow node.
     * @return the realizing element
     */
    @objid ("8f24ec50-c068-11e1-8c0a-002564c97630")
    private ModelElement getRealizing() {
        ModelElement ret = null;
        
        EList<StructuralFeature> featureList = this.theEditedElement.getRealizingFeature();
        if (featureList.size() > 0) {
             ret = featureList.get(0);
             if (ret != null)
                 return ret;
        }
        
        EList<LinkEnd> linkList = this.theEditedElement.getRealizingLink();
        if (linkList.size() > 0) {
             ret = linkList.get(0);
             if (ret != null)
                 return ret;
        }
        
        EList<ActivityEdge> edgeList = this.theEditedElement.getRealizingActivityEdge();
        if (edgeList.size() > 0) {
             ret = edgeList.get(0);
             if (ret != null)
                 return ret;
        }
        
         EList<Message> messageList = this.theEditedElement.getRealizingMessage();
         if (messageList.size() > 0) {
             ret = messageList.get(0);
             if (ret != null)
                 return ret;
         }
         
         EList<CommunicationMessage> communicationMessageList = this.theEditedElement.getRealizingCommunicationMessage();
         if (communicationMessageList.size() > 0) {
             ret = communicationMessageList.get(0);
             if (ret != null)
                 return ret;
         }
        return null;
    }

    /**
     * Set the InstanceNode realizing elements.
     * This method set the right dependency and clears the otheEditedElement.
     * @param theEditedElement the instance node
     * @param value the new represented element
     */
    @objid ("8f24ec57-c068-11e1-8c0a-002564c97630")
    private void setRealizing(InformationFlow theEditedElement, Object value) {
        // Erase old value or exit if old value is new value
        EList<StructuralFeature> featureList = theEditedElement.getRealizingFeature();
        if (featureList.size() > 0) {
            StructuralFeature old1 = featureList.get(0);
            if (old1.equals(value)) return;
            theEditedElement.getRealizingFeature().remove(old1);
        } 
        
         EList<LinkEnd> linkList = theEditedElement.getRealizingLink();
         if (linkList.size() > 0) {
             LinkEnd old1 = linkList.get(0);
             if (old1.equals(value)) return;
             theEditedElement.getRealizingLink().remove(old1);
         } 
         
         EList<ActivityEdge> edgeList = theEditedElement.getRealizingActivityEdge();
         if (edgeList.size() > 0) {
             ActivityEdge old3 = edgeList.get(0);
             if (old3.equals(value)) return;
             theEditedElement.getRealizingActivityEdge().remove(old3);
         }
         
         EList<Message> messageList = theEditedElement.getRealizingMessage();
         if (messageList.size() > 0) {
             Message old4 = messageList.get(0);
             if (old4.equals(value)) return;
             theEditedElement.getRealizingMessage().remove(old4);
         }
         
         EList<CommunicationMessage> communicationMessageList = theEditedElement.getRealizingCommunicationMessage();
         if (communicationMessageList.size() > 0) {
             CommunicationMessage old5 = communicationMessageList.get(0);
             if (old5.equals(value)) return;
             theEditedElement.getRealizingCommunicationMessage().remove(old5);
         }
         
         if (value != null) {
             // Set new value
             if (LinkEnd.class.isAssignableFrom(value.getClass()))
                 theEditedElement.getRealizingLink().add((LinkEnd) value);
             if (StructuralFeature.class.isAssignableFrom(value.getClass()))
                 theEditedElement.getRealizingFeature().add((StructuralFeature) value);
             else if (ActivityEdge.class.isAssignableFrom(value.getClass()))
                 theEditedElement.getRealizingActivityEdge().add((ActivityEdge) value);
             else if (Message.class.isAssignableFrom(value.getClass()))
                 theEditedElement.getRealizingMessage().add((Message) value);
             else if (CommunicationMessage.class.isAssignableFrom(value.getClass()))
                 theEditedElement.getRealizingCommunicationMessage().add((CommunicationMessage) value);
         }
    }

}
