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

//使用PreparedStatement来替换	Statement，实现对数据的增删改查操作

public class PreparedStatementUpdataTest {
	@Test
	public void testCommonUpdate() {
		String sql = "delete from customers where id = ?";
		upDate(sql, 1);
	}

	// 通用的增删改操作
	public void upDate(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 获取数据库连接
			conn = JDBCutil.getConnection();
			// 预编译sql语句，返回prepareStatement实例
			ps = conn.prepareStatement(sql);
			// 填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// 小心参数申明错误
			}
			// 执行sql
			ps.execute();
			// 关闭资源
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutil.closeResource(conn, ps);
		}
	}

	// 删除一条信息
	@Test
	public void testUpdateTest() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 获取数据库连接
			conn = JDBCutil.getConnection();
			// 预编译sql语句，返回prepareStatement实例
			String sql = "update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			// 填充占位符
			ps.setObject(1, "木吒");
			ps.setObject(2, 1);
			// 执行sql
			ps.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// 关闭资源
			JDBCutil.closeResource(conn, ps);
		}
	}

	// 添加
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
			// 加载驱动
			Class.forName(driverClass);
			// 获取连接
			conn = DriverManager.getConnection(url, user, password);
			// System.out.println(conn);
			// 预编译sql语句，返回prepareStatement实例
			String sql = "insert into customers(name,email,birth)value(?,?,?)";
			ps = conn.prepareStatement(sql);
			// 填充占位符
			ps.setString(1, "哪吒");
			ps.setString(2, "nezha@gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1000-01-01");
			ps.setDate(3, new Date(date.getTime()));
			// 执行sql
			ps.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			// 关闭资源
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
