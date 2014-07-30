package org.gradoservice.mapRender.geo.crs;

import org.gradoservice.mapRender.geo.crs.CRSTest;
import org.gradoservice.mapRender.geo.crs.EPSG4326;
import org.gradoservice.mapRender.geometry.Point;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Formatting.format;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class EPSG4326Test extends CRSTest<EPSG4326> {

    public EPSG4326Test() {
        super(EPSG4326.getInstance());
    }

    @Test
    public void getSizeTest() {
        long worldSize =256;
        int i = 0;
        Point crsSize;
        for (i = 0; i <= 22; i++) {
            crsSize = crs.getProjectedBounds(i).getSize();
            assertThat(crsSize.getX()).as(format("size test for x: %s", i)).isEqualTo(worldSize * 2);
            assertThat(crsSize.getY()).as(format("size test for y: %s", i)).isEqualTo(worldSize);
            worldSize *= 2;
        }
    }
}
