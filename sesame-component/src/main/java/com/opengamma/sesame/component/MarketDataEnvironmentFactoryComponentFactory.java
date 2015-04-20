/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 * <p/>
 * Please see distribution for license.
 */
package com.opengamma.sesame.component;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.core.link.ConfigLink;
import com.opengamma.financial.currency.CurrencyMatrix;
import com.opengamma.sesame.engine.ComponentMap;
import com.opengamma.sesame.marketdata.MarketDataFactory;
import com.opengamma.sesame.marketdata.builders.MarketDataBuilders;
import com.opengamma.sesame.marketdata.builders.MarketDataEnvironmentFactory;

/**
 * Component factory to build a {@link MarketDataEnvironmentFactory}.
 */
@BeanDefinition
public class MarketDataEnvironmentFactoryComponentFactory extends AbstractComponentFactory {

  /** Name under which the components will be published. */
  @PropertyDefinition(validate = "notEmpty")
  private String _classifier;

  /** Singleton components used when constructing the market data builders. */
  @PropertyDefinition(validate = "notNull")
  private ComponentMap _componentMap;

  /** Name of the currency matrix for creating FX rates. */
  @PropertyDefinition(validate = "notEmpty")
  private String _currencyMatrixName;

  /** The data source name used when querying time series. */
  @PropertyDefinition(validate = "notEmpty")
  private String _timeSeriesDataSource;

  /** Market data factory for sourcing market data quotes from an underlying provider. */
  @PropertyDefinition(validate = "notNull")
  private MarketDataFactory<?> _marketDataFactory;

  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) throws Exception {
    ConfigLink<CurrencyMatrix> currencyMatrixLink = ConfigLink.resolvable(_currencyMatrixName, CurrencyMatrix.class);

    MarketDataEnvironmentFactory environmentFactory =
        new MarketDataEnvironmentFactory(
            _marketDataFactory,
            MarketDataBuilders.raw(_componentMap, _timeSeriesDataSource),
            MarketDataBuilders.multicurve(_componentMap, currencyMatrixLink),
            MarketDataBuilders.issuerMulticurve(_componentMap, currencyMatrixLink),
            MarketDataBuilders.security(),
            MarketDataBuilders.fxMatrix(),
            MarketDataBuilders.fxRate(currencyMatrixLink));

    repo.registerComponent(MarketDataEnvironmentFactory.class, _classifier, environmentFactory);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MarketDataEnvironmentFactoryComponentFactory}.
   * @return the meta-bean, not null
   */
  public static MarketDataEnvironmentFactoryComponentFactory.Meta meta() {
    return MarketDataEnvironmentFactoryComponentFactory.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(MarketDataEnvironmentFactoryComponentFactory.Meta.INSTANCE);
  }

  @Override
  public MarketDataEnvironmentFactoryComponentFactory.Meta metaBean() {
    return MarketDataEnvironmentFactoryComponentFactory.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets name under which the components will be published.
   * @return the value of the property, not empty
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets name under which the components will be published.
   * @param classifier  the new value of the property, not empty
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notEmpty(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets singleton components used when constructing the market data builders.
   * @return the value of the property, not null
   */
  public ComponentMap getComponentMap() {
    return _componentMap;
  }

  /**
   * Sets singleton components used when constructing the market data builders.
   * @param componentMap  the new value of the property, not null
   */
  public void setComponentMap(ComponentMap componentMap) {
    JodaBeanUtils.notNull(componentMap, "componentMap");
    this._componentMap = componentMap;
  }

  /**
   * Gets the the {@code componentMap} property.
   * @return the property, not null
   */
  public final Property<ComponentMap> componentMap() {
    return metaBean().componentMap().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets name of the currency matrix for creating FX rates.
   * @return the value of the property, not empty
   */
  public String getCurrencyMatrixName() {
    return _currencyMatrixName;
  }

  /**
   * Sets name of the currency matrix for creating FX rates.
   * @param currencyMatrixName  the new value of the property, not empty
   */
  public void setCurrencyMatrixName(String currencyMatrixName) {
    JodaBeanUtils.notEmpty(currencyMatrixName, "currencyMatrixName");
    this._currencyMatrixName = currencyMatrixName;
  }

  /**
   * Gets the the {@code currencyMatrixName} property.
   * @return the property, not null
   */
  public final Property<String> currencyMatrixName() {
    return metaBean().currencyMatrixName().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the data source name used when querying time series.
   * @return the value of the property, not empty
   */
  public String getTimeSeriesDataSource() {
    return _timeSeriesDataSource;
  }

  /**
   * Sets the data source name used when querying time series.
   * @param timeSeriesDataSource  the new value of the property, not empty
   */
  public void setTimeSeriesDataSource(String timeSeriesDataSource) {
    JodaBeanUtils.notEmpty(timeSeriesDataSource, "timeSeriesDataSource");
    this._timeSeriesDataSource = timeSeriesDataSource;
  }

  /**
   * Gets the the {@code timeSeriesDataSource} property.
   * @return the property, not null
   */
  public final Property<String> timeSeriesDataSource() {
    return metaBean().timeSeriesDataSource().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets market data factory for sourcing market data quotes from an underlying provider.
   * @return the value of the property, not null
   */
  public MarketDataFactory<?> getMarketDataFactory() {
    return _marketDataFactory;
  }

  /**
   * Sets market data factory for sourcing market data quotes from an underlying provider.
   * @param marketDataFactory  the new value of the property, not null
   */
  public void setMarketDataFactory(MarketDataFactory<?> marketDataFactory) {
    JodaBeanUtils.notNull(marketDataFactory, "marketDataFactory");
    this._marketDataFactory = marketDataFactory;
  }

  /**
   * Gets the the {@code marketDataFactory} property.
   * @return the property, not null
   */
  public final Property<MarketDataFactory<?>> marketDataFactory() {
    return metaBean().marketDataFactory().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public MarketDataEnvironmentFactoryComponentFactory clone() {
    return JodaBeanUtils.cloneAlways(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      MarketDataEnvironmentFactoryComponentFactory other = (MarketDataEnvironmentFactoryComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(getComponentMap(), other.getComponentMap()) &&
          JodaBeanUtils.equal(getCurrencyMatrixName(), other.getCurrencyMatrixName()) &&
          JodaBeanUtils.equal(getTimeSeriesDataSource(), other.getTimeSeriesDataSource()) &&
          JodaBeanUtils.equal(getMarketDataFactory(), other.getMarketDataFactory()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash = hash * 31 + JodaBeanUtils.hashCode(getComponentMap());
    hash = hash * 31 + JodaBeanUtils.hashCode(getCurrencyMatrixName());
    hash = hash * 31 + JodaBeanUtils.hashCode(getTimeSeriesDataSource());
    hash = hash * 31 + JodaBeanUtils.hashCode(getMarketDataFactory());
    return hash ^ super.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(192);
    buf.append("MarketDataEnvironmentFactoryComponentFactory{");
    int len = buf.length();
    toString(buf);
    if (buf.length() > len) {
      buf.setLength(buf.length() - 2);
    }
    buf.append('}');
    return buf.toString();
  }

  @Override
  protected void toString(StringBuilder buf) {
    super.toString(buf);
    buf.append("classifier").append('=').append(JodaBeanUtils.toString(getClassifier())).append(',').append(' ');
    buf.append("componentMap").append('=').append(JodaBeanUtils.toString(getComponentMap())).append(',').append(' ');
    buf.append("currencyMatrixName").append('=').append(JodaBeanUtils.toString(getCurrencyMatrixName())).append(',').append(' ');
    buf.append("timeSeriesDataSource").append('=').append(JodaBeanUtils.toString(getTimeSeriesDataSource())).append(',').append(' ');
    buf.append("marketDataFactory").append('=').append(JodaBeanUtils.toString(getMarketDataFactory())).append(',').append(' ');
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MarketDataEnvironmentFactoryComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", MarketDataEnvironmentFactoryComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code componentMap} property.
     */
    private final MetaProperty<ComponentMap> _componentMap = DirectMetaProperty.ofReadWrite(
        this, "componentMap", MarketDataEnvironmentFactoryComponentFactory.class, ComponentMap.class);
    /**
     * The meta-property for the {@code currencyMatrixName} property.
     */
    private final MetaProperty<String> _currencyMatrixName = DirectMetaProperty.ofReadWrite(
        this, "currencyMatrixName", MarketDataEnvironmentFactoryComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code timeSeriesDataSource} property.
     */
    private final MetaProperty<String> _timeSeriesDataSource = DirectMetaProperty.ofReadWrite(
        this, "timeSeriesDataSource", MarketDataEnvironmentFactoryComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code marketDataFactory} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<MarketDataFactory<?>> _marketDataFactory = DirectMetaProperty.ofReadWrite(
        this, "marketDataFactory", MarketDataEnvironmentFactoryComponentFactory.class, (Class) MarketDataFactory.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "componentMap",
        "currencyMatrixName",
        "timeSeriesDataSource",
        "marketDataFactory");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return _classifier;
        case -499150049:  // componentMap
          return _componentMap;
        case 1305503965:  // currencyMatrixName
          return _currencyMatrixName;
        case -2110575831:  // timeSeriesDataSource
          return _timeSeriesDataSource;
        case -1673716700:  // marketDataFactory
          return _marketDataFactory;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends MarketDataEnvironmentFactoryComponentFactory> builder() {
      return new DirectBeanBuilder<MarketDataEnvironmentFactoryComponentFactory>(new MarketDataEnvironmentFactoryComponentFactory());
    }

    @Override
    public Class<? extends MarketDataEnvironmentFactoryComponentFactory> beanType() {
      return MarketDataEnvironmentFactoryComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code componentMap} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ComponentMap> componentMap() {
      return _componentMap;
    }

    /**
     * The meta-property for the {@code currencyMatrixName} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> currencyMatrixName() {
      return _currencyMatrixName;
    }

    /**
     * The meta-property for the {@code timeSeriesDataSource} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> timeSeriesDataSource() {
      return _timeSeriesDataSource;
    }

    /**
     * The meta-property for the {@code marketDataFactory} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MarketDataFactory<?>> marketDataFactory() {
      return _marketDataFactory;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return ((MarketDataEnvironmentFactoryComponentFactory) bean).getClassifier();
        case -499150049:  // componentMap
          return ((MarketDataEnvironmentFactoryComponentFactory) bean).getComponentMap();
        case 1305503965:  // currencyMatrixName
          return ((MarketDataEnvironmentFactoryComponentFactory) bean).getCurrencyMatrixName();
        case -2110575831:  // timeSeriesDataSource
          return ((MarketDataEnvironmentFactoryComponentFactory) bean).getTimeSeriesDataSource();
        case -1673716700:  // marketDataFactory
          return ((MarketDataEnvironmentFactoryComponentFactory) bean).getMarketDataFactory();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          ((MarketDataEnvironmentFactoryComponentFactory) bean).setClassifier((String) newValue);
          return;
        case -499150049:  // componentMap
          ((MarketDataEnvironmentFactoryComponentFactory) bean).setComponentMap((ComponentMap) newValue);
          return;
        case 1305503965:  // currencyMatrixName
          ((MarketDataEnvironmentFactoryComponentFactory) bean).setCurrencyMatrixName((String) newValue);
          return;
        case -2110575831:  // timeSeriesDataSource
          ((MarketDataEnvironmentFactoryComponentFactory) bean).setTimeSeriesDataSource((String) newValue);
          return;
        case -1673716700:  // marketDataFactory
          ((MarketDataEnvironmentFactoryComponentFactory) bean).setMarketDataFactory((MarketDataFactory<?>) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notEmpty(((MarketDataEnvironmentFactoryComponentFactory) bean)._classifier, "classifier");
      JodaBeanUtils.notNull(((MarketDataEnvironmentFactoryComponentFactory) bean)._componentMap, "componentMap");
      JodaBeanUtils.notEmpty(((MarketDataEnvironmentFactoryComponentFactory) bean)._currencyMatrixName, "currencyMatrixName");
      JodaBeanUtils.notEmpty(((MarketDataEnvironmentFactoryComponentFactory) bean)._timeSeriesDataSource, "timeSeriesDataSource");
      JodaBeanUtils.notNull(((MarketDataEnvironmentFactoryComponentFactory) bean)._marketDataFactory, "marketDataFactory");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
