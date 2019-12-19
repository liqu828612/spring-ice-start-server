package com.orator.spring.ice.start.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerI implements Ice.Logger {

    private final Logger logger = LoggerFactory.getLogger(LoggerI.class);

    private final String prefix;

    public LoggerI(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void print(String message) {
        logger.info(message);
    }

    @Override
    public void trace(String category, String message) {
        logger.debug(String.format("[%s]%s", category, message));
    }

    @Override
    public void warning(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public Ice.Logger cloneWithPrefix(String prefix) {
        return new LoggerI(prefix);
    }
}
