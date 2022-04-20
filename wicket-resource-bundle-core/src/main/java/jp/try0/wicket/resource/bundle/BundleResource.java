package jp.try0.wicket.resource.bundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.wicket.Component;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Marker.
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
	 * @return
	 */
	Class<? extends Component> scope() default Component.class;

	/**
	 * {@link CssResourceReference#getName()}
	 * 
	 * @return
	 */
	String name();

}
