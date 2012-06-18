/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.opengamma.core.position.Position;
import com.opengamma.core.position.Trade;
import com.opengamma.core.position.impl.SimplePosition;
import com.opengamma.engine.ComputationTargetResolver;
import com.opengamma.id.UniqueId;

/**
 * A position implementation that may not be fully resolved at construction but will appear fully resolved when used.
 */
public final class LazyResolvedPosition extends LazyResolvedPositionOrTrade<Position> implements Position {

  // TODO: should be package visible

  private static final long serialVersionUID = 1L;

  private volatile Collection<Trade> _trades;

  /**
   * Creates a new lazily resolved position.
   * 
   * @param underlying the underlying, un-resolved position
   * @param context the lazy resolution context
   */
  public LazyResolvedPosition(final LazyResolveContext context, final Position underlying) {
    super(context, underlying);
  }

  @Override
  public UniqueId getParentNodeId() {
    return getUnderlying().getParentNodeId();
  }

  @Override
  public Collection<Trade> getTrades() {
    if (_trades == null) {
      synchronized (this) {
        if (_trades == null) {
          final Collection<Trade> trades = getUnderlying().getTrades();
          if (trades.isEmpty()) {
            _trades = Collections.emptySet();
          } else {
            final Collection<Trade> newTrades = new ArrayList<Trade>(trades.size());
            for (Trade trade : trades) {
              newTrades.add(new LazyResolvedTrade(getLazyResolveContext(), trade));
            }
            _trades = newTrades;
            getLazyResolveContext().cacheTrades(newTrades);
          }
        }
      }
    }
    return _trades;
  }

  @Override
  public Map<String, String> getAttributes() {
    return getUnderlying().getAttributes();
  }

  @Override
  protected TargetResolverPosition targetResolverObject(final ComputationTargetResolver targetResolver) {
    return new TargetResolverPosition(targetResolver, this);
  }

  @Override
  protected SimplePosition simpleObject() {
    return new SimplePosition(this);
  }

}
