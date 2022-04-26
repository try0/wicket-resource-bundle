package jp.try0.wicket.resource.bundle;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.application.IComponentInstantiationListener;

/**
 * Adds bundle resource to {@link Page}.
 * 
 * @author Ryo Tsunoda
 *
 */
public class BundleResourceAutoAppender implements IComponentInstantiationListener {

	/**
	 * Constructor.
	 */
	public BundleResourceAutoAppender() {
	}

	@Override
	public void onInstantiation(Component component) {

		BundleResourceManager bundleResourceManager = BundleResourceManager.get();
		
		switch (bundleResourceManager.getRendererConfig()) {

		case ALL_PAGE:
			if (component instanceof Page) {
				component.add(newBundleResourceRenderer());
			}
			break;

		case ONLY_BUNDLE_RESOURCE_HOLDER:
			if (bundleResourceManager.isResourceHolderClass(component.getClass())) {
				BundleResourceRenderer renderer = newBundleResourceRenderer();
				component.add(renderer);
			}
			break;

		case MANUAL_RENDERING:
			// noop
			// Users implement their own header item rendering process
			break;

		default:
			break;

		}

	}

	/**
	 * Creates renderer.
	 * 
	 * @return key resource renderer for resolve bundle resource
	 */
	protected BundleResourceRenderer newBundleResourceRenderer() {
		return new BundleResourceRenderer();
	}
}
