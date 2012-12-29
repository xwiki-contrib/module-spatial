This is an experimental module.

## Requirements

* XWiki 4.4-SNAPSHOT + (post lucene/solr 4.0.0 migration)

## Installation

* Build xwiki-platform-search-solr-api from https://github.com/jvelo/xwiki-platform/tree/feature-spatial branch and replace the one in your installation by the built artifact
* Copy the JTS jar in your WEB-INF/lib/
* Build this module and copy it in your WEB-INF/lib/
* Add the following to your solr schema.xml (or copy the one from src/main/resources in jvelo:feature-spatial

```xml
<fieldType name="geohash" class="solr.SpatialRecursivePrefixTreeFieldType"
           spatialContextFactory="com.spatial4j.core.context.jts.JtsSpatialContextFactory"
           distErrPct="0.025"
           maxDistErr="0.000009"
           units="degrees" />

<field name="property_geo"  type="geohash"  indexed="true" stored="true"  multiValued="true" />
```
