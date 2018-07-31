package org.company.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to resolve the given property from the message resource bundle which gets refreshed for a 
 * specific interval.The refreshed properties will be always read and returned.
 * 
 * @author ravi Shankar anupindi
 *
 */

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;


@Component
public class ResourceBundlesScanner {

    private static final Logger LOGGER = LogManager
            .getLogger(ResourceBundlesScanner.class.getName());
    /**
     * Reference of this resource, is used to read the reloaded properties
     */
    private static ReloadableResourceBundleMessageSource reloadableResourceBundles;

    public static ReloadableResourceBundleMessageSource getReloadableResourceBundles() {
        return reloadableResourceBundles;
    }

    /**
     * Setter based message resource injection
     * 
     * @param reloadableResourceBundles
     *            - resource which is injected
     */
    public static void setReloadableResourceBundles(
            final ReloadableResourceBundleMessageSource reloadableResourceBundles) {
        ResourceBundlesScanner.reloadableResourceBundles = reloadableResourceBundles;
    }

    /**
     * Read the property value from the resource
     * 
     * @param key
     *            - given key
     * @return - value, set to the property
     */ 
    public static String getMessage(final String key) {
        String retVal = "";
        try {
            retVal = reloadableResourceBundles.getMessage(key, null, null);
        } catch (Exception exception) {
            LOGGER.debug("Resource bundle key definition is missing - "+key,
                    exception);
            return null;
        }
        return retVal;
    }
    
	 
	/**
	 * Read the property value from the resource. if the value is not found then
	 * the passed in default value will be returned instead.
	 * 
	 * @param key
	 *            - given key
	 * @param defaultValue
	 *            - given value to return if the property is not found
	 * 
	 * @return - value, set to the property
	 */
    public static String getMessageOrDefault(final String key, final String defaultValue) {
    	return reloadableResourceBundles.getMessage(key, null, defaultValue, null);
    }
}