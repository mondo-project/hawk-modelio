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
                                    

package org.modelio.xmi.util;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.internal.impl.ModelImpl;
import org.eclipse.uml2.uml.internal.impl.PrimitiveTypeImpl;
import org.modelio.api.model.IUMLTypes;
import org.modelio.api.modelio.Modelio;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.xmi.api.FormatExport;

/**
 * This class made the mapping between Modelio predefined type and Ecore org.eclipse.uml2.uml.DataType
 * @author ebrosse
 */
@objid ("c95fbb47-4279-43e8-9246-e58e1eea8df2")
public class PrimitiveTypeMapper {
    @objid ("9737a4ec-7ae5-4296-97f6-902da70b9161")
    private static final String booleanEcoreName = "Boolean";

    @objid ("952c3a04-ffc4-471b-8a04-762e207b7fb5")
    private static final String byteEcoreName = "EByte";

    @objid ("201b22c2-3d35-4c4b-aacd-e908d6395572")
    private static final String charEcoreName = "EChar";

    @objid ("72882c99-8b24-4c8a-99d7-d4f6d8e00c00")
    private static final String dateEcoreName = "EDate";

    @objid ("313eafe8-9704-4f29-b6cf-4940dbf14f5a")
    private static final String doubleEcoreName = "EDouble";

    @objid ("02eb97ce-ca85-4e18-847c-1c1ab96622fc")
    private static final String floatEcoreName = "EFloat";

    @objid ("1c315c73-caa1-4a06-942f-0737c265e0dc")
    private static final String integerEcoreName = "Integer";

    @objid ("b2dc5b5d-ca1c-4c44-a0a9-9867312243a9")
    private static final String longEcoreName = "ELong";

    @objid ("9ef70033-a28d-4e4d-b521-d0e162574fc4")
    private static final String shortEcoreName = "EShort";

    @objid ("20649c74-1e3f-4608-821a-0fac82d7b801")
    private static final String stringEcoreName = "String";

    @objid ("47fc370e-7906-46d9-a95a-aa0bfd4ecf28")
    private static final String unlimitedNaturalName = "UnlimitedNatural";

    @objid ("ea81f12d-fe85-4953-9cf7-a6e2ecb953a2")
    private static final String eStringEcoreName = "EString";

    @objid ("652a94e7-9949-418a-885d-5c013fc36be1")
    private static List<org.eclipse.uml2.uml.PrimitiveType> predefinedType = new ArrayList<>();

    @objid ("2f6ac003-73f4-449b-a322-742e2e1d9553")
    private static org.eclipse.uml2.uml.PrimitiveType BYTE = null;

    @objid ("e836709e-8f2c-43b7-b6f6-69c5adeedec1")
    private static org.eclipse.uml2.uml.PrimitiveType CHAR = null;

    @objid ("df3028d3-2308-4fff-8a06-f23f64f4b197")
    private static org.eclipse.uml2.uml.PrimitiveType DATE = null;

    @objid ("fd7a0c02-f4dd-4ca1-8852-173e81858a14")
    private static org.eclipse.uml2.uml.PrimitiveType DOUBLE = null;

    @objid ("c268c1d4-12ca-4182-803c-5b21f3eaf860")
    private static org.eclipse.uml2.uml.PrimitiveType FLOAT = null;

    @objid ("b1ec4208-51f6-437c-b0cb-80807ceca4bb")
    private static org.eclipse.uml2.uml.PrimitiveType LONG = null;

    @objid ("19050c46-42ff-4007-94b4-963605c24aa8")
    private static org.eclipse.uml2.uml.PrimitiveType SHORT = null;

    @objid ("345156ef-4d29-4bc0-b38d-999c4219140d")
    private static org.eclipse.uml2.uml.PrimitiveType UNDEFINED = null;

    @objid ("c23dc734-9c2c-49e9-9645-ca43ed54375e")
    private static org.eclipse.uml2.uml.PrimitiveType BOOLEAN = null;

    @objid ("e2801d2a-b7ed-440b-8418-f2d6d3307d34")
    private static org.eclipse.uml2.uml.PrimitiveType STRING = null;

    @objid ("53390db0-c195-45e1-8f53-55a0ecb7ac83")
    private static org.eclipse.uml2.uml.PrimitiveType INTEGER = null;

    @objid ("1c2de596-f2fd-4ea3-9089-437009856f8f")
    private static org.eclipse.uml2.uml.PrimitiveType UNLIMITED = null;

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Byte Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Byte' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("db07f430-b3cf-4902-934b-6650b56b4350")
    public static org.eclipse.uml2.uml.PrimitiveType getByte() {
        if (BYTE == null){
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                BYTE = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(byteEcoreName);
            }else{
                BYTE = UMLFactory.eINSTANCE.createPrimitiveType();
                BYTE.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getBYTE().getName());
                predefinedType.add(BYTE);
            }
        
        }
        return BYTE;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Char Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Char' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("9c7385d9-ef96-4cad-a327-d5828d75b558")
    public static org.eclipse.uml2.uml.PrimitiveType getChar() {
        if (CHAR == null){
        
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                CHAR = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(charEcoreName);
            }else{
                CHAR = UMLFactory.eINSTANCE.createPrimitiveType();
                CHAR.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getCHAR().getName());
                predefinedType.add(CHAR);
            }
        }
        return CHAR;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Date Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Date' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("e4f970c2-75bb-4cf2-bb0c-eee80c70ed01")
    public static org.eclipse.uml2.uml.PrimitiveType getDate() {
        if (DATE == null){
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                DATE = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(dateEcoreName);
            }else{
                DATE = UMLFactory.eINSTANCE.createPrimitiveType();
                DATE.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getDATE().getName());
                predefinedType.add(DATE);
            }
        }
        return DATE;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Double Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Double' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("01ddc61a-fc66-48bd-aa16-6e9c3029da6a")
    public static org.eclipse.uml2.uml.PrimitiveType getDouble() {
        if (DOUBLE == null){
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                DOUBLE = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(doubleEcoreName);
            }else{
                DOUBLE = UMLFactory.eINSTANCE.createPrimitiveType();
                DOUBLE.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getDOUBLE().getName());
                predefinedType.add(DOUBLE);
            }
        }
        return DOUBLE;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Float Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Float' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("67473aee-9a8e-4c2a-9352-eba81a7dc2c7")
    public static org.eclipse.uml2.uml.PrimitiveType getFloat() {
        if (FLOAT == null){
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                FLOAT = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(floatEcoreName);
            }else{
                FLOAT = UMLFactory.eINSTANCE.createPrimitiveType();
                FLOAT.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getFLOAT().getName());
                predefinedType.add(FLOAT);
            }
        }
        return FLOAT;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Long Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Long' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("5950840e-d409-4079-a796-70b5903e1c8e")
    public static org.eclipse.uml2.uml.PrimitiveType getLong() {
        if (LONG == null){
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                LONG = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(longEcoreName);
            }else{
                LONG = UMLFactory.eINSTANCE.createPrimitiveType();
                LONG.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getLONG().getName());
                predefinedType.add(LONG);
            }
        }
        return LONG;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the String Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'String' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("05cfa059-696c-4710-97b8-875e02279080")
    public static org.eclipse.uml2.uml.PrimitiveType getString() {
        if (STRING == null){        
            STRING = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getUMLLibrary().getOwnedType(stringEcoreName);
        }
        return STRING;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Boolean Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Boolean' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("a070dc3e-82a5-4c32-b1f2-4fea1d490c64")
    public static org.eclipse.uml2.uml.PrimitiveType getBoolean() {
        if (BOOLEAN == null){
            BOOLEAN = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getUMLLibrary().getOwnedType(booleanEcoreName);
        }
        return BOOLEAN;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Integer Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Integer' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("807a7431-bcf6-406f-af1d-d03c2d7322d8")
    public static org.eclipse.uml2.uml.PrimitiveType getInteger() {
        if (INTEGER == null){
            INTEGER = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getUMLLibrary().getOwnedType(integerEcoreName);
        }
        return INTEGER;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Unlimited Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Unlimited' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("f3c7fba2-57bb-46d4-bbf0-af6cccf94686")
    public static org.eclipse.uml2.uml.PrimitiveType getUnlimited() {
        if (UNLIMITED == null){
            UNLIMITED = (org.eclipse.uml2.uml.PrimitiveType ) UMLMetamodel.getInstance().getUMLLibrary().getOwnedType(unlimitedNaturalName);
        }
        return UNLIMITED;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Short Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Short' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("09d854ea-9f2a-484e-9d0b-5736fca74b1b")
    public static org.eclipse.uml2.uml.PrimitiveType getShort() {
        if (SHORT == null){
            if (GenerationProperties.getInstance().getExportVersion().equals(FormatExport.EMF300)){
                SHORT = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(shortEcoreName);
            }else{
                SHORT = UMLFactory.eINSTANCE.createPrimitiveType();
                SHORT.setName(Modelio.getInstance().getModelingSession()
                        .getModel().getUmlTypes().getSHORT().getName());
                predefinedType.add(SHORT);
            }
        }
        return SHORT;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the Undefined Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'Undefined' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("d92d6b3e-e3e9-4546-b4c7-e95edfcd3d3e")
    public static org.eclipse.uml2.uml.PrimitiveType getUndefined() {
        if (UNDEFINED == null){
            UNDEFINED = UMLFactory.eINSTANCE.createPrimitiveType();
            UNDEFINED.setName(Modelio.getInstance().getModelingSession()
                    .getModel().getUmlTypes().getUNDEFINED().getName());
            predefinedType.add(UNDEFINED);
        }
        return UNDEFINED;
    }

    /**
     * This method returns a list containing all Ecore Predefined org.eclipse.uml2.uml.Type
     * @return the list of all Ecore Predefined org.eclipse.uml2.uml.Type
     */
    @objid ("f92842c3-3787-4912-b382-76db01c42341")
    public static List<org.eclipse.uml2.uml.PrimitiveType> getPredifinedTypeList() {
        return predefinedType;
    }

    /**
     * This method returns the Ecore org.eclipse.uml2.uml.Type corresponding to a given Modelio Predefined org.eclipse.uml2.uml.Type
     * @param objingPredefinedType : the given Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the matching Ecore org.eclipse.uml2.uml.Type
     */
    @objid ("911b792f-5c8d-4d8f-83a2-42e2f813117c")
    public static org.eclipse.uml2.uml.Type getEcoreType(DataType objingPredefinedType) {
        IUMLTypes umlTypes = Modelio.getInstance().getModelingSession().getModel().getUmlTypes();
        
        org.eclipse.uml2.uml.PrimitiveType primitiveType = null;
        
        if ((umlTypes.getBOOLEAN() != null) && umlTypes.getBOOLEAN().equals(objingPredefinedType)) {
            primitiveType = getBoolean();
        }else if ((umlTypes.getBYTE() != null) &&  umlTypes.getBYTE().equals(objingPredefinedType)) {
            primitiveType = getByte();
        } else if ((umlTypes.getCHAR() != null) &&  umlTypes.getCHAR().equals(objingPredefinedType)) {
            primitiveType = getChar();
        }else if ((umlTypes.getDOUBLE() != null) &&  umlTypes.getDOUBLE().equals(objingPredefinedType)) {
            primitiveType = getDouble();
        }else if ((umlTypes.getFLOAT() != null) &&  umlTypes.getFLOAT().equals(objingPredefinedType)) {
            primitiveType = getFloat();
        }else if ((umlTypes.getLONG() != null) &&  umlTypes.getLONG().equals(objingPredefinedType)) {
            primitiveType = getLong();
        }else if ((umlTypes.getSHORT() != null) &&  umlTypes.getSHORT().equals(objingPredefinedType)) {
            primitiveType = getShort();
        } else if ((umlTypes.getINTEGER() != null) &&  umlTypes.getINTEGER().equals(objingPredefinedType)){
            primitiveType = getInteger();
        }else if ((umlTypes.getDATE() != null) &&  umlTypes.getDATE().equals(objingPredefinedType)) {
            primitiveType = getDate();
        } else if ((umlTypes.getSTRING() != null) &&  umlTypes.getSTRING().equals(objingPredefinedType)){
            primitiveType = getString();
        }else if ((umlTypes.getUNDEFINED() != null) && umlTypes.getUNDEFINED().equals(objingPredefinedType)) {      
            primitiveType = getUndefined();
        }
        return primitiveType;
    }

    /**
     * This method types an Ecore org.eclipse.uml2.uml.TypedElement by a given Modelio org.eclipse.uml2.uml.DataType
     * @param typed : the Ecore org.eclipse.uml2.uml.TypedElement
     * @param objingPredefinedType : the Modelio org.eclipse.uml2.uml.DataType
     */
    @objid ("474628a9-2337-4d2f-ba3f-72db09d570a3")
    public static void setEcorePredefinedType(org.eclipse.uml2.uml.TypedElement typed, DataType objingPredefinedType) {
        IUMLTypes umlTypes = Modelio.getInstance().getModelingSession()
                .getModel().getUmlTypes();
        
        org.eclipse.uml2.uml.PrimitiveType primitiveType = null;
        
        if ((umlTypes.getBOOLEAN() != null) && umlTypes.getBOOLEAN().equals(objingPredefinedType)) {
            primitiveType = getBoolean();
        }else if ((umlTypes.getBYTE() != null) &&  umlTypes.getBYTE().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "byte"
            primitiveType = getByte();
        } else if ((umlTypes.getCHAR() != null) &&  umlTypes.getCHAR().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "char"
            primitiveType = getChar();
        }else if ((umlTypes.getDOUBLE() != null) &&  umlTypes.getDOUBLE().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "double"
            primitiveType = getDouble();
        }else if ((umlTypes.getFLOAT() != null) &&  umlTypes.getFLOAT().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "float"
            primitiveType = getFloat();
        }else if ((umlTypes.getLONG() != null) &&  umlTypes.getLONG().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "long"
            primitiveType = getLong();
        }else if ((umlTypes.getSHORT() != null) &&  umlTypes.getSHORT().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "short"
            primitiveType = getShort();
        } else if ((umlTypes.getINTEGER() != null) &&  umlTypes.getINTEGER().equals(objingPredefinedType)){
            primitiveType = getInteger();
        }else if ((umlTypes.getDATE() != null) &&  umlTypes.getDATE().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "date"
            primitiveType = getDate();
        
        } else if ((umlTypes.getSTRING() != null) &&  umlTypes.getSTRING().equals(objingPredefinedType))
            primitiveType = getString();
        else if ((umlTypes.getUNDEFINED() != null) && umlTypes.getUNDEFINED().equals(objingPredefinedType)) {
            // Stores the original Modelio predefined type: "undefined"
            primitiveType = getUndefined();
        }
        
        if (primitiveType != null)
            typed.setType(primitiveType);
    }

    /**
     * Suppress all Primitive org.eclipse.uml2.uml.Type matching done in a previous export
     */
    @objid ("115e0f46-5224-4770-8893-d09f501c8c35")
    public static void clean() {
        BYTE = null;
        CHAR = null;
        DATE = null;
        DOUBLE = null;
        FLOAT = null;
        LONG = null;
        SHORT = null;
        UNDEFINED = null;
        BOOLEAN = null;
        STRING = null;
        INTEGER = null;
        UNLIMITED = null;
        predefinedType = new ArrayList<>();
    }

    /**
     * Test if a given GeneralClass is a Modelio Predefined org.eclipse.uml2.uml.Type
     * @param type : the tested GeneralClass
     * @return true if it is a Modelio Predefined org.eclipse.uml2.uml.Type
     */
    @objid ("7ac3cdb5-362c-4f82-b794-c1b9a22fdbfc")
    public static boolean isPredefinedType(final GeneralClass type) {
        if (type != null) {
        
            IUMLTypes umlTypes = Modelio.getInstance().getModelingSession()
                    .getModel().getUmlTypes();
        
            if ((umlTypes.getBOOLEAN() != null) && (type.equals(umlTypes.getBOOLEAN())))
                return true;
            else if  ((umlTypes.getCHAR() != null) &&(type.equals(umlTypes.getCHAR())))
                return true;
            else if ((umlTypes.getINTEGER() != null) && (type.equals(umlTypes.getINTEGER())))
                return true;
            else if ((umlTypes.getBYTE() != null) &&(type.equals(umlTypes.getBYTE())))
                return true;
            else if ((umlTypes.getDATE() != null) &&(type.equals(umlTypes.getDATE())))
                return true;
            else if ((umlTypes.getDOUBLE()!= null) &&(type.equals(umlTypes.getDOUBLE())))
                return true;
            else if ((umlTypes.getFLOAT() != null) &&(type.equals(umlTypes.getFLOAT())))
                return true;
            else if ((umlTypes.getLONG() != null) &&(type.equals(umlTypes.getLONG())))
                return true;
            else if ((umlTypes.getSHORT() != null) &&(type.equals(umlTypes.getSHORT())))
                return true;
            else if ((umlTypes.getSTRING() != null) &&(type.equals(umlTypes.getSTRING())))
                return true;
            else if ((umlTypes.getUNDEFINED() != null) &&(type.equals(umlTypes.getUNDEFINED())))
                return true;
        }
        return false;
    }

    /**
     * provide the Modelio org.eclipse.uml2.uml.DataType cooresponding to a given Ecore org.eclipse.uml2.uml.Type
     * @param ecoreType : the given Ecore org.eclipse.uml2.uml.Type
     * @return the corresponding Modelio org.eclipse.uml2.uml.DataType
     */
    @objid ("fa5efca7-fdad-45b1-b130-7c8e5126a563")
    public static DataType getPredefinedType(final org.eclipse.uml2.uml.Type ecoreType) {
        //UMLPredefined type
        
        IUMLTypes umlTypes = Modelio.getInstance().getModelingSession().getModel().getUmlTypes();
        
        //Compute primitiveType name
        String currentEcoreTypeName = ecoreType.getName();
        
        if (currentEcoreTypeName == null) {
            if (ecoreType instanceof PrimitiveTypeImpl){
                currentEcoreTypeName = ((PrimitiveTypeImpl) ecoreType).eProxyURI().fragment();
            }else
                currentEcoreTypeName = ecoreType.eResource().getURI().fragment();
        }
        
        if ((ecoreType.eContainer() instanceof ModelImpl) 
                && ((ModelImpl) ecoreType.eContainer()).getName().equals("EcorePrimitiveTypes")){
            if (currentEcoreTypeName.equals(byteEcoreName)){
                return umlTypes.getBYTE();
            }else if (currentEcoreTypeName.equals(dateEcoreName)){
                return umlTypes.getDATE();
            }else if (currentEcoreTypeName.equals(doubleEcoreName)){
                return umlTypes.getDOUBLE();
            }else if (currentEcoreTypeName.equals(floatEcoreName)){
                return umlTypes.getFLOAT();
            }else if (currentEcoreTypeName.equals(longEcoreName)){
                return umlTypes.getLONG();
            }else if (currentEcoreTypeName.equals(shortEcoreName)){
                return umlTypes.getSHORT();
            }else if (currentEcoreTypeName.equals(charEcoreName)){
                return umlTypes.getCHAR();
            }else if (currentEcoreTypeName.equals(eStringEcoreName)){
                return umlTypes.getSTRING();
            }
        }
        
        if (currentEcoreTypeName != null) {
            currentEcoreTypeName = currentEcoreTypeName.toLowerCase();
            if (currentEcoreTypeName.equals(umlTypes.getINTEGER().getName())){
                return umlTypes.getINTEGER();
            }else if (currentEcoreTypeName.equals(umlTypes.getBOOLEAN().getName())){
                return umlTypes.getBOOLEAN();
            }else if (currentEcoreTypeName.toLowerCase().equals(umlTypes.getSTRING().getName())){
                return umlTypes.getSTRING();
            }else if (currentEcoreTypeName.equals(umlTypes.getBYTE().getName())){
                return umlTypes.getBYTE();
            }else if (currentEcoreTypeName.equals(unlimitedNaturalName)){
                return umlTypes.getINTEGER();
            }else if (currentEcoreTypeName.equals(umlTypes.getDATE().getName())){
                return umlTypes.getDATE();
            }else if (currentEcoreTypeName.equals(umlTypes.getDOUBLE().getName())){
                return umlTypes.getDOUBLE();
            }else if ((currentEcoreTypeName.equals(umlTypes.getFLOAT().getName()))){
                return umlTypes.getFLOAT();
            }else if (currentEcoreTypeName.equals(umlTypes.getLONG().getName())){
                return umlTypes.getLONG();
            }else if (currentEcoreTypeName.equals(umlTypes.getSTRING().getName())){
                return umlTypes.getSTRING();
            }else if (currentEcoreTypeName.equals(umlTypes.getSHORT().getName())){
                return umlTypes.getSHORT();
            }else if (currentEcoreTypeName.equals(umlTypes.getCHAR().getName())){
                return umlTypes.getCHAR();
            }else if (currentEcoreTypeName.equals(umlTypes.getUNDEFINED().getName())){
                return umlTypes.getUNDEFINED();
            }
        }
        return null;
    }

    /**
     * Test if a given Ecore org.eclipse.uml2.uml.Type is a Modelio Predefined org.eclipse.uml2.uml.Type
     * @param ecoreType : the tested Ecore org.eclipse.uml2.uml.Type
     * @return true if the Ecore org.eclipse.uml2.uml.Type corresponds to a Modelio Predefined org.eclipse.uml2.uml.Type
     */
    @objid ("ad653adc-02cd-4ba0-8dc3-58a3c958013f")
    public static boolean isPredefinedType(final org.eclipse.uml2.uml.Type ecoreType) {
        String ecoreTypeName = ecoreType.getName();
        
        String ecoreContainerName = "";
        
        if (ecoreType.getPackage() != null)
            ecoreContainerName = ecoreType.getPackage().getName();
        
        if (ecoreTypeName == null) {
            if (ecoreType instanceof PrimitiveTypeImpl){
                ecoreTypeName = ((PrimitiveTypeImpl) ecoreType).eProxyURI().fragment();
                ecoreContainerName = ((PrimitiveTypeImpl) ecoreType).eProxyURI().lastSegment();
            }else if (ecoreType.eResource() != null)
                ecoreTypeName = ecoreType.eResource().getURI().fragment();
        }
        
        if (ecoreTypeName != null) {
        
            if (ecoreContainerName.contains("EcorePrimitiveTypes")){
                if ((ecoreTypeName.equals(byteEcoreName)) 
                        || (ecoreTypeName.equals(dateEcoreName))
                        || (ecoreTypeName.equals(doubleEcoreName))
                        || (ecoreTypeName.equals(floatEcoreName)) 
                        || (ecoreTypeName.equals(longEcoreName))
                        || (ecoreTypeName.equals(shortEcoreName))
                        || (ecoreTypeName.equals(charEcoreName))
                        || (ecoreTypeName.equals(eStringEcoreName))){
                    return true;
                }
            }
        
            IUMLTypes umlTypes = Modelio.getInstance().getModelingSession()
                    .getModel().getUmlTypes();
        
            ecoreTypeName = ecoreTypeName.toLowerCase();
        
        
            return  (((ecoreType instanceof org.eclipse.uml2.uml.PrimitiveType) || (ecoreType instanceof org.eclipse.uml2.uml.DataType) ) 
                    && ((ecoreTypeName.equals(umlTypes.getBOOLEAN().getName()))
                    || (ecoreTypeName.equals(umlTypes.getBYTE().getName()))
                    || (ecoreTypeName.equals(umlTypes.getDATE().getName()))
                    || (ecoreTypeName.equals(umlTypes.getDOUBLE().getName()))
                    || (ecoreTypeName.equals(umlTypes.getFLOAT().getName()))
                    || (ecoreTypeName.equals(umlTypes.getLONG().getName()))
                    || (ecoreTypeName.equals(umlTypes.getSHORT().getName()))
                    || (ecoreTypeName.equals(umlTypes.getCHAR().getName()))
                    || (ecoreTypeName.equals(umlTypes.getINTEGER().getName()))
                    || (ecoreTypeName.equals(umlTypes.getSTRING().getName()))
                    || (ecoreTypeName.equals(umlTypes.getUNDEFINED().getName()))
                    || (ecoreTypeName.equals(integerEcoreName))
                    || (ecoreTypeName.equals(booleanEcoreName))
                    || (ecoreTypeName.equals(stringEcoreName))
                    || (ecoreTypeName.equals(unlimitedNaturalName))));
        }
        return false;
    }

    /**
     * @param ecoreType : the tested org.eclipse.uml2.uml.Type
     * @return true if the test org.eclipse.uml2.uml.Type is the Boolean org.eclipse.uml2.uml.Type
     */
    @objid ("9df119ba-8681-438e-a40d-cf2d78191a4c")
    public static boolean isBoolean(final org.eclipse.uml2.uml.Type ecoreType) {
        if (PrimitiveTypeMapper.isPredefinedType(ecoreType)){
            DataType obType = PrimitiveTypeMapper.getPredefinedType(ecoreType);
            return  (obType.equals(Modelio.getInstance().getModelingSession().getModel().getUmlTypes().getBOOLEAN()));
        }
        return false;
    }

    /**
     * This methods return the Ecore org.eclipse.uml2.uml.PrimitiveType associated to the String Modelio Predefined org.eclipse.uml2.uml.Type
     * @return the 'String' Ecore org.eclipse.uml2.uml.PrimitiveType
     */
    @objid ("be15c4e5-7396-465d-962b-e53d003cdc75")
    public static org.eclipse.uml2.uml.PrimitiveType getEString() {
        if (STRING == null){        
            STRING = (org.eclipse.uml2.uml.PrimitiveType) UMLMetamodel.getInstance().getEcoreLibrary().getOwnedType(eStringEcoreName);
        }
        return STRING;
    }

}
