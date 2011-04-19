/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.model.finitedifference;

import org.testng.annotations.Test;

import com.opengamma.financial.model.finitedifference.ConvectionDiffusionPDESolver2D;
import com.opengamma.financial.model.finitedifference.ExplicitFiniteDifference2D;

/**
 * 
 */
public class ExplicitFiniteDifference2DTest {

  private static final HestonPDETestCase HESTON_TESTER = new HestonPDETestCase();
  private static final SpreadOptionPDETestCase SPREAD_OPTION_TESTER = new SpreadOptionPDETestCase();
  private static final ConvectionDiffusionPDESolver2D SOLVER = new ExplicitFiniteDifference2D();

  // @Test
  public void testSpreadOption() {

    int timeSteps = 5000;
    int xSteps = 100;
    int ySteps = 100;

    SPREAD_OPTION_TESTER.testAgaintBSPrice(SOLVER, timeSteps, xSteps, ySteps);
  }

  @Test
  public void testHeston() {

    int timeSteps = 5000;
    int xSteps = 80;
    int ySteps = 80;

    HESTON_TESTER.testCallPrice(SOLVER, timeSteps, xSteps, ySteps);
  }
}
