package com.bet.data.report.generator.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Target URI specifier.
 *
 * @author francisz
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TargetUri {

  /**
   * @return targetUri
   */
  String targetTo();

  /**
   * @return sourceUri
   */
  String targetFrom() default "direct:target";

}
