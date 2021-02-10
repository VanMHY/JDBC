package connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class ConnectionTest {
	// ��ʽһ
	@Test
	public void testConntion1() throws SQLException {
		//��ȡ	Driverʵ�������
		Driver driver = new com.mysql.jdbc.Driver();
		// jdbc :mysqlΪЭ��
		// localhost��ip��ַ
		// 3306��Ĭ�϶˿ں�
		// testΪ���ݿ�
		String url = "jdbc :mysql://localhost:3306/test";
		// ���û����������װ��Properties��
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "123456");
		Connection conn = driver.connect(url, info);
		System.out.println(conn);
	}
	//��ʽ���Է�ʽһ�ĵ���:�����ֵ�����apiʹ�����и��õĿ���ֲ��
	@Test
	public void testConntion2() throws Exception  {
		//��ȡ	Driverʵ�������ʹ�÷���
		Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
		@SuppressWarnings("deprecation")
		Driver driver = (Driver)clazz.newInstance();
		//�ṩҪ���ӵ����ݿ�
		String url = "jdbc :mysql://localhost:3306/test";
		//�ṩ������Ҫ���û���������
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "123456");
		//��ȡ����
		Connection conn = driver.connect(url, info);
		System.out.println(conn);
	}
	//��ʽ����ʹ��DriverManager�滻Driver
	@Test
	public void testConntion3() throws Exception {
		//��ȡDriverʵ�������
		Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
		@SuppressWarnings("deprecation")
		Driver driver = (Driver)clazz.newInstance();
		//�����������ӵĻ�����Ϣ
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";
		//ע������
		DriverManager.registerDriver(driver);
		//��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	//��ʽ�ģ�����ֻ�Ǽ���������������ʾ��ע������
	@Test
	public void testConntion4() throws Exception {
		// �����������ӵĻ�����Ϣ
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123456";
		//��������
		Class.forName("com.mysql.jdbc.Driver");
//		Driver driver = (Driver) clazz.newInstance();
//		// ע������
//		DriverManager.registerDriver(driver);
		// ��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
	//��ʽ��(final��)�������ݿ�������Ҫ���ĸ�������Ϣ�����������ļ��У�ͨ����ȡ�������ļ��ķ�ʽ����ȡ����
	@Test
	public void getConnection5() throws Exception {
		//��ȡ�����ļ����ĸ�������Ϣ
		InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		//��������
		Class.forName(driverClass);
		//��ȡ����
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println(conn);
	}
}
