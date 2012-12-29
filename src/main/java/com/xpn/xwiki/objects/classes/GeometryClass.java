/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.objects.classes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.xpn.xwiki.objects.BaseProperty;
import com.xpn.xwiki.objects.StringProperty;
import com.xpn.xwiki.objects.meta.PropertyMetaClass;

/**
 * The geometry Class allows to create a field for geometries (point, polygon, shapes, etc).
 * 
 * @since 1.0
 */
public class GeometryClass extends StringClass
{
    /** Logging helper object. */
    private static final Logger LOG = LoggerFactory.getLogger(GeometryClass.class);

    /**
     * Constant defining the field name.
     **/
    protected static final String XCLASSNAME = "geometry";

    /**
     * Constructor for geometry Class.
     * 
     * @param wclass Meta Class
     **/
    public GeometryClass(PropertyMetaClass wclass)
    {
        super(XCLASSNAME, "Geometry", wclass);
    }

    /**
     * Constructor for geometry Class.
     **/
    public GeometryClass()
    {
        super();
    }

    @Override
    public BaseProperty fromString(String value)
    {
        WKTReader reader = new WKTReader();
        try {
            Geometry geometry = reader.read(value);
            BaseProperty property = new StringProperty();

            property.setName(getName());
            
            //property.setValue(geometry);
            property.setValue(value);
            return property;
        } catch (ParseException e1) {
            LOG.warn("Invalid Well-known-text value for property " + getName() + " of class " + getObject().getName()
                + ": " + value);
            // Returning null makes sure that the old value (if one exists) will not be discarded/replaced
            return null;
        }
    }

    private String getGeometryType()
    {
        return getStringValue("geometryType");
    }

    public void setGeometryType(String ntype)
    {
        setStringValue("geometryType", ntype);
    }

}
