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
package org.modelio.edition.html.epfcommon.html;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.edition.html.epfcommon.IHTMLFormatter;
import org.modelio.edition.html.epfcommon.utils.StrUtil;
import org.w3c.tidy.Tidy;

/**
 * Pretty-formats HTML source and makes it XHTML compliant.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("d6b8ed76-6a28-40c2-91a3-79f74a879974")
public class DefaultHTMLFormatter implements IHTMLFormatter {
    @objid ("d0b92a94-2e21-4219-83c5-22c14cbe3f51")
    protected int lineWidth;

    @objid ("6ad8db60-04d1-4eaa-b768-bf489d9f1e08")
    protected boolean indent;

    @objid ("357d10cd-809d-4282-a10e-6d60b90c7256")
    protected int indentSize;

    @objid ("9b3b521f-d799-4969-a2da-967a1157d8c3")
    protected String lastErrorStr;

    @objid ("57bf50f4-38fc-4af0-918f-463908108fe0")
    protected static final String PRE_TAG_START = "<pre>"; // $NON-NLS-1$

    @objid ("2330e973-c2b1-4b77-a9f2-a398b1e2a478")
    protected static final String PRE_TAG_END = "</pre>"; // $NON-NLS-1$

    @objid ("3f75fd48-7a14-4d81-a254-248709bad081")
    protected static final int PRE_TAG_END_LENGTH = PRE_TAG_END.length();

    @objid ("d1b3a256-922f-4046-becb-0762ce4689b4")
    private boolean defReturnBodyOnly;

    @objid ("ac799c46-7b8b-46c4-aaa3-223b932d42e6")
    private boolean defForceOutput;

    @objid ("9f11483e-5ba0-4cde-9d55-2cb80d8cbe9c")
    private boolean defMakeBare;

    @objid ("40b86a97-40ce-4803-ae2a-4ae054895faf")
    private boolean defWord2000;

    /**
     * Creates a new instance.
     * <p>
     * Line width is 132, idents with 4 spaces per tab.
     */
    @objid ("1fc4fe7f-df4f-46c3-b3f6-5647422fe2ce")
    public DefaultHTMLFormatter() {
        this(132, true, 4);
    }

    /**
     * Creates a new instance.
     * @param lineWidth maximum line width
     * @param indent <i>true</i> to indent, <i>false</i> to not indent
     * @param indentSize indent tab size.
     */
    @objid ("9c2ba3b5-af45-41ff-93df-6f61479e9a88")
    public DefaultHTMLFormatter(int lineWidth, boolean indent, int indentSize) {
        this.lineWidth = lineWidth;
        this.indent = indent;
        this.indentSize = indentSize;
        
        this.defReturnBodyOnly = true;
        this.defForceOutput = false;
        this.defMakeBare = false;
        this.defWord2000 = false;
    }

    /**
     * Sets the maximum character width of a line.
     * @param lineWidth The line width (in number of characters).
     */
    @objid ("f4a7ba07-6397-45d8-a96f-58841fb33293")
    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Enables or disables tags indent.
     * @param indent If true, ident the tags.
     */
    @objid ("7dc3df47-8bfb-4be1-a99d-db65170bc2bf")
    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    /**
     * Sets the indent size.
     * @param indentSize The indent size (in number of characters).
     */
    @objid ("b148d811-f98b-432a-8edd-566b3293a3b1")
    public void setIndentSize(int indentSize) {
        this.indentSize = indentSize;
    }

    /**
     * Formats the given HTML source.
     * @param html The HTML source.
     * @return The pretty-formatted HTML source.
     */
    @objid ("348961e6-bd00-47e3-9abb-ac6c34846413")
    @Override
    public String formatHTML(String html) throws UnsupportedEncodingException {
        return formatHTML(html, this.defReturnBodyOnly, this.defForceOutput, this.defMakeBare, this.defWord2000);
    }

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
    @objid ("a61faba2-75ea-410b-a1f5-ce4796b3049d")
    @Override
    public String formatHTML(String html, boolean returnBodyOnly, boolean forceOutput, boolean makeBare, boolean word2000) throws UnsupportedEncodingException {
        this.lastErrorStr = null;
        String editedHtml = html;
        
        if (editedHtml == null || editedHtml.length() == 0) {
            return editedHtml;
        }
        
        editedHtml = removeLeadingWhitespace(editedHtml);
        
        Tidy tidy = new Tidy();
        tidy.setXHTML(!returnBodyOnly);
        tidy.setDropEmptyParas(false);
        tidy.setDropFontTags(false);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setSmartIndent(false);
        tidy.setTidyMark(false);
        tidy.setWraplen(this.lineWidth);
        tidy.setIndentAttributes(false);
        tidy.setIndentContent(this.indent);
        tidy.setSpaces(this.indentSize);
        tidy.setFixBackslash(false);
        
        // this will add <p> around each text block (?that isn't in a block already?)
        //tidy.setEncloseBlockText(true);
        
        // (WRONG: setting this seemed to prevent JTidy from indenting the source)
        // this strips <html>, <head>, <body> tags
        tidy.setPrintBodyOnly(returnBodyOnly);
        
        // output document even if errors are present
        tidy.setForceOutput(forceOutput);
        
        // remove MS clutter
        tidy.setMakeBare(makeBare);
        
        // This removes <u> & <i> and makes <div style ="....">
        //tidy.setMakeClean(true);
        
        // draconian Word2000 cleaning
        tidy.setWord2000(word2000);
        
        StringWriter errorSw = new StringWriter();
        tidy.setErrout(new PrintWriter(errorSw));
        StringReader input= new StringReader(editedHtml);
        
        StringWriter output = new StringWriter();
        tidy.parse(input, output);
        
        String error = errorSw.getBuffer().toString();
        if (error != null && error.length() > 0
                && error.startsWith("line") && error.indexOf("column") > 0) { //$NON-NLS-1$ //$NON-NLS-2$
            this.lastErrorStr = error;
            if (!forceOutput) {
                // if forceOutput is true, JTidy will return clean HTML so don't return here
                return editedHtml;
            }
        }
        
        String formattedHTML = output.toString();
        formattedHTML = StrUtil.getEscapedHTML(formattedHTML);
        return formattedHTML;
    }

    /**
     * Returns the indent string.
     */
    @objid ("4abb698b-a482-4c29-8af0-c87c3f86bd03")
    protected static String getIndentStr(int indentLength) {
        if (indentLength == 0) {
            return ""; //$NON-NLS-1$
        }
        StringBuilder indentStr = new StringBuilder();
        for (int i = 0; i < indentLength; i++) {
            indentStr.append(' ');
        }
        return indentStr.toString();
    }

    /**
     * Undo the JTidy indent, but ignore &lt;pre&gt; tags
     * @param aHtml a HTML string
     * @param indentStr the indentation  string to remove
     * @return the fixed string
     */
    @objid ("3d1f31af-dd5b-435f-a0c7-4f0812466398")
    protected static String fixIndentation(String aHtml, String indentStr) {
        String html = aHtml;
        if (html.startsWith(indentStr)) {
            html = html.substring(indentStr.length());
        }
        StringBuilder strBuf = new StringBuilder();
        int pre_index = -1;
        int last_pre_end_index = -1;
        while ((pre_index = html.indexOf(PRE_TAG_START, last_pre_end_index)) != -1) {
            strBuf.append(html.substring(
                    last_pre_end_index < 0 ? 0 : last_pre_end_index
                            + PRE_TAG_END_LENGTH, pre_index).replaceAll(
                    "\r\n" + indentStr, "\r\n")); //$NON-NLS-1$ //$NON-NLS-2$
            last_pre_end_index = html.indexOf(PRE_TAG_END, pre_index);
            if (last_pre_end_index != -1) {
                strBuf.append(html.substring(pre_index, last_pre_end_index
                        + PRE_TAG_END_LENGTH));
            } else {
                // found <pre>, but no ending </pre> - shouldn't ever get here
                // append rest of string and return it
                strBuf.append(html.substring(pre_index));
                return strBuf.toString();
            }
        }
        strBuf.append(html.substring(
                last_pre_end_index < 0 ? 0 : last_pre_end_index
                        + PRE_TAG_END_LENGTH).replaceAll("\r\n" + indentStr, //$NON-NLS-1$
                "\r\n")); //$NON-NLS-1$
        return strBuf.toString();
    }

    @objid ("89f6259d-c3ef-4396-9dbc-df738d68979d")
    @Override
    public String getLastErrorStr() {
        return this.lastErrorStr;
    }

    @objid ("22ed1e13-eda8-4637-9310-cb082bb7d00f")
    @Override
    public String removeLeadingWhitespace(String input) {
        return p_whitespace.matcher(input).replaceAll(""); //$NON-NLS-1$
    }

    /**
     * @param defForceOutput force putput by default
     */
    @objid ("2a493733-2b22-4aee-97d6-f2056a7f6f18")
    public void setForceOutput(boolean defForceOutput) {
        this.defForceOutput = defForceOutput;
    }

    /**
     * @param defMakeBare clean MS clutter by default
     */
    @objid ("a7439fd6-04e8-4e2c-9dd4-1467e46f0a7c")
    public void setMakeBare(boolean defMakeBare) {
        this.defMakeBare = defMakeBare;
    }

    /**
     * @param defReturnBodyOnly return only body by default
     */
    @objid ("a52e29ad-27af-4fe2-a5ac-cfb40f4bfdb9")
    public void setReturnBodyOnly(boolean defReturnBodyOnly) {
        this.defReturnBodyOnly = defReturnBodyOnly;
    }

    /**
     * @param defWord2000 clean all Word 2000 tags by default
     */
    @objid ("7a61f5ce-0daa-4e75-a7b3-bcbd1408e691")
    public void setWord2000(boolean defWord2000) {
        this.defWord2000 = defWord2000;
    }

}
