package jp.try0.wicket.resource.bundle;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

/**
 * Key resource renderer for resolve bundle resource.
 * 
 * @author Ryo Tsunoda
 *
 */
public class BundleResourceRenderer extends Behavior {

	/**
	 * Constructor.
	 */
	public BundleResourceRenderer() {
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		// render bundle resources
		BundleResourceManager bundleResourceManager = BundleResourceManager.get();

		switch (bundleResourceManager.getRendererConfig()) {
		case ALL_PAGE: {
			CssHeaderItem cssRenderKeyItem = bundleResourceManager.getCssKeyHeaderItem();
			if (cssRenderKeyItem != null) {
				response.render(cssRenderKeyItem);
			}

			JavaScriptHeaderItem jsRenderKeyItem = bundleResourceManager.getJavaScriptKeyHeaderItem();
			if (jsRenderKeyItem != null) {
				response.render(jsRenderKeyItem);
			}
			break;
		}

		case ONLY_BUNDLE_RESOURCE_HOLDER: {

			if (bundleResourceManager.isCssHolderClass(component.getClass())) {
				CssHeaderItem cssRenderKeyItem = bundleResourceManager.getCssKeyHeaderItem();
				if (cssRenderKeyItem != null) {
					response.render(cssRenderKeyItem);
				}
			}

			if (bundleResourceManager.isJsHolderClass(component.getClass())) {
				JavaScriptHeaderItem jsRenderKeyItem = bundleResourceManager.getJavaScriptKeyHeaderItem();
				if (jsRenderKeyItem != null) {
					response.render(jsRenderKeyItem);
				}
			}
			break;
		}

		case MANUAL_RENDERING:
			break;
		default:
			break;

		}

	}
}
