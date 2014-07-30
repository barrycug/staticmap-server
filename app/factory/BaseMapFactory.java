package factory;

import org.gradoservice.mapRender.layers.Layer;
import org.gradoservice.mapRender.layers.OSM;
import org.gradoservice.mapRender.layers.Rekod;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: germanosin
 * Date: 15.06.14
 * Time: 13:08
 */
public class BaseMapFactory {
    private static volatile BaseMapFactory instance;

    public static BaseMapFactory getInstance() {
        BaseMapFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (BaseMapFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new BaseMapFactory();
                }
            }
        }
        return localInstance;
    }

    private static ConcurrentHashMap<String, Class> baseMaps = new ConcurrentHashMap<>();

    public static void registerBaseMap(String name, Class baseLayerClass) {
        if (!baseMaps.contains(name)) {
            baseMaps.put(name, baseLayerClass);
        }
    }

    public Layer getBaseLayer(String name) throws IllegalAccessException, InstantiationException {
        Class clazz = baseMaps.get(name);
        return (Layer)clazz.newInstance();
    }

    public boolean hasBaseLayer(String name) {
        return baseMaps.containsKey(name);
    }
}
