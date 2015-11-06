package org.modelio.model.search.engine.searchers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.modeliosoft.modelio.javadesigner.annotations.mdl;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.model.search.engine.ISearchCriteria;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.smkernel.mapi.MClass;

@objid ("1a9d1962-724e-4515-8968-0963140b4b70")
public class ModelSearchCriteria implements ISearchCriteria {
    @mdl.prop
    @objid ("0006df38-c59e-10ab-8258-001ec947cd2a")
    private boolean includeRamc;

    @mdl.propgetter
    public boolean isIncludeRamc() {
        // Automatically generated method. Please do not modify this code.
        return this.includeRamc;
    }

    @mdl.propsetter
    public void setIncludeRamc(boolean value) {
        // Automatically generated method. Please do not modify this code.
        this.includeRamc = value;
    }

    @mdl.prop
    @objid ("135d0ab1-aeaf-4275-afdd-048d1b84408d")
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
    @objid ("00516f6c-c5a4-10ab-8258-001ec947cd2a")
    private String stereotype;

    @mdl.propgetter
    public String getStereotype() {
        // Automatically generated method. Please do not modify this code.
        return this.stereotype;
    }

    @mdl.propsetter
    public void setStereotype(String value) {
        // Automatically generated method. Please do not modify this code.
        this.stereotype = value;
    }

    @mdl.prop
    @objid ("11b84168-66b5-4215-b4d0-c8c875acc69b")
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

    @objid ("f8beebe2-8966-4d67-9cde-a5d1497533a4")
    private final List<MClass> metaclasses = new ArrayList<>();

    @mdl.prop
    @objid ("9e7bea95-3871-4427-8c1e-298a2c03990f")
    private IMObjectFilter filter;

    @mdl.propgetter
    public IMObjectFilter getFilter() {
        // Automatically generated method. Please do not modify this code.
        return this.filter;
    }

    @mdl.propsetter
    public void setFilter(IMObjectFilter value) {
        // Automatically generated method. Please do not modify this code.
        this.filter = value;
    }

    @objid ("e69b5b62-d496-493a-8597-3524dfe50050")
    public void addMetaclass(MClass metaclass) {
        if (metaclass != null) {
            this.metaclasses.add(metaclass);
        }
    }

    @objid ("460e1645-7275-4455-8bd2-83d3229b2e11")
    public void reset() {
        this.expression = ".*";
        this.metaclasses.clear();
        this.includeRamc = true;
        this.stereotype = null;
        this.filter = null;
    }

    @objid ("46b5f8e4-a1dc-4156-b6d9-75d7dccdcd3c")
    public List<MClass> getMetaclasses() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.metaclasses;
    }

    @objid ("4c5665ed-5b83-43d9-8812-2cc46a090903")
    public static boolean isValidExpression(final String expression) {
        try {
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            return true;
        } catch (final PatternSyntaxException e) {
            return false;
        }
    }

    @objid ("070a9c97-c843-4075-adac-9b8e9eddae5e")
    public void addMetaclass(Class<? extends Element> metaclass) {
        final MClass mc = Metamodel.getMClass(metaclass);
        if (mc != null) {
            this.metaclasses.add(mc);
        }
    }

}
