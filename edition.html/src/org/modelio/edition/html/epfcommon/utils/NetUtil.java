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
package org.modelio.edition.html.epfcommon.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Implements a utility class for managing URLs and URIs.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("c5864e02-e82e-4156-8fcb-9e066453cd51")
public class NetUtil {
    /**
     * FILE scheme.
     */
    @objid ("023b8775-25ff-44ab-9ff1-1444d3a6890c")
    public static final String FILE_SCHEME = "file"; // $NON-NLS-1$

    /**
     * File URI prefix.
     */
    @objid ("bbc40413-0e0c-4e1d-b323-918fd005222d")
    public static final String FILE_URI_PREFIX = FILE_SCHEME + ":/"; // $NON-NLS-1$

    /**
     * File URI prefix size.
     */
    @objid ("6a59f306-1784-4529-af19-318576225a9b")
    public static final int FILE_URI_PREFIX_LENGTH = FILE_URI_PREFIX.length();

    /**
     * HTTP scheme.
     */
    @objid ("d96ba592-551d-48bb-8558-960a3933e0f4")
    public static final String HTTP_SCHEME = "http"; // $NON-NLS-1$

    /**
     * HTTP URI prefix.
     */
    @objid ("b7972b9f-8848-44aa-b6f7-c20044fa057f")
    public static final String HTTP_URI_PREFIX = HTTP_SCHEME + "://"; // $NON-NLS-1$

    /**
     * HTTP URI prefix size.
     */
    @objid ("b65f0e04-4c41-4dea-9163-fb3086c2f281")
    public static final int HTTP_URI_PREFIX_LENGTH = HTTP_URI_PREFIX.length();

    @objid ("e4a3752a-6b39-4e10-b760-24bfe12cfd97")
    public static final String FILE_PREFIX_2 = "file://"; // $NON-NLS-1$

    @objid ("f4283bba-5197-499c-894a-4cd7ec6ca334")
    public static final String FILE_PREFIX_3 = "file:///"; // $NON-NLS-1$

    /**
     * Private constructor to prevent this class from being instantiated. All
     * methods in this class should be static.
     */
    @objid ("d4b31f5d-a12b-4043-9f63-6b86954ddfa5")
    private NetUtil() {
    }

    @objid ("d39c8c2c-d8b6-41da-b12b-b2418ae72fcb")
    public static String decodeURL(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, "UTF-8"); //$NON-NLS-1$
    }

}
