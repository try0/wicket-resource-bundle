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

		if (component instanceof Page) {
			component.add(new BundleResourceRenderer(cssBundleRenderKeyHeaderItem, jsBundleRenderKeyHeaderItem));
		}

	}

}
