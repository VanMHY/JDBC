package connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class ConnectionTest {
	// 方式一
	@Test
	public void testConntion1() throws SQLException {
		//获取	Driver实现类对象
		Driver driver = new com.mysql.jdbc.Driver();
		// jdbc :mysql为协议
		// localhost：ip地址
		// 3306：默认端口号
		// test为数据库
		String url = "jdbc :mysql://localhost:3306/test";
		// 将用户名和密码封装在Properties中
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "123456");
		Connection conn = driver.connect(url, info);
		System.out.println(conn);
	}
	//方式二对方式一的迭代:不出现第三方api使程序有更好的可移植性
	@Test
	public void testConntion2() throws Exception  {
		//获取	Driver实现类对象：使用反射
		Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
		@SuppressWarnings("deprecation")
		Driver driver = (Driver)clazz.newInstance();
		//提供要连接的数据库
		String url = "jdbc :mysql://localhost:3306/test";
		//提供连接需要的用户名和密码
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "123456");
		//获取连接
		Connection conn = driver.connect(url, info);
		System.out.println(conn);
	}
	//方式三：使用DriverManager替换Driver
	@Test
	public void testConntion3() throws Exception {
		//获取Driver实现类对象
		Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
		@SuppressWarnings("deprecation")
		Driver driver = (Driver)clazz.newInstance();
		//另外三个连接的基本信息
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";
		//注册驱动
		DriverManager.registerDriver(driver);
		//获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	//方式四：可以只是加载驱动，不用显示的注册驱动
	@Test
	public void testConntion4() throws Exception {
		// 另外三个连接的基本信息
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";
		//加载驱动
		Class.forName("com.mysql.jdbc.Driver");
//		Driver driver = (Driver) clazz.newInstance();
//		// 注册驱动
//		DriverManager.registerDriver(driver);
		// 获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	//方式五(final版)：将数据库连接需要的四个基本信息声明到配置文件中，通过读取怕配置文件的方式，获取连接
	@Test
	public void getConnection5() throws Exception {
		//读取配置文件的四个基本信息
		InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		//加载驱动
		Class.forName(driverClass);
		//获取连接
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
}
