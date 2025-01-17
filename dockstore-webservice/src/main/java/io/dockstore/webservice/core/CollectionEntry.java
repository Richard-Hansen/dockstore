package io.dockstore.webservice.core;

import java.io.Serializable;
import java.util.Date;

import io.dockstore.common.SourceControl;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Used to retrieve specific entry fields from workflows/tools.  Also used in response for all endpoints that return a single collection.
 */
public class CollectionEntry implements Serializable {
    private String entryPath;
    @ApiModelProperty(dataType = "long")
    @Schema(type = "integer", format = "int64")
    private Date dbUpdateDate;
    private long id;
    private String entryType;
    private String versionName;

    public CollectionEntry(long id, Date dbUpdateDate, String entryTypeString, SourceControl sourceControl, String organization, String repository, String entryName)  {
        this(id, dbUpdateDate, entryTypeString, sourceControl, organization, repository, entryName, null);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public CollectionEntry(long id, Date dbUpdateDate, String entryTypeString, SourceControl sourceControl, String organization, String repository, String entryName, String versionName)  {
        setEntryType(entryTypeString);
        setDbUpdateDate(dbUpdateDate);
        setId(id);
        setEntryPath(sourceControl.toString(), organization, repository, entryName);
        setVersionName(versionName);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public CollectionEntry(long id, Date dbUpdateDate, String entryTypeString, String registry, String organization, String repository, String entryName)  {
        this(id, dbUpdateDate, entryTypeString, registry, organization, repository, entryName, null);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    public CollectionEntry(long id, Date dbUpdateDate, String entryTypeString, String registry, String organization, String repository, String entryName, String versionName)  {
        setEntryType(entryTypeString);
        setDbUpdateDate(dbUpdateDate);
        setId(id);
        setEntryPath(registry, organization, repository, entryName);
        setVersionName(versionName);
    }

    private void setEntryPath(String sourceControl, String organization, String repository, String entryName) {
        setEntryPath(sourceControl + '/' + organization + '/' + repository + (entryName == null || "".equals(entryName) ? "" : '/' + entryName));
    }

    public void setEntryPath(String entryPath) {
        this.entryPath = entryPath;
    }

    public String getEntryPath() {
        return entryPath;
    }

    public Date getDbUpdateDate() {
        return dbUpdateDate;
    }

    public void setDbUpdateDate(Date dbUpdateDate) {
        this.dbUpdateDate = dbUpdateDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
