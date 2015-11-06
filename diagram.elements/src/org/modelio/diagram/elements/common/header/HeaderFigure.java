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
                                    

package org.modelio.diagram.elements.common.header;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.LabelFigure;

/**
 * A header figure is a stack set of:
 * <ul>
 * <li>an icon area</li>
 * <li>a 'top' labels area, containing a vertical stack of labels</li>
 * <li>main label</li>
 * <li>a 'bottom' labels area, containing a vertical stack of labels</li>
 * </ul>
 * <p>
 * <br>
 * The top and bottom labels are displayed using a font derived from the main label font (reduced size).
 * </p>
 */
@objid ("7e6a0dcf-1dec-11e2-8cad-001ec947c8cc")
public class HeaderFigure extends GradientFigure {
    @objid ("7e6c700b-1dec-11e2-8cad-001ec947c8cc")
    protected LabelFigure mainArea;

    @objid ("c81efcb1-a847-4580-934f-4e6b59fc83e4")
    protected Figure mIconsArea;

    @objid ("28518536-b51e-4f6a-9954-fee910f30a27")
    protected Figure sIconsArea;

    @objid ("449a8291-6903-45ad-87bd-7ba635f573f2")
    protected Figure keywordsArea;

    @objid ("b59b014a-270f-4b1d-ace1-0f183a6d941d")
    protected Font stereotypeFont = null;

    @objid ("f8949f1c-c79d-45f4-a076-aaecb0569cbc")
    protected Font tagFont = null;

    @objid ("56664683-7d27-4b6b-ab0b-f3a23e40d7bd")
    protected Label keywordLabel;

    @objid ("3e229773-f600-4b9e-b475-f50782f1e2d6")
    protected Figure tagsArea;

    @objid ("a3519b0b-1aa1-45ca-ba9e-c18f6c3de26d")
    protected Figure keywordLabels;

    /**
     * Constructor.
     */
    @objid ("7e6c7017-1dec-11e2-8cad-001ec947c8cc")
    public HeaderFigure() {
        // The header figure is a 'BorderLayout' container.
        // Children layout:
        // - TOP   : keywordsArea - FlowPage (keyword and stereotypes labels)
        // - RIGHT : sIconsArea   - Figure with tool bar layout (stereotypes icons)
        // - BOTTOM: tagsArea   - FlowPage ( tagged values)
        // - LEFT  : mIconsArea   - Figure with tool bar layout (metaclass icon)
        // - CENTER: mainArea (main label)
        // Children are transparent without borders
        // this.setLayoutManager(new BorderLayout());
        Figure container = this;
        container.setLayoutManager(new BorderLayout());
        // TRACE: container.setBorder(new LineBorder(ColorConstants.red, 2));
        
        // -- LEFT Area --
        this.mIconsArea = createMIconsArea();
        container.add(this.mIconsArea, BorderLayout.LEFT);
        
        // -- RIGHT Area --
        this.sIconsArea = createSIconsAreaFigures();
        container.add(this.sIconsArea, BorderLayout.RIGHT);
        
        // -- TOP Area --
        this.keywordsArea = createKeywordsArea();
        container.add(this.keywordsArea, BorderLayout.TOP);
        
        // -- CENTER Area --
        this.mainArea = createMainArea();
        container.add(this.mainArea, BorderLayout.CENTER);
        
        // -- BOTTOM area --
        this.tagsArea = createTagsArea();
        container.add(this.tagsArea, BorderLayout.BOTTOM);
    }

    /**
     * Set the icons displayed on the upper left corner.
     * @param icons The left icons
     */
    @objid ("7e6c701a-1dec-11e2-8cad-001ec947c8cc")
    public void setLeftIcons(List<Image> icons) {
        // remove existing labels
        this.mIconsArea.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            this.mIconsArea.add(imgFigure);
        }
    }

    /**
     * Set the icons displayed on the upper right corner.
     * @param icons The right icons
     */
    @objid ("7e6c7020-1dec-11e2-8cad-001ec947c8cc")
    public void setRightIcons(List<Image> icons) {
        // remove existing labels
        this.sIconsArea.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            this.sIconsArea.add(imgFigure);
            
        }
    }

    /**
     * Set the keyword label.
     * @param text the keyword label.
     */
    @objid ("7e6c7026-1dec-11e2-8cad-001ec947c8cc")
    public void setKeywordLabel(String text) {
        if (text == null) {
            if (this.keywordLabel != null) {
                this.keywordsArea.remove(this.keywordLabel);
                this.keywordLabel = null;
            }
        } else {
            if (this.keywordLabel != null) {
                this.keywordLabel.setText(text);
            } else {
                this.keywordLabel = new Label(text);
                this.keywordLabel.setLabelAlignment(PositionConstants.CENTER);
                this.keywordLabel.setOpaque(false);
                this.keywordLabel.setFont(this.stereotypeFont);
                // TRACE: this.keywordLabel.setBorder(new LineBorder(ColorConstants.blue, 1));
                this.keywordsArea.add(this.keywordLabel, 1);
            }
        
        }
    }

    /**
     * Set the labels displayed on top of the main label.
     * @param keywordLabels the top labels.
     */
    @objid ("7e6c702a-1dec-11e2-8cad-001ec947c8cc")
    public void setTopLabels(List<String> keywordLabels) {
        // remove existing labels
        this.keywordLabels.removeAll();
        
        // add new label figures
        for (String s : keywordLabels) {
            Label labelFigure = new Label(s);
            // TRACE: labelFigure.setBorder(new LineBorder(1));
            labelFigure.setTextAlignment(PositionConstants.CENTER);
            this.keywordLabels.add(labelFigure);
        }
    }

    /**
     * Set the main label.
     * @param s the main label.
     */
    @objid ("7e6c7030-1dec-11e2-8cad-001ec947c8cc")
    public void setMainLabel(String s) {
        this.mainArea.setText(s);
    }

    /**
     * Set the labels displayed below the main label.
     * @param bottomLabels the bottom labels.
     */
    @objid ("7e6c7034-1dec-11e2-8cad-001ec947c8cc")
    public void setBottomLabels(List<String> bottomLabels) {
        // remove existing labels
        this.tagsArea.removeAll();
        
        // add new label figures
        for (String s : bottomLabels) {
            Label labelFigure = new Label(s);
            labelFigure.setTextAlignment(PositionConstants.CENTER);
            this.tagsArea.add(labelFigure);
        }
    }

    /**
     * Get the main label figure.
     * @return the main label figure.
     */
    @objid ("7e6c703a-1dec-11e2-8cad-001ec947c8cc")
    Label getMainLabelFigure() {
        return this.mainArea;
    }

    @objid ("7e6c7041-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.mainArea.getForegroundColor();
    }

    @objid ("7e6c7046-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.mainArea.getFont();
    }

    @objid ("7e6c704b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.keywordLabels.setForegroundColor(textColor);
        if (this.keywordLabel != null)
            this.keywordLabel.setForegroundColor(textColor);
        this.mainArea.setForegroundColor(textColor);
        this.tagsArea.setForegroundColor(textColor);
    }

    @objid ("7e6c704f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        updateDerivedFonts(textFont);
        
        this.keywordLabels.setFont(this.stereotypeFont);
        if (this.keywordLabel != null)
            this.keywordLabel.setFont(this.stereotypeFont);
        this.mainArea.setFont(textFont);
        this.tagsArea.setFont(this.tagFont);
    }

    @objid ("7e6ed255-1dec-11e2-8cad-001ec947c8cc")
    private void updateDerivedFonts(Font baseFont) {
        if (this.mainArea.getFont() == baseFont && this.tagFont != null && this.stereotypeFont != null)
            return;
        
        FontData[] fontData = baseFont.getFontData();
        for (FontData data : fontData) {
            data.setHeight(deriveFontHeight(data.getHeight()));
        }
        this.stereotypeFont = CoreFontRegistry.getFont(fontData);
        this.tagFont = CoreFontRegistry.getModifiedFont(this.stereotypeFont, SWT.ITALIC);
    }

    @objid ("7e6ed258-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getLineColor() {
        return null;
    }

    @objid ("7e6ed25d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getLineWidth() {
        return 0;
    }

    @objid ("7e6ed262-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineColor(Color lineColor) {
        // Nothing to do.
    }

    @objid ("7e6ed266-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineWidth(int lineWidth) {
        // Nothing to do.
    }

    @objid ("7e6ed26a-1dec-11e2-8cad-001ec947c8cc")
    private int deriveFontHeight(int height) {
        switch (height) {
        case 8:
            return 7;
        
        case 9:
            return 7;
        
        case 10:
            return 8;
        
        case 11:
            return 8;
        
        case 12:
            return 9;
        
        case 13:
            return 10;
        
        case 14:
            return 10;
        
        default:
            if (height < 8)
                return height;
            // else
            return height * 10 / 14;
        }
    }

    @objid ("7e6ed26e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Dimension getMinimumSize(int wHint, int hHint) {
        // Always ignore the vertical hint!
        return super.getMinimumSize(wHint, -1);
    }

    /**
     * Set whether the main label is underlined.
     * @param underline true to underline the main label
     */
    @objid ("7e6ed277-1dec-11e2-8cad-001ec947c8cc")
    public void setUnderline(final boolean underline) {
        this.mainArea.setUnderline(underline);
    }

    /**
     * Set whether the main label is stroked through.
     * @param strikeThrough true to strike the label
     */
    @objid ("7e6ed27c-1dec-11e2-8cad-001ec947c8cc")
    public void setStrikeThrough(final boolean strikeThrough) {
        this.mainArea.setStrikeThrough(strikeThrough);
    }

/*
     * A Figure holding texts
     */
    @objid ("40480b74-bb5e-4b4a-85c1-6040305eefc3")
    protected Figure createTagsArea() {
        Figure bottomLabels = new Figure();
        ToolbarLayout bottomAreaLayout = new ToolbarLayout(false);
        bottomAreaLayout.setSpacing(0);
        bottomAreaLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        bottomAreaLayout.setStretchMinorAxis(true);
        bottomLabels.setLayoutManager(bottomAreaLayout);
        bottomLabels.setOpaque(false);
        // TRACE: this.bottomLabelsArea.setBorder(new LineBorder(ColorConstants.blue, 1));
        return bottomLabels;
    }

/*
     * A Figure holding two texts: keyword and stereotypes
     */
    @objid ("78b50383-ec87-493b-8212-fdee1d177725")
    protected Figure createKeywordsArea() {
        Figure top = new Figure();
        ToolbarLayout tbLayout = new ToolbarLayout(false);
        tbLayout.setStretchMinorAxis(true);
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        top.setLayoutManager(tbLayout);
        
        // first row : Keyword label
        this.keywordLabel = new Label("");
        top.add(this.keywordLabel);
        
        // second row: top labels
        this.keywordLabels = new Figure();
        ToolbarLayout topAreaLayout = new ToolbarLayout(false);
        topAreaLayout.setSpacing(0);
        topAreaLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        topAreaLayout.setStretchMinorAxis(true);
        this.keywordLabels.setLayoutManager(topAreaLayout);
        this.keywordLabels.setOpaque(false);
        top.add(this.keywordLabels);
        return top;
    }

/*
     * A LabelFigure to hold the main label
     */
    @objid ("524fb36a-0a3e-4101-ad3c-cf2f193bc478")
    protected LabelFigure createMainArea() {
        LabelFigure label = new LabelFigure();
        label.setLabelAlignment(PositionConstants.CENTER);
        label.setBorder(new MarginBorder(2, 0, 3, 0));
        return label;
    }

/*
     * An horizontal tool bar layouted container, center aligned
     */
    @objid ("4441e618-c03d-4590-af59-e671c81852f2")
    protected Figure createMIconsArea() {
        Figure leftFigure = new Figure();
        ToolbarLayout tbLayout = new ToolbarLayout(true);
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        tbLayout.setSpacing(1);
        leftFigure.setLayoutManager(tbLayout);
        leftFigure.setOpaque(false);
        leftFigure.setBorder(new MarginBorder(new Insets(0, 1, 0, 1)));
        // TRACE:  leftFigure.setBorder(new LineBorder(1));
        return leftFigure;
    }

/*
     * An horizontal tool bar layouted container, center aligned
     */
    @objid ("a2c9b9f0-e078-46c4-8904-0d80357d3a2d")
    protected Figure createSIconsAreaFigures() {
        Figure rightFigure = new Figure();
        ToolbarLayout tbLayout = new ToolbarRLayout();
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        tbLayout.setSpacing(1);
        rightFigure.setLayoutManager(tbLayout);
        rightFigure.setOpaque(false);
        rightFigure.setBorder(new MarginBorder(new Insets(0, 1, 0, 1)));
        // TRACE: rightIconsContainer.setBorder(new LineBorder(1));
        return rightFigure;
    }

    /**
     * This class implements a right-aligned ToolBarLayout, ie children are stacked on the right side of the toolbar. NOTE: A
     * ToolbarRLayout is always horizontal.
     */
    @objid ("7e6ed281-1dec-11e2-8cad-001ec947c8cc")
    public static class ToolbarRLayout extends ToolbarLayout {
        @objid ("7e6ed286-1dec-11e2-8cad-001ec947c8cc")
        public ToolbarRLayout(boolean isHorizontal) {
            super(isHorizontal);
        }

        @objid ("7e6ed289-1dec-11e2-8cad-001ec947c8cc")
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

        @objid ("7e6ed28f-1dec-11e2-8cad-001ec947c8cc")
        public ToolbarRLayout() {
            super(true); // force horizontal
        }

    }

}
