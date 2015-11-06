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
                                    

package org.modelio.diagram.elements.umlcommon.packaze;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbstractHintLayout;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.IBrushOptionsSupport;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.figures.PenOptions;
import org.modelio.diagram.elements.core.figures.borders.TLBRBorder;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * Represents a package.
 * <p>
 * This figure is decomposed of an header area and a content area that each can receive one figure.
 * 
 * @author phv
 */
@objid ("8194ad2a-1dec-11e2-8cad-001ec947c8cc")
public class PackageFigure extends Figure implements IPenOptionsSupport, IBrushOptionsSupport {
    /**
     * Header figure placed in the header area.
     */
    @objid ("b92c22de-6e85-4fd7-9a82-03b2729eb4e3")
    private IFigure headerFigure;

    /**
     * Content figure placed in the content area.
     */
    @objid ("80474c67-dc54-44f3-a403-65aeff307fcb")
    private IFigure contentsFigure;

    @objid ("8194ad2e-1dec-11e2-8cad-001ec947c8cc")
    private PenOptions penOptions;

    /**
     * area in which the header figure will be placed
     */
    @objid ("8194ad2f-1dec-11e2-8cad-001ec947c8cc")
    private GradientFigure headerArea;

    /**
     * area in which the content figure will be placed
     */
    @objid ("8194ad31-1dec-11e2-8cad-001ec947c8cc")
    private GradientFigure contentsArea;

    /**
     * Creates a package figure.
     */
    @objid ("8194ad3b-1dec-11e2-8cad-001ec947c8cc")
    public PackageFigure() {
        // init text and line pen support
        this.penOptions = new PenOptions();
        
        // The package figure is a container managing two areas the headerArea and the contentsArea
        // Dedicated figures can be set in each of theses areas.
        // The areas are transparent
        
        this.setLayoutManager(new BorderLayout());
        
        // The top figure contains the header area and a dummy figure
        Figure top = new Figure();
        top.setLayoutManager(new HeaderAreaLayout());
        top.setOpaque(false);
        this.add(top, BorderLayout.TOP);
        
        this.headerArea = new GradientFigure();
        
        // Use a toolbar layout for label wrapping...
        final ToolbarLayout layout = new ToolbarLayout();
        layout.setHorizontal(false);
        layout.setStretchMinorAxis(true);
        this.headerArea.setLayoutManager(layout);
        this.headerArea.setOpaque(true);
        top.add(this.headerArea);
        
        this.contentsArea = new GradientFigure();
        this.contentsArea.setLayoutManager(new BorderLayout());
        this.contentsArea.setPreferredSize(new Dimension(100, 60));
        this.contentsArea.setOpaque(true);
        this.add(this.contentsArea, BorderLayout.CENTER);
        
        this.updateBorders();
        
        this.setOpaque(false);
    }

    /**
     * Get the content figure placed in the content area.
     * @return the content figure.
     */
    @objid ("8194ad3e-1dec-11e2-8cad-001ec947c8cc")
    public IFigure getContentsFigure() {
        return this.contentsFigure;
    }

    @objid ("81970f87-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getFillColor() {
        return this.contentsArea.getFillColor();
    }

    /**
     * Get the header figure.
     * @return the header figure.
     */
    @objid ("81970f8c-1dec-11e2-8cad-001ec947c8cc")
    public IFigure getHeaderFigure() {
        return this.headerFigure;
    }

    @objid ("81970f93-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getLineColor() {
        return this.penOptions.lineColor;
    }

    @objid ("81970f98-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public LinePattern getLinePattern() {
        return this.penOptions.linePattern;
    }

    @objid ("81970f9d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getLineWidth() {
        return this.penOptions.lineWidth;
    }

    @objid ("81970fa2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.penOptions.textColor;
    }

    @objid ("81970fa7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.penOptions.textFont;
    }

    @objid ("81970fac-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean getUseGradient() {
        return this.contentsArea.getUseGradient();
    }

    /**
     * Set the content figure that will be placed in the content area.
     * @param figure the content figure.
     */
    @objid ("81970fb1-1dec-11e2-8cad-001ec947c8cc")
    public void setContentsFigure(IFigure figure) {
        if (!this.contentsArea.getChildren().isEmpty()) {
            this.contentsArea.removeAll();
        }
        
        if (figure != null) {
            this.contentsArea.add(figure, BorderLayout.CENTER);
        }
    }

    @objid ("81970fb7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setFillColor(Color fillColor) {
        this.headerArea.setFillColor(fillColor);
        this.contentsArea.setFillColor(fillColor);
    }

    /**
     * Set the header figure.
     * <p>
     * The header figure will be placed in the header area.
     * @param figure the new header figure.
     */
    @objid ("81970fbb-1dec-11e2-8cad-001ec947c8cc")
    public void setHeaderFigure(IFigure figure) {
        if (!this.headerArea.getChildren().isEmpty()) {
            this.headerArea.removeAll();
        }
        
        if (figure != null) {
            this.headerArea.add(figure, BorderLayout.LEFT);
            figure.setBackgroundColor(ColorConstants.red);
        }
    }

    @objid ("81970fc1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineColor(Color lineColor) {
        this.penOptions.lineColor = lineColor;
        this.updateBorders();
    }

    @objid ("81970fc5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLinePattern(LinePattern linePattern) {
        this.penOptions.linePattern = linePattern;
        this.updateBorders();
    }

    @objid ("81970fc9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineWidth(int lineWidth) {
        this.penOptions.lineWidth = lineWidth;
        this.updateBorders();
    }

    @objid ("81970fcd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.penOptions.textColor = textColor;
    }

    @objid ("81970fd1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        this.penOptions.textFont = textFont;
    }

    @objid ("81970fd5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setUseGradient(boolean useGradient) {
        this.contentsArea.setUseGradient(useGradient);
        this.headerArea.setUseGradient(useGradient);
    }

    @objid ("81970fd9-1dec-11e2-8cad-001ec947c8cc")
    private void updateBorders() {
        final TLBRBorder headerLineBorder = new TLBRBorder(this.penOptions.lineColor,
                                                           this.penOptions.lineWidth,
                                                           true,
                                                           true,
                                                           false,
                                                           true);
        final LineBorder contentLineBorder = new LineBorder(this.penOptions.lineColor,
                                                            this.penOptions.lineWidth);
        headerLineBorder.setStyle(this.getLinePattern().toSWTConstant());
        contentLineBorder.setStyle(this.getLinePattern().toSWTConstant());
        
        this.headerArea.setBorder(new CompoundBorder(headerLineBorder, new MarginBorder(0, 4, 0, 4)));
        this.contentsArea.setBorder(new CompoundBorder(contentLineBorder, new MarginBorder(1)));
    }

    @objid ("81970fdb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Dimension getMinimumSize(int wHint, int hHint) {
        Dimension minimumSize = super.getMinimumSize(wHint, hHint);
        
        if (minimumSize.height < 100) {
            minimumSize.height = 100;
        }
        
        if (minimumSize.width < 100) {
            minimumSize.width = 100;
        }
        return minimumSize;
    }

    @objid ("464d5504-fd46-4550-bbe1-91b4367fc76c")
    @Override
    public void setFillAlpha(int alpha) {
        // ignore
    }

    @objid ("c109747c-e462-4f15-926e-fe3f5b588c04")
    @Override
    public int getFillAlpha() {
        // always opaque
        return 255;
    }

    /**
     * Specific layout for header zone.
     */
    @objid ("819971e2-1dec-11e2-8cad-001ec947c8cc")
    final class HeaderAreaLayout extends AbstractHintLayout {
        @objid ("819971e6-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void layout(final IFigure container) {
            if (!container.getChildren().isEmpty()) {
                IFigure headerAreaFigure = (IFigure) container.getChildren().get(0);
                Rectangle headerAreaBounds = container.getBounds().getCopy();
                int width = headerAreaBounds.width;
                
                // Header can occupy up to 1/2 of available horizontal space but no less than 1/3, and all of vertical space.
                headerAreaBounds.width = Math.min(headerAreaFigure.getPreferredSize().width, width / 2);
                headerAreaBounds.width = Math.max(headerAreaBounds.width, width / 3);
                headerAreaFigure.setBounds(headerAreaBounds);
            }
        }

        @objid ("819971ed-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected Dimension calculatePreferredSize(final IFigure container, final int wHint, final int hHint) {
            if (!container.getChildren().isEmpty()) {
                // Compute base preferred size
                IFigure headerAreaFigure = (IFigure) container.getChildren().get(0);
                Dimension headerAreaPreferredSize = headerAreaFigure.getPreferredSize(wHint, hHint);
                
                // Header can occupy up to 1/2 of available horizontal space but no less than 1/3, and all of vertical space.
                int reducedWidthHint = Math.min(headerAreaFigure.getPreferredSize().width, wHint / 2);
                reducedWidthHint = Math.max(reducedWidthHint, wHint / 3);
                
                // Compute real preferred size, including the reduction
                headerAreaPreferredSize = headerAreaFigure.getPreferredSize(reducedWidthHint, hHint);
                
                return headerAreaPreferredSize.getExpanded(headerAreaPreferredSize.width, 0);
            } else {
                return new Dimension(0, 0);
            }
        }

    }

}
