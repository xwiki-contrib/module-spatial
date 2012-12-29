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
package org.xwiki.contrib.spatial.internal;

import javax.inject.Inject;

import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.spatial.internal.search.SpatialSearch;
import org.xwiki.script.service.ScriptService;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@Component("spatial")
public class SpatialScriptService implements ScriptService
{
    @Inject
    private SpatialSearch search;

    public Geometry fromWKT(String wkt)
    {
        WKTReader reader = new WKTReader();

        try {
            return reader.read(wkt);
        } catch (ParseException e) {
            return null;
        }
    }

    public SpatialSearch getSearch()
    {
        return this.search;
    }

}
