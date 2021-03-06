package jp.try0.wicket.resource.bundle.examples;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.examples.panel.MyPanel;

@BundleResource(scope = HomePage.class, name = "HomePage.css")
public class HomePage extends WebPage {

	public HomePage(final PageParameters parameters) {
		super(parameters);
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		add(new MyPanel("myPanel"));
		add(new Link<Void>("toNoResPage") {
			{
				setBody(Model.of("To NoResourcePage"));
			}

			@Override
			public void onClick() {
				setResponsePage(NoResourcePage.class);
			}

		});
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "HomePage.css")));
	}

}
