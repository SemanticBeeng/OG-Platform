/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.option;

import java.util.Map;

import javax.time.calendar.ZonedDateTime;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityVisitor;
import com.opengamma.financial.security.LongShort;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.Expiry;

/**
 * A security for FX barrier options.
 */
@BeanDefinition
public class FXBarrierOptionSecurity extends FinancialSecurity {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The security type.
   */
  public static final String SECURITY_TYPE = "FX_BARRIER_OPTION";

  /**
   * The put currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _putCurrency;
  /**
   * The call currency.
   */
  @PropertyDefinition(validate = "notNull")
  private Currency _callCurrency;
  /**
   * The put amount.
   */
  @PropertyDefinition(validate = "notNull")
  private double _putAmount;
  /**
   * The call amount.
   */
  @PropertyDefinition(validate = "notNull")
  private double _callAmount;
  /**
   * The expiry.
   */
  @PropertyDefinition(validate = "notNull")
  private Expiry _expiry;
  /**
   * The settlement date.
   */
  @PropertyDefinition(validate = "notNull")
  private ZonedDateTime _settlementDate;
  /**
   * The barrier type.
   */
  @PropertyDefinition(validate = "notNull")
  private BarrierType _barrierType;
  /**
   * The barrier direction.
   */
  @PropertyDefinition(validate = "notNull")
  private BarrierDirection _barrierDirection;
  /**
   * The monitoring type.
   */
  @PropertyDefinition(validate = "notNull")
  private MonitoringType _monitoringType;
  /**
   * The sampling frequency.
   */
  @PropertyDefinition(validate = "notNull")
  private SamplingFrequency _samplingFrequency;
  /**
   * The barrier level.
   */
  @PropertyDefinition(validate = "notNull")
  private double _barrierLevel;
  /**
   * The long/short type.
   */
  @PropertyDefinition(validate = "notNull")
  private LongShort _longShort = LongShort.LONG;

  /**
   * Creates an empty instance.
   * <p>
   * The security details should be set before use.
   */
  public FXBarrierOptionSecurity() {
  }

  public FXBarrierOptionSecurity(Currency putCurrency, Currency callCurrency, double putAmount, double callAmount, Expiry expiry,
      ZonedDateTime settlementDate, BarrierType barrierType, BarrierDirection barrierDirection, MonitoringType monitoringType,
      SamplingFrequency samplingFrequency, double barrierLevel, boolean isLong) {
    super(SECURITY_TYPE);
    setPutCurrency(putCurrency);
    setCallCurrency(callCurrency);
    setPutAmount(putAmount);
    setCallAmount(callAmount);
    setExpiry(expiry);
    setSettlementDate(settlementDate);
    setBarrierType(barrierType);
    setBarrierDirection(barrierDirection);
    setMonitoringType(monitoringType);
    setSamplingFrequency(samplingFrequency);
    setBarrierLevel(barrierLevel); 
    setLongShort(LongShort.ofLong(isLong));
  }

  //-------------------------------------------------------------------------
  @Override
  public final <T> T accept(FinancialSecurityVisitor<T> visitor) {
    return visitor.visitFXBarrierOptionSecurity(this);
  }

  /**
   * Accepts a visitor to manage traversal of the hierarchy.
   * 
   * @param <T> the result type of the visitor
   * @param visitor  the visitor, not null
   * @return the result
   */
  public <T> T accept(FXBarrierOptionSecurityVisitor<T> visitor) {
    return visitor.visitFXBarrierOptionSecurity(this);
  }

  //-------------------------------------------------------------------------
  /**
   * Checks if the long/short type is long.
   * 
   * @return true if long, false if short
   */
  public boolean isLong() {
    return getLongShort().isLong();
  }

  /**
   * Checks if the long/short type is short.
   * 
   * @return true if short, false if long
   */
  public boolean isShort() {
    return getLongShort().isShort();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FXBarrierOptionSecurity}.
   * @return the meta-bean, not null
   */
  public static FXBarrierOptionSecurity.Meta meta() {
    return FXBarrierOptionSecurity.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(FXBarrierOptionSecurity.Meta.INSTANCE);
  }

  @Override
  public FXBarrierOptionSecurity.Meta metaBean() {
    return FXBarrierOptionSecurity.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 516393024:  // putCurrency
        return getPutCurrency();
      case 643534991:  // callCurrency
        return getCallCurrency();
      case -984864697:  // putAmount
        return getPutAmount();
      case 1066661974:  // callAmount
        return getCallAmount();
      case -1289159373:  // expiry
        return getExpiry();
      case -295948169:  // settlementDate
        return getSettlementDate();
      case 1029043089:  // barrierType
        return getBarrierType();
      case 502579592:  // barrierDirection
        return getBarrierDirection();
      case -1483652190:  // monitoringType
        return getMonitoringType();
      case 1178782005:  // samplingFrequency
        return getSamplingFrequency();
      case 1827586573:  // barrierLevel
        return getBarrierLevel();
      case 116685664:  // longShort
        return getLongShort();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case 516393024:  // putCurrency
        setPutCurrency((Currency) newValue);
        return;
      case 643534991:  // callCurrency
        setCallCurrency((Currency) newValue);
        return;
      case -984864697:  // putAmount
        setPutAmount((Double) newValue);
        return;
      case 1066661974:  // callAmount
        setCallAmount((Double) newValue);
        return;
      case -1289159373:  // expiry
        setExpiry((Expiry) newValue);
        return;
      case -295948169:  // settlementDate
        setSettlementDate((ZonedDateTime) newValue);
        return;
      case 1029043089:  // barrierType
        setBarrierType((BarrierType) newValue);
        return;
      case 502579592:  // barrierDirection
        setBarrierDirection((BarrierDirection) newValue);
        return;
      case -1483652190:  // monitoringType
        setMonitoringType((MonitoringType) newValue);
        return;
      case 1178782005:  // samplingFrequency
        setSamplingFrequency((SamplingFrequency) newValue);
        return;
      case 1827586573:  // barrierLevel
        setBarrierLevel((Double) newValue);
        return;
      case 116685664:  // longShort
        setLongShort((LongShort) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_putCurrency, "putCurrency");
    JodaBeanUtils.notNull(_callCurrency, "callCurrency");
    JodaBeanUtils.notNull(_putAmount, "putAmount");
    JodaBeanUtils.notNull(_callAmount, "callAmount");
    JodaBeanUtils.notNull(_expiry, "expiry");
    JodaBeanUtils.notNull(_settlementDate, "settlementDate");
    JodaBeanUtils.notNull(_barrierType, "barrierType");
    JodaBeanUtils.notNull(_barrierDirection, "barrierDirection");
    JodaBeanUtils.notNull(_monitoringType, "monitoringType");
    JodaBeanUtils.notNull(_samplingFrequency, "samplingFrequency");
    JodaBeanUtils.notNull(_barrierLevel, "barrierLevel");
    JodaBeanUtils.notNull(_longShort, "longShort");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      FXBarrierOptionSecurity other = (FXBarrierOptionSecurity) obj;
      return JodaBeanUtils.equal(getPutCurrency(), other.getPutCurrency()) &&
          JodaBeanUtils.equal(getCallCurrency(), other.getCallCurrency()) &&
          JodaBeanUtils.equal(getPutAmount(), other.getPutAmount()) &&
          JodaBeanUtils.equal(getCallAmount(), other.getCallAmount()) &&
          JodaBeanUtils.equal(getExpiry(), other.getExpiry()) &&
          JodaBeanUtils.equal(getSettlementDate(), other.getSettlementDate()) &&
          JodaBeanUtils.equal(getBarrierType(), other.getBarrierType()) &&
          JodaBeanUtils.equal(getBarrierDirection(), other.getBarrierDirection()) &&
          JodaBeanUtils.equal(getMonitoringType(), other.getMonitoringType()) &&
          JodaBeanUtils.equal(getSamplingFrequency(), other.getSamplingFrequency()) &&
          JodaBeanUtils.equal(getBarrierLevel(), other.getBarrierLevel()) &&
          JodaBeanUtils.equal(getLongShort(), other.getLongShort()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getPutCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCallCurrency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPutAmount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCallAmount());
    hash += hash * 31 + JodaBeanUtils.hashCode(getExpiry());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSettlementDate());
    hash += hash * 31 + JodaBeanUtils.hashCode(getBarrierType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getBarrierDirection());
    hash += hash * 31 + JodaBeanUtils.hashCode(getMonitoringType());
    hash += hash * 31 + JodaBeanUtils.hashCode(getSamplingFrequency());
    hash += hash * 31 + JodaBeanUtils.hashCode(getBarrierLevel());
    hash += hash * 31 + JodaBeanUtils.hashCode(getLongShort());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the put currency.
   * @return the value of the property, not null
   */
  public Currency getPutCurrency() {
    return _putCurrency;
  }

  /**
   * Sets the put currency.
   * @param putCurrency  the new value of the property, not null
   */
  public void setPutCurrency(Currency putCurrency) {
    JodaBeanUtils.notNull(putCurrency, "putCurrency");
    this._putCurrency = putCurrency;
  }

  /**
   * Gets the the {@code putCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> putCurrency() {
    return metaBean().putCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the call currency.
   * @return the value of the property, not null
   */
  public Currency getCallCurrency() {
    return _callCurrency;
  }

  /**
   * Sets the call currency.
   * @param callCurrency  the new value of the property, not null
   */
  public void setCallCurrency(Currency callCurrency) {
    JodaBeanUtils.notNull(callCurrency, "callCurrency");
    this._callCurrency = callCurrency;
  }

  /**
   * Gets the the {@code callCurrency} property.
   * @return the property, not null
   */
  public final Property<Currency> callCurrency() {
    return metaBean().callCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the put amount.
   * @return the value of the property, not null
   */
  public double getPutAmount() {
    return _putAmount;
  }

  /**
   * Sets the put amount.
   * @param putAmount  the new value of the property, not null
   */
  public void setPutAmount(double putAmount) {
    JodaBeanUtils.notNull(putAmount, "putAmount");
    this._putAmount = putAmount;
  }

  /**
   * Gets the the {@code putAmount} property.
   * @return the property, not null
   */
  public final Property<Double> putAmount() {
    return metaBean().putAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the call amount.
   * @return the value of the property, not null
   */
  public double getCallAmount() {
    return _callAmount;
  }

  /**
   * Sets the call amount.
   * @param callAmount  the new value of the property, not null
   */
  public void setCallAmount(double callAmount) {
    JodaBeanUtils.notNull(callAmount, "callAmount");
    this._callAmount = callAmount;
  }

  /**
   * Gets the the {@code callAmount} property.
   * @return the property, not null
   */
  public final Property<Double> callAmount() {
    return metaBean().callAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry.
   * @return the value of the property, not null
   */
  public Expiry getExpiry() {
    return _expiry;
  }

  /**
   * Sets the expiry.
   * @param expiry  the new value of the property, not null
   */
  public void setExpiry(Expiry expiry) {
    JodaBeanUtils.notNull(expiry, "expiry");
    this._expiry = expiry;
  }

  /**
   * Gets the the {@code expiry} property.
   * @return the property, not null
   */
  public final Property<Expiry> expiry() {
    return metaBean().expiry().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the settlement date.
   * @return the value of the property, not null
   */
  public ZonedDateTime getSettlementDate() {
    return _settlementDate;
  }

  /**
   * Sets the settlement date.
   * @param settlementDate  the new value of the property, not null
   */
  public void setSettlementDate(ZonedDateTime settlementDate) {
    JodaBeanUtils.notNull(settlementDate, "settlementDate");
    this._settlementDate = settlementDate;
  }

  /**
   * Gets the the {@code settlementDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTime> settlementDate() {
    return metaBean().settlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the barrier type.
   * @return the value of the property, not null
   */
  public BarrierType getBarrierType() {
    return _barrierType;
  }

  /**
   * Sets the barrier type.
   * @param barrierType  the new value of the property, not null
   */
  public void setBarrierType(BarrierType barrierType) {
    JodaBeanUtils.notNull(barrierType, "barrierType");
    this._barrierType = barrierType;
  }

  /**
   * Gets the the {@code barrierType} property.
   * @return the property, not null
   */
  public final Property<BarrierType> barrierType() {
    return metaBean().barrierType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the barrier direction.
   * @return the value of the property, not null
   */
  public BarrierDirection getBarrierDirection() {
    return _barrierDirection;
  }

  /**
   * Sets the barrier direction.
   * @param barrierDirection  the new value of the property, not null
   */
  public void setBarrierDirection(BarrierDirection barrierDirection) {
    JodaBeanUtils.notNull(barrierDirection, "barrierDirection");
    this._barrierDirection = barrierDirection;
  }

  /**
   * Gets the the {@code barrierDirection} property.
   * @return the property, not null
   */
  public final Property<BarrierDirection> barrierDirection() {
    return metaBean().barrierDirection().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the monitoring type.
   * @return the value of the property, not null
   */
  public MonitoringType getMonitoringType() {
    return _monitoringType;
  }

  /**
   * Sets the monitoring type.
   * @param monitoringType  the new value of the property, not null
   */
  public void setMonitoringType(MonitoringType monitoringType) {
    JodaBeanUtils.notNull(monitoringType, "monitoringType");
    this._monitoringType = monitoringType;
  }

  /**
   * Gets the the {@code monitoringType} property.
   * @return the property, not null
   */
  public final Property<MonitoringType> monitoringType() {
    return metaBean().monitoringType().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the sampling frequency.
   * @return the value of the property, not null
   */
  public SamplingFrequency getSamplingFrequency() {
    return _samplingFrequency;
  }

  /**
   * Sets the sampling frequency.
   * @param samplingFrequency  the new value of the property, not null
   */
  public void setSamplingFrequency(SamplingFrequency samplingFrequency) {
    JodaBeanUtils.notNull(samplingFrequency, "samplingFrequency");
    this._samplingFrequency = samplingFrequency;
  }

  /**
   * Gets the the {@code samplingFrequency} property.
   * @return the property, not null
   */
  public final Property<SamplingFrequency> samplingFrequency() {
    return metaBean().samplingFrequency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the barrier level.
   * @return the value of the property, not null
   */
  public double getBarrierLevel() {
    return _barrierLevel;
  }

  /**
   * Sets the barrier level.
   * @param barrierLevel  the new value of the property, not null
   */
  public void setBarrierLevel(double barrierLevel) {
    JodaBeanUtils.notNull(barrierLevel, "barrierLevel");
    this._barrierLevel = barrierLevel;
  }

  /**
   * Gets the the {@code barrierLevel} property.
   * @return the property, not null
   */
  public final Property<Double> barrierLevel() {
    return metaBean().barrierLevel().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the long/short type.
   * @return the value of the property, not null
   */
  public LongShort getLongShort() {
    return _longShort;
  }

  /**
   * Sets the long/short type.
   * @param longShort  the new value of the property, not null
   */
  public void setLongShort(LongShort longShort) {
    JodaBeanUtils.notNull(longShort, "longShort");
    this._longShort = longShort;
  }

  /**
   * Gets the the {@code longShort} property.
   * @return the property, not null
   */
  public final Property<LongShort> longShort() {
    return metaBean().longShort().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FXBarrierOptionSecurity}.
   */
  public static class Meta extends FinancialSecurity.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code putCurrency} property.
     */
    private final MetaProperty<Currency> _putCurrency = DirectMetaProperty.ofReadWrite(
        this, "putCurrency", FXBarrierOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code callCurrency} property.
     */
    private final MetaProperty<Currency> _callCurrency = DirectMetaProperty.ofReadWrite(
        this, "callCurrency", FXBarrierOptionSecurity.class, Currency.class);
    /**
     * The meta-property for the {@code putAmount} property.
     */
    private final MetaProperty<Double> _putAmount = DirectMetaProperty.ofReadWrite(
        this, "putAmount", FXBarrierOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code callAmount} property.
     */
    private final MetaProperty<Double> _callAmount = DirectMetaProperty.ofReadWrite(
        this, "callAmount", FXBarrierOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<Expiry> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", FXBarrierOptionSecurity.class, Expiry.class);
    /**
     * The meta-property for the {@code settlementDate} property.
     */
    private final MetaProperty<ZonedDateTime> _settlementDate = DirectMetaProperty.ofReadWrite(
        this, "settlementDate", FXBarrierOptionSecurity.class, ZonedDateTime.class);
    /**
     * The meta-property for the {@code barrierType} property.
     */
    private final MetaProperty<BarrierType> _barrierType = DirectMetaProperty.ofReadWrite(
        this, "barrierType", FXBarrierOptionSecurity.class, BarrierType.class);
    /**
     * The meta-property for the {@code barrierDirection} property.
     */
    private final MetaProperty<BarrierDirection> _barrierDirection = DirectMetaProperty.ofReadWrite(
        this, "barrierDirection", FXBarrierOptionSecurity.class, BarrierDirection.class);
    /**
     * The meta-property for the {@code monitoringType} property.
     */
    private final MetaProperty<MonitoringType> _monitoringType = DirectMetaProperty.ofReadWrite(
        this, "monitoringType", FXBarrierOptionSecurity.class, MonitoringType.class);
    /**
     * The meta-property for the {@code samplingFrequency} property.
     */
    private final MetaProperty<SamplingFrequency> _samplingFrequency = DirectMetaProperty.ofReadWrite(
        this, "samplingFrequency", FXBarrierOptionSecurity.class, SamplingFrequency.class);
    /**
     * The meta-property for the {@code barrierLevel} property.
     */
    private final MetaProperty<Double> _barrierLevel = DirectMetaProperty.ofReadWrite(
        this, "barrierLevel", FXBarrierOptionSecurity.class, Double.TYPE);
    /**
     * The meta-property for the {@code longShort} property.
     */
    private final MetaProperty<LongShort> _longShort = DirectMetaProperty.ofReadWrite(
        this, "longShort", FXBarrierOptionSecurity.class, LongShort.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "putCurrency",
        "callCurrency",
        "putAmount",
        "callAmount",
        "expiry",
        "settlementDate",
        "barrierType",
        "barrierDirection",
        "monitoringType",
        "samplingFrequency",
        "barrierLevel",
        "longShort");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 516393024:  // putCurrency
          return _putCurrency;
        case 643534991:  // callCurrency
          return _callCurrency;
        case -984864697:  // putAmount
          return _putAmount;
        case 1066661974:  // callAmount
          return _callAmount;
        case -1289159373:  // expiry
          return _expiry;
        case -295948169:  // settlementDate
          return _settlementDate;
        case 1029043089:  // barrierType
          return _barrierType;
        case 502579592:  // barrierDirection
          return _barrierDirection;
        case -1483652190:  // monitoringType
          return _monitoringType;
        case 1178782005:  // samplingFrequency
          return _samplingFrequency;
        case 1827586573:  // barrierLevel
          return _barrierLevel;
        case 116685664:  // longShort
          return _longShort;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FXBarrierOptionSecurity> builder() {
      return new DirectBeanBuilder<FXBarrierOptionSecurity>(new FXBarrierOptionSecurity());
    }

    @Override
    public Class<? extends FXBarrierOptionSecurity> beanType() {
      return FXBarrierOptionSecurity.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code putCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> putCurrency() {
      return _putCurrency;
    }

    /**
     * The meta-property for the {@code callCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Currency> callCurrency() {
      return _callCurrency;
    }

    /**
     * The meta-property for the {@code putAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> putAmount() {
      return _putAmount;
    }

    /**
     * The meta-property for the {@code callAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> callAmount() {
      return _callAmount;
    }

    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Expiry> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code settlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTime> settlementDate() {
      return _settlementDate;
    }

    /**
     * The meta-property for the {@code barrierType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BarrierType> barrierType() {
      return _barrierType;
    }

    /**
     * The meta-property for the {@code barrierDirection} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<BarrierDirection> barrierDirection() {
      return _barrierDirection;
    }

    /**
     * The meta-property for the {@code monitoringType} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<MonitoringType> monitoringType() {
      return _monitoringType;
    }

    /**
     * The meta-property for the {@code samplingFrequency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<SamplingFrequency> samplingFrequency() {
      return _samplingFrequency;
    }

    /**
     * The meta-property for the {@code barrierLevel} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> barrierLevel() {
      return _barrierLevel;
    }

    /**
     * The meta-property for the {@code longShort} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<LongShort> longShort() {
      return _longShort;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
