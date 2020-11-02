package com.bet.data.report.generator.service;

import com.bet.data.report.generator.model.CsvDataset;
import com.bet.data.report.generator.model.SelectionLiability;
import com.bet.data.report.generator.model.TotalLiability;
import com.bet.data.report.generator.sort.SortByCurrency;
import com.bet.data.report.generator.sort.SortByName;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Processor service for CSV.
 *
 * @author francisz
 *
 */
@Service
public class CsvBidDataProcessorService extends BidDataProcessor {

  List<SelectionLiability> selectionLiabilityList;
  List<TotalLiability> totalLiabilityArrayList;
  List<List<?>> resultList;

  @Override
  public void processExchange(Exchange exchange) {


    @SuppressWarnings("unchecked")
    List<CsvDataset> csvDatasetList = exchange.getIn().getBody(ArrayList.class);
    createSelectionLiabilityList(csvDatasetList);
    createTotalLiabilityList();

    sortSelectionLiabilityList(selectionLiabilityList);

    resultList = new ArrayList<>();
    resultList.add(selectionLiabilityList);
    resultList.add(totalLiabilityArrayList);
    exchange.getIn().setBody(resultList);
  }

  @Override
  public void formatOutput(Exchange exchange) {

  }

  private void createSelectionLiabilityList(List<CsvDataset> csvDatasetList) {
    selectionLiabilityList = new ArrayList<>();
    Map<String, List<CsvDataset>> currencySelectionNameMap = new HashMap<>();
    String currencyNameKey;

    for (CsvDataset csvDataset : csvDatasetList) {

      currencyNameKey = csvDataset.getCurrency() + csvDataset.getSelectionName();

      if (currencySelectionNameMap.containsKey(currencyNameKey)) {
        List<CsvDataset> csvDatasetFromMapList = currencySelectionNameMap.get(currencyNameKey);
        csvDatasetFromMapList.add(csvDataset);
      } else {
        List<CsvDataset> selectionLiabilityList = new ArrayList<>();
        selectionLiabilityList.add(csvDataset);
        currencySelectionNameMap.put(currencyNameKey, selectionLiabilityList);
      }
    }

    for (String key : currencySelectionNameMap.keySet()) {

      String currency;
      BigDecimal totalStakes = BigDecimal.ZERO;
      BigDecimal totalLiability = BigDecimal.ZERO;
      String selectionName;
      int numBets = 0;

      List<CsvDataset> csvDatasetFromMapList = currencySelectionNameMap.get(key);
      CsvDataset firstCsvDataset = csvDatasetFromMapList.get(0);
      selectionName = firstCsvDataset.getSelectionName();
      currency = firstCsvDataset.getCurrency();

      for (CsvDataset csvDataset : csvDatasetFromMapList) {
        numBets++;
        totalStakes = totalStakes.add(csvDataset.getStake());
        totalLiability = totalLiability.add(((csvDataset.getStake()).multiply(csvDataset.getPrice())));
      }

      selectionLiabilityList.add(new SelectionLiability(selectionName, currency, numBets, totalStakes, totalLiability));
    }
  }

  private void createTotalLiabilityList() {
    totalLiabilityArrayList = new ArrayList<>();
    Map<String, List<SelectionLiability>> currencyLiabilityListMap = new HashMap<>();

    for (SelectionLiability selectionLiability : selectionLiabilityList) {
      String currencyKey = selectionLiability.getCurrency();
      if (currencyLiabilityListMap.containsKey(currencyKey)) {
        List<SelectionLiability> selectionLiabilityList = currencyLiabilityListMap.get(currencyKey);
        selectionLiabilityList.add(selectionLiability);
      } else {
        List<SelectionLiability> selectionLiabilityList = new ArrayList<>();
        selectionLiabilityList.add(selectionLiability);
        currencyLiabilityListMap.put(currencyKey, selectionLiabilityList);
      }
    }

    for (String currencyKey : currencyLiabilityListMap.keySet()) {
      int noOfBets = 0;
      BigDecimal totalStakes = BigDecimal.ZERO;
      BigDecimal totalLiability = BigDecimal.ZERO;

      for (SelectionLiability selectionLiability : currencyLiabilityListMap.get(currencyKey)) {
        noOfBets += selectionLiability.getNumBets();
        totalStakes = totalStakes.add(selectionLiability.getTotalStakes());
        totalLiability = totalLiability.add(selectionLiability.getTotalLiability());
      }

      totalLiabilityArrayList.add(new TotalLiability(currencyKey, noOfBets, totalStakes,
              totalLiability));
    }
  }

  private void sortSelectionLiabilityList(List<SelectionLiability> selectionLiabilityList) {
    selectionLiabilityList.sort(new SortByName());
    selectionLiabilityList.sort(new SortByCurrency());
  }
}
