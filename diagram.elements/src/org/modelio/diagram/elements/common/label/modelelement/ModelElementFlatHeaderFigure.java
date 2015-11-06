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
                                    

package org.modelio.diagram.elements.common.label.modelelement;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.LabelFigure;

/**
 * A header figure is an horizontal stack set of:
 * <ul>
 * <li>an experimental optional 'left' figure</li>
 * <li>an optional 'left' icon</li>
 * <li>a label composed of a keyword, stereotype, main and tag part
 * <li>a 'right' icon area</li>
 * </ul>
 * </p>
 */
@objid ("7e9e819f-1dec-11e2-8cad-001ec947c8cc")
public class ModelElementFlatHeaderFigure extends GradientFigure {
    @objid ("907b09c7-1e83-11e2-8cad-001ec947c8cc")
    private String stereotypeText;

    @objid ("907b09cc-1e83-11e2-8cad-001ec947c8cc")
    private String mainText;

    @objid ("907b09d1-1e83-11e2-8cad-001ec947c8cc")
    private String tagText;

    @objid ("907b09d6-1e83-11e2-8cad-001ec947c8cc")
    private String keywordText;

    /**
     * The main label
     */
    @objid ("7e9e81a5-1dec-11e2-8cad-001ec947c8cc")
    private LabelFigure mainLabel;

    /**
     * The right icons container.
     */
    @objid ("65492f32-1e83-11e2-8cad-001ec947c8cc")
    private Figure rightIconsContainer;

    /**
     * Experimental left figure.
     */
    @objid ("654b918c-1e83-11e2-8cad-001ec947c8cc")
    private IFigure leftFigure = null;

    /**
     * Creates a ModelElementLabelFigure.
     */
    @objid ("7ea0e3b8-1dec-11e2-8cad-001ec947c8cc")
    public ModelElementFlatHeaderFigure() {
        // The header figure is a horizontal toolbar layouted container.
        // There are plenty children ordered from left to right: 
        //   - the left icons  
        //   - the keyword
        //   - the left labels
        //   - the main label
        //   - the right labels
        //   - the right icons  
        // Children are transparent without borders.
        
        //final ToolbarLayout layoutManager = new ToolbarLayout(true);
        //layoutManager.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
        
        final BorderLayout layoutManager = new BorderLayout();
        this.setLayoutManager(layoutManager);
        
        // Fourth child: the main label area
        this.mainLabel = new LabelFigure();
        this.mainLabel.setTextAlignment(PositionConstants.LEFT);
        //this.mainLabel.setBorder(new LineBorder(ColorConstants.darkGreen, 1));
        this.mainLabel.setIconAlignment(PositionConstants.LEFT);
        this.mainLabel.setLabelAlignment(PositionConstants.LEFT);
        this.add(this.mainLabel, BorderLayout.CENTER);
        
        // Right icons container
        // an horizontal toolbar layouted container, right aligned
        this.rightIconsContainer = new Figure();
        final ToolbarLayout righIconAreaLayout = new ToolbarRLayout(true);
        righIconAreaLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        righIconAreaLayout.setSpacing(1);
        this.rightIconsContainer.setLayoutManager(righIconAreaLayout);
        //this.rightIconsContainer.setBorder(new LineBorder(ColorConstants.cyan,1));
        this.rightIconsContainer.setOpaque(false);
        this.add(this.rightIconsContainer, BorderLayout.RIGHT);
        
        // CENTER figure : the contents area
        // The contents area is a grid layouted figure holding a centered cell child
        
        //this.setBorder(new LineBorder());
    }

    @objid ("7ea0e3bb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void addNotify() {
        super.addNotify();
        setTextFont(getFont());
    }

    @objid ("7ea0e3be-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getLineColor() {
        return null;
    }

    @objid ("7ea0e3c3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getLineWidth() {
        return 0;
    }

    @objid ("7ea0e3c8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.mainLabel.getForegroundColor();
    }

    @objid ("7ea0e3cd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.mainLabel.getFont();
    }

    /**
     * Experimental: set the left figure.
     * <p>
     * This figure is placed at the left of the label and its optional image.
     * @param aFigure The new left figure.
     */
    @objid ("7ea0e3d2-1dec-11e2-8cad-001ec947c8cc")
    public void setLeftFigure(IFigure aFigure) {
        if (this.leftFigure != null)
            remove(this.leftFigure);
        
        this.leftFigure = aFigure;
        add(aFigure, BorderLayout.LEFT);
    }

    /**
     * Set the left icon
     * @param leftIcon The left icon.
     */
    @objid ("7ea0e3d8-1dec-11e2-8cad-001ec947c8cc")
    public void setLeftIcon(Image leftIcon) {
        this.mainLabel.setIcon(leftIcon);
    }

    /**
     * Set the stereotype labels.
     * @param value the stereotype label.
     */
    @objid ("7ea0e3dc-1dec-11e2-8cad-001ec947c8cc")
    public void setStereotypeText(String value) {
        if (value != null && value.equals(this.stereotypeText))
            return;
        
        // The text should never be null
        if (value == null) {
            this.stereotypeText = "";
        } else {
            this.stereotypeText = value;
        }
        
        updateLabel();
    }

    @objid ("7ea0e3e0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineColor(Color lineColor) {
        // Nothing to do
    }

    @objid ("7ea0e3e4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineWidth(int lineWidth) {
        // Nothing to do
    }

    /**
     * Set the main label.
     * <p>
     * The main label is usually the element name with its signature.
     * @param s The new main label
     */
    @objid ("7ea0e3e8-1dec-11e2-8cad-001ec947c8cc")
    public void setMainLabel(String s) {
        this.mainText = s;
        updateLabel();
    }

    /**
     * Set the right icons.
     * @param icons the right icons.
     */
    @objid ("7ea0e3ec-1dec-11e2-8cad-001ec947c8cc")
    public void setRightIcons(List<Image> icons) {
        // remove existing icons
        this.rightIconsContainer.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            this.rightIconsContainer.add(imgFigure);
        }
    }

    /**
     * Set the right text.
     * @param value the right text.
     */
    @objid ("7ea0e3f2-1dec-11e2-8cad-001ec947c8cc")
    public void setTagText(String value) {
        if (value != null && value.equals(this.tagText))
            return;
        
        // The text should never be null
        if (value == null) {
            this.tagText = "";
        } else {
            this.tagText = value;
        }
        
        updateLabel();
    }

    @objid ("7ea0e3f6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.mainLabel.setForegroundColor(textColor);
    }

    @objid ("7ea0e3fa-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        //this.mainLabel.setFont(textFont);
        this.setFont(textFont);
    }

    /**
     * Get the main label figure.
     * @return the main label figure.
     */
    @objid ("7ea0e3fe-1dec-11e2-8cad-001ec947c8cc")
    Label getMainLabelFigure() {
        return this.mainLabel;
    }

    /**
     * Update the displayed label
     */
    @objid ("7ea34613-1dec-11e2-8cad-001ec947c8cc")
    private void updateLabel() {
        StringBuilder labelText = new StringBuilder();
        
        if (this.keywordText != null && !this.keywordText.isEmpty()) {
            labelText.append(this.keywordText);
            labelText.append(" ");
        }
        
        if (this.stereotypeText != null && !this.stereotypeText.isEmpty()) {
            labelText.append(this.stereotypeText);
            labelText.append(" ");
        }
        
        if (this.mainText != null && !this.mainText.isEmpty()) {
            labelText.append(this.mainText);
            labelText.append(" ");
        }
        
        if (this.tagText != null && !this.tagText.isEmpty()) {
            labelText.append(this.tagText);
        }
        
        this.mainLabel.setText(labelText.toString());
    }

    /**
     * Set the keyword labels.
     * @param value the keyword label.
     */
    @objid ("7ea34616-1dec-11e2-8cad-001ec947c8cc")
    public void setKeywordText(String value) {
        if (value != null && value.equals(this.keywordText))
            return;
        
        // The text should never be null
        if (value == null) {
            this.keywordText = "";
        } else {
            this.keywordText = value;
        }
        
        updateLabel();
    }

    @objid ("7ea3461a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setFont(final Font f) {
        super.setFont(f);
        this.mainLabel.setFont(f);
    }

    /**
     * Set whether the main label is underlined.
     * @param underline true to underline the main label
     */
    @objid ("7ea3461f-1dec-11e2-8cad-001ec947c8cc")
    public void setUnderline(final boolean underline) {
        this.mainLabel.setUnderline(underline);
    }

    /**
     * Set whether the main label is stroked through.
     * @param strikeThrough true to strike the label
     */
    @objid ("7ea34624-1dec-11e2-8cad-001ec947c8cc")
    public void setStrikeThrough(final boolean strikeThrough) {
        this.mainLabel.setStrikeThrough(strikeThrough);
    }

    /**
     * This class implements a right-aligned ToolBarLayout, ie children are stacked on the right side of the toolbar.
     * <p>
     * NOTE: A ToolbarRLayout is always horizontal.
     */
    @objid ("7ea34629-1dec-11e2-8cad-001ec947c8cc")
    private static class ToolbarRLayout extends ToolbarLayout {
        @objid ("7ea3462e-1dec-11e2-8cad-001ec947c8cc")
        public ToolbarRLayout(boolean isHorizontal) {
            super(isHorizontal);
        }

        @objid ("7ea34631-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void layout(IFigure parent) {
            super.layout(parent);
            final int Xl = parent.getBounds().x;
            final int Xr = parent.getBounds().right();
            
            for (Object obj : parent.getChildren()) {
                final IFigure child = (IFigure) obj;
                final Rectangle aBounds = child.getBounds().getCopy();
                child.translate(Xr - (aBounds.x - Xl) - aBounds.x - aBounds.width, 0);
            }
        }

    }

}
