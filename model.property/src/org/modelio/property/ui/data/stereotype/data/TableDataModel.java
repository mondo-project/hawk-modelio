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
                                    

package org.modelio.property.ui.data.stereotype.data;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.modelio.property.ui.data.stereotype.model.IPropertyModel2;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.impl.CoreSession;

@objid ("3b497c29-6136-40b2-b65c-179e934c35ef")
public class TableDataModel {
    @objid ("1503064a-304a-4ceb-a8dc-0ef1558b7d61")
    protected final List<Integer> columns = new ArrayList<>();

    @objid ("9f3127f1-99e6-4c3d-bd65-48885130f099")
    protected List<Integer> rows = new ArrayList<>();

    @objid ("49db3b9e-8370-4879-82ce-d209796edf00")
    protected final BodyDataProvider bodyDataProvider;

    @objid ("88d1f6cb-1e5f-4acf-ba8a-f47995841f75")
    protected final RowHeaderDataProvider rowHeaderDataProvider;

    @objid ("88f195c8-5ebe-46ac-bdb0-fd61ea1bd31f")
    protected final ColumnHeaderDataProvider columnHeaderDataProvider;

    @objid ("e7866c27-775d-417e-b91b-11cdd9cdedff")
    protected final IPropertyModel2 propertyModel;

    /**
     * It returns the Objects directly hold by the propertyModel.
     * @return
     */
    @objid ("8cf9d60e-fc5b-4cf9-94ff-744edbee33ae")
    public List<Integer> getRows() {
        return this.rows;
    }

    /**
     * It returns the Objects directly hold by the propertyModel.
     * @return
     */
    @objid ("4d0be201-cf05-4bcb-817e-9890aea87f59")
    public Iterable<Integer> getColumns() {
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < this.propertyModel.getColumnNumber() ; i++) {
            ret.add(i);
        }
        return ret;
    }

    @objid ("a80c5251-ff3c-400f-900c-99788b0f175e")
    public TableDataModel(IPropertyModel2 propertyModel) {
        this.propertyModel = propertyModel;
        
        this.bodyDataProvider = new BodyDataProvider(this);
        this.columnHeaderDataProvider = new ColumnHeaderDataProvider(this);
        this.rowHeaderDataProvider = new RowHeaderDataProvider(this);
        
        // set data
        rebuildData();
    }

    @objid ("0ec77746-5868-4a83-84d7-6092341f4f30")
    public IPropertyModel2 getPropertyModel() {
        return this.propertyModel;
    }

    /**
     * @return the table main data
     */
    @objid ("510406e1-10bc-4cf3-846c-720975eadc14")
    public IDataProvider getBodyDataProvider() {
        return this.bodyDataProvider;
    }

    /**
     * @return the table row headers data
     */
    @objid ("7fa4c12f-606b-4798-8893-63d55eee9b45")
    public IDataProvider getRowHeaderDataProvider() {
        return this.rowHeaderDataProvider;
    }

    /**
     * @return the table column headers data
     */
    @objid ("b5877575-213a-4185-a518-b561fbc7a30a")
    public IDataProvider getColumnHeaderDataProvider() {
        return this.columnHeaderDataProvider;
    }

    @objid ("d031941f-d64c-4a70-bdcb-761e4f46c00a")
    private void rebuildData() {
        // Rows
        this.rows.clear();
        for (int i = 0; i < this.propertyModel.getRowsNumber() ; i++) {
            this.rows.add(i);
        }
        
        // Columns
        this.columns.clear();
        for (final Integer elt : this.getColumns()) {
            this.columns.add(elt);
        }
    }

    @objid ("3c486977-e80a-4ede-8a8f-0a0cd9c76fda")
    public Integer getObjectAtRow(int rowIndex) {
        return this.rows.get(rowIndex);
    }

    @objid ("e23ad05e-b2ad-40e7-b421-88cac119adee")
    public Integer getObjectAtCol(int colIndex) {
        return this.columns.get(colIndex);
    }

    @objid ("58b28e31-72d4-41f4-b0fd-5b4c9ffe6b8d")
    public Integer getRowIndex(Object o) {
        return this.rows.indexOf(o);
    }

    @objid ("2e7d5ef8-91b2-4da4-8368-7c49234a40af")
    public int getColumnIndex(Object o) {
        return this.columns.indexOf(o);
    }

    @objid ("cb0c3300-867f-41a1-bd57-5c0b45a7aa37")
    public Object getValueAt(int row, int col) {
        return this.propertyModel.getValueAt(getObjectAtRow(row), getObjectAtCol(col));
    }

    @objid ("8906aa68-9255-4bd9-896f-43384029e170")
    public void setValueAt(int row, int col, Object val) {
        this.propertyModel.setValueAt(getObjectAtRow(row), getObjectAtCol(col), val);
    }

    @objid ("6973ec5c-afbc-435a-b0e8-c09615608d89")
    private static class BodyDataProvider implements IDataProvider {
        @objid ("e100adb0-ad47-49bf-aa0a-f8abe7b04139")
        private final TableDataModel base;

        @objid ("82958108-ed13-4984-8230-c5194c2b92b0")
        public BodyDataProvider(TableDataModel base) {
            this.base = base;
        }

        @objid ("c75423fc-02f2-405d-9338-ecdb88eb0fb5")
        @Override
        public int getColumnCount() {
            return this.base.columns.size();
        }

        @objid ("3765d25f-9516-474e-8345-5f7ab2c9cd5d")
        @Override
        public Object getDataValue(int col, int row) {
            //final Object elt = this.base.sortedRows.get(row);
            return this.base.getValueAt(1 + row, col);
        }

        @objid ("f46c9a71-ba56-439a-9656-7f7e9e361626")
        @Override
        public int getRowCount() {
            return this.base.rows.size() - 1;
        }

        @objid ("ec3e020e-9f01-480a-b25d-a4fd8ca2c859")
        @Override
        public void setDataValue(int col, int row, Object value) {
            //final Object elt = this.base.sortedRows.get(row);
            
            final CoreSession session = CoreSession.getSession(this.base.propertyModel.getEditedElement());
            try (ITransaction t = session.getTransactionSupport().createTransaction("");) {
                // code
                this.base.setValueAt(1 + row, col, value);
            
                // Commit transaction
                t.commit();
                return;
            }
        }

    }

    /**
     * @author phv
     */
    @objid ("a050f14b-7928-4acf-8e75-e05f12bb8730")
    private static class ColumnHeaderDataProvider implements IDataProvider {
        @objid ("20a3e08f-dc3f-4ccb-b062-43f0c9dcc7b7")
        private final TableDataModel base;

        @objid ("364d0ec7-ef49-4e04-a9e9-dc311b2ac369")
        public ColumnHeaderDataProvider(TableDataModel base) {
            this.base = base;
        }

        @objid ("60238aa1-40cc-4301-ad11-b541b0660cd7")
        @Override
        public int getColumnCount() {
            return this.base.getBodyDataProvider().getColumnCount();
        }

        @objid ("0d55e9a6-5f32-412b-920e-866dec6008e1")
        @Override
        public Object getDataValue(int col, int row) {
            return this.base.getObjectAtCol(col);
        }

        @objid ("90f7f192-dc14-45b0-9cb4-b52d1daa3c2a")
        @Override
        public int getRowCount() {
            return 1;
        }

        @objid ("81ad4559-35c9-48af-8ab0-2615bbc98bed")
        @Override
        public void setDataValue(int col, int row, Object value) {
            throw new UnsupportedOperationException();
        }

    }

    @objid ("4b59eca4-fea1-4202-84d5-dd86fb5277f1")
    private static class RowHeaderDataProvider implements IRowDataProvider<Object> {
        @objid ("2e85615e-697c-4906-a6b3-2fccbe26be93")
        private final TableDataModel base;

        @objid ("ea656d3e-abdd-4780-a9ab-ecb7e7eff66a")
        public RowHeaderDataProvider(TableDataModel base) {
            this.base = base;
        }

        @objid ("5ebac1bc-b539-48fa-8166-0668725360c7")
        @Override
        public int getColumnCount() {
            return 1;
        }

        @objid ("3582429a-17c8-49f1-bbad-2781289d5019")
        @Override
        public Object getDataValue(int col, int row) {
            return this.base.getObjectAtRow(1 + row);
        }

        @objid ("e6f80307-d43d-4069-b9c4-b1c524a81434")
        @Override
        public int getRowCount() {
            return this.base.getBodyDataProvider().getRowCount();
        }

        @objid ("edd775bf-693d-47f1-95b4-9788ac6d25af")
        @Override
        public void setDataValue(int arg0, int arg1, Object arg2) {
            throw new UnsupportedOperationException();
        }

        @objid ("4278f448-6118-4663-b1c7-a742d6333be1")
        @Override
        public Object getRowObject(int rowIndex) {
            return this.base.getObjectAtRow(rowIndex);
        }

        @objid ("74b83b03-dd16-4ba0-9a67-9a893a6ad195")
        @Override
        public int indexOfRowObject(Object rowObject) {
            return this.base.getRowIndex(rowObject);
        }

    }

}
