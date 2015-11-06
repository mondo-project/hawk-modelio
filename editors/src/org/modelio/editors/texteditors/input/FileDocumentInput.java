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
                                    

package org.modelio.editors.texteditors.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.modelio.editors.service.IEditorListener;

@objid ("7b5333e0-2a77-11e2-9fb9-bc305ba4815c")
public class FileDocumentInput extends AbstractInput implements IDocumentInput {
    @objid ("7b5333e1-2a77-11e2-9fb9-bc305ba4815c")
    private long cachedModificationStamp;

    @objid ("7b5333e9-2a77-11e2-9fb9-bc305ba4815c")
    private static final int DEFAULT_FILE_SIZE = 15 * 1024;

    @objid ("ab520472-2a77-11e2-9fb9-bc305ba4815c")
    private static final String FILE_ENCODING = "UTF-8";

    @objid ("7b5333e2-2a77-11e2-9fb9-bc305ba4815c")
    private File file;

    @objid ("7b5333e3-2a77-11e2-9fb9-bc305ba4815c")
    private IDocument document;

    @objid ("c1ea9a7e-2e5d-11e2-a8ff-bc305ba4815c")
    private IEditorListener saveListener;

    @objid ("7b5333eb-2a77-11e2-9fb9-bc305ba4815c")
    public FileDocumentInput(File file) {
        this.file = file;
    }

    @objid ("7b5333ee-2a77-11e2-9fb9-bc305ba4815c")
    @Override
    public File getFile() {
        return this.file;
    }

    @objid ("7b533401-2a77-11e2-9fb9-bc305ba4815c")
    @Override
    public IStatus save() {
        if (file.exists()) {
            if (file.lastModified() != cachedModificationStamp) {
                String[] tab = { "Overwrite disk contents", "Reload from disk", "Cancel" };
                MessageDialog dialog = new MessageDialog(null, "Out of sync file.", null,
                        "This file has been modified outside the editor.", MessageDialog.INFORMATION, tab, 0);
                int choice = dialog.open();
                switch (choice) {
                case 0: // overwrite
                    doSave();
                    break;
                case 1: // reload from disk
                    doLoad();
                    break;
                case 2: // cancel
                    break;
                }
                return null;
            }
        }
        
        doSave();
        return null;
    }

    @objid ("7b533405-2a77-11e2-9fb9-bc305ba4815c")
    public IDocument getDocument(IDocument inputDocument) {
        if (this.document == null) {
            if (inputDocument == null) {
                this.document = new Document();
            } else {
                this.document = inputDocument;
            }
        
            doLoad();
        }
        return this.document;
    }

    @objid ("7b53340a-2a77-11e2-9fb9-bc305ba4815c")
    public void doSave() {
        try{
            byte[] utf8Bytes = this.document.get().getBytes(FILE_ENCODING);
            this.file.createNewFile();
            try (OutputStream out = new FileOutputStream(this.file)){
        
                out.write(utf8Bytes);
                out.close();
                setDirty(false);         
                this.cachedModificationStamp = this.file.lastModified();
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }catch (IOException e) {
            e.printStackTrace();
        } 
             
        if(this.saveListener != null){
            this.saveListener.documentSaved(file);
        }
    }

    @objid ("7b53340c-2a77-11e2-9fb9-bc305ba4815c")
    public void doLoad() {
        try (
            InputStream contentStream = new FileInputStream(file);
            Reader in = new BufferedReader(new InputStreamReader(contentStream, FILE_ENCODING), DEFAULT_FILE_SIZE)){
            StringBuffer buffer = new StringBuffer(DEFAULT_FILE_SIZE);
            char[] readBuffer = new char[2048];
            int n = in.read(readBuffer);
            while (n > 0) {
                buffer.append(readBuffer, 0, n);
                n = in.read(readBuffer);
            }
        
            this.document.set(buffer.toString());
            this.cachedModificationStamp = this.file.lastModified();
            this.document.addDocumentListener(new IDocumentListener() {
        
                public void documentChanged(DocumentEvent event) {
                    setDirty(true);
                }
        
                public void documentAboutToBeChanged(DocumentEvent event) {
        
                }
            });
            
            setDirty(false);
        } catch (IOException x) {
            x.printStackTrace();
        }
    }

    @objid ("c1ea9a83-2e5d-11e2-a8ff-bc305ba4815c")
    public void setSaveListener(IEditorListener saveListener) {
        this.saveListener = saveListener;
    }

    @objid ("c1ea9a86-2e5d-11e2-a8ff-bc305ba4815c")
    @Override
    public void dispose() {
        if(this.saveListener != null){
            this.saveListener.editorClosed();
        }
    }

}
