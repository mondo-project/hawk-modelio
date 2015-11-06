package org.modelio.api.impl.model.change;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.model.change.IElementDeletedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This event is used to define a model deletion.
 * <p><p>
 * A deleted element is defined by the following information:
 * <ul>
 * <li>The destroyed element ({@link #getDeletedElement()})</li>
 * <li>The old parent element ({@link #getOldParent()})</li>
 * </ul>
 * <p><p>
 */
@objid ("15eb12bb-081d-4dc3-88e4-27025aaf6e0d")
public class ModelDeleteEvent implements IElementDeletedEvent {
    @objid ("1ebe82e7-0729-4bb1-8a07-b97c906e0516")
    private org.modelio.vcore.session.api.model.change.IElementDeletedEvent coreElementDeletedEvent;

    /**
     * @exclude
     */
    @objid ("0ccdbbf2-80ee-4a4c-a766-719521397435")
    public ModelDeleteEvent(org.modelio.vcore.session.api.model.change.IElementDeletedEvent coreElementDeletedEvent) {
        this.coreElementDeletedEvent = coreElementDeletedEvent;
    }

    /**
     * @exclude
     */
    @objid ("fbb571e8-6120-4b12-9f0a-b9f492c9c580")
    @Override
    public String toString() {
        return this.coreElementDeletedEvent.toString();
    }

    /**
     * Used to get the destroyed element.
     * @return Returns the deleted element.
     */
    @objid ("b2afcb2d-7fda-4b93-aa71-311819c623f1")
    @Override
    public MObject getDeletedElement() {
        return this.coreElementDeletedEvent.getDeletedElement();
    }

    /**
     * Used to get the destroyed element parent.
     * <p><p>
     * The parent corresponds to the element accessed through the use of the
     * {@link Element#getCompositionOwner()} method.
     * @return Returns the old parent.
     */
    @objid ("73f9df5d-75bd-4344-ad65-f98cb538639e")
    @Override
    public MObject getOldParent() {
        return this.coreElementDeletedEvent.getOldParent();
    }

}
