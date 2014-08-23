/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package ru.formatq.gwtapp.theme.client;

import com.sencha.gxt.themebuilder.base.client.config.TypeDetails;

/**
 *
 */
public interface TabDetails {
  @TypeDetails(sampleValue = "3", comment = "border radius of tabs")
  int borderRadius();

  @TypeDetails(sampleValue = "#ffffff", comment = "background color of tab body")
  String bodyBackgroundColor();

  @TypeDetails(sampleValue = "#037ecf", comment = "border color or tab panel")
  String borderColor();

  @TypeDetails(sampleValue = "util.solidGradientString('#add2ed')", comment = "tab gradient string (top to bottom)")
  String gradient();

  @TypeDetails(sampleValue = "util.solidGradientString('#4b9cd7')", comment = "inactive tab gradient string (top to bottom)")
  String inactiveGradient();

  @TypeDetails(sampleValue = "util.solidGradientString('#5fa7db')", comment = "hover tab gradient string (top to bottom)")
  String hoverGradient();

  @TypeDetails(sampleValue = "#add2ed", comment = "last color in gradient")
  String lastStopColor();

  @TypeDetails(sampleValue = "#4b9cd7", comment = "last color in inactive gradient")
  String inactiveLastStopColor();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "tab heading text style")
  FontDetails headingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "active tab heading text style")
  FontDetails activeHeadingText();

  @TypeDetails(sampleValue = "util.fontStyle('sans-serif','medium','#000000','normal')", comment = "active tab heading text style")
  FontDetails hoverHeadingText();

  @TypeDetails(sampleValue = "util.solidGradientString('#037ecf')", comment = "tab strip background gradient string (top to bottom)")
  String tabStripGradient();

  @TypeDetails(sampleValue = "'none'", comment = "tab item left border")
  String tabItemBorderLeft();

  @TypeDetails(sampleValue = "'none'", comment = "tab item top border")
  String tabItemBorderTop();

  @TypeDetails(sampleValue = "'none'", comment = "tab item right border")
  String tabItemBorderRight();

  @TypeDetails(sampleValue = "'none'", comment = "tab bar border")
  String tabBarBorder();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails padding();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails paddingWithIcon();

  @TypeDetails(sampleValue = "6", comment = "left offset for tab icon")
  int iconLeftOffset();

  @TypeDetails(sampleValue = "5", comment = "top offset for tab icon")
  int iconTopOffset();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails paddingWithClosable();

  @TypeDetails(sampleValue = "#037ecf", comment = "background color of the tab scrollers")
  String scrollerBackgroundColor();

  @TypeDetails(sampleValue = "18", comment = "width of scroller")
  int scrollerWidth();

  @TypeDetails(sampleValue = "31", comment = "height of tabs")
  int tabHeight();

  @TypeDetails(sampleValue = "1", comment = "spacing between tabs")
  int tabSpacing();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails tabStripPadding();

  @TypeDetails(sampleValue = "4", comment = "Spacing between tab bar and body")
  int tabBarBottomHeight();

  @TypeDetails(sampleValue = "'none'", comment = "tab strip bottom border")
  String tabStripBottomBorder();

  @TypeDetails(sampleValue = "'none'", comment = "border of tab panel body")
  String tabBodyBorder();

  @TypeDetails(sampleValue = "util.padding(0)", comment = "padding")
  EdgeDetails tabTextPadding();
}
