package com.github.henrikac.weblogparser.controller;

import com.github.henrikac.weblogparser.form.LogUploadForm;
import com.github.henrikac.weblogparser.view.ResultView;
import jakarta.validation.Valid;
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

    @GetMapping("/")
    public String upload(Model model) {
        model.addAttribute("logUploadForm", new LogUploadForm());

        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(
            @Valid @ModelAttribute("logUploadForm") LogUploadForm form,
            BindingResult bindingResult,
            RedirectAttributes redirect,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "upload";
        }

        MultipartFile file = form.getFile();
        Optional<String> filename = Optional.ofNullable(file.getOriginalFilename());

        if (filename.isEmpty()) {
            redirect.addFlashAttribute("error", "Missing filename");

            return "redirect:/";
        }

        try {
            Path tmpDir = Paths.get(System.getProperty("java.io.tmpdir"));
            Path dest = tmpDir.resolve(UUID.randomUUID() + "-" + filename);

            file.transferTo(dest);

            redirect.addFlashAttribute("path", dest.toString());
        } catch (IOException e) {
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
        Optional<String> content;

        try {
            content = Optional.of(Files.readString(filePath));
        } catch (IOException _) {
            content = Optional.empty();
        } finally {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException _) {}
        }

        ResultView result = new ResultView(content);

        model.addAttribute("result", result);

        return "result";
    }
}
