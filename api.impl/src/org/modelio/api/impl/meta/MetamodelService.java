package org.modelio.api.impl.meta;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.meta.IMetamodelService;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

@objid ("bd05b9fd-c038-4e22-bc58-45428ddd2ccf")
public class MetamodelService implements IMetamodelService {
    /**
     * Returns the textual name of a metaclass.<br>
     * <em>Note: The returned name is <u>NOT</u> i18n'd.</em>
     * @param metaclassName the metaclass whose name is sought.
     * @return the textual name of the metaclass. Might be <code>null</code> if no metaclass matches.
     */
    @objid ("f86306c6-33de-466b-9bd8-6854f3af3a54")
    @Override
    public Class<? extends MObject> getMetaclass(final String metaclassName) {
        SmClass smClass = SmClass.getClass(metaclassName);
        if (smClass != null) {
            return smClass.getJavaInterface();
        }
        return null;
    }

    /**
     * Returns the textual name of a metaclass.<br>
     * <em>Note: The returned name is <u>NOT</u> i18n'd.</em>
     * @param metaclass the metaclass whose name is sought, or <code>null</code> if the given class is not a metaclass.
     * @return the textual name of the metaclass.
     */
    @objid ("7becb93f-7a41-4d9b-bb38-c734451a41d5")
    @Override
    public String getMetaclassName(final Class<? extends MObject> metaclass) {
        return SmClass.getClass(metaclass).getName();
    }

    /**
     * Get the metaclasses that inherit from the given metaclass.
     * <p>
     * The given metaclass will in the result list.
     * @param metaclass The parent metaclass of the wanted metaclasses.
     * @return A list of metaclasses that inherit from the given metaclass.
     */
    @objid ("7ea438fc-d17d-411d-959d-103735c97d3e")
    @Override
    public List<Class<? extends MObject>> getInheritingMetaclasses(final Class<? extends MObject> metaclass) {
        List<Class<? extends MObject>> ret = new ArrayList<>();
        
        SmClass smClass = SmClass.getClass(metaclass);
        for (MClass childMClass : smClass.getSub(true)) {
            ret.add(SmClass.getClass(childMClass.getName()).getJavaInterface());
        }
        return ret;
    }

}
