package com.bet.data.report.generator;

import com.bet.data.report.generator.annotation.SourceUri;
import com.bet.data.report.generator.annotation.TargetUri;
import com.bet.data.report.generator.builder.BettingDataReportRouteBuilder;
import com.bet.data.report.generator.model.CsvDataset;
import com.bet.data.report.generator.model.SelectionLiability;
import com.bet.data.report.generator.model.TotalLiability;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class.
 *
 * @author francisz
 *
 */

@SpringBootApplication
@SourceUri(sourceFrom = "file:{{java.io.tmpdir}}?fileName=dataset.csv&autoCreate=false")
@TargetUri(targetTo = "stream:out")
public class Application extends BettingDataReportRouteBuilder<CsvDataset, SelectionLiability, TotalLiability> {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
