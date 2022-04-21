package jp.try0.wicket.resource.bundle.examples.panel;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResources;

@BundleResources({ @BundleResource(name = "MyPanel.css"), @BundleResource(name = "MyPanel.js") })
public class MyPanel extends Panel {

	public MyPanel(String id) {
		super(id);

		add(new Label("pnlLabel", "MyPanel"));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "MyPanel.css")));
		response.render(CssHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "MyPanel.js")));
	}
}
