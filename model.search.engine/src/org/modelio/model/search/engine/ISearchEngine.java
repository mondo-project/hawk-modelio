package org.modelio.model.search.engine;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("6ca4945d-ce55-454e-a518-cd3d4f78d1cb")
public interface ISearchEngine {
    @objid ("af530e90-7d11-4e95-bbda-64e0dd896b12")
    List<Element> search(ICoreSession session, ISearchCriteria criteria);

}
