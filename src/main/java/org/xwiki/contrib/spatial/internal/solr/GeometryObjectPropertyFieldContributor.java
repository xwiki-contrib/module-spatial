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
package org.xwiki.contrib.spatial.internal.solr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.context.Execution;
import org.xwiki.context.ExecutionContext;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.ObjectPropertyReference;
import org.xwiki.search.solr.internal.metadata.ObjectPropertyFieldContributor;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseProperty;
import com.xpn.xwiki.objects.PropertyInterface;
import com.xpn.xwiki.objects.classes.GeometryClass;

@Component
public class GeometryObjectPropertyFieldContributor implements ObjectPropertyFieldContributor 
{
    @Inject
    private Logger logger;
    
    @Inject
    private Execution execution;

    public List<Field> contribute(BaseProperty<ObjectPropertyReference> objectProperty)
    {
        try {
        XWikiDocument xclass = getDocument(objectProperty.getObject().getXClassReference());
        PropertyInterface property = xclass.getXClass().get(objectProperty.getName());

        if (property instanceof GeometryClass) {
            return Arrays.asList(new Field("property_geo", objectProperty.getValue()));
        }
        } catch (XWikiException e) {
            this.logger.error("Error while obtaining property class", e);
        }
        
        return Collections.emptyList();
    }

    /**
     * Utility method.
     * 
     * @param documentReference reference to a document.
     * @return the {@link XWikiDocument} instance referenced.
     * @throws XWikiException if problems occur.
     */
    protected XWikiDocument getDocument(DocumentReference documentReference) throws XWikiException
    {
        XWikiContext context = getXWikiContext();
        XWikiDocument document = context.getWiki().getDocument(documentReference, context);

        return document;
    }

    /**
     * @return the XWikiContext
     */
    protected XWikiContext getXWikiContext()
    {
        ExecutionContext executionContext = this.execution.getContext();
        XWikiContext context = (XWikiContext) executionContext.getProperty(XWikiContext.EXECUTIONCONTEXT_KEY);
        return context;
    }

}
