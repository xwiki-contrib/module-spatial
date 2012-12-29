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
package com.xpn.xwiki.objects.meta;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;

import com.xpn.xwiki.objects.classes.GeometryClass;
import com.xpn.xwiki.objects.classes.NumberClass;
import com.xpn.xwiki.objects.classes.PropertyClassInterface;
import com.xpn.xwiki.objects.classes.StaticListClass;

/**
 * The Spatial Coordinates Field Class allows to create a field for spatial coordinates values.
 * 
 * @since 1.0
 */
@Component
@Named("Geometry")
@Singleton
public class GeometryMetaClass extends StringMetaClass
{
    /**
     * Constructor for GeometryMetaClass.
     */
    public GeometryMetaClass()
    {
        setPrettyName("Geometry");
        setName(getClass().getAnnotation(Named.class).value());
        
        StaticListClass typeClass = new StaticListClass(this);
        typeClass.setName("geometryType");
        typeClass.setPrettyName("Geometry Type");
        typeClass.setValues("all|point|polygon|rectangle|circle");
        typeClass.setRelationalStorage(false);
        typeClass.setDisplayType("select");
        typeClass.setMultiSelect(true);
        typeClass.setSize(1);
        safeput(typeClass.getName(), typeClass);
    }

    @Override
    public PropertyClassInterface getInstance()
    {
        return new GeometryClass();
    }

}
