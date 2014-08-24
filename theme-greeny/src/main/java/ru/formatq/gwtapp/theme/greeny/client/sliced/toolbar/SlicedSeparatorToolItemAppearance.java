/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package ru.formatq.gwtapp.theme.greeny.client.sliced.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import ru.formatq.gwtapp.theme.greeny.client.base.toolbar.Css3SeparatorToolItemAppearance;

/**
 *
 */
public class SlicedSeparatorToolItemAppearance extends Css3SeparatorToolItemAppearance {

  public interface SlicedSeparatorToolItemResources extends Css3SeparatorToolItemResources {

    @Source({"ru/formatq/gwtapp/theme/greeny/client/base/toolbar/Css3SeparatorToolItem.css", "SlicedSeparatorToolItem.css"})
    @Override
    Css3SeparatorToolItemStyle style();

    ImageResource separator();
  }

  public SlicedSeparatorToolItemAppearance() {
    this(GWT.<SlicedSeparatorToolItemResources>create(SlicedSeparatorToolItemResources.class));
  }

  public SlicedSeparatorToolItemAppearance(SlicedSeparatorToolItemResources resources) {
    super(resources);
  }
}
