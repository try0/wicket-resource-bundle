package jp.try0.wicket.resource.bundle;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

/**
 * 
 * @author Ryo Tsunoda
 *
 */
public class BundleResourceRenderer extends Behavior {

	private final CssHeaderItem cssBundleRenderKeyHeaderItem;

	private final JavaScriptHeaderItem jsBundleRenderKeyHeaderItem;

	/**
	 * Constructor
	 * 
	 * @param cssBundleRenderKeyHeaderItem
	 * @param jsBundleRenderKeyHeaderItem
	 */
	public BundleResourceRenderer(CssHeaderItem cssBundleRenderKeyHeaderItem,
			JavaScriptHeaderItem jsBundleRenderKeyHeaderItem) {
		this.cssBundleRenderKeyHeaderItem = cssBundleRenderKeyHeaderItem;
		this.jsBundleRenderKeyHeaderItem = jsBundleRenderKeyHeaderItem;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		// render bundle resources

		if (cssBundleRenderKeyHeaderItem != null) {
			response.render(cssBundleRenderKeyHeaderItem);
		}

		if (jsBundleRenderKeyHeaderItem != null) {
			response.render(jsBundleRenderKeyHeaderItem);
		}

	}
}
