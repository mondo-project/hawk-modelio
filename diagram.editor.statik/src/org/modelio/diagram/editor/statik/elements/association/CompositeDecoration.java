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
                                    

package org.modelio.diagram.editor.statik.elements.association;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.figures.LinkFigure;
import org.modelio.diagram.elements.core.figures.geometry.Direction;
import org.modelio.diagram.elements.core.figures.geometry.GeomUtils;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;

/**
 * Decoration composed of the qualifier group and the arrow decoration.
 * 
 * @author cmarin
 */
@objid ("33e2c7df-55b7-11e2-877f-002564c97630")
public class CompositeDecoration extends Figure implements IPenOptionsSupport, RotatableDecoration {
    /**
     * Constant to set the container.
     */
    @objid ("33e44e41-55b7-11e2-877f-002564c97630")
    public static final Integer CONTAINER = 1;

    /**
     * Constant to set the decoration.
     */
    @objid ("33e44e45-55b7-11e2-877f-002564c97630")
    public static final Integer DECO = 2;

    @objid ("5a50b8e8-5bd5-11e2-9e33-00137282c51b")
    protected IFigure qualifierGroup;

    @objid ("5a557d90-5bd5-11e2-9e33-00137282c51b")
    protected RotatableDecoration decoration;

    /**
     * Location set by {@link #setLocation(Point)}
     */
    @objid ("9f8e4d9b-97e5-40ff-b070-39d4c4f37f19")
    private Point location = new Point();

    /**
     * Direction the arrow should be pointing FROM.
     */
    @objid ("a2d8c566-b29b-4078-85e7-cc16261721ad")
    private Dimension rotationDirection = new Dimension();

    /**
     * Initialize the figure and its layout manager.
     */
    @objid ("33e44e49-55b7-11e2-877f-002564c97630")
    public CompositeDecoration() {
        setLayoutManager(new CompositeDecorationLayout());
    }

    @objid ("33e44e4c-55b7-11e2-877f-002564c97630")
    @Override
    public void setReferencePoint(final Point p) {
        Dimension newDir = p.getDifference(this.location);
        if (!newDir.equals(this.rotationDirection)) {
            this.rotationDirection = newDir;
            invalidate();
        }
    }

    @objid ("33e44e51-55b7-11e2-877f-002564c97630")
    @Override
    public void setLocation(final Point p) {
        if (!p.equals(this.location)) {
            this.location.setLocation(p);
            invalidate();
        }
    }

    /**
     * Get the location given to the last call to {@link #setLocation(Point)}.
     * @return this decoration location.
     */
    @objid ("33e44e56-55b7-11e2-877f-002564c97630")
    public Point getDecorationLocation() {
        return this.location;
    }

    /**
     * Set the qualifier group
     * @param qualifierGroup the qualifier group
     */
    @objid ("33e44e5b-55b7-11e2-877f-002564c97630")
    public void setQualifier(final IFigure qualifierGroup) {
        if (qualifierGroup == this.qualifierGroup)
            return;
        if (this.qualifierGroup != null)
            remove(this.qualifierGroup);
        
        this.qualifierGroup = qualifierGroup;
        
        if (qualifierGroup != null) {
            add(qualifierGroup, CompositeDecoration.CONTAINER, -1);
        }
    }

    /**
     * Set the arrow decoration.
     * @param deco the arrow decoration.
     */
    @objid ("33e44e60-55b7-11e2-877f-002564c97630")
    public void setDecoration(final RotatableDecoration deco) {
        if (this.decoration == deco)
            return;
        if (this.decoration != null)
            remove(this.decoration);
        
        this.decoration = deco;
        
        if (deco != null)
            add(deco, CompositeDecoration.DECO, -1);
    }

    @objid ("33e44e65-55b7-11e2-877f-002564c97630")
    @Override
    public Rectangle getBounds() {
        Rectangle ret = new Rectangle(this.location, this.location);
        for (Object o : getChildren()) {
            IFigure c = (IFigure) o;
            ret.union(c.getBounds());
        }
        return ret;
    }

    /**
     * Direction the arrow should be pointing FROM.
     * @return Direction the arrow should be pointing FROM.
     */
    @objid ("33e44e6a-55b7-11e2-877f-002564c97630")
    public Dimension getReferenceDirection() {
        return this.rotationDirection;
    }

    @objid ("33e44e6f-55b7-11e2-877f-002564c97630")
    @Override
    public Color getLineColor() {
        if (this.decoration instanceof IPenOptionsSupport)
            return ((IPenOptionsSupport) this.decoration).getLineColor();
        else
            return null;
    }

    @objid ("33e44e73-55b7-11e2-877f-002564c97630")
    @Override
    public LinePattern getLinePattern() {
        if (this.decoration instanceof IPenOptionsSupport)
            return ((IPenOptionsSupport) this.decoration).getLinePattern();
        else
            return null;
    }

    @objid ("33e5d4d9-55b7-11e2-877f-002564c97630")
    @Override
    public int getLineWidth() {
        if (this.decoration instanceof IPenOptionsSupport)
            return ((IPenOptionsSupport) this.decoration).getLineWidth();
        else
            return 0;
    }

    @objid ("33e5d4dd-55b7-11e2-877f-002564c97630")
    @Override
    public Color getTextColor() {
        if (this.decoration instanceof IPenOptionsSupport)
            return ((IPenOptionsSupport) this.decoration).getTextColor();
        else
            return null;
    }

    @objid ("33e5d4e1-55b7-11e2-877f-002564c97630")
    @Override
    public Font getTextFont() {
        if (this.decoration instanceof IPenOptionsSupport)
            return ((IPenOptionsSupport) this.decoration).getTextFont();
        else
            return null;
    }

    @objid ("33e5d4e5-55b7-11e2-877f-002564c97630")
    @Override
    public void setLineColor(final Color lineColor) {
        if (this.decoration instanceof IPenOptionsSupport)
            ((IPenOptionsSupport) this.decoration).setLineColor(lineColor);
    }

    @objid ("33e5d4ea-55b7-11e2-877f-002564c97630")
    @Override
    public void setLinePattern(final LinePattern lineStyle) {
        if (this.decoration instanceof IPenOptionsSupport)
            ((IPenOptionsSupport) this.decoration).setLinePattern(lineStyle);
    }

    @objid ("33e5d4f1-55b7-11e2-877f-002564c97630")
    @Override
    public void setLineWidth(final int lineWidth) {
        if (this.decoration instanceof IPenOptionsSupport)
            ((IPenOptionsSupport) this.decoration).setLineWidth(lineWidth);
    }

    @objid ("33e5d4f6-55b7-11e2-877f-002564c97630")
    @Override
    public void setTextColor(final Color textColor) {
        if (this.decoration instanceof IPenOptionsSupport)
            ((IPenOptionsSupport) this.decoration).setTextColor(textColor);
    }

    @objid ("33e5d4fb-55b7-11e2-877f-002564c97630")
    @Override
    public void setTextFont(final Font textFont) {
        if (this.decoration instanceof IPenOptionsSupport)
            ((IPenOptionsSupport) this.decoration).setTextFont(textFont);
    }

    @objid ("33e5d500-55b7-11e2-877f-002564c97630")
    @Override
    public Color getBackgroundColor() {
        if (this.decoration != null)
            return this.decoration.getBackgroundColor();
        else
            return super.getBackgroundColor();
    }

    @objid ("33e5d504-55b7-11e2-877f-002564c97630")
    @Override
    public void setBackgroundColor(final Color bg) {
        if (this.decoration != null)
            this.decoration.setBackgroundColor(bg);
        else
            super.setBackgroundColor(bg);
    }

    /**
     * Get the decoration part of the composite decoration.
     * @return the decoration part.
     */
    @objid ("33e5d509-55b7-11e2-877f-002564c97630")
    public RotatableDecoration getDecoration() {
        return this.decoration;
    }

    @objid ("33e5d50e-55b7-11e2-877f-002564c97630")
    private class CompositeDecorationLayout extends AbstractLayout {
        @objid ("33e5d50f-55b7-11e2-877f-002564c97630")
        public CompositeDecorationLayout() {
            super();
        }

        @objid ("33e5d511-55b7-11e2-877f-002564c97630")
        @SuppressWarnings("unqualified-field-access")
        @Override
        public Object getConstraint(final IFigure child) {
            if (child == qualifierGroup)
                return CONTAINER;
            else if (child == decoration)
                return DECO;
            else
                return null;
        }

        @objid ("33e75b79-55b7-11e2-877f-002564c97630")
        @Override
        protected Dimension calculatePreferredSize(final IFigure container, final int wHint, final int hHint) {
            Dimension ret = new Dimension();
            for (Object o : getChildren()) {
                IFigure c = (IFigure) o;
                ret.union(c.getMinimumSize());
            }
            
            ret.union(getBorderPreferredSize(container));
            return ret;
        }

        @objid ("33e75b84-55b7-11e2-877f-002564c97630")
        @SuppressWarnings("unqualified-field-access")
        @Override
        public void layout(final IFigure container) {
            if (qualifierGroup == null)
                return;
            
            // Get side of the 'location' relative to the node
            IFigure node = getParentConnectionExtremity();
            Direction side;
            if (node == null) {
                Rectangle r = new Rectangle(getDecorationLocation().getTranslated(-1, -1),
                                            new Dimension(3, 3));
                side = GeomUtils.getDirection(getDecorationLocation().getTranslated(getReferenceDirection()),
                                              r);
            } else {
                Rectangle nodeBounds = getRelativeBounds(node);
                side = GeomUtils.getDirection(getDecorationLocation(), nodeBounds);
            }
            
            // Put the qualifier on the side
            Rectangle qualifierBounds = calcQualifierBounds(side);
            qualifierGroup.setBounds(qualifierBounds);
            
            // Move the decoration behind
            moveDecoration(qualifierBounds, side);
        }

        /**
         * Get the given node bounds in the container figure coordinates.
         * @param node a figure
         * @return the figure bounds in the container coordinates.
         */
        @objid ("33e75b89-55b7-11e2-877f-002564c97630")
        private Rectangle getRelativeBounds(final IFigure node) {
            if (node == null) {
                Rectangle nodeBounds = new Rectangle(CompositeDecoration.this.location.x, CompositeDecoration.this.location.y, 0, 0);
                return nodeBounds;
            } else {
                Rectangle nodeBounds = node.getBounds().getCopy();
                node.translateToAbsolute(nodeBounds);
                translateToRelative(nodeBounds);
                return nodeBounds;
            }
        }

        /**
         * Get the connection extremity this decoration is sticked on.
         * @return a node figure.
         */
        @objid ("33e75b8f-55b7-11e2-877f-002564c97630")
        private IFigure getParentConnectionExtremity() {
            final LinkFigure conn = getParentConnection();
            if (conn.getSourceDecoration() == CompositeDecoration.this) {
                return conn.getSourceAnchor().getOwner();
            } else {
                return conn.getTargetAnchor().getOwner();
            }
        }

        @objid ("33e75b93-55b7-11e2-877f-002564c97630")
        private LinkFigure getParentConnection() {
            return (LinkFigure) getParent();
        }

        /**
         * Place the decoration
         * @param r the qualifier bounds
         * @param side the side where to place the decoration
         */
        @objid ("33e75b99-55b7-11e2-877f-002564c97630")
        private void moveDecoration(final Rectangle r, final Direction side) {
            if (CompositeDecoration.this.decoration != null) {
                Point p = GeomUtils.getLineIntersection(getDecorationLocation(),
                                                        getDecorationLocation().getTranslated(getReferenceDirection().getScaled(10)),
                                                        r);
                if (p == null)
                    p = r.getTopLeft(); //getSideCenter(r, side);
            
                CompositeDecoration.this.decoration.setLocation(p);
                CompositeDecoration.this.decoration.setReferencePoint(p.getTranslated(getReferenceDirection()));
            }
        }

        @objid ("33e75ba2-55b7-11e2-877f-002564c97630")
        private Point getSideCenter(final Rectangle r, final Direction side) {
            switch (side) {
                case EAST:
                    return r.getRight();
                case NORTH:
                    return r.getTop();
                case SOUTH:
                    return r.getBottom();
                case WEST:
                    return r.getLeft();
                default:
                    throw new IllegalArgumentException(side + " is invalid side.");
            }
        }

        @objid ("33e75bab-55b7-11e2-877f-002564c97630")
        private Rectangle calcQualifierBounds(final Direction side) {
            Dimension qualSize = CompositeDecoration.this.qualifierGroup.getMinimumSize();
            Dimension halfSize = qualSize.getScaled(0.5);
            Point topleft = CompositeDecoration.this.getDecorationLocation().getCopy();
            switch (side) {
                case EAST:
                    topleft.translate(-2, -halfSize.height);
                    break;
                case NORTH:
                    topleft.translate(-halfSize.width, -qualSize.height + 2);
                    break;
                case SOUTH:
                    topleft.translate(-halfSize.width, -2);
                    break;
                case WEST:
                    topleft.translate(-qualSize.width + 2, -halfSize.height);
                    break;
                case NONE:
                    throw new IllegalArgumentException(side + " is invalid side.");
            }
            return new Rectangle(topleft, qualSize);
        }

    }

}
