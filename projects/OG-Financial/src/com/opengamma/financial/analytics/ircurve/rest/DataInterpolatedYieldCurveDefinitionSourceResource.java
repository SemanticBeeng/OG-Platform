/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve.rest;

import java.net.URI;

import javax.time.Instant;
import javax.time.InstantProvider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.opengamma.financial.analytics.ircurve.InterpolatedYieldCurveDefinitionSource;
import com.opengamma.financial.analytics.ircurve.YieldCurveDefinition;
import com.opengamma.id.VersionCorrection;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;

/**
 * RESTful resource for the yield curve source.
 * <p>
 * This resource receives and processes RESTful calls to the source.
 */
@Path("yieldCurveDefinitionSource")
public class DataInterpolatedYieldCurveDefinitionSourceResource {

  /**
   * The source.
   */
  private final InterpolatedYieldCurveDefinitionSource _source;

  /**
   * Creates the resource, exposing the underlying source over REST.
   * 
   * @param source  the underlying source, not null
   */
  public DataInterpolatedYieldCurveDefinitionSourceResource(final InterpolatedYieldCurveDefinitionSource source) {
    ArgumentChecker.notNull(source, "source");
    _source = source;
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the source.
   * 
   * @return the source, not null
   */
  public InterpolatedYieldCurveDefinitionSource getInterpolatedYieldCurveDefinitionSource() {
    return _source;
  }

  //-------------------------------------------------------------------------
  @GET
  @Path("definitions/searchSingle")
  public Response searchSingle(
      @QueryParam("currency") String currencyStr,
      @QueryParam("versionAsOf") String versionAsOfStr,
      @QueryParam("name") String name) {
    final Currency currency = Currency.parse(currencyStr);
    if (versionAsOfStr != null) {
      final Instant versionAsOf = VersionCorrection.parse(versionAsOfStr, null).getVersionAsOf();
      YieldCurveDefinition result = getInterpolatedYieldCurveDefinitionSource().getDefinition(currency, name, versionAsOf);
      return Response.ok(result).build();
    } else {
      YieldCurveDefinition result = getInterpolatedYieldCurveDefinitionSource().getDefinition(currency, name);
      return Response.ok(result).build();
    }
  }

  /**
   * Builds a URI.
   * 
   * @param baseUri  the base URI, not null
   * @param currency  the currency, not null
   * @param name  the name, not null
   * @param versionAsOf  the version to fetch, null means latest
   * @return the URI, not null
   */
  public static URI uriSearchSingle(URI baseUri, Currency currency, String name, InstantProvider versionAsOf) {
    UriBuilder bld = UriBuilder.fromUri(baseUri).path("/definitions/searchSingle");
    bld.queryParam("currency", currency.toString());
    bld.queryParam("name", name);
    if (versionAsOf != null) {
      bld.queryParam("versionAsOf", versionAsOf.toInstant().toString());
    }
    return bld.build();
  }

}
