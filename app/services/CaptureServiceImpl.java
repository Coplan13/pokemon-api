package services;

import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeromeheissler on 28/02/2017.
 */
@Singleton
public class CaptureServiceImpl implements CaptureService {

    @Override
    public boolean acceptCapture() {
        return true;
    }
}
