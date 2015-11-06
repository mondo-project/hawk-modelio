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
                                    

package org.modelio.diagram.editor.activity.elements.partition.header;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbstractBackground;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageUtilities;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.elements.core.figures.LabelFigure;

/**
 * Same as label, only vertical.
 * 
 * @author fpoyer
 */
@objid ("2b0d89d9-55b6-11e2-877f-002564c97630")
public class VerticalLabel extends LabelFigure {
    @objid ("2b0d89de-55b6-11e2-877f-002564c97630")
    private String ELLIPSIS = "..."; // $NON-NLS-1$

    @objid ("2b0db0eb-55b6-11e2-877f-002564c97630")
    private String text = ""; // $NON-NLS-1$

    @objid ("2b0dd7f9-55b6-11e2-877f-002564c97630")
    private String subStringText;

    @objid ("2b0dff09-55b6-11e2-877f-002564c97630")
    private int textAlignment = CENTER;

    @objid ("2b0dff0a-55b6-11e2-877f-002564c97630")
    private int iconAlignment = CENTER;

    @objid ("2b0dff0b-55b6-11e2-877f-002564c97630")
    private int labelAlignment = CENTER;

    @objid ("2b0dff0c-55b6-11e2-877f-002564c97630")
    private int textPlacement = NORTH;

    @objid ("2b0dff0d-55b6-11e2-877f-002564c97630")
    private int iconTextGap = 3;

    @objid ("2b0db0ea-55b6-11e2-877f-002564c97630")
    private Image icon;

    @objid ("2b0dd7fa-55b6-11e2-877f-002564c97630")
    private Dimension textSize;

    @objid ("2b0dd7fb-55b6-11e2-877f-002564c97630")
    private Dimension subStringTextSize;

    @objid ("2b0dd7fc-55b6-11e2-877f-002564c97630")
    private Dimension iconSize = new Dimension(0, 0);

    @objid ("2b0dd7fd-55b6-11e2-877f-002564c97630")
    private Point iconLocation;

    @objid ("2b0dd7fe-55b6-11e2-877f-002564c97630")
    private Point textLocation;

    /**
     * Construct an empty VerticalLabel.
     */
    @objid ("2b0e2619-55b6-11e2-877f-002564c97630")
    public VerticalLabel() {
        // Nothing to do.
    }

    /**
     * Construct a VerticalLabel with passed String as its text.
     * @param s the label text
     */
    @objid ("2b0e261c-55b6-11e2-877f-002564c97630")
    public VerticalLabel(String s) {
        setText(s);
    }

    /**
     * Construct a VerticalLabel with passed Image as its icon.
     * @param i the label image
     */
    @objid ("2b0e7439-55b6-11e2-877f-002564c97630")
    public VerticalLabel(Image i) {
        setIcon(i);
    }

    /**
     * Construct a VerticalLabel with passed String as text and passed Image as its icon.
     * @param s the label text
     * @param i the label image
     */
    @objid ("2b0e9b49-55b6-11e2-877f-002564c97630")
    public VerticalLabel(String s, Image i) {
        setText(s);
        setIcon(i);
    }

    @objid ("2b0ec259-55b6-11e2-877f-002564c97630")
    private void alignOnWidth(Point loc, Dimension size, int alignment) {
        // used a rotated text size
        Insets insets = getInsets();
        switch (alignment) {
        case LEFT:
            loc.x = insets.left;
            break;
        case RIGHT:
            loc.x = this.bounds.width - size.width - insets.right;
            break;
        default:
            loc.x = (this.bounds.width - size.width) / 2;
        }
    }

    @objid ("2b0ec25e-55b6-11e2-877f-002564c97630")
    private void alignOnHeight(Point loc, Dimension size, int alignment) {
        // used a rotated text size
        Dimension rSize = new Dimension(size.height, size.width);
        Insets insets = getInsets();
        
        switch (alignment) {
        case TOP:
            loc.y = insets.top;
            break;
        case BOTTOM:
            loc.y = this.bounds.height - rSize.height - insets.bottom;
            break;
        default:
            loc.y = (this.bounds.height - rSize.height) / 2;
        }
    }

    @objid ("2b0f107c-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("incomplete-switch")
    private void calculateAlignment() {
        switch (this.textPlacement) {
        case EAST:
        case WEST:
            alignOnHeight(this.textLocation, getTextSize(), this.textAlignment);
            alignOnHeight(this.iconLocation, getIconSize(), this.iconAlignment);
            break;
        case NORTH:
        case SOUTH:
            alignOnWidth(this.textLocation, getSubStringTextSize(), this.textAlignment);
            alignOnWidth(this.iconLocation, getIconSize(), this.iconAlignment);
            break;
        }
    }

    /**
     * Calculates the size of the Label using the passed Dimension as the size of the Label's text.
     * @param txtSize the precalculated size of the label's text
     * @return the label's size
     */
    @objid ("2b0f3789-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension calculateLabelSize(Dimension txtSize) {
        int gap = getIconTextGap();
        if (getIcon() == null || getText().equals("")) //$NON-NLS-1$
            gap = 0;
        Dimension d = new Dimension(0, 0);
        if (this.textPlacement == SOUTH || this.textPlacement == NORTH) {
            d.height = getIconSize().height + gap + txtSize.height;
            d.width = Math.max(getIconSize().width, txtSize.width);
        } else {
            d.height = Math.max(getIconSize().height, txtSize.height);
            d.width = getIconSize().width + gap + txtSize.width;
        }
        return d;
    }

    @objid ("2b0f5e9a-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("incomplete-switch")
    private void calculateLocations() {
        this.textLocation = new Point();
        this.iconLocation = new Point();
        
        calculatePlacement();
        calculateAlignment();
        Dimension offset = getSize().getShrinked(getPreferredSize());
        offset.height += getTextSize().height - getSubStringTextSize().height;
        switch (this.labelAlignment) {
        case CENTER:
            offset.scale(0.5f);
            break;
        case BOTTOM:
            offset.scale(0.0f);
            break;
        case TOP:
            offset.scale(1.0f);
            break;
        case LEFT:
            offset.width = 0;
            offset.scale(0.5f);
            break;
        case RIGHT:
            offset.width = offset.width * 2;
            offset.scale(0.5f);
            break;
        default:
            offset.scale(0.5f);
            break;
        }
        
        switch (this.textPlacement) {
        case NORTH:
        case SOUTH:
            offset.width = 0;
            break;
        case WEST:
        case EAST:
            offset.height = 0;
            break;
        }
        
        this.textLocation.translate(offset);
        this.iconLocation.translate(offset);
    }

    @objid ("2b0f5e9c-55b6-11e2-877f-002564c97630")
    @SuppressWarnings("incomplete-switch")
    private void calculatePlacement() {
        int gap = getIconTextGap();
        if (this.icon == null || this.text.equals("")) //$NON-NLS-1$
            gap = 0;
        Insets insets = getInsets();
        
        switch (this.textPlacement) {
        case EAST:
            this.iconLocation.x = insets.left;
            this.textLocation.x = getIconSize().width + gap + insets.left;
            break;
        case WEST:
            this.textLocation.x = insets.left;
            this.iconLocation.x = getSubStringTextSize().width + gap + insets.left;
            break;
        case NORTH:
            this.textLocation.y = insets.top;
            this.iconLocation.y = getTextSize().height + gap + insets.top;
            break;
        case SOUTH:
            this.textLocation.y = getIconSize().height + gap + insets.top;
            this.iconLocation.y = insets.top;
        }
    }

    /**
     * Calculates the size of the Label's text size. The text size calculated takes into consideration if the Label's text is
     * currently truncated. If text size without considering current truncation is desired, use {@link #calculateTextSize()}.
     * @return the size of the label's text, taking into account truncation
     * @since 2.0
     */
    @objid ("2b0f5e9e-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension calculateSubStringTextSize() {
        return getTextUtilities().getTextExtents(getSubStringText(), getFont()).getTransposed();
    }

    /**
     * Calculates and returns the size of the Label's text. Note that this Dimension is calculated using the Label's full text,
     * regardless of whether or not its text is currently truncated. If text size considering current truncation is desired, use
     * {@link #calculateSubStringTextSize()}.
     * @return the size of the label's text, ignoring truncation
     * @since 2.0
     */
    @objid ("2b0facb9-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension calculateTextSize() {
        return getTextUtilities().getTextExtents(getText(), getFont()).getTransposed();
    }

    @objid ("2b0fd3c9-55b6-11e2-877f-002564c97630")
    private void clearLocations() {
        this.iconLocation = this.textLocation = null;
    }

    /**
     * Returns the Label's icon.
     * @return the label icon
     * @since 2.0
     */
    @objid ("2b0fd3cb-55b6-11e2-877f-002564c97630")
    @Override
    public Image getIcon() {
        return this.icon;
    }

    /**
     * Returns the current alignment of the Label's icon. The default is {@link PositionConstants#CENTER}.
     * @return the icon alignment
     * @since 2.0
     */
    @objid ("2b0ffadc-55b6-11e2-877f-002564c97630")
    @Override
    public int getIconAlignment() {
        return this.iconAlignment;
    }

    /**
     * Returns the bounds of the Label's icon.
     * @return the icon's bounds
     * @since 2.0
     */
    @objid ("2b1021ed-55b6-11e2-877f-002564c97630")
    @Override
    public Rectangle getIconBounds() {
        Rectangle currentBounds = getBounds();
        return new Rectangle(currentBounds.getLocation().translate(getIconLocation()), getIconSize());
    }

    /**
     * Returns the location of the Label's icon relative to the Label.
     * @return the icon's location
     * @since 2.0
     */
    @objid ("2b107009-55b6-11e2-877f-002564c97630")
    @Override
    protected Point getIconLocation() {
        if (this.iconLocation == null)
            calculateLocations();
        return this.iconLocation;
    }

    /**
     * Returns the gap in pixels between the Label's icon and its text.
     * @return the gap
     * @since 2.0
     */
    @objid ("2b109719-55b6-11e2-877f-002564c97630")
    @Override
    public int getIconTextGap() {
        return this.iconTextGap;
    }

    /**
     * @see IFigure#getMinimumSize(int, int)
     */
    @objid ("2b10be29-55b6-11e2-877f-002564c97630")
    @Override
    public Dimension getMinimumSize(int w, int h) {
        if (this.minSize != null)
            return this.minSize;
        this.minSize = new Dimension();
        if (getLayoutManager() != null)
            this.minSize.setSize(getLayoutManager().getMinimumSize(this, w, h));
        
        Dimension labelSize = calculateLabelSize(getTextUtilities().getTextExtents(getTruncationString(), getFont())
                .getTransposed().intersect(getTextUtilities().getTextExtents(getText(), getFont()).getTransposed()));
        Insets insets = getInsets();
        labelSize.expand(insets.getHeight(), insets.getWidth());
        this.minSize.union(labelSize);
        return this.minSize;
    }

    /**
     * @see IFigure#getPreferredSize(int, int)
     */
    @objid ("2b10e53b-55b6-11e2-877f-002564c97630")
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        if (this.prefSize == null) {
            this.prefSize = calculateLabelSize(getTextSize());
            Insets insets = getInsets();
            this.prefSize.expand(insets.getHeight(), insets.getWidth());
            if (getLayoutManager() != null)
                this.prefSize.union(getLayoutManager().getPreferredSize(this, wHint, hHint));
        }
        if (hHint >= 0 && hHint < this.prefSize.height) {
            Dimension minimumSize = getMinimumSize(wHint, hHint);
            Dimension result = this.prefSize.getCopy();
            result.height = Math.min(result.height, hHint);
            result.height = Math.max(minimumSize.height, result.height);
            return result;
        }
        return this.prefSize;
    }

    /**
     * Calculates the amount of the Label's current text will fit in the Label, including an elipsis "..." if truncation is
     * required.
     * @return the substring
     * @since 2.0
     */
    @objid ("2b113359-55b6-11e2-877f-002564c97630")
    @Override
    public String getSubStringText() {
        if (this.subStringText != null)
            return this.subStringText;
        
        this.subStringText = this.text;
        int heightShrink = getPreferredSize().height - getSize().height;
        if (heightShrink <= 0)
            return this.subStringText;
        
        Dimension effectiveSize = getTextSize().getExpanded(0, -heightShrink);
        Font currentFont = getFont();
        int dotsheight = getTextUtilities().getTextExtents(getTruncationString(), currentFont).getTransposed().height;
        
        if (effectiveSize.height < dotsheight)
            effectiveSize.height = dotsheight;
        
        int subStringLength = getTextUtilities().getLargestSubstringConfinedTo(this.text, currentFont,
                effectiveSize.height - dotsheight);
        this.subStringText = new String(this.text.substring(0, subStringLength) + getTruncationString());
        return this.subStringText;
    }

    /**
     * Returns the size of the Label's current text. If the text is currently truncated, the truncated text with its ellipsis is
     * used to calculate the size.
     * @return the size of this label's text, taking into account truncation
     * @since 2.0
     */
    @objid ("2b115a69-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension getSubStringTextSize() {
        if (this.subStringTextSize == null)
            this.subStringTextSize = calculateSubStringTextSize();
        return this.subStringTextSize;
    }

    /**
     * Returns the text of the label. Note that this is the complete text of the label, regardless of whether it is currently being
     * truncated. Call {@link #getSubStringText()} to return the label's current text contents with truncation considered.
     * @return the complete text of this label
     * @since 2.0
     */
    @objid ("2b11817a-55b6-11e2-877f-002564c97630")
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Returns the current alignment of the Label's text. The default text alignment is {@link PositionConstants#CENTER}.
     * @return the text alignment
     */
    @objid ("2b11a88c-55b6-11e2-877f-002564c97630")
    @Override
    public int getTextAlignment() {
        return this.textAlignment;
    }

    /**
     * Returns the bounds of the label's text. Note that the bounds are calculated using the label's complete text regardless of
     * whether the label's text is currently truncated.
     * @return the bounds of this label's complete text
     * @since 2.0
     */
    @objid ("2b11cf9d-55b6-11e2-877f-002564c97630")
    @Override
    public Rectangle getTextBounds() {
        Rectangle currentBounds = getBounds();
        return new Rectangle(currentBounds.getLocation().translate(getTextLocation()), this.textSize);
    }

    /**
     * Returns the location of the label's text relative to the label.
     * @return the text location
     * @since 2.0
     */
    @objid ("2b121db9-55b6-11e2-877f-002564c97630")
    @Override
    protected Point getTextLocation() {
        if (this.textLocation != null)
            return this.textLocation;
        calculateLocations();
        return this.textLocation;
    }

    /**
     * Returns the current placement of the label's text relative to its icon. The default text placement is
     * {@link PositionConstants#EAST}.
     * @return the text placement
     * @since 2.0
     */
    @objid ("2b1244ca-55b6-11e2-877f-002564c97630")
    @Override
    public int getTextPlacement() {
        return this.textPlacement;
    }

    /**
     * Returns the size of the label's complete text. Note that the text used to make this calculation is the label's full text,
     * regardless of whether the label's text is currently being truncated and is displaying an ellipsis. If the size considering
     * current truncation is desired, call {@link #getSubStringTextSize()}.
     * @return the size of this label's complete text
     * @since 2.0
     */
    @objid ("2b126bdd-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension getTextSize() {
        if (this.textSize == null)
            this.textSize = calculateTextSize();
        return this.textSize;
    }

    /**
     * @see IFigure#invalidate()
     */
    @objid ("2b12b9f9-55b6-11e2-877f-002564c97630")
    @Override
    public void invalidate() {
        this.prefSize = null;
        this.minSize = null;
        clearLocations();
        this.textSize = null;
        this.subStringTextSize = null;
        this.subStringText = null;
        super.invalidate();
    }

    /**
     * Returns <code>true</code> if the label's text is currently truncated and is displaying an ellipsis, <code>false</code>
     * otherwise.
     * @return <code>true</code> if the label's text is truncated
     * @since 2.0
     */
    @objid ("2b12e10a-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isTextTruncated() {
        return !getSubStringText().equals(getText());
    }

    /**
     * @see Figure#paintFigure(Graphics)
     */
    @objid ("2b13081c-55b6-11e2-877f-002564c97630")
    @Override
    protected void paintFigure(Graphics graphics) {
        // if (true) {
        if (isOpaque()) {
            graphics.fillRectangle(getBounds());
            if (getBorder() instanceof AbstractBackground)
                ((AbstractBackground) getBorder()).paintBackground(this, graphics, NO_INSETS);
        }
        Image rotatedTextImage = createRotatedImageOfString(graphics.getAbsoluteScale(), getText(), getFont(),
                getForegroundColor(), getBackgroundColor());
        
        graphics.pushState();
        
        double scale = graphics.getAbsoluteScale();
        Point origin = getBounds().getTopLeft();
        graphics.translate(origin.x, origin.y);
        
        final Point textLoc = getTextLocation();
        
        // Draw the text image taking care of the scale
        if (rotatedTextImage != null) {
            Rectangle srcRect = new Rectangle(rotatedTextImage.getBounds());
            Rectangle destRect = new Rectangle(textLoc.x, textLoc.y, (int) (srcRect.width / scale), (int) (srcRect.height / scale));
            graphics.drawImage(rotatedTextImage, srcRect, destRect);
        
            rotatedTextImage.dispose();
        }
        
        // Draw the icon
        if (this.icon != null)
            graphics.drawImage(this.icon, getIconLocation());
        
        
        // restore graphics
        graphics.popState();
    }

    /**
     * Sets the label's icon to the passed image.
     * @param image the new label image
     * @since 2.0
     */
    @objid ("2b132f2c-55b6-11e2-877f-002564c97630")
    @Override
    public void setIcon(Image image) {
        if (this.icon == image)
            return;
        this.icon = image;
        // Call repaint, in case the image dimensions are the same.
        repaint();
        if (this.icon == null)
            setIconDimension(new Dimension());
        else
            setIconDimension(new Dimension(image));
    }

    /**
     * This method sets the alignment of the icon within the bounds of the label. If the label is larger than the icon, then the
     * icon will be aligned according to this alignment. Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * @param align the icon alignment
     * @since 2.0
     */
    @objid ("2b13563c-55b6-11e2-877f-002564c97630")
    @Override
    public void setIconAlignment(int align) {
        if (this.iconAlignment == align)
            return;
        this.iconAlignment = align;
        clearLocations();
        repaint();
    }

    /**
     * Sets the label's icon size to the passed Dimension.
     * @param d the new icon size
     * @deprecated the icon is automatically displayed at 1:1
     * @since 2.0
     */
    @objid ("2b137d4c-55b6-11e2-877f-002564c97630")
    @Deprecated
    @Override
    public void setIconDimension(Dimension d) {
        if (d.equals(getIconSize()))
            return;
        this.iconSize = d;
        revalidate();
    }

    /**
     * Sets the gap in pixels between the label's icon and text to the passed value. The default is 4.
     * @param gap the gap
     * @since 2.0
     */
    @objid ("2b13cb69-55b6-11e2-877f-002564c97630")
    @Override
    public void setIconTextGap(int gap) {
        if (this.iconTextGap == gap)
            return;
        this.iconTextGap = gap;
        repaint();
        revalidate();
    }

    /**
     * Sets the alignment of the label (icon and text) within the figure. If this figure's bounds are larger than the size needed to
     * display the label, the label will be aligned accordingly. Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * @param align label alignment
     */
    @objid ("2b13f279-55b6-11e2-877f-002564c97630")
    @Override
    public void setLabelAlignment(int align) {
        if (this.labelAlignment == align)
            return;
        this.labelAlignment = align;
        clearLocations();
        repaint();
    }

    /**
     * Sets the label's text.
     * @param s the new label text
     * @since 2.0
     */
    @objid ("2b144099-55b6-11e2-877f-002564c97630")
    @Override
    public void setText(String s) {
        String string = s;
        // "text" will never be null.
        if (string == null)
            string = "";//$NON-NLS-1$
        if (this.text.equals(string))
            return;
        this.text = string;
        revalidate();
        repaint();
    }

    /**
     * Sets the alignment of the text relative to the icon within the label. The text alignment must be orthogonal to the text
     * placement. For example, if the placement is EAST, then the text can be aligned using TOP, CENTER, or BOTTOM. Valid values
     * are:
     * <UL>
     * <LI><EM>{@link PositionConstants#CENTER}</EM>
     * <LI>{@link PositionConstants#TOP}
     * <LI>{@link PositionConstants#BOTTOM}
     * <LI>{@link PositionConstants#LEFT}
     * <LI>{@link PositionConstants#RIGHT}
     * </UL>
     * @see #setLabelAlignment(int)
     * @param align the text alignment
     * @since 2.0
     */
    @objid ("2b1467a9-55b6-11e2-877f-002564c97630")
    @Override
    public void setTextAlignment(int align) {
        if (this.textAlignment == align)
            return;
        this.textAlignment = align;
        clearLocations();
        repaint();
    }

    /**
     * Sets the placement of the text relative to the icon within the label. Valid values are:
     * <UL>
     * <LI><EM>{@link PositionConstants#EAST}</EM>
     * <LI>{@link PositionConstants#NORTH}
     * <LI>{@link PositionConstants#SOUTH}
     * <LI>{@link PositionConstants#WEST}
     * </UL>
     * @param where the text placement
     * @since 2.0
     */
    @objid ("2b148eb9-55b6-11e2-877f-002564c97630")
    @Override
    public void setTextPlacement(int where) {
        if (this.textPlacement == where)
            return;
        this.textPlacement = where;
        revalidate();
        repaint();
    }

    /**
     * Gets the <code>TextUtilities</code> instance to be used in measurement calculations.
     * @return a <code>TextUtilities</code> instance
     * @since 3.4
     */
    @objid ("2b14b5c9-55b6-11e2-877f-002564c97630")
    @Override
    public TextUtilities getTextUtilities() {
        return TextUtilities.INSTANCE;
    }

    /**
     * Gets the string that will be appended to the text when the label is truncated. By default, this returns an ellipsis.
     * @return the string to append to the text when truncated
     * @since 3.4
     */
    @objid ("2b14dcda-55b6-11e2-877f-002564c97630")
    @Override
    protected String getTruncationString() {
        return this.ELLIPSIS;
    }

    /**
     * Gets the icon size
     * @return the icon size
     * @since 3.4
     */
    @objid ("2b1503eb-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension getIconSize() {
        return this.iconSize;
    }

    /**
     * Returns the alignment of the entire label (icon and text). The default label alignment is {@link PositionConstants#CENTER}.
     * @return the label alignment
     * @since 3.5
     */
    @objid ("2b152afd-55b6-11e2-877f-002564c97630")
    @Override
    public int getLabelAlignment() {
        return this.labelAlignment;
    }

    /**
     * Do not use the ImageUtilities version, as it has issues with zoom.
     * @param scale the double scale to use
     * @param string the String to be rendered
     * @param font the font
     * @param foreground the text's color
     * @param background the background color
     * @return an Image which must be disposed
     */
    @objid ("2b157919-55b6-11e2-877f-002564c97630")
    public static Image createRotatedImageOfString(double scale, String string, Font font, Color foreground, Color background) {
        Display display = Display.getCurrent();
        if (display == null) {
            SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
        }
        
        FontData[] fontDatas = font.getFontData();
        // Take all font datas, mac and linux specific
        for (FontData data : fontDatas) {
            data.setHeight((int) (data.getHeight() * scale));
        }
        
        // Create the new font
        Font zoomedFont = CoreFontRegistry.getFont(fontDatas);
        // Get the dimension in this font
        Dimension strDim = FigureUtilities.getTextExtents(string, zoomedFont);
        if (strDim.width == 0 || strDim.height == 0) {
            strDim = FigureUtilities.getTextExtents(string, font);
        }
        
        if (strDim.width > 0) {
            // Create an empty image.
            Image srcImage = new Image(display, strDim.width, strDim.height);
        
            // Draw the text in the image
            GC gc = new GC(srcImage);
            gc.setFont(zoomedFont);
        
            gc.setForeground(foreground);
            gc.setBackground(background);
            gc.drawText(string, gc.getClipping().x, gc.getClipping().y);
            gc.dispose();
        
            // Rotate the image
            Image rotated = ImageUtilities.createRotatedImage(srcImage);
            srcImage.dispose();
        
            // Get the image data of the rotated image
            ImageData data = rotated.getImageData();
            rotated.dispose();
            // Set the background color as transparent color
            int whitePixel = data.palette.getPixel(background.getRGB());
            data.transparentPixel = whitePixel;
        
            // Return the rotated image with transparency
            return new Image(display, data);
        } else {
            return null;
        }
    }

}
