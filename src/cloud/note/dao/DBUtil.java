package cloud.note.dao;

import java.sql.*;

public class DBUtil {
    Connection conn;
    PreparedStatement preparedStatement;
    ResultSet rs;

    /**
     * 得到数据库连接
     */
    public void myGetConnection() throws SQLException {
        String DRIVER = "com.mysql.cj.jdbc.Driver";
        String URL = "jdbc:mysql://localhost:3306/notes?useUnicode=true&useSSL=false&serverTimezone=UTC";

        String USERNAME = "root";
        String PASSWORD = "admin";

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("驱动错误或连接失败！");
        }
    }

    /**
     * 释放资源
     */
    public void closeAll() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行SQL语句，可以进行查询
     */
    public ResultSet myExecuteQuery(String preparedSql, String[] param) {
        // 处理SQL,执行SQL
        try {
            // 得到PreparedStatement对象
            preparedStatement = conn.prepareStatement(preparedSql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    // 为预编译 sql设置参数
                    preparedStatement.setString(i + 1, param[i]);
                }
            }
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 执行SQL语句，可以进行增、删、改的操作，不能执行查询
     */
    public int myExecuteUpdate(String preparedSql, String[] param) {
        int num = 0;
        // 处理SQL,执行SQL
        try {
            preparedStatement = conn.prepareStatement(preparedSql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setString(i + 1, param[i]);
                }
            }
            num = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }
}
