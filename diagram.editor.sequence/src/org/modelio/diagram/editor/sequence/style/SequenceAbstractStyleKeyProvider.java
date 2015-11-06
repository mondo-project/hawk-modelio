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
                                    

package org.modelio.diagram.editor.sequence.style;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.sequence.plugin.DiagramEditorSequence;
import org.modelio.diagram.styles.core.AbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

@objid ("d9a6cdda-55b6-11e2-877f-002564c97630")
public class SequenceAbstractStyleKeyProvider extends AbstractStyleKeyProvider {
    @objid ("d9a6cdde-55b6-11e2-877f-002564c97630")
    private static final String CATEGORY_SUFFIX = ".category";

    @objid ("d9a6cde0-55b6-11e2-877f-002564c97630")
    private static final String LABEL_SUFFIX = ".label";

    @objid ("d9a6cde2-55b6-11e2-877f-002564c97630")
    private static final String TOOLTIP_SUFFIX = ".tooltip";

    @objid ("d9a6cde4-55b6-11e2-877f-002564c97630")
    private static final String STYLEKEY_PREFIX = "StyleKey.";

    @objid ("d9a6cde6-55b6-11e2-877f-002564c97630")
    public static StyleKey createStyleKey(final String key, final MetaKey metakey) {
        return new StyleKey(key,
                            metakey,
                            metakey.getLabel(),
                            metakey.getTooltip(),
                            DiagramEditorSequence.I18N.getMessage(STYLEKEY_PREFIX + key + CATEGORY_SUFFIX));
    }

    @objid ("d9a6cdf3-55b6-11e2-877f-002564c97630")
    public static StyleKey createStyleKey(final String key, final Class<?> type) {
        return new StyleKey(key,
                            type,
                            DiagramEditorSequence.I18N.getMessage(STYLEKEY_PREFIX + key + LABEL_SUFFIX),
                            DiagramEditorSequence.I18N.getMessage(STYLEKEY_PREFIX + key + TOOLTIP_SUFFIX),
                            DiagramEditorSequence.I18N.getMessage(STYLEKEY_PREFIX + key + CATEGORY_SUFFIX));
    }

}
