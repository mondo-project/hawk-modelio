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
                                    

package org.modelio.edition.html.epfcommon;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Extracts the title, meta tags and text from a HTML file or source.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("7ccfcb48-8c8a-4507-9242-1aff31061511")
public interface IHTMLParser {
    /**
     * Returns the body text.
     * @return the body text.
     */
    @objid ("eb102d91-9e95-440f-be89-e15384bf2394")
    String getText();

    /**
     * Returns the summary.
     * @return the summary.
     */
    @objid ("6a869259-7808-4680-ba0b-347cb9f30b86")
    String getSummary();

    /**
     * Returns the HTML meta tags.
     * @return the HTML meta tags.
     */
    @objid ("f0d4fb59-5cca-47da-ab2b-7f4bac0a7552")
    Properties getMetaTags();

    /**
     * Parses the given HTML file.
     * <p>
     * Returns empty string if the file does not exist or is not accessible.
     * @param file the HTML file to parse
     * @throws java.io.UnsupportedEncodingException If the Character Encoding is not supported.
     * @throws java.io.IOException in case of I/O error
     */
    @objid ("ec7ea427-f7ba-447f-b1f5-cc7fea0173e7")
    void parse(File file) throws IOException, UnsupportedEncodingException;

}
