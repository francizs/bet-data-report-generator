package com.bet.data.report.generator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

/**
 * Dataset POJO for Camel's Bindy data format
 *
 * @author francisz
 *
 */
@AllArgsConstructor
@ToString
@CsvRecord(separator = ",", skipFirstLine = true)
public class CsvDataset {

  public CsvDataset() {
    // Empty constructor
  }

  @Getter
  @Setter
  @DataField(pos = 1)
  private String betId;
  @Getter
  @Setter
  @DataField(pos = 2)
  private String betTimestamp;
  @Getter
  @Setter
  @DataField(pos = 3)
  private String selectionId;
  @Getter
  @Setter
  @DataField(pos = 4)
  private String selectionName;
  @Getter
  @Setter
  @DataField(pos = 5, precision = 2, pattern = "00.00")
  private BigDecimal stake;
  @Getter
  @Setter
  @DataField(pos = 6, precision = 2, pattern = "00.00")
  private BigDecimal price;
  @Getter
  @Setter
  @DataField(pos = 7)
  private String currency;
}
