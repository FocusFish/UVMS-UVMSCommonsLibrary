/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package fish.focus.uvms.commons.geometry.model;

import org.opengis.feature.simple.SimpleFeature;

import java.util.Objects;

public class SimpleFeatureWrapper {

    private SimpleFeature value;

    public SimpleFeatureWrapper() {
    }

    public SimpleFeatureWrapper(SimpleFeature value) {
        this.value = value;
    }

    public SimpleFeature getValue() {
        return value;
    }

    public void setValue(SimpleFeature value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleFeatureWrapper that = (SimpleFeatureWrapper) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "SimpleFeatureWrapper{" +
                "value=" + value +
                '}';
    }
}
