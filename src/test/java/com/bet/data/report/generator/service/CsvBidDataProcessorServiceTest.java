package com.bet.data.report.generator.service;

import com.bet.data.report.generator.TestBase;
import com.bet.data.report.generator.model.CsvDataset;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.impl.engine.HashMapHeadersMapFactory;
import org.apache.camel.support.DefaultExchange;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvBidDataProcessorServiceTest extends TestBase {

  @Test
  public void testCsvBidDataProcessorService_createSelectionLiabilityAndTotalList_expectedReportsCreated()
          throws Exception {

    // Setup
    CamelContext camelContext = mock(CamelContext.class);
    when(camelContext.getHeadersMapFactory()).thenReturn(new HashMapHeadersMapFactory());

    CsvBidDataProcessorService csvBidDataProcessorService = new CsvBidDataProcessorService();

    Exchange exchange = new DefaultExchange(camelContext);
    exchange.getIn().setHeader("CamelCharsetName", "UTF-8");

    String inputDataset = readFromFile("/input/dataset.csv");
    InputStream inputStream = new ByteArrayInputStream(inputDataset.getBytes());

    BindyCsvDataFormat bindyCsvDataFormat = new BindyCsvDataFormat(CsvDataset.class);

    String expectedSelectionLiabilitiesProcessed = readFromFile("/expected/selection-liabilities-processed.txt");
    String expectedTotalLiabilitiesProcessed = readFromFile("/expected/total-liabilities-processed.txt");

    // Act
    exchange.getIn().setBody(bindyCsvDataFormat.unmarshal(exchange, inputStream));
    csvBidDataProcessorService.processExchange(exchange);

    // Assert
    ArrayList selectionLiabilityList = (ArrayList)exchange.getIn().getBody(ArrayList.class).get(0);
    ArrayList totalLiabilityList = (ArrayList)exchange.getIn().getBody(ArrayList.class).get(1);

    String actualSelectionLiabilities = Arrays.toString(selectionLiabilityList.toArray());
    String actualTotalLiabilities = Arrays.toString(totalLiabilityList.toArray());

    Assert.assertEquals(expectedSelectionLiabilitiesProcessed, actualSelectionLiabilities);
    Assert.assertEquals(expectedTotalLiabilitiesProcessed, actualTotalLiabilities);
  }

  @Test
  public void testCsvBidDataProcessorService_createSelectionLiabilityList_expectedListCreated()
          throws Exception {

    // Setup
    CamelContext camelContext = mock(CamelContext.class);
    when(camelContext.getHeadersMapFactory()).thenReturn(new HashMapHeadersMapFactory());

    CsvBidDataProcessorService csvBidDataProcessorService = new CsvBidDataProcessorService();

    Exchange exchange = new DefaultExchange(camelContext);
    exchange.getIn().setHeader("CamelCharsetName", "UTF-8");

    String inputDataset = readFromFile("/input/dataset20.csv");
    InputStream inputStream = new ByteArrayInputStream(inputDataset.getBytes());

    BindyCsvDataFormat bindyCsvDataFormat = new BindyCsvDataFormat(CsvDataset.class);

    String expectedSelectionLiabilitiesProcessed = readFromFile("/expected/selection-liabilities-processed-20.txt");
    String expectedTotalLiabilitiesProcessed = readFromFile("/expected/total-liabilities-processed-20.txt");

    // Act
    exchange.getIn().setBody(bindyCsvDataFormat.unmarshal(exchange, inputStream));
    csvBidDataProcessorService.processExchange(exchange);

    // Assert
    ArrayList selectionLiabilityList = (ArrayList)exchange.getIn().getBody(ArrayList.class).get(0);
    ArrayList totalLiabilityList = (ArrayList)exchange.getIn().getBody(ArrayList.class).get(1);

    String actualSelectionLiabilities = Arrays.toString(selectionLiabilityList.toArray());
    String actualTotalLiabilities = Arrays.toString(totalLiabilityList.toArray());

    Assert.assertEquals(expectedSelectionLiabilitiesProcessed, actualSelectionLiabilities);
    Assert.assertEquals(expectedTotalLiabilitiesProcessed, actualTotalLiabilities);
  }

}
