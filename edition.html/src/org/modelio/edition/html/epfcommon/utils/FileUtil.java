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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.edition.html.plugin.HtmlTextPlugin;

/**
 * Utility class for managing directories and files.
 * 
 * @author Kelvin Low
 * @author Jinhua Xi
 * @since 1.0
 */
@objid ("ed3d0e8c-7f3b-40e2-a75d-d22c35723e04")
public class FileUtil {
    /**
     * Platform-specific line separator.
     */
    @objid ("8dfb7fe8-72ed-4dc5-bb25-e7691b6c6e41")
    public static final String LINE_SEP = System.getProperty("line.separator"); // $NON-NLS-1$

    /**
     * Platform-specific file separator.
     */
    @objid ("3b4f8a3b-d01e-48a0-9532-501004f324cd")
    public static final String FILE_SEP = System.getProperty("file.separator"); // $NON-NLS-1$

    /**
     * Platform-specific line separator length.
     */
    @objid ("6c029f26-b007-419e-ac30-d095fe67421a")
    public static final int LINE_SEP_LENGTH = LINE_SEP.length();

    /**
     * UNC path prefix.
     */
    @objid ("eef57863-f90a-4044-a9eb-6b502aa1b798")
    public static final String UNC_PATH_PREFIX = "\\\\"; // $NON-NLS-1$

    /**
     * UNC path prefix length.
     */
    @objid ("b6bc40bb-2e91-478c-abe5-0c7d2ce0cb29")
    public static final int UNC_PATH_PREFIX_LENGTH = UNC_PATH_PREFIX.length();

    /**
     * ISO-8859-1 encoding.
     */
    @objid ("620f0eff-a883-4cb6-a148-f926efaed38c")
    public static final String ENCODING_ISO_8859_1 = "ISO-8859-1"; // $NON-NLS-1$

    /**
     * UTF-8 encoding.
     */
    @objid ("9d4fd532-1a30-4bcb-b502-5d43b2967bf2")
    public static final String ENCODING_UTF_8 = "UTF-8"; // $NON-NLS-1$

    /**
     * Private constructor to prevent this class from being instantiated. All
     * methods in this class should be static.
     */
    @objid ("420515c8-03af-4bab-b9cf-ccef6fc31997")
    private FileUtil() {
    }

    /**
     * Returns the parent directory of a path.
     * @param path a path name
     * @return the name of the parent directory
     */
    @objid ("281eb76c-f4eb-435b-a6d6-4e8f1010f594")
    public static String getParentDirectory(String path) {
        return (new File(path)).getParent();
    }

    /**
     * Returns the file name and extension from a path.
     * @param path a path name
     * @return the file name including the file extension
     */
    @objid ("71587a83-020a-41ac-a250-b7d050be2225")
    public static String getFileName(String path) {
        return getFileName(path, true);
    }

    /**
     * Returns the file name from a path, with or without the file extension.
     * @param path a path name
     * @param withExtension if <code>true</code>, include the file extension in the
     * result
     * @return the file name with or without the file extension
     */
    @objid ("cb54a57b-5a51-4476-b49f-4200bc3b9fc0")
    public static String getFileName(String path, boolean withExtension) {
        String normalizedPath = path.replace('\\', '/');
        
        int prefixLength = 0;
        if (normalizedPath.startsWith(NetUtil.FILE_URI_PREFIX)) {
            prefixLength = NetUtil.FILE_URI_PREFIX_LENGTH;
        } else if (normalizedPath.startsWith(NetUtil.HTTP_URI_PREFIX)) {
            prefixLength = NetUtil.HTTP_URI_PREFIX_LENGTH;
        }
        
        String fileName;
        int index = normalizedPath.lastIndexOf("/"); //$NON-NLS-1$
        if (index < prefixLength) {
            fileName = normalizedPath.substring(prefixLength);
        } else {
            fileName = path.substring(index + 1);
        }
        
        if (withExtension) {
            return fileName;
        }
        
        index = fileName.indexOf("."); //$NON-NLS-1$
        return (index > 0) ? fileName.substring(0, index) : fileName;
    }

    /**
     * Appends the platform specific path separator to the end of a path.
     * @param path a path name
     * @return the path name appended with the platform specific path separator
     */
    @objid ("e5927349-58eb-4a3d-b6cd-a2e0ac23efb7")
    public static String appendSeparator(String path) {
        return appendSeparator(path, File.separator);
    }

    /**
     * Appends the given path separator to the end of a path
     * @param path a path name
     * @param separator a path separator
     * @return the path name appended with the given separator
     */
    @objid ("a303c71d-ff47-4648-b84e-7d135feeda4f")
    public static String appendSeparator(String path, String separator) {
        return path.endsWith(separator) ? path : path + separator;
    }

    /**
     * Copies the content of the source file to the target file. Will overwrite
     * an existing file if it has write permission
     * @param srcFile the source file or path
     * @param tgtFile the target file or path
     * @return <i>true</i> on success, <i>false</i> on failure.
     */
    @objid ("fccf71e1-ccfb-4f52-adf0-9eca73c11051")
    public static boolean copyFile(File srcFile, File tgtFile) {
        Map<File, File> map = null; //TODO maybe: getCopiedFileMap();
        File keyFile = null;
        File valFile = null;
        if (map != null) {
            try {
                keyFile = tgtFile.getCanonicalFile();
                valFile = srcFile.getCanonicalFile();
                if (valFile.equals(map.get(keyFile))) {
                    return true;
                }
            } catch (Exception e) {
                keyFile = valFile = null;
            }            
        }
                
        try {
            boolean ret = copyfile(srcFile, tgtFile);
            if (map != null && keyFile != null && valFile != null) {
                map.put(keyFile, valFile);
            }
            return ret;
        } catch (IOException ex) {
            HtmlTextPlugin.getDefault().getLogger().warning(ex);
            return false;
        }
    }

    @objid ("55968085-0f8a-4f7f-831d-aa585b198357")
    private static boolean copyfile(File srcFile, File tgtFile) throws IOException {
        Files.copy(srcFile.toPath(), tgtFile.toPath());
        return true;
    }

}
