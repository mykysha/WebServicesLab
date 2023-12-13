package org.example.component;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public String name;
    public List<Row> rows = new ArrayList<>();
    public List<Column> columns = new ArrayList<>();

    @JsonCreator
    public Table(@JsonProperty("name") String name,
                 @JsonProperty("rows") List<Row> rows,
                 @JsonProperty("columns") List<Column> columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public void deleteRow(int rowIndex) {
        rows.remove(rowIndex);
    }

    public void deleteColumn(int columnIndex) {
        columns.remove(columnIndex);
        for (Row row: rows) {
            row.values.remove(columnIndex);
        }
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void setName(String name) {
        this.name = name;
    }
}
