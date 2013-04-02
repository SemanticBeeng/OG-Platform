/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.compilation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;

import com.google.common.collect.Maps;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.depgraph.DependencyGraph;
import com.opengamma.engine.depgraph.DependencyNode;
import com.opengamma.engine.function.MarketDataAliasingFunction;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;

/**
 * Default implementation of {@link CompiledViewCalculationConfiguration}.
 */
public class CompiledViewCalculationConfigurationImpl implements CompiledViewCalculationConfiguration {

  private final String _name;
  private final Set<ComputationTargetSpecification> _computationTargets;
  private final Map<ValueSpecification, Set<ValueRequirement>> _terminalOutputSpecifications;
  private final Map<ValueSpecification, Collection<ValueSpecification>> _marketDataAliases;

  /**
   * Constructs an instance
   * 
   * @param name the name of the view calculation configuration, not null
   * @param computationTargets the computation targets, not null
   * @param terminalOutputSpecifications the output specifications, not null
   * @param marketDataRequirements the market data requirements, not null
   */
  public CompiledViewCalculationConfigurationImpl(final String name, final Set<ComputationTargetSpecification> computationTargets,
      final Map<ValueSpecification, Set<ValueRequirement>> terminalOutputSpecifications,
      final Map<ValueSpecification, Collection<ValueSpecification>> marketDataRequirements) {
    ArgumentChecker.notNull(name, "name");
    ArgumentChecker.notNull(computationTargets, "computationTargets");
    ArgumentChecker.notNull(terminalOutputSpecifications, "terminalOutputSpecifications");
    ArgumentChecker.notNull(marketDataRequirements, "marketDataRequirements");
    _name = name;
    _computationTargets = computationTargets;
    _terminalOutputSpecifications = terminalOutputSpecifications;
    _marketDataAliases = marketDataRequirements;
  }

  /**
   * Constructs an instance from a dependency graph
   * 
   * @param dependencyGraph the dependency graph, not null
   */
  public CompiledViewCalculationConfigurationImpl(final DependencyGraph dependencyGraph) {
    this(dependencyGraph.getCalculationConfigurationName(), processComputationTargets(dependencyGraph),
        processTerminalOutputSpecifications(dependencyGraph), processMarketDataRequirements(dependencyGraph));
  }

  private static Map<ValueSpecification, Collection<ValueSpecification>> processMarketDataRequirements(final DependencyGraph dependencyGraph) {
    ArgumentChecker.notNull(dependencyGraph, "dependencyGraph");
    final Collection<ValueSpecification> marketDataEntries = dependencyGraph.getAllRequiredMarketData();
    final Map<ValueSpecification, Collection<ValueSpecification>> result = Maps.newHashMapWithExpectedSize(marketDataEntries.size());
    for (ValueSpecification marketData : marketDataEntries) {
      final DependencyNode marketDataNode = dependencyGraph.getNodeProducing(marketData);
      Collection<ValueSpecification> aliases = null;
      boolean usedDirectly = false;
      Collection<DependencyNode> aliasNodes = marketDataNode.getDependentNodes();
      for (DependencyNode aliasNode : aliasNodes) {
        if (aliasNode.getFunction().getFunction() instanceof MarketDataAliasingFunction) {
          if (aliases == null) {
            aliases = new ArrayList<ValueSpecification>(aliasNodes.size());
            result.put(marketData, Collections.unmodifiableCollection(aliases));
          }
          aliases.addAll(aliasNode.getOutputValues());
        } else {
          usedDirectly = true;
        }
      }
      if (usedDirectly) {
        if (aliases != null) {
          aliases.add(marketData);
        } else {
          result.put(marketData, Collections.singleton(marketData));
        }
      }
    }
    return Collections.unmodifiableMap(result);
  }

  private static Map<ValueSpecification, Set<ValueRequirement>> processTerminalOutputSpecifications(final DependencyGraph dependencyGraph) {
    ArgumentChecker.notNull(dependencyGraph, "dependencyGraph");
    return dependencyGraph.getTerminalOutputs();
  }

  private static Set<ComputationTargetSpecification> processComputationTargets(final DependencyGraph dependencyGraph) {
    ArgumentChecker.notNull(dependencyGraph, "dependencyGraph");
    return dependencyGraph.getAllComputationTargets();
  }

  // CompiledViewCalculationConfiguration

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public Map<ValueSpecification, Set<ValueRequirement>> getTerminalOutputSpecifications() {
    return Collections.unmodifiableMap(_terminalOutputSpecifications);
  }

  @Override
  public Set<Pair<String, ValueProperties>> getTerminalOutputValues() {
    final Set<Pair<String, ValueProperties>> valueNames = new HashSet<Pair<String, ValueProperties>>();
    for (final ValueSpecification spec : getTerminalOutputSpecifications().keySet()) {
      valueNames.add(Pair.of(spec.getValueName(), spec.getProperties()));
    }
    return valueNames;

  }

  @Override
  public Set<ComputationTargetSpecification> getComputationTargets() {
    return _computationTargets;
  }

  @Override
  public Set<ValueSpecification> getMarketDataRequirements() {
    return getMarketDataAliases().keySet();
  }

  @Override
  public Map<ValueSpecification, Collection<ValueSpecification>> getMarketDataAliases() {
    return _marketDataAliases;
  }

  // Object

  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof CompiledViewCalculationConfiguration)) {
      return false;
    }
    final CompiledViewCalculationConfiguration other = (CompiledViewCalculationConfiguration) o;
    return ObjectUtils.equals(getName(), other.getName())
        && ObjectUtils.equals(getTerminalOutputSpecifications(), other.getTerminalOutputSpecifications())
        && ObjectUtils.equals(getComputationTargets(), other.getComputationTargets())
        && ObjectUtils.equals(getMarketDataRequirements(), other.getMarketDataRequirements());
  }

  @Override
  public int hashCode() {
    int hc = ObjectUtils.hashCode(getName());
    hc += (hc << 4) + ObjectUtils.hashCode(getTerminalOutputSpecifications());
    hc += (hc << 4) + ObjectUtils.hashCode(getComputationTargets());
    hc += (hc << 4) + ObjectUtils.hashCode(getMarketDataRequirements());
    return hc;
  }

}
