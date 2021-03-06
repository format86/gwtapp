/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.chart;

import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.explorer.client.chart.ThemedChartExample.ThemedChartAppearance;

public class GreenyThemedChartAppearance implements ThemedChartAppearance {
  @Override
  public Gradient barColor() {
    Gradient g = new Gradient(0);
    g.addStop(0, new Color("#ADEDD2"));
    return g;
  }

  @Override
  public Color lineColor() {
    return new Color("#157F33");
  }

  @Override
  public Color markerColor() {
    return lineColor();
  }
}
