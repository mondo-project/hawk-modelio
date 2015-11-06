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
                                    

package org.modelio.diagram.editor.usecase.elements.usecase;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.usecase.style.UseCaseAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

@objid ("5e5e0643-55b7-11e2-877f-002564c97630")
public class GmUseCaseImageStyleKeys extends UseCaseAbstractStyleKeyProvider {
    @objid ("d9bc824a-55c2-11e2-9337-002564c97630")
    public static final StyleKey REPMODE = createStyleKey("USECASE_REPMODE", MetaKey.REPMODE);

    @objid ("d9be08e9-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("USECASE_FONT", MetaKey.FONT);

    @objid ("d9be08ec-55c2-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("USECASE_TEXTCOLOR", MetaKey.TEXTCOLOR);

    @objid ("d9be08ef-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("USECASE_SHOWNAME", MetaKey.SHOWNAME);

    @objid ("d9be08f2-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("USECASE_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    @objid ("d9be08f5-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("USECASE_SHOWTAGS", MetaKey.SHOWTAGS);

}
