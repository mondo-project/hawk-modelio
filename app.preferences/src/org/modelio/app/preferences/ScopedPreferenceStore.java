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
                                    

package org.modelio.app.preferences;

import java.io.IOException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.common.EventManager;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.SafeRunnable;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The ScopedPreferenceStore is an IPreferenceStore that uses the scopes
 * provided in org.eclipse.core.runtime.preferences.
 * <p>
 * A ScopedPreferenceStore does the lookup of a preference based on it's search
 * scopes and sets the value of the preference based on its store scope.
 * </p>
 * <p>
 * The default scope is always included in the search scopes when searching for
 * preference values.
 * </p>
 * 
 * @see org.eclipse.core.runtime.preferences
 * @since 3.1
 */
@objid ("ef047fda-e263-4051-958c-f91ae6267b1f")
@SuppressWarnings({ "unqualified-field-access", "unused" })
public class ScopedPreferenceStore extends EventManager implements IPreferenceStore, IPersistentPreferenceStore {
    /**
     * The storeContext is the context where values will stored with the
     * setValue methods. If there are no searchContexts this will be the search
     * context. (along with the "default" context)
     */
    @objid ("f98c3f32-61a3-4ca4-bae9-55511233d6e1")
    private IScopeContext storeContext;

    /**
     * The searchContext is the array of contexts that will be used by the get
     * methods for searching for values.
     */
    @objid ("e8f72a85-2ce9-48a6-8983-e949c32fa0a6")
    private IScopeContext[] searchContexts;

    /**
     * A boolean to indicate the property changes should not be propagated.
     */
    @objid ("399dd4c0-2989-48f9-a3d4-e40abd166556")
    protected boolean silentRunning = false;

    /**
     * The listener on the IEclipsePreferences. This is used to forward updates
     * to the property change listeners on the preference store.
     */
    @objid ("025bef57-b4a5-47bc-b1ec-758fc2abdb6b")
    public org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener preferencesListener;

    /**
     * The default context is the context where getDefault and setDefault
     * methods will search. This context is also used in the search.
     */
    @objid ("067040b6-516a-4624-b036-289dfcca3210")
    private IScopeContext defaultContext = new DefaultScope();

    /**
     * The nodeQualifer is the string used to look up the node in the contexts.
     */
    @objid ("8e558740-fc20-4dca-b009-02eee22ff4da")
    public String nodeQualifier;

    /**
     * The defaultQualifier is the string used to look up the default node.
     */
    @objid ("355a6cfe-7245-448d-8f52-8441d31e02bd")
    public String defaultQualifier;

    /**
     * Boolean value indicating whether or not this store has changes to be
     * saved.
     */
    @objid ("278c5de1-9a5a-4606-9252-d490ca227be9")
    private boolean dirty;

    /**
     * Create a new instance of the receiver. Store the values in context in the
     * node looked up by qualifier. <strong>NOTE:</strong> Any instance of
     * ScopedPreferenceStore should call
     * @param context the scope to store to
     * @param qualifier the qualifier used to look up the preference node
     * @param defaultQualifierPath the qualifier used when looking up the defaults
     */
    @objid ("d733e14a-41e4-4392-9366-77ff1690f096")
    public ScopedPreferenceStore(IScopeContext context, String qualifier, String defaultQualifierPath) {
        this(context, qualifier);
        this.defaultQualifier = defaultQualifierPath;
    }

    /**
     * Create a new instance of the receiver. Store the values in context in the
     * node looked up by qualifier.
     * @param context the scope to store to
     * @param qualifier the qualifer used to look up the preference node
     */
    @objid ("27a86882-91d9-4c68-84c8-ebc4c972cc5f")
    public ScopedPreferenceStore(IScopeContext context, String qualifier) {
        storeContext = context;
        this.nodeQualifier = qualifier;
        this.defaultQualifier = qualifier;
        
        ((IEclipsePreferences) getStorePreferences().parent())
                .addNodeChangeListener(getNodeChangeListener());
    }

    /**
     * Return a node change listener that adds a removes the receiver when nodes
     * change.
     * @return INodeChangeListener
     */
    @objid ("f22bca94-c32d-4dbe-b128-1703f8929eae")
    private INodeChangeListener getNodeChangeListener() {
        return new IEclipsePreferences.INodeChangeListener() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener#added(org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent)
             */
            @Override
            public void added(NodeChangeEvent event) {
                if (nodeQualifier.equals(event.getChild().name())
                        && isListenerAttached()) {
                    getStorePreferences().addPreferenceChangeListener(
                            preferencesListener);
                }
            }
        
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener#removed(org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent)
             */
            @Override
            public void removed(NodeChangeEvent event) {
                // Do nothing as there are no events from removed node
            }
        };
    }

    /**
     * Initialize the preferences listener.
     */
    @objid ("743fa2c3-9fba-4898-82e1-ae5a5c582fd5")
    private void initializePreferencesListener() {
        if (preferencesListener == null) {
            preferencesListener = new IEclipsePreferences.IPreferenceChangeListener() {
                /*
                 * (non-Javadoc)
                 * 
                 * @see org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener#preferenceChange(org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
                 */
                @Override
                public void preferenceChange(PreferenceChangeEvent event) {
        
                    if (silentRunning) {
                        return;
                    }
        
                    Object oldValue = event.getOldValue();
                    Object newValue = event.getNewValue();
                    String key = event.getKey();
                    if (newValue == null) {
                        newValue = getDefault(key, oldValue);
                    } else if (oldValue == null) {
                        oldValue = getDefault(key, newValue);
                    }
                    firePropertyChangeEvent(event.getKey(), oldValue, newValue);
                }
            };
            getStorePreferences().addPreferenceChangeListener(
                    preferencesListener);
        }
    }

    /**
     * Does its best at determining the default value for the given key. Checks
     * the given object's type and then looks in the list of defaults to see if
     * a value exists. If not or if there is a problem converting the value, the
     * default default value for that type is returned.
     * @param key the key to search
     * @param obj the object who default we are looking for
     * @return Object or <code>null</code>
     */
    @objid ("2a05bbb3-b545-4537-b89a-7ceefb44b63b")
    public Object getDefault(String key, Object obj) {
        IEclipsePreferences defaults = getDefaultPreferences();
        if (obj instanceof String) {
            return defaults.get(key, STRING_DEFAULT_DEFAULT);
        } else if (obj instanceof Integer) {
            return new Integer(defaults.getInt(key, INT_DEFAULT_DEFAULT));
        } else if (obj instanceof Double) {
            return new Double(defaults.getDouble(key, DOUBLE_DEFAULT_DEFAULT));
        } else if (obj instanceof Float) {
            return new Float(defaults.getFloat(key, FLOAT_DEFAULT_DEFAULT));
        } else if (obj instanceof Long) {
            return new Long(defaults.getLong(key, LONG_DEFAULT_DEFAULT));
        } else if (obj instanceof Boolean) {
            return defaults.getBoolean(key, BOOLEAN_DEFAULT_DEFAULT) ? Boolean.TRUE
                    : Boolean.FALSE;
        } else {
            return null;
        }
    }

    /**
     * Return the IEclipsePreferences node associated with this store.
     * @return the preference node for this store
     */
    @objid ("3a5d04c1-c439-443a-a8b5-a228df077e5c")
    public IEclipsePreferences getStorePreferences() {
        return storeContext.getNode(nodeQualifier);
    }

    /**
     * Return the default IEclipsePreferences for this store.
     * @return this store's default preference node
     */
    @objid ("7e08576f-2d1a-4c1d-8a75-799f54a0b17e")
    private IEclipsePreferences getDefaultPreferences() {
        return defaultContext.getNode(defaultQualifier);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    @objid ("a8de75c4-d185-4370-9200-2f6854f04a9e")
    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
        initializePreferencesListener();// Create the preferences listener if it
        // does not exist
        addListenerObject(listener);
    }

    /**
     * Return the preference path to search preferences on. This is the list of
     * preference nodes based on the scope contexts for this store. If there are
     * no search contexts set, then return this store's context.
     * <p>
     * Whether or not the default context should be included in the resulting
     * list is specified by the <code>includeDefault</code> parameter.
     * </p>
     * @param includeDefault <code>true</code> if the default context should be included
     * and <code>false</code> otherwise
     * @return IEclipsePreferences[]
     * @since 3.4 public, was added in 3.1 as private method
     */
    @objid ("c8d62a3a-d0d9-4b8c-8468-9db81bb62952")
    public IEclipsePreferences[] getPreferenceNodes(boolean includeDefault) {
        // if the user didn't specify a search order, then return the scope that
        // this store was created on. (and optionally the default)
        if (searchContexts == null) {
            if (includeDefault) {
                return new IEclipsePreferences[] { getStorePreferences(),
                        getDefaultPreferences() };
            }
            return new IEclipsePreferences[] { getStorePreferences() };
        }
        // otherwise the user specified a search order so return the appropriate
        // nodes based on it
        int length = searchContexts.length;
        if (includeDefault) {
            length++;
        }
        IEclipsePreferences[] preferences = new IEclipsePreferences[length];
        for (int i = 0; i < searchContexts.length; i++) {
            preferences[i] = searchContexts[i].getNode(nodeQualifier);
        }
        if (includeDefault) {
            preferences[length - 1] = getDefaultPreferences();
        }
        return preferences;
    }

    /**
     * Set the search contexts to scopes. When searching for a value the seach
     * will be done in the order of scope contexts and will not search the
     * storeContext unless it is in this list.
     * <p>
     * If the given list is <code>null</code>, then clear this store's search
     * contexts. This means that only this store's scope context and default
     * scope will be used during preference value searching.
     * </p>
     * <p>
     * The defaultContext will be added to the end of this list automatically
     * and <em>MUST NOT</em> be included by the user.
     * </p>
     * @param scopes a list of scope contexts to use when searching, or
     * <code>null</code>
     */
    @objid ("7caf8903-fbfe-492e-8a77-2b16d05b548b")
    public void setSearchContexts(IScopeContext[] scopes) {
        this.searchContexts = scopes;
        if (scopes == null) {
            return;
        }
        
        // Assert that the default was not included (we automatically add it to
        // the end)
        for (int i = 0; i < scopes.length; i++) {
            if (scopes[i].equals(defaultContext)) {
                Assert
                        .isTrue(
                                false,
                                "WorkbenchMessages.ScopedPreferenceStore_DefaultAddedError");
            }
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
     */
    @objid ("ea4536ef-682e-4086-a3f2-a0dc7213fec3")
    @Override
    public boolean contains(String name) {
        if (name == null) {
            return false;
        }
        return (Platform.getPreferencesService().get(name, null,
                getPreferenceNodes(true))) != null;
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent(java.lang.String,
     *      java.lang.Object, java.lang.Object)
     */
    @objid ("63af2892-f55d-424b-b447-41b5a5c80917")
    @Override
    public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
        // important: create intermediate array to protect against listeners
        // being added/removed during the notification
        final Object[] list = getListeners();
        if (list.length == 0) {
            return;
        }
        final PropertyChangeEvent event = new PropertyChangeEvent(this, name,
                oldValue, newValue);
        for (int i = 0; i < list.length; i++) {
            final IPropertyChangeListener listener = (IPropertyChangeListener) list[i];
            SafeRunner.run(new SafeRunnable(JFaceResources
                    .getString("PreferenceStore.changeError")) { //$NON-NLS-1$
                        @Override
                        public void run() {
                            listener.propertyChange(event);
                        }
                    });
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String)
     */
    @objid ("3c8acd5c-76c2-4b36-a2cf-8752b6c42e2e")
    @Override
    public boolean getBoolean(String name) {
        String value = internalGet(name);
        return value == null ? BOOLEAN_DEFAULT_DEFAULT : Boolean.valueOf(value)
                .booleanValue();
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.lang.String)
     */
    @objid ("ee41e29e-f72e-4edd-80c8-19bab7f6c9ef")
    @Override
    public boolean getDefaultBoolean(String name) {
        return getDefaultPreferences()
                .getBoolean(name, BOOLEAN_DEFAULT_DEFAULT);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang.String)
     */
    @objid ("3b1b7ded-6ef6-4637-aec1-8e062d1db1cc")
    @Override
    public double getDefaultDouble(String name) {
        return getDefaultPreferences().getDouble(name, DOUBLE_DEFAULT_DEFAULT);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang.String)
     */
    @objid ("ec5098a0-88d4-40a3-869b-9dd01ca41f6d")
    @Override
    public float getDefaultFloat(String name) {
        return getDefaultPreferences().getFloat(name, FLOAT_DEFAULT_DEFAULT);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang.String)
     */
    @objid ("e78b51c7-3e2e-4e20-83c1-65bb3b1b643b")
    @Override
    public int getDefaultInt(String name) {
        return getDefaultPreferences().getInt(name, INT_DEFAULT_DEFAULT);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang.String)
     */
    @objid ("07eb99b6-a71e-4718-97b1-b66fd9ac762f")
    @Override
    public long getDefaultLong(String name) {
        return getDefaultPreferences().getLong(name, LONG_DEFAULT_DEFAULT);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang.String)
     */
    @objid ("2fe68d0b-947f-471a-84fd-4d1446625553")
    @Override
    public String getDefaultString(String name) {
        return getDefaultPreferences().get(name, STRING_DEFAULT_DEFAULT);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
     */
    @objid ("0108e81f-c091-46e7-a651-c0f94420494f")
    @Override
    public double getDouble(String name) {
        String value = internalGet(name);
        if (value == null) {
            return DOUBLE_DEFAULT_DEFAULT;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return DOUBLE_DEFAULT_DEFAULT;
        }
    }

    /**
     * Return the string value for the specified key. Look in the nodes which
     * are specified by this object's list of search scopes. If the value does
     * not exist then return <code>null</code>.
     * @param key the key to search with
     * @return String or <code>null</code> if the value does not exist.
     */
    @objid ("17e365ce-cabc-4651-bcbc-db811d4ca6ce")
    private String internalGet(String key) {
        return Platform.getPreferencesService().get(key, null,
                getPreferenceNodes(true));
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
     */
    @objid ("652cfc85-27cb-4b52-8bbf-93e6aa21d2d7")
    @Override
    public float getFloat(String name) {
        String value = internalGet(name);
        if (value == null) {
            return FLOAT_DEFAULT_DEFAULT;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return FLOAT_DEFAULT_DEFAULT;
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
     */
    @objid ("a921cd9d-854f-4601-85a7-596d041a29e7")
    @Override
    public int getInt(String name) {
        String value = internalGet(name);
        if (value == null) {
            return INT_DEFAULT_DEFAULT;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return INT_DEFAULT_DEFAULT;
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
     */
    @objid ("e56426be-ba15-484f-94c2-e456268b553b")
    @Override
    public long getLong(String name) {
        String value = internalGet(name);
        if (value == null) {
            return LONG_DEFAULT_DEFAULT;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return LONG_DEFAULT_DEFAULT;
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
     */
    @objid ("0448d8c0-0625-4edd-bcdd-160f656665bf")
    @Override
    public String getString(String name) {
        String value = internalGet(name);
        return value == null ? STRING_DEFAULT_DEFAULT : value;
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
     */
    @objid ("530c12cd-8e8a-4b02-b4ef-d56dd829a541")
    @Override
    public boolean isDefault(String name) {
        if (name == null) {
            return false;
        }
        return (Platform.getPreferencesService().get(name, null,
                getPreferenceNodes(false))) == null;
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
     */
    @objid ("abb5af03-0f15-493f-b5b6-6e535d4fc55f")
    @Override
    public boolean needsSaving() {
        return dirty;
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#putValue(java.lang.String,
     *      java.lang.String)
     */
    @objid ("52d65337-c502-421a-a223-0e1961fcf9ea")
    @Override
    public void putValue(String name, String value) {
        try {
            // Do not notify listeners
            silentRunning = true;
            getStorePreferences().put(name, value);
        } finally {
            // Be sure that an exception does not stop property updates
            silentRunning = false;
            dirty = true;
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    @objid ("1cfc07fe-ad8a-43d9-89d5-90b939bdeca4")
    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener) {
        removeListenerObject(listener);
        if (!isListenerAttached()) {
            disposePreferenceStoreListener();
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
     *      double)
     */
    @objid ("5ad86405-4a07-4d1c-8fd3-8925d3c6e866")
    @Override
    public void setDefault(String name, double value) {
        getDefaultPreferences().putDouble(name, value);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
     *      float)
     */
    @objid ("69140a08-7f8e-436d-9491-2a88c0a86412")
    @Override
    public void setDefault(String name, float value) {
        getDefaultPreferences().putFloat(name, value);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
     *      int)
     */
    @objid ("75ff05c3-32e1-4149-a244-62ffcfa254cb")
    @Override
    public void setDefault(String name, int value) {
        getDefaultPreferences().putInt(name, value);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
     *      long)
     */
    @objid ("ebb6a7fe-af3b-40d7-9221-e3e584bb2dde")
    @Override
    public void setDefault(String name, long value) {
        getDefaultPreferences().putLong(name, value);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
     *      java.lang.String)
     */
    @objid ("aa94e120-0410-43fb-9c3a-39d42e8883a6")
    @Override
    public void setDefault(String name, String defaultObject) {
        getDefaultPreferences().put(name, defaultObject);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String,
     *      boolean)
     */
    @objid ("76345088-485d-4c97-b0c1-f35e4d9cc9c5")
    @Override
    public void setDefault(String name, boolean value) {
        getDefaultPreferences().putBoolean(name, value);
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.String)
     */
    @objid ("0c4fe23a-3234-4f73-a327-7c8e3cf611ce")
    @Override
    public void setToDefault(String name) {
        String oldValue = getString(name);
        String defaultValue = getDefaultString(name);
        try {
            silentRunning = true;// Turn off updates from the store
            // removing a non-existing preference is a no-op so call the Core
            // API directly
            getStorePreferences().remove(name);
            if (oldValue != defaultValue){
                dirty = true;
                firePropertyChangeEvent(name, oldValue, defaultValue);
            }
                
        } finally {
            silentRunning = false;// Restart listening to preferences
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
     *      double)
     */
    @objid ("449a598a-0ca1-45d7-91ff-060e3c99dfa2")
    @Override
    public void setValue(String name, double value) {
        double oldValue = getDouble(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;// Turn off updates from the store
            if (getDefaultDouble(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putDouble(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Double(oldValue), new Double(
                    value));
        } finally {
            silentRunning = false;// Restart listening to preferences
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
     *      float)
     */
    @objid ("87b22b8a-8b9b-40df-954b-f0ae03dfd236")
    @Override
    public void setValue(String name, float value) {
        float oldValue = getFloat(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;// Turn off updates from the store
            if (getDefaultFloat(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putFloat(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Float(oldValue), new Float(value));
        } finally {
            silentRunning = false;// Restart listening to preferences
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
     *      int)
     */
    @objid ("6468b5b2-a96e-4cd9-9a59-185b6b71ffae")
    @Override
    public void setValue(String name, int value) {
        int oldValue = getInt(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;// Turn off updates from the store
            if (getDefaultInt(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putInt(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Integer(oldValue), new Integer(
                    value));
        } finally {
            silentRunning = false;// Restart listening to preferences
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
     *      long)
     */
    @objid ("f74d41d1-06b2-4ccb-abf2-81aad810369c")
    @Override
    public void setValue(String name, long value) {
        long oldValue = getLong(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;// Turn off updates from the store
            if (getDefaultLong(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putLong(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, new Long(oldValue), new Long(value));
        } finally {
            silentRunning = false;// Restart listening to preferences
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
     *      java.lang.String)
     */
    @objid ("05983ed8-5924-43ed-9fcc-8ca4f1006bfd")
    @Override
    public void setValue(String name, String value) {
        // Do not turn on silent running here as Strings are propagated
        if (getDefaultString(name).equals(value)) {
            getStorePreferences().remove(name);
        } else {
            getStorePreferences().put(name, value);
        }
        dirty = true;
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String,
     *      boolean)
     */
    @objid ("90a7e8a0-866c-415c-8246-c4ee5747d40c")
    @Override
    public void setValue(String name, boolean value) {
        boolean oldValue = getBoolean(name);
        if (oldValue == value) {
            return;
        }
        try {
            silentRunning = true;// Turn off updates from the store
            if (getDefaultBoolean(name) == value) {
                getStorePreferences().remove(name);
            } else {
                getStorePreferences().putBoolean(name, value);
            }
            dirty = true;
            firePropertyChangeEvent(name, oldValue ? Boolean.TRUE
                    : Boolean.FALSE, value ? Boolean.TRUE : Boolean.FALSE);
        } finally {
            silentRunning = false;// Restart listening to preferences
        }
    }

/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.IPersistentPreferenceStore#save()
     */
    @objid ("1b46df50-3296-47a5-8098-ac33394a005f")
    @Override
    public void save() throws IOException {
        try {
            getStorePreferences().flush();
            dirty = false;
        } catch (BackingStoreException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Dispose the receiver.
     */
    @objid ("eeae6501-adf2-4316-bbd5-45debed460d3")
    private void disposePreferenceStoreListener() {
        IEclipsePreferences root = (IEclipsePreferences) Platform
                .getPreferencesService().getRootNode().node(
                        Plugin.PLUGIN_PREFERENCE_SCOPE);
        try {
            if (!(root.nodeExists(nodeQualifier))) {
                return;
            }
        } catch (BackingStoreException e) {
            return;// No need to report here as the node won't have the
            // listener
        }
        
        IEclipsePreferences preferences = getStorePreferences();
        if (preferences == null) {
            return;
        }
        if (preferencesListener != null) {
            preferences.removePreferenceChangeListener(preferencesListener);
            preferencesListener = null;
        }
    }

}
