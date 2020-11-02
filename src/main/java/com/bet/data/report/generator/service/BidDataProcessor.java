package com.bet.data.report.generator.service;

import org.apache.camel.Exchange;

/**
 * BidDataProcessor class to be extended by other types of processor services, e.g. for JSON processor service.
 *
 * @author francisz
 *
 */
public abstract class BidDataProcessor {
  public abstract void processExchange(Exchange exchange);
  public abstract void formatOutput(Exchange exchange);
}
