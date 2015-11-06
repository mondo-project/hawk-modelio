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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.modelio.diagram.elements.common.header.HeaderFigure;
import org.modelio.diagram.elements.core.figures.LabelFigure;

/**
 * Vertical version of HeaderFigure: all is rotated 90? counterclockwise.
 */
@objid ("2b0aa3a9-55b6-11e2-877f-002564c97630")
public class PartitionHeaderFigure extends HeaderFigure {
    /**
     * Set the keyword label.
     * @param text the keyword label.
     */
    @objid ("2b0b3fea-55b6-11e2-877f-002564c97630")
    @Override
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
                this.keywordLabel = new VerticalLabel(text);
                this.keywordLabel.setLabelAlignment(PositionConstants.CENTER);
                this.keywordLabel.setTextPlacement(PositionConstants.NORTH);
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
    @objid ("2b0b66f9-55b6-11e2-877f-002564c97630")
    @Override
    public void setTopLabels(List<String> keywordLabels) {
        // remove existing labels
        this.keywordLabels.removeAll();
        
        // add new label figures
        for (String s : keywordLabels) {
            Label labelFigure = new VerticalLabel(s);
            // TRACE: labelFigure.setBorder(new LineBorder(1));
            labelFigure.setLabelAlignment(PositionConstants.CENTER);
            labelFigure.setTextPlacement(PositionConstants.NORTH);
            this.keywordLabels.add(labelFigure);
        }
    }

    /**
     * Set the labels displayed below the main label.
     * @param bottomLabels the bottom labels.
     */
    @objid ("2b0bb519-55b6-11e2-877f-002564c97630")
    @Override
    public void setBottomLabels(List<String> bottomLabels) {
        // remove existing labels
        this.tagsArea.removeAll();
        
        // add new label figures
        for (String s : bottomLabels) {
            Label labelFigure = new VerticalLabel(s);
            labelFigure.setLabelAlignment(PositionConstants.CENTER);
            labelFigure.setTextPlacement(PositionConstants.NORTH);
            this.tagsArea.add(labelFigure);
        }
    }

    @objid ("878e48d2-9f7c-4ede-b671-077e8ef4b6ce")
    @Override
    protected Figure createMIconsArea() {
        Figure leftFigure = new Figure();
        ToolbarLayout tbLayout = new ToolbarLayout(false);
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        tbLayout.setSpacing(1);
        leftFigure.setLayoutManager(tbLayout);
        leftFigure.setOpaque(false);
        leftFigure.setBorder(new MarginBorder(new Insets(1, 0, 1, 0)));
        // TRACE: leftFigure.setBorder(new LineBorder(ColorConstants.red, 1));
        return leftFigure;
    }

    @objid ("47ba1eb8-68a2-4ae9-b04f-d72784711a41")
    @Override
    protected Figure createSIconsAreaFigures() {
        Figure rightFigure = new Figure();
        ToolbarLayout tbLayout = new ToolbarLayout(false);
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        tbLayout.setSpacing(1);
        rightFigure.setLayoutManager(tbLayout);
        rightFigure.setOpaque(false);
        rightFigure.setBorder(new MarginBorder(new Insets(1, 0, 1, 0)));
        // TRACE: rightFigure.setBorder(new LineBorder(ColorConstants.cyan, 1));
        return rightFigure;
    }

    @objid ("e29c2c78-b295-40aa-bcaf-a641f69841c3")
    @Override
    protected LabelFigure createMainArea() {
        LabelFigure label = new VerticalLabel();
        // label.setBorder(new MarginBorder(0, 2, 0, 3));
        // TRACE: label.setBorder(new LineBorder(ColorConstants.yellow, 1));
        return label;
    }

    @objid ("bafeece6-df19-4b93-9dc2-12776725cd1d")
    @Override
    protected Figure createKeywordsArea() {
        Figure top = new Figure();
        ToolbarLayout tbLayout = new ToolbarLayout(true);
        tbLayout.setStretchMinorAxis(true);
        tbLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        top.setLayoutManager(tbLayout);
        
        // first row : Keyword label
        this.keywordLabel = new VerticalLabel("");
        this.keywordLabel.setLabelAlignment(PositionConstants.CENTER);
        this.keywordLabel.setTextPlacement(PositionConstants.NORTH);
        top.add(this.keywordLabel);
        
        // second row: top labels
        this.keywordLabels = new Figure();
        ToolbarLayout topAreaLayout = new ToolbarLayout(true);
        topAreaLayout.setSpacing(0);
        topAreaLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        topAreaLayout.setStretchMinorAxis(true);
        this.keywordLabels.setLayoutManager(topAreaLayout);
        this.keywordLabels.setOpaque(false);
        top.add(this.keywordLabels);
        
        // TRACE: top.setBorder(new LineBorder(ColorConstants.cyan, 1));
        return top;
    }

    @objid ("c6adf0c7-69f2-4141-9a0a-5bbad8d02876")
    @Override
    protected Figure createTagsArea() {
        Figure bottomLabels = new Figure();
        ToolbarLayout bottomAreaLayout = new ToolbarLayout(true);
        bottomAreaLayout.setSpacing(0);
        bottomAreaLayout.setMinorAlignment(OrderedLayout.ALIGN_CENTER);
        bottomAreaLayout.setStretchMinorAxis(true);
        bottomLabels.setLayoutManager(bottomAreaLayout);
        bottomLabels.setOpaque(false);
        // TRACE: bottomLabels.setBorder(new LineBorder(ColorConstants.blue, 1));
        return bottomLabels;
    }

    /**
     * Constructor.
     */
    @objid ("d2264042-0b9d-4df7-b6e1-73512dda7929")
    public PartitionHeaderFigure() {
        // The header figure is a 'BorderLayout' container.
        // Children layout:
        // - TOP : sIconsArea - Figure with tool bar layout (stereotypes icons)
        // - RIGHT : tagsArea - FlowPage ( tagged values)
        // - BOTTOM: mIconsArea - Figure with tool bar layout (metaclass icon)
        // - LEFT : keywordsArea - FlowPage (keyword and stereotypes labels)
        // - CENTER: mainArea (main label)
        // Children are transparent without borders
        Figure container = this;
        container.setLayoutManager(new BorderLayout());
        // TRACE: container.setBorder(new LineBorder(ColorConstants.orange, 2));
        
        // -- BOTTOM Area --
        this.mIconsArea = createMIconsArea();
        container.add(this.mIconsArea, BorderLayout.BOTTOM);
        
        // -- TOP Area --
        this.sIconsArea = createSIconsAreaFigures();
        container.add(this.sIconsArea, BorderLayout.TOP);
        
        // -- LEFT Area --
        this.keywordsArea = createKeywordsArea();
        container.add(this.keywordsArea, BorderLayout.LEFT);
        
        // -- CENTER Area --
        this.mainArea = createMainArea();
        container.add(this.mainArea, BorderLayout.CENTER);
        
        // -- RIGHT area --
        this.tagsArea = createTagsArea();
        container.add(this.tagsArea, BorderLayout.RIGHT);
    }

}
