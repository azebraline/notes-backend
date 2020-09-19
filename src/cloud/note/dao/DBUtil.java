package cloud.note.dao;

import java.sql.*;

public class DBUtil {
	// 一个数据库的连接/关闭
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	/**
	 * 得到数据库连接
	 */
	public Connection myGetConnection()
			throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		String DRIVER = "com.mysql.cj.jdbc.Driver";
		//String URL = "jdbc:mysql://localhost:3306/userLogin?useUnicode=true&useSSL=false&serverTimezone=UTC";
		String URL = "jdbc:mysql://localhost:3306/notes?useUnicode=true&useSSL=false&serverTimezone=UTC";

		String USERNAME = "root";
		String PASSWORD = "admin";

		try {
			// 指定驱动程序
			Class.forName(DRIVER);
			// 建立数据库连结
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			return conn;
		} catch (Exception e) {
			// 如果连接过程出现异常，抛出异常信息
			e.printStackTrace();
			System.out.println(conn);
			throw new SQLException("驱动错误或连接失败！");
		}
	}

	/**
	 * 释放资源
	 */
	public void closeAll() {
		// 如果 rs不空，关闭rs
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 如果pstmt不空，关闭pstmt
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 如果conn不空，关闭conn
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
			pstmt = conn.prepareStatement(preparedSql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					// 为预编译 sql设置参数
					pstmt.setString(i + 1, param[i]);
				}
			}
			// 执行SQL语句
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// 处理SQLException异常
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
			// 得到PreparedStatement对象
			pstmt = conn.prepareStatement(preparedSql);
			if (param != null) {
				for (int i = 0; i < param.length; i++) {
					// 为预编译sql设置参数
					pstmt.setString(i + 1, param[i]);
				}
			}
			// 执行SQL语句
			num = pstmt.executeUpdate();
		} catch (SQLException e) {
			// 处理SQLException异常
			e.printStackTrace();
		}
		return num;
	}
}
