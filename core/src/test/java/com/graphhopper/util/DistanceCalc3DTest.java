package com.graphhopper.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceCalc3DTest {

    /*
     * Test pour verifier que la distance est nulle sans deplacement
     */
    @Test
    public void testCalcDistWithSamePoint(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist = distCalc.calcDist(48.8566, 2.3522, 5, 48.8566, 2.3522, 5);
        assertEquals(0,dist, 0);
    }

    /*
     * Test pour verifier que la distance est non nulle avec variation de latitude
     */
    @Test
    public void testCalcDistWithDiffLatitude(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist = distCalc.calcDist(48.8566, 2.3522, 5, 48.9666, 2.3522, 5);
        assertNotEquals(0, dist, 0);
    }

    /*
     * Test pour verifier que la distance est non nulle avec variation de longitude
     */
    @Test
    public void testCalcDistWithDiffLongitude(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist = distCalc.calcDist(48.8566, 2.3522, 5, 48.8566, 2.4689, 5);
        assertNotEquals(0, dist, 0);
    }

    /*
     * Test pour verifier que la distance est non nulle avec variation de l'altitude
     */
    @Test
    public void testCalcDistWithDiffAltitudePositive(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist = distCalc.calcDist(48.8566, 2.3522, 5, 48.8566, 2.3522, 1000);
        assertNotEquals(0, dist, 0);
    }


    /*
     * Test pour verifier que s'il y a juste une variation de l'altitude
     * la distance est egale a l'altitude
     */
    @Test
    public void testCalcDistWithDiffAltitude(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist = distCalc.calcDist(48.8566, 2.3522, 0, 48.8566, 2.3522, 1000);
        assertEquals(1000, dist, 0);
    }

    /*
     * Test que la distance 3D est toujours > distance 2D avec altitude
     */
    @Test
    public void test3DDistanceGreaterthan2D() {
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist2D = distCalc.calcDist(48.8566, 2.3522, 50.6293, 3.1212);
        double dist3D = distCalc.calcDist(48.8566, 2.3522, 100, 50.6293, 3.1212, 500);
        assertTrue(dist3D > dist2D);
    }

    /*
     * Test que la distance 3D (Sans altitude) = 2D
     */
    @Test
    public void test3DDistanceEquals2D() {
        DistanceCalc3D distCalc = new DistanceCalc3D();
        double dist2D = distCalc.calcDist(48.8566, 2.3522, 50.6293, 3.1212);
        double dist3D = distCalc.calcDist(48.8566, 2.3522, 0, 50.6293, 3.1212, 0);
        assertEquals(dist3D, dist2D);
    }

    /*
     * Verifie que pour de longues distance l'altitude n'a pas d'impact sur la distance
     * (les hauteurs faibles sont negligeables) -- (voir le javaDoc de de DistanceCalc3D)
     * le pourcentage d'erreur choisie est de 5% de l'altitude
     * donc la distance de 3D est sensiblement egalale a la distance de 2D sans l'altitude
     *
     */
    @Test
    public void testCalcDistWithDiffAltitudeLongDistance(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        int altitude = 10000;
        double distA = distCalc.calcDist(48.8566, 2.3522, 0, 50.6293, 3.1212, altitude);
        double distWithoutAltitude = distCalc.calcDist(48.8566, 2.3522, 50.6293, 3.1212);
        assertEquals(distWithoutAltitude, distA, 0.05*altitude);
    }

    /*
     * Test pour verifier la negation de l'enonce de la classe DistanceCalc3D
     * Pour de courte distance l'altitude devrait avoir un impact sur la distance
     * par consequent la distance de 3D est differente de la distance de 2D avec l'altitude
     */
    @Test
    public void testCalcDistWithDiffAltitudeShortDistance(){
        DistanceCalc3D distCalc = new DistanceCalc3D();
        int altitude = 100;
        double distA = distCalc.calcDist(48.8566, 2.3522, 0, 48.8567, 2.3523, altitude);
        double distWithoutAltitude = distCalc.calcDist(48.8566, 2.3522, 48.8567, 2.3522);
        assertNotEquals(distWithoutAltitude, distA, 0.05*altitude);
    }


}
