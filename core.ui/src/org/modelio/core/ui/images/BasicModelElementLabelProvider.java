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
                                    

package org.modelio.core.ui.images;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.PassingMode;
import org.modelio.metamodel.visitors.DefaultModelVisitor;

/**
 * Basic label provider for model elements.
 * <p>
 * Provides the element name as label, the metaclass icon as image and apply text standard
 * decoration (static, abstract).<br>
 * This provider can typically be used as a base provider for a {@link ElementDecoratedStyledLabelProvider}.
 * 
 * @author phv
 */
@objid ("39807b29-9409-41a9-a853-8482f2952d74")
public class BasicModelElementLabelProvider extends AbstractModelioElementLabelProvider {
    @objid ("6d29b7ad-1d8b-4878-9f8f-bc27a891796c")
    private static final StyleRange[] NO_STYLERANGE = new StyleRange[0];

    @objid ("88cbf065-4df5-4c34-a6a8-802db16452c3")
    @Override
    public String getText(Object element) {
        //return ((Element) element).getName();
          BasicModelElementLabelService bmels = new BasicModelElementLabelService();
        return bmels.getLabel((Element)element);
    }

    @objid ("11090afc-b3ae-436d-8f40-4c29daf4e13f")
    @Override
    public Image getImage(Object obj) {
        final Element element = (Element) obj;
        return ElementImageService.getIcon(element);
    }

    @objid ("85f45c86-cba0-482f-a733-f1a25aedfe50")
    @Override
    public StyledString getStyledText(Object element) {
        final String s = getText(element);
        return new StyledString(s);
    }

    @objid ("12da74bb-fbd1-4695-ac6c-6d32fbfbb023")
    private static class BasicModelElementLabelService extends DefaultModelVisitor {
        @objid ("8a3ba406-e9ce-47c9-9c25-0fa387d4898c")
         String label;

        @objid ("94062cac-b313-49ed-bca2-c02dbb5d20da")
        public BasicModelElementLabelService() {
            super();
        }

        @objid ("fad05253-1cb3-40eb-b0b3-ae09ad25860c")
        public String getLabel(Element element) {
            if (element == null)
                return "<null>";
            element.accept(this);
            return this.label;
        }

        @objid ("5850394e-5986-4898-9333-140c834b641b")
        @Override
        public Object visitElement(Element theElement) {
            this.label = theElement.getName();
            return null;
        }

        @objid ("bb7696cd-869e-4651-b1ad-9f134b3616fd")
        @Override
        public Object visitOperation(Operation theOperation) {
            final StyledString symbol = new StyledString();
            final Styler styler = ElementStyler.getStyler(theOperation);
            symbol.append(theOperation.getName(), styler);
            
            symbol.append("(", styler);
            final List<Parameter> parameters = theOperation.getIO();
            final int parameterNumber = parameters.size();
            
            for (int i = 0; i < parameterNumber; i++) {
                final Parameter para = parameters.get(i);
                symbol.append(getParameterSymbol(para, styler, theOperation));
                if (i < parameterNumber - 1) {
                    symbol.append(", ", styler);
                }
            }
            
            symbol.append(")", styler);
            
            final Parameter returnParameter = theOperation.getReturn();
            if (returnParameter != null) {
                final GeneralClass type = returnParameter.getType();
                symbol.append(":", styler);
                symbol.append(" ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(theOperation, type));
                } else {
                    symbol.append(CoreUi.I18N.getString("NoType"), styler);
                }
                symbol.append(getParameterMultiplicity(returnParameter).toString(), ElementStyler.getStyler(theOperation, returnParameter));
            }
            
            this.label = symbol.getString();
            return null;
        }

        @objid ("bd28d639-4550-4609-916e-bbf37c663c65")
        private static StyledString getParameterSymbol(Parameter theParameter, Styler styler, Operation fromOperation) {
            final StyledString symbol = new StyledString();
            
            final PassingMode passingMode = theParameter.getParameterPassing();
            
            final GeneralClass type = theParameter.getType();
            
            if (theParameter.getReturned() != null) {
                symbol.append("out", styler);
            
                symbol.append(" : ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(theParameter, type));
                } else {
                    symbol.append(CoreUi.I18N.getString("NoType"), styler);
                }
            
                symbol.append(getParameterMultiplicity(theParameter).toString(), styler);
            } else if (theParameter.getComposed() != null) {
                symbol.append(theParameter.getName(), styler);
                symbol.append(" ", styler);
            
                if (passingMode == PassingMode.IN) {
                    symbol.append("in", styler);
                }
                if (passingMode == PassingMode.OUT) {
                    symbol.append("out", styler);
                }
                if (passingMode == PassingMode.INOUT) {
                    symbol.append("inout", styler);
                }
            
                symbol.append(" : ", styler);
                if (type != null) {
                    symbol.append(type.getName(), ElementStyler.getStyler(fromOperation==null?theParameter:fromOperation, type));
                } else {
                    symbol.append(CoreUi.I18N.getString("NoType"), styler);
                }
            
                symbol.append(getParameterMultiplicity(theParameter).toString(), styler);
            }
            return symbol;
        }

        @objid ("72c46c9c-a84e-4457-a0d7-5cca6d8f17de")
        private static StringBuilder getParameterMultiplicity(Parameter theParameter) {
            final StringBuilder multiplicity = new StringBuilder();
            
            final String multiplicityMinStr = theParameter.getMultiplicityMin();
            final String multiplicityMaxStr = theParameter.getMultiplicityMax();
            String separator = "";
            
            if (multiplicityMinStr.equals("1") && multiplicityMaxStr.equals("1")) {
                return multiplicity;
            }
            
            if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
                multiplicity.append(" [");
                if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                    multiplicity.append(multiplicityMinStr);
                } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                    multiplicity.append("*");
                } else {
                    if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                        separator = "..";
                    }
            
                    multiplicity.append(multiplicityMinStr);
                    multiplicity.append(separator);
                    multiplicity.append(multiplicityMaxStr);
                }
                multiplicity.append("]");
            }
            return multiplicity;
        }

    }

}
