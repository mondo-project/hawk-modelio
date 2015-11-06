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
                                    

package org.modelio.edition.html.view;

import java.net.URL;
import java.util.Iterator;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.modelio.edition.html.view.actions.FindReplaceAction;

/**
 * The interface for a rich text control.
 * <p>
 * A rich text control is an editable user interface object that displays rich
 * text and images.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("266ed94a-b7a4-4db4-8825-55403b4b2142")
public interface IRichText {
    /**
     * Property name to use on {@link Control#getData(String)} and {@link Control#setData(String, Object)}
     * to store the <code>IRichText</code> instance.
     */
    @objid ("33b05eae-67aa-4f8a-bf20-9e21db47a7c4")
    public static final String PROPERTY_NAME = "richText"; // $NON-NLS-1$

    /**
     * Returns this rich text control.
     * @return this rich text control
     */
    @objid ("566378ae-85a8-49c0-80ca-b5ab5bd5e393")
    Control getControl();

    /**
     * Sets the layout data.
     * @param layoutData the layout data to set
     */
    @objid ("31dd718e-3095-4d46-9d56-7c49c51740a8")
    void setLayoutData(Object layoutData);

    /**
     * Returns the layout data.
     * @return this control's layout data
     */
    @objid ("e411be88-ab06-4e64-a3a3-4fa9d2d6d28c")
    Object getLayoutData();

    /**
     * Sets focus to this control.
     */
    @objid ("13d9d38b-218c-4cf2-b965-64d974a0f0ee")
    void setFocus();

    /**
     * Tells the control it does not have focus.
     */
    @objid ("e39483eb-08df-4092-a640-284b77ffd811")
    void setBlur();

    /**
     * Checks whether this control has focus.
     * @return <code>true</code> if this control has the user-interface focus
     */
    @objid ("b77d8b6d-9125-49d1-8f88-7d77d930521e")
    boolean hasFocus();

    /**
     * Returns the base path used for resolving text and image links.
     * @return the base path used for resolving links in this control
     */
    @objid ("122c4f0f-ffc0-4d04-a707-98c60eb806ea")
    String getBasePath();

    /**
     * Returns the editable state.
     * @return <code>true</code> if the content is ediatble
     */
    @objid ("0f33741f-1208-4b64-8eb8-613f4a244d1d")
    boolean getEditable();

    /**
     * Sets the editable state.
     * @param editable the editable state
     */
    @objid ("0695514c-689c-466b-9f2c-da5235a1221d")
    void setEditable(boolean editable);

    /**
     * Checks whether the content has been modified.
     * @return <code>true</code> if the content has been modified
     */
    @objid ("9e510ba0-1006-4779-9095-bbac8d2e89c4")
    boolean getModified();

    /**
     * Sets the modified state.
     * @param modified the modified state
     */
    @objid ("768e0436-ee3d-463f-86b6-b613faebce75")
    void setModified(boolean modified);

    /**
     * Returns the rich text content.
     * @return the rich text content formatted in a markup language
     */
    @objid ("a7f0a4b1-f194-4d24-8861-796d694e6c59")
    String getText();

    /**
     * Sets the rich text content.
     * @param text the rich text content formatted in a markup language
     */
    @objid ("dfdc738f-a920-4864-b4ce-d49695dfb3b5")
    void setText(String text);

    /**
     * Restores the rich text content back to the initial value.
     */
    @objid ("b5573c96-38ff-4468-b55a-8b97abafa1dc")
    void restoreText();

    /**
     * Checks whether the editor content has been modified. If it has been
     * modified, notify the modify listeners.
     */
    @objid ("8fa5e032-3b58-493a-bc6a-c0f47ce61874")
    void checkModify();

    /**
     * Returns an object that describes the current selection
     * @return a <code>RichTextSelection</code> object
     */
    @objid ("6b49e157-4190-4573-825c-ae1cfd5cfe6a")
    RichTextSelection getSelected();

    /**
     * Returns an application specific property value.
     * @param key the name of the property
     * @return the value of the property or <code>null</code> if it has not
     * been set
     */
    @objid ("a130d414-4e69-4649-b42a-f551afa695d5")
    Object getData(String key);

    /**
     * Sets an application specific property name and value.
     * @param key the name of the property
     * @param value the property value
     */
    @objid ("dfe822e4-0869-4f4b-a107-d9b87ec194a8")
    void setData(String key, Object value);

    /**
     * Executes the given rich text command. The supported command strings are
     * defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @return a status code returned by the executed command
     */
    @objid ("c749addb-0047-4871-bc78-7c6f6b18326d")
    int executeCommand(String command);

    /**
     * Executes the given rich text command with a single parameter. The
     * supported command strings are defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @param    param        a parameter for the command or <code>null</code>
     * @return a status code returned by the executed command
     */
    @objid ("72ae6563-9887-4584-b598-6fd61728b823")
    int executeCommand(String command, String param);

    /**
     * Executes the given rich text command with an array of parameters. The
     * supported command strings are defined in <code>RichTextCommand<code>.
     * @param    command        a rich text command string
     * @param    params        an array of parameters for the command or <code>null</code>
     * @return a status code returned by the executed command
     */
    @objid ("03ad779f-aa85-4be2-ad17-1ab7f57651f2")
    int executeCommand(String command, String[] params);

    /**
     * Returns the modify listeners attached to this control.
     * @return an iterator for retrieving the modify listeners
     */
    @objid ("9d6d77e6-3b2b-4046-84a5-d49d745cfb69")
    Iterator<ModifyListener> getModifyListeners();

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * keys are pressed and released within this control.
     * @param listener the listener which should be notified
     */
    @objid ("5f0ebaa9-6385-4ba5-9f0e-ad82d5a5a1f3")
    void addKeyListener(KeyListener listener);

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when keys are pressed and released within this control.
     * @param listener the listener which should no longer be notified
     */
    @objid ("e6f8e8ad-784b-4ef0-acd9-c3a55ebf6c99")
    void removeKeyListener(KeyListener listener);

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * the content of this control is modified.
     * @param listener the listener which should be notified
     */
    @objid ("e92873af-c071-4e8a-a3f1-136618145757")
    void addModifyListener(ModifyListener listener);

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when the content of this control is modified.
     * @param listener the listener which should no longer be notified
     */
    @objid ("c2f9897c-49d5-414a-93f6-f2ae9c148176")
    void removeModifyListener(ModifyListener listener);

    /**
     * Adds the listener to the collection of listeners who will be notifed when
     * this control is disposed.
     * @param listener the listener which should be notified
     */
    @objid ("02144ef6-a71c-4dd8-aa29-9faac8126f24")
    void addDisposeListener(DisposeListener listener);

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when this control is disposed.
     * @param listener the listener which should no longer be notified
     */
    @objid ("dea8bbbc-99fc-4537-994d-a5e6cf069e39")
    void removeDisposeListener(DisposeListener listener);

    /**
     * Adds a listener to the collection of listeners who will be notified when
     * help events are generated for this control.
     * @param listener the listener which should be notified
     */
    @objid ("f894da8d-bd89-447f-85ee-1331d27f5dfa")
    void addHelpListener(HelpListener listener);

    /**
     * Removes a listener from the collection of listeners who will be notified
     * when help events are generated for this control.
     * @param listener the listener which should no longer be notified
     */
    @objid ("725ac94b-9d2c-47f4-9775-6c83857dcf71")
    void removeHelpListener(HelpListener listener);

    /**
     * Adds the listener to the collection of listeners who will be notifed when
     * an event of the given type occurs within this control.
     * @param eventType the type of event to listen for
     * @param listener the listener which should be notified when the event occurs
     */
    @objid ("5611cab7-bb87-4454-be69-d7ebf75d5a09")
    void addListener(int eventType, Listener listener);

    /**
     * Removes the listener from the collection of listeners who will be notifed
     * when an event of the given type occurs within this econtrol.
     * @param eventType the type of event to listen to
     * @param listener the listener which should no longer be notified when the event
     * occurs
     */
    @objid ("a596d7e0-6584-453c-824e-b3a6c1386bc9")
    void removeListener(int eventType, Listener listener);

    /**
     * Notifies the modify listeners
     */
    @objid ("7c9cb046-504c-422b-b344-fa9c6a6cc5d4")
    void notifyModifyListeners();

    /**
     * Returns the event listeners attached to this control.
     * @return an iterator for retrieving the event listeners attached to this
     * control
     */
    @objid ("47f6afc0-45ce-4b96-a595-dcedafa10688")
    Iterator<RichTextListener> getListeners();

    /**
     * Returns the base URL of the rich text control whose content was last
     * copied to the clipboard.
     * @return the base URL of a rich text control
     */
    @objid ("7d0ddb32-4181-4052-a076-6175a2142130")
    URL getCopyURL();

    /**
     * Sets the base URL of the rich text control whose content was last copied
     * to the clipboard.
     */
    @objid ("f35c1356-274c-4237-9dfe-532cd52786a4")
    void setCopyURL();

    /**
     * Disposes the operating system resources allocated by this control.
     */
    @objid ("26c2d858-8e85-491f-acac-a8ee1850c0fb")
    void dispose();

    /**
     * Checks whether this control has been disposed.
     * @return <code>true</code> if this control is disposed successfully
     */
    @objid ("ca62b8e2-040e-434b-847b-9c6692c28dca")
    boolean isDisposed();

    /**
     * @return the control's FindReplaceAction
     */
    @objid ("56c59fab-74f6-4c1e-8e31-64dbc244b9e8")
    FindReplaceAction getFindReplaceAction();

    /**
     * Sets the FindReplaceAction to use.
     * @param findReplaceAction the FindReplaceAction to use.
     */
    @objid ("83147ecd-72d1-4265-b0cd-0faeefc733cc")
    void setFindReplaceAction(FindReplaceAction findReplaceAction);

    /**
     * Sets the initialText variable, which stores what is saved on disk
     * @param text the initial Text.
     */
    @objid ("38ae7da9-5a8e-4699-8e23-0313c63da90f")
    void setInitialText(String text);

    /**
     * Returns the rich text content filtered by Tidy.
     * @return the rich text content formatted in a markup language
     */
    @objid ("99e0dbcc-ae38-4140-93eb-7341a2ece18f")
    String getTidyText();

}
