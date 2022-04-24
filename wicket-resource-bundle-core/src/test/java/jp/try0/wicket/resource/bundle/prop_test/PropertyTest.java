package jp.try0.wicket.resource.bundle.prop_test;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.ResourceReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.try0.wicket.resource.bundle.AbstractResourceBundleTest;
import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResourceManager;
import jp.try0.wicket.resource.bundle.BundleResources;

/**
 * {@link BundleResourceManager} Tests.
 * 
 * @author Ryo Tsunoda
 *
 */
public class PropertyTest extends AbstractResourceBundleTest {

	private static Logger logger = LoggerFactory.getLogger(PropertyTest.class);

	@BundleResources({ @BundleResource(name = "res1.css"), @BundleResource(name = "res1.js") })
	public static class TestComponent1 extends WebComponent {
		public TestComponent1(String id) {
			super(id);
		}
	}

	@BundleResources({ @BundleResource(name = "res2.css"), @BundleResource(name = "res2.js") })
	public static class TestComponent2 extends WebComponent {
		public TestComponent2(String id) {
			super(id);
		}
	}

	@Test
	public void setBundleResourceName() {
		BundleResourceManager manager = new BundleResourceManager(application);
		manager.setScanPackageName(getClass().getPackage().getName());
		manager.setBundleResourceName("setBundleResourceName");
		manager.register();

		// test created bundles
		BundleResourceMock bundles = (BundleResourceMock) application.getResourceBundles();
		List<HeaderItem> items = new ArrayList<>(bundles.getProvidedResourcesToBundles().values());
		for (HeaderItem bundle : items) {

			if (bundle instanceof JavaScriptReferenceHeaderItem) {
				JavaScriptReferenceHeaderItem item = (JavaScriptReferenceHeaderItem) bundle;
				ResourceReference resRef = item.getReference();
				Assertions.assertEquals("setBundleResourceName.js", resRef.getName());
				continue;
			}

			if (bundle instanceof CssReferenceHeaderItem) {
				CssReferenceHeaderItem item = (CssReferenceHeaderItem) bundle;
				ResourceReference resRef = item.getReference();
				Assertions.assertEquals("setBundleResourceName.css", resRef.getName());
				continue;
			}

			Assertions.fail();
		}
	}

	@Test
	public void setBundleResourceScope() {
		BundleResourceManager manager = new BundleResourceManager(application);
		manager.setScanPackageName(getClass().getPackage().getName());
		manager.setBundleResourceScope(getClass());
		manager.register();

		// test created bundles
		BundleResourceMock bundles = (BundleResourceMock) application.getResourceBundles();
		List<HeaderItem> items = new ArrayList<>(bundles.getProvidedResourcesToBundles().values());
		for (HeaderItem bundle : items) {

			if (bundle instanceof JavaScriptReferenceHeaderItem) {
				JavaScriptReferenceHeaderItem item = (JavaScriptReferenceHeaderItem) bundle;
				ResourceReference resRef = item.getReference();
				Assertions.assertEquals(getClass(), resRef.getScope());
				continue;
			}

			if (bundle instanceof CssReferenceHeaderItem) {
				CssReferenceHeaderItem item = (CssReferenceHeaderItem) bundle;
				ResourceReference resRef = item.getReference();
				Assertions.assertEquals(getClass(), resRef.getScope());
				continue;
			}

			Assertions.fail();
		}
	}

}
