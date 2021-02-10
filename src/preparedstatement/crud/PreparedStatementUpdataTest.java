package preparedstatement.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Test;

import util.JDBCutil;

//ʹ��PreparedStatement���滻	Statement��ʵ�ֶ����ݵ���ɾ�Ĳ����

public class PreparedStatementUpdataTest {
	@Test
	public void testCommonUpdate() {
		String sql = "delete from customers where id = ?";
		upDate(sql, 1);
	}

	// ͨ�õ���ɾ�Ĳ���
	public void upDate(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// ��ȡ���ݿ�����
			conn = JDBCutil.getConnection();
			// Ԥ����sql��䣬����prepareStatementʵ��
			ps = conn.prepareStatement(sql);
			// ���ռλ��
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// С�Ĳ�����������
			}
			// ִ��sql
			ps.execute();
			// �ر���Դ
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutil.closeResource(conn, ps);
		}
	}

	// ɾ��һ����Ϣ
	@Test
	public void testUpdateTest() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// ��ȡ���ݿ�����
			conn = JDBCutil.getConnection();
			// Ԥ����sql��䣬����prepareStatementʵ��
			String sql = "update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			// ���ռλ��
			ps.setObject(1, "ľ߸");
			ps.setObject(2, 1);
			// ִ��sql
			ps.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// �ر���Դ
			JDBCutil.closeResource(conn, ps);
		}
	}

	// ���
	@Test
	public void testInsert() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
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
			conn = DriverManager.getConnection(url, user, password);
			// System.out.println(conn);
			// Ԥ����sql��䣬����prepareStatementʵ��
			String sql = "insert into customers(name,email,birth)value(?,?,?)";
			ps = conn.prepareStatement(sql);
			// ���ռλ��
			ps.setString(1, "��߸");
			ps.setString(2, "nezha@gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1000-01-01");
			ps.setDate(3, new Date(date.getTime()));
			// ִ��sql
			ps.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

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
	}
}
