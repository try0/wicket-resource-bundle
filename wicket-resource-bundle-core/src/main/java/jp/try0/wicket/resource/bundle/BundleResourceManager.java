package jp.try0.wicket.resource.bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

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
	 * @return bundle resource manager
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

	private Class<?> bundleResourceScope;

	private String bundleResourceName;

	private ResourceBundleRendererConfig rendererConfig = ResourceBundleRendererConfig.MANUAL_RENDERING;

	private final Set<Class<? extends Component>> cssHolderClasses = new HashSet<>();

	private final Set<Class<? extends Component>> jsHolderClasses = new HashSet<>();

	private final List<CssResourceReference> cssResoucereferences = new ArrayList<>();

	private final List<JavaScriptResourceReference> jsResoucereferences = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param app the Application
	 */
	public BundleResourceManager(Application app) {
		this.app = app;
	}

	/**
	 * Sets the scope of resource bundle.
	 * 
	 * @param bundleResourceScope the scope of resource bundle
	 * @return manager
	 */
	public BundleResourceManager setBundleResourceScope(Class<?> bundleResourceScope) {
		this.bundleResourceScope = bundleResourceScope;
		return this;
	}

	/**
	 * Sets the name of resource bundle.
	 * 
	 * @param bundleResourceName the name of resource bundle
	 * @return manager
	 */
	public BundleResourceManager setBundleResourceName(String bundleResourceName) {
		this.bundleResourceName = bundleResourceName;
		return this;
	}

	/**
	 * Sets the name of scan package.<br>
	 * for use {@link AnnotatedMountScanner#getPatternForPackage(String)}.
	 * 
	 * @see AnnotatedMountScanner
	 * @param scanPackageName the name of scan package
	 * @return manager
	 */
	public BundleResourceManager setScanPackageName(String scanPackageName) {
		this.scanPackageName = scanPackageName;
		return this;
	}

	/**
	 * Gets Application.
	 * 
	 * @return application
	 */
	public Application getApplication() {
		return app;
	}

	/**
	 * Gets resource reference to render bundle resource.
	 * 
	 * @return render key resource
	 */
	public CssResourceReference getCssKeyResourceReference() {
		if (!cssResoucereferences.isEmpty()) {
			return cssResoucereferences.get(0);
		}

		return null;
	}

	/**
	 * Gets resource reference to render bundle resource.
	 * 
	 * @return render key resource
	 */
	public JavaScriptResourceReference getJavaScriptKeyResourceReference() {
		if (!jsResoucereferences.isEmpty()) {
			return jsResoucereferences.get(0);
		}

		return null;
	}

	/**
	 * Gets header item to render bundole resource.
	 * 
	 * @return
	 */
	public CssHeaderItem getCssKeyHeaderItem() {
		CssResourceReference ref = getCssKeyResourceReference();
		if (ref == null) {
			return null;
		}

		return CssHeaderItem.forReference(ref);
	}

	/**
	 * Gets header item to render bundole resource.
	 * 
	 * @return
	 */
	public JavaScriptHeaderItem getJavaScriptKeyHeaderItem() {
		JavaScriptResourceReference ref = getJavaScriptKeyResourceReference();
		if (ref == null) {
			return null;
		}

		return JavaScriptHeaderItem.forReference(ref);
	}

	/**
	 * Adds resource reference that register to bundles.
	 * 
	 * @param ref css reference
	 * @return manager
	 */
	public BundleResourceManager addCssResourceReference(CssResourceReference ref) {
		cssResoucereferences.add(ref);
		return this;
	}

	/**
	 * Adds resource reference that register to bundles.
	 * 
	 * @param ref js reference
	 * @return manager
	 */
	public BundleResourceManager addJavaScriptResourceReference(JavaScriptResourceReference ref) {
		jsResoucereferences.add(ref);
		return this;
	}

	/**
	 * Sets the whether or not to add behavior that render bundle resources to all
	 * pages.
	 * 
	 * @param rendererConfig render config
	 * @return manager
	 */
	public BundleResourceManager setResourceBundleRendererConfig(ResourceBundleRendererConfig rendererConfig) {
		this.rendererConfig = rendererConfig;
		return this;
	}

	/**
	 * Gets the classes of resource holder.
	 * 
	 * @return the classes of resource holder
	 */
	public Set<Class<? extends Component>> getResourceHolderClasses() {
		Set<Class<? extends Component>> resourceHolders = new HashSet<>();
		resourceHolders.addAll(cssHolderClasses);
		resourceHolders.addAll(jsHolderClasses);
		return resourceHolders;
	}

	/**
	 * Whether the class has bundled resources.
	 * 
	 * @param clazz check for exists resource
	 * @return if exists resource is true otherwise false
	 */
	public boolean isResourceHolderClass(Class<?> clazz) {
		return isCssHolderClass(clazz) || isJsHolderClass(clazz);
	}

	/**
	 * Whether the class has css resources.
	 * 
	 * @param clazz check for exists css
	 * @return if exists css is true otherwise false
	 */
	public boolean isCssHolderClass(Class<?> clazz) {
		return cssHolderClasses.contains(clazz);
	}

	/**
	 * Whether the class has js resources.
	 * 
	 * @param clazz check for exists js
	 * @return if exists js is true otherwise false
	 */
	public boolean isJsHolderClass(Class<?> clazz) {
		return jsHolderClasses.contains(clazz);
	}

	/**
	 * Gets the render config.
	 * 
	 * @return the render config
	 */
	public ResourceBundleRendererConfig getRendererConfig() {
		return rendererConfig;
	}

	/**
	 * Look up {@link BundleResource}, {@link BundleResources} and register them in
	 * the {@link ResourceBundles}.
	 */
	public void register() {

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

		if (bundleResourceScope == null) {
			// use app class
			bundleResourceScope = appClass;
		}

		// lookup components
		List<Class<? extends Component>> targetComponents = lookupComponents();
		if (targetComponents.isEmpty() && cssResoucereferences.isEmpty() && jsResoucereferences.isEmpty()) {
			logger.warn("No resources.");
			return;
		}

		// make resource reference
		createResourceReference(targetComponents);
		if (cssResoucereferences.isEmpty() && jsResoucereferences.isEmpty()) {
			logger.warn("No resources.");
			return;
		}

		// register to bundle
		ResourceBundles bundles = app.getResourceBundles();
		registerBundles(bundles, cssResoucereferences, jsResoucereferences);

		if (rendererConfig != ResourceBundleRendererConfig.MANUAL_RENDERING) {
			// add resource renderer
			app.getComponentInstantiationListeners().add(newBundleResourceAutoAppender());
		}

	}

	/**
	 * Creates bundle resource appender.
	 * 
	 * @return the bundle resource appender
	 */
	protected BundleResourceAutoAppender newBundleResourceAutoAppender() {
		return new BundleResourceAutoAppender();
	}

	/**
	 * Lookup components that has resources.
	 * 
	 * @return the classes has {@link BundleResources} or {@link BundleResource}
	 */
	@SuppressWarnings("unchecked")
	private List<Class<? extends Component>> lookupComponents() {

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

		List<Class<? extends Component>> targetComponents = targets.stream().map(c -> {
			if (!(Component.class.isAssignableFrom(c))) {
				throw new RuntimeException("@" + BundleResource.class.getName() + " / "
						+ BundleResources.class.getName() + " annotated class should subclass Component: " + c);
			}

			return (Class<? extends Component>) c;
		}).collect(Collectors.toList());

		sortComponents(targetComponents);

		return targetComponents;
	}

	/**
	 * Sort classes according to resource dependencies.
	 * 
	 * @param targets
	 * @return
	 */
	private List<Class<? extends Component>> sortComponents(List<Class<? extends Component>> targets) {

		Map<Class<?>, ResourceDependency> dependencyMap = new HashMap<>();

		// make dependency list
		for (Class<?> componentClass : targets) {

			ResourceDependency resDependency = dependencyMap.computeIfAbsent(componentClass,
					k -> new ResourceDependency(k));
			dependencyMap.put(componentClass, resDependency);

			for (BundleResources globalResources : componentClass.getAnnotationsByType(BundleResources.class)) {
				for (BundleResource globalResource : globalResources.value()) {

					for (Class<?> depClass : globalResource.dependencies()) {
						ResourceDependency subResDependency = dependencyMap.computeIfAbsent(depClass,
								k -> new ResourceDependency(k));

						resDependency.dependencies.add(subResDependency);
						dependencyMap.put(depClass, subResDependency);
					}

				}
			}

			for (BundleResource globalResource : componentClass.getAnnotationsByType(BundleResource.class)) {
				for (Class<?> depClass : globalResource.dependencies()) {
					ResourceDependency subResDependency = dependencyMap.computeIfAbsent(depClass,
							k -> new ResourceDependency(k));

					resDependency.dependencies.add(subResDependency);
					dependencyMap.put(depClass, subResDependency);
				}
			}
		}

		// sort acording to resource dependencies

		List<ResourceDependency> resourceDependencies = new ArrayList<>(dependencyMap.values());
		resourceDependencies.sort((d1, d2) -> d1.clazz.getName().compareTo(d2.clazz.getName()));

		DependencyComparator comparator = new DependencyComparator();
		comparator.analyze(resourceDependencies);
		targets.sort(comparator);

		return targets;
	}

	/**
	 * Creates resource reference.
	 * 
	 * @param targets
	 */
	private void createResourceReference(Collection<Class<? extends Component>> targetComponents) {
		for (Class<? extends Component> componentClass : targetComponents) {

			for (BundleResources globalResources : componentClass.getAnnotationsByType(BundleResources.class)) {
				for (BundleResource globalResource : globalResources.value()) {
					createResouceReference(componentClass, globalResource, cssResoucereferences, jsResoucereferences);
				}
			}

			for (BundleResource globalResource : componentClass.getAnnotationsByType(BundleResource.class)) {
				createResouceReference(componentClass, globalResource, cssResoucereferences, jsResoucereferences);
			}
		}
	}

	/**
	 * Crates ResourceReference.
	 * 
	 * @param globalResource
	 * @param cssResouceRefs
	 * @param jsResouceRefs
	 */
	private void createResouceReference(Class<? extends Component> componentClass, BundleResource globalResource,
			List<CssResourceReference> cssResouceRefs, List<JavaScriptResourceReference> jsResouceRefs) {

		String resourceName = globalResource.name();
		Class<? extends Component> scope = globalResource.scope();

		if (scope == Component.class) {
			scope = componentClass;
		}

		if (resourceName.toLowerCase().endsWith(".css")) {
			cssHolderClasses.add(componentClass);
			cssResouceRefs.add(new CssResourceReference(scope, resourceName));
			return;
		}

		if (resourceName.toLowerCase().endsWith(".js")) {
			jsHolderClasses.add(componentClass);
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
			logger.info("Register bundle " + bundleResourceScope.getName() + " - " + bundleResourceName + ".css");
			cssResouceRefs.forEach(ref -> logger.info(ref.getScope().getName() + " - " + ref.getName()));

			bundles.addCssBundle(bundleResourceScope, bundleResourceName + ".css",
					cssResouceRefs.toArray(new CssResourceReference[0]));
		}

		if (!jsResouceRefs.isEmpty()) {
			logger.info("Register bundle " + bundleResourceScope.getName() + " - " + bundleResourceName + ".js");
			jsResouceRefs.forEach(ref -> logger.info(ref.getScope().getName() + " - " + ref.getName()));

			bundles.addJavaScriptBundle(bundleResourceScope, bundleResourceName + ".js",
					jsResouceRefs.toArray(new JavaScriptResourceReference[0]));
		}
	}

	/**
	 * Comparator that compares resource dependencies.
	 * 
	 * @author Ryo Tsunoda
	 *
	 */
	static class DependencyComparator implements Comparator<Class<?>> {

		static String nl = System.getProperty("line.separator");
		private final Stack<Class<?>> stack = new Stack<>();

		/**
		 * Prepare for sort.
		 * 
		 * @param classResources
		 */
		public void analyze(Collection<ResourceDependency> classResources) {
			for (ResourceDependency curDependency : classResources) {
				Set<Class<?>> visitedCurDep = new HashSet<>();
				analyze(curDependency, visitedCurDep);
			}
		}

		/**
		 * Analyze resource dependencies.
		 * 
		 * @param dependency
		 * @param visited
		 */
		private void analyze(ResourceDependency dependency, Set<Class<?>> visited) {

			boolean circulardependency = dependency.visited && visited.contains(dependency.clazz);
			if (circulardependency) {
				StringBuilder sb = new StringBuilder(dependency.clazz.getSimpleName() + nl);
				for (ResourceDependency child : dependency.dependencies) {
					createTreeString(child, dependency, sb, "");
				}
				throw new RuntimeException("Circular dependency." + nl + sb.toString());
			}

			visited.add(dependency.clazz);
			dependency.visited = true;
			for (ResourceDependency child : dependency.dependencies) {
				analyze(child, visited);
			}

			stack.push(dependency.clazz);
		}

		/**
		 * Make tree string for exception.
		 * 
		 * @param dependency
		 * @param error
		 * @param sb
		 * @param prefix
		 */
		private void createTreeString(ResourceDependency dependency, ResourceDependency error, StringBuilder sb,
				String prefix) {

			prefix += " ";
			sb.append(prefix + dependency.clazz.getSimpleName() + nl);

			if (error == dependency) {
				sb.append(prefix + "??? Cause component class." + nl);
				return;
			}

			for (ResourceDependency child : dependency.dependencies) {
				createTreeString(child, error, sb, prefix);
			}
		}

		/**
		 * Compare resource dependencies.
		 */
		@Override
		public int compare(Class<?> o1, Class<?> o2) {
			return Integer.compare(stack.indexOf(o1), stack.indexOf(o2));
		}

	}

	static class ResourceDependency {

		/**
		 * Component class.
		 */
		public final Class<?> clazz;

		/**
		 * Dependencies.
		 */
		public final Set<ResourceDependency> dependencies = new HashSet<>();

		/**
		 * Check for circular dependency.
		 */
		public boolean visited = false;

		/**
		 * Constructor.
		 * 
		 * @param componentClass
		 */
		public ResourceDependency(Class<?> componentClass) {
			this.clazz = componentClass;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof ResourceDependency)) {
				return false;
			}

			return clazz == ((ResourceDependency) obj).clazz;
		}

		@Override
		public int hashCode() {
			return Objects.hash(clazz);
		}
	}

	/**
	 * The Config that render bundles.
	 * 
	 * @author Ryo Tsunoda
	 *
	 */
	public static enum ResourceBundleRendererConfig {
		/**
		 * Add header items yourself.<br>
		 */
		MANUAL_RENDERING,
		/**
		 * Renders bundle resource to all pages.<br>
		 * When pages are instantiated, appends {@link BundleResourceRenderer}.
		 */
		ALL_PAGE,
		/**
		 * Renders only if the page contains components with bundled resources. When
		 * reousrce holder components are instantiated, appends
		 * {@link BundleResourceRenderer}.
		 */
		ONLY_BUNDLE_RESOURCE_HOLDER
	}
}
