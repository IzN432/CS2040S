import org.junit.Test;

import static org.junit.Assert.*;

public class WiFiTest {

    @org.junit.Test
    public void computeDistance() {
        int[] houses = {1, 3, 19};
        int numAccessPoints = 2;
        assertEquals(1.0, WiFi.computeDistance(houses, numAccessPoints), 0.5);
    }

    @org.junit.Test
    public void computeDistance1() {
        int[] houses = {1, 2, 3, 10, 11};
        int numAccessPoints = 1;
        assertEquals(5.5, WiFi.computeDistance(houses, numAccessPoints), 0.5);
    }

    @org.junit.Test
    public void computeDistance2() {
        int[] houses = {1, 2, 3, 10, 11};
        int numAccessPoints = 5;
        assertEquals(0, WiFi.computeDistance(houses, numAccessPoints), 0.5);
    }

    @org.junit.Test
    public void computeDistance3() {
        int[] houses = {1, 2, 3, 10, 11};
        int numAccessPoints = 5;
        assertEquals(0, WiFi.computeDistance(houses, numAccessPoints), 0.5);
    }

    @org.junit.Test
    public void coverable1() {
        int[] houses = {1, 3, 10};
        int numAccessPoints = 2;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 1.0));
    }

    @org.junit.Test
    public void coverable2() {
        int[] houses = {1, 3, 10};
        int numAccessPoints = 2;
        assertFalse(WiFi.coverable(houses, numAccessPoints, 0.5));
    }

    @Test
    public void coverable3() {
        int[] houses = {1, 3, 4, 7, 9};
        int numAccessPoints = 2;
        assertTrue(WiFi.coverable(houses, numAccessPoints, 1.5));
    }

    @Test
    public void coverable4() {
        int[] houses = {1, 3, 4, 7, 9};
        int numAccessPoints = 2;
        assertFalse(WiFi.coverable(houses, numAccessPoints, 1.45));
    }
}