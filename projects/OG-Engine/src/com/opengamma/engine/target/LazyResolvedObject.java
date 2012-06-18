/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.target;

import java.io.ObjectStreamException;
import java.io.Serializable;

import com.opengamma.engine.ComputationTargetResolver;

/**
 * Base class for lazily resolved object.
 */
/* package */abstract class LazyResolvedObject<T> implements Serializable {

  private final LazyResolveContext _context;
  private final T _underlying;

  protected LazyResolvedObject(final LazyResolveContext context, final T underlying) {
    _context = context;
    _underlying = underlying;
  }

  protected LazyResolveContext getLazyResolveContext() {
    return _context;
  }

  protected T getUnderlying() {
    return _underlying;
  }

  protected abstract TargetResolverObject targetResolverObject(final ComputationTargetResolver resolver);

  protected abstract Serializable simpleObject();

  public Object writeReplace() throws ObjectStreamException {
    if (getLazyResolveContext().getTargetResolver() != null) {
      getLazyResolveContext().beginWrite();
      try {
        return targetResolverObject(getLazyResolveContext().getTargetResolver());
      } finally {
        getLazyResolveContext().endWrite();
      }
    } else {
      return simpleObject();
    }
  }

}
