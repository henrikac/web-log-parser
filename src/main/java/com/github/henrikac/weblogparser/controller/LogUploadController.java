package com.github.henrikac.weblogparser.controller;

import com.github.henrikac.logparser.core.LogLevel;
import com.github.henrikac.weblogparser.form.LogUploadForm;
import com.github.henrikac.weblogparser.view.ResultView;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LogUploadController {

    private static final Logger logger = LoggerFactory.getLogger(LogUploadController.class);

    @SuppressWarnings("unused")
    @ModelAttribute("allLogLevels")
    public LogLevel[] allLogLevels() {
        return LogLevel.values();
    }

    @GetMapping("/")
    public String upload(Model model) {
        logger.debug("Serving upload form");

        model.addAttribute("logUploadForm", new LogUploadForm());

        return "upload";
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleUpload(
            @Valid @ModelAttribute("logUploadForm") LogUploadForm form,
            BindingResult bindingResult,
            RedirectAttributes redirect,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "upload";
        }

        MultipartFile file = form.getFile();
        Optional<String> originalFilename = Optional.ofNullable(file.getOriginalFilename());

        if (originalFilename.isEmpty()) {
            logger.warn("Uploaded file is missing filename");

            redirect.addFlashAttribute("error", "Missing filename");

            return "redirect:/";
        }

        String filename = UUID.randomUUID() + "-" + originalFilename.get();

        try {
            Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path dest = tmpDir.resolve(filename);

            file.transferTo(dest);

            redirect.addFlashAttribute("path", dest.toString());

            logger.info("Successfully saved file: {}", filename);
        } catch (IOException e) {
            logger.warn("Failed to save file: {}", filename, e);

            redirect.addFlashAttribute("error", "Something went wrong while saving the file");

            return "redirect:/";
        }

        return "redirect:/result";
    }

    @GetMapping("/result")
    public String result(@ModelAttribute(value = "path", binding = false) String path, Model model) {

        if (path == null || path.isEmpty()) {
            return "redirect:/";
        }

        Path filePath = Paths.get(path);
        String filename = String.valueOf(filePath.getFileName());
        Optional<String> content;

        try {
            content = Optional.of(Files.readString(filePath));

            logger.info("Successfully read file: {}", filename);
        } catch (IOException e) {
            logger.warn("Failed to read file {}", filename, e);

            content = Optional.empty();
        } finally {
            try {
                if (Files.deleteIfExists(filePath)) {
                    logger.debug("Successfully deleted file: {}", filePath);
                } else {
                    logger.debug("Failed to delete file (non-existing file): {}", filePath);
                }
            } catch (IOException e) {
                logger.warn("Failed to delete file: {}", filename, e);
            }
        }

        ResultView result = new ResultView(content);

        model.addAttribute("result", result);

        return "result";
    }
}
