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
                                    

package org.modelio.diagram.elements.core.link.ortho;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.core.link.GmPath;
import org.modelio.diagram.elements.core.model.IGmLinkObject;
import org.modelio.diagram.elements.core.model.IGmPath;

/**
 * A command that translates all bendpoints of a connection.
 */
@objid ("80421f55-1dec-11e2-8cad-001ec947c8cc")
public class TranslateBendpointsCommand extends Command {
    @objid ("80421f59-1dec-11e2-8cad-001ec947c8cc")
    private IGmPath path;

    @objid ("80421f5f-1dec-11e2-8cad-001ec947c8cc")
    private IGmLinkObject model;

    @objid ("65c78bd8-1e83-11e2-8cad-001ec947c8cc")
    private List<Bendpoint> routingConstraint;

    @objid ("80421f60-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    public TranslateBendpointsCommand(final IGmPath path, final ConnectionEditPart connectionEP) {
        this.model = (IGmLinkObject) connectionEP.getModel();
        this.path = new GmPath(path);
        this.routingConstraint = (List<Bendpoint>) ((Connection) connectionEP.getFigure()).getRoutingConstraint();
    }

    @objid ("80421f69-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        List<Point> points = new ArrayList<>(this.routingConstraint.size());
        for (Bendpoint bendpoint : this.routingConstraint) {
            points.add(new Point(bendpoint.getLocation()));
        }
        this.path.setPathData(points);
        this.model.setLayoutData(this.path);
    }

}
