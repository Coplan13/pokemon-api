package services;

import java.util.List;

/**
 * Created by jeromeheissler on 28/02/2017.
 */
public interface PokomonGeneratorService<T> {
    List<T> findNearBy(double lat, double lng, int max);
}
