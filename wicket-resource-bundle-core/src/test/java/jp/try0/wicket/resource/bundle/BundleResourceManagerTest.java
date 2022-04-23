package jp.try0.wicket.resource.bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.WebComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.try0.wicket.resource.bundle.BundleResourceManager.DependencyComparator;
import jp.try0.wicket.resource.bundle.BundleResourceManager.ResourceDependency;

/**
 * {@link BundleResourceManager} Tests.
 * 
 * @author Ryo Tsunoda
 *
 */
public class BundleResourceManagerTest extends AbstractResourceBundleTest {

	private static Logger logger = LoggerFactory.getLogger(BundleResourceManagerTest.class);

	public static class TestComponent1 extends WebComponent {
		public TestComponent1(String id) {
			super(id);
		}
	}

	public static class TestComponent2 extends WebComponent {
		public TestComponent2(String id) {
			super(id);
		}
	}

	public static class TestComponent3 extends WebComponent {
		public TestComponent3(String id) {
			super(id);
		}
	}

	public static class TestComponent4 extends WebComponent {
		public TestComponent4(String id) {
			super(id);
		}
	}

	@Test
	public void sortDependency() {

		List<ResourceDependency> classResources = new ArrayList<>();
		ResourceDependency rd1 = new ResourceDependency(TestComponent1.class);
		classResources.add(rd1);

		ResourceDependency rd2 = new ResourceDependency(TestComponent2.class);
		classResources.add(rd2);
		rd1.dependencies.add(rd2);

		ResourceDependency rd3 = new ResourceDependency(TestComponent3.class);
		classResources.add(rd3);
		rd2.dependencies.add(rd3);

		ResourceDependency rd4 = new ResourceDependency(TestComponent4.class);
		classResources.add(rd4);
		rd3.dependencies.add(rd4);

		DependencyComparator comparator = new DependencyComparator();
		comparator.analyze(classResources);

		{
			List<Class<?>> classes = Arrays.asList(TestComponent1.class, TestComponent2.class, TestComponent3.class,
					TestComponent4.class);

			classes.sort(comparator);

			Assertions.assertEquals(0, classes.indexOf(TestComponent4.class));
			Assertions.assertEquals(1, classes.indexOf(TestComponent3.class));
			Assertions.assertEquals(2, classes.indexOf(TestComponent2.class));
			Assertions.assertEquals(3, classes.indexOf(TestComponent1.class));
			
			classes.sort(comparator);
			
			Assertions.assertEquals(0, classes.indexOf(TestComponent4.class));
			Assertions.assertEquals(1, classes.indexOf(TestComponent3.class));
			Assertions.assertEquals(2, classes.indexOf(TestComponent2.class));
			Assertions.assertEquals(3, classes.indexOf(TestComponent1.class));
		}

		{
			List<Class<?>> classes = Arrays.asList(TestComponent2.class, TestComponent1.class, TestComponent4.class,
					TestComponent3.class);

			classes.sort(comparator);

			Assertions.assertEquals(0, classes.indexOf(TestComponent4.class));
			Assertions.assertEquals(1, classes.indexOf(TestComponent3.class));
			Assertions.assertEquals(2, classes.indexOf(TestComponent2.class));
			Assertions.assertEquals(3, classes.indexOf(TestComponent1.class));
		}

		{
			List<Class<?>> classes = Arrays.asList(TestComponent4.class, TestComponent1.class);

			classes.sort(comparator);

			Assertions.assertEquals(0, classes.indexOf(TestComponent4.class));
			Assertions.assertEquals(1, classes.indexOf(TestComponent1.class));
		}
	}

	@Test
	public void throwExceptionCircularDependency() {

		List<ResourceDependency> classResources = new ArrayList<>();
		ResourceDependency rd1 = new ResourceDependency(TestComponent1.class);
		classResources.add(rd1);

		ResourceDependency rd2 = new ResourceDependency(TestComponent2.class);
		classResources.add(rd2);
		rd1.dependencies.add(rd2);

		ResourceDependency rd3 = new ResourceDependency(TestComponent3.class);
		classResources.add(rd3);
		rd2.dependencies.add(rd3);

		ResourceDependency rd4 = new ResourceDependency(TestComponent4.class);
		classResources.add(rd4);
		rd3.dependencies.add(rd4);

		// circular dependency
		rd4.dependencies.add(rd1);

		try {
			DependencyComparator comparator = new DependencyComparator();
			comparator.analyze(classResources);

			Assertions.fail();
		} catch (Exception e) {
			String nl = System.getProperty("line.separator");

			StringBuilder error = new StringBuilder();
			error.append("Circular dependency." + nl);
			error.append("TestComponent1" + nl);
			error.append(" TestComponent2" + nl);
			error.append("  TestComponent3" + nl);
			error.append("   TestComponent4" + nl);
			error.append("    TestComponent1" + nl);
			error.append("    ↑ Cause component class.");

			Assertions.assertTrue(e.getMessage().contains(error.toString()));
			Assertions.assertTrue(e instanceof RuntimeException);
		}
	}
	
}
