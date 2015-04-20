/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.trade;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.core.position.Trade;
import com.opengamma.financial.security.irs.InterestRateSwapSecurity;

/**
 * Trade wrapper for interest rate swap trades.
 */
@BeanDefinition
public final class InterestRateSwapTrade extends TradeWrapper<InterestRateSwapSecurity> implements ImmutableBean {

  @PropertyDefinition(overrideGet = true)
  private final ImmutableTradeBundle _tradeBundle;

  /**
   * Base trade wrapper constructor that wraps a trade in an explicit instrument type.
   *
   * @param trade the trade containing the instrument, not null.
   */
  public InterestRateSwapTrade(Trade trade) {
    this(ImmutableTradeBundle.of(trade));
  }

  @ImmutableConstructor
  private InterestRateSwapTrade(ImmutableTradeBundle tradeBundle) {
    super(InterestRateSwapSecurity.class, tradeBundle);
    _tradeBundle = tradeBundle;
  }
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code InterestRateSwapTrade}.
   * @return the meta-bean, not null
   */
  public static InterestRateSwapTrade.Meta meta() {
    return InterestRateSwapTrade.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(InterestRateSwapTrade.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static InterestRateSwapTrade.Builder builder() {
    return new InterestRateSwapTrade.Builder();
  }

  @Override
  public InterestRateSwapTrade.Meta metaBean() {
    return InterestRateSwapTrade.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the tradeBundle.
   * @return the value of the property
   */
  @Override
  public ImmutableTradeBundle getTradeBundle() {
    return _tradeBundle;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      InterestRateSwapTrade other = (InterestRateSwapTrade) obj;
      return JodaBeanUtils.equal(getTradeBundle(), other.getTradeBundle());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(getTradeBundle());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("InterestRateSwapTrade{");
    buf.append("tradeBundle").append('=').append(JodaBeanUtils.toString(getTradeBundle()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code InterestRateSwapTrade}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code tradeBundle} property.
     */
    private final MetaProperty<ImmutableTradeBundle> _tradeBundle = DirectMetaProperty.ofImmutable(
        this, "tradeBundle", InterestRateSwapTrade.class, ImmutableTradeBundle.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "tradeBundle");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1481798406:  // tradeBundle
          return _tradeBundle;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public InterestRateSwapTrade.Builder builder() {
      return new InterestRateSwapTrade.Builder();
    }

    @Override
    public Class<? extends InterestRateSwapTrade> beanType() {
      return InterestRateSwapTrade.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code tradeBundle} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableTradeBundle> tradeBundle() {
      return _tradeBundle;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 1481798406:  // tradeBundle
          return ((InterestRateSwapTrade) bean).getTradeBundle();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code InterestRateSwapTrade}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<InterestRateSwapTrade> {

    private ImmutableTradeBundle _tradeBundle;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(InterestRateSwapTrade beanToCopy) {
      this._tradeBundle = beanToCopy.getTradeBundle();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 1481798406:  // tradeBundle
          return _tradeBundle;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 1481798406:  // tradeBundle
          this._tradeBundle = (ImmutableTradeBundle) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public InterestRateSwapTrade build() {
      return new InterestRateSwapTrade(
          _tradeBundle);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code tradeBundle} property in the builder.
     * @param tradeBundle  the new value
     * @return this, for chaining, not null
     */
    public Builder tradeBundle(ImmutableTradeBundle tradeBundle) {
      this._tradeBundle = tradeBundle;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("InterestRateSwapTrade.Builder{");
      buf.append("tradeBundle").append('=').append(JodaBeanUtils.toString(_tradeBundle));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
