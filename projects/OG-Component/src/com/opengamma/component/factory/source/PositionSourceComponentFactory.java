/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.source;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.ehcache.CacheManager;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.component.factory.ComponentInfoAttributes;
import com.opengamma.core.position.PositionSource;
import com.opengamma.core.position.impl.DataPositionSourceResource;
import com.opengamma.core.position.impl.EHCachingPositionSource;
import com.opengamma.core.position.impl.RemotePositionSource;
import com.opengamma.master.portfolio.PortfolioMaster;
import com.opengamma.master.position.PositionMaster;
import com.opengamma.master.position.impl.MasterPositionSource;

/**
 * Component factory for the position source.
 */
@BeanDefinition
public class PositionSourceComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The flag determining whether the component should be published by REST (default true).
   */
  @PropertyDefinition
  private boolean _publishRest = true;
  /**
   * The cache manager.
   */
  @PropertyDefinition
  private CacheManager _cacheManager;
  /**
   * The portfolio master.
   */
  @PropertyDefinition(validate = "notNull")
  private PortfolioMaster _portfolioMaster;
  /**
   * The position master.
   */
  @PropertyDefinition(validate = "notNull")
  private PositionMaster _positionMaster;

  //-------------------------------------------------------------------------
  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) {
    ComponentInfo info = new ComponentInfo(PositionSource.class, getClassifier());
    info.addAttribute(ComponentInfoAttributes.LEVEL, 1);
    info.addAttribute(ComponentInfoAttributes.REMOTE_CLIENT_JAVA, RemotePositionSource.class);
    
    PositionSource source = new MasterPositionSource(getPortfolioMaster(), getPositionMaster());
    if (getCacheManager() != null) {
      source = new EHCachingPositionSource(source, getCacheManager());
    }
    
    repo.registerComponent(info, source);
    if (isPublishRest()) {
      repo.getRestComponents().publish(info, new DataPositionSourceResource(source));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code PositionSourceComponentFactory}.
   * @return the meta-bean, not null
   */
  public static PositionSourceComponentFactory.Meta meta() {
    return PositionSourceComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(PositionSourceComponentFactory.Meta.INSTANCE);
  }

  @Override
  public PositionSourceComponentFactory.Meta metaBean() {
    return PositionSourceComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        return getClassifier();
      case -614707837:  // publishRest
        return isPublishRest();
      case -1452875317:  // cacheManager
        return getCacheManager();
      case -772274742:  // portfolioMaster
        return getPortfolioMaster();
      case -1840419605:  // positionMaster
        return getPositionMaster();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        setClassifier((String) newValue);
        return;
      case -614707837:  // publishRest
        setPublishRest((Boolean) newValue);
        return;
      case -1452875317:  // cacheManager
        setCacheManager((CacheManager) newValue);
        return;
      case -772274742:  // portfolioMaster
        setPortfolioMaster((PortfolioMaster) newValue);
        return;
      case -1840419605:  // positionMaster
        setPositionMaster((PositionMaster) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_classifier, "classifier");
    JodaBeanUtils.notNull(_portfolioMaster, "portfolioMaster");
    JodaBeanUtils.notNull(_positionMaster, "positionMaster");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      PositionSourceComponentFactory other = (PositionSourceComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(isPublishRest(), other.isPublishRest()) &&
          JodaBeanUtils.equal(getCacheManager(), other.getCacheManager()) &&
          JodaBeanUtils.equal(getPortfolioMaster(), other.getPortfolioMaster()) &&
          JodaBeanUtils.equal(getPositionMaster(), other.getPositionMaster()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(isPublishRest());
    hash += hash * 31 + JodaBeanUtils.hashCode(getCacheManager());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPortfolioMaster());
    hash += hash * 31 + JodaBeanUtils.hashCode(getPositionMaster());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier that the factory should publish under.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier that the factory should publish under.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
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
   * Gets the flag determining whether the component should be published by REST (default true).
   * @return the value of the property
   */
  public boolean isPublishRest() {
    return _publishRest;
  }

  /**
   * Sets the flag determining whether the component should be published by REST (default true).
   * @param publishRest  the new value of the property
   */
  public void setPublishRest(boolean publishRest) {
    this._publishRest = publishRest;
  }

  /**
   * Gets the the {@code publishRest} property.
   * @return the property, not null
   */
  public final Property<Boolean> publishRest() {
    return metaBean().publishRest().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the cache manager.
   * @return the value of the property
   */
  public CacheManager getCacheManager() {
    return _cacheManager;
  }

  /**
   * Sets the cache manager.
   * @param cacheManager  the new value of the property
   */
  public void setCacheManager(CacheManager cacheManager) {
    this._cacheManager = cacheManager;
  }

  /**
   * Gets the the {@code cacheManager} property.
   * @return the property, not null
   */
  public final Property<CacheManager> cacheManager() {
    return metaBean().cacheManager().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the portfolio master.
   * @return the value of the property, not null
   */
  public PortfolioMaster getPortfolioMaster() {
    return _portfolioMaster;
  }

  /**
   * Sets the portfolio master.
   * @param portfolioMaster  the new value of the property, not null
   */
  public void setPortfolioMaster(PortfolioMaster portfolioMaster) {
    JodaBeanUtils.notNull(portfolioMaster, "portfolioMaster");
    this._portfolioMaster = portfolioMaster;
  }

  /**
   * Gets the the {@code portfolioMaster} property.
   * @return the property, not null
   */
  public final Property<PortfolioMaster> portfolioMaster() {
    return metaBean().portfolioMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the position master.
   * @return the value of the property, not null
   */
  public PositionMaster getPositionMaster() {
    return _positionMaster;
  }

  /**
   * Sets the position master.
   * @param positionMaster  the new value of the property, not null
   */
  public void setPositionMaster(PositionMaster positionMaster) {
    JodaBeanUtils.notNull(positionMaster, "positionMaster");
    this._positionMaster = positionMaster;
  }

  /**
   * Gets the the {@code positionMaster} property.
   * @return the property, not null
   */
  public final Property<PositionMaster> positionMaster() {
    return metaBean().positionMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code PositionSourceComponentFactory}.
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
        this, "classifier", PositionSourceComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code publishRest} property.
     */
    private final MetaProperty<Boolean> _publishRest = DirectMetaProperty.ofReadWrite(
        this, "publishRest", PositionSourceComponentFactory.class, Boolean.TYPE);
    /**
     * The meta-property for the {@code cacheManager} property.
     */
    private final MetaProperty<CacheManager> _cacheManager = DirectMetaProperty.ofReadWrite(
        this, "cacheManager", PositionSourceComponentFactory.class, CacheManager.class);
    /**
     * The meta-property for the {@code portfolioMaster} property.
     */
    private final MetaProperty<PortfolioMaster> _portfolioMaster = DirectMetaProperty.ofReadWrite(
        this, "portfolioMaster", PositionSourceComponentFactory.class, PortfolioMaster.class);
    /**
     * The meta-property for the {@code positionMaster} property.
     */
    private final MetaProperty<PositionMaster> _positionMaster = DirectMetaProperty.ofReadWrite(
        this, "positionMaster", PositionSourceComponentFactory.class, PositionMaster.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "publishRest",
        "cacheManager",
        "portfolioMaster",
        "positionMaster");

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
        case -614707837:  // publishRest
          return _publishRest;
        case -1452875317:  // cacheManager
          return _cacheManager;
        case -772274742:  // portfolioMaster
          return _portfolioMaster;
        case -1840419605:  // positionMaster
          return _positionMaster;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends PositionSourceComponentFactory> builder() {
      return new DirectBeanBuilder<PositionSourceComponentFactory>(new PositionSourceComponentFactory());
    }

    @Override
    public Class<? extends PositionSourceComponentFactory> beanType() {
      return PositionSourceComponentFactory.class;
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
     * The meta-property for the {@code publishRest} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> publishRest() {
      return _publishRest;
    }

    /**
     * The meta-property for the {@code cacheManager} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CacheManager> cacheManager() {
      return _cacheManager;
    }

    /**
     * The meta-property for the {@code portfolioMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PortfolioMaster> portfolioMaster() {
      return _portfolioMaster;
    }

    /**
     * The meta-property for the {@code positionMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<PositionMaster> positionMaster() {
      return _positionMaster;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
