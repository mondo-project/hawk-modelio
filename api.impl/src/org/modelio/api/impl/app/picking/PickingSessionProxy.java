package org.modelio.api.impl.app.picking;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.app.picking.IPickingSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("1d2cb330-686d-45a9-9ea8-65169b291ffb")
public class PickingSessionProxy implements IPickingSession {
    @objid ("49e93060-2f7c-4162-8092-30827ac8ef70")
    private org.modelio.app.core.picking.IPickingSession session;

    @objid ("25196193-1073-4148-8df4-70daa21354fd")
    public PickingSessionProxy(org.modelio.app.core.picking.IPickingSession session) {
        this.session = session;
    }

    @objid ("18ae9aa8-a6e9-4a90-8aaa-d312a9f00faf")
    @Override
    public boolean hoverElement(MObject target) {
        return this.session.hover(target);
    }

    @objid ("afe95326-48e9-4225-a137-b2ee8fcfd0ed")
    @Override
    public void selectElement(MObject target) {
        this.session.pick(target);
    }

    @objid ("5f0fac2d-7d2e-4a20-841a-dff4c0dda234")
    public org.modelio.app.core.picking.IPickingSession getSession() {
        return this.session;
    }

}
