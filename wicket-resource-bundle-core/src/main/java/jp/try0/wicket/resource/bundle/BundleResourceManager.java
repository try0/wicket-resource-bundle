package jp.try0.wicket.resource.bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;
import org.wicketstuff.config.MatchingResources;

/**
 * Make bundle resource.
 * 
 * @author Ryo Tsunoda
 *
 */
public class BundleResourceManager {
	private static Logger logger = LoggerFactory.getLogger(BundleResourceManager.class);

	public static final MetaDataKey<BundleResourceManager> APP_META_DATA_KEY = new MetaDataKey<BundleResourceManager>() {
	};

	/**
	 * Gets manager. <br>
	 * Need to execute {@link #register()} beforehand.
	 * 
	 * @return
	 */
	public static BundleResourceManager get() {
		if (Application.exists()) {
			Application app = Application.get();
			return app.getMetaData(APP_META_DATA_KEY);
		}

		return null;
	}

	private final Application app;

	private String scanPackageName;

	private String bundleResourceName;

	private ResourceBundleRendererConfig rendererConfig = ResourceBundleRendererConfig.MANUAL_RENDERING;

	private final List<Class<? extends Component>> resourceHolderClasses = new ArrayList<>();

	private final List<CssResourceReference> cssResoucereferences = new ArrayList<>();

	private final List<JavaScriptResourceReference> jsResoucereferences = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param app
	 */
	public BundleResourceManager(Application app) {
		this.app = app;
	}

	/**
	 * Sets the name that scan package.<br>
	 * for use {@link AnnotatedMountScanner#getPatternForPackage(String)}.
	 * 
	 * @see AnnotatedMountScanner
	 * @param scanPackageName
	 * @return
	 */
	public BundleResourceManager setScanPackageName(String scanPackageName) {
		this.scanPackageName = scanPackageName;
		return this;
	}

	/**
	 * Gets Application.
	 * 
	 * @return
	 */
	public Application getApplication() {
		return app;
	}

	/**
	 * Gets resource reference to render bundle resource.
	 * 
	 * @return
	 */
	public CssResourceReference getCssKeyResource() {
		if (!cssResoucereferences.isEmpty()) {
			return cssResoucereferences.get(0);
		}

		return null;
	}

	/**
	 * Gets resource reference to render bundle resource.
	 * 
	 * @return
	 */
	public JavaScriptResourceReference getJsKeyResource() {
		if (!jsResoucereferences.isEmpty()) {
			return jsResoucereferences.get(0);
		}

		return null;
	}

	/**
	 * Adds resource reference that register to bundles.
	 * 
	 * @param ref
	 * @return
	 */
	public BundleResourceManager addCssResourceReference(CssResourceReference ref) {
		cssResoucereferences.add(ref);
		return this;
	}

	/**
	 * Adds resource reference that register to bundles.
	 * 
	 * @param ref
	 * @return
	 */
	public BundleResourceManager addJavaScriptResourceReference(JavaScriptResourceReference ref) {
		jsResoucereferences.add(ref);
		return this;
	}

	/**
	 * Sets the whether or not to add behavior that render bundle resources to all
	 * pages.
	 * 
	 * @param appendAutoResourceRenderer
	 * @return
	 */
	public BundleResourceManager setResourceBundleRendererConfig(ResourceBundleRendererConfig rendererConfig) {
		this.rendererConfig = rendererConfig;
		return this;
	}

	public List<Class<? extends Component>> getResourceHolderClasses() {
		return resourceHolderClasses;
	}

	public boolean isResourceHolderClass(Class<?> clazz) {
		return getResourceHolderClasses().contains(clazz);
	}

	public ResourceBundleRendererConfig getRendererConfig() {
		return rendererConfig;
	}

	/**
	 * Look up {@link BundleResource}, {@link BundleResources} and register them in
	 * the {@link ResourceBundles}.
	 */
	public void register() {

		logger.info("Register resouces to bundle.");

		app.setMetaData(APP_META_DATA_KEY, this);

		Class<?> appClass = app.getClass();

		if (scanPackageName == null || scanPackageName.isEmpty()) {
			// use app package
			scanPackageName = appClass.getPackage().getName();
		}

		if (bundleResourceName == null || bundleResourceName.isEmpty()) {
			// use app name
			bundleResourceName = appClass.getSimpleName();
		}

		// lookup components
		List<Class<?>> targets = lookupComponents();
		if (targets.isEmpty() && cssResoucereferences.isEmpty() && jsResoucereferences.isEmpty()) {
			logger.warn("No resources.");
			return;
		}

		// make resource reference
		for (Class<?> componentClass : targets) {

			if (!(Component.class.isAssignableFrom(componentClass))) {
				throw new RuntimeException(
						"@" + BundleResource.class.getName() + " / " + BundleResources.class.getName()
								+ " annotated class should subclass Component: " + componentClass);
			}

			for (BundleResources globalResources : componentClass.getAnnotationsByType(BundleResources.class)) {
				for (BundleResource globalResource : globalResources.value()) {
					newResouceReference(componentClass, globalResource, cssResoucereferences, jsResoucereferences);
				}
			}

			for (BundleResource globalResource : componentClass.getAnnotationsByType(BundleResource.class)) {
				newResouceReference(componentClass, globalResource, cssResoucereferences, jsResoucereferences);
			}

		}

		if (cssResoucereferences.isEmpty() && jsResoucereferences.isEmpty()) {
			return;
		}

		// register to bundle
		ResourceBundles bundles = app.getResourceBundles();
		registerBundles(bundles, cssResoucereferences, jsResoucereferences);

		// render bundle resource config

		if (rendererConfig != ResourceBundleRendererConfig.MANUAL_RENDERING) {
			app.getComponentInstantiationListeners()
					.add(newBundleResourceAutoAppender(getCssKeyResource(), getJsKeyResource()));
		}

	}

	/**
	 * Creates bundle resource appender.
	 * 
	 * @param cssKeyResource
	 * @param jsKeyResource
	 * @return
	 */
	protected BundleResourceAutoAppender newBundleResourceAutoAppender(CssResourceReference cssKeyResource,
			JavaScriptResourceReference jsKeyResource) {

		CssHeaderItem cssRenderKeyItem = null;
		if (cssKeyResource != null) {
			cssRenderKeyItem = CssHeaderItem.forReference(jsKeyResource);
		}
		JavaScriptHeaderItem jsRenderKeyItem = null;
		if (jsKeyResource != null) {
			jsRenderKeyItem = JavaScriptHeaderItem.forReference(jsKeyResource);
		}

		return new BundleResourceAutoAppender(cssRenderKeyItem, jsRenderKeyItem);
	}

	/**
	 * Lookup components that has resources.
	 * 
	 * @return
	 */
	private List<Class<?>> lookupComponents() {

		AnnotatedMountScanner scanner = new AnnotatedMountScanner();
		String pattern = scanner.getPatternForPackage(scanPackageName);
		MatchingResources resources = new MatchingResources(pattern);

		List<Class<?>> bundleTargets = resources.getAnnotatedMatches(BundleResource.class);
		List<Class<?>> bundlesTargets = resources.getAnnotatedMatches(BundleResources.class);

		if (bundleTargets.isEmpty() && bundlesTargets.isEmpty()) {
			return Collections.emptyList();
		}

		List<Class<?>> targets = new ArrayList<>();
		targets.addAll(bundleTargets);
		targets.addAll(bundlesTargets);
		targets.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

		return targets;
	}

	/**
	 * Crates ResourceReference.
	 * 
	 * @param globalResource
	 * @param cssResouceRefs
	 * @param jsResouceRefs
	 */
	@SuppressWarnings("unchecked")
	private void newResouceReference(Class<?> componentClass, BundleResource globalResource,
			List<CssResourceReference> cssResouceRefs, List<JavaScriptResourceReference> jsResouceRefs) {

		String resourceName = globalResource.name();
		Class<? extends Component> scope = globalResource.scope();

		if (scope == Component.class) {
			scope = (Class<? extends Component>) componentClass;
		}

		logger.info(scope.getName() + "/" + resourceName);

		if (resourceName.toLowerCase().endsWith(".css")) {
			cssResouceRefs.add(new CssResourceReference(scope, resourceName));
			return;
		}

		if (resourceName.toLowerCase().endsWith(".js")) {
			jsResouceRefs.add(new JavaScriptResourceReference(scope, resourceName));
			return;
		}

		throw new UnsupportedOperationException(resourceName + " is unsupported type.");
	}

	/**
	 * Adds ResourceReference to ResourceBundles.
	 * 
	 * @param bundles
	 * @param cssResouceRefs
	 * @param jsResouceRefs
	 */
	private void registerBundles(ResourceBundles bundles, List<CssResourceReference> cssResouceRefs,
			List<JavaScriptResourceReference> jsResouceRefs) {

		if (!cssResouceRefs.isEmpty()) {
			logger.info("Make bundle " + bundleResourceName + ".css");
			bundles.addCssBundle(app.getClass(), bundleResourceName + ".css",
					cssResouceRefs.toArray(new CssResourceReference[0]));
		}

		if (!jsResouceRefs.isEmpty()) {
			logger.info("Make bundle " + bundleResourceName + ".js");
			bundles.addJavaScriptBundle(app.getClass(), bundleResourceName + ".js",
					jsResouceRefs.toArray(new JavaScriptResourceReference[0]));
		}
	}

	public static enum ResourceBundleRendererConfig {
		/**
		 * Add header items yourself.
		 */
		MANUAL_RENDERING,
		/**
		 * Renders bundle resource to all pages.
		 */
		ALL_PAGE,
		/**
		 * Renders only if the page contains components with bundled resources
		 */
		ONLY_BUNDLE_RESOURCE_HOLDER
	}
}
