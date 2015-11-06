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
                                    

package org.modelio.diagram.editor.deployment.style;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.deployment.plugin.DiagramEditorDeployment;
import org.modelio.diagram.styles.core.AbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class is an extension of AbstractStyleKeyProvider managing i18n for StyleKeys.<br>
 * You must use {@link DeploymentAbstractStyleKeyProvider#createStyleKey} to create each StyleKey.
 * 
 * @author chm
 */
@objid ("974b31c8-55b6-11e2-877f-002564c97630")
public abstract class DeploymentAbstractStyleKeyProvider extends AbstractStyleKeyProvider {
    @objid ("974b31cc-55b6-11e2-877f-002564c97630")
    private static final String CATEGORY_SUFFIX = ".category";

    @objid ("974b31ce-55b6-11e2-877f-002564c97630")
    private static final String LABEL_SUFFIX = ".label";

    @objid ("974b31d0-55b6-11e2-877f-002564c97630")
    private static final String TOOLTIP_SUFFIX = ".tooltip";

    @objid ("974cb859-55b6-11e2-877f-002564c97630")
    private static final String STYLEKEY_PREFIX = "StyleKey.";

    /**
     * Creates a StyleKey based on a MetaKey.
     * @param key The style key.
     * @param metakey the base meta key.
     * @return a StyleKey with i18n informations filled.
     */
    @objid ("974cb85b-55b6-11e2-877f-002564c97630")
    public static StyleKey createStyleKey(String key, MetaKey metakey) {
        return new StyleKey(key,
                            metakey,
                            metakey.getLabel(),
                            metakey.getTooltip(),
                            DiagramEditorDeployment.I18N.getMessage(STYLEKEY_PREFIX + key + CATEGORY_SUFFIX));
    }

    /**
     * Creates a style key.
     * @param key The style key id.
     * @param type The type of the style key.
     * @return a StyleKey with i18n informations filled.
     */
    @objid ("974cb866-55b6-11e2-877f-002564c97630")
    public static StyleKey createStyleKey(String key, Class<?> type) {
        return new StyleKey(key,
                            type,
                            DiagramEditorDeployment.I18N.getMessage(STYLEKEY_PREFIX + key + LABEL_SUFFIX),
                            DiagramEditorDeployment.I18N.getMessage(STYLEKEY_PREFIX + key + TOOLTIP_SUFFIX),
                            DiagramEditorDeployment.I18N.getMessage(STYLEKEY_PREFIX + key + CATEGORY_SUFFIX));
    }

}
