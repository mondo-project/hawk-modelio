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
                                    

package org.modelio.audit.view.providers;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.view.providers.byelement.ByElementContentProvider;
import org.modelio.audit.view.providers.byelement.NumberedElementLabelProvier;
import org.modelio.audit.view.providers.byrules.ByRuleContentProvider;
import org.modelio.audit.view.providers.byrules.NumberedRuleLabelProvider;
import org.modelio.audit.view.providers.bytype.ByTypeContentProvider;
import org.modelio.audit.view.providers.bytype.NumberedTypeLabelProvier;
import org.modelio.audit.view.providers.commons.DescriptionLabelProvider;
import org.modelio.audit.view.providers.commons.ElementLabelProvider;
import org.modelio.audit.view.providers.commons.RuleLabelProvider;
import org.modelio.audit.view.providers.commons.TimeLabelProvider;
import org.modelio.audit.view.providers.commons.TypeLabelProvier;
import org.modelio.audit.view.providers.flat.FlatContentProvider;

@objid ("6e3e02dc-806a-4783-967f-3c60ac4b9a8d")
public class AuditProviderFactory {
    @objid ("38dca49b-6e3b-4384-ad0d-7a86e2730743")
    private AuditViewMode viewMode;

    @objid ("2ea55bc2-a306-49e3-bf2a-1d570fd662dd")
    private Map<AuditViewMode, ViewModeImpl> modeMap;

    @objid ("485a6140-5f6b-412c-94b4-6b0b541ae580")
    public AuditProviderFactory(String jobId) {
        this.viewMode = AuditViewMode.BYTYPE;
        
        this.modeMap = new HashMap<>();
        
        ViewModeImpl flatImpl = new ViewModeImpl();
        flatImpl.defaultColumnSize = new int[] { 80, 20, 50, 60, 400 };
        flatImpl.columnName = new String[] { Audit.I18N.getString("AuditView.TableTitle.Time"),Audit.I18N.getString("AuditView.TableTitle.Type"), Audit.I18N.getString("AuditView.TableTitle.Rule"),Audit.I18N.getString("AuditView.TableTitle.Element"), Audit.I18N.getString("AuditView.TableTitle.Description") };
        flatImpl.contentProvider = new FlatContentProvider(jobId);
        flatImpl.labelsProviders = new CellLabelProvider[] { new TimeLabelProvider(), new TypeLabelProvier(),new RuleLabelProvider(), new ElementLabelProvider(), new DescriptionLabelProvider() };
        this.modeMap.put(AuditViewMode.FLAT, flatImpl);
        
        ViewModeImpl bytypeImpl = new ViewModeImpl();
        bytypeImpl.defaultColumnSize = new int[] { 145, 60, 400 };
        bytypeImpl.columnName =  new String[] { Audit.I18N.getString("AuditView.TableTitle.FullType"),Audit.I18N.getString("AuditView.TableTitle.Rule"), Audit.I18N.getString("AuditView.TableTitle.Description") };
        bytypeImpl.contentProvider = new ByTypeContentProvider(jobId);
        bytypeImpl.labelsProviders = new CellLabelProvider[] { new NumberedTypeLabelProvier(), new RuleLabelProvider(),new DescriptionLabelProvider() };
        this.modeMap.put(AuditViewMode.BYTYPE, bytypeImpl);
        
        ViewModeImpl byelementImpl = new ViewModeImpl();
        byelementImpl.defaultColumnSize =  new int[] { 120, 20, 60, 400 };
        byelementImpl.columnName = new String[] { Audit.I18N.getString("AuditView.TableTitle.Element"),Audit.I18N.getString("AuditView.TableTitle.Type"), Audit.I18N.getString("AuditView.TableTitle.Rule"), Audit.I18N.getString("AuditView.TableTitle.Description") };
        byelementImpl.contentProvider = new ByElementContentProvider(jobId);
        byelementImpl.labelsProviders = new CellLabelProvider[] { new NumberedElementLabelProvier(), new TypeLabelProvier(), new RuleLabelProvider(), new DescriptionLabelProvider() };
        this.modeMap.put(AuditViewMode.BYELEMENT, byelementImpl);
        
        ViewModeImpl byrule = new ViewModeImpl();
        byrule.defaultColumnSize =  new int[] { 120, 60, 400 };
        byrule.columnName =   new String[] { Audit.I18N.getString("AuditView.TableTitle.Rule"),Audit.I18N.getString("AuditView.TableTitle.Element"),Audit.I18N.getString("AuditView.TableTitle.Description") };
        byrule.contentProvider = new ByRuleContentProvider(jobId);
        byrule.labelsProviders = new CellLabelProvider[] { new NumberedRuleLabelProvider(), new ElementLabelProvider(), new DescriptionLabelProvider() };
        this.modeMap.put(AuditViewMode.BYRULE, byrule);
    }

    @objid ("f280c61d-3fdf-48fa-904b-a5dbc69f2b3b")
    public void setViewMode(AuditViewMode mode) {
        this.viewMode = mode;
    }

    @objid ("dc8d03d2-21f5-4c4c-a595-846ace08e5e4")
    public CellLabelProvider getLabelProvider(int column) {
        return this.modeMap.get(this.viewMode).labelsProviders[column];
    }

    @objid ("6d230ba6-dfda-4bd1-990b-1f800d3af9ce")
    public IContentProvider getContentProvider() {
        return this.modeMap.get(this.viewMode).contentProvider;
    }

    @objid ("2933f859-f0b1-484e-858d-2fd258301253")
    public int getDefaultColumnSize(int column) {
        return this.modeMap.get(this.viewMode).defaultColumnSize[column];
    }

    @objid ("12e35b39-7d95-43d4-ac7d-49eafe3f1110")
    public String getColumnName(int column) {
        return this.modeMap.get(this.viewMode).columnName[column];
    }

    @objid ("88fcbb14-e389-4f6e-a082-278115a0f6bd")
    public int getColumns() {
        return this.modeMap.get(this.viewMode).labelsProviders.length;
    }

    @objid ("f639a43f-1e09-47c2-a0d2-eb83b7be9f99")
    public AuditViewMode getViewMode() {
        return this.viewMode;
    }

    @objid ("4267c2ad-6430-4954-8781-3e33e317850d")
    private class ViewModeImpl {
        @objid ("524d76d3-ff70-47a6-92e1-dd9426f76ad3")
        public int[] defaultColumnSize;

        @objid ("c2e65275-114c-4361-9e7d-1028b888f49a")
        public String[] columnName;

        @objid ("4474597f-f202-4208-aa47-09575d4b7eab")
        public IContentProvider contentProvider;

        @objid ("28fe365a-67be-45e7-8e53-b8ecd008f7de")
        public CellLabelProvider[] labelsProviders;

    }

    @objid ("61a16928-906a-4cf2-b5a3-c3795689d9c9")
    public enum AuditViewMode {
        FLAT,
        BYTYPE,
        BYELEMENT,
        BYRULE;
    }

}
