package com.github.henrikac.weblogparser.form;

import com.github.henrikac.logparser.core.LogLevel;
import com.github.henrikac.weblogparser.validation.ValidLogFile;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class LogUploadForm {

    @ValidLogFile(message = "Please choose a non-empty log file to upload")
    private MultipartFile file;

    @NotEmpty(message = "Please select at least one log level")
    private List<LogLevel> logLevels;

    public LogUploadForm() {
        this.logLevels = new ArrayList<>(List.of(LogLevel.values()));
    }

    public MultipartFile getFile() {
        return this.file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<LogLevel> getLogLevels() {
        return this.logLevels;
    }

    public void setLogLevels(List<LogLevel> logLevels) {
        this.logLevels = logLevels;
    }
}
