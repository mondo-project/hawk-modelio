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
                                    

package org.modelio.linkeditor.options;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.preference.IPreferenceStore;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

/**
 * Simple class used to store the current state of the LinkEditor options.
 * <p>Relies on {@link IPreferenceStore} for persistence.</p>
 */
@objid ("1b4c39af-5e33-11e2-b81d-002564c97630")
public class LinkEditorOptions {
    @objid ("1b4c39b7-5e33-11e2-b81d-002564c97630")
    private static final String DEPENDENCY_FILTER_LIST = "org.modelio.linkeditor.property.DependencyFilterList";

    @objid ("1b4c39b9-5e33-11e2-b81d-002564c97630")
    private static final String SHOW_ASSOCIATION = "org.modelio.linkeditor.property.ShowAssociation";

    @objid ("1b4c39bb-5e33-11e2-b81d-002564c97630")
    private static final String SHOW_INHERITANCE = "org.modelio.linkeditor.property.ShowInheritance";

    @objid ("1b4c39bd-5e33-11e2-b81d-002564c97630")
    private static final String SHOW_ELEMENTIMPORT = "org.modelio.linkeditor.property.ShowElementImport";

    @objid ("1b4c39bf-5e33-11e2-b81d-002564c97630")
    private static final String IS_PIN_EDITOR_ENABLE = "org.modelio.linkeditor.property.PinEditor";

    @objid ("1b4c39c1-5e33-11e2-b81d-002564c97630")
    private static final String ORIENTATION = "org.modelio.property.SetOrientation";

    @objid ("1b4c39c3-5e33-11e2-b81d-002564c97630")
    private static final String SHOW_DEPENDENCY = "org.modelio.linkeditor.property.ShowDependency";

    @objid ("1b4c39c5-5e33-11e2-b81d-002564c97630")
    private static final String SHOW_NAMESPACEUSE = "org.modelio.property.ShowNamespaceUse";

    @objid ("1b4c39c7-5e33-11e2-b81d-002564c97630")
    private static final String SHOW_TRACE = "org.modelio.linkeditor.property.ShowTrace";

    @objid ("2867a134-f665-4317-9c3e-b708bee82f35")
    private static final boolean SHOW_DEPENDENCY_DEFAULT = false;

    @objid ("9603c792-d129-4a82-8a5d-2180f8643269")
    private static final String IS_DEPENDENCY_FILTERED = "org.modelio.linkeditor.property.IsDependencyFiltered";

    @objid ("26473150-0bc8-4f4d-bd45-3b55515a3077")
    private static final boolean IS_DEPENDENCY_FILTERED_DEFAULT = false;

    @objid ("b2e639bf-3b03-4cf2-839f-b08b407bcf80")
    private static final boolean SHOW_ASSOCIATION_DEFAULT = true;

    @objid ("c589b653-2851-4630-b48d-05399aecf88f")
    private static final boolean SHOW_INHERITANCE_DEFAULT = true;

    @objid ("5b74f008-4e99-42f4-af3c-3f9a6d8f0965")
    private static final boolean SHOW_ELEMENTIMPORT_DEFAULT = false;

    @objid ("34b04412-732a-4b82-934b-7f71429fd9de")
    private static final boolean IS_PIN_EDITOR_ENABLE_DEFAULT = false;

    @objid ("45b6507a-22e2-4dee-9e5a-b0a3e80d72a7")
    private static final String ORIENTATION_AUTO = "Auto";

    @objid ("f8ac8777-bfee-4f53-9963-3e4b69a8a143")
    private static final boolean SHOW_NAMESPACEUSE_DEFAULT = false;

    @objid ("81156f17-4682-4e52-9a67-2e3c94bc64c0")
    private static final boolean SHOW_TRACE_DEFAULT = false;

    @objid ("64744ef1-2285-47d1-8a6f-c3a2981ad8d6")
    private static final String LEFT_DEPTH_VALUE = "org.modelio.linkeditor.property.LeftDepthValue";

    @objid ("6cebd809-bad7-45db-a271-f75d248bffdb")
    private static final int LEFT_DEPTH_VALUE_DEFAULT = 1;

    @objid ("99e130da-b30d-4720-8567-96f3af63e8f1")
    private static final String RIGHT_DEPTH_VALUE = "org.modelio.linkeditor.property.RightDepthValue";

    @objid ("ab02002c-d958-4bf3-9527-b5c538eee341")
    private static final int RIGHT_DEPTH_VALUE_DEFAULT = 1;

    @objid ("de240961-0dd9-4627-b628-476e5a9b2219")
    private static final String ORIENTATION_VERTICAL = "Vertical";

    @objid ("894580dc-d93f-435f-aefc-c4fd27ef6c6b")
    private static final String ORIENTATION_DEFAULT = ORIENTATION_AUTO;

    @objid ("b55b3b92-648b-49a5-9518-03dacd95390d")
    private IPreferenceStore preferences;

    @objid ("4fbe35cc-a2c3-46de-b57c-d22fdb4a9488")
    private IMModelServices modelServices;

    /**
     * @return <code>true</code> if associations should be shown.
     */
    @objid ("1b4c39c9-5e33-11e2-b81d-002564c97630")
    public boolean isAssociationShown() {
        boolean state = SHOW_ASSOCIATION_DEFAULT;
        if (this.preferences != null && this.preferences.contains(SHOW_ASSOCIATION)) {
            state = this.preferences.getBoolean(SHOW_ASSOCIATION);
        }
        return state;
    }

    /**
     * @param value <code>true</code> if associations should be shown.
     */
    @objid ("1b4c39ce-5e33-11e2-b81d-002564c97630")
    public void setAssociationShown(final boolean isAssociationShown) {
        if(this.preferences != null) {
            this.preferences.setValue(LinkEditorOptions.SHOW_ASSOCIATION, isAssociationShown);
        }
    }

    /**
     * @return <code>true</code> if inheritance should be shown.
     */
    @objid ("1b4c39d3-5e33-11e2-b81d-002564c97630")
    public boolean isInheritanceShown() {
        boolean state = SHOW_INHERITANCE_DEFAULT;
        if (this.preferences != null && this.preferences.contains(SHOW_INHERITANCE)) {
            state = this.preferences.getBoolean(SHOW_INHERITANCE);
        }
        return state;
    }

    /**
     * @param isInheritanceShown <code>true</code> if inheritance should be shown.
     */
    @objid ("1b4c39d8-5e33-11e2-b81d-002564c97630")
    public void setInheritanceShown(final boolean isInheritanceShown) {
        if(this.preferences != null){            
            this.preferences.setValue(SHOW_INHERITANCE, isInheritanceShown);
        }
    }

    /**
     * @return <code>true</code> if dependency should be shown.
     */
    @objid ("1b4e9b0d-5e33-11e2-b81d-002564c97630")
    public boolean isDependencyShown() {
        boolean state = SHOW_DEPENDENCY_DEFAULT;
        if (this.preferences != null && this.preferences.contains(SHOW_DEPENDENCY)) {
            state = this.preferences.getBoolean(SHOW_DEPENDENCY);
        }
        return state;
    }

    /**
     * @param isDependencyShown <code>true</code> if dependencies should be shown.
     */
    @objid ("1b4e9b12-5e33-11e2-b81d-002564c97630")
    public void setDependencyShown(final boolean isDependencyShown) {
        if(this.preferences != null){            
            this.preferences.setValue(SHOW_DEPENDENCY, isDependencyShown);
        }
    }

    /**
     * @return the current dependency filter
     */
    @objid ("1b4e9b17-5e33-11e2-b81d-002564c97630")
    public List<Stereotype> getDependencyFilter() {
        //Parse stereotypes uuid to build the stereotype list
        List<Stereotype> dependencyFilter = new ArrayList<>();
        if(!getDependencyFilterList().isEmpty()){
            StringTokenizer tokenizer = new StringTokenizer(getDependencyFilterList(), ",");
            while(tokenizer.hasMoreTokens()){
                String stereotypeUuid = tokenizer.nextToken();
                Stereotype stereotype = (Stereotype) this.modelServices.findById(Metamodel.getMClass(Stereotype.class), UUID.fromString(stereotypeUuid));
                if(stereotype!=null) {
                    dependencyFilter.add(stereotype);
                }
            }
        }
        return dependencyFilter;
    }

    /**
     * @param dependencyFilter the new dependency filter.
     */
    @objid ("1b4e9b1e-5e33-11e2-b81d-002564c97630")
    public void setDependencyFilter(final List<Stereotype> dependencyFilter) {
        StringBuilder builder = new StringBuilder(2000);
        for(Stereotype s : dependencyFilter){
            builder.append(s.getUuid()+",");
        }
        setDependencyFilterList(builder.toString());
    }

    /**
     * @return the current maximum left depth.
     */
    @objid ("1b4e9b2f-5e33-11e2-b81d-002564c97630")
    public int getLeftDepth() {
        int value = LEFT_DEPTH_VALUE_DEFAULT;
        if(this.preferences != null && this.preferences.contains(LEFT_DEPTH_VALUE)) {
            value = this.preferences.getInt(LEFT_DEPTH_VALUE);
        }
        return value;
    }

    /**
     * Sets the new maximum left depth.
     * @param fromDepth the new maximum left depth
     */
    @objid ("1b4e9b34-5e33-11e2-b81d-002564c97630")
    public void setLeftDepth(final int fromDepth) {
        if(this.preferences != null){            
            this.preferences.setValue(LEFT_DEPTH_VALUE, fromDepth);
        }
    }

    /**
     * @return the current maximum right depth.
     */
    @objid ("1b4e9b39-5e33-11e2-b81d-002564c97630")
    public int getRightDepth() {
        int value = RIGHT_DEPTH_VALUE_DEFAULT;
        if(this.preferences != null && this.preferences.contains(RIGHT_DEPTH_VALUE)) {
            value = this.preferences.getInt(RIGHT_DEPTH_VALUE);            
        }
        return value;
    }

    /**
     * Sets the new maximum right depth.
     * @param toDepth the new maximum right depth
     */
    @objid ("1b4e9b3e-5e33-11e2-b81d-002564c97630")
    public void setRightDepth(final int toDepth) {
        if(this.preferences != null){            
            this.preferences.setValue(RIGHT_DEPTH_VALUE, toDepth);
        }
    }

    /**
     * @return <code>true</code> if element import should be shown.
     */
    @objid ("1b4e9b43-5e33-11e2-b81d-002564c97630")
    public boolean isImportShown() {
        boolean state = SHOW_ELEMENTIMPORT_DEFAULT;
        if (this.preferences != null && this.preferences.contains(SHOW_ELEMENTIMPORT)) {
            state = this.preferences.getBoolean(SHOW_ELEMENTIMPORT);
        }
        return state;
    }

    /**
     * @param isImportShown <code>true</code> if element import should be shown.
     */
    @objid ("1b4e9b48-5e33-11e2-b81d-002564c97630")
    public void setImportShown(final boolean isImportShown) {
        if(this.preferences != null){            
            this.preferences.setValue(SHOW_ELEMENTIMPORT, isImportShown);
        }
    }

    /**
     * C'tor. Initializes by project preferences.
     */
    @objid ("1b4e9b4d-5e33-11e2-b81d-002564c97630")
    public LinkEditorOptions(IPreferenceStore preferences, IMModelServices aModelServices) {
        this.preferences = preferences;
        this.modelServices = aModelServices;
        
        if (preferences != null) {
            preferences.setDefault(SHOW_DEPENDENCY, SHOW_DEPENDENCY_DEFAULT);
            preferences.setDefault(IS_DEPENDENCY_FILTERED, IS_DEPENDENCY_FILTERED_DEFAULT);
            preferences.setDefault(SHOW_ASSOCIATION, SHOW_ASSOCIATION_DEFAULT);
            preferences.setDefault(SHOW_INHERITANCE, SHOW_INHERITANCE_DEFAULT);
            preferences.setDefault(SHOW_ELEMENTIMPORT, SHOW_ELEMENTIMPORT_DEFAULT);
            preferences.setDefault(IS_PIN_EDITOR_ENABLE, IS_PIN_EDITOR_ENABLE_DEFAULT);
            preferences.setDefault(SHOW_NAMESPACEUSE, SHOW_NAMESPACEUSE_DEFAULT);
            preferences.setDefault(SHOW_TRACE, SHOW_TRACE_DEFAULT);
            preferences.setDefault(LEFT_DEPTH_VALUE, LEFT_DEPTH_VALUE_DEFAULT);
            preferences.setDefault(RIGHT_DEPTH_VALUE, RIGHT_DEPTH_VALUE_DEFAULT);
            preferences.setDefault(ORIENTATION, ORIENTATION_DEFAULT);
        }
    }

    /**
     * @return <code>true</code> if the current state of the view is pinned.
     */
    @objid ("1b4e9b56-5e33-11e2-b81d-002564c97630")
    public boolean isPinned() {
        boolean state = IS_PIN_EDITOR_ENABLE_DEFAULT;
        if (this.preferences != null && this.preferences.contains(IS_PIN_EDITOR_ENABLE)) {
            state = this.preferences.getBoolean(IS_PIN_EDITOR_ENABLE);
        }
        return state;
    }

    /**
     * @return <code>true</code> if the view should use a vertical layout.
     */
    @objid ("1b4e9b5b-5e33-11e2-b81d-002564c97630")
    public boolean isLayoutOrientationVertical() {
        String layoutOrientation = getOrientation();
        
        if (layoutOrientation.equals(ORIENTATION_AUTO)) {
            // true if inheritance is the only shown link category.
            return (isInheritanceShown() && !isAssociationShown() && !isDependencyShown() && !isTraceShown() && !isImportShown() && !isNamespaceUseShown());
        } else if (layoutOrientation.equals(ORIENTATION_VERTICAL)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return <code>true</code> if the current dependency filter should be used.
     */
    @objid ("1b4e9b62-5e33-11e2-b81d-002564c97630")
    public boolean isDependencyFiltered() {
        boolean state = IS_DEPENDENCY_FILTERED_DEFAULT;
        if (this.preferences != null && this.preferences.contains(IS_DEPENDENCY_FILTERED)) {
            state = this.preferences.getBoolean(IS_DEPENDENCY_FILTERED);
        }
        return state;
    }

    /**
     * @param isDependencyFiltered <code>true</code> if the current dependency should be used.
     */
    @objid ("1b50fc6f-5e33-11e2-b81d-002564c97630")
    public void setDependencyFiltered(final boolean isDependencyFiltered) {
        if(this.preferences != null){            
            this.preferences.setValue(LinkEditorOptions.IS_DEPENDENCY_FILTERED, isDependencyFiltered);
        }
    }

    /**
     * @param isPinned <code>true</code> if the editor is pinned.
     */
    @objid ("1b50fc74-5e33-11e2-b81d-002564c97630")
    public void setPinned(final boolean isPinned) {
        if(this.preferences != null){            
            this.preferences.setValue(IS_PIN_EDITOR_ENABLE, isPinned);
        }
    }

    /**
     * @return <code>true</code> if NamespaceUse should be shown.
     */
    @objid ("1b50fc79-5e33-11e2-b81d-002564c97630")
    public boolean isNamespaceUseShown() {
        boolean state = SHOW_NAMESPACEUSE_DEFAULT;
        if (this.preferences != null && this.preferences.contains(SHOW_NAMESPACEUSE)) {
            state = this.preferences.getBoolean(SHOW_NAMESPACEUSE);
        }
        return state;
    }

    /**
     * @param isNamespaceUseShown <code>true</code> if namespaceuse should be shown.
     */
    @objid ("1b50fc7e-5e33-11e2-b81d-002564c97630")
    public void setNamespaceUseShown(final boolean isNamespaceUseShown) {
        if(this.preferences != null){            
            this.preferences.setValue(SHOW_NAMESPACEUSE, isNamespaceUseShown);
        }
    }

    /**
     * @return <code>true</code> if traceability links should be shown.
     */
    @objid ("1b50fc83-5e33-11e2-b81d-002564c97630")
    public boolean isTraceShown() {
        boolean state = SHOW_TRACE_DEFAULT;
        if (this.preferences != null && this.preferences.contains(SHOW_TRACE)) {
            state = this.preferences.getBoolean(SHOW_TRACE);
        }
        return state;
    }

    /**
     * @param isTraceShown <code>true</code> if traceability links should be shown.
     */
    @objid ("1b50fc88-5e33-11e2-b81d-002564c97630")
    public void setTraceShown(final boolean isTraceShown) {
        if(this.preferences != null){            
            this.preferences.setValue(SHOW_TRACE, isTraceShown);
        }
    }

    @objid ("80d5f141-b39e-4cf3-964e-12b96f2ceef2")
    public void changeLayoutOrientation(String orientation) {
        if(this.preferences != null){          
            this.preferences.setValue(ORIENTATION, orientation);
        }
    }

    @objid ("b4013fd8-8908-49dc-a8fd-7ab4e891bc53")
    public String getOrientation() {
        String orientation = ORIENTATION_DEFAULT;
        if(this.preferences != null && this.preferences.contains(ORIENTATION)) {
            orientation = this.preferences.getString(ORIENTATION);
        }
        return orientation;
    }

    @objid ("a8e4f452-d0cb-45c9-bc70-a0ea2063d375")
    private String getDependencyFilterList() {
        if(this.preferences != null && this.preferences.contains(DEPENDENCY_FILTER_LIST)){
            return this.preferences.getString(DEPENDENCY_FILTER_LIST);
        }
        return "";
    }

    @objid ("3efd80e1-4008-47e0-9616-ceeaf0202b7e")
    public void setDependencyFilterList(String value) {
        if(this.preferences != null){
            this.preferences.setValue(DEPENDENCY_FILTER_LIST, value);
        }
    }

}
