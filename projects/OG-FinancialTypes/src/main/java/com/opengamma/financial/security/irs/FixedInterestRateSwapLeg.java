/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.irs;

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

import com.opengamma.financial.convention.FixedInterestRateSwapLegConvention;

/**
 * A fixed interest rate swap leg.
 */
@BeanDefinition
public final class FixedInterestRateSwapLeg extends InterestRateSwapLeg {

  /**
   * The rate
   */
  @PropertyDefinition(validate = "notNull")
  private Rate _rate;

  /**
   * The schedule for the dates on this leg.
   * Allows the conventions to be overridden with specific dates.
   */
  @PropertyDefinition
  private FloatingInterestRateSwapLegSchedule _schedule;

  //-------------------------------------------------------------------------
  @Override
  public <T> T accept(InterestRateSwapLegVisitor<T> visitor) {
    return visitor.visitFixedInterestRateSwapLeg(this);
  }

  @Override
  public String toString() {
    return String.format("Fixed swap leg: %s %f%% %s %d", getPayReceiveType(),
                         getRate().getInitialRate(), getNotional().getCurrency(), (long) getNotional().getInitialAmount());
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FixedInterestRateSwapLeg}.
   * @return the meta-bean, not null
   */
  public static FixedInterestRateSwapLeg.Meta meta() {
    return FixedInterestRateSwapLeg.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(FixedInterestRateSwapLeg.Meta.INSTANCE);
  }

  @Override
  public FixedInterestRateSwapLeg.Meta metaBean() {
    return FixedInterestRateSwapLeg.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the rate
   * @return the value of the property, not null
   */
  public Rate getRate() {
    return _rate;
  }

  /**
   * Sets the rate
   * @param rate  the new value of the property, not null
   */
  public void setRate(Rate rate) {
    JodaBeanUtils.notNull(rate, "rate");
    this._rate = rate;
  }

  /**
   * Gets the the {@code rate} property.
   * @return the property, not null
   */
  public Property<Rate> rate() {
    return metaBean().rate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the schedule for the dates on this leg.
   * Allows the conventions to be overridden with specific dates.
   * @return the value of the property
   */
  public FloatingInterestRateSwapLegSchedule getSchedule() {
    return _schedule;
  }

  /**
   * Sets the schedule for the dates on this leg.
   * Allows the conventions to be overridden with specific dates.
   * @param schedule  the new value of the property
   */
  public void setSchedule(FloatingInterestRateSwapLegSchedule schedule) {
    this._schedule = schedule;
  }

  /**
   * Gets the the {@code schedule} property.
   * Allows the conventions to be overridden with specific dates.
   * @return the property, not null
   */
  public Property<FloatingInterestRateSwapLegSchedule> schedule() {
    return metaBean().schedule().createProperty(this);
  }

  //-----------------------------------------------------------------------
  @Override
  public FixedInterestRateSwapLeg clone() {
    return (FixedInterestRateSwapLeg) super.clone();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FixedInterestRateSwapLeg other = (FixedInterestRateSwapLeg) obj;
      return JodaBeanUtils.equal(getRate(), other.getRate()) &&
          JodaBeanUtils.equal(getSchedule(), other.getSchedule()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getRate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSchedule());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FixedInterestRateSwapLeg}.
   */
  public static final class Meta extends InterestRateSwapLeg.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code rate} property.
     */
    private final MetaProperty<Rate> _rate = DirectMetaProperty.ofReadWrite(
        this, "rate", FixedInterestRateSwapLeg.class, Rate.class);
    /**
     * The meta-property for the {@code schedule} property.
     */
    private final MetaProperty<FloatingInterestRateSwapLegSchedule> _schedule = DirectMetaProperty.ofReadWrite(
        this, "schedule", FixedInterestRateSwapLeg.class, FloatingInterestRateSwapLegSchedule.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "rate",
        "schedule");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          return _rate;
        case -697920873:  // schedule
          return _schedule;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FixedInterestRateSwapLeg> builder() {
      return new DirectBeanBuilder<FixedInterestRateSwapLeg>(new FixedInterestRateSwapLeg());
    }

    @Override
    public Class<? extends FixedInterestRateSwapLeg> beanType() {
      return FixedInterestRateSwapLeg.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code rate} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Rate> rate() {
      return _rate;
    }

    /**
     * The meta-property for the {@code schedule} property.
     * @return the meta-property, not null
     */
    public MetaProperty<FloatingInterestRateSwapLegSchedule> schedule() {
      return _schedule;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          return ((FixedInterestRateSwapLeg) bean).getRate();
        case -697920873:  // schedule
          return ((FixedInterestRateSwapLeg) bean).getSchedule();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 3493088:  // rate
          ((FixedInterestRateSwapLeg) bean).setRate((Rate) newValue);
          return;
        case -697920873:  // schedule
          ((FixedInterestRateSwapLeg) bean).setSchedule((FloatingInterestRateSwapLegSchedule) newValue);
          return;
      }
      super.propertySet(bean, propertyName, newValue, quiet);
    }

    @Override
    protected void validate(Bean bean) {
      JodaBeanUtils.notNull(((FixedInterestRateSwapLeg) bean)._rate, "rate");
      super.validate(bean);
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
