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
package org.xwiki.contrib.spatial.internal.search.solr;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.xwiki.bridge.DocumentAccessBridge;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.spatial.internal.search.SpatialSearch;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReferenceResolver;
import org.xwiki.model.reference.EntityReferenceSerializer;
import org.xwiki.model.reference.ObjectReference;
import org.xwiki.query.Query;
import org.xwiki.query.QueryException;
import org.xwiki.query.QueryExecutor;
import org.xwiki.query.internal.DefaultQuery;
import org.xwiki.query.solr.internal.SolrQueryExecutor;
import org.xwiki.search.solr.internal.api.Fields;

@Component(hints = {"solr", "default"})
public class SolrSpatialSearch implements SpatialSearch
{
    private static final String PROPERTY_GEO = "property_geo";

    @Inject
    @Named("compactwiki")
    private EntityReferenceSerializer<String> serializer;

    @Inject
    private EntityReferenceResolver<String> resolver;

    @Inject
    @Named(SolrQueryExecutor.SOLR)
    private QueryExecutor queryExecutor;

    @Inject
    private Logger logger;

    /**
     * XWiki model bridge.
     */
    @Inject
    protected DocumentAccessBridge documentAccessBridge;

    private interface ResultAdapter<T>
    {
        T adapt(SolrDocument document);
    }

    @Override
    public List<ObjectReference> searchObjectsWithinDistanceOfPoint(DocumentReference classReference,
        String propertyName, double x, double y, double distance)
    {
        return this.searchWithinDistanceOfPoint(classReference, propertyName, x, y, distance,
            new ResultAdapter<ObjectReference>()
            {
                @Override
                public ObjectReference adapt(SolrDocument document)
                {
                    return getObjectReference(document);
                }
            });
    }

    @Override
    public List<DocumentReference> searchDocumentsWithinDistanceOfPoint(DocumentReference classReference,
        String propertyName, double x, double y, double distance)
    {
        return this.searchWithinDistanceOfPoint(classReference, propertyName, x, y, distance,
            new ResultAdapter<DocumentReference>()
            {
                @Override
                public DocumentReference adapt(SolrDocument document)
                {
                    return new DocumentReference(
                        (String) document.getFieldValue(Fields.WIKI),
                        (String) document.getFieldValue(Fields.SPACE),
                        (String) document.getFieldValue(Fields.NAME)
                    );
                }
            });
    }
    
    protected <T> List<T> searchWithinDistanceOfPoint(DocumentReference classReference, String propertyName, double x,
        double y, double distance, ResultAdapter<T> adapter)
    {
        String query = this.createIntersectionQuery(classReference, propertyName, x, y, distance);
        try {
            List< ? > result = this.executeQuery(query);
            if (result.size() > 0) {
                List<T> objects = new ArrayList<T>();
                QueryResponse response = (QueryResponse) result.get(0);
                SolrDocumentList list = response.getResults();
                for (SolrDocument entry : list) {
                    objects.add(adapter.adapt(entry));
                }
                return objects;
            }
        } catch (QueryException e) {
            this.logger.warn("Failed to execute solr spatial query {}", query);
        }
        return Collections.emptyList();
    }

    protected String createIntersectionQuery(DocumentReference classReference, String propertyName, double x, double y,
        double distance)
    {
        return MessageFormat.format("class:{0} and propertyname:{1} and " + PROPERTY_GEO
            + ":\"Intersects(Circle({2} {3} d={4}))\"", serializer.serialize(classReference), propertyName, x, y,
            distance);
    }

    private ObjectReference getObjectReference(SolrDocument document)
    {
        String serializedReference = (String) document.getFieldValue(Fields.ID);
        return new ObjectReference(this.resolver.resolve(serializedReference, EntityType.OBJECT));
    }

    private List< ? > executeQuery(String query) throws QueryException
    {
        Query actualQuery = new DefaultQuery(query, this.queryExecutor);
        return actualQuery.execute();
    }

}
