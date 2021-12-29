
package com.pega.gcs.fringecommon.guiutilities.datatable;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.pega.gcs.fringecommon.guiutilities.CheckBoxMenuItemPopupEntry;
import com.pega.gcs.fringecommon.guiutilities.CustomJTableModel;
import com.pega.gcs.fringecommon.guiutilities.DefaultTableColumn;
import com.pega.gcs.fringecommon.guiutilities.FilterColumn;
import com.pega.gcs.fringecommon.guiutilities.FilterTableModel;
import com.pega.gcs.fringecommon.guiutilities.FilterTableModelNavigation;
import com.pega.gcs.fringecommon.guiutilities.MyColor;
import com.pega.gcs.fringecommon.guiutilities.search.SearchModel;
import com.pega.gcs.fringecommon.guiutilities.treetable.AbstractTreeTableNode;
import com.pega.gcs.fringecommon.utilities.GeneralUtilities;

// T data type, V column type
public abstract class AbstractDataTableModel<T, V extends DefaultTableColumn> extends FilterTableModel<Integer> {

    private static final long serialVersionUID = 4472090203203373472L;

    private List<Integer> ftmEntryKeyList;

    private HashMap<Integer, Integer> keyIndexMap;

    protected abstract Map<Integer, T> getDataMap();

    protected abstract List<V> getColumnList();

    protected abstract String getColumnData(T data, V dataTableColumn);

    public AbstractDataTableModel() {

        super(null);

    }

    protected void reset() {

        resetModel();

        updateColumnFilterMap();

        updateKeyIndexMap();
    }

    @Override
    public int getColumnCount() {

        List<V> columnList = getColumnList();

        return columnList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Integer key = getFtmEntryKeyList().get(rowIndex);

        T data = getEventForKey(key);

        return data;
    }

    @Override
    protected int getModelColumnIndex(int column) {
        return column;
    }

    @Override
    protected boolean search(Integer key, Object searchStrObj) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected FilterTableModelNavigation<Integer> getNavigationRowIndex(List<Integer> resultList,
            int currSelectedRowIndex, boolean forward, boolean first, boolean last, boolean wrap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Integer> getFtmEntryKeyList() {

        if (ftmEntryKeyList == null) {
            ftmEntryKeyList = new ArrayList<>();
        }

        return ftmEntryKeyList;
    }

    @Override
    protected HashMap<Integer, Integer> getKeyIndexMap() {

        if (keyIndexMap == null) {
            keyIndexMap = new HashMap<>();
        }

        return keyIndexMap;
    }

    @Override
    public void resetModel() {

        Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<Integer>>> columnFilterMap;
        columnFilterMap = getColumnFilterMap();
        columnFilterMap.clear();

        int columnIndex = 0;

        List<V> columnList = getColumnList();

        for (V dataTableColumn : columnList) {

            // preventing unnecessary buildup of filter map
            if (dataTableColumn.isFilterable()) {
                FilterColumn filterColumn = new FilterColumn(columnIndex);
                filterColumn.setColumnFilterEnabled(true);
                columnFilterMap.put(filterColumn, null);
            }

            columnIndex++;

        }
    }

    @Override
    public T getEventForKey(Integer key) {

        T data = null;

        Map<Integer, T> dataMap = getDataMap();

        if (key != null) {
            data = dataMap.get(key);
        }

        return data;
    }

    @Override
    public AbstractTreeTableNode getTreeNodeForKey(Integer key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearSearchResults(boolean clearResults) {
        // TODO Auto-generated method stub

    }

    @Override
    public SearchModel<Integer> getSearchModel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getColumnValue(Object valueAtObject, int columnIndex) {

        @SuppressWarnings("unchecked")
        T data = (T) valueAtObject;

        V dataTableColumn = getColumn(columnIndex);

        String columnValue = null;

        if ((data != null) && (dataTableColumn != null)) {
            columnValue = getColumnData(data, dataTableColumn);
        }

        return columnValue;
    }

    @Override
    public TableColumnModel getTableColumnModel() {

        TableColumnModel tableColumnModel = new DefaultTableColumnModel();

        List<V> columnList = getColumnList();

        for (int index = 0; index < getColumnCount(); index++) {

            V dataTableColumn = columnList.get(index);

            DefaultTableCellRenderer dtcr = getDefaultTableCellRenderer();

            int horizontalAlignment = dataTableColumn.getHorizontalAlignment();

            dtcr.setHorizontalAlignment(horizontalAlignment);

            int prefColumnWidth = dataTableColumn.getPrefColumnWidth();

            TableColumn tableColumn = new TableColumn(index);
            tableColumn.setHeaderValue(dataTableColumn.getDisplayName());
            tableColumn.setCellRenderer(dtcr);
            tableColumn.setPreferredWidth(prefColumnWidth);
            tableColumn.setWidth(prefColumnWidth);
            tableColumn.setResizable(true);

            tableColumnModel.addColumn(tableColumn);
        }

        return tableColumnModel;
    }

    private V getColumn(int columnIndex) {

        List<V> columnList = getColumnList();

        V dataTableColumn = columnList.get(columnIndex);

        return dataTableColumn;
    }

    protected void updateColumnFilterMap() {

        Map<Integer, T> dataMap = getDataMap();

        List<Integer> ftmEntryKeyList = getFtmEntryKeyList();
        ftmEntryKeyList.clear();

        Map<FilterColumn, List<CheckBoxMenuItemPopupEntry<Integer>>> columnFilterMap = getColumnFilterMap();

        for (Integer key : dataMap.keySet()) {

            ftmEntryKeyList.add(key);

            T data = dataMap.get(key);

            Iterator<FilterColumn> fcIterator = columnFilterMap.keySet().iterator();

            while (fcIterator.hasNext()) {

                FilterColumn filterColumn = fcIterator.next();

                int columnIndex = filterColumn.getIndex();

                DefaultTableColumn dataTableColumn = getColumn(columnIndex);

                boolean filterable = dataTableColumn.isFilterable();

                if (filterable) {

                    List<CheckBoxMenuItemPopupEntry<Integer>> columnFilterEntryList;
                    columnFilterEntryList = columnFilterMap.get(filterColumn);

                    if (columnFilterEntryList == null) {
                        columnFilterEntryList = new ArrayList<CheckBoxMenuItemPopupEntry<Integer>>();
                        columnFilterMap.put(filterColumn, columnFilterEntryList);
                    }

                    String value = getColumnValue(data, columnIndex);

                    String columnValueStr = (value != null) ? value.toString() : null;

                    if (columnValueStr == null) {
                        columnValueStr = FilterTableModel.NULL_STR;
                    } else if ("".equals(columnValueStr)) {
                        columnValueStr = FilterTableModel.EMPTY_STR;
                    }

                    CheckBoxMenuItemPopupEntry<Integer> columnFilterEntry;

                    CheckBoxMenuItemPopupEntry<Integer> searchKey;
                    searchKey = new CheckBoxMenuItemPopupEntry<Integer>(columnValueStr);

                    int index = columnFilterEntryList.indexOf(searchKey);

                    if (index == -1) {
                        columnFilterEntry = new CheckBoxMenuItemPopupEntry<Integer>(columnValueStr);
                        columnFilterEntryList.add(columnFilterEntry);
                    } else {
                        columnFilterEntry = columnFilterEntryList.get(index);
                    }

                    columnFilterEntry.addRowIndex(key);

                    if (columnFilterEntryList.size() > 1) {
                        filterColumn.setColumnFilterEnabled(true);
                    }
                }
            }
        }

        Collections.sort(ftmEntryKeyList);

    }

    protected Color getBackground(T data) {
        return MyColor.LIGHTEST_LIGHT_GRAY;
    }

    private DefaultTableCellRenderer getDefaultTableCellRenderer() {

        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {

            private static final long serialVersionUID = 1504347306097747771L;

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.table.DefaultTableCellRenderer# getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean,
             * boolean, int, int)
             */
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                if (value != null) {

                    @SuppressWarnings("unchecked")
                    T data = (T) value;

                    CustomJTableModel tableModel = (CustomJTableModel) table.getModel();

                    String text = tableModel.getColumnValue(data, column);

                    super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);

                    if (!table.isRowSelected(row)) {

                        Color backgroundColor = AbstractDataTableModel.this.getBackground(data);
                        setBackground(backgroundColor);
                    }

                    setBorder(new EmptyBorder(2, 10, 2, 4));

                    setToolTipText(text);

                } else {
                    setBackground(Color.WHITE);
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }

                return this;
            }

        };

        return dtcr;
    }

    public String getSelectedRowsData(int[] selectedRows) {

        StringBuilder selectedRowsDataSB = new StringBuilder();

        List<V> columnList = getColumnList();

        List<String> columnNameList = DefaultTableColumn.getColumnNameList(columnList);

        String columnNameListCSV = GeneralUtilities.getCollectionAsSeperatedValues(columnNameList, null, true);

        selectedRowsDataSB.append(columnNameListCSV);
        selectedRowsDataSB.append(System.getProperty("line.separator"));

        for (int selectedRow : selectedRows) {

            Integer entryKey = getFtmEntryKeyList().get(selectedRow);

            T data = getEventForKey(entryKey);

            List<String> recordDataList = new ArrayList<>();

            for (V column : columnList) {
                String columnValue = getColumnData(data, column);
                recordDataList.add(columnValue);
            }

            String recordDataListCSV = GeneralUtilities.getCollectionAsSeperatedValues(recordDataList, null, true);

            selectedRowsDataSB.append(recordDataListCSV);
            selectedRowsDataSB.append(System.getProperty("line.separator"));
        }

        return selectedRowsDataSB.toString();
    }
}
