// License: GPL. For details, see LICENSE file.
// Copyright 2012 by Josh Doe and others.
package org.openstreetmap.josm.plugins.conflation;

import java.util.Map;

import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.plugins.jts.JTSConverter;

import com.vividsolutions.jump.feature.AbstractBasicFeature;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.FeatureSchema;

public class OsmFeature extends AbstractBasicFeature {
    private Object[] attributes;
    private OsmPrimitive primitive;
    private JTSConverter converter;

    /**
     * Create a copy of the OSM geometry
     * TODO: update from underlying primitive
     */
    public OsmFeature(OsmPrimitive prim, JTSConverter jtsConverter) {
        super(new FeatureSchema());
        primitive = prim;
        Map<String, String> keys = prim.getKeys();
        attributes = new Object[keys.size() + 1];
        getSchema().addAttribute("__GEOMETRY__", AttributeType.GEOMETRY);
        for (String key : keys.keySet()) {
            getSchema().addAttribute(key, AttributeType.STRING);
            setAttribute(key, keys.get(key));
        }
        if (jtsConverter != null)
            converter = jtsConverter;
        else
            converter = new JTSConverter(true);
        setGeometry(converter.convert(prim));
    }

    @Override
    public void setAttributes(Object[] attributes) {
        this.attributes = attributes;
    }

    @Override
    public void setAttribute(int attributeIndex, Object newAttribute) {
        attributes[attributeIndex] = newAttribute;
    }

    @Override
    public Object getAttribute(int i) {
        return attributes[i];
    }

    @Override
    public Object[] getAttributes() {
        return attributes;
    }
    
    public OsmPrimitive getPrimitive() {
        return primitive;
    }
    
    @Override
    public int getID() {
        // FIXME: should work most of the time, GeoAPI more robust, need to
        // consider the dataset (e.g. two non-uploaded layers can have different
        // objects with the same id
        return (int) primitive.getUniqueId();
    }
}
