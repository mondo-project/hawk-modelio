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
                                    

package org.modelio.core.ui.ktable.types.hybrid;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.ui.UIImages;

/**
 * This renderer displays a single element as its name followed by a 'from' clause. The 'from' clause shows the element
 * owner's name.
 * 
 * @author pvlaemyn
 */
@objid ("8ddb5555-c068-11e1-8c0a-002564c97630")
public class HybridCellRenderer extends DefaultCellRenderer {
    @objid ("8ddb5559-c068-11e1-8c0a-002564c97630")
    private boolean displayOwner = true;

    @objid ("a538d6cf-c068-11e1-8c0a-002564c97630")
    private static String RENDERER_LIGHTBULB = "renderer.lightbulb";

    @objid ("8ddb5557-c068-11e1-8c0a-002564c97630")
    protected Text m_text;

    @objid ("8ddb5558-c068-11e1-8c0a-002564c97630")
    private Image indicatorImage = null;

    @objid ("8ddb555b-c068-11e1-8c0a-002564c97630")
    public HybridCellRenderer(int style, boolean displayOwner) {
        super(style);        
        this.displayOwner = displayOwner;
        this.indicatorImage = UIImages.INDICATOR;
    }

    /**
     * Creates an ElementCellRenderer.
     * <p>
     * 
     * <p>
     */
    @objid ("8ddcdbc8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed, KTableModel model) {
        String text;
        
        if (content == null) {
            text = "null";
        } else if (content instanceof ModelElement) {
            ModelElement me = (ModelElement) content;
            text = me.getName();
        
            if (this.displayOwner) {
                Element owner = (Element) me.getCompositionOwner();
                if (owner instanceof ModelElement) {
                    text = text + " (from " + ((ModelElement) owner).getName() + ")";
                }
            }
        } else if (content instanceof String) {
            text = (String) content;
        } else if (content instanceof Element) {
            // TODO CHM metaclass name
            text = ((Element) content).getClass().getSimpleName();
        } else
            text = content.getClass().getSimpleName();
        
        int width = SWTX.getCachedStringExtent(gc, text).x + 20;
        
        if (this.indicatorImage != null) {
            width = width + this.indicatorImage.getBounds().width;
        }
        return width;
    }

    /**
     * A default implementation that paints cells in a way that is more or less Excel-like. Only the cell with focus
     * looks very different.
     * @see de.kupzog.ktable.KTableCellRenderer#drawCell(GC, Rectangle, int, int, Object, boolean, boolean, boolean,
     * KTableModel)
     */
    @objid ("8ddcdbd4-c068-11e1-8c0a-002564c97630")
    @Override
    public void drawCell(GC gc, final Rectangle initialRect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked, KTableModel model) {
        Rectangle rect = initialRect;
        
        applyFont(gc);
        
        String text;
        
        if (content == null) {
            text = "null";
        } else if (content instanceof Operation) {
            //PAN issue #902
            Operation operation = (Operation)content;
            final List<Parameter> parameters = operation.getIO();
            final int parameterNumber = parameters.size();
            
            text = operation.getName();
            text = text + "(";
            
            for (int i = 0; i < parameterNumber; i++) {
                final Parameter parameter = parameters.get(i);
                final GeneralClass type = parameter.getType();
                text = text + (type != null ? type.getName() : CoreUi.I18N.getString("KTable.NoType"));
                if (i < parameterNumber - 1) {
                    text = text + ", ";
                }
            }
            text = text + ")";
           final Parameter returnParam = operation.getReturn();
           if (returnParam != null) {
            final GeneralClass type = returnParam.getType();
            text = text + ":" + (type != null ? type.getName() : CoreUi.I18N.getString("KTable.NoType"));
        }
            
            if (this.displayOwner) {
                Element owner = (Element) operation.getCompositionOwner();
                if (owner instanceof ModelElement) {
                    text = text + CoreUi.I18N.getMessage("ResultsProposalPopup.From", ((ModelElement) owner).getName());
                }
            }
            //PAN
        } else if (content instanceof ModelElement) {
            ModelElement me = (ModelElement) content;
            text = me.getName();
        
            if (this.displayOwner) {
                Element owner = (Element) me.getCompositionOwner();
                if (owner instanceof ModelElement) {
                    text = text + CoreUi.I18N.getMessage("ResultsProposalPopup.From", ((ModelElement) owner).getName());
                }
            }
        } else if (content instanceof String) {
            text = (String) content;
        } else if (content instanceof Element) {
            // TODO CHM metaclass name
            text = ((Element) content).getClass().getSimpleName();
        } else
            text = content.getClass().getSimpleName();
        
        // draw focus sign:
        if (focus && (this.m_Style & INDICATION_FOCUS) != 0) {
            // draw content:
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            drawCellContent(gc, rect, text, null, getForeground(), COLOR_BGFOCUS);
            gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
        
        } else if (focus && (this.m_Style & INDICATION_FOCUS_ROW) != 0) {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_BGROWFOCUS, COLOR_BGROWFOCUS);
            // draw content:
            drawCellContent(gc, rect, text, null, COLOR_FGROWFOCUS, COLOR_BGROWFOCUS);
        
        } else {
            rect = drawDefaultSolidCellLine(gc, rect, COLOR_LINE_LIGHTGRAY, COLOR_LINE_LIGHTGRAY);
            // draw content:
            drawCellContent(gc, rect, text, null, getForeground(), getBackground());
        }
        
        if ((this.m_Style & INDICATION_COMMENT) != 0)
            drawCommentSign(gc, rect);
        
        drawIndicator(gc, rect);
        resetFont(gc);
    }

    /**
     * @param value If true, the comment sign is painted. Else it is omitted.
     */
    @objid ("8ddcdbe2-c068-11e1-8c0a-002564c97630")
    public void setCommentIndication(boolean value) {
        if (value)
            this.m_Style = this.m_Style | INDICATION_COMMENT;
        else
            this.m_Style = this.m_Style & ~INDICATION_COMMENT;
    }

    /**
     * Paints a sign that a comment is present in the right upper corner!
     * @param gc The GC to use when painting.
     * @param rect The cell area where content should be added.
     */
    @objid ("8ddcdbe6-c068-11e1-8c0a-002564c97630")
    protected final void drawIndicator(GC gc, Rectangle rect) {
        if (this.indicatorImage != null) {
            Rectangle imageRect = this.indicatorImage.getBounds();
            gc.drawImage(this.indicatorImage, rect.x + rect.width - imageRect.width, rect.y);
        }
    }

    @objid ("8ddcdbeb-c068-11e1-8c0a-002564c97630")
    @Override
    protected void finalize() throws Throwable {
        this.indicatorImage = null;
        super.finalize();
    }

    @objid ("8ddcdbee-c068-11e1-8c0a-002564c97630")
    public Image getIndicatorImage() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.indicatorImage;
    }

    @objid ("8ddcdbf2-c068-11e1-8c0a-002564c97630")
    public Text getM_text() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.m_text;
    }

}
