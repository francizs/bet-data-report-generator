package com.bet.data.report.generator.sort;

import com.bet.data.report.generator.model.SelectionLiability;

import java.util.Comparator;

/**
 * Comparator class for sorting by name.
 *
 * @author francisz
 *
 */
public class SortByName implements Comparator<SelectionLiability>
{
  public int compare(SelectionLiability firstSelectionLiability, SelectionLiability secondSelectionLiability) {
    return secondSelectionLiability.getTotalLiability().compareTo(firstSelectionLiability.getTotalLiability());
  }
}