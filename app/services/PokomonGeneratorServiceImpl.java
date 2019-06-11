package services;

import util.LocationUtil;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by jeromeheissler on 28/02/2017.
 */
public class PokomonGeneratorServiceImpl<T> implements PokomonGeneratorService<T> {

    @Override
    public List<T> findNearBy(double lat, double lng, int max) {
        return null;
    }

}
