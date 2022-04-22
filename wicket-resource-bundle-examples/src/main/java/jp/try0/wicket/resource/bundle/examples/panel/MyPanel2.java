package jp.try0.wicket.resource.bundle.examples.panel;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResources;

@BundleResources({ @BundleResource(name = "MyPanel2.css", dependencies = { MyPanel.class }),
		@BundleResource(name = "MyPanel2.js", dependencies = { MyPanel.class }) })
public class MyPanel2 extends Panel {

	public MyPanel2(String id) {
		super(id);
		add(new Label("pnlLabel", "MyPanel"));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "MyPanel2.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "MyPanel2.js")));
	}
}
