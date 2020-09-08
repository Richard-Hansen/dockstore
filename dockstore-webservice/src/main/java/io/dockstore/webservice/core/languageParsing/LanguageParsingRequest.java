package io.dockstore.webservice.core.languageParsing;

import java.util.List;
import java.util.Objects;

import io.dockstore.common.DescriptorLanguage;
import io.dockstore.webservice.core.SourceFile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request sent to the external language parsing service")
public class LanguageParsingRequest {
    @Schema(description = "The Git URI", required = true)
    private String uri;

    @Schema(description = "The Git branch/tag", required = true)
    private String branch;

    @Schema(description = "The relative path to the primary descriptor (relative to the base in Git)", required = true)
    private String descriptorRelativePathInGit;

    @Schema(description = "Id of the Dockstore entry", required = true)
    private long entryId;

    @Schema(description = "Id of the Dockstore entry's workflowVersion", required = true)
    private long versionId;

    @Schema(description = "List of SourceFiles that will be required for parsing hosted entries from Dockstore")
    private List<SourceFile> sourceFiles;

    @Schema(description = "The language of the workflow")
    private DescriptorLanguage descriptorLanguage;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescriptorRelativePathInGit() {
        return descriptorRelativePathInGit;
    }

    public void setDescriptorRelativePathInGit(String descriptorRelativePathInGit) {
        this.descriptorRelativePathInGit = descriptorRelativePathInGit;
    }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getVersionId() {
        return versionId;
    }

    public void setVersionId(long versionId) {
        this.versionId = versionId;
    }

    public List<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    public void setSourceFiles(List<SourceFile> sourceFiles) {
        this.sourceFiles = sourceFiles;
    }

    public DescriptorLanguage getDescriptorLanguage() {
        return descriptorLanguage;
    }

    public void setDescriptorLanguage(DescriptorLanguage descriptorLanguage) {
        this.descriptorLanguage = descriptorLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LanguageParsingRequest that = (LanguageParsingRequest)o;
        return entryId == that.entryId && versionId == that.versionId && Objects.equals(uri, that.uri) && Objects
            .equals(branch, that.branch) && Objects.equals(descriptorRelativePathInGit, that.descriptorRelativePathInGit) && Objects
            .equals(sourceFiles, that.sourceFiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, branch, descriptorRelativePathInGit, entryId, versionId, sourceFiles);
    }
}
