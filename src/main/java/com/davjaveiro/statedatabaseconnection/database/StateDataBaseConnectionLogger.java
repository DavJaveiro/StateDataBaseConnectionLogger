package com.davjaveiro.statedatabaseconnection.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class StateDataBaseConnectionLogger {

    private static final Logger logger = LoggerFactory.getLogger(StateDataBaseConnectionLogger.class);
    private final DataSource dataSource;

    public StateDataBaseConnectionLogger(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void verifyDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isClosed()) {
                logger.info("✅ Database connection, from datasource, successfully established!");
            }
        } catch (Exception e) {
            logger.error("❌ Failed to establish database connection from datasource.", e);
        }
    }
}
