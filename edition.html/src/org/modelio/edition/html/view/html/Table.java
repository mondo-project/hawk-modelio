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
package org.modelio.edition.html.view.html;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Models a simplified HTML &lt;table&gt; tag.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("5d9be244-b3ee-4e66-928c-152c5e48a072")
public class Table {
    /**
     * A table with no headers.
     */
    @objid ("848d6a4f-eb75-4faa-874b-93a1f09cafc9")
    public static final int TABLE_HEADERS_NONE = 0;

    /**
     * A table with column headers.
     */
    @objid ("424a66cc-4810-46fe-9abf-29e809053b1f")
    public static final int TABLE_HEADERS_COLS = 1;

    /**
     * A table with row headers.
     */
    @objid ("01c8c26a-5949-4c62-a33b-435bb2841e65")
    public static final int TABLE_HEADERS_ROWS = 2;

    /**
     * A table with both column and row headers.
     */
    @objid ("aa2f506a-990b-446e-bc59-3b6268c7cfdb")
    public static final int TABLE_HEADERS_BOTH = 3;

// The number of rows.
    @objid ("eb9a1172-54c8-4780-ad21-72f6f207baa6")
    private int rows = 2;

// The number of columns.
    @objid ("dd8463fe-08f1-472b-a135-88c17b65eafa")
    private int cols = 2;

// The table width.
    @objid ("bd660d19-8d84-4ae9-9c09-8275e566d9a4")
    private String width = "85%"; // $NON-NLS-1$

// The type of tableheaders for this table.
    @objid ("6151cdec-952c-43d5-84fd-dd602f92f571")
    private int tableHeaders = 0;

// The table summary.
    @objid ("d37cf255-1d4b-40c7-a01a-e3d4f551c89e")
    private String summary;

// The table caption.
    @objid ("971324a4-4947-4a0f-96e1-2d04316948fb")
    private String caption;

    /**
     * Creates a new instance.
     */
    @objid ("65f7af00-f251-4974-9684-bf239f506712")
    public Table() {
    }

    /**
     * Gets the number of rows in the table.
     * @return the number of rows
     */
    @objid ("f189baa0-54f8-4cd3-bb52-ba9b32a8491d")
    public int getRows() {
        return this.rows;
    }

    /**
     * Sets the number of rows in the table.
     * @param rows the number of rows
     */
    @objid ("74bdb03f-ccc9-43c3-9b69-b7579787edd1")
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Gets the number of columns in the table.
     * @return the number of columns
     */
    @objid ("d713d969-7453-414b-beb9-30e9ea5bdc48")
    public int getColumns() {
        return this.cols;
    }

    /**
     * Sets the number of columns in the table.
     * @param cols the number of columns
     */
    @objid ("6dae05fd-8839-46fc-889d-9e2eb108b85e")
    public void setColumns(int cols) {
        this.cols = cols;
    }

    /**
     * Gets the table width.
     * @return the width of the table
     */
    @objid ("8da8c46b-584a-49b3-8c8e-71d72f563246")
    public String getWidth() {
        return this.width;
    }

    /**
     * Sets the table width.
     * @param width the width of the table
     */
    @objid ("10eb0208-d663-4ff7-9b38-e7da70233e6a")
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Gets the table caption.
     * @return the table caption
     */
    @objid ("29c98352-bc0d-49b8-a19a-43075ec35697")
    public String getCaption() {
        return this.caption;
    }

    /**
     * Sets the table caption.
     * @param caption the table caption
     */
    @objid ("28404ec4-e96e-47c5-b97e-7885328647bf")
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Gets the table summary.
     * @return the table summary
     */
    @objid ("788d8ab6-18d1-4ce9-bef1-eb18d8458b56")
    public String getSummary() {
        return this.summary;
    }

    /**
     * Sets the table summary.
     * @param summary the table summary
     */
    @objid ("6664d2ab-c1f0-458f-9085-0903b4bea313")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets the table headers style.
     * @return the integer with value representing
     * <code>TABLE_HEADERS_NONE</code>,
     * <code>TABLE_HEADERS_COLS</code>,
     * <code>TABLE_HEADERS_ROWS</code> or
     * <code>TABLE_HEADERS_BOTH</code>
     */
    @objid ("b8d74bdc-1658-424c-8a02-45d1bea903f1")
    public int getTableHeaders() {
        return this.tableHeaders;
    }

    /**
     * Sets the table headers style.
     * @param tableHeaders an integer with value representing
     * <code>TABLE_HEADERS_NONE</code>,
     * <code>TABLE_HEADERS_COLS</code>,
     * <code>TABLE_HEADERS_ROWS</code> or
     * <code>TABLE_HEADERS_BOTH</code>
     */
    @objid ("bbdc455c-9bd0-4e68-bc27-f48b625caf98")
    public void setTableHeaders(int tableHeaders) {
        this.tableHeaders = tableHeaders;
    }

}
