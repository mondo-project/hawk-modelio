package org.modelio.api.impl.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.modelio.api.editor.IExternDocumentChangeListener;
import org.modelio.editors.richnote.editor.IRichNoteEditor;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("1870008a-fc13-4ad2-a170-6e08cd98d9f4")
public class MdaRichNoteEditor implements IRichNoteEditor {
    @objid ("d8d341ab-7f75-4b31-9a15-224b74c40824")
    private IExternDocumentChangeListener editor;

    @objid ("1d36b8f1-2b4c-46d1-8238-fe73297b385b")
    private ExternDocument doc;

    @objid ("08d84219-45fb-416c-9fba-cda969bcdb42")
    public MdaRichNoteEditor(ExternDocument doc, final IExternDocumentChangeListener editor) {
        this.editor = editor;
        this.doc = doc;
    }

    @objid ("f6570646-9200-4ac1-a0bc-356d3dcb863f")
    @Override
    public void onOriginalModified(MObject model) {
        this.editor.onOriginalModified(model);
    }

    @objid ("a6b9a659-bcb5-45dd-8cb3-fb1bd3642117")
    @Override
    public void onOriginalDeleted(MObject model) {
        this.editor.onOriginalDeleted(model);
    }

    @objid ("2e68d820-8ee3-47b8-b28a-5152c11bfd87")
    @Override
    public MPart getMPart() {
        return null;
    }

    @objid ("338803ba-cfc6-4b7e-939a-57db6424ecc8")
    @Override
    public void disposeResources() {
        // Nothing to dispose
    }

    @objid ("94bf24fb-da2a-4612-be7f-15cedfde17f6")
    public ExternDocument getDoc() {
        return this.doc;
    }

    @objid ("4320b3cd-a346-4e9f-abb2-1a88ee5a850e")
    public IExternDocumentChangeListener getEditor() {
        return this.editor;
    }

}
