package org.ei.drishti.commonregistry;

/**
 * Created by raihan on 3/16/15.
 */
public class RepositoryInformationHolder {
    String bindtypename;
    String [] columnNames;

    public String getBindtypename() {
        return bindtypename;
    }

    public RepositoryInformationHolder(String bindtypename, String[] columnNames) {
        this.bindtypename = bindtypename;
        this.columnNames = columnNames;
    }

    public void setBindtypename(String bindtypename) {
        this.bindtypename = bindtypename;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
