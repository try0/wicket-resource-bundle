package jp.try0.wicket.resource.bundle.examples;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.examples.panel.MyPanel;

@BundleResource(scope = HomePage.class, name = "HomePage.css")
public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		add(new MyPanel("myPanel"));

	}

}
