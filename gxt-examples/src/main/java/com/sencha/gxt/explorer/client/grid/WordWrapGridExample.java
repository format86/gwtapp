/**
 * Sencha GXT 3.1.1 - Sencha for GWT
 * Copyright(c) 2007-2014, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.explorer.client.grid;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.TextAreaAppearance;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.examples.resources.client.TestData;
import com.sencha.gxt.examples.resources.client.images.ExampleImages;
import com.sencha.gxt.explorer.client.model.Example.Detail;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent.ViewReadyHandler;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * An example that illustrates a way to implement word wrap in {@link com.sencha.gxt.widget.core.client.grid.Grid} column headers and cells.
 * <p />
 * This class demonstrates many capabilities of word wrap in grids, including setting heading text, setting cell text,
 * setting column width, setting header height, getting header height and toggling force fit. Most applications will not
 * have need for all of these functions. The basic word wrap column header support can be found in
 * {@link WordWrapColumnHeader}.
 * <p />
 * This class generally uses the term <i>heading</i> to refer to the text of a column heading and the term <i>header</i>
 * to refer to the visual elements that display the heading. In some cases, this terminology is relaxed to make the
 * example more usable.
 */
@Detail(name = "Word Wrap Grid", icon = "basicgrid", category = "Grid", classes = {WordWrapColumnHeader.class})
public class WordWrapGridExample implements IsWidget, EntryPoint {

  /**
   * A value provider that supports a spreadsheet-like table with an arbitrary number of columns.
   */
  public static class ColumnValueProvider implements ValueProvider<Row, String> {

    private static int nextId;
    private final int columnIndex;
    private final String path = "Column" + Integer.toString(nextId++);

    public ColumnValueProvider(int columnIndex) {
      this.columnIndex = columnIndex;
    }

    @Override
    public String getPath() {
      return path;
    }

    @Override
    public String getValue(Row row) {
      return row.getColumn(columnIndex);
    }

    @Override
    public void setValue(Row row, String value) {
      row.setValue(columnIndex, value);
    }

  }

  /**
   * An array of column widths that can be used with {@link com.sencha.gxt.widget.core.client.grid.GridView#setForceFit(boolean)} to save column widths before
   * invoking <code>setForceFit(true)</code> and restore them after invoking <code>setForceFit(false)</code>.
   */
  public class ColumnWidths {

    private int[] columnWidths;

    public void restore(Grid<Row> grid) {
      ColumnModel<Row> columnModel = grid.getColumnModel();
      for (int columnIndex = 0; columnIndex < columnWidths.length; columnIndex++) {
        columnModel.setColumnWidth(columnIndex, columnWidths[columnIndex]);
      }
    }

    public void save(Grid<Row> grid) {
      ColumnModel<Row> columnModel = grid.getColumnModel();
      int columnCount = columnModel.getColumnCount();
      columnWidths = new int[columnCount];
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        ColumnConfig<Row, Object> columnConfig = columnModel.getColumn(columnIndex);
        columnWidths[columnIndex] = columnConfig.getWidth();
      }
    }
  }

  /**
   * A data model that supports a spreadsheet-like table with an arbitrary number of rows and columns.
   */
  public static class Row {

    private static int nextId;
    private final String[] columns;
    private final String key = "Row" + Integer.toString(nextId++);

    public Row(int columnCount) {
      columns = new String[columnCount];
    }

    public String getColumn(int columnIndex) {
      return columns[columnIndex];
    }

    public String getKey() {
      return key;
    }

    public void setValue(int columnIndex, String value) {
      columns[columnIndex] = value;
    }

  }

  /**
   * A model key provider that supports a spreadsheet-like table with an arbitrary number of rows.
   */
  public class RowKeyProvider implements ModelKeyProvider<Row> {
    @Override
    public String getKey(Row row) {
      return row.getKey();
    }
  }

  /**
   * A value provider that supports a spreadsheet-like table with an arbitrary number of rows.
   */
  public class RowValueProvider {
    public ValueProvider<Row, String> getColumnValueProvider(int columnIndex) {
      return new ColumnValueProvider(columnIndex);
    }
  }

  /**
   * A grid view that limits the width of the items in the column header menu's column sub-menu. Strictly speaking, this
   * is not required for a word wrap grid header because the sub-menu resizes itself to the width of the longest column
   * heading, but this does improve the usability by limiting the sub-menu width.
   */
  public static class WordWrapGridView extends GridView<Row> {
    @Override
    protected Menu createContextMenu(int colIndex) {
      Menu contextMenu = super.createContextMenu(colIndex);
      int lastItem = contextMenu.getWidgetCount() - 1;
      if (lastItem >= 0) {
        Widget widget = contextMenu.getWidget(lastItem);
        if (widget instanceof MenuItem) {
          MenuItem columns = (MenuItem) widget;
          Menu columnMenu = columns.getSubMenu();
          if (columnMenu != null) {
            int cols = columnMenu.getWidgetCount();
            for (int i = 0; i < cols; i++) {
              widget = columnMenu.getWidget(i);
              if (widget instanceof CheckMenuItem) {
                CheckMenuItem menuItem = (CheckMenuItem) widget;
                menuItem.setWidth(200);
              }
            }
          }
        }
      }
      return contextMenu;
    }
  }

  private static final int ROW_COUNT = 15;
  private static final int COLUMN_COUNT = 5;
  private static final Margins M1 = new Margins(5, 5, 0, 5);
  private static final Margins M2 = new Margins(5, 5, 5, 5);
  private static final String[] SENTENCES = TestData.DUMMY_TEXT_LONG.replaceAll("\\<[^>]*>", "").split("\\. ");

  private ContentPanel fp;
  private RowValueProvider rvp = new RowValueProvider();
  private ColumnWidths columnWidths = new ColumnWidths();

  @Override
  public Widget asWidget() {

    if (fp == null) {
      fp = new FramedPanel();
      fp.setHeadingText("Word Wrap Grid");
      fp.getHeader().setIcon(ExampleImages.INSTANCE.table());
      fp.setPosition(10, 10);
      fp.setPixelSize(600, 400);
      new Resizable(fp, Dir.E, Dir.SE, Dir.S);

      ToolButton tb = new ToolButton(ToolButton.QUESTION);
      ToolTipConfig ttc = new ToolTipConfig("Example Info", "This example illustrates word wrap in grid headings and rows.");
      ttc.setMaxWidth(225);
      tb.setToolTipConfig(ttc);
      fp.addTool(tb);

      List<ColumnConfig<Row, ?>> ccs = new LinkedList<ColumnConfig<Row, ?>>();

      for (int i = 0; i < COLUMN_COUNT; i++) {
        ValueProvider<Row, String> cvp = rvp.getColumnValueProvider(i);
        SafeHtml sh = wrapString(createDummyText());
        ColumnConfig<Row, String> cc = new ColumnConfig<Row, String>(cvp, 200, sh);
        // Use a custom cell renderer to support word wrap in the grid's cells
        cc.setCell(new AbstractCell<String>() {
          @Override
          public void render(Context context, String value, SafeHtmlBuilder sb) {
            if (value == null || value.isEmpty()) {
              sb.appendHtmlConstant("&nbsp;");
            } else {
              sb.append(wrapString(value));
            }
          }
        });
        ccs.add(cc);
      }

      final ColumnModel<Row> cm = new ColumnModel<Row>(ccs);

      final ListStore<Row> ls = new ListStore<Row>(new RowKeyProvider());
      ls.setAutoCommit(true);

      int columnCount = ccs.size();
      for (int i = 0; i < ROW_COUNT; i++) {
        Row row = new Row(columnCount);
        for (int j = 0; j < columnCount; j++) {
          row.setValue(j, createDummyText());
        }
        ls.add(row);
      }

      final Grid<Row> g = new Grid<Row>(ls, cm, new WordWrapGridView());
      g.getView().setColumnHeader(new WordWrapColumnHeader<Row>(g, cm));
      g.getView().setColumnLines(true);

      final GridInlineEditing<Row> gie = new GridInlineEditing<Row>(g) {
        protected void onScroll(ScrollEvent event) {
          // Suppress default action, which may result in canceling edit
        }
      };
      gie.setClicksToEdit(ClicksToEdit.TWO);
      gie.addStartEditHandler(new StartEditHandler<Row>() {
        @Override
        public void onStartEdit(StartEditEvent<Row> event) {
          GridCell cell = event.getEditCell();
          ColumnConfig<Row, ?> cc = cm.getColumn(cell.getCol());
          Field<Object> editor = (Field<Object>) gie.getEditor(cc);
          Element rowElement = g.getView().getRow(cell.getRow());
          // Resize the inline editor to the height of the row and style it to match the text
          int height = rowElement.getOffsetHeight() - 1;
          editor.setHeight(height);
          XElement cellElement = g.getView().getCell(cell.getRow(), cell.getCol()).cast();
          Style style = ((TextAreaAppearance) editor.getCell().getAppearance()).getInputElement(editor.getElement()).getStyle();
          String fontSize = cellElement.getComputedStyle("fontSize");
          if (fontSize != null) {
            style.setProperty("fontSize", fontSize);
          }
          String fontFamily = cellElement.getComputedStyle("fontFamily");
          if (fontFamily != null) {
            style.setProperty("fontFamily", fontFamily);
          }
          style.setOverflow(Overflow.HIDDEN);
        }
      });

      for (ColumnConfig<Row, ?> cc : ccs) {
        @SuppressWarnings("unchecked")
        ColumnConfig<Row, String> scc = (ColumnConfig<Row, String>) cc;
        final TextArea ta = new TextArea();
        ta.setPreventScrollbars(true);
        ta.addKeyDownHandler(new KeyDownHandler() {
          @Override
          public void onKeyDown(KeyDownEvent event) {
            // Allow the enter key to end grid inline editing
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
              Scheduler.get().scheduleFinally(new RepeatingCommand() {
                @Override
                public boolean execute() {
                  gie.completeEditing();
                  ta.clear();
                  return false;
                }
              });
            }
          }
        });
        gie.addEditor(scc, ta);
      }

      g.addViewReadyHandler(new ViewReadyHandler() {
        @Override
        public void onViewReady(ViewReadyEvent event) {
          Info.display("onViewReady", "heading width=" + g.getView().getHeader().getOffsetWidth() + ", height=" + g.getView().getHeader().getOffsetHeight());
          g.getView().getHeader().addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
              Info.display("onResize", "heading width=" + event.getWidth() + ", height=" + event.getHeight());
            }
          });
        }
      });

      fp.setWidget(g);

      fp.addButton(new TextButton("Set Heading Text", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          final Window w = new Window();
          w.setHeadingText("Set Heading Text");
          w.setPixelSize(300, 200);
          w.setResizable(false);
          w.setModal(true);
          VerticalLayoutContainer vlc = new VerticalLayoutContainer();
          w.setWidget(vlc);
          final IntegerSpinnerField index = new IntegerSpinnerField();
          index.setMinValue(0);
          index.setMaxValue(COLUMN_COUNT - 1);
          index.setValue(0);
          index.setAllowBlank(false);
          index.setSelectOnFocus(true);
          vlc.add(new FieldLabel(index, "Column Index"), new VerticalLayoutData(1, -1, M1));
          final TextArea text = new TextArea();
          vlc.add(new FieldLabel(text, "Heading Text"), new VerticalLayoutData(1, 1, M2));
          w.addButton(new TextButton("Cancel", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              w.hide();
            }
          }));
          w.addButton(new TextButton("OK", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              if (index.validate()) {
                w.hide();
                SafeHtml newHeading;
                String newText = text.getValue();
                if (newText == null) {
                  newHeading = SafeHtmlUtils.fromSafeConstant("&nbsp;");
                } else {
                  newHeading = wrapString(newText);
                }
                ColumnModel<Row> columnModel = g.getColumnModel();
                columnModel.setColumnHeader(index.getValue(), newHeading);
              }
            }
          }));
          w.show();
          w.setFocusWidget(index);
        }
      }));

      fp.addButton(new TextButton("Set Cell Text", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          final Window w = new Window();
          w.setHeadingText("Set Cell Text");
          w.setPixelSize(300, 200);
          w.setResizable(false);
          w.setModal(true);
          VerticalLayoutContainer vlc = new VerticalLayoutContainer();
          w.setWidget(vlc);
          final IntegerSpinnerField rowIndex = new IntegerSpinnerField();
          rowIndex.setMinValue(0);
          rowIndex.setMaxValue(ROW_COUNT - 1);
          rowIndex.setValue(0);
          rowIndex.setAllowBlank(false);
          rowIndex.setSelectOnFocus(true);
          vlc.add(new FieldLabel(rowIndex, "Row Index"), new VerticalLayoutData(1, -1, M1));
          final IntegerSpinnerField columnIndex = new IntegerSpinnerField();
          columnIndex.setMinValue(0);
          columnIndex.setMaxValue(COLUMN_COUNT - 1);
          columnIndex.setValue(0);
          columnIndex.setAllowBlank(false);
          columnIndex.setSelectOnFocus(true);
          vlc.add(new FieldLabel(columnIndex, "Column Index"), new VerticalLayoutData(1, -1, M1));
          final TextArea text = new TextArea();
          vlc.add(new FieldLabel(text, "Cell Text"), new VerticalLayoutData(1, 1, M2));
          w.addButton(new TextButton("Cancel", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              w.hide();
            }
          }));
          w.addButton(new TextButton("OK", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              if (rowIndex.validate() && columnIndex.validate()) {
                w.hide();
                String newText = text.getValue();
                if (newText == null) {
                  newText = "";
                }
                Row row = ls.get(rowIndex.getValue());
                row.setValue(columnIndex.getValue(), newText);
                ls.update(row);
              }
            }
          }));
          w.show();
          w.setFocusWidget(rowIndex);
        }
      }));

      fp.addButton(new TextButton("Set Column Width", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          final Window w = new Window();
          w.setHeadingText("Set Column Width");
          w.setPixelSize(300, 150);
          w.setResizable(false);
          w.setModal(true);
          VerticalLayoutContainer vlc = new VerticalLayoutContainer();
          w.setWidget(vlc);
          final IntegerSpinnerField columnIndex = new IntegerSpinnerField();
          columnIndex.setMinValue(0);
          columnIndex.setMaxValue(COLUMN_COUNT - 1);
          columnIndex.setValue(0);
          columnIndex.setAllowBlank(false);
          columnIndex.setSelectOnFocus(true);
          vlc.add(new FieldLabel(columnIndex, "Column Index"), new VerticalLayoutData(1, -1, M1));
          final IntegerSpinnerField width = new IntegerSpinnerField();
          width.setMinValue(0);
          width.setValue(50);
          width.setAllowBlank(false);
          width.setSelectOnFocus(true);
          vlc.add(new FieldLabel(width, "Column Width"), new VerticalLayoutData(1, -1, M2));
          w.addButton(new TextButton("Cancel", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              w.hide();
            }
          }));
          w.addButton(new TextButton("OK", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              if (columnIndex.validate() && width.validate()) {
                w.hide();
                g.getColumnModel().setColumnWidth(columnIndex.getValue(), width.getValue());
              }
            }
          }));
          w.show();
          w.setFocusWidget(columnIndex);
        }
      }));

      fp.addButton(new TextButton("Set Heading Height", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          final Window w = new Window();
          w.setHeadingText("Set Heading Height");
          w.setPixelSize(300, 250);
          w.setResizable(false);
          w.setModal(true);
          VerticalLayoutContainer vlc = new VerticalLayoutContainer();
          w.setWidget(vlc);
          vlc.add(new HTML("<span style='font: 12px tahoma,arial,verdana,sans-serif;'>Sets the size of the heading to a fixed height. If this height is less than the height of the heading text, the heading text will be truncated.<br><br>To restore automatic sizing, specify a value of -1.<br><br></span>"), new VerticalLayoutData(1, -1, M1));
          final IntegerSpinnerField height = new IntegerSpinnerField();
          height.setMinValue(-1);
          height.setValue(50);
          height.setAllowBlank(false);
          height.setSelectOnFocus(true);
          vlc.add(new FieldLabel(height, "Heading Height"), new VerticalLayoutData(1, -1, M2));
          w.addButton(new TextButton("Cancel", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              w.hide();
            }
          }));
          w.addButton(new TextButton("OK", new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              if (height.validate()) {
                w.hide();
                g.getView().getHeader().setHeight(height.getValue());
              }
            }
          }));
          w.show();
          w.setFocusWidget(height);
        }
      }));

      fp.addButton(new TextButton("Get Heading Height", new SelectHandler() {
        @Override
        public void onSelect(SelectEvent event) {
          int headerHeight = g.getView().getHeader().getOffsetHeight();
          Info.display("getHeight", "height=" + headerHeight);
        }
      }));

      final ToggleButton fftb = new ToggleButton("Force Fit");
      fftb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
          if (fftb.getValue()) {
            columnWidths.save(g);
            g.getView().setForceFit(true);
          } else {
            g.getView().setForceFit(false);
            columnWidths.restore(g);
          }
          g.getView().layout();
        }
      });
      fp.addButton(fftb);

      fp.show();
    }

    return fp;
  }

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(asWidget());
  }

  private String createDummyText() {
    int sentenceCount = 1 + Random.nextInt(5);
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < sentenceCount; i++) {
      s.append(SENTENCES[Random.nextInt(SENTENCES.length)]);
      s.append(". ");
    }
    return s.toString();
  }

  private SafeHtml wrapString(String untrustedString) {
    SafeHtml escapedString = SafeHtmlUtils.fromString(untrustedString);
    SafeHtml safeHtml = SafeHtmlUtils.fromTrustedString("<span style='white-space: normal;'>" + escapedString.asString() + "</span>");
    return safeHtml;
  }

}
