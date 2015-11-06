package org.modelio.api.impl.model;

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.model.IUMLTypes;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.vcore.session.api.model.IModel;

/**
 * This class give access to UML types managed by Modelio.
 * 
 * <p>
 * The accessible types are boolean, char, integer, real, string, undefined.
 * </p>
 * 
 * <p>
 * undefined type is used to set a type to property when the real type of the property is not known. (A property should not be left
 * without type)
 * </p>
 * 
 * <p>
 * Exemple:
 * </p>
 * <code>
 * UMLModel model = Modelio.getInstance().getModelingSession().getModel();<br>
 * DataType type = model.getUmlTypes().BOOLEAN;
 * </code>
 */
@objid ("a479c418-2555-435b-988e-8c32a6fba116")
public class UMLTypes implements IUMLTypes {
    @objid ("d63fda90-01bd-4a4a-ab3d-705c26bb150d")
    private IModel model;

    @objid ("5db2e093-c984-4026-b33a-2bf1597065b0")
    UMLTypes(IModel model) {
        this.model = model;
    }

    @objid ("74683d8c-343e-46eb-80a6-85ac9f063315")
    @Override
    public DataType getBOOLEAN() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0005-0000-000000000000"));
    }

    @objid ("69c1603b-afc7-43e1-bcde-e70ea3f24a99")
    @Override
    public DataType getBYTE() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0013-0000-000000000000"));
    }

    @objid ("21c3ef72-7578-4fe4-9a84-2730931975fd")
    @Override
    public DataType getCHAR() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0007-0000-000000000000"));
    }

    @objid ("39681b1c-01b1-4c0d-a5ef-8341426df674")
    @Override
    public DataType getDATE() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0014-0000-000000000000"));
    }

    @objid ("ec0aa747-28b2-4718-a175-dd3d09e18aaa")
    @Override
    public DataType getDOUBLE() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0010-0000-000000000000"));
    }

    @objid ("42b810a4-1b8f-4426-8a84-28e8b0229509")
    @Override
    public DataType getFLOAT() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-000b-0000-000000000000"));
    }

    @objid ("6eb87156-ca9b-471a-b0d9-e7ec04a05f97")
    @Override
    public DataType getINTEGER() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0009-0000-000000000000"));
    }

    @objid ("10515ffe-2605-4a19-8b5d-351eab2fb18c")
    @Override
    public DataType getLONG() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0011-0000-000000000000"));
    }

    @objid ("2045a38c-c39b-454a-acde-a081d1872b7c")
    @Override
    public DataType getSHORT() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-0012-0000-000000000000"));
    }

    @objid ("c8a911bd-5d88-40ce-b8e2-679c5260e6cd")
    @Override
    public DataType getSTRING() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-000d-0000-000000000000"));
    }

    @objid ("f4b757c1-fee2-4285-aa8b-b61cf554cde6")
    @Override
    public DataType getUNDEFINED() {
        return this.model.findById(DataType.class, UUID.fromString("00000004-0000-000f-0000-000000000000"));
    }

}
