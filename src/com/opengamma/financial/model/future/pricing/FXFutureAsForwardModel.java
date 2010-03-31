/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.model.future.pricing;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.financial.greeks.Greek;
import com.opengamma.financial.greeks.GreekResultCollection;
import com.opengamma.financial.model.forward.definition.FXForwardDataBundle;
import com.opengamma.financial.model.forward.definition.ForwardDefinition;
import com.opengamma.financial.model.forward.pricing.FXForwardModel;
import com.opengamma.financial.model.forward.pricing.ForwardModel;
import com.opengamma.financial.model.future.definition.FXFutureDataBundle;
import com.opengamma.financial.model.future.definition.FutureDefinition;

/**
 * @author emcleod
 *
 */
public class FXFutureAsForwardModel implements FutureModel<FXFutureDataBundle> {
  private static final Logger s_Log = LoggerFactory.getLogger(FXFutureAsForwardModel.class);
  private final ForwardModel<FXForwardDataBundle> _forwardModel = new FXForwardModel();

  @Override
  public GreekResultCollection getGreeks(final FutureDefinition definition, final FXFutureDataBundle data, final Set<Greek> requiredGreeks) {
    if (definition == null)
      throw new IllegalArgumentException("Future definition was null");
    if (data == null)
      throw new IllegalArgumentException("Data bundle was null");
    if (requiredGreeks == null)
      throw new IllegalArgumentException("Set of required greeks was null");
    if (requiredGreeks.isEmpty())
      return new GreekResultCollection();
    if (!requiredGreeks.contains(Greek.FAIR_PRICE)) {
      s_Log.warn("Currently only fair price is calculated for futures");
      return new GreekResultCollection();
    }
    final ForwardDefinition forward = new ForwardDefinition(definition.getExpiry());
    final FXForwardDataBundle forwardData = new FXForwardDataBundle(data.getDiscountCurve(), data.getForeignCurve(), data.getSpot(), data.getDate());
    return _forwardModel.getGreeks(forward, forwardData, requiredGreeks);
  }

}
