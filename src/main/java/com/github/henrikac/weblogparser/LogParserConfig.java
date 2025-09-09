package com.github.henrikac.weblogparser;

import com.github.henrikac.logparser.core.LogParser;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LogParserConfig {
    @Bean
    public LogParser logParser() {
        return new LogParser();
    }
}
