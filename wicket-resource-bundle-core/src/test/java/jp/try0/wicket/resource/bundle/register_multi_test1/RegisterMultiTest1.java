package jp.try0.wicket.resource.bundle.register_multi_test1;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.ResourceReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jp.try0.wicket.resource.bundle.AbstractResourceBundleTest;
import jp.try0.wicket.resource.bundle.BundleResource;
import jp.try0.wicket.resource.bundle.BundleResourceManager;
import jp.try0.wicket.resource.bundle.BundleResources;

public class RegisterMultiTest1 extends AbstractResourceBundleTest {

	@BundleResources({ @BundleResource(name = "TestComponent_registerResources1.js"),
			@BundleResource(name = "TestComponent_registerResources1.css") })
	public static class TestComponent_registerResources1 extends WebComponent {
		public TestComponent_registerResources1(String id) {
			super(id);
		}
	}

	@BundleResources({ @BundleResource(name = "TestComponent_registerResources2.js"),
			@BundleResource(name = "TestComponent_registerResources2.css") })
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
		Assertions.assertTrue(manager.isCssHolderClass(TestComponent_registerResources1.class));
		Assertions.assertTrue(manager.isCssHolderClass(TestComponent_registerResources2.class));
		Assertions.assertTrue(manager.isJsHolderClass(TestComponent_registerResources1.class));
		Assertions.assertTrue(manager.isJsHolderClass(TestComponent_registerResources2.class));

		Assertions.assertTrue(manager.getResourceHolderClasses().contains(TestComponent_registerResources1.class));
		Assertions.assertTrue(manager.getResourceHolderClasses().contains(TestComponent_registerResources2.class));

		// test created bundles
		BundleResourceMock bundles = (BundleResourceMock) application.getResourceBundles();
		Assertions.assertEquals(4, bundles.getProvidedResourcesToBundles().size());

		List<HeaderItem> items = new ArrayList<>(bundles.getProvidedResourcesToBundles().keySet());
		for (HeaderItem key : items) {

			if (key instanceof JavaScriptReferenceHeaderItem) {
				JavaScriptReferenceHeaderItem item = (JavaScriptReferenceHeaderItem) key;
				ResourceReference resRef = item.getReference();
				Assertions.assertEquals(resRef.getScope().getSimpleName() + ".js", resRef.getName());
				continue;
			}

			if (key instanceof CssReferenceHeaderItem) {
				CssReferenceHeaderItem item = (CssReferenceHeaderItem) key;
				ResourceReference resRef = item.getReference();
				Assertions.assertEquals(resRef.getScope().getSimpleName() + ".css", resRef.getName());
				continue;
			}

			Assertions.fail();

		}

	}

}
