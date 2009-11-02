/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.math.regression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.opengamma.math.function.Function2D;

/**
 * 
 * @author emcleod
 */
public class LeastSquaresRegressionResultTest {
  private static final double EPS = 1e-12;

  @Test(expected = IllegalArgumentException.class)
  public void testInputs() {
    new LeastSquaresRegressionResult(null);
  }

  @Test
  public void testPredictedValue() {
    final double beta0 = 3.9;
    final double beta1 = -1.4;
    final double beta2 = 4.6;
    final Function2D<Double, Double> f1 = new Function2D<Double, Double>() {

      @Override
      public Double evaluate(final Double x1, final Double x2) {
        return x1 * beta1 + x2 * beta2;
      }

    };
    final Function2D<Double, Double> f2 = new Function2D<Double, Double>() {

      @Override
      public Double evaluate(final Double x1, final Double x2) {
        return beta0 + x1 * beta1 + x2 * beta2;
      }

    };
    final int n = 100;
    final Double[][] x = new Double[n][2];
    final Double[] y1 = new Double[n];
    final Double[] y2 = new Double[n];
    for (int i = 0; i < n; i++) {
      x[i][0] = Math.random();
      x[i][1] = Math.random();
      y1[i] = f1.evaluate(x[i][0], x[i][1]);
      y2[i] = f2.evaluate(x[i][0], x[i][1]);
    }
    final LeastSquaresRegression regression = new OrdinaryLeastSquaresRegression();
    final LeastSquaresRegressionResult result1 = regression.regress(x, null, y1, false);
    final LeastSquaresRegressionResult result2 = regression.regress(x, null, y2, true);
    try {
      result1.getPredictedValue(null);
      fail();
    } catch (final IllegalArgumentException e) {
      // Expected
    }
    try {
      result1.getPredictedValue(new Double[] { 2.4, 2.5, 3.4 });
      fail();
    } catch (final IllegalArgumentException e) {
      // Expected
    }
    try {
      result2.getPredictedValue(new Double[] { 1.3 });
      fail();
    } catch (final IllegalArgumentException e) {
      // Expected
    }
    Double[] z;
    for (int i = 0; i < 10; i++) {
      z = new Double[] { Math.random(), Math.random() };
      assertEquals(f1.evaluate(z), result1.getPredictedValue(z), EPS);
      assertEquals(f2.evaluate(z), result2.getPredictedValue(z), EPS);
    }
  }
}
