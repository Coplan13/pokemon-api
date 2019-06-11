import com.google.inject.AbstractModule;
import services.CaptureService;
import services.CaptureServiceImpl;
import services.PokomonGeneratorService;
import services.PokomonGeneratorServiceImpl;

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.
 *
 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(PokomonGeneratorService.class).to(PokomonGeneratorServiceImpl.class);
        bind(CaptureService.class).to(CaptureServiceImpl.class).asEagerSingleton();
    }

}
