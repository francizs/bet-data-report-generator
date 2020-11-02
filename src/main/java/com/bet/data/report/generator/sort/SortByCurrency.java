package com.bet.data.report.generator.sort;

import com.bet.data.report.generator.model.SelectionLiability;

import java.util.Comparator;

/**
 * Comparator class for sorting by currency.
 *
 * @author francisz
 *
 */
public class SortByCurrency implements Comparator<SelectionLiability>
{
  public int compare(SelectionLiability firstSelectionLiability, SelectionLiability secondSelectionLiability) {
    return secondSelectionLiability.getCurrency().compareTo(firstSelectionLiability.getCurrency());
  }
}