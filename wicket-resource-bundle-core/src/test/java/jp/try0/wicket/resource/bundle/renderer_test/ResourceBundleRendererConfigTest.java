package jp.try0.wicket.resource.bundle.renderer_test;

import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.protocol.http.WebApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jp.try0.wicket.resource.bundle.AbstractResourceBundleTest;
import jp.try0.wicket.resource.bundle.BundleResourceAutoAppender;
import jp.try0.wicket.resource.bundle.BundleResourceManager;
import jp.try0.wicket.resource.bundle.BundleResourceManager.ResourceBundleRendererConfig;
import jp.try0.wicket.resource.bundle.BundleResourceRenderer;
import jp.try0.wicket.resource.bundle.renderer_test.ResourceBundleRendererConfigTestPage.ResourceRendererTestPage_NoResource;

/**
 * {@link ResourceBundleRendererConfig} Tests.
 * 
 * @author Ryo Tsunoda
 *
 */
public class ResourceBundleRendererConfigTest extends AbstractResourceBundleTest {

	@Test
	public void MANUAL_RENDERING() {

		// register
		BundleResourceManager manager = new BundleResourceManager(application);
		manager.setScanPackageName(getClass().getPackage().getName());
		manager.setResourceBundleRendererConfig(ResourceBundleRendererConfig.MANUAL_RENDERING);
		manager.register();

		WebApplication app = tester.getApplication();

		boolean hasAppender = false;
		for (IComponentInstantiationListener listener : app.getComponentInstantiationListeners()) {
			if (listener instanceof BundleResourceAutoAppender) {
				hasAppender = true;
				break;
			}
		}

		Assertions.assertFalse(hasAppender);

		ResourceBundleRendererConfigTestPage page = tester.startPage(ResourceBundleRendererConfigTestPage.class);
		tester.assertRenderedPage(ResourceBundleRendererConfigTestPage.class);

		boolean hasRenderer = !findBehaviors(page, BundleResourceRenderer.class).isEmpty();
		Assertions.assertFalse(hasRenderer);
	}

	@Test
	public void ALL_PAGE() {

		// register
		BundleResourceManager manager = new BundleResourceManager(application);
		manager.setScanPackageName(getClass().getPackage().getName());
		manager.setResourceBundleRendererConfig(ResourceBundleRendererConfig.ALL_PAGE);
		manager.register();

		WebApplication app = tester.getApplication();

		boolean hasAppender = false;
		for (IComponentInstantiationListener listener : app.getComponentInstantiationListeners()) {
			if (listener instanceof BundleResourceAutoAppender) {
				hasAppender = true;
				break;
			}
		}

		Assertions.assertTrue(hasAppender);

		{
			ResourceBundleRendererConfigTestPage page = tester.startPage(ResourceBundleRendererConfigTestPage.class);
			tester.assertRenderedPage(ResourceBundleRendererConfigTestPage.class);

			boolean hasRenderer = !findBehaviors(page, BundleResourceRenderer.class).isEmpty();
			Assertions.assertTrue(hasRenderer);
		}

		{
			ResourceRendererTestPage_NoResource page = tester.startPage(ResourceRendererTestPage_NoResource.class);
			tester.assertRenderedPage(ResourceRendererTestPage_NoResource.class);

			boolean hasRenderer = !findBehaviors(page, BundleResourceRenderer.class).isEmpty();
			Assertions.assertTrue(hasRenderer);
		}
	}

	@Test
	public void ONLY_BUNDLE_RESOURCE_HOLDER() {

		// register
		BundleResourceManager manager = new BundleResourceManager(application);
		manager.setScanPackageName(getClass().getPackage().getName());
		manager.setResourceBundleRendererConfig(ResourceBundleRendererConfig.ONLY_BUNDLE_RESOURCE_HOLDER);
		manager.register();

		WebApplication app = tester.getApplication();

		boolean hasAppender = false;
		for (IComponentInstantiationListener listener : app.getComponentInstantiationListeners()) {
			if (listener instanceof BundleResourceAutoAppender) {
				hasAppender = true;
				break;
			}
		}

		Assertions.assertTrue(hasAppender);

		{
			ResourceBundleRendererConfigTestPage page = tester.startPage(ResourceBundleRendererConfigTestPage.class);
			tester.assertRenderedPage(ResourceBundleRendererConfigTestPage.class);

			boolean hasRenderer = !findBehaviors(page, BundleResourceRenderer.class).isEmpty();
			Assertions.assertTrue(hasRenderer);
		}

		{
			ResourceRendererTestPage_NoResource page = tester.startPage(ResourceRendererTestPage_NoResource.class);
			tester.assertRenderedPage(ResourceRendererTestPage_NoResource.class);

			boolean hasRenderer = !findBehaviors(page, BundleResourceRenderer.class).isEmpty();
			Assertions.assertFalse(hasRenderer);
		}
	}

}
