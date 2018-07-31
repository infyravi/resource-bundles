package org.company.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.VfsResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import org.apache.commons.lang.StringUtils;

/**
 * WildcardReloadableResourceBundleMessageSource.java
 * 
 * Spring-specific {@link org.springframework.context.MessageSource}
 * implementation that accesses resource bundles using specified basenames using
 * wild card entries, participating in the Spring
 * {@link org.springframework.context.ApplicationContext}'s resource loading.
 * 
 * <p>
 * This class extends spring's ReloadableResourceBundleMessageSource class and
 * overrides the setBasenames() method by providing the custom implementation to
 * load all the basenames matching the wild card entry specified in the spring
 * configuration file.
 * 
 * <p>
 * It internally uses spring's PathMatchingResourcePatternResolver class to
 * match all the resources matching the specified wild card entry. To use this
 * class we need to replace ReloadableResourceBundleMessageSource in the spring
 * coniguration file with this class. All other features which are supported by
 * ReloadableResourceBundleMessageSource remain as it is.
 * 
 */
public class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	private static final Logger LOGGER = LogManager.getLogger(WildcardReloadableResourceBundleMessageSource.class.getName());
	/*
	 * Constant to define the properties file extension
	 */
	private static final String PROPERTIESSUFFIX = ".properties";
	/*
	 * Constant to define the classes string in the URI
	 */
	private static final String CLASSES ="/classes/";
	/*
	 * constant to define the class path string
	 */
	private static final String CLASSPATH ="classpath:";
	/*
	 * Variable reference to PathMatching resource
	 */
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();


	@Override
	public void setBasenames(String... passedInBaseNames) {
		if (passedInBaseNames != null) {
			List<String> baseNames = new ArrayList<>();
			for (int i = 0, len=passedInBaseNames.length; i < len; i++) {
				String basename = StringUtils.trimToEmpty(passedInBaseNames[i]);
				if (StringUtils.isNotBlank(basename)) {
					try {
						Resource[] resources = resourcePatternResolver.getResources(basename);
						for (int j = 0, resLen=resources.length; j <resLen  ; j++) {
							Resource resource = resources[j];
							String baseName = getBaseName(resource);
							if (baseName != null) {
								baseNames.add(baseName);
							}
						}
					} catch (IOException e) {
						logger.error("No message source files found for basename " + basename + ".");
								logger.error(e);
					}
				}
				super.setBasenames(baseNames.toArray(new String[baseNames.size()]));
			}
		}
	}

	/**
	 * This method is used to get the basename from the resource URI.
	 * @param resource
	 * @return the baseName
	 * @throws IOException
	 */
	private String getBaseName(Resource resource) throws IOException {
		String baseName = null;
		String uri = resource.getURI().toString();
		if(!uri.endsWith(PROPERTIESSUFFIX)){
			return baseName;
		}
		/*
		 * On jBOSS application server the property files are stored in virtual
		 * file system so the resource type wil be VfsResource. On local it will
		 * be FileSystemResource. We are using OR condition so that it will work
		 * on both local and jBOSS without any issues
		 */
		if (resource instanceof FileSystemResource || resource instanceof VfsResource) {
			baseName = CLASSPATH + StringUtils.substringBetween(uri, CLASSES, PROPERTIESSUFFIX);
		} else if (resource instanceof ClassPathResource) {
			baseName = StringUtils.substringBefore(uri, PROPERTIESSUFFIX);
		} else if (resource instanceof UrlResource) {
			baseName = CLASSPATH + StringUtils.substringBetween(uri, ".jar!/",PROPERTIESSUFFIX);
		} else {
			LOGGER.info("URI not matched any of the resource types..so just reading it as a string");
			baseName = CLASSPATH + StringUtils.substringBetween(uri, CLASSES,PROPERTIESSUFFIX);
		}
		return baseName;
	}
}