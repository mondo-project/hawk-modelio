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

import java.net.URI;
import java.util.Map;
import java.util.regex.Pattern;
import com.ibm.icu.lang.UCharacter;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.modelio.edition.html.plugin.HtmlTextPlugin;

/**
 * A helper class for manuipulating strings.
 * 
 * @author Kelvin Low
 * @author Jinhua Xi
 * @since 1.0
 */
@objid ("278284d3-a126-4391-9774-c3303630b86d")
@SuppressWarnings("javadoc")
public class StrUtil {
    @objid ("a58fc692-73c9-4754-b9bd-e3c02467936f")
    public static final String EMPTY_STRING = ""; // $NON-NLS-1$

    @objid ("6e29792d-eaca-496a-9ad1-71c423034a5f")
    public static final String TAB = "\t"; // $NON-NLS-1$

    @objid ("f2bb075d-8f40-4848-be50-63d2a91e66a4")
    private static final String REGEXP_ANY_SPECIAL_CHARS = "(`|~|!|@|#|\\$|%|\\^|&|\\*|\\(|\\)|\\+|=|\\[|\\]|\\||\\:|\"|<|>|\\?|/|'|\\s|\\\\)+"; // $NON-NLS-1$

    @objid ("05526f57-4542-400c-a3b0-8bef5ea3140d")
    private static final String REGEXP_INVALID_PUBLISHED_PATH_CHARS = "(\\[|#|\\*|\\?|\"|<|>|\\||!|%|/|\\])+"; // $NON-NLS-1$

    @objid ("834ff0b7-9326-4eec-be8c-cf57382085f0")
    private static final String REGEXP_INVALID_PUBLISHED_PATH_CHARS_LINUX = "(\\[|#|\\*|\\?|\"|<|>|\\||!|%|\\])+"; // $NON-NLS-1$

    @objid ("5ffe7a87-c071-48a8-b62a-5e445e818706")
    private static final String REGEXP_INVALID_FILENAME_CHARS = "(\\[|#|/|\\\\|\\:|\\*|\\?|\"|<|>|\\||\\]|\\s)+"; // $NON-NLS-1$

    @objid ("b6393173-cfa9-4571-a11e-dadb9440dc0d")
    public static final String LINE_FEED = System.getProperty("line.separator"); // $NON-NLS-1$

    @objid ("994676bd-4ca1-414e-a735-ebacca6243e0")
    public static final String ESCAPED_LF = "&#xA;"; // $NON-NLS-1$

    @objid ("1b0eaafe-e062-4f5c-9e6a-c1fd01d5a63a")
    public static final String ESCAPED_CR = "&#xD;"; // $NON-NLS-1$

    @objid ("6ddb81b5-8500-4c2e-92e8-271062d182fb")
    public static final String ESCAPED_LINE_FEED = LINE_FEED.replace(
			"\n", ESCAPED_LF).replace("\r", ESCAPED_CR); // $NON-NLS-1$ //$NON-NLS-2$

    @objid ("274c2d8f-033c-41d1-a5b6-03f1cb822f0f")
    public static final String LINE_FEED_REGEX = LINE_FEED.replaceAll(
			"\\\\", "\\\\"); // $NON-NLS-1$ //$NON-NLS-2$

    @objid ("c1250c77-7595-4bc3-a4c4-6a3c6a545ffd")
    public static final String ESCAPED_LINE_FEED_REGEX = ESCAPED_LINE_FEED;

    @objid ("96ff4c95-db9f-4844-8495-129f160c85d2")
    public static final String HTML_BREAK = "<br/>"; // $NON-NLS-1$

    @objid ("98c35e64-8339-45bc-9521-ac18cdf6719c")
    public static final String HTML_COPY = "&copy;"; // $NON-NLS-1$

    @objid ("9579a1d7-f7c4-4dfc-a0d8-2c7df923ec9e")
    public static final String HTML_EURO = "&euro;"; // $NON-NLS-1$

    @objid ("1ea227b1-8995-4da8-869c-56c0fcba2d8f")
    public static final String HTML_REG = "&reg;"; // $NON-NLS-1$

    @objid ("bb97fd55-66b5-479e-8521-57576ab41811")
    public static final String HTML_TRADEMARK = "&trade;"; // $NON-NLS-1$

    @objid ("43cbe7dc-36c8-477a-92ba-8671dbf981f2")
    public static boolean during_migration = false;

    @objid ("28bc218c-755b-403a-a3fd-663bd6dc8cf1")
    private static StrUtilOptions options;

    @objid ("69ac27d5-6391-43ca-b1d3-0e8c2a1c099b")
    private static Pattern REGEX_IS_HTML = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);

    /**
     * Private constructor to prevent this class from being instantiated. All
     * methods in this class should be static.
     */
    @objid ("870956ce-0dd4-4474-841b-9b589ddc0c84")
    private StrUtil() {
    }

    /**
     * Tests for null string.
     * <p>
     * A null string is defined as one that has an empty reference or has zero
     * length.
     * @param str a string
     * @return <code>true</code> if the given string is a null string
     */
    @objid ("4cf71c86-568f-4fa5-8d00-cee9f508e3ed")
    public static boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Tests for blank string.
     * <p>
     * A blank string is defined as one that has an empty reference or has zero
     * length after the leading and trailing space characters are trimmed.
     * @param str a string
     * @return <code>true</code> if the given string is a blank string
     */
    @objid ("702053d8-835b-4728-9c23-463eb8dadec9")
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * Removes the leading and trailing space characters from a string.
     * @param str a string
     * @return a string with no leading and trailing space characters
     */
    @objid ("2314b4ed-20d2-46fc-9a84-331a0eaa723e")
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * Returns the escaped HTML representation of a string.
     * @param html a HTML string
     * @return the escaped Unicode representation of the given HTML string
     */
    @objid ("e38a4d0b-0720-49eb-87f4-2da37128d057")
    public static String getEscapedHTML(String html) {
        if (html == null || html.length() == 0) {
            return ""; //$NON-NLS-1$
        }
        
        StrUtilOptions options = getOptions();
        StringBuffer result = new StringBuffer();
        int length = html.length();
        for (int i = 0; i < length; i++) {
            char ch = html.charAt(i);
            switch (ch) {
            case '%':
                if (i + 4 < length) {
                    String hexStr = html.substring(i + 1, i + 5);
                    boolean validHextStr = true;
                    
                    for (int j = 0; j < hexStr.length(); j++) {
                        char c = hexStr.charAt(j);
                        if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
                            validHextStr = false;
                            break;
                        }
                    }
                    
                    if (options == null) {
                        //code below will treat "%20de" as " de"
                        //this may lose some double bytes character(e.g. Chinese), which start with %20, but keep all url links
                        //so far open this convertion not only during library migration to support file like "my design.gif"
                        if (/*during_migration && */validHextStr) {
                            if (hexStr.startsWith("20")) { //$NON-NLS-1$
                                result.append("%20"); //$NON-NLS-1$
                                i += 2;
                                break;
                            }
                        }
                    } else {
                        int ix = options.getRteUrlDecodingOption();
                        if (ix == 1) {
                            validHextStr = false;
                        } else if (ix == 2) {
                            String key = getHexStr("%" + hexStr);
                            if (key != null && options.getRteUrlDecodingHexMap().containsKey(key)) {
                                validHextStr = false;
                            }
                        }
                    }
                    
                    if (validHextStr) {
                        try {
                            int codePoint = Integer.parseInt(hexStr, 16);
                            char[] c = UCharacter.toChars(codePoint);
                            result.append(c);
                            i += 4;
                            break;
                        } catch (NumberFormatException e) {
                            // wasn't a valid hex string..
                            // fall through to the result.append(ch)
                        } catch (Exception e) {
                            HtmlTextPlugin.getDefault().getLogger().error(e);
                        }
                    }
                }
                result.append(ch);
                break;
            case '\u00a9':
                result.append(HTML_COPY);
                break;
            case '\u00ae':
                result.append(HTML_REG);
                break;
            case '\u20ac':
                result.append(HTML_EURO);
                break;
            case '\u2122':
                result.append(HTML_TRADEMARK);
                break;
            default:
                result.append(ch);
                break;
            }
        }
        return result.toString();
    }

    /**
     * Returns the plain text from HTML text.
     * <p>
     * Note: All HTML tags will be stripped.
     * @param html the HTML text.
     * @return the plain text representation of the given HTML text
     */
    @objid ("543f8c9b-5cf6-4072-93fa-7e0c6737e6e3")
    public static String getPlainText(String html) {
        if (html == null) {
            return ""; //$NON-NLS-1$
        }
        
        final Pattern p_plaintext_filter = Pattern.compile(
                "<[^>]*?>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); //$NON-NLS-1$
        final Pattern p_plaintext_filter2 = Pattern.compile(
                "&.{1,5}[^;];", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); //$NON-NLS-1$
        final Pattern p_plaintext_filter3 = Pattern.compile(
                "\\s+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL); //$NON-NLS-1$
        
        String result = html.replaceAll(p_plaintext_filter.pattern(), " ") //$NON-NLS-1$
                .replaceAll(p_plaintext_filter2.pattern(), " ") //$NON-NLS-1$
                .replaceAll(p_plaintext_filter3.pattern(), " "); //$NON-NLS-1$
        return result;
    }

    /**
     * Converts a string into a valid file name.
     * @param str a string
     * @return a valid file name derived from the given string
     */
    @objid ("2a5e46f4-98a4-411e-af78-cad2b5189fdc")
    public static String makeValidFileName(String str) {
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        return getPlainText(str)
                .replaceAll(REGEXP_INVALID_FILENAME_CHARS, " ").trim(); //$NON-NLS-1$
    }

    /**
     * Returns true if the path does not contain any invalid filename
     * characters.
     * @param path the file path
     * @return <code>true</code> if the given path contains only valid
     * filename characters
     */
    @objid ("300178d0-cad4-43d9-838a-957fb8c3795a")
    public static boolean isValidPublishPath(String path) {
        // return path.replaceAll(invalidPublishPathCharsRegExp,
        // "").equals(path);
        
        if (Platform.getOS().equals(Platform.WS_WIN32)) {
            return path
                    .replaceAll(REGEXP_INVALID_PUBLISHED_PATH_CHARS, "").equals(path); //$NON-NLS-1$
        }
        
        // else default to Linux
        return path
                .replaceAll(REGEXP_INVALID_PUBLISHED_PATH_CHARS_LINUX, "").equals(path); //$NON-NLS-1$
    }

    /**
     * Converts the platform line-separator (\n or \n\r or \r) to &lt;br/&gt;
     * @param text @return
     */
    @objid ("38b3dd13-cdee-4616-9e8d-23be37a7411c")
    public static String convertNewlinesToHTML(String text) {
        if (text != null) {
            text = text.replaceAll(LINE_FEED_REGEX, HTML_BREAK + LINE_FEED);
            text = text.replaceAll(ESCAPED_LINE_FEED_REGEX, HTML_BREAK
                    + ESCAPED_LINE_FEED);
        }
        return text;
    }

    @objid ("be853ade-6b7a-47c1-9d88-15e7ea9def92")
    public static URI toURI(String pathStr) {
        if (pathStr != null && pathStr.length() > 0) {
            IPath path = Path.fromOSString(pathStr);
            try {
                return path.toFile().toURI();
            } catch (Exception e) {
                HtmlTextPlugin.getDefault().getLogger().error(e);
            }
        }
        return null;
    }

    @objid ("239cc561-2aa0-4abc-9c59-5d93cf9ccd34")
    public static String escapeChar(String text, char c) {
        int i=text.indexOf(c); 
        if ( i < 0 ) {
            return text;
        }
        
        int start = 0;
        StringBuffer buffer = new StringBuffer();
        while ( i > start ) {
            buffer.append(text.substring(start, i)).append("\\"); //$NON-NLS-1$
            start = i;
            i=text.indexOf(c, start+1); 
        }
        
        buffer.append(text.substring(start));
        return buffer.toString();
    }

    @objid ("0f94328f-71a8-4826-82d5-e3e76f322706")
    public static String getHexStr(String str) {
        if (str.length() < 3) {
            return null;
        }
        if (str.charAt(0) != '%') {
            return null;
        }
        StringBuffer b = new StringBuffer();
        b.append('%');
        for (int i = 1 ; i <= 2; i++) {
            char c = str.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c -= 'a';
                c += 'A'; 
            }             
            boolean valid = (c >= '0' && c <= '9') ||
                            (c >= 'A' && c <= 'F');
            if (!valid) {
                return null;
            }
            b.append(c);            
        }
        return b.toString();
    }

    @objid ("2fc87cc7-f94f-4b29-9117-dcf478d387a7")
    private static StrUtilOptions getOptions() {
        return options;
    }

    @objid ("0daf3c05-6ceb-4409-9e9f-688c6eea092f")
    public static boolean isHtml(String str) {
        return REGEX_IS_HTML.matcher(str).matches();
    }

    @objid ("8a2dfc59-bb66-4d05-9436-4a8d85df7a66")
    public interface StrUtilOptions {
        @objid ("810c067e-dfe7-4908-863d-2f943c5ddd8e")
        int getRteUrlDecodingOption();

        @objid ("dd55b29e-2796-4384-83db-bd1d4cb3db70")
        Map<String, String> getRteUrlDecodingHexMap();

    }

}
