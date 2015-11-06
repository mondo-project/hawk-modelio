package org.modelio.model.search.engine.searchers.note;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.modeliosoft.modelio.javadesigner.annotations.mdl;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.model.search.engine.ISearchCriteria;

@objid ("97b747cf-44d7-4b4c-9cd7-6d5092134f63")
public class NoteSearchCriteria2 implements ISearchCriteria {
    @mdl.prop
    @objid ("2a2f51bd-1e5b-4455-aed7-b8a7d1809d67")
    private String expression = ".*";

    @mdl.propgetter
    public String getExpression() {
        // Automatically generated method. Please do not modify this code.
        return this.expression;
    }

    @mdl.propsetter
    public void setExpression(String value) {
        // Automatically generated method. Please do not modify this code.
        this.expression = value;
    }

    @mdl.prop
    @objid ("d9ee8995-c35b-44e1-a611-1f19963e9266")
    private boolean caseSensitive;

    @mdl.propgetter
    public boolean isCaseSensitive() {
        // Automatically generated method. Please do not modify this code.
        return this.caseSensitive;
    }

    @mdl.propsetter
    public void setCaseSensitive(boolean value) {
        // Automatically generated method. Please do not modify this code.
        this.caseSensitive = value;
    }

    @mdl.prop
    @objid ("826b0aa2-4e70-454a-be4a-aa93896086e8")
    private String noteType = ".*";

    @mdl.propgetter
    public String getNoteType() {
        // Automatically generated method. Please do not modify this code.
        return this.noteType;
    }

    @mdl.propsetter
    public void setNoteType(String value) {
        // Automatically generated method. Please do not modify this code.
        this.noteType = value;
    }

    @objid ("a21a1b01-6552-48ca-a5a6-e0aafb985bdf")
    public void reset() {
        this.expression = ".*";
        this.noteType = "description";
        this.caseSensitive = false;
    }

    @objid ("336eaedb-57a5-4f2f-91af-08d0f578bb46")
    public static boolean isValidExpression(final String expression) {
        try {
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            return true;
        } catch (final PatternSyntaxException e) {
            return false;
        }
    }

}
