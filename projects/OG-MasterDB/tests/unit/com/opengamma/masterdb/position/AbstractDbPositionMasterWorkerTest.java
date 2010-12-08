/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.masterdb.position;

import static com.opengamma.util.db.DbDateUtils.MAX_SQL_TIMESTAMP;
import static com.opengamma.util.db.DbDateUtils.toSqlDate;
import static com.opengamma.util.db.DbDateUtils.toSqlTimestamp;

import java.math.BigDecimal;
import java.util.TimeZone;

import javax.time.Instant;
import javax.time.TimeSource;
import javax.time.calendar.OffsetDateTime;
import javax.time.calendar.OffsetTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.opengamma.masterdb.DbMasterTestUtils;
import com.opengamma.util.test.DBTest;

/**
 * Base tests for DbPositionMasterWorker via DbPositionMaster.
 */
@Ignore
public abstract class AbstractDbPositionMasterWorkerTest extends DBTest {

  private static final Logger s_logger = LoggerFactory.getLogger(AbstractDbPositionMasterWorkerTest.class);

  protected DbPositionMaster _posMaster;
  protected Instant _version1Instant;
  protected Instant _version2Instant;
  protected int _totalPortfolios;
  protected int _totalPositions;
  protected OffsetDateTime _now;

  public AbstractDbPositionMasterWorkerTest(String databaseType, String databaseVersion) {
    super(databaseType, databaseVersion);
    s_logger.info("running testcases for {}", databaseType);
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    ConfigurableApplicationContext context = DbMasterTestUtils.getContext(getDatabaseType());
    _posMaster = (DbPositionMaster) context.getBean(getDatabaseType() + "DbPositionMaster");
    
    _now = OffsetDateTime.now();
    _posMaster.setTimeSource(TimeSource.fixed(_now.toInstant()));
    _version1Instant = _now.toInstant().minusSeconds(100);
    _version2Instant = _now.toInstant().minusSeconds(50);
    s_logger.debug("test data now:   {}", _version1Instant);
    s_logger.debug("test data later: {}", _version2Instant);
    final SimpleJdbcTemplate template = _posMaster.getDbSource().getJdbcTemplate();
    template.update("INSERT INTO pos_portfolio VALUES (?,?,?,?,?, ?,?)",
        101, 101, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, "TestPortfolio101");
    template.update("INSERT INTO pos_portfolio VALUES (?,?,?,?,?, ?,?)",
        201, 201, toSqlTimestamp(_version1Instant), toSqlTimestamp(_version2Instant), toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, "TestPortfolio201");
    template.update("INSERT INTO pos_portfolio VALUES (?,?,?,?,?, ?,?)",
        202, 201, toSqlTimestamp(_version2Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version2Instant), MAX_SQL_TIMESTAMP, "TestPortfolio202");
    _totalPortfolios = 2;
    template.update("INSERT INTO pos_node VALUES (?,?,?,?,?, ?,?,?,?)",
        111, 111, 101, 101, null, 0, 1, 6, "TestNode111");
    template.update("INSERT INTO pos_node VALUES (?,?,?,?,?, ?,?,?,?)",
        112, 112, 101, 101, 111, 1, 2, 5, "TestNode112");
    template.update("INSERT INTO pos_node VALUES (?,?,?,?,?, ?,?,?,?)",
        113, 113, 101, 101, 112, 2, 3, 4, "TestNode113");
    template.update("INSERT INTO pos_node VALUES (?,?,?,?,?, ?,?,?,?)",
        211, 211, 201, 201, null, 0, 1, 2, "TestNode211");
    template.update("INSERT INTO pos_node VALUES (?,?,?,?,?, ?,?,?,?)",
        212, 211, 202, 201, null, 0, 1, 2, "TestNode212");
    
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        100, 100, 101, 112, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(100.987));
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        120, 120, 101, 112, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(120.987));
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        121, 121, 101, 112, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(121.987));
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        122, 122, 101, 112, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(122.987));
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        123, 123, 101, 112, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(123.987));
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        221, 221, 201, 211, toSqlTimestamp(_version1Instant), toSqlTimestamp(_version2Instant), toSqlTimestamp(_version1Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(221.987));
    template.update("INSERT INTO pos_position VALUES (?,?,?,?,?, ?,?,?,?)",
        222, 221, 201, 211, toSqlTimestamp(_version2Instant), MAX_SQL_TIMESTAMP, toSqlTimestamp(_version2Instant), MAX_SQL_TIMESTAMP, BigDecimal.valueOf(222.987));
    _totalPositions = 6;
    
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        500, "TICKER", "S100");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        501, "TICKER", "T130");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        502, "TICKER", "MSFT");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        503, "NASDAQ", "Micro");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        504, "TICKER", "ORCL");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        505, "TICKER", "ORCL134");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        506, "NASDAQ", "ORCL135");
    template.update("INSERT INTO pos_idkey VALUES (?,?,?)",
        507, "TICKER", "IBMC");
    
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 100, 500);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 120, 501);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 121, 502);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 121, 503);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 122, 504);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 123, 505);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 123, 506);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 221, 507);
    template.update("INSERT INTO pos_position2idkey VALUES (?,?)", 222, 507);
    
    OffsetTime tradeTime = _now.toOffsetTime().minusSeconds(220);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        400, 400, 120, 120, BigDecimal.valueOf(120.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C100");
    tradeTime = _now.toOffsetTime().minusSeconds(221);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        401, 401, 121, 121, BigDecimal.valueOf(121.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C101");
    tradeTime = _now.toOffsetTime().minusSeconds(222);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        402, 402, 122, 122, BigDecimal.valueOf(100.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "JMP");
    tradeTime = _now.toOffsetTime().minusSeconds(222);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        403, 403, 122, 122, BigDecimal.valueOf(22.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "CISC");
    tradeTime = _now.toOffsetTime().minusSeconds(223);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        404, 404, 123, 123, BigDecimal.valueOf(100.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C104");
    tradeTime = _now.toOffsetTime().minusSeconds(223);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        405, 405, 123, 123, BigDecimal.valueOf(200.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C105");
    tradeTime = _now.toOffsetTime().minusSeconds(223);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        406, 406, 123, 123, BigDecimal.valueOf(300.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C106");
    tradeTime = _now.toOffsetTime().minusSeconds(200);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        407, 407, 221, 221, BigDecimal.valueOf(221.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C221");
    tradeTime = _now.toOffsetTime().minusSeconds(150);
    template.update("INSERT INTO pos_trade VALUES(?,?,?,?,?,?,?,?,?,?)", 
        408, 407, 222, 221, BigDecimal.valueOf(222.987), toSqlDate(_now.toLocalDate()), toSqlTimestamp(tradeTime), tradeTime.getOffset().getAmountSeconds(), "CPARTY", "C222");
    
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 400, 501);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 401, 502);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 401, 503);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 402, 504);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 403, 504);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 404, 505);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 404, 506);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 405, 505);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 405, 506);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 406, 505);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 406, 506);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 407, 507);
    template.update("INSERT INTO pos_trade2idkey VALUES (?,?)", 408, 507);
    
  }

  @After
  public void tearDown() throws Exception {
    _posMaster = null;
    super.tearDown();
  }

}
