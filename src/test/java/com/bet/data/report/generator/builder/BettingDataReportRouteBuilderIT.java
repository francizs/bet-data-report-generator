package com.bet.data.report.generator.builder;

import com.bet.data.report.generator.Application;
import com.bet.data.report.generator.TestBase;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;


/**
 * Test of the implementation of the email pattern parametrized by Bindy annotated class.
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockEndpoints
@ContextConfiguration(classes = Application.class)
@TestPropertySource("classpath:application-test.properties")
public class BettingDataReportRouteBuilderIT extends TestBase {

  @EndpointInject("mock:stream:out")
  private MockEndpoint mockEndpoint;

  @Produce("file:{{java.io.tmpdir}}?fileName=dataset.csv&autoCreate=false")
  protected ProducerTemplate producer;

  /**
   * Test CSV String.
   * @throws InterruptedException mockEndpoint issue exception.
   */
  @Test
  public void testBettingDataReportRoute_fileWithBetDataPassed_expectedOutputReceivedInTarget()
          throws InterruptedException {

    String inputDataset = readFromFile("/input/dataset.csv");
    String expectedSelectionLiabilityReport = readFromFile("/expected/selection-liability-report.csv");
    String expectedTotalLiabilityReport = readFromFile("/expected/total-liability-report.csv");

    mockEndpoint.expectedMessageCount(2);

    producer.sendBody(inputDataset);

    mockEndpoint.assertIsSatisfied();

    Assert.assertEquals(expectedSelectionLiabilityReport,
            mockEndpoint.getExchanges().get(0).getIn().getBody(String.class));
    Assert.assertEquals(expectedTotalLiabilityReport,
            mockEndpoint.getExchanges().get(1).getIn().getBody(String.class));

  }
}

