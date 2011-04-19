/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.model.finitedifference;

import org.testng.annotations.Test;

import com.opengamma.financial.model.finitedifference.ConvectionDiffusionPDESolver2D;
import com.opengamma.financial.model.finitedifference.ImplicitFiniteDifference2D;

/**
 * NOT WORKING
 */
public class ImplicitFiniteDifference2DTest {

  private static final HestonPDETestCase HESTON_TESTER = new HestonPDETestCase();
  private static final SpreadOptionPDETestCase SPREAD_OPTION_TESTER = new SpreadOptionPDETestCase();
  private static final ConvectionDiffusionPDESolver2D SOLVER = new ImplicitFiniteDifference2D();

  @Test
  public void testSpreadOption() {

    int timeSteps = 40;
    int xSteps = 100;
    int ySteps = 100;

    SPREAD_OPTION_TESTER.testAgaintBSPrice(SOLVER, timeSteps, xSteps, ySteps);
  }

  @Test(enabled = false)
  public void testHeston() {

    int timeSteps = 200;
    int xSteps = 100;
    int ySteps = 100;

    HESTON_TESTER.testCallPrice(SOLVER, timeSteps, xSteps, ySteps);
  }

}
