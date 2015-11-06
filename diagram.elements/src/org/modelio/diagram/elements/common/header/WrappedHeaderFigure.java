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
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.AbstractFlowBorder;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowContext;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.elements.core.figures.GradientFigure;

/**
 * A wrapped header figure is a stack set of:
 * <ul>
 * <li>an icon area</li>
 * <li>a 'top' labels area, containing a vertical stack of labels</li>
 * <li>main label</li>
 * <li>a 'bottom' labels area, containing a vertical stack of labels</li>
 * </ul>
 * <p>
 * <br>
 * All labels wraps to a new line. The top and bottom labels are displayed using a font derived from the main label font (reduced
 * size).
 * </p>
 */
@objid ("7e75f9af-1dec-11e2-8cad-001ec947c8cc")
public class WrappedHeaderFigure extends GradientFigure {
    @objid ("66b5f6fb-1e83-11e2-8cad-001ec947c8cc")
    private FlowPage topArea;

    @objid ("66b13249-1e83-11e2-8cad-001ec947c8cc")
    private Figure rightArea;

    @objid ("0ba8b524-6f4d-4ae0-8dab-2df39f8d8c12")
    private FlowPage bottomArea;

    @objid ("66b13248-1e83-11e2-8cad-001ec947c8cc")
    private Figure leftArea;

    @objid ("7e785bcb-1dec-11e2-8cad-001ec947c8cc")
    private HeaderTextFlow mainLabel;

    @objid ("66b1324a-1e83-11e2-8cad-001ec947c8cc")
    private TextFlow keywordLabel;

    @objid ("66b394a1-1e83-11e2-8cad-001ec947c8cc")
    private BlockFlow topLabels;

    @objid ("66b394a2-1e83-11e2-8cad-001ec947c8cc")
    private BlockFlow bottomLabels;

    @objid ("7e785bcf-1dec-11e2-8cad-001ec947c8cc")
    private Font stereotypeFont = null;

    @objid ("7e785bd0-1dec-11e2-8cad-001ec947c8cc")
    private Font tagFont = null;

    @objid ("4f9918c0-8924-4871-bc51-a9f2834bf2a9")
    private BlockFlow mainLabelBlock;

    /**
     * Constructor.
     */
    @objid ("7e7abe1a-1dec-11e2-8cad-001ec947c8cc")
    public WrappedHeaderFigure() {
        // The header figure is a 'BorderLayout' container.
        // Children layout:
        // - TOP : topArea - FlowPage (keyword and stereotypes labels)
        // - RIGHT : rightArea - Figure with tool bar layout (stereotypes icons)
        // - BOTTOM: bottomArea - FlowPage ( tagged values)
        // - LEFT : leftArea - Figure with tool bar layout (metaclass icon)
        // - CENTER: contentsArea (main label)
        // Children are transparent without borders
        Figure container = this;
        container.setLayoutManager(new BorderLayout());
        // TRACE: container.setBorder(new LineBorder(ColorConstants.orange, 2));
        
        // -- LEFT Area --
        this.leftArea = createLeftFigures(container);
        
        // -- RIGHT Area --
        this.rightArea = createRightFigures(container);
        
        // -- TOP Area --
        
        this.topArea = createTopFigures(container);
        
        // -- BOTTOM area --
        // Lazily added. Do nothing
        
        // -- CENTER Area --
        // a Flowpage/TextFlow to hold the main label
        this.mainLabel = createCenterFigures(container);
    }

    /**
     * Set the icons displayed on the upper left corner.
     * @param icons The left icons
     */
    @objid ("7e7abe1d-1dec-11e2-8cad-001ec947c8cc")
    public void setLeftIcons(List<Image> icons) {
        // remove existing labels
        this.leftArea.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            this.leftArea.add(imgFigure);
        }
    }

    /**
     * Set the icons displayed on the upper right corner.
     * @param icons The right icons
     */
    @objid ("7e7abe23-1dec-11e2-8cad-001ec947c8cc")
    public void setRightIcons(List<Image> icons) {
        // remove existing labels
        this.rightArea.removeAll();
        // add new image figures
        for (Image img : icons) {
            ImageFigure imgFigure = new ImageFigure(img);
            this.rightArea.add(imgFigure);
        }
    }

    /**
     * Set the keyword label.
     * @param text the keyword label.
     */
    @objid ("7e7abe29-1dec-11e2-8cad-001ec947c8cc")
    public void setKeywordLabel(String text) {
        if (text == null) {
            if (this.keywordLabel != null) {
                this.topArea.remove(this.keywordLabel.getParent());
                this.keywordLabel = null;
            }
        } else {
            if (this.keywordLabel != null) {
                this.keywordLabel.setText(text);
            } else {
                BlockFlow keywordBlock = new BlockFlow();
                keywordBlock.setHorizontalAligment(PositionConstants.CENTER);
                this.keywordLabel = new TextFlow(text);
                this.keywordLabel.setOpaque(false);
                this.keywordLabel.setFont(this.stereotypeFont);
                // TRACE: this.keywordLabel.setBorder(new LineBorder(ColorConstants.blue, 1));
        
                // XXX Workaround for silent diagram editor: since validation is synchronous, we must set the flow context before
                // adding the figure.
                keywordBlock.setFlowContext((FlowContext) this.topArea.getLayoutManager());
                this.topArea.add(keywordBlock, 1);
        
                this.keywordLabel.setFlowContext((FlowContext) keywordBlock.getLayoutManager());
                keywordBlock.add(this.keywordLabel);
            }
        
        }
    }

    /**
     * Set the labels displayed on top of the main label.
     * @param topLabels the top labels.
     */
    @objid ("7e7abe2d-1dec-11e2-8cad-001ec947c8cc")
    public void setTopLabels(List<String> topLabels) {
        // remove existing labels
        this.topLabels.removeAll();
        
        if (topLabels.isEmpty())
            return;
        
        StringBuilder sb = new StringBuilder(topLabels.size() * 20);
        for (String s : topLabels) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(s.replace(" ", "\u00A0")); // replace spaces by non breaking spaces
        }
        
        TextFlow labelFigure = new TextFlow(sb.toString());
        // TRACE: labelFigure.setBorder(new LineBorder(1));
        
        // XXX Workaround for silent diagram editor: since validation is synchronous, we must set the flow context before adding the
        // figure.
        labelFigure.setFlowContext((FlowContext) this.topLabels.getLayoutManager());
        this.topLabels.add(labelFigure);
    }

    /**
     * Set the main label.
     * @param s the main label.
     */
    @objid ("7e7abe33-1dec-11e2-8cad-001ec947c8cc")
    public void setMainLabel(String s) {
        this.mainLabel.setText(s);
        
        if (s.isEmpty()) { 
            if(this.mainLabel.getParent() == this.mainLabelBlock)
                this.mainLabelBlock.remove(this.mainLabel);
        } else if (this.mainLabel.getParent() == null)
            this.mainLabelBlock.add(this.mainLabel);
        
        revalidate();
    }

    /**
     * Set the labels displayed below the main label.
     * @param bottomLabels the bottom labels.
     */
    @objid ("7e7abe37-1dec-11e2-8cad-001ec947c8cc")
    public void setBottomLabels(List<String> bottomLabels) {
        if (bottomLabels == null || bottomLabels.isEmpty()) {
            if (this.bottomArea != null) {
                // this.table.remove(this.bottomLabelsArea);
                this.remove(this.bottomArea);
                this.bottomArea = null;
            }
        } else {
            if (this.bottomArea == null) {
                this.bottomArea = new FlowPage();
                this.bottomLabels = new BlockFlow();
                this.bottomLabels.setHorizontalAligment(PositionConstants.CENTER);
                this.bottomLabels.setFont(this.tagFont);
                this.bottomLabels.setForegroundColor(this.mainLabel.getForegroundColor());
                // this.bottomLabelsArea.setOpaque(false);
                // TRACE: this.bottomLabelsArea.setBorder(new LineBorder(ColorConstants.blue, 1));
        
                // XXX Workaround for silent diagram editor: since validation is synchronous, we must set the flow context before
                // adding the figure.
                this.bottomLabels.setFlowContext((FlowContext) this.bottomArea.getLayoutManager());
                // this.table.add(this.bottomLabelsArea);
        
                this.bottomArea.add(this.bottomLabels);
                this.add(this.bottomArea, BorderLayout.BOTTOM);
            } else {
                // remove existing labels
                this.bottomLabels.removeAll();
        
            }
        
            StringBuilder sb = new StringBuilder(bottomLabels.size() * 20);
        
            // add new label figures
            for (String s : bottomLabels) {
                if (sb.length() != 0)
                    sb.append("\n");
                sb.append(s);
            }
        
            TextFlow labelFigure = new TextFlow(sb.toString());
            // XXX Workaround for silent diagram editor: since validation is synchronous, we must set the flow context before adding
            // the figure.
            labelFigure.setFlowContext((FlowContext) this.bottomLabels.getLayoutManager());
            this.bottomLabels.add(labelFigure);
        }
    }

    @objid ("7e7abe44-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getTextColor() {
        return this.mainLabel.getForegroundColor();
    }

    @objid ("7e7abe49-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Font getTextFont() {
        return this.mainLabel.getFont();
    }

    @objid ("7e7abe4e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextColor(Color textColor) {
        this.topLabels.setForegroundColor(textColor);
        if (this.keywordLabel != null)
            this.keywordLabel.setForegroundColor(textColor);
        this.mainLabel.setForegroundColor(textColor);
        if (this.bottomLabels != null)
            this.bottomLabels.setForegroundColor(textColor);
    }

    @objid ("7e7abe52-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setTextFont(Font textFont) {
        updateDerivedFonts(textFont);
        
        this.topLabels.setFont(this.stereotypeFont);
        if (this.keywordLabel != null)
            this.keywordLabel.setFont(this.stereotypeFont);
        this.mainLabel.setFont(textFont);
        if (this.bottomLabels != null)
            this.bottomLabels.setFont(this.tagFont);
    }

    @objid ("7e7abe59-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Color getLineColor() {
        return null;
    }

    @objid ("7e7abe5e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getLineWidth() {
        return 0;
    }

    @objid ("7e7abe63-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineColor(Color lineColor) {
        // Nothing to do.
    }

    @objid ("7e7d2071-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setLineWidth(int lineWidth) {
        // Nothing to do.
    }

    @objid ("7e7d2079-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Dimension getMinimumSize(int wHint, int hHint) {
        // Always ignore the vertical hint!
        return super.getMinimumSize(wHint, -1);
    }

    /**
     * Set whether the main label is underlined.
     * @param underline true to underline the main label
     */
    @objid ("7e7d2082-1dec-11e2-8cad-001ec947c8cc")
    public void setUnderline(final boolean underline) {
        this.mainLabel.setUnderline(underline);
    }

    /**
     * Set whether the main label is stroked through.
     * @param strikeThrough true to strike the label
     */
    @objid ("7e7d2087-1dec-11e2-8cad-001ec947c8cc")
    public void setStrikeThrough(final boolean strikeThrough) {
        this.mainLabel.setStrikeThrough(strikeThrough);
    }

    /**
     * Get the main label figure.
     * @return the main label figure.
     */
    @objid ("7e7abe3d-1dec-11e2-8cad-001ec947c8cc")
    public TextFlow getMainLabelFigure() {
        return this.mainLabel;
    }

    @objid ("7e7abe56-1dec-11e2-8cad-001ec947c8cc")
    private void updateDerivedFonts(Font baseFont) {
        if (this.mainLabel.getFont() == baseFont && this.tagFont != null && this.stereotypeFont != null)
            return;
        
        FontData[] fontData = baseFont.getFontData();
        for (FontData data : fontData) {
            data.setHeight(deriveFontHeight(data.getHeight()));
        }
        this.stereotypeFont = CoreFontRegistry.getFont(fontData);
        this.tagFont = CoreFontRegistry.getModifiedFont(this.stereotypeFont, SWT.ITALIC);
    }

    @objid ("7e7d2075-1dec-11e2-8cad-001ec947c8cc")
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

    @objid ("aae63863-c1fa-497f-8254-c7ede775788e")
    protected HeaderTextFlow createCenterFigures(Figure container) {
        FlowPage centerArea = new FlowPage();
        this.mainLabelBlock = new BlockFlow();
        this.mainLabelBlock.setHorizontalAligment(PositionConstants.CENTER);
        HeaderTextFlow label = new HeaderTextFlow();
        this.mainLabelBlock.add(label);
        // this.mainLabel.setBorder(new MarginBorder(2, 0, 4, 0));
        this.mainLabelBlock.setBorder(new MarginFlowBorder(new Insets(2, 0, 3, 0)));
        centerArea.add(this.mainLabelBlock);
        container.add(centerArea, BorderLayout.CENTER);
        return label;
    }

/*
     * A FlowPage holding two texts: keyword and stereotypes
     */
    @objid ("ed7ac99f-993e-4c96-8279-3e2247e6445b")
    protected FlowPage createTopFigures(Figure container) {
        FlowPage topFigure = new FlowPage();
        
        // first row : Keyword label
        BlockFlow keywordBlock = new BlockFlow();
        keywordBlock.setHorizontalAligment(PositionConstants.CENTER);
        this.keywordLabel = new TextFlow("");
        keywordBlock.add(this.keywordLabel);
        topFigure.add(keywordBlock);
        
        // second row: top labels
        this.topLabels = new BlockFlow();
        this.topLabels.setHorizontalAligment(PositionConstants.CENTER);
        // TRACE: this.topLabelsArea.setBorder(new LineBorder(ColorConstants.green, 1));
        topFigure.add(this.topLabels);
        container.add(topFigure, BorderLayout.TOP);
        return topFigure;
    }

/*
     * An horizontal tool bar layouted container, center aligned
     */
    @objid ("4aa4308a-f09c-4ce2-9ff0-58f28e9bc061")
    protected Figure createRightFigures(Figure container) {
        Figure rightFigure = new Figure();
        ToolbarLayout tbLayout = new ToolbarRLayout();
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        tbLayout.setSpacing(1);
        rightFigure.setLayoutManager(tbLayout);
        rightFigure.setOpaque(false);
        rightFigure.setBorder(new MarginFlowBorder(new Insets(0, 1, 0, 1)));
        // TRACE: rightIconsContainer.setBorder(new LineBorder(1));
        container.add(rightFigure, BorderLayout.RIGHT);
        return rightFigure;
    }

/*
     * an horizontal tool bar layouted container, center aligned
     */
    @objid ("3507a4fb-44ec-43eb-85bf-4b6b7db34ed9")
    protected Figure createLeftFigures(Figure container) {
        Figure leftFigure = new Figure();
        ToolbarLayout tbLayout = new ToolbarLayout(true);
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        tbLayout.setSpacing(1);
        leftFigure.setLayoutManager(tbLayout);
        leftFigure.setOpaque(false);
        leftFigure.setBorder(new MarginFlowBorder(new Insets(0, 1, 0, 1)));
        // TRACE: leftIconsContainer.setBorder(new LineBorder(1));
        container.add(leftFigure, BorderLayout.LEFT);
        return leftFigure;
    }

    /**
     * Sets the horitontal aligment of the main label block. Valid values are:
     * <UL>
     * <LI>{@link PositionConstants#NONE NONE} - (default) Alignment is
     * inherited from parent. If a parent is not found then LEFT is used.</LI>
     * <LI>{@link PositionConstants#LEFT} - Alignment is with leading edge</LI>
     * <LI>{@link PositionConstants#RIGHT} - Alignment is with trailing edge</LI>
     * <LI>{@link PositionConstants#CENTER}</LI>
     * <LI>{@link PositionConstants#ALWAYS_LEFT} - Left, irrespective of
     * orientation</LI>
     * <LI>{@link PositionConstants#ALWAYS_RIGHT} - Right, irrespective of
     * orientation</LI>
     * </UL>
     * @param value the aligment
     */
    @objid ("0f194a3d-86e7-492a-9811-d1928c2bf940")
    public void setMainLabelAlignement(int value) {
        this.mainLabelBlock.setHorizontalAligment(value);
    }

    /**
     * Returns the effective horizontal alignment of the main label block.
     * This method will never return {@link PositionConstants#NONE}. If the value is none, it will return the
     * inherited alignment. If no alignment was inherited, it will return the
     * default alignment ({@link PositionConstants#LEFT}).
     * @return the effective alignment
     */
    @objid ("7c2231e4-9c9b-4dd6-8bfb-b6bf1cf05d9f")
    public int getMainLabelAlignement() {
        return this.mainLabelBlock.getHorizontalAligment();
    }

    /**
     * This class implements a right-aligned ToolBarLayout, ie children are stacked on the right side of the toolbar. NOTE: A
     * ToolbarRLayout is always horizontal.
     */
    @objid ("7e7d208c-1dec-11e2-8cad-001ec947c8cc")
    public static class ToolbarRLayout extends ToolbarLayout {
        @objid ("7e7d2091-1dec-11e2-8cad-001ec947c8cc")
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

        /**
         * C'tor.
         */
        @objid ("7e7d2097-1dec-11e2-8cad-001ec947c8cc")
        public ToolbarRLayout() {
            super(true); // force horizontal
        }

    }

    /**
     * Same as {@link org.eclipse.draw2d.MarginBorder} for {@link FlowFigure}.
     */
    @objid ("7e7d2099-1dec-11e2-8cad-001ec947c8cc")
    public static final class MarginFlowBorder extends AbstractFlowBorder {
        @objid ("e1862a70-b4d8-4b7e-9ab1-1fdac53bd43a")
        private Insets inset;

        /**
         * Constructor.
         * @param inset the insets.
         */
        @objid ("7e7d20a1-1dec-11e2-8cad-001ec947c8cc")
        public MarginFlowBorder(final Insets inset) {
            this.inset = inset;
        }

        @objid ("7e7d20a8-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Insets getInsets(final IFigure figure) {
            return this.inset;
        }

    }

    /**
     * Center area layout.
     * <p>
     * Fills all available space, center its content on the available space and tries to have a 2/1 width/height ratio.
     * 
     * @author cmarin
     */
    @objid ("7e7d20b3-1dec-11e2-8cad-001ec947c8cc")
    private static final class CenterAreaLayout extends GridLayout {
        @objid ("33cf8391-d9ba-440f-b4b9-ab88c95ec9eb")
        private Dimension cachedPreferredHint = new Dimension(-1, -1);

        @objid ("7e7f82cd-1dec-11e2-8cad-001ec947c8cc")
        CenterAreaLayout() {
            super(1, true);
        }

        @objid ("7e7f82cf-1dec-11e2-8cad-001ec947c8cc")
        @Override
        protected Dimension calculatePreferredSize(final IFigure cont, final int wHint, final int hHint) {
            Dimension ret = super.calculatePreferredSize(cont, wHint, hHint);
            
            if (ret.height == 0)
                return ret;
            
            if (ret.width / ret.height < 2)
                ret.width = Math.round(ret.height * 2);
            
            if (ret.width / ret.height > 8) {
                int r = 2 * (ret.width / ret.height) / 8;
                ret = super.calculatePreferredSize(cont, ret.width / r, hHint);
            }
            return ret;
        }

    }

}
