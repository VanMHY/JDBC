package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * �������ݿ�Ĺ�����
 * 
 * @author yf
 *
 */
public class JDBCutil {
	/**
	 * ��ȡ����
	 * 
	 * @return
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		// ��������
		Class.forName(driverClass);
		// ��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}

	/**
	 * �ر���Դ
	 * 
	 * @param conn
	 * @param ps
	 */
	public static void closeResource(Connection conn, Statement ps) {
		// �ر���Դ
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
		// �ر���Դ
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
