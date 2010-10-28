/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.conversion;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.time.calendar.LocalDate;

import org.junit.Test;

import com.opengamma.util.timeseries.DoubleTimeSeries;
import com.opengamma.util.timeseries.localdate.ArrayLocalDateDoubleTimeSeries;

/**
 * 
 */
public class TimeSeriesConverterTest {
  
  private final TimeSeriesConverter _converter = new TimeSeriesConverter();
  
  @Test
  public void convertEmpty() {
    Map<String, Double> expected = new HashMap<String, Double>();
    
    Map<String, Double> actual = _converter.convert("Foo", new ArrayLocalDateDoubleTimeSeries());
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void convertNonEmpty() {
    Map<String, Double> expected = new HashMap<String, Double>();
    expected.put("Foo[2005-04-04]", 5.5);
    expected.put("Foo[2005-04-05]", 6.6);
    
    Map<String, Double> actual = _converter.convert("Foo", 
        new ArrayLocalDateDoubleTimeSeries(
            new LocalDate[] { LocalDate.of(2005, 4, 4), LocalDate.of(2005, 4, 5) },
            new double[] { 5.5, 6.6 }));
    
    assertEquals(expected, actual);
  }

  @Test
  public void getConvertedClass() {
    assertEquals(DoubleTimeSeries.class, _converter.getConvertedClass());  
  }

}
