/*
 * Copyright 2013 Modeliosoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */  
                                    

package org.modelio.admtool.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.modelio.app.preferences.ScopedPreferenceStore;
import org.modelio.app.ui.logconfig.LogPreferencesKeys;
import org.modelio.app.ui.plugin.AppUi;

@objid ("d8ad7d7a-df88-4b35-bd84-28230e49866e")
public class AdminModeTester extends PropertyTester {
    @objid ("85455b1f-127a-4b07-9f66-19df40a17881")
    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (! (receiver instanceof ISelection))
            return false;
        
        switch (property) {
        case "adminmode":
            final IPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, AppUi.PLUGIN_ID);
            final boolean value = preferenceStore.getBoolean(LogPreferencesKeys.SHOWADMTOOLS_PREFKEY);
            return value;
        default:
            throw new IllegalArgumentException(property +" property not supported by "+getClass().getSimpleName());
        }
    }

}
