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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlane.header;

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
import org.modelio.diagram.editor.activity.elements.partition.header.VerticalLabel;
import org.modelio.diagram.elements.common.header.HeaderFigure;
import org.modelio.diagram.elements.core.figures.LabelFigure;

/**
 * Vertical version of HeaderFigure: all is rotated 90? counterclockwise.
 */
@objid ("4e40475d-4d86-40d6-959f-99e2f66bfb63")
public class BpmnLaneHeaderFigure extends HeaderFigure {
    /**
     * Constructor.
     */
    @objid ("94fca7ae-5424-4647-b919-0f7690345407")
    public BpmnLaneHeaderFigure() {
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

    @objid ("285eedd2-4623-4999-bcf2-972b44f2b8cf")
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

    @objid ("fa4498f7-f126-421d-ae5b-e57073ec0ad6")
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

    @objid ("27b94ebe-3393-4f09-abe9-69a9405a7cd9")
    @Override
    protected LabelFigure createMainArea() {
        LabelFigure label = new VerticalLabel();
        // label.setBorder(new MarginBorder(0, 2, 0, 3));
        // TRACE: label.setBorder(new LineBorder(ColorConstants.yellow, 1));
        return label;
    }

    @objid ("6039ec7f-6d42-445f-bed2-7e2651fd277a")
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

    @objid ("88c0dc4a-c128-4db1-868f-50df924eda6e")
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
     * Set the labels displayed below the main label.
     * @param bottomLabels the bottom labels.
     */
    @objid ("1bb22f73-add5-41d4-b44d-ce1e06f400e5")
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

    /**
     * Set the keyword label.
     * @param text the keyword label.
     */
    @objid ("d89ade1a-e56b-42b1-8ec0-076dbc35cbd7")
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
    @objid ("8b980411-91aa-4582-8eb4-53402a393f2e")
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

}
