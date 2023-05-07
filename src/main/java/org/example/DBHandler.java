package org.example;

import org.json.JSONObject;

import java.sql.*;

public class DBHandler {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/test";
    private static final String USER = "postgres";
    private static final String PASS = "yamato2202";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        }

        public static boolean checkIfExists(JSONObject jsonObject) {
            // column1 と column2 の値で重複をチェックします
            String query = "SELECT COUNT(*) FROM test WHERE column1 = ? AND column2 = ?";
            String column1 = jsonObject.getString("column1");
            String column2 = jsonObject.getString("column2");

            try (Connection connection = getConnection();
                 PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, column1);
                pstmt.setString(2, column2);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        public static synchronized int getNextId() {
// データベースに保存されている最大の id の次の値を取得します
            String query = "SELECT MAX(id) FROM test";
            try (Connection connection = getConnection();
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1; // テーブルが空の場合、id は 1 から始まります
        }

        public static void insertJsonObject(JSONObject jsonObject) {
            // 既存のデータと重複しないことを確認
            if (!checkIfExists(jsonObject)) {
                // 重複がない場合は、そのままデータを挿入する
                String query = "INSERT INTO test (id, column1, column2) VALUES (?, ?, ?)";
                try (Connection connection = getConnection();
                     PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setInt(1, getNextId());
                    pstmt.setString(2, jsonObject.getString("column1"));
                    pstmt.setString(3, jsonObject.getString("column2"));
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

