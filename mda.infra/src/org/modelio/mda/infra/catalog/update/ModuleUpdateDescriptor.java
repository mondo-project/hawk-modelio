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
                                    

package org.modelio.mda.infra.catalog.update;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Data model class for a module in the update dialog.
 */
@objid ("ec5b3ecc-6f4b-4f02-9250-bc751faf0529")
public class ModuleUpdateDescriptor {
    @objid ("c692fb34-3ce8-4625-827f-841c90ce1093")
    private boolean toUpdate;

    @objid ("8c1d113d-0ba9-4e47-bfbc-95d35b2b2fef")
    private String name;

    @objid ("24909880-f9fe-4be9-bc66-2a81667fa187")
    private String currentVersion;

    @objid ("cb61c8fe-5d36-4d30-b038-7394ff1e3078")
    private String newVersion;

    @objid ("6b22e3ee-03b9-416f-b4b2-cda3aced5016")
    private String link;

    @objid ("83d55a85-612d-4c87-8cd8-2b9176b8e54e")
    private String downloadLink;

    /**
     * Constructor initializing all fields.
     * @param name the module name.
     * @param currentVersion the current version of the module.
     * @param newVersion the new version of the module.
     * @param link the download page for this module.
     * @param downloadLink the link to download this module.
     */
    @objid ("bc206c0a-b5fa-4647-868f-f97694094398")
    public ModuleUpdateDescriptor(String name, String currentVersion, String newVersion, String link, String downloadLink) {
        super();
        this.name = name;
        this.currentVersion = currentVersion;
        this.newVersion = newVersion;
        this.link = link;
        this.toUpdate = true;
        this.downloadLink = downloadLink;
    }

    /**
     * @return the name
     */
    @objid ("7963e7f0-c9f1-4515-9733-74d992126063")
    public String getName() {
        return this.name;
    }

    /**
     * @return the currentVersion
     */
    @objid ("085ff909-4421-442e-8d46-cfbdc35f9e37")
    public String getCurrentVersion() {
        return this.currentVersion;
    }

    /**
     * @return the newVersion
     */
    @objid ("6ab2e1b8-9840-4447-9f70-f468b129e298")
    public String getNewVersion() {
        return this.newVersion;
    }

    /**
     * @return the toUpdate
     */
    @objid ("01961bfa-90f3-4828-8183-ce5cbcd8462a")
    public boolean isToUpdate() {
        return this.toUpdate;
    }

    /**
     * @param toUpdate
     */
    @objid ("7b690bf7-8dbb-4730-bacb-a99f853c6974")
    public void setToUpdate(boolean toUpdate) {
        this.toUpdate = toUpdate;
    }

    /**
     * @return the link
     */
    @objid ("b7902f65-26c7-476d-819d-336b28f436f4")
    public String getLink() {
        return this.link;
    }

    /**
     * @return
     */
    @objid ("0f42091c-9025-4087-993a-a2413c8bf5e0")
    public String getDownloadLink() {
        return this.downloadLink;
    }

    /**
     * @param downloadLink
     */
    @objid ("535d7107-f56c-4be6-ad35-97df17c2ea8f")
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

}
