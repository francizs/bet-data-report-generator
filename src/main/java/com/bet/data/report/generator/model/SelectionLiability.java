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
@CsvRecord(separator = ",", generateHeaderColumns = true)
public class SelectionLiability {

  @Getter
  @Setter
  @DataField(pos = 1, columnName = "Selection Name")
  String selectionName;
  @Getter
  @Setter
  @DataField(pos = 2, columnName = "Currency")
  String currency;
  @Getter
  @Setter
  @DataField(pos = 3, columnName = "Num Bets")
  Integer numBets;
  @Getter
  @Setter
  @DataField(pos = 4, columnName = "Total Stakes", precision = 2, pattern = "00.00")
  BigDecimal totalStakes;
  @Getter
  @Setter
  @DataField(pos = 5, columnName = "Total Liability", precision = 2, pattern = "00.00")
  BigDecimal totalLiability;
}
