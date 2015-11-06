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

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * HTML strings formatter interface.
 * <p>
 * To pretty-print HTML code.
 */
@objid ("f32aabad-c5c5-4c71-894e-ab9410ad06f8")
public interface IHTMLFormatter {
    /**
     * 
     * JTidy error line parsing pattern.
     * <p>
     * Usage:
     * <pre>
     * String location = m.group(1);
     * String lineStr = m.group(2);
     * String columnStr = m.group(3);
     * String errorMsg = m.group(4);
     * </pre>
     */
    @objid ("50d90c41-45cc-4a8b-ab26-1b88c03f6a65")
    public static final Pattern jTidyErrorParser = Pattern
			.compile(
					"(line\\s+(\\d+)\\s+column\\s+(\\d+))\\s+-\\s+(.+)", Pattern.CASE_INSENSITIVE); // $NON-NLS-1$

    /**
     * Whitespace regex.
     */
    @objid ("0d813073-e25f-4de8-8361-e3dc852d8f5e")
    public static final Pattern p_whitespace = Pattern.compile("^\\s+", Pattern.MULTILINE); // $NON-NLS-1$

    /**
     * Formats the given HTML source.
     * @param html The HTML source.
     * @param returnBodyOnly if false, return full HTML document or body content based on what is passed in.  if true, always return body content only
     * @param forceOutput if true, return cleaned HTML even if errors. if false, will clean minor problems and return clean HTML, but on a major error, will set getLastErrorStr() and return passed-in html
     * @param makeBare set to true for cleaning MS HTML
     * @param word2000 set to true for cleaning MS Word 2000 HTML
     * @return the pretty-formatted HTML
     * @throws java.io.UnsupportedEncodingException in case of encoding error
     */
    @objid ("798acd27-b600-4420-8588-97fd429f45d1")
    String formatHTML(String html, boolean returnBodyOnly, boolean forceOutput, boolean makeBare, boolean word2000) throws UnsupportedEncodingException;

    /**
     * Format HTML source with no options.
     * @param text the HTML to format.
     * @return the pretty-formatted HTML string.
     * @throws java.io.UnsupportedEncodingException in case of encoding error.
     */
    @objid ("ce9a2589-1b65-4d81-a9ab-80ad63ffbe08")
    String formatHTML(String text) throws UnsupportedEncodingException;

    /**
     * @return last error message.
     */
    @objid ("bf723deb-c4ea-4b80-9780-2ca36504b27c")
    String getLastErrorStr();

    /**
     * Remove leading white spaces.
     * @param input a string
     * @return the fixed string.
     */
    @objid ("9205047b-34d1-4663-9c41-b7e94dff4d36")
    String removeLeadingWhitespace(String input);

}
