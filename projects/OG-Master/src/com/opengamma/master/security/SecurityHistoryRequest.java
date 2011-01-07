/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.master.security;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.time.InstantProvider;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.id.UniqueIdentifier;
import com.opengamma.master.AbstractHistoryRequest;

/**
 * Request for the history of a security.
 * <p>
 * A full security master implements historical storage of data.
 * History can be stored in two dimensions and this request provides searching.
 * <p>
 * The first historic dimension is the classic series of versions.
 * Each new version is stored in such a manor that previous versions can be accessed.
 * <p>
 * The second historic dimension is corrections.
 * A correction occurs when it is realized that the original data stored was incorrect.
 * A simple security master might simply replace the original version with the corrected value.
 * A full implementation will store the correction in such a manner that it is still possible
 * to obtain the value before the correction was made.
 * <p>
 * For example, a security added on Monday and updated on Thursday has two versions.
 * If it is realized on Friday that the version stored on Monday was incorrect, then a
 * correction may be applied. There are now two versions, the first of which has one correction.
 * This may continue, with multiple corrections allowed for each version.
 * <p>
 * Versions and corrections are represented by instants in the search.
 */
@BeanDefinition
public class SecurityHistoryRequest extends AbstractHistoryRequest {

  /**
   * The depth of security data to return.
   * False will only return the basic information held in the {@code ManageableSecurity} class.
   * True will load the full security subclass for each returned security.
   * By default this is true returning all the data.
   */
  @PropertyDefinition
  private boolean _fullDetail = true;

  /**
   * Creates an instance.
   * The object identifier must be added before searching.
   */
  public SecurityHistoryRequest() {
  }

  /**
   * Creates an instance with object identifier.
   * This will retrieve all versions and corrections unless the relevant fields are set.
   * 
   * @param oid  the object identifier, not null
   */
  public SecurityHistoryRequest(final UniqueIdentifier oid) {
    super(oid);
  }

  /**
   * Creates an instance with object identifier and optional version and correction.
   * 
   * @param oid  the object identifier, not null
   * @param versionInstantProvider  the version instant to retrieve, null for all versions
   * @param correctedToInstantProvider  the instant that the data should be corrected to, null for all corrections
   */
  public SecurityHistoryRequest(final UniqueIdentifier oid, InstantProvider versionInstantProvider, InstantProvider correctedToInstantProvider) {
    super(oid, versionInstantProvider, correctedToInstantProvider);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SecurityHistoryRequest}.
   * @return the meta-bean, not null
   */
  public static SecurityHistoryRequest.Meta meta() {
    return SecurityHistoryRequest.Meta.INSTANCE;
  }

  @Override
  public SecurityHistoryRequest.Meta metaBean() {
    return SecurityHistoryRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -1233600576:  // fullDetail
        return isFullDetail();
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -1233600576:  // fullDetail
        setFullDetail((Boolean) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the depth of security data to return.
   * False will only return the basic information held in the {@code DefaultSecurity} class.
   * True will load the full security subclass for each returned security.
   * By default this is false to save space in the response.
   * @return the value of the property
   */
  public boolean isFullDetail() {
    return _fullDetail;
  }

  /**
   * Sets the depth of security data to return.
   * False will only return the basic information held in the {@code DefaultSecurity} class.
   * True will load the full security subclass for each returned security.
   * By default this is false to save space in the response.
   * @param fullDetail  the new value of the property
   */
  public void setFullDetail(boolean fullDetail) {
    this._fullDetail = fullDetail;
  }

  /**
   * Gets the the {@code fullDetail} property.
   * False will only return the basic information held in the {@code DefaultSecurity} class.
   * True will load the full security subclass for each returned security.
   * By default this is false to save space in the response.
   * @return the property, not null
   */
  public final Property<Boolean> fullDetail() {
    return metaBean().fullDetail().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SecurityHistoryRequest}.
   */
  public static class Meta extends AbstractHistoryRequest.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code fullDetail} property.
     */
    private final MetaProperty<Boolean> _fullDetail = DirectMetaProperty.ofReadWrite(this, "fullDetail", Boolean.TYPE);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap(super.metaPropertyMap());
      temp.put("fullDetail", _fullDetail);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public SecurityHistoryRequest createBean() {
      return new SecurityHistoryRequest();
    }

    @Override
    public Class<? extends SecurityHistoryRequest> beanType() {
      return SecurityHistoryRequest.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code fullDetail} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> fullDetail() {
      return _fullDetail;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
