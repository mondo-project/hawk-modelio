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
                                    

//------------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;

/**
 * Listens to a rich text editing event.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("fecc6116-3dd0-4abc-9df2-c26d21ae6a50")
public class RichTextListener {
    @objid ("0224c81f-b42c-46d1-a8d9-1d0076ee3cf0")
    private int eventType;

    @objid ("9f7bbc02-1193-4643-b0ba-6f12981cc523")
    private Listener listener;

    /**
     * Creates a new instance.
     * @param eventType the event type
     * @param listener the event listener
     */
    @objid ("26d6d08c-a8d0-4e5a-a7dd-0bda93f7e35e")
    public RichTextListener(int eventType, Listener listener) {
        this.eventType = eventType;
        this.listener = listener;
    }

    /**
     * Returns the event type.
     * @return the event type
     */
    @objid ("280c01ec-f460-4f53-8df0-5ffd7fcd54dd")
    public int getEventType() {
        return this.eventType;
    }

    /**
     * Returns the event listener.
     * @return the event listener.
     */
    @objid ("ee20f4fe-c904-40b5-9951-acb892ded79f")
    public Listener getListener() {
        return this.listener;
    }

    @objid ("39928a50-5ac1-4408-b465-28761e5dfb40")
    @Override
    public String toString() {
        return "RichTextListener [eventType=" + toSwtEventString(this.eventType) + ", listener=" + this.listener + "]";
    }

    /**
     * @param eventType a SWT event type.
     * @return its string representation
     */
    @objid ("e4e4802c-5fa2-4acd-bdaa-536308800039")
    public static String toSwtEventString(int eventType) {
        switch (eventType) {
        case SWT.Activate:
            return "Activate";
        case SWT.Deactivate:
            return "Deactivate";
        case SWT.Arm:
            return "Arm";
        case SWT.Close:
            return "Close";
        case SWT.Collapse:
            return "Collapse";
        case SWT.DefaultSelection:
            return "DefaultSelection";
        case SWT.Deiconify:
            return "Deiconify";
        case SWT.Dispose:
            return "Dispose";
        case SWT.DragDetect:
            return "DragDetect";
        case SWT.EraseItem:
            return "EraseItem";
        case SWT.Expand:
            return "Expand";
        case SWT.FocusIn:
            return "FocusIn";
        case SWT.FocusOut:
            return "FocusOut";
        case SWT.Gesture:
            return "Gesture";
        case SWT.HardKeyDown:
            return "HardKeyDown";
        case SWT.HardKeyUp:
            return "HardKeyUp";
        case SWT.Help:
            return "Help";
        case SWT.Hide:
            return "Hide";
        case SWT.Iconify:
            return "Iconify";
        case SWT.ImeComposition:
            return "ImeComposition";
        case SWT.KeyDown:
            return "KeyDown";
        case SWT.KeyUp:
            return "KeyUp";
        case SWT.MeasureItem:
            return "MeasureItem";
        case SWT.MenuDetect:
            return "MenuDetect";
        case SWT.None:
            return "None";
        case SWT.OpenDocument:
            return "OpenDocument";
        case SWT.OrientationChange:
            return "OrientationChange";
        case SWT.Paint:
            return "Paint";
        case SWT.PaintItem:
            return "PaintItem";
        case SWT.Resize:
            return "Resize";
        case SWT.Segments:
            return "Segments";
        case SWT.Selection:
            return "Selection";
        case SWT.SetData:
            return "SetData";
        case SWT.Settings:
            return "Settings";
        case SWT.Show:
            return "Show";
        case SWT.Skin:
            return "Skin";
        case SWT.Touch:
            return "Touch";
        case SWT.Traverse:
            return "Traverse";
        case SWT.Verify:
            return "Verify";
        default:
            return "Unknown event "+Integer.toString(eventType);
        
        }
    }

}
