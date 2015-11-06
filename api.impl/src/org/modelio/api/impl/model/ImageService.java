package org.modelio.api.impl.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.model.IImageService;
import org.modelio.api.module.IPeerModule;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * This class implements the API to get the image of a model element.
 */
@objid ("cd3f4d4a-a4b4-4785-af85-0037f7ba6d09")
public class ImageService implements IImageService {
    /**
     * Get the stereotyped image of an element.
     * <p>
     * The returned image depends on the filter:
     * <ul>
     * <li>if filter is not <code>null</code>, the first stereotype belonging to that module having an image is used. <br>
     * If no such stereotype is found, the method returns <code>null</code></li>
     * <li>otherwise, the first stereotype with an image is used. <br>
     * If no such stereotype is found, the method returns the UML image for this element.</li>
     * </ul>
     * @param element the element for which the image must be returned.
     * @param filter the module to use as filter.
     * @param useCmsDecoration if <code>true</code> the image will be decorated with the CMS state.
     * @return the image or <code>null</code>.
     */
    @objid ("7f20b490-605f-453c-9376-03391306366f")
    @Override
    public Image getStereotypedImage(final MObject element, final IPeerModule filter, final boolean useCmsDecoration) {
        return ElementImageService.getIcon(element);
    }

    /**
     * Get the UML standard image of an model object.
     * <p>
     * This method return the standard image of the model object ignoring the stereotypes owned by the object.
     * <p>
     * The returned image can be decorated with CMS state image depending on the <code>useCmsDecoration</code>
     * parameter.
     * @param element the model object for which the image must be displayed.
     * @param useCmsDecoration true if the image must be decorated with the CMS state, false otherwise.
     * @return the image corresponding to the model element.
     */
    @objid ("7783b96e-8086-4302-8b99-0d96ba6a6ba4")
    @Override
    public Image getUmlImage(final MObject element, final boolean useCmsDecoration) {
        return MetamodelImageService.getIcon(element.getMClass());
    }

    /**
     * Get the small icon (16x16 pixels) representing the given metaclass.
     * @param metaclass A metamodel metaclass.
     * @return The representing icon.
     */
    @objid ("f1537e43-1a44-4f07-9da5-687e2bacd67d")
    @Override
    public Image getMetaclassImage(final Class<? extends MObject> metaclass) {
        return MetamodelImageService.getIcon(SmClass.getClass(metaclass));
    }

}
