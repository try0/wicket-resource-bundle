package jp.try0.wicket.resource.bundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link BundleResource} array.
 * 
 * @author Ryo Tsunoda
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BundleResources {

	/**
	 * Resources for register bundel.
	 * 
	 * @return Resources for register bundel
	 */
	BundleResource[] value();

}
