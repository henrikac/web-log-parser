package com.github.henrikac.weblogparser.service;

import com.github.henrikac.logparser.cli.CommandLineOption;
import com.github.henrikac.logparser.core.LogLevel;
import com.github.henrikac.logparser.core.LogParser;
import com.github.henrikac.logparser.core.LogRecord;
import com.github.henrikac.weblogparser.exception.ProcessingException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class LogProcessingService {

    private final LogParser parser;

    public LogProcessingService(LogParser parser) {
        this.parser = parser;
    }

    public List<String> parseLogFile(List<CommandLineOption> options) throws ProcessingException {
        List<Path> paths = new ArrayList<>();
        EnumSet<LogLevel> levels = EnumSet.noneOf(LogLevel.class);

        for (CommandLineOption option : options) {
            if (option.getArg().equals("--file")) {
                paths.add(Paths.get(option.getValue()));
            } else if (option.getArg().equals("--level")) {
                levels.add(LogLevel.fromString(option.getValue()));
            }
        }

        Predicate<LogRecord> byLevel = record -> levels.contains(record.level());

        ArrayList<String> result = new ArrayList<>(1024);

        for (Path p : paths) {
            try (Stream<String> lines = this.readLines(p)) {
                lines.map(this.parser::parseLine)
                    .filter(Objects::nonNull)
                    .filter(byLevel)
                    .map(LogRecord::line)
                    .forEachOrdered(result::add);
            } catch (IOException e) {
                throw new ProcessingException("Failed to process file: " + p.getFileName(), e);
            }
        }

        return result;
    }

    private Stream<String> readLines(Path path) throws IOException {
        InputStream in = Files.newInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        return reader.lines().onClose(() -> {
            try { reader.close(); } catch (IOException ignore) {}
        });
    }
}
