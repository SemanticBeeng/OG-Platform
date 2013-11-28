/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.marketdata;

import org.threeten.bp.LocalDate;

import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.historicaltimeseries.HistoricalTimeSeriesSource;
import com.opengamma.engine.marketdata.spec.FixedHistoricalMarketDataSpecification;
import com.opengamma.engine.marketdata.spec.MarketDataSpecification;
import com.opengamma.sesame.engine.ComponentMap;
import com.opengamma.util.ArgumentChecker;

/**
 * Creates a {@link MarketDataProviderFunction} given a {@link MarketDataSpecification}.
 */
public class SpecificationMarketDataFactory implements MarketDataFactory {

  // TODO do we want to reuse MarketDataSpecification or replace it?
  private final MarketDataSpecification _marketDataSpecification;

  public SpecificationMarketDataFactory(MarketDataSpecification marketDataSpecification) {
    _marketDataSpecification = ArgumentChecker.notNull(marketDataSpecification, "marketDataSpecification");
    if (!(_marketDataSpecification instanceof FixedHistoricalMarketDataSpecification)) {
      throw new IllegalArgumentException("Only FixedHistoricalMarketDataSpecification is currently supported");
    }
  }

  @Override
  public MarketDataProviderFunction create(ComponentMap components) {
    ConfigSource configSource = components.getComponent(ConfigSource.class);
    HistoricalTimeSeriesSource timeSeriesSource = components.getComponent(HistoricalTimeSeriesSource.class);
    LocalDate date = ((FixedHistoricalMarketDataSpecification) _marketDataSpecification).getSnapshotDate();
    HistoricalRawMarketDataSource rawDataSource =
        new HistoricalRawMarketDataSource(timeSeriesSource, date, "BLOOMBERG", "Market_Value");
    return new EagerMarketDataProvider(rawDataSource, configSource, "BloombergLiveData");
  }
}
