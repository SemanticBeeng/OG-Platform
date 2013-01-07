/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.volatility.surface.black.defaultproperties;

import com.opengamma.core.security.Security;
import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.financial.analytics.model.equity.EquitySecurityUtils;
import com.opengamma.financial.security.option.EquityIndexOptionSecurity;
import com.opengamma.financial.security.option.EquityOptionSecurity;

/**
 *
 */
public class EquityBlackVolatilitySurfacePerEquityTradeDefaults extends EquityBlackVolatilitySurfaceDefaults {

  /**
   * @param priority The priority of these defaults
   * @param defaultsPerTicker The default values per ticker
   */
  public EquityBlackVolatilitySurfacePerEquityTradeDefaults(final String priority, final String... defaultsPerTicker) {
    super(ComputationTargetType.TRADE, priority, defaultsPerTicker);
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    if (target.getType() != ComputationTargetType.TRADE) {
      return false;
    }
    final Security security = target.getTrade().getSecurity();
    if (!(security instanceof EquityIndexOptionSecurity) && !(security instanceof EquityOptionSecurity)) {
      return false;
    }
    final String ticker = EquitySecurityUtils.getIndexOrEquityName(security);
    if (getAllIds().contains(ticker)) {
      return true;
    }
    return false;
  }

  @Override
  protected String getId(final ComputationTarget target) {
    return EquitySecurityUtils.getIndexOrEquityName(target.getTrade().getSecurity());
  }
}
