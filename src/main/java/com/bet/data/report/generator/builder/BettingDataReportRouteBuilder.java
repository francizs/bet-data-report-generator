package com.bet.data.report.generator.builder;

import com.bet.data.report.generator.annotation.SourceUri;
import com.bet.data.report.generator.annotation.TargetUri;
import com.bet.data.report.generator.exception.DataFormatSetupException;
import com.bet.data.report.generator.model.CsvDataset;
import com.bet.data.report.generator.service.BidDataProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.support.MessageHelper;
import org.apache.camel.support.processor.DefaultExchangeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import java.util.Objects;

/**
 * Betting data report RouteBuilder.
 *
 * @author francisz
 *
 */
public class BettingDataReportRouteBuilder<S, R1, R2> extends RouteBuilder {

  public BettingDataReportRouteBuilder() {

  }

  private DataFormat sourceDataFormat;
  private DataFormat firstReportTargetDataFormat;
  private DataFormat secondReportTargetDataFormat;

  SourceUri sourceUri;
  TargetUri targetUri;

  /**
   * Logger.
   */
  protected static final Logger LOGGER = LoggerFactory.getLogger("mainLogger");

  @Autowired
  BidDataProcessor bidDataProcessor;

  @Override
  public void configure() throws DataFormatSetupException {

    Class<?> typeOfS = Objects.requireNonNull(GenericTypeResolver
            .resolveTypeArguments(getClass(), BettingDataReportRouteBuilder.class))[0];
    Class<?> typeOfR1 = Objects.requireNonNull(GenericTypeResolver
            .resolveTypeArguments(getClass(), BettingDataReportRouteBuilder.class))[1];
    Class<?> typeOfR2 = Objects.requireNonNull(GenericTypeResolver
            .resolveTypeArguments(getClass(), BettingDataReportRouteBuilder.class))[2];

    if (typeOfS.isInstance(new CsvDataset())) {
      sourceDataFormat = new BindyCsvDataFormat(typeOfS);
      firstReportTargetDataFormat = new BindyCsvDataFormat(typeOfR1);
      secondReportTargetDataFormat = new BindyCsvDataFormat(typeOfR2);
    } else {
      throw new DataFormatSetupException("Failed to setup a DataFormat.");
    }

    configureExceptions();

    configureSource();

    configureProcess();

    configureTarget();

  }

  private void configureExceptions(){

  }

  private void configureSource() {
    boolean isSourceUriAnnotationPresent = this.getClass()
            .isAnnotationPresent(SourceUri.class);

    if (isSourceUriAnnotationPresent) {
      sourceUri = getClass().getAnnotation(SourceUri.class);
      final String sourceFromUri = sourceUri.sourceFrom();
      final String sourceToUri = sourceUri.sourceTo();

      from(sourceFromUri)
              .routeId("sourceRoute")
              .process(this::enrichExchange)
              .wireTap("direct:logOriginalMessage")
              .unmarshal(sourceDataFormat)
              .to(sourceToUri);

      from("direct:logOriginalMessage")
              .routeId("logOriginalMessageRoute")
              .doTry()
              .process(exchange -> LOGGER.info(exchange.getIn().getBody(String.class)))
              .doCatch(Throwable.class)
              .process(this::loggerForException);
    }
  }

  private void configureProcess() {

    boolean isTargetUriAnnotationPresent = this.getClass()
            .isAnnotationPresent(TargetUri.class);

    if (isTargetUriAnnotationPresent) {
      targetUri = getClass().getAnnotation(TargetUri.class);
      final String targetFromUri = targetUri.targetFrom();

      final String sourceToUri = sourceUri.sourceTo();
      from(sourceToUri)
              .routeId("processRoute")
              .process(exchange -> bidDataProcessor.processExchange(exchange))
              .to(targetFromUri);
    }
  }

  private void configureTarget(){
    boolean isTargetUriAnnotationPresent = this.getClass()
            .isAnnotationPresent(TargetUri.class);

    if (isTargetUriAnnotationPresent) {

      if (targetUri != null) {

        from(targetUri.targetFrom())
                .routeId("targetRoute")
                .wireTap("direct:logTargetMessage")
                .split(body())
                .choice()
                  .when(exchangeProperty(Exchange.SPLIT_INDEX).isEqualTo(0))
                    .marshal(firstReportTargetDataFormat)
                  .endChoice()
                .end()
                .choice()
                  .when(exchangeProperty(Exchange.SPLIT_INDEX).isEqualTo(1))
                    .marshal(secondReportTargetDataFormat)
                  .endChoice()
                .end()
                .process(exchange -> bidDataProcessor.formatOutput(exchange))
                .to(targetUri.targetTo());

        from("direct:logTargetMessage")
                .routeId("logTargetMessageRoute")
                .doTry()
                .process(exchange -> LOGGER.info(exchange.getIn().getBody(String.class)))
                .doCatch(Throwable.class)
                .process(this::loggerForException);
      }
    }
  }

  private void loggerForException(Exchange exchange) {

    String msgHist = MessageHelper.dumpMessageHistoryStacktrace(exchange,
            new DefaultExchangeFormatter(), true);
    LOGGER.error(msgHist, exchange);

    LOGGER.error("Following exception thrown: " +
            exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class));
  }

  private void enrichExchange(Exchange exchange) {
    // Enrich exchange, for example from as REST API endpoint.
  }
}
