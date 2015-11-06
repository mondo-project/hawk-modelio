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
                                    

package org.modelio.mda.infra.service.dynamic;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.mdl;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.modelio.api.diagram.IDiagramCustomizer;
import org.modelio.api.diagram.IDiagramService;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.IModule;
import org.modelio.mda.infra.plugin.MdaInfra;

@objid ("5ecd3ae0-1ea5-4428-abe4-560039814380")
public class GenericDiagramCustomizer implements IDiagramCustomizer {
    @mdl.prop
    @objid ("b708f5aa-667b-41c7-b1c2-553b06778468")
    private boolean keepPalette = true;

    @mdl.propgetter
    public boolean isKeepPalette() {
        // Automatically generated method. Please do not modify this code.
        return this.keepPalette;
    }

    @mdl.propsetter
    public void setKeepPalette(boolean value) {
        // Automatically generated method. Please do not modify this code.
        this.keepPalette = value;
    }

    @objid ("3fdcc952-f5bf-492f-9561-2f032f0e6e83")
    private List<PaletteCommand> commands = new ArrayList<>();

    @objid ("83bd7af3-1fef-4e7d-907a-307965b68e05")
    private IModule module;

    @objid ("86a3f30b-8646-4a6b-a9dd-0b07ec9beae0")
    public GenericDiagramCustomizer(final IModule module) {
        this.module = module;
    }

    @objid ("cf7d99ae-eb93-4764-b091-2db98a68e545")
    @Override
    public boolean keepBasePalette() {
        return this.keepPalette;
    }

    /**
     * Called by the diagram editor when opening.
     */
    @objid ("d956621f-1b35-4499-a07f-381289d7dd74")
    @Override
    public void fillPalette(final PaletteRoot paletteRoot) {
        IDiagramService toolRegistry = Modelio.getInstance().getDiagramService();
        
        if (!this.keepPalette) {
            final PaletteDrawer commonGroup = new PaletteDrawer("Default", null);
            commonGroup.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
            commonGroup.add(new SelectionToolEntry());
            commonGroup.add(new MarqueeToolEntry());
            paletteRoot.add(commonGroup);
        }
        
        Map<String, PaletteDrawer> paletteMap = new HashMap<>();
        
        for (PaletteCommand command : this.commands) {
            PaletteDrawer palette = paletteMap.get(internationalize(command.group, this.module));
            if (palette == null) {
                palette = new PaletteDrawer(command.group, null);
                palette.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
                paletteRoot.add(palette);
                paletteMap.put(internationalize(command.group, this.module), palette);
            }
            palette.add(toolRegistry.getRegisteredTool(command.commandId));
        }
    }

    @objid ("5f1981f0-9487-4669-b21e-c7554a28999b")
    private String internationalize(final String value, final IModule module) {
        try {
            if (value == null)
                return "";
        
            if (value.startsWith("%")) {
                return getManifestBundle(module).getString(value.substring(1));
            }
        } catch (MissingResourceException e) {
            MdaInfra.LOG.warning("[" + module.getName() + "] Missing Resource :" + value);
        }
        return value;
    }

    @objid ("e4788aab-227a-4e00-8d05-c5dc7f9230fd")
    private ResourceBundle getManifestBundle(final IModule module) throws MissingResourceException {
        final Path manifestDir = module.getConfiguration().getModuleResourcesPath();
        try (final URLClassLoader cl = new URLClassLoader(new URL[] { manifestDir.toUri().toURL() })) {
            // Create a class loader initialized with the 'manifest' directory in module resource,
            // then give it to ResourceBundle.getBundle(...)
            return ResourceBundle.getBundle("module", Locale.getDefault(), cl);
        } catch (IOException e) {
            throw new MissingResourceException(e.getLocalizedMessage(), "module", "");
        } catch (MissingResourceException e) {
            MissingResourceException e2 = new MissingResourceException(e.getLocalizedMessage() + " in '" + manifestDir + "'",
                    e.getClassName(), e.getKey());
            e2.initCause(e);
            throw e2;
        }
    }

    @objid ("8e8f9eef-15d3-4865-bdca-1dcf26a10ec9")
    public void registerPaletteCommand(PaletteCommand cmd) {
        this.commands.add(cmd);
    }

    @objid ("5107edc6-f090-4fe5-95c4-bdca85aa0202")
    public static class PaletteCommand {
        @objid ("5fee88a5-1727-4c29-97c1-5f320da4159d")
        public String group;

        @objid ("211fccda-3d34-424e-b45c-9168448c769b")
        public String commandId;

        @objid ("a64b1df4-24c1-4076-a425-22d9ed2ae933")
        public PaletteCommand(String commandId, String group) {
            this.commandId = commandId;
            this.group = group;
        }

    }

}
