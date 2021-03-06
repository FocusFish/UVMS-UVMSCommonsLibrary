/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package fish.focus.uvms.commons.geometry.mapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.opengis.feature.simple.SimpleFeature;
import fish.focus.uvms.commons.geometry.model.FeatureCollectionWrapper;
import fish.focus.uvms.commons.geometry.model.GeometryWrapper;
import fish.focus.uvms.commons.geometry.model.SimpleFeatureWrapper;
import fish.focus.uvms.commons.geometry.model.StringWrapper;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

@Mapper(imports = {WKTWriter.class, WKTReader.class, GeometryJSON.class, StringReader.class, FeatureJSON.class})
public abstract class GeometryMapper {

    public static GeometryMapper INSTANCE = Mappers.getMapper(GeometryMapper.class);

    @Mapping(target = "value", expression = "java(new WKTWriter().write(geometry))")
    public abstract StringWrapper geometryToWkt(Geometry geometry);

    @Mapping(target = "value", expression = "java(new WKTReader().read(wkt))")
    public abstract GeometryWrapper wktToGeometry(String wkt) throws ParseException;

    @Mapping(target = "value", expression = "java(new GeometryJSON().toString(geometry))")
    public abstract StringWrapper geometryToJson(Geometry geometry);

    @Mapping(target = "value", expression = "java(new GeometryJSON().read(new StringReader(json)))")
    public abstract GeometryWrapper jsonToGeometryJson(String json) throws ParseException, IOException;

    @Mapping(target = "value", expression = "java(new FeatureJSON().readFeature(new StringReader(feature)))")
    public abstract SimpleFeatureWrapper geoJsonFeatureToSimpleFeature(String feature) throws IOException;

    @Mapping(target = "value", expression = "java(new FeatureJSON().readFeatureCollection(new StringReader(fCollection)))")
    public abstract FeatureCollectionWrapper geoJsonToFeatureCollection(String fCollection) throws IOException;

    public void simpleFeatureToGeoJson(SimpleFeature geoJsonFeature, StringWriter writer) throws IOException {
        new FeatureJSON().writeFeature(geoJsonFeature, writer);
    }

    public void featureCollectionToGeoJson(FeatureCollection featureCollection, StringWriter writer) throws IOException {
        new FeatureJSON().writeFeatureCollection(featureCollection, writer);
    }

}
