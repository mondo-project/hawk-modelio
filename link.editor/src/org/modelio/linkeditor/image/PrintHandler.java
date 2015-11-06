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
                                    

/**
 * 
 */
package org.modelio.linkeditor.image;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PrintFigureOperation;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.print.PrintGraphicalViewerOperation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Shell;
import org.modelio.linkeditor.view.LinkEditorView;

/**
 * Handler that print the current content of the Link Editor.
 * 
 * @author fpoyer
 */
@objid ("1b47772b-5e33-11e2-b81d-002564c97630")
public class PrintHandler {
    @objid ("1b47772d-5e33-11e2-b81d-002564c97630")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        if (! (part.getObject() instanceof LinkEditorView)) {
            return null;
        }
        
        LinkEditorView editor = (LinkEditorView) part.getObject();
        
        int style = editor.getActiveShell().getStyle();
        Shell shell = new Shell((style & SWT.MIRRORED) != 0 ? SWT.RIGHT_TO_LEFT : SWT.NONE);
        PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
        PrinterData data = dialog.open();
        
        if (data != null) {
            PrintGraphicalViewerOperation operation = new PrintGraphicalViewerOperation(new Printer(data),
                    editor.getGraphicalViewer());
            // here you can set the Print Mode
            operation.setPrintMode(PrintFigureOperation.TILE);
        
            operation.run("Printing diagram");
        }
        return null;
    }

}
