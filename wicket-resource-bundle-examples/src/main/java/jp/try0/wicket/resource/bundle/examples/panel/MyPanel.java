package jp.try0.wicket.resource.bundle.examples.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResources;

@BundleResources({
		@BundleResource(name = "MyPanel.css"),
		@BundleResource(name = "MyPanel.js") })
public class MyPanel extends Panel {

	public MyPanel(String id) {
		super(id);

		add(new Label("pnlLabel", "MyPanel"));
	}
}
