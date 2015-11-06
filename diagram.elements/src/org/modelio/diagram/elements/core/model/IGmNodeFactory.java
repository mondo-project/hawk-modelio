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
                                    

package org.modelio.diagram.elements.core.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Factory to unmask an element under a composite graphic node.
 */
@objid ("80827ed1-1dec-11e2-8cad-001ec947c8cc")
public interface IGmNodeFactory {
    /**
     * Unmask an element under a composite graphic node.
     * @param parentNode the graphic node in which the element must be displayed
     * @param elementToUnmask The element to unmask
     * @param initialLayoutData The initial layout data of the node
     * @return The created graphic node
     */
    @objid ("80827ed3-1dec-11e2-8cad-001ec947c8cc")
    GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parentNode, MObject elementToUnmask, Object initialLayoutData);

    /**
     * Tells if a Graphical Model (Gm) class exist or will exist in the future for the given Ob class.
     * <p>
     * For information, there will be never Gm class for TaggedValue, TagParameter, IParameter.
     * @param metaclass the Ob class to test.
     * @return true if a Gm class may exist, false if no Gm class will ever exist.
     */
    @objid ("80827eda-1dec-11e2-8cad-001ec947c8cc")
    boolean isRepresentable(Class<? extends MObject> metaclass);

    /**
     * Register an GmNode factory extension.
     * <p>
     * Extension GmNode factories are called first when looking for an GmNode.<br>
     * Extension GmNode factories also have a class loader role when loading diagrams.
     * @param id id for the extension factory
     * @param factory the edit part factory.
     */
    @objid ("80827ee0-1dec-11e2-8cad-001ec947c8cc")
    void registerFactory(String id, IGmNodeFactory factory);

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("80827ee4-1dec-11e2-8cad-001ec947c8cc")
    void unregisterFactory(String id);

    /**
     * Returns the Class object associated with the class or interface with the given string name.
     * @param className The class name
     * @return The matching class or <i>null</i> if none was found.
     */
    @objid ("80827ee7-1dec-11e2-8cad-001ec947c8cc")
    Class<?> getClass(String className);

}
