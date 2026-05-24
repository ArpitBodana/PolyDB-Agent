package com.absys.io.postgresql_mcp.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostgresAgentTool {

    private final JdbcTemplate jdbcTemplate;

    @Tool(name="postgres_create", description = "Create table")
    public String createTable(String tableName, String schema) {
        String sql = null;

        try {
            if (!tableName.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_create] Invalid table name: {}", tableName);
                return "Invalid table name";
            }

            sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + schema + ")";

            log.info("[postgres_create] Executing SQL: {}", sql);

            jdbcTemplate.execute(sql);

            log.info("[postgres_create] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_create] Failed SQL: {}", sql, e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name="postgres_insert", description = "Insert into table")
    public String insert(String table, Map<String, Object> data) {

        String sql = null;

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_insert] Invalid table name: {}", table);
                return "Invalid table name";
            }

            String cols = String.join(", ", data.keySet());
            String placeholders = String.join(", ", data.keySet().stream().map(k -> "?").toList());

            sql = "INSERT INTO " + table +
                    " (" + cols + ") VALUES (" + placeholders + ")";

            log.info("[postgres_insert] Executing SQL: {} | values={}", sql, data.values());

            jdbcTemplate.update(sql, data.values().toArray());

            log.info("[postgres_insert] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_insert] Failed SQL: {} | data={}", sql, data, e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name="postgres_update", description = "Update table rows")
    public String update(String table,
                         Map<String, Object> data,
                         String where,
                         Object[] args) {

        String sql = null;

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_update] Invalid table name: {}", table);
                return "Invalid table name";
            }

            String set = String.join(", ",
                    data.keySet().stream().map(k -> k + "=?").toList()
            );

            Object[] params = concat(data.values().toArray(), args);

            sql = "UPDATE " + table + " SET " + set + " WHERE " + where;

            log.info("[postgres_update] Executing SQL: {} | params={}", sql, params);

            jdbcTemplate.update(sql, params);

            log.info("[postgres_update] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_update] Failed SQL: {}", sql, e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name="postgres_delete", description = "Delete from table")
    public String delete(String table, String where, Object[] args) {

        String sql = null;

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_delete] Invalid table name: {}", table);
                return "Invalid table name";
            }

            sql = "DELETE FROM " + table + " WHERE " + where;

            log.info("[postgres_delete] Executing SQL: {} | args={}", sql, args);

            jdbcTemplate.update(sql, args);

            log.info("[postgres_delete] Success");
            return "OK";

        } catch (Exception e) {
            log.error("[postgres_delete] Failed SQL: {}", sql, e);
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool(name="postgres_query", description = "Select from table")
    public List<Map<String, Object>> query(String table, String where, Object[] args) {

        String sql = null;

        try {
            if (!table.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                log.warn("[postgres_query] Invalid table name: {}", table);
                return List.of(Map.of("error", "Invalid table"));
            }

            sql = "SELECT * FROM " + table +
                    (where == null || where.isBlank() ? "" : " WHERE " + where);

            log.info("[postgres_query] Executing SQL: {} | args={}", sql, args);

            List<Map<String, Object>> result =
                    jdbcTemplate.queryForList(sql, args == null ? new Object[]{} : args);

            log.info("[postgres_query] Success | rows={}", result.size());

            return result;

        } catch (Exception e) {
            log.error("[postgres_query] Failed SQL: {}", sql, e);
            return List.of(Map.of("error", e.getMessage()));
        }
    }

    private Object[] concat(Object[] a, Object[] b) {
        Object[] r = new Object[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }
}