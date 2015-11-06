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
                                    

package com.sun.star.comp.beans;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * OS Detection service class.
 * @author cmarin
 */
@objid ("b9b53cb5-da43-4d8e-a527-b095fe6cd612")
public class OSDetect {
    /**
     * Tells whether the OS is a Linux
     * @return <code>true</code> if the OS is Linux.
     */
    @objid ("7cac20e4-5fd6-42c1-b298-60bc3143b11b")
    public static boolean isLinux() {
        return ! isWindows() ;
    }

    /**
     * Don't instantiate.
     */
    @objid ("2f9d665f-fef9-43ba-b7cd-c1952b3d58c5")
    private OSDetect() {
    }

    /**
     * Tells whether the OS is a Windows
     * @return <code>true</code> if the OS is Windows.
     */
    @objid ("f147a91d-b9c8-4095-920b-b9cdca218fe3")
    public static boolean isWindows() {
        return (System.getProperty("os.name").toLowerCase().startsWith("win")) ;
    }

}
