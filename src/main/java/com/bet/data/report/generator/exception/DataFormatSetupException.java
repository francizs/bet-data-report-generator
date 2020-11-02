package com.bet.data.report.generator.exception;


/**
 * DataFormat configuration exception.
 *
 * @author francisz
 *
 */
public class DataFormatSetupException extends Exception {
  /**
   * The serial version UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new exception with the errorMessage. Delegates it to the super
   * class.
   *
   * @see Exception
   * @param errorMessage
   *          the errorMessage
   */
  public DataFormatSetupException(final String errorMessage) {
    super(errorMessage);
  }

}
