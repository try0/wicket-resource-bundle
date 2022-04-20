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

	private final CssHeaderItem cssItem;

	private final JavaScriptHeaderItem jsItem;

	/**
	 * Constructor
	 * 
	 * @param scanner
	 */
	public BundleResourceAutoAppender(CssHeaderItem cssItem, JavaScriptHeaderItem jsItem) {
		this.cssItem = cssItem;
		this.jsItem = jsItem;
	}

	@Override
	public void onInstantiation(Component component) {

		if (component instanceof Page) {
			component.add(new BundleResourceRenderer(cssItem, jsItem));
		}

	}

}
