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

	private final CssHeaderItem cssItem;

	private final JavaScriptHeaderItem jsItem;

	/**
	 * Constructor
	 * 
	 * @param scanner
	 */
	public BundleResourceRenderer(CssHeaderItem cssItem, JavaScriptHeaderItem jsItem) {
		this.cssItem = cssItem;
		this.jsItem = jsItem;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		// render bundle resources

		if (cssItem != null) {
			response.render(cssItem);
		}

		if (jsItem != null) {
			response.render(jsItem);
		}

	}
}
