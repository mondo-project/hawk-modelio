package org.modelio.api.impl.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.model.IDefaultNameService;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("c6a85a4e-8e3f-4151-8ea3-32b8e29ce3d7")
public class DefaultNameService implements IDefaultNameService {
    @objid ("f127d0bf-dc35-4882-b3ab-69617dba177b")
    private IElementNamer elementNamer;

    @objid ("d40d2f95-35ff-491d-9e79-1e5a905c5c95")
    public DefaultNameService(IElementNamer elementNamer) {
        this.elementNamer = elementNamer;
    }

    @objid ("2d1bb6b0-d0f4-4ea0-ae36-3e2c747ecad9")
    @Override
    public void setDefaultName(ModelElement element) {
        element.setName(this.elementNamer.getUniqueName(element));
    }

    @objid ("29339318-a227-455b-9ab9-9fdfdf4c09bb")
    @Override
    public void setDefaultName(ModelElement element, String baseName) {
        element.setName(this.elementNamer.getUniqueName(baseName, element));
    }

}
