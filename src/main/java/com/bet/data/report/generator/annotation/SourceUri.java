package com.bet.data.report.generator.annotation;

import java.lang.annotation.*;

/**
 * Source URI specifier.
 *
 * @author francisz
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SourceUri {

  /**
   * @return sourceUri
   */
  String sourceFrom();

  /**
   * @return Process route
   */
  String sourceTo() default "direct:process";

}
