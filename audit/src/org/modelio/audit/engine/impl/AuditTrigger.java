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
                                    

package org.modelio.audit.engine.impl;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("b16595a2-f678-4826-ad25-b24f67b06292")
public final class AuditTrigger {
    @objid ("aefdcfab-7a65-4544-af6a-a7611c122622")
    public static final int CREATE = 1;

    @objid ("676706a0-833d-466e-bf4b-5d3590737fa5")
    public static final int UPDATE = 2;

    @objid ("1d26ab4b-d201-467f-90ab-c3daab4d6a41")
    public static final int DELETE = 4;

    @objid ("79f7ebe8-6796-48e1-b399-010048f630ff")
    public static final int MOVE = 8;

    @objid ("901af8bb-8500-47e6-a3ae-9b268bfeb5de")
    public static final int ALL = CREATE | UPDATE | DELETE | MOVE;

    @objid ("e1703f89-6715-4571-97ed-20b44e99d91b")
    public static boolean isCreate(int value) {
        return (value & CREATE) == CREATE;
    }

    @objid ("f7d42ab5-ebb1-4316-b9d4-a879f9ea9661")
    public static boolean isUpdate(int value) {
        return (value & UPDATE) == UPDATE;
    }

    @objid ("bee4522a-b293-4010-9b09-885255294679")
    public static boolean isMove(int value) {
        return (value & MOVE) == MOVE;
    }

    @objid ("9be7f225-1bb5-4729-bd07-ae4a255a6efe")
    public static boolean isDelete(final int value) {
        return (value & DELETE) == DELETE;
    }

}
