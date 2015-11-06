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
package org.modelio.edition.html.epfcommon.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.edition.html.plugin.HtmlTextPlugin;

/**
 * A wrapper over the XSLT processor bundled with the JRE.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("1ebec9e7-f59b-4466-a19e-ed76b88c8392")
public class XSLTProcessor {
// If true, cache the compiled the XSL transformer with the compiled XSL
// templates.
    @objid ("685b0a08-5b0a-4d80-9b46-704b7485f0a0")
    private static boolean cacheXSL;

// Caches the XSL transformers.
    @objid ("44d320ef-1cf9-4e79-903b-83651e4eec3d")
    private static Map<String, CachedTransformer> transformerCache;

    /**
     * Default private constructor to prevent this class from being
     * instantiated.
     */
    @objid ("45f25d92-a26f-44bd-9488-8c5c4a84781c")
    private XSLTProcessor() {
    }

    /**
     * Executes the XSL transformation given the XSL source, XML source, target
     * output and encoding.
     * @param xslSource The XSL source.
     * @param xmlSource The XML source.
     * @param output The output target.
     * @param params The parameters for the XSL transformation.
     * @param encoding The target encoding.
     * @throws javax.xml.transform.TransformerException in case of XSL transformation failure
     */
    @objid ("c2bf24c0-2143-40cf-9f2d-ab6f42c95269")
    public static void transform(Source xslSource, Source xmlSource, Writer output, Properties params, String encoding) throws TransformerException {
        if (xslSource != null && xmlSource != null) {
            Transformer transformer = null;
            String xslFile = xslSource.getSystemId();
            if (cacheXSL && xslFile != null) {
                CachedTransformer cachedTransformer = null;
                synchronized (transformerCache) {
                    cachedTransformer = transformerCache.get(xslFile);
                    if (cachedTransformer == null) {
                        TransformerFactory factory = TransformerFactory
                                .newInstance();
                        transformer = factory.newTransformer(xslSource);
                        transformerCache.put(xslFile, new CachedTransformer(
                                transformer, params));
                    } else {
                        cachedTransformer.setParams(params);
                        transformer = cachedTransformer.getTransformer();
                    }
                }
            } else {
                TransformerFactory factory = TransformerFactory.newInstance();
                transformer = factory.newTransformer(xslSource);
        
                if (params != null && params.size() > 0) {
                    for (Iterator<Object> i = params.keySet().iterator(); i
                            .hasNext();) {
                        String paramName = (String) i.next();
                        String paramValue = params.getProperty(paramName);
                        transformer.setParameter(paramName, paramValue);
                    }
                }
            }
        
            if (encoding != null && encoding.length() > 0) {
                transformer.setOutputProperty("encoding", encoding); //$NON-NLS-1$
            } else {
                transformer.setOutputProperty("encoding", "utf-8"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            transformer.transform(xmlSource, new StreamResult(output));
        }
    }

    /**
     * Executes the XSL transformation given the XSL source, XML source, target
     * output and encoding.
     * @param xslSource The XSL source.
     * @param xmlSource The XML source.
     * @param output The output target.
     * @param encoding The target encoding.
     * @throws javax.xml.transform.TransformerException in case of failure
     */
    @objid ("74def411-d523-4334-8b78-0451906da630")
    public static void transform(Source xslSource, Source xmlSource, Writer output, String encoding) throws TransformerException {
        transform(xslSource, xmlSource, output, null, encoding);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML source,
     * target output and encoding.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlSource The XML source.
     * @param output The output target.
     * @param params The parameters for the XSL transformation.
     * @param encoding The target encoding.
     * @throws java.io.IOException in case of failure
     */
    @objid ("7b69bfac-aa42-46e6-9623-5b927c14597d")
    public static void transform(String xslUri, Source xmlSource, Writer output, Properties params, String encoding) throws IOException {
        try (InputStream xslInput = getXslInputStream(xslUri);) {
        
            StreamSource xslSource = new StreamSource(xslInput);
            xslSource.setSystemId(new File(xslUri));
            transform(xslSource, xmlSource, output, params, encoding);
        } catch (TransformerException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML source,
     * target output and encoding.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlSource The XML source.
     * @param output The output target.
     * @param encoding The target encoding.
     * @throws java.io.IOException in case of failure
     */
    @objid ("7d7fe3af-b479-4c60-bf80-1b020537c7db")
    public static void transform(String xslUri, Source xmlSource, Writer output, String encoding) throws IOException {
        transform(xslUri, xmlSource, output, null, encoding);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string,
     * target output and encoding.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param output The output target.
     * @param params The parameters for the XSL transformation.
     * @param encoding The target encoding.
     * @throws java.io.IOException in case of I/O error
     */
    @objid ("c2bb715e-0140-412a-93ef-0b2bee4ef235")
    public static void transform(String xslUri, String xmlStr, Writer output, Properties params, String encoding) throws IOException {
        try(InputStream xslInput = getXslInputStream(xslUri);) {
            if (xslInput != null) {
                StreamSource xslSource = new StreamSource(xslInput);
                xslSource.setSystemId(new File(xslUri));
        
                byte[] xml = xmlStr.getBytes("utf-8"); //$NON-NLS-1$
                try (ByteArrayInputStream xmlInput = new ByteArrayInputStream(xml);) {
                    StreamSource xmlSource = new StreamSource(xmlInput);
        
                    transform(xslSource, xmlSource, output, params, encoding);
        
                } catch (Exception e) {
                    HtmlTextPlugin.LOG.warning(e);
                }
            }
        }
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string,
     * target output and encoding.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param output The output target.
     * @param encoding The target encoding.
     * @throws java.io.IOException in case of failure
     */
    @objid ("d57712da-31d6-45a9-805d-d2f3ce9b42cf")
    public static void transform(String xslUri, String xmlStr, Writer output, String encoding) throws IOException {
        transform(xslUri, xmlStr, output, null, encoding);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string,
     * target output and encoding.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param file The output file.
     * @param params The parameters for the XSL transformation.
     * @param encoding The target encoding.
     * @throws java.io.IOException in case of failure
     */
    @objid ("66357eba-abe6-4a0c-abd8-426151b34b60")
    public static void transform(String xslUri, String xmlStr, File file, Properties params, String encoding) throws IOException {
        try (FileWriter output = new FileWriter(file);) {
            transform(xslUri, xmlStr, output, params, encoding);
        }
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string,
     * target output and encoding.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param file The output file.
     * @param encoding The target encoding.
     * @throws java.io.IOException in case of failure
     */
    @objid ("b46d6851-34b9-437f-9ade-d0b3800eefcf")
    public static void transform(String xslUri, String xmlStr, File file, String encoding) throws IOException {
        transform(xslUri, xmlStr, file, null, encoding);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML source
     * and target output.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlSource The XML source.
     * @param params The parameters for the XSL transformation.
     * @param output The output target.
     * @throws java.io.IOException in case of failure
     */
    @objid ("17a5124c-ff9b-44ee-a500-208630fae7cf")
    public static void transform(String xslUri, Source xmlSource, Properties params, Writer output) throws IOException {
        transform(xslUri, xmlSource, output, params, null);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML source
     * and target output.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlSource The XML source.
     * @param output The output target.
     * @throws java.io.IOException in case of failure
     */
    @objid ("6f20e3f5-c902-4670-a81e-af86ca94fbf8")
    public static void transform(String xslUri, Source xmlSource, Writer output) throws IOException {
        transform(xslUri, xmlSource, output, null, null);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string
     * and target output.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param params The parameters for the XSL transformation.
     * @param output The output target.
     * @throws java.io.IOException in case of failure
     */
    @objid ("78007759-38f3-4263-b515-4de2a9f5a4d0")
    public static void transform(String xslUri, String xmlStr, Properties params, Writer output) throws IOException {
        transform(xslUri, xmlStr, output, params, null);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string
     * and target output.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param output The output target.
     * @throws java.io.IOException in case of failure
     */
    @objid ("72fd5b8e-9025-4289-8a49-869466fc76ea")
    public static void transform(String xslUri, String xmlStr, Writer output) throws IOException {
        transform(xslUri, xmlStr, output, null, null);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string
     * and target output file.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param params The parameters for the XSL transformation.
     * @param file The output target.
     * @throws java.io.IOException in case of failure
     */
    @objid ("06b3576b-a876-4ea0-9572-8beaf36ca974")
    public static void transform(String xslUri, String xmlStr, Properties params, File file) throws IOException {
        transform(xslUri, xmlStr, file, params, null);
    }

    /**
     * Executes the XSL transformation given the XSL stylesheet URI, XML string
     * and target output file.
     * @param xslUri The XSL stylesheet URI.
     * @param xmlStr The XML string.
     * @param file The output target.
     * @throws java.io.IOException in case of failure
     */
    @objid ("948619cd-17fe-4997-ba03-e91e5d8a528b")
    public static void transform(String xslUri, String xmlStr, File file) throws IOException {
        transform(xslUri, xmlStr, file, null, null);
    }

    /**
     * Returns the XSL input stream given the XSL stylesheet URI.
     * @param xslUri The XSL stylesheet URI.
     */
    @objid ("e880f98e-7b42-479e-8450-56b322c21638")
    private static InputStream getXslInputStream(String xslUri) {
        try {
            return new FileInputStream(xslUri);
        } catch (IOException e) {
            return XSLTProcessor.class.getClassLoader()
                    .getResourceAsStream(xslUri);
        }
    }


static {
        String cacheXSLProperty = HtmlTextPlugin.I18N.getString(
                "cacheXSL"); //$NON-NLS-1$
        if (cacheXSLProperty != null && !cacheXSLProperty.startsWith("[")) { //$NON-NLS-1$
            cacheXSL = Boolean.getBoolean(cacheXSLProperty);
        } else {
            cacheXSL = true;
        }
        if (cacheXSL) {
            transformerCache = new HashMap<>();
        }

        // Increase the entity expansion line limit to handle a larger number of
        // XML entities.
        System.setProperty("entityExpansionLimit", "1000000"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    /**
     * A cached XSL Transformer object.
     */
    @objid ("cbdf549a-dffd-47df-b937-cfa331f88d28")
    private static class CachedTransformer {
        @objid ("3e0c9982-b32b-408a-99f3-f150830cc9ba")
        private Transformer transformer;

        @objid ("24955540-cc69-41d7-a1d9-68343a049aa8")
        private Properties params;

        @objid ("947a15e5-2de5-4f45-81c1-26610fe60ed9")
        public CachedTransformer(Transformer transformer, Properties params) {
            this.transformer = transformer;
            setParams(params);
        }

        @objid ("0ba0cff2-cf74-4d21-a936-4b9c752f34d6")
        public Transformer getTransformer() {
            return this.transformer;
        }

        @objid ("2171562e-c28d-400f-a6c2-8a0fe17279bc")
        public void setParams(Properties params) {
            if (params != this.params) {
                this.transformer.clearParameters();
                for (Iterator<Object> i = params.keySet().iterator(); i.hasNext();) {
                    String paramName = (String) i.next();
                    String paramValue = params.getProperty(paramName);
                    this.transformer.setParameter(paramName, paramValue);
                }
                this.params = params;           
            }
        }

    }

}
