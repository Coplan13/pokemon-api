package util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jeromeheissler on 28/02/2017.
 */
public class LocationUtil {

    public static double[] getRandomLocationNear(double lat, double lng, int rayon) {
        double[] circle = getBoundingBox(lat, lng, rayon);

        double newLat = ThreadLocalRandom.current().nextDouble(circle[0], circle[2]);
        double newLng = ThreadLocalRandom.current().nextDouble(circle[1], circle[3]);

        return new double[]{newLat, newLng};
    }

    private static double[] getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInMeters) {

        final double[] boundingBox = new double[4];

        final double latRadian = Math.toRadians(pLatitude);

        final double degLatKm = 110.574235;
        final double degLongKm = 110.572833 * Math.cos(latRadian);
        final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
        final double deltaLong = pDistanceInMeters / 1000.0 / degLongKm;

        final double minLat = pLatitude - deltaLat;
        final double minLong = pLongitude - deltaLong;
        final double maxLat = pLatitude + deltaLat;
        final double maxLong = pLongitude + deltaLong;

        boundingBox[0] = minLat;
        boundingBox[1] = minLong;
        boundingBox[2] = maxLat;
        boundingBox[3] = maxLong;

        return boundingBox;
    }


}
