/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.math.rootfinding;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;

import com.opengamma.financial.interestrate.InterestRateDerivative;
import com.opengamma.financial.interestrate.InterestRateDerivativeVisitor;
import com.opengamma.financial.interestrate.MultipleYieldCurveFinderDataBundle;
import com.opengamma.financial.interestrate.MultipleYieldCurveFinderFunction;
import com.opengamma.financial.interestrate.MultipleYieldCurveFinderJacobian;
import com.opengamma.financial.interestrate.ParRateCalculator;
import com.opengamma.financial.interestrate.ParRateCurveSensitivityCalculator;
import com.opengamma.financial.interestrate.ParRateDifferenceCalculator;
import com.opengamma.financial.interestrate.YieldCurveBundle;
import com.opengamma.financial.interestrate.annuity.definition.ConstantCouponAnnuity;
import com.opengamma.financial.interestrate.annuity.definition.VariableAnnuity;
import com.opengamma.financial.interestrate.swap.definition.FixedFloatSwap;
import com.opengamma.financial.model.interestrate.curve.InterpolatedYieldCurve;
import com.opengamma.financial.model.interestrate.curve.YieldAndDiscountCurve;
import com.opengamma.math.function.Function1D;
import com.opengamma.math.interpolation.CombinedInterpolatorExtrapolator;
import com.opengamma.math.interpolation.FlatExtrapolator1D;
import com.opengamma.math.interpolation.Interpolator1D;
import com.opengamma.math.interpolation.LinearExtrapolator1D;
import com.opengamma.math.interpolation.NaturalCubicSplineInterpolator1D;
import com.opengamma.math.interpolation.data.Interpolator1DCubicSplineDataBundle;
import com.opengamma.math.interpolation.data.Interpolator1DDataBundle;
import com.opengamma.math.interpolation.sensitivity.CombinedInterpolatorExtrapolatorNodeSensitivityCalculator;
import com.opengamma.math.interpolation.sensitivity.FiniteDifferenceInterpolator1DNodeSensitivityCalculator;
import com.opengamma.math.interpolation.sensitivity.FlatExtrapolator1DNodeSensitivityCalculator;
import com.opengamma.math.interpolation.sensitivity.Interpolator1DNodeSensitivityCalculator;
import com.opengamma.math.interpolation.sensitivity.LinearExtrapolator1DNodeSensitivityCalculator;
import com.opengamma.math.interpolation.sensitivity.NaturalCubicSplineInterpolator1DNodeSensitivityCalculator;
import com.opengamma.math.matrix.DoubleMatrix1D;
import com.opengamma.math.matrix.DoubleMatrix2D;
import com.opengamma.math.rootfinding.newton.BroydenVectorRootFinder;
import com.opengamma.math.rootfinding.newton.FiniteDifferenceJacobianCalculator;
import com.opengamma.math.rootfinding.newton.JacobianCalculator;
import com.opengamma.math.rootfinding.newton.NewtonDefaultVectorRootFinder;
import com.opengamma.math.rootfinding.newton.ShermanMorrisonVectorRootFinder;
import com.opengamma.math.statistics.distribution.NormalDistribution;
import com.opengamma.util.monitor.OperationTimer;
import com.opengamma.util.tuple.Pair;

/**
 * 
 */
public class YieldCurveBootStrapTest {
  private static final Logger s_logger = LoggerFactory.getLogger(YieldCurveBootStrapTest.class);
  private static final int HOTSPOT_WARMUP_CYCLES = 100;
  private static final int BENCHMARK_CYCLES = 1000;
  private static final RandomEngine RANDOM = new MersenneTwister64(MersenneTwister64.DEFAULT_SEED);

  private static final Interpolator1D<Interpolator1DCubicSplineDataBundle> EXTRAPOLATOR;
  private static final Interpolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle> EXTRAPOLATOR_WITH_SENSITIVITY;
  private static final Interpolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle> EXTRAPOLATOR_WITH_FD_SENSITIVITY;

  private static final InterestRateDerivativeVisitor<Double> PAR_RATE_CALCULATOR = ParRateDifferenceCalculator.getInstance();
  private static final InterestRateDerivativeVisitor<Map<String, List<Pair<Double, Double>>>> PAR_RATE_SENSITIVITY_CALCULATOR = ParRateCurveSensitivityCalculator.getInstance();

  private static List<InterestRateDerivative> SINGLE_CURVE_INSTRUMENTS;
  private static List<InterestRateDerivative> DOUBLE_CURVE_INSTRUMENTS;
  private static double[] SWAP_VALUES;
  private static final double[] TIME_GRID;
  private static final double EPS = 1e-8;
  private static final int STEPS = 100;
  private static final DoubleMatrix1D X0;

  private static final ParRateCalculator SWAP_RATE_CALCULATOR = ParRateCalculator.getInstance();
  private static final Function1D<DoubleMatrix1D, DoubleMatrix1D> SINGLE_CURVE_FINDER;
  private static final Function1D<DoubleMatrix1D, DoubleMatrix1D> SINGLE_CURVE_FINDER_FD;
  private static final Function1D<DoubleMatrix1D, DoubleMatrix1D> DOUBLE_CURVE_FINDER;
  private static final Function1D<DoubleMatrix1D, DoubleMatrix1D> DOUBLE_CURVE_FINDER_FD;
  private static final JacobianCalculator SINGLE_CURVE_JACOBIAN;
  private static final JacobianCalculator DOUBLE_CURVE_JACOBIAN;
  private static final JacobianCalculator SINGLE_CURVE_JACOBIAN_FD;
  private static final JacobianCalculator DOUBLE_CURVE_JACOBIAN_FD;
  private static final String FUNDING_CURVE_NAME = "Repo";
  private static final String LIBOR_CURVE_NAME = "Libor_3m_USD";
  private static YieldAndDiscountCurve FUNDING_CURVE;
  private static YieldAndDiscountCurve LIBOR_CURVE;
  private static final double[] FUNDING_CURVE_TIMES;
  private static final double[] LIBOR_CURVE_TIMES;
  private static final double[] FUNDING_YIELDS;
  private static final double[] LIBOR_YIELDS;

  static {
    final int[] payments = new int[] {1, 2, 3, 4, 6, 8, 10, 14, 20, 30, 40, 50, 60};

    FUNDING_CURVE_TIMES = new double[] {1, 2, 5, 10, 20, 31};
    LIBOR_CURVE_TIMES = new double[] {0.5, 1, 2, 5, 10, 20, 31};
    FUNDING_YIELDS = new double[] {0.021, 0.036, 0.06, 0.054, 0.049, 0.044};
    LIBOR_YIELDS = new double[] {0.01, 0.02, 0.035, 0.06, 0.055, 0.05, 0.045};

    final int n = payments.length;
    TIME_GRID = new double[n];
    SWAP_VALUES = new double[n];
    SINGLE_CURVE_INSTRUMENTS = new ArrayList<InterestRateDerivative>();
    DOUBLE_CURVE_INSTRUMENTS = new ArrayList<InterestRateDerivative>();

    final NaturalCubicSplineInterpolator1D cubicInterpolator = new NaturalCubicSplineInterpolator1D();
    final NaturalCubicSplineInterpolator1DNodeSensitivityCalculator cubicSensitivityCalculator = new NaturalCubicSplineInterpolator1DNodeSensitivityCalculator();
    final LinearExtrapolator1D<Interpolator1DCubicSplineDataBundle> linearExtrapolator = new LinearExtrapolator1D<Interpolator1DCubicSplineDataBundle>(cubicInterpolator);
    final LinearExtrapolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle> linearExtrapolatorSensitivityCalculator = new LinearExtrapolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle>(
        cubicSensitivityCalculator);
    final FlatExtrapolator1D<Interpolator1DCubicSplineDataBundle> flatExtrapolator = new FlatExtrapolator1D<Interpolator1DCubicSplineDataBundle>();
    final FlatExtrapolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle> flatExtrapolatorSensitivityCalculator = new FlatExtrapolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle>();
    EXTRAPOLATOR = new CombinedInterpolatorExtrapolator<Interpolator1DCubicSplineDataBundle>(cubicInterpolator, linearExtrapolator, flatExtrapolator);
    EXTRAPOLATOR_WITH_SENSITIVITY = new CombinedInterpolatorExtrapolatorNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle>(cubicSensitivityCalculator,
        linearExtrapolatorSensitivityCalculator, flatExtrapolatorSensitivityCalculator);
    EXTRAPOLATOR_WITH_FD_SENSITIVITY = new FiniteDifferenceInterpolator1DNodeSensitivityCalculator<Interpolator1DCubicSplineDataBundle>(EXTRAPOLATOR);

    final YieldCurveBundle curveBundle = new YieldCurveBundle();
    FUNDING_CURVE = makeYieldCurve(FUNDING_YIELDS, FUNDING_CURVE_TIMES, EXTRAPOLATOR);
    LIBOR_CURVE = makeYieldCurve(LIBOR_YIELDS, LIBOR_CURVE_TIMES, EXTRAPOLATOR);
    curveBundle.setCurve(FUNDING_CURVE_NAME, FUNDING_CURVE);
    curveBundle.setCurve(LIBOR_CURVE_NAME, LIBOR_CURVE);

    InterestRateDerivative instrument;
    double swapRate;
    for (int i = 0; i < n; i++) {
      instrument = setupSwap(payments[i], 0.0, FUNDING_CURVE_NAME, LIBOR_CURVE_NAME);
      swapRate = SWAP_RATE_CALCULATOR.getValue(instrument, curveBundle);
      instrument = setupSwap(payments[i], swapRate, FUNDING_CURVE_NAME, LIBOR_CURVE_NAME);
      DOUBLE_CURVE_INSTRUMENTS.add(instrument);
      instrument = setupSwap(payments[i], swapRate, LIBOR_CURVE_NAME, LIBOR_CURVE_NAME);
      SINGLE_CURVE_INSTRUMENTS.add(instrument);
      TIME_GRID[i] = payments[i] * 0.5;
      SWAP_VALUES[i] = swapRate;
    }

    LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>> unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    LinkedHashMap<String, double[]> unknownCurveNodes = new LinkedHashMap<String, double[]>();
    LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>> unknownCurveNodeSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(LIBOR_CURVE_NAME, TIME_GRID);
    unknownCurveNodeSensitivityCalculators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR_WITH_SENSITIVITY);
    MultipleYieldCurveFinderDataBundle data = new MultipleYieldCurveFinderDataBundle(SINGLE_CURVE_INSTRUMENTS, null, unknownCurveNodes, unknownCurveInterpolators,
        unknownCurveNodeSensitivityCalculators);
    SINGLE_CURVE_FINDER = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);
    SINGLE_CURVE_JACOBIAN = new MultipleYieldCurveFinderJacobian(data, PAR_RATE_SENSITIVITY_CALCULATOR);
    unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    unknownCurveNodes = new LinkedHashMap<String, double[]>();
    unknownCurveNodeSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(LIBOR_CURVE_NAME, TIME_GRID);
    unknownCurveNodeSensitivityCalculators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR_WITH_FD_SENSITIVITY);
    data = new MultipleYieldCurveFinderDataBundle(SINGLE_CURVE_INSTRUMENTS, null, unknownCurveNodes, unknownCurveInterpolators, unknownCurveNodeSensitivityCalculators);
    SINGLE_CURVE_FINDER_FD = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);
    SINGLE_CURVE_JACOBIAN_FD = new MultipleYieldCurveFinderJacobian(data, PAR_RATE_SENSITIVITY_CALCULATOR);
    unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    unknownCurveNodes = new LinkedHashMap<String, double[]>();
    unknownCurveNodeSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(FUNDING_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveInterpolators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(FUNDING_CURVE_NAME, FUNDING_CURVE_TIMES);
    unknownCurveNodes.put(LIBOR_CURVE_NAME, LIBOR_CURVE_TIMES);
    unknownCurveNodeSensitivityCalculators.put(FUNDING_CURVE_NAME, EXTRAPOLATOR_WITH_SENSITIVITY);
    unknownCurveNodeSensitivityCalculators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR_WITH_SENSITIVITY);
    data = new MultipleYieldCurveFinderDataBundle(DOUBLE_CURVE_INSTRUMENTS, null, unknownCurveNodes, unknownCurveInterpolators, unknownCurveNodeSensitivityCalculators);
    DOUBLE_CURVE_FINDER = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);
    DOUBLE_CURVE_JACOBIAN = new MultipleYieldCurveFinderJacobian(data, PAR_RATE_SENSITIVITY_CALCULATOR);
    unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    unknownCurveNodes = new LinkedHashMap<String, double[]>();
    unknownCurveNodeSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(FUNDING_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveInterpolators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(FUNDING_CURVE_NAME, FUNDING_CURVE_TIMES);
    unknownCurveNodes.put(LIBOR_CURVE_NAME, LIBOR_CURVE_TIMES);
    unknownCurveNodeSensitivityCalculators.put(FUNDING_CURVE_NAME, EXTRAPOLATOR_WITH_FD_SENSITIVITY);
    unknownCurveNodeSensitivityCalculators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR_WITH_FD_SENSITIVITY);
    data = new MultipleYieldCurveFinderDataBundle(DOUBLE_CURVE_INSTRUMENTS, null, unknownCurveNodes, unknownCurveInterpolators, unknownCurveNodeSensitivityCalculators);
    DOUBLE_CURVE_FINDER_FD = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);
    DOUBLE_CURVE_JACOBIAN_FD = new MultipleYieldCurveFinderJacobian(data, PAR_RATE_SENSITIVITY_CALCULATOR);
    final double[] rates = new double[TIME_GRID.length];
    for (int i = 0; i < FUNDING_YIELDS.length; i++) {
      rates[i] = 0.05;
    }

    for (int i = 0; i < LIBOR_YIELDS.length; i++) {
      rates[i + FUNDING_YIELDS.length] = LIBOR_YIELDS[i] + 0.03;
    }
    X0 = new DoubleMatrix1D(rates);
  }

  @Test
  public void doNothing() {

  }

  @Test
  public void testNewton() {
    VectorRootFinder rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS);
    doHotSpot(rootFinder, "default Newton, single curve, finite difference", SINGLE_CURVE_FINDER);
    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN);
    doHotSpot(rootFinder, "default Newton, single curve", SINGLE_CURVE_FINDER);
    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN_FD);
    doHotSpot(rootFinder, "default Newton, single curve FD sensitivity", SINGLE_CURVE_FINDER_FD);

    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS);
    doHotSpot(rootFinder, "default Newton, double curve, finite difference", DOUBLE_CURVE_FINDER, true);
    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS, DOUBLE_CURVE_JACOBIAN);
    doHotSpot(rootFinder, "default Newton, double curve", DOUBLE_CURVE_FINDER, true);
    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS, DOUBLE_CURVE_JACOBIAN_FD);
    doHotSpot(rootFinder, "default Newton, double curve FD sensitivity", DOUBLE_CURVE_FINDER_FD, true);
  }

  @Test
  public void testShermanMorrison() {
    VectorRootFinder rootFinder = new ShermanMorrisonVectorRootFinder(EPS, EPS, STEPS);
    doHotSpot(rootFinder, "Sherman Morrison, single curve, finite difference", SINGLE_CURVE_FINDER);
    rootFinder = new ShermanMorrisonVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN);
    doHotSpot(rootFinder, "Sherman Morrison, single curve", SINGLE_CURVE_FINDER);
    rootFinder = new ShermanMorrisonVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN_FD);
    doHotSpot(rootFinder, "Sherman Morrison, single curve FD sensitivity", SINGLE_CURVE_FINDER_FD);

    rootFinder = new ShermanMorrisonVectorRootFinder(EPS, EPS, STEPS);
    doHotSpot(rootFinder, "Sherman Morrisonn, double curve, finite difference", DOUBLE_CURVE_FINDER, true);
    rootFinder = new ShermanMorrisonVectorRootFinder(EPS, EPS, STEPS, DOUBLE_CURVE_JACOBIAN);
    doHotSpot(rootFinder, "Sherman Morrison, double curve", DOUBLE_CURVE_FINDER, true);
    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS, DOUBLE_CURVE_JACOBIAN_FD);
    doHotSpot(rootFinder, "Sherman Morrison, double curve FD sensitivity", DOUBLE_CURVE_FINDER_FD, true);
  }

  @Test
  public void testBroyden() {
    VectorRootFinder rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS);
    doHotSpot(rootFinder, "Broyden, single curve, finite difference", SINGLE_CURVE_FINDER);
    rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN);
    doHotSpot(rootFinder, "Broyden, single curve", SINGLE_CURVE_FINDER);
    rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN_FD);
    doHotSpot(rootFinder, "Broyden, single curve, FD sensitivity", SINGLE_CURVE_FINDER_FD);

    rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS);
    doHotSpot(rootFinder, "Broyden, double curve, finite difference", DOUBLE_CURVE_FINDER, true);
    rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS, DOUBLE_CURVE_JACOBIAN);
    doHotSpot(rootFinder, "Broyden, double curve", DOUBLE_CURVE_FINDER, true);
    rootFinder = new NewtonDefaultVectorRootFinder(EPS, EPS, STEPS, DOUBLE_CURVE_JACOBIAN_FD);
    doHotSpot(rootFinder, "Broyden, double curve FD sensitivity", DOUBLE_CURVE_FINDER_FD, true);
  }

  private void doHotSpot(final VectorRootFinder rootFinder, final String name, final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor) {
    doHotSpot(rootFinder, name, functor, false);
  }

  private void doHotSpot(final VectorRootFinder rootFinder, final String name, final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor, final Boolean doubleCurveTest) {
    for (int i = 0; i < HOTSPOT_WARMUP_CYCLES; i++) {
      doTest(rootFinder, functor, doubleCurveTest);
    }
    if (BENCHMARK_CYCLES > 0) {
      final OperationTimer timer = new OperationTimer(s_logger, "processing {} cycles on " + name, BENCHMARK_CYCLES);
      for (int i = 0; i < BENCHMARK_CYCLES; i++) {
        doTest(rootFinder, functor, doubleCurveTest);
      }
      timer.finished();
    }
  }

  private void doTest(final VectorRootFinder rootFinder, final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor, final Boolean doubleCurveTest) {
    if (doubleCurveTest) {
      doTestForDoubleCurve(rootFinder, functor);
    } else {
      doTestForSingleCurve(rootFinder, functor);
    }
  }

  private void doTestForSingleCurve(final VectorRootFinder rootFinder, final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor) {
    final DoubleMatrix1D yieldCurveNodes = rootFinder.getRoot(functor, X0);
    final DoubleMatrix1D instrumentPVs = functor.evaluate(yieldCurveNodes);

    for (int i = 0; i < instrumentPVs.getNumberOfElements(); i++) {
      assertEquals(0.0, instrumentPVs.getEntry(i), EPS);
    }

    final YieldAndDiscountCurve curve = makeYieldCurve(yieldCurveNodes.getData(), TIME_GRID, EXTRAPOLATOR);
    final YieldCurveBundle bundle = new YieldCurveBundle();
    bundle.setCurve(LIBOR_CURVE_NAME, curve);
    for (int i = 0; i < SWAP_VALUES.length; i++) {
      assertEquals(SWAP_VALUES[i], SWAP_RATE_CALCULATOR.getValue(SINGLE_CURVE_INSTRUMENTS.get(i), bundle), EPS);
    }
  }

  private void doTestForDoubleCurve(final VectorRootFinder rootFinder, final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor) {
    final DoubleMatrix1D yieldCurveNodes = rootFinder.getRoot(functor, X0);
    final DoubleMatrix1D instrumentPVs = functor.evaluate(yieldCurveNodes);

    for (int i = 0; i < instrumentPVs.getNumberOfElements(); i++) {
      assertEquals(0.0, instrumentPVs.getEntry(i), EPS);
    }

    final double[] fundingYields = Arrays.copyOfRange(yieldCurveNodes.getData(), 0, FUNDING_CURVE_TIMES.length);
    final double[] liborYields = Arrays.copyOfRange(yieldCurveNodes.getData(), FUNDING_CURVE_TIMES.length, yieldCurveNodes.getNumberOfElements());
    for (int i = 0; i < FUNDING_CURVE_TIMES.length; i++) {
      assertEquals(FUNDING_YIELDS[i], fundingYields[i], EPS);
    }
    for (int i = 0; i < LIBOR_CURVE_TIMES.length; i++) {
      assertEquals(LIBOR_YIELDS[i], liborYields[i], EPS);
    }

    final YieldAndDiscountCurve fundingCurve = makeYieldCurve(fundingYields, FUNDING_CURVE_TIMES, EXTRAPOLATOR);
    final YieldAndDiscountCurve liborCurve = makeYieldCurve(liborYields, LIBOR_CURVE_TIMES, EXTRAPOLATOR);
    final YieldCurveBundle bundle = new YieldCurveBundle();
    bundle.setCurve(LIBOR_CURVE_NAME, liborCurve);
    bundle.setCurve(FUNDING_CURVE_NAME, fundingCurve);

    for (int i = 0; i < SWAP_VALUES.length; i++) {
      assertEquals(SWAP_VALUES[i], SWAP_RATE_CALCULATOR.getValue(DOUBLE_CURVE_INSTRUMENTS.get(i), bundle), EPS);
    }
  }

  @Test
  public void testTickingSwapRates() {
    final NormalDistribution normDist = new NormalDistribution(0, 1.0, RANDOM);
    final LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>> unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    final LinkedHashMap<String, double[]> unknownCurveNodes = new LinkedHashMap<String, double[]>();
    final LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>> unknownCurveSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(LIBOR_CURVE_NAME, TIME_GRID);
    unknownCurveSensitivityCalculators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR_WITH_SENSITIVITY);
    final VectorRootFinder rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS, SINGLE_CURVE_JACOBIAN);
    final double[] swapRates = Arrays.copyOf(SWAP_VALUES, SWAP_VALUES.length);
    DoubleMatrix1D yieldCurveNodes = X0;
    YieldAndDiscountCurve curve;
    final double sigma = 0.03;
    for (int t = 0; t < 100; t++) {
      final List<InterestRateDerivative> instruments = new ArrayList<InterestRateDerivative>();
      for (int i = 0; i < SWAP_VALUES.length; i++) {
        swapRates[i] *= Math.exp(-0.5 * sigma * sigma + sigma * normDist.nextRandom());
        final FixedFloatSwap swap = (FixedFloatSwap) SINGLE_CURVE_INSTRUMENTS.get(i);
        final ConstantCouponAnnuity fixedLeg = swap.getFixedLeg();
        final ConstantCouponAnnuity newLeg = new ConstantCouponAnnuity(fixedLeg.getPaymentTimes(), fixedLeg.getNotional(), swapRates[i], fixedLeg.getYearFractions(), fixedLeg.getFundingCurveName());
        final InterestRateDerivative ird = new FixedFloatSwap(newLeg, swap.getFloatingLeg());
        instruments.add(ird);
      }
      final MultipleYieldCurveFinderDataBundle data = new MultipleYieldCurveFinderDataBundle(instruments, null, unknownCurveNodes, unknownCurveInterpolators, unknownCurveSensitivityCalculators);
      final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);
      yieldCurveNodes = rootFinder.getRoot(functor, yieldCurveNodes);
      curve = makeYieldCurve(yieldCurveNodes.getData(), TIME_GRID, EXTRAPOLATOR);
      final YieldCurveBundle bundle = new YieldCurveBundle();
      bundle.setCurve(LIBOR_CURVE_NAME, curve);
      for (int i = 0; i < swapRates.length; i++) {
        assertEquals(swapRates[i], SWAP_RATE_CALCULATOR.getValue(SINGLE_CURVE_INSTRUMENTS.get(i), bundle), EPS);
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSingleCurveJacobian() {
    final JacobianCalculator jacobianFD = new FiniteDifferenceJacobianCalculator(1e-6);
    final DoubleMatrix2D jacExact = SINGLE_CURVE_JACOBIAN.evaluate(X0, SINGLE_CURVE_FINDER);
    final DoubleMatrix2D jacFDSensitivity = SINGLE_CURVE_JACOBIAN_FD.evaluate(X0, SINGLE_CURVE_FINDER_FD);
    final DoubleMatrix2D jacFD = jacobianFD.evaluate(X0, SINGLE_CURVE_FINDER);
    assertMatrixEquals(jacExact, jacFDSensitivity, 1e-6);
    assertMatrixEquals(jacExact, jacFD, 1e-6);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testDoubleCurveJacobian() {
    final JacobianCalculator jacobianFD = new FiniteDifferenceJacobianCalculator(1e-6);
    final DoubleMatrix2D jacExact = DOUBLE_CURVE_JACOBIAN.evaluate(X0, DOUBLE_CURVE_FINDER);
    final DoubleMatrix2D jacFDSensitivity = DOUBLE_CURVE_JACOBIAN_FD.evaluate(X0, DOUBLE_CURVE_FINDER_FD);
    final DoubleMatrix2D jacFD = jacobianFD.evaluate(X0, DOUBLE_CURVE_FINDER);
    assertMatrixEquals(jacExact, jacFDSensitivity, 1e-6);
    assertMatrixEquals(jacExact, jacFD, 1e-6);
  }

  @Test
  public void testForwardCurveOnly() {
    final YieldCurveBundle knownCurves = new YieldCurveBundle();
    knownCurves.setCurve(FUNDING_CURVE_NAME, FUNDING_CURVE);
    final LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>> unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    final LinkedHashMap<String, double[]> unknownCurveNodes = new LinkedHashMap<String, double[]>();
    final LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>> unknownCurveSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(LIBOR_CURVE_NAME, TIME_GRID);
    unknownCurveSensitivityCalculators.put(LIBOR_CURVE_NAME, EXTRAPOLATOR_WITH_SENSITIVITY);
    final MultipleYieldCurveFinderDataBundle data = new MultipleYieldCurveFinderDataBundle(DOUBLE_CURVE_INSTRUMENTS, knownCurves, unknownCurveNodes, unknownCurveInterpolators,
        unknownCurveSensitivityCalculators);
    final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);

    final JacobianCalculator jacCal = new MultipleYieldCurveFinderJacobian(data, PAR_RATE_SENSITIVITY_CALCULATOR);
    final VectorRootFinder rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS, jacCal);
    final DoubleMatrix1D yieldCurveNodes = rootFinder.getRoot(functor, X0);
    final YieldAndDiscountCurve fwdCurve = makeYieldCurve(yieldCurveNodes.getData(), TIME_GRID, EXTRAPOLATOR);

    knownCurves.setCurve(LIBOR_CURVE_NAME, fwdCurve);
    for (int i = 0; i < SWAP_VALUES.length; i++) {
      assertEquals(SWAP_VALUES[i], SWAP_RATE_CALCULATOR.getValue(DOUBLE_CURVE_INSTRUMENTS.get(i), knownCurves), EPS);
    }
  }

  @Test
  public void testFundingCurveOnly() {
    final YieldCurveBundle knownCurves = new YieldCurveBundle();
    knownCurves.setCurve(LIBOR_CURVE_NAME, LIBOR_CURVE);

    final LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>> unknownCurveInterpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
    final LinkedHashMap<String, double[]> unknownCurveNodes = new LinkedHashMap<String, double[]>();
    final LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>> unknownCurveSensitivityCalculators = new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
    unknownCurveInterpolators.put(FUNDING_CURVE_NAME, EXTRAPOLATOR);
    unknownCurveNodes.put(FUNDING_CURVE_NAME, TIME_GRID);
    unknownCurveSensitivityCalculators.put(FUNDING_CURVE_NAME, EXTRAPOLATOR_WITH_SENSITIVITY);
    final MultipleYieldCurveFinderDataBundle data = new MultipleYieldCurveFinderDataBundle(DOUBLE_CURVE_INSTRUMENTS, knownCurves, unknownCurveNodes, unknownCurveInterpolators,
        unknownCurveSensitivityCalculators);
    final Function1D<DoubleMatrix1D, DoubleMatrix1D> functor = new MultipleYieldCurveFinderFunction(data, PAR_RATE_CALCULATOR);

    final JacobianCalculator jacCal = new MultipleYieldCurveFinderJacobian(data, PAR_RATE_SENSITIVITY_CALCULATOR);
    final VectorRootFinder rootFinder = new BroydenVectorRootFinder(EPS, EPS, STEPS, jacCal);
    final DoubleMatrix1D yieldCurveNodes = rootFinder.getRoot(functor, X0);
    final YieldAndDiscountCurve fundCurve = makeYieldCurve(yieldCurveNodes.getData(), TIME_GRID, EXTRAPOLATOR);

    knownCurves.setCurve(FUNDING_CURVE_NAME, fundCurve);
    for (int i = 0; i < SWAP_VALUES.length; i++) {
      assertEquals(SWAP_VALUES[i], SWAP_RATE_CALCULATOR.getValue(DOUBLE_CURVE_INSTRUMENTS.get(i), knownCurves), EPS);
    }

  }

  private static YieldAndDiscountCurve makeYieldCurve(final double[] yields, final double[] times, final Interpolator1D<? extends Interpolator1DDataBundle> interpolator) {
    final int n = yields.length;
    if (n != times.length) {
      throw new IllegalArgumentException("rates and times different lengths");
    }
    return new InterpolatedYieldCurve(times, yields, interpolator);
  }

  /**
   * 
   * @param payments
   * @param fundingCurveName
   * @param liborCurveName
   * @return
   */
  private static FixedFloatSwap setupSwap(final int payments, final double swapRate, final String fundingCurveName, final String liborCurveName) {
    final double[] fixed = new double[payments];
    final double[] floating = new double[2 * payments];
    final double[] deltaStart = new double[2 * payments];
    final double[] deltaEnd = new double[2 * payments];

    final double sigma = 0.0;

    for (int i = 0; i < payments; i++) {
      floating[2 * i + 1] = fixed[i] = 0.5 * (1 + i) + sigma * (RANDOM.nextDouble() - 0.5);
    }
    for (int i = 0; i < 2 * payments; i++) {
      if (i % 2 == 0) {
        floating[i] = 0.25 * (1 + i) + sigma * (RANDOM.nextDouble() - 0.5);
      }
      deltaStart[i] = sigma * (i == 0 ? RANDOM.nextDouble() : (RANDOM.nextDouble() - 0.5));
      deltaEnd[i] = sigma * (RANDOM.nextDouble() - 0.5);
    }
    final ConstantCouponAnnuity fixedLeg = new ConstantCouponAnnuity(fixed, swapRate, fundingCurveName);
    final VariableAnnuity floatingLeg = new VariableAnnuity(floating, 1.0, deltaStart, deltaEnd, fundingCurveName, liborCurveName);
    return new FixedFloatSwap(fixedLeg, floatingLeg);
  }

  private void assertMatrixEquals(final DoubleMatrix2D m1, final DoubleMatrix2D m2, final double eps) {
    final int m = m1.getNumberOfRows();
    final int n = m1.getNumberOfColumns();
    assertEquals(m2.getNumberOfRows(), m);
    assertEquals(m2.getNumberOfColumns(), n);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        assertEquals(m1.getEntry(i, j), m2.getEntry(i, j), eps);
      }
    }
  }
}
