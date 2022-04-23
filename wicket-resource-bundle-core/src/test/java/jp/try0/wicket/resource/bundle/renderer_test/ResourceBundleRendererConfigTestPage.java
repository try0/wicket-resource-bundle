package jp.try0.wicket.resource.bundle.renderer_test;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResources;

/**
 * Mock page for {@link ResourceBundleRendererConfigTest}
 * 
 * @author Ryo Tsunoda
 *
 */
public class ResourceBundleRendererConfigTestPage extends WebPage implements IMarkupResourceStreamProvider {

	public ResourceBundleRendererConfigTestPage(final PageParameters parameters) {
		super(parameters);

		add(new TestComponent_registerResources1("p1"));
		add(new TestComponent_registerResources1("p2"));
	}

	private String markup() {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html><html>");
		sb.append("<head>");
		sb.append("<title>ResourceRendererTestPage</title>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append(
				"<h1>ResourceRendererTestPage</h1>" + "<div wicket:id=\"p1\"></div>" + "<div wicket:id=\"p2\"></div>");
		sb.append("</body>");
		sb.append("</html>");

		return sb.toString();
	}

	@Override
	public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
		StringResourceStream resourceStream = new StringResourceStream(markup());
		return resourceStream;
	}

//	@Override
//	public Markup getAssociatedMarkup() {
//		return Markup.of(markup());
//	}

	@BundleResource(scope = ResourceBundleRendererConfigTestPage.class, name = "TestComponent_registerResources1.css")
	public static class TestComponent_registerResources1 extends Panel implements IMarkupResourceStreamProvider {
		public TestComponent_registerResources1(String id) {
			super(id);
		}

		private String markup() {
			return "<wicket:panel><div>TestComponent_registerResources1</div></wicket:panel>";
		}

		@Override
		public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
			StringResourceStream resourceStream = new StringResourceStream(markup());
			return resourceStream;
		}

//		@Override
//		public Markup getAssociatedMarkup() {
//			return Markup.of(markup());
//		}
	}

	@BundleResources({
			@BundleResource(scope = ResourceBundleRendererConfigTestPage.class, name = "TestComponent_registerResources2.js"),
			@BundleResource(scope = ResourceBundleRendererConfigTestPage.class, name = "TestComponent_registerResources2.css") })
	public static class TestComponent_registerResources2 extends Panel implements IMarkupResourceStreamProvider {
		public TestComponent_registerResources2(String id) {
			super(id);
		}

		private String markup() {
			return "<wicket:panel><div>TestComponent_registerResources2</div></wicket:panel>";
		}

		@Override
		public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
			StringResourceStream resourceStream = new StringResourceStream(markup());
			return resourceStream;
		}

//		@Override
//		public Markup getAssociatedMarkup() {
//			return Markup.of(markup());
//		}
	}

	public static class ResourceRendererTestPage_NoResource extends WebPage implements IMarkupResourceStreamProvider {
		public ResourceRendererTestPage_NoResource(final PageParameters parameters) {
			super(parameters);
		}

		private String markup() {
			StringBuilder sb = new StringBuilder();
			sb.append("<!DOCTYPE html><html>");
			sb.append("<head>");
			sb.append("<title>ResourceRendererTestPage_NoResource</title>");
			sb.append("</head>");
			sb.append("<body>");
			sb.append("<h1>ResourceRendererTestPage_NoResource</h1>");
			sb.append("</body>");
			sb.append("</html>");

			return sb.toString();
		}

		@Override
		public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
			StringResourceStream resourceStream = new StringResourceStream(markup());
			return resourceStream;
		}

//		@Override
//		public Markup getAssociatedMarkup() {
//			return Markup.of(markup());
//		}
	}

}