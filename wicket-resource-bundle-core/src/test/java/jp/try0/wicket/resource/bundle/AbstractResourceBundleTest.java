package jp.try0.wicket.resource.bundle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ResourceBundles;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractResourceBundleTest {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected WebApplication application;
	protected WicketTester tester;

	@BeforeEach
	public void setUp() {
		application = new WebApplication() {

			@Override
			public Class<? extends Page> getHomePage() {
				return null;
			}

			@Override
			protected ResourceBundles newResourceBundles(final ResourceReferenceRegistry registry) {
				return AbstractResourceBundleTest.this.newResourceBundles(registry);
			}
		};

		tester = new WicketTester(application);
	}

	protected ResourceBundles newResourceBundles(final ResourceReferenceRegistry registry) {
		return new BundleResourceMock(registry);
	}

	public static class BundleResourceMock extends ResourceBundles {

		public BundleResourceMock(ResourceReferenceRegistry registry) {
			super(registry);
		}

		@SuppressWarnings("unchecked")
		public Map<HeaderItem, HeaderItem> getProvidedResourcesToBundles() {

			try {
				Field privateStringField = ResourceBundles.class.getDeclaredField("providedResourcesToBundles");
				privateStringField.setAccessible(true);
				return (Map<HeaderItem, HeaderItem>) privateStringField.get(this);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static <T extends Behavior> List<T> findBehaviors(MarkupContainer component, Class<T> behaviorCls) {
		List<T> behaviors = new ArrayList<>();
		behaviors.addAll(component.getBehaviors(behaviorCls));
		component.visitChildren((c, v) -> behaviors.addAll(c.getBehaviors(behaviorCls)));

		return behaviors;
	}
}
