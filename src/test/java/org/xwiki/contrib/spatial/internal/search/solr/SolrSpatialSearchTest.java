package org.xwiki.contrib.spatial.internal.search.solr;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.Test;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.contrib.spatial.internal.search.SpatialSearch;
import org.xwiki.model.EntityType;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.model.reference.EntityReferenceValueProvider;
import org.xwiki.test.AbstractMockingComponentTestCase;
import org.xwiki.test.annotation.MockingRequirement;
import org.xwiki.test.annotation.MockingRequirements;

import com.xpn.xwiki.internal.model.reference.CompactWikiStringEntityReferenceSerializer;

@MockingRequirements({@MockingRequirement(SolrSpatialSearch.class),
@MockingRequirement(CompactWikiStringEntityReferenceSerializer.class)})
public class SolrSpatialSearchTest extends AbstractMockingComponentTestCase<SpatialSearch>
{
    private EntityReferenceValueProvider provider;

    @Before
    public void setUpExpectations() throws Exception
    {
        provider = getComponentManager().getInstance(EntityReferenceValueProvider.class, "current");

        getMockery().checking(new Expectations()
        {
            {
                allowing(provider).getDefaultValue(with((any(EntityType.class))));
                will(returnValue("xwiki"));
            }
        });
    }

    @Test
    public void testCreateIntersectionQuery() throws ComponentLookupException, Exception
    {
        SolrSpatialSearch solrSpatialSearch = getComponentManager().getInstance(SpatialSearch.class, "solr");
        DocumentReference classReference = new DocumentReference("xwiki", "XWiki", "SomeClassWithGeo");
        String query = solrSpatialSearch.createIntersectionQuery(classReference, "my_property", 0, 1, 2);
        Assert
            .assertEquals(
                "class:XWiki.SomeClassWithGeo and propertyname:my_property and property_geo:\"Intersects(Circle(0 1 d=2))\"",
                query);
    }

}
