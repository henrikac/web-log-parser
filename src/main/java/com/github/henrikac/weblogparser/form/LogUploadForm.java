package com.github.henrikac.weblogparser.form;

import com.github.henrikac.weblogparser.validation.ValidLogFile;
import org.springframework.web.multipart.MultipartFile;

public class LogUploadForm {

    @ValidLogFile(message = "Please choose a non-empty log file to upload")
    private MultipartFile file;

    public MultipartFile getFile() {
        return this.file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
