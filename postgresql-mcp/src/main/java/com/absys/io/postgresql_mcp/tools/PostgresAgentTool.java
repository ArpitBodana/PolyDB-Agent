//package com.absys.io.postgresql_mcp.tools;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class PostgresAgentTool {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Tool(name = "postgres_create", description = "Create a new table")
//    public String createTable(String tableName, String schema) {
//        String sql = null;
//        log.info("create table called: tableName={}, schema={}", tableName, schema);
//
//        try {
//            if (!isValidIdentifier(tableName)) return "Invalid table name";
//
//            sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + schema + ")";
//            log.info("[postgres_create] Executing SQL: {}", sql);
//            jdbcTemplate.execute(sql);
//
//            log.info("[postgres_create] Success");
//            return "OK";
//        } catch (Exception e) {
//            log.error("[postgres_create] Failed SQL: {}", sql, e);
//            return "ERROR: " + e.getMessage();
//        }
//    }
//
//    @Tool(name = "postgres_insert", description = "Insert one or more rows into a table.")
//    public String insert(String table, List<String> columns, List<List<Object>> values) {
//        String sql = null;
//        log.info("insert called: table={}, columns={}, valuesSize={}",
//                table, columns, values != null ? values.size() : 0);
//
//        try {
//            if (!isValidIdentifier(table)) return "Invalid table name";
//            if (columns == null || columns.isEmpty() || values == null || values.isEmpty()) {
//                return "Columns and values cannot be null/empty";
//            }
//
//            String cols = String.join(", ", columns);
//            String placeholders = String.join(", ", Collections.nCopies(columns.size(), "?"));
//
//            sql = "INSERT INTO " + table + " (" + cols + ") VALUES (" + placeholders + ")";
//            log.info("[postgres_insert] Executing SQL: {}", sql);
//
//            // Convert List<List<Object>> to List<Object[]> for JdbcTemplate
//            List<Object[]> batchArgs = values.stream()
//                    .map(List::toArray)
//                    .collect(Collectors.toList());
//
//            if (batchArgs.size() == 1) {
//                jdbcTemplate.update(sql, batchArgs.getFirst());
//            } else {
//                jdbcTemplate.batchUpdate(sql, batchArgs);
//            }
//
//            log.info("[postgres_insert] Success");
//            return "OK";
//        } catch (Exception e) {
//            log.error("[postgres_insert] Failed SQL: {}", sql, e);
//            return "ERROR: " + e.getMessage();
//        }
//    }
//
//    @Tool(name = "postgres_update", description = "Update table rows.")
//    public String update(String table, List<String> columns, List<Object> values, String whereClause, List<Object> whereArgs) {
//        log.info("update called: table={}, columns={}, whereClause={}", table, columns, whereClause);
//
//        try {
//            if (!isValidIdentifier(table)) return "Invalid table name";
//            if (columns == null || values == null || columns.size() != values.size()) {
//                return "Columns and values must be non-null and of the same size";
//            }
//            if (whereClause == null || whereClause.isBlank()) {
//                return "Safety Error: whereClause cannot be empty. To update all rows, specify '1=1'.";
//            }
//
//            String setClause = columns.stream()
//                    .map(col -> col + "=?")
//                    .collect(Collectors.joining(", "));
//
//            String sql = "UPDATE " + table + " SET " + setClause + " WHERE " + whereClause;
//
//            List<Object> params = new ArrayList<>(values);
//            if (whereArgs != null) {
//                params.addAll(whereArgs);
//            }
//
//            log.info("[postgres_update] Executing SQL: {} | args={}", sql, params);
//            int rowsAffected = jdbcTemplate.update(sql, params.toArray());
//
//            log.info("[postgres_update] Success, rows affected: {}", rowsAffected);
//            return "OK. Rows affected: " + rowsAffected;
//        } catch (Exception e) {
//            log.error("[postgres_update] Failed", e);
//            return "ERROR: " + e.getMessage();
//        }
//    }
//
//    @Tool(name = "postgres_delete", description = "Delete rows from a table.")
//    public String delete(String table, String whereClause, List<Object> whereArgs) {
//        log.info("delete called: table={}, whereClause={}", table, whereClause);
//        String sql = null;
//
//        try {
//            if (!isValidIdentifier(table)) return "Invalid table name";
//            if (whereClause == null || whereClause.isBlank()) {
//                return "Safety Error: whereClause cannot be empty. To delete all rows, specify '1=1'.";
//            }
//
//            sql = "DELETE FROM " + table + " WHERE " + whereClause;
//
//            Object[] params = (whereArgs != null) ? whereArgs.toArray() : new Object[]{};
//
//            log.info("[postgres_delete] Executing SQL: {} | args={}", sql, params);
//            int rowsAffected = jdbcTemplate.update(sql, params);
//
//            log.info("[postgres_delete] Success, rows affected: {}", rowsAffected);
//            return "OK. Rows affected: " + rowsAffected;
//        } catch (Exception e) {
//            log.error("[postgres_delete] Failed SQL: {}", sql, e);
//            return "ERROR: " + e.getMessage();
//        }
//    }
//
//    @Tool(name = "postgres_query", description = "Query a table. Can specify selectColumns (e.g., 'id, name' or '*'), whereClause (e.g., 'id = ?'), whereArgs list, orderBy, and limit.")
//    public List<Map<String, Object>> query(String table, String selectColumns, String whereClause, List<Object> whereArgs, String orderBy, Integer limit) {
//        log.info("query called: table={}, whereClause={}", table, whereClause);
//        String sql = null;
//
//        try {
//            if (!isValidIdentifier(table)) return List.of(Map.of("error", "Invalid table"));
//
//            String cols = (selectColumns != null && !selectColumns.isBlank()) ? selectColumns : "*";
//            sql = "SELECT " + cols + " FROM " + table;
//
//            if (whereClause != null && !whereClause.isBlank()) {
//                sql += " WHERE " + whereClause;
//            }
//            if (orderBy != null && !orderBy.isBlank()) {
//                sql += " ORDER BY " + orderBy;
//            }
//            if (limit != null && limit > 0) {
//                sql += " LIMIT " + limit;
//            }
//
//            Object[] params = (whereArgs != null) ? whereArgs.toArray() : new Object[]{};
//            log.info("[postgres_query] Executing SQL: {} | args={}", sql, params);
//
//            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, params);
//            log.info("[postgres_query] Success | rows={}", result.size());
//
//            return result;
//        } catch (Exception e) {
//            log.error("[postgres_query] Failed SQL: {}", sql, e);
//            return List.of(Map.of("error", e.getMessage()));
//        }
//    }
//
//    @Tool(name = "postgres_get_schema", description = "Get the schema (columns, data types, nullability, defaults) of a specific table. ALWAYS use this before inserting or updating data if you do not know the exact table structure.")
//    public List<Map<String, Object>> getTableSchema(String table) {
//        log.info("getTableSchema called: table={}", table);
//        String sql = null;
//
//        try {
//            if (!isValidIdentifier(table)) {
//                return List.of(Map.of("error", "Invalid table name"));
//            }
//
//            // Query PostgreSQL information_schema to get column details
//            sql = "SELECT column_name, data_type, is_nullable, column_default " +
//                    "FROM information_schema.columns " +
//                    "WHERE table_name = ? " +
//                    // Optionally uncomment the next line if you only want the public schema
//                    // "AND table_schema = 'public' " +
//                    "ORDER BY ordinal_position";
//
//            log.info("[postgres_get_schema] Executing SQL: {} | table={}", sql, table);
//            List<Map<String, Object>> schema = jdbcTemplate.queryForList(sql, table);
//
//            if (schema.isEmpty()) {
//                log.warn("[postgres_get_schema] Table '{}' not found or empty", table);
//                return List.of(Map.of("warning", "Table not found or has no columns."));
//            }
//
//            log.info("[postgres_get_schema] Success | columns={}", schema.size());
//            return schema;
//
//        } catch (Exception e) {
//            log.error("[postgres_get_schema] Failed SQL: {}", sql, e);
//            return List.of(Map.of("error", e.getMessage()));
//        }
//    }
//
//    /**
//     * Helper method to validate table names and prevent basic syntax breaking.
//     */
//    private boolean isValidIdentifier(String identifier) {
//        return identifier != null && identifier.matches("[a-zA-Z_][a-zA-Z0-9_]*");
//    }
//}