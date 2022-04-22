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

@BundleResources({ @BundleResource(name = "APanel.css", dependencies = { MyPanel2.class }),
		@BundleResource(name = "APanel.js", dependencies = { MyPanel2.class }) })
public class APanel extends Panel {

	public APanel(String id) {
		super(id);
		add(new Label("pnlLabel", "MyPanel"));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "APanel.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "APanel.js")));
	}
}
