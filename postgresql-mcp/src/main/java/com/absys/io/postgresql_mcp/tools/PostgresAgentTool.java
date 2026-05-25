package com.absys.io.postgresql_mcp.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresAgentTool {

    private final JdbcTemplate jdbcTemplate;

    @Tool(name = "postgres_create", description = "Create table")
    public String createTable(String tableName, String schema) {
        String sql = null;
        log.info("create table called: tableName={}, schema={}",
                tableName,
                schema);

        try {
            if (!tableName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_create] Invalid table name: {}", tableName);
                return "Invalid table name";
            }

            sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + schema + ")";

            log.info("[postgres_create] " +
                    "Executing SQL: {}", sql);

            jdbcTemplate.execute(sql);

            log.info("[postgres_create] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_create] Failed SQL: {}", sql, e);
            return "ERROR: " + e.getMessage();
        }
    }


    @Tool(name = "postgres_insert", description = "Insert one or more rows into a table")
    public String insert(String table, String[] columns, Object[][] values) {

        String sql = null;
        log.info("insert called: table={}, columns={}, values={}",
                table,
                Arrays.toString(columns),
                Arrays.deepToString(values));

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_insert] Invalid table name: {}", table);
                return "Invalid table name";
            }

            String cols = String.join(", ", columns);
            String placeholders = String.join(", ", java.util.Collections.nCopies(columns.length, "?"));

            sql = "INSERT INTO " + table +
                    " (" + cols + ") VALUES (" + placeholders + ")";

            log.info("[postgres_insert] Executing SQL: {} | values={}", sql, values);

//            jdbcTemplate.batchUpdate(sql, Arrays.asList(values));

            if (values.length == 1) {
                jdbcTemplate.update(sql, values[0]);
            } else {
                jdbcTemplate.batchUpdate(sql, Arrays.asList(values));
            }

            log.info("[postgres_insert] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_insert] Failed SQL: {} | data={}", sql, values, e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name = "postgres_update", description = "Update table rows")
    public String update(String table,
                         String[] columns,
                         Object[] values,
                         String whereColumn,
                         String whereValue) {

        // SAFE LOGGING (no NPE, no ToolContext leaks)
        log.info("update called: table={}, columns={}, values={}, whereColumn={}, whereValue={}",
                table,
                Arrays.toString(columns),
                Arrays.toString(values),
                whereColumn,
                whereValue
        );

        // VALIDATION
        if (table == null || !table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return "Invalid table name";
        }

        if (columns == null || values == null || columns.length != values.length) {
            return "Columns and values must be non-null and of same length";
        }

        if (whereColumn == null || !whereColumn.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
            return "Invalid whereColumn";
        }

        if (whereValue.trim().isEmpty()) {
            return "Invalid whereValue";
        }

        // BUILD SET CLAUSE SAFELY
        String setClause = java.util.Arrays.stream(columns)
                .map(col -> col + "=?")
                .collect(java.util.stream.Collectors.joining(", "));

        String sql = "UPDATE " + table +
                " SET " + setClause +
                " WHERE " + whereColumn + "=?";

        // BUILD PARAMETERS (STRICT ORDER)
        List<Object> params = new ArrayList<>();

        for (Object v : values) {
            if (v instanceof java.util.List<?>) {
                params.addAll((java.util.List<?>) v);
            } else {
                params.add(v);
            }
        }

        params.add(whereValue);

        try {
            jdbcTemplate.update(sql, params.toArray());
            log.info("[postgres_update] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_update] Failed SQL: {} | params={}",
                    sql,
                    params,
                    e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name = "postgres_delete", description = "Delete from table")
    public String delete(String table, String whereColumn, Object whereValue) {

        String sql = null;

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_delete] Invalid table name: {}", table);
                return "Invalid table name";
            }

            sql = "DELETE FROM " + table + " WHERE " + whereColumn + "=?";

            jdbcTemplate.update(sql, whereValue);

            log.info("[postgres_delete] Success");

            return "OK";

        } catch (Exception e) {
            log.error("[postgres_delete] Failed SQL: {}", sql, e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name = "postgres_query", description = "Select from table")
    public List<Map<String, Object>> query(String table,
                                           String whereColumn,
                                           Object whereValue) {

        String sql = null;

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_query] Invalid table name: {}", table);
                return List.of(Map.of("error", "Invalid table"));
            }

            Object[] params;

            if (whereColumn == null || whereColumn.isBlank()) {
                sql = "SELECT * FROM " + table;
                params = new Object[]{};
            } else {
                sql = "SELECT * FROM " + table + " WHERE " + whereColumn + "=?";
                params = new Object[]{whereValue};
            }
            log.info("[postgres_query] Executing SQL: {} | args={}", sql, params);

            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params);
            log.info("[postgres_query] Success | rows={}", result.size());

            return result;

        } catch (Exception e) {
            log.error("[postgres_query] Failed SQL: {}", sql, e);
            return List.of(Map.of("error", e.getMessage()));
        }
    }
}