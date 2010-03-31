/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.model.future.definition;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.financial.model.interestrate.curve.DiscountCurve;

/**
 * @author emcleod
 *
 */
public class StandardFutureDataBundle extends FutureDataBundle {
  private final double _yield;
  private final double _storageCost;

  public StandardFutureDataBundle(final double yield, final DiscountCurve discountCurve, final double spot, final ZonedDateTime date, final double storageCost) {
    super(discountCurve, spot, date);
    if (storageCost < 0)
      throw new IllegalArgumentException("Storage cost cannot be negative");
    _yield = yield;
    _storageCost = storageCost;
  }

  public double getYield() {
    return _yield;
  }

  public double getStorageCost() {
    return _storageCost;
  }

  public FutureDataBundle withYield(final double newYield) {
    return new StandardFutureDataBundle(newYield, getDiscountCurve(), getSpot(), getDate(), getStorageCost());
  }

  @Override
  public FutureDataBundle withDate(final ZonedDateTime newDate) {
    if (newDate == null)
      throw new IllegalArgumentException("New date was null");
    return new StandardFutureDataBundle(getYield(), getDiscountCurve(), getSpot(), newDate, getStorageCost());
  }

  @Override
  public FutureDataBundle withDiscountCurve(final DiscountCurve newCurve) {
    if (newCurve == null)
      throw new IllegalArgumentException("New curve was null");
    return new StandardFutureDataBundle(getYield(), newCurve, getSpot(), getDate(), getStorageCost());
  }

  @Override
  public FutureDataBundle withSpot(final double newSpot) {
    if (newSpot < 0)
      throw new IllegalArgumentException("New spot was negative");
    return new StandardFutureDataBundle(getYield(), getDiscountCurve(), newSpot, getDate(), getStorageCost());
  }

  public FutureDataBundle withStorageCost(final double newStorageCost) {
    if (newStorageCost < 0)
      throw new IllegalArgumentException("New storage cost was negative");
    return new StandardFutureDataBundle(getYield(), getDiscountCurve(), getSpot(), getDate(), newStorageCost);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    long temp;
    temp = Double.doubleToLongBits(_storageCost);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_yield);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    final StandardFutureDataBundle other = (StandardFutureDataBundle) obj;
    if (Double.doubleToLongBits(_storageCost) != Double.doubleToLongBits(other._storageCost))
      return false;
    if (Double.doubleToLongBits(_yield) != Double.doubleToLongBits(other._yield))
      return false;
    return true;
  }

}
