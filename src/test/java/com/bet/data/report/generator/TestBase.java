package com.bet.data.report.generator;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;

public class TestBase {

  public String readFromFile(final String fileName) {
    String content = "";
    final InputStream contentAsStream = getClass().getResourceAsStream(fileName);
    try {
      content = IOUtils.toString(contentAsStream, "UTF-8");
    } catch (IOException e) {
      Assert.fail("Unable to read test file: " + e.getMessage());
    }
    return  content;
  }
}
