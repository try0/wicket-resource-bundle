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

	private final BundleResourceManager bundleResourceManager;

	/**
	 * Constructor
	 * 
	 * @param scanner
	 */
	public BundleResourceAutoAppender(CssHeaderItem cssBundleRenderKeyHeaderItem,
			JavaScriptHeaderItem jsBundleRenderKeyHeaderItem) {
		this.cssBundleRenderKeyHeaderItem = cssBundleRenderKeyHeaderItem;
		this.jsBundleRenderKeyHeaderItem = jsBundleRenderKeyHeaderItem;

		this.bundleResourceManager = BundleResourceManager.get();
	}

	@Override
	public void onInstantiation(Component component) {

		switch (bundleResourceManager.getRendererConfig()) {

		case ALL_PAGE:
			if (component instanceof Page) {
				component.add(newBundleResourceRenderer(cssBundleRenderKeyHeaderItem, jsBundleRenderKeyHeaderItem));
			}
			break;

		case ONLY_BUNDLE_RESOURCE_HOLDER:

			CssHeaderItem cssItem = null;
			JavaScriptHeaderItem jsItem = null;

			if (bundleResourceManager.isCssHolderClass(component.getClass())) {
				cssItem = cssBundleRenderKeyHeaderItem;
			}
			if (bundleResourceManager.isJsHolderClass(component.getClass())) {
				jsItem = jsBundleRenderKeyHeaderItem;
			}

			if (cssItem != null || jsItem != null) {
				BundleResourceRenderer renderer = newBundleResourceRenderer(cssItem, jsItem);
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
	 * @param cssBundleRenderKeyHeaderItem the render key
	 * @param jsBundleRenderKeyHeaderItem the render key
	 * @return key resource renderer for resolve bundle resource
	 */
	protected BundleResourceRenderer newBundleResourceRenderer(CssHeaderItem cssBundleRenderKeyHeaderItem,
			JavaScriptHeaderItem jsBundleRenderKeyHeaderItem) {
		return new BundleResourceRenderer(cssBundleRenderKeyHeaderItem, jsBundleRenderKeyHeaderItem);
	}
}
