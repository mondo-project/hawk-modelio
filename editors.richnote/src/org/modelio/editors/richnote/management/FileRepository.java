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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.editors.richnote.api.RichNoteFormat;
import org.modelio.editors.richnote.editor.IRichNoteEditor;
import org.modelio.editors.richnote.editor.IRichNoteFileRepository;
import org.modelio.editors.richnote.helper.RichNoteFilesGeometry;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.blob.BlobCopier;
import org.modelio.vcore.session.api.blob.BlobInfo;
import org.modelio.vcore.session.api.blob.IBlobInfo;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rich notes files extracted from blobs to a directory in the project runtime directory.
 * <p>
 * Provide services to extract a rich note from blobs to the runtime directory back and forth.
 */
@objid ("a2b9bd65-bc58-4b0c-9909-32b5b1d7336e")
class FileRepository implements IRichNoteFileRepository {
    @objid ("6467e67d-30e4-42f3-a4f9-9523f5cbdc68")
    private ICoreSession session;

    @objid ("aee2cebb-0a19-498c-8d99-602f8d5d2683")
    private RichNoteFilesGeometry geometry;

    @objid ("998817c1-6228-44a8-a038-7ec537e421ba")
    private EditorsRegistry editorsRegistry;

    /**
     * Initialize
     * @param project the project to handle
     * @param editorsRegistry the editors registry.
     */
    @objid ("4d46a9b7-81d1-4a24-8a24-83173f1d32d5")
    public FileRepository(GProject project, EditorsRegistry editorsRegistry) {
        this.session = project.getSession();
        this.geometry = new RichNoteFilesGeometry(project);
        this.editorsRegistry = editorsRegistry;
    }

    @objid ("e3371f5e-234c-41b9-8014-34b334958e08")
    @Override
    public Path openRichNote(ExternDocument doc, IRichNoteEditor editor) throws IOException {
        IRepository repo = this.session.getRepositorySupport().getRepository(doc);
        
        String blobId = getBlobId(doc);
        try (InputStream is = repo.readBlob(blobId);) {
            if (is == null) 
                return null;
            
            Path dest = this.geometry.getPath(doc);
            if (dest == null)
                throw new IllegalArgumentException(doc.toString());
            
            Files.createDirectories(dest.getParent());
            Files.copy(is, dest, StandardCopyOption.REPLACE_EXISTING);
            
            if (editor != null) {
                this.editorsRegistry.addEditor(blobId, doc, editor);
            }
            
            return dest;
        }
    }

    @objid ("33594142-28e6-4dd9-bafc-5822e8b13276")
    private String getBlobId(MObject doc) {
        return "richnote."+doc.getUuid();
    }

    @objid ("ca00b217-6317-4039-8cd3-a5989c33de10")
    @Override
    public Path getArtifactFile(Artifact art) {
        return this.geometry.getPath(art);
    }

    @objid ("04bf795f-596e-42ab-a56d-b76b100b2765")
    @Override
    public Path getNewRichNotePath(ExternDocument doc, RichNoteFormat format) {
        return this.geometry.getDefaultPath(doc, format.getFileExtensions().iterator().next());
    }

    @objid ("1f8eae59-a8f1-409d-b764-37b7dc9c363e")
    @Override
    public void saveRichNote(ExternDocument doc, Path fileToSave) throws IOException {
        IRepository repo = this.session.getRepositorySupport().getRepository(doc);
        String label = this.geometry.getRelativePath(fileToSave);
        try (OutputStream os = repo.writeBlob(new BlobInfo(getBlobId(doc), label))) {
            Files.copy(fileToSave, os);
        }
        
        // Touch the model element so that it is included in SVN commit
        String name = doc.getName();
        try (ITransaction t= this.session.getTransactionSupport().createTransaction("touch "+name)) {
            doc.setName("");
            doc.setName(name);
            t.disableUndo();
            t.commit();
        }
    }

    /**
     * Get the rich note blob for the given model object.
     * <p>
     * Returns <i>null</i> if there is no rich note blob for this object.
     * @param obj a model object
     * @return the rich note blob or <i>null</i>.
     */
    @objid ("2e72bae3-5a11-41d5-910d-be9027a3faa1")
    String getRelatedBlob(MObject obj) {
        if (obj instanceof ExternDocument) {
            ExternDocument doc = (ExternDocument) obj;
            if (doc.getPath().isEmpty())
                return null;
            else
                return getBlobId(obj);
        }
        return null;
    }

    @objid ("75e7a7c9-a8dd-4cd1-89cf-54a392892f43")
    @Override
    public void removeEditor(IRichNoteEditor editor) {
        this.editorsRegistry.removeEditor(editor);
    }

    @objid ("05b48adc-7c2c-46ea-9477-eb4caefd754f")
    void copyBlob(ExternDocument from, IRepository fromRepo, ExternDocument to, IRepository toRepo) {
        String fromPath = from.getPath();
        if (fromPath.isEmpty())
            return;
        
        int ix = fromPath.lastIndexOf('.');
        String ext = ix == -1 ? "" : fromPath.substring(ix+1);
        
        String label = this.geometry.getRelativePath(this.geometry.getDefaultPath(to, ext));
        
        IBlobInfo toInfo = new BlobInfo(getBlobId(to), label);
        if (BlobCopier.copy(getBlobId(from), fromRepo, toInfo, toRepo))        
            to.setPath(label);
    }

    @objid ("4bdae04b-d4d3-4f3b-8b19-0396a5c482d4")
    @Override
    public void initRichNoteFromFile(ExternDocument doc, Path fileToSave) throws IOException {
        IRepository repo = this.session.getRepositorySupport().getRepository(doc);
        String label = this.geometry.getRelativePath(fileToSave);
        try (OutputStream os = repo.writeBlob(new BlobInfo(getBlobId(doc), label))) {
            Files.copy(fileToSave, os);
        }
        
        // Compute the extension
        String extension;
        int i = fileToSave.toString().lastIndexOf('.');
        if (i >= 0) {
            extension = fileToSave.toString().substring(i + 1);
        } else {
            extension = ""; 
        }
        
        Path p = this.geometry.getDefaultPath(doc, extension);
        doc.setPath(p.toString());
    }

}
