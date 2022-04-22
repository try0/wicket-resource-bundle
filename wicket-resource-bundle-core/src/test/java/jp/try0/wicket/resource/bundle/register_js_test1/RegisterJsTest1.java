package jp.try0.wicket.resource.bundle.register_js_test1;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.ResourceReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jp.try0.wicket.resource.bundle.AbstractResourceBundleTest;
import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResourceManager;

public class RegisterJsTest1 extends AbstractResourceBundleTest {

	@BundleResource(name = "TestComponent_registerResources1.js")
	public static class TestComponent_registerResources1 extends WebComponent {
		public TestComponent_registerResources1(String id) {
			super(id);
		}
	}

	@BundleResource(name = "TestComponent_registerResources2.js")
	public static class TestComponent_registerResources2 extends WebComponent {
		public TestComponent_registerResources2(String id) {
			super(id);
		}
	}

	@Test
	public void registerJsResources() {

		// register
		BundleResourceManager manager = new BundleResourceManager(application);
		manager.setScanPackageName(getClass().getPackage().getName());
		manager.register();

		// test lookup classes
		Assertions.assertEquals(2, manager.getResourceHolderClasses().size());
		Assertions.assertTrue(manager.getResourceHolderClasses().contains(TestComponent_registerResources1.class));
		Assertions.assertTrue(manager.getResourceHolderClasses().contains(TestComponent_registerResources2.class));

		// test created bundles
		BundleResourceMock bundles = (BundleResourceMock) application.getResourceBundles();
		Assertions.assertEquals(2, bundles.getProvidedResourcesToBundles().size());

		List<HeaderItem> items = new ArrayList<>(bundles.getProvidedResourcesToBundles().keySet());
		for (HeaderItem key : items) {

			Assertions.assertTrue(key instanceof JavaScriptReferenceHeaderItem);

			JavaScriptReferenceHeaderItem item = (JavaScriptReferenceHeaderItem) key;
			ResourceReference resRef = item.getReference();

			String className = "TestComponent_registerResources" + (items.indexOf(key) + 1);

			Assertions.assertEquals(className, resRef.getScope().getSimpleName());
			Assertions.assertEquals(className + ".js", resRef.getName());
		}

	}

}
