package org.prototype.study;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationManager {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(ConfigurationManager.class.getResourceAsStream("/configuration.properties"));
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    public static String getPropertyValue(String propertyKey) {
        if (propertyKey != null && !propertyKey.isEmpty()) {
            return properties.getProperty(propertyKey);
        }
        return null;
    }

    public static Map<String, String> getProperties() {
        Map<String, String> propertyMap = new HashMap<String, String>();
        for (String propertyKey : properties.stringPropertyNames()) {
            propertyMap.put(propertyKey, properties.getProperty(propertyKey));
        }
        return propertyMap;
    }
}
