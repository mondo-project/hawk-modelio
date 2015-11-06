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
                                    

package org.modelio.app.project.core.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.mdl;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.project.core.plugin.AppProjectCore;

/**
 * Modelio command line arguments.
 * 
 * 
 * 
 * 
 * @author phv
 */
@objid ("acd57237-7ee2-4420-a2b2-ac6a3a3f09c7")
public class CommandLineData {
    @objid ("16213bdb-f898-4ea6-a256-3cf9e4fad233")
    private static final String TEMPLATE_OPTION = "-template";

    @objid ("2faf65a2-5e9b-4b82-9e68-c5b37661031b")
    private static final String CREATE_OPTION = "-create";

    @objid ("b5265f8e-f29e-4c19-8bd6-1aff53ff405a")
    private static final String WORKSPACE_OPTION = "-workspace";

    @objid ("40df4870-3ad5-453c-9139-26461d6f1554")
    private static final String BATCH_OPTION = "-batch";

    @objid ("d1261b0b-ce83-490a-8ced-8d345d34ee53")
    private static final String PROJECT_OPTION = "-project";

    @objid ("7fa703e5-76d2-4ba1-aade-8442981ed327")
    private static final String MDEBUG_OPTION = "-mdebug";

    @objid ("140f173c-e34c-4a00-88e6-558df7f52b04")
    private boolean emptyBatch = true;

    @objid ("58514b1f-a34a-4b60-8d66-133e0260509a")
    private boolean createProject = false;

    @mdl.prop
    @objid ("4c6c622f-97ad-4012-b56a-b9c31a9cd6e4")
    private String workspace = null;

    @mdl.propgetter
    public String getWorkspace() {
        // Automatically generated method. Please do not modify this code.
        return this.workspace;
    }

    @mdl.prop
    @objid ("052ba9e3-ac78-4ffc-9e9d-57f28c31ba01")
    private String projectName = null;

    @mdl.propgetter
    public String getProjectName() {
        // Automatically generated method. Please do not modify this code.
        return this.projectName;
    }

    @mdl.prop
    @objid ("bcf181a3-293e-4a63-9207-f2a3c7f49d95")
    private String script = null;

    @mdl.propgetter
    public String getScript() {
        // Automatically generated method. Please do not modify this code.
        return this.script;
    }

    @mdl.prop
    @objid ("358dac90-7485-452a-a734-daef72638ebe")
    private String template = null;

    @mdl.propgetter
    public String getTemplate() {
        // Automatically generated method. Please do not modify this code.
        return this.template;
    }

    @mdl.prop
    @objid ("92c45bac-a113-4a57-8392-439e30343384")
    private boolean debug;

    @mdl.propgetter
    public boolean isDebug() {
        // Automatically generated method. Please do not modify this code.
        return this.debug;
    }

    /**
     * Batch parameter.
     */
    @objid ("a1e3682c-3ef5-4126-a182-196c1071240b")
    private static final String PARAM_OPTION = "-param";

    @objid ("f56f34a4-f175-42c4-8ae4-4d83fd356ca8")
    private final Map<String, String> batchParams = new HashMap<>();

    /**
     * C'tor
     * @param args the command line arguments
     * @throws java.lang.IllegalArgumentException if the command line is invalid
     */
    @objid ("df8b6edb-1fab-478f-a8ce-81850b7732e6")
    public CommandLineData(String[] args) throws IllegalArgumentException {
        parse(args);
    }

    /**
     * @return <i>true</i> if batch mode but there is nothing to do.
     */
    @objid ("8943438c-bdd0-44ff-baa8-10ad28be47b1")
    public boolean isEmpty() {
        return this.emptyBatch;
    }

    @objid ("53df1e39-e8ab-4903-9cbe-97e11dfa8b7b")
    @Override
    public String toString() {
        return "project=" + this.projectName + " create=" + this.createProject + " workspace=" + this.workspace;
    }

    /**
     * @return <i>true</i> if the project is to be created else <i>false</i>.
     */
    @objid ("0b10bee0-f483-48fa-a1f7-0cb8843600fc")
    public boolean isCreate() {
        return this.createProject;
    }

    @objid ("118f3313-13e8-45d7-bae3-e63c3e13e4f8")
    private void parse(String[] args) {
        // Parse command line arguments for a batch run
        int i = 0;
        
        while (i < args.length) {
            final String argi = args[i];
            if (argi.equals(PROJECT_OPTION)) {
                checkSupArg(args, i, 1);
                this.projectName = args[i + 1];
                this.emptyBatch = false;
                i += 2;
            } else if (argi.equals(BATCH_OPTION)) {
                checkSupArg(args, i, 1);
                this.script = args[i + 1];
                this.emptyBatch = false;
                i += 2;
            } else if (argi.equals(WORKSPACE_OPTION)) {
                checkSupArg(args, i, 1);
                this.workspace = args[i + 1];
                this.emptyBatch = false;
                i += 2;
            } else if (argi.equals(CREATE_OPTION)) {
                checkSupArg(args, i, 1);
                this.createProject = true;
                this.projectName = args[i + 1];
                this.emptyBatch = false;
                i += 2;
            } else if (argi.equals(TEMPLATE_OPTION)) {
                checkSupArg(args, i, 1);
                this.template = args[i + 1];
                this.emptyBatch = false;
                i += 2;
            } else if (argi.equals(MDEBUG_OPTION)) {
                this.debug = true;
                i += 1; 
            } else if (argi.equals(PARAM_OPTION)) {
                checkSupArg(args, i, 2);
                this.batchParams.put(args[i + 1], args[i + 2]);
                i += 3; 
            } else {
                i++;
            }
        }
    }

    /**
     * @return <i>true</i> if batch mode else <i>false</i>.
     */
    @objid ("fa50daad-f574-410c-9615-3e3588c44e20")
    public boolean isBatch() {
        return this.projectName != null && this.script != null;
    }

    /**
     * Check the command line arguments contains <i>nbRequired</i> more arguments to match an argument parameter.
     * @param args the command line arguments
     * @param index index of the parameterized argument
     * @param nbRequired count of required more arguments for the argument <i>args[index]</i>
     */
    @objid ("927f5681-348b-4c67-b3a7-274839b8a2fd")
    private void checkSupArg(String[] args, int index, int nbRequired) {
        if (args.length < index + nbRequired) {
            String msg = AppProjectCore.I18N.getMessage("CommandlineData.ArgNeedParameter", 
                    Arrays.toString(args),
                    args[index],
                    nbRequired);
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Get the batch parameters.
     * @return batch parameters.
     */
    @objid ("4fdfb664-311b-4089-88bb-b90063c00be0")
    public Map<String, String> getBatchParameters() {
        return this.batchParams;
    }

}
