/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.volatility.surface.black.defaultproperties;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.financial.analytics.model.equity.EquitySecurityUtils;
import com.opengamma.id.UniqueId;

/**
 *
 */
public class EquityBlackVolatilitySurfacePrimitiveDefaults extends EquityBlackVolatilitySurfaceDefaults {

  /**
   * @param priority The priority of these defaults
   * @param defaultsPerTicker The default values per ticker
   */
  public EquityBlackVolatilitySurfacePrimitiveDefaults(final String priority, final String... defaultsPerTicker) {
    super(ComputationTargetType.PRIMITIVE, priority, defaultsPerTicker);
  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    if (target.getType() != ComputationTargetType.PRIMITIVE) {
      return false;
    }
    final UniqueId uniqueId = target.getUniqueId();
    final String ticker = EquitySecurityUtils.getIndexOrEquityName(uniqueId);
    if (getAllIds().contains(ticker)) {
      return true;
    }
    return false;
  }

  @Override
  protected String getId(final ComputationTarget target) {
    return EquitySecurityUtils.getIndexOrEquityName(target.getUniqueId());
  }
}
