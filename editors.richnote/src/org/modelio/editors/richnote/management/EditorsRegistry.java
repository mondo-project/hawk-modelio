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
                                    

package org.modelio.editors.richnote.management;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.editors.richnote.editor.IRichNoteEditor;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("b4b38209-1b85-4d0f-9a58-a5bf18a40f9f")
class EditorsRegistry {
    @objid ("98090f82-c0be-459e-bfec-3b7f8e2cb687")
    private Map<String, RichNoteToken> openBlobEditors = new HashMap<>();

    @objid ("a56c6a3a-91bb-45ff-aaad-7812c04813df")
    private Map<MObject, RichNoteToken> openEditors = new HashMap<>();

    @objid ("494b9393-1cab-48e3-b098-bf036659da42")
    EditorsRegistry() {
        // nothing
    }

    @objid ("27f10ff6-b219-414f-a19d-5127e5b40fc6")
    public IRichNoteEditor getEditor(MObject obj) {
        RichNoteToken token = this.openEditors.get(obj);
        return token == null ? null : token.editor;
    }

    @objid ("5246ec8f-00fc-4917-a416-98c066c68d96")
    RichNoteToken getBlobEditor(String blobId) {
        return this.openBlobEditors.get(blobId);
    }

    @objid ("b795be08-9afe-4afd-a9b0-d6129479101b")
    public void addEditor(String blobId, MObject model, IRichNoteEditor editor) {
        RichNoteToken token = new RichNoteToken(model, editor);
        this.openEditors.put(model, token);
        this.openBlobEditors.put(blobId, token);
    }

    @objid ("12dc5faa-0959-435a-a9c1-259f3a45f24c")
    public void removeEditor(IRichNoteEditor editor) {
        for (Iterator<Entry<MObject, RichNoteToken>> it = this.openEditors.entrySet().iterator(); it.hasNext(); ) {
            Entry<MObject, RichNoteToken> entry = it.next();
            if (entry.getValue().editor == editor)
                it.remove();
        }
        
        for (Iterator<Entry<String, RichNoteToken>> it = this.openBlobEditors.entrySet().iterator(); it.hasNext(); ) {
            Entry<String, RichNoteToken> entry = it.next();
            if (entry.getValue().editor == editor)
                it.remove();
        }
    }

    /**
     * @return all editors.
     */
    @objid ("f7f16b85-0abb-45ef-94a0-540786c52cda")
    Collection<RichNoteToken> getAllEditors() {
        return this.openEditors.values();
    }

    /**
     * Empty the whole registry.
     */
    @objid ("35e9e2a3-0ec1-4a5c-aa71-681d27d5d20d")
    public void clear() {
        this.openBlobEditors = new HashMap<>();
        this.openEditors = new HashMap<>();
    }

    @objid ("f4bbc736-efb1-4c0e-a74c-229b7fad51f2")
    static class RichNoteToken {
        @objid ("0360ca70-7e0a-4a99-8530-bfbbb4273d8a")
         MObject model;

        @objid ("332b5992-578b-4991-9750-1ff6db739698")
         IRichNoteEditor editor;

        @objid ("837648cd-cf9e-4ed5-bf58-baec4f1ac42c")
        public RichNoteToken(MObject model, IRichNoteEditor editor) {
            this.model = model;
            this.editor = editor;
        }

    }

}
