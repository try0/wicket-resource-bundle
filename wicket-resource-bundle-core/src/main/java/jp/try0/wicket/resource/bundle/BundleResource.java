package jp.try0.wicket.resource.bundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.Component;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Marker of components that has resource for register bundle.
 * 
 * @author Ryo Tsunoda
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BundleResource {

	/**
	 * {@link ResourceReference#getScope()}
	 * 
	 * @return resource scope
	 */
	Class<? extends Component> scope() default Component.class;

	/**
	 * {@link ResourceReference#getName()}
	 * 
	 * @return resource name
	 */
	String name();

	/**
	 * Resource dependency.
	 * 
	 * @return resource dependencies
	 */
	Class<? extends Component>[] dependencies() default {};

}
