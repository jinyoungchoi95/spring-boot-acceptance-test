package com.ori.acceptancetest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

class DatabaseCleanerExtension implements AfterEachCallback {

    @Override
    @Transactional
    public void afterEach(final ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            executeResetTableQuery(jdbcTemplate, rs);
        } catch (Exception exception) {
            throw new RuntimeException("database table clean error");
        }
    }

    private void executeResetTableQuery(final JdbcTemplate jdbcTemplate, final ResultSet rs) throws SQLException {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            jdbcTemplate.execute(createTruncateTableQuery(tableName));
            jdbcTemplate.execute(createResetAutoIncrementQuery(tableName));
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private String createTruncateTableQuery(final String tableName) {
        return "TRUNCATE TABLE " + tableName;
    }

    private String createResetAutoIncrementQuery(final String tableName) {
        return "ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1";
    }
}
