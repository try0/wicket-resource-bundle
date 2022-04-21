package jp.try0.wicket.resource.bundle;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

/**
 * Adds bundle resource to {@link Page}.
 * 
 * @author Ryo Tsunoda
 *
 */
public class BundleResourceAutoAppender implements IComponentInstantiationListener {

	private final CssHeaderItem cssBundleRenderKeyHeaderItem;

	private final JavaScriptHeaderItem jsBundleRenderKeyHeaderItem;

	private transient BundleResourceManager bundleResourceManager;

	/**
	 * Constructor
	 * 
	 * @param scanner
	 */
	public BundleResourceAutoAppender(CssHeaderItem cssBundleRenderKeyHeaderItem,
			JavaScriptHeaderItem jsBundleRenderKeyHeaderItem) {
		this.cssBundleRenderKeyHeaderItem = cssBundleRenderKeyHeaderItem;
		this.jsBundleRenderKeyHeaderItem = jsBundleRenderKeyHeaderItem;
	}

	@Override
	public void onInstantiation(Component component) {

		BundleResourceManager brManager = getBundleResourceManager();

		switch (brManager.getRendererConfig()) {

		case ALL_PAGE:
			if (component instanceof Page) {
				component.add(new BundleResourceRenderer(cssBundleRenderKeyHeaderItem, jsBundleRenderKeyHeaderItem));
			}
			break;
		case ONLY_BUNDLE_RESOURCE_HOLDER:

			if (brManager.isResourceHolderClass(component.getClass())) {

				BundleResourceRenderer renderer = new BundleResourceRenderer(cssBundleRenderKeyHeaderItem,
						jsBundleRenderKeyHeaderItem);
				
				Page page = component.getPage();
				if (page != null) {
					page.add(renderer);
				} else {
					component.add(renderer);
				}
			}
			break;
		case MANUAL_RENDERING:
			// noop
			break;
		default:
			break;

		}

	}

	private BundleResourceManager getBundleResourceManager() {
		if (bundleResourceManager == null) {
			bundleResourceManager = BundleResourceManager.get();
		}

		return bundleResourceManager;
	}
}
