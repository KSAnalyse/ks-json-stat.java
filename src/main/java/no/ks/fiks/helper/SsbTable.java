package no.ks.fiks.helper;

import java.util.List;
import java.util.Map;

public class SsbTable {
    private String tableName;
    private List<String> columnName;
    private Map<String, Integer> columLength;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }

    public Map<String, Integer> getColumLength() {
        return columLength;
    }

    public void setColumLength(Map<String, Integer> columLength) {
        this.columLength = columLength;
    }
}
