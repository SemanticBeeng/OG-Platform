/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.sesame.credit;

import java.util.Arrays;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import com.opengamma.analytics.financial.credit.isdastandardmodel.ISDACompliantYieldCurve;
import com.opengamma.analytics.financial.credit.isdastandardmodel.ISDACompliantYieldCurveBuild;
import com.opengamma.analytics.financial.credit.isdastandardmodel.ISDAInstrumentTypes;
import com.opengamma.financial.convention.businessday.BusinessDayConventions;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.financial.convention.daycount.DayCounts;

/**
 * Helper class to produce standard objects for credit testing.
 * 
 * Objects here are based on those in /sesame-function/src/test/resources/credit/YC Test Data.xls
 */
public final class CreditTestData {

  private static final LocalDate VALUATION_DATE = LocalDate.of(2014, 3, 27);

  private static int MONEY_MARKET_INSTS = 6;
  private static Period[] TENORS = {Period.ofMonths(1), Period.ofMonths(2), Period.ofMonths(3), Period.ofMonths(6), Period.ofMonths(9), Period.ofYears(1), Period.ofYears(2), Period.ofYears(3), Period.ofYears(4), Period.ofYears(5), Period.ofYears(6), Period.ofYears(7), Period.ofYears(8), Period.ofYears(9), Period.ofYears(10), Period.ofYears(11), Period.ofYears(12), Period.ofYears(15), Period.ofYears(20), Period.ofYears(25), Period.ofYears(30)};
  private static double[] RATES = {0.00445, 0.009488, 0.012337, 0.017762, 0.01935, 0.020838, 0.01652, 0.02018, 0.023033, 0.02525, 0.02696, 0.02825, 0.02931, 0.03017, 0.03092, 0.0316, 0.03231, 0.03367, 0.03419, 0.03411, 0.03412};

  private CreditTestData() {}
  
  public static ISDACompliantYieldCurve createYieldCurve() {
    
    ISDAInstrumentTypes[] instrumentTypes = new ISDAInstrumentTypes[TENORS.length];
    
    Arrays.fill(instrumentTypes, 0, MONEY_MARKET_INSTS, ISDAInstrumentTypes.MoneyMarket);
    Arrays.fill(instrumentTypes, MONEY_MARKET_INSTS, TENORS.length, ISDAInstrumentTypes.Swap);
    
    ISDACompliantYieldCurveBuild builder = new ISDACompliantYieldCurveBuild(
        VALUATION_DATE,
        VALUATION_DATE,
        instrumentTypes, 
        TENORS,
        DayCounts.ACT_360,
        DayCounts.THIRTY_360,
        Period.ofYears(1),
        DayCounts.ACT_365,
        BusinessDayConventions.MODIFIED_FOLLOWING,
        new MondayToFridayCalendar("test")
    );
    
    return builder.build(RATES);
    
  }
  
}
