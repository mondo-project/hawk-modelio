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
                                    

package org.modelio.vaudit.nsuse;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.data.project.FragmentType;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("9f91583c-9917-4541-b8a8-5b44e3e4443e")
class NSUseUtils {
    /**
     * Remove the NSU caused by the passed element.
     * <p>
     * Apply recursively for those NSUs that becomes 'no cause'.
     * @param cause the element to forget from blue links
     */
    @objid ("094582dd-957c-476f-a043-d5e1700e1470")
    public static void dereferenceNSUsesCausedBy(Element cause) {
        //if (Vaudit.LOG.isDebugEnabled())
        //    Vaudit.LOG.debug("\t\t\tdereference %s", cause.toString());
        
        List<NamespaceUse> uses = cause.getCausing();
        if (uses.size() > 0) {
            for (NamespaceUse ns : new ArrayList<>(uses)) {
                if (ns.isValid()) {
                    ns.getCause().remove(cause);
                    if (ns.getCause().isEmpty()) {
                        dereferenceNSUsesCausedBy(ns);
                        // delete non referenced use
                        //if (Vaudit.LOG.isDebugEnabled())
                        //    Vaudit.LOG.debug("\t\t\tdeleting ns: %s", ns.toString());
                        ns.delete();
                    }
                }
            }
        }
    }

    @objid ("6d7abc33-1d6a-45da-b381-6bfef4d9de47")
    public static NameSpace getNameSpaceOwner(MObject element) {
        if (element == null)
            return null;
        
        if (element instanceof NameSpace)
            return (NameSpace) element;
        
        MObject owner;
        for (owner = element.getCompositionOwner(); owner != null && !owner.equals(element) && owner.isValid()
                && !(owner instanceof NameSpace); owner = owner.getCompositionOwner()) {
            // empty
        }
        
        if (owner == null || owner.equals(element) || !owner.isValid() || !(owner instanceof NameSpace))
            return null;
        else
            return (NameSpace) owner;
    }

    /**
     * Compute the composition path to the 2 given namespace, starting to the
     * common composition owner.
     * <p>
     * There may be no common composition root if the elements belong to
     * different repositories/fragments. In this case the full composition tree
     * is returned.
     * @param firstElement a namespace
     * @param otherElement another namespace
     * @param firstPath the composition path from the common root to the first
     * namespace.
     * @param otherPath the composition path from the common root to the second
     * namespace.
     * @return the common composition root or <code>null</code>.
     */
    @objid ("3b6962d8-dc3b-47dd-9c9f-68c2879f5ed8")
    public static NameSpace getRelativePathsFromCommonRoot(NameSpace firstElement, NameSpace otherElement, Deque<NameSpace> firstPath, Deque<NameSpace> otherPath) {
        MObject o = firstElement.getCompositionOwner();
        
        // std::list < NameSpace * > otherPath;
        
        while (o != null && o instanceof NameSpace) {
            firstPath.addFirst((NameSpace) o);
            o = o.getCompositionOwner();
        }
        
        o = otherElement.getCompositionOwner();
        while (o != null && o instanceof NameSpace) {
            otherPath.addFirst((NameSpace) o);
            o = o.getCompositionOwner();
        }
        NameSpace lastCommon = null;
        
        while (!firstPath.isEmpty() && !otherPath.isEmpty()) {
            NameSpace fns = firstPath.getFirst();
            NameSpace ons = otherPath.getFirst();
            if (ons.equals(fns)) {
                lastCommon = fns;
                firstPath.removeFirst();
                otherPath.removeFirst();
            } else
                break;
        }
        return lastCommon;
    }

    @objid ("98682b58-3134-4acf-a386-eff8f8a75195")
    public static boolean isEditableFragment(IProjectFragment f) {
        return (f.getType() == FragmentType.EXML || f.getType() == FragmentType.EXML_SVN);
    }

}
