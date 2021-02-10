package preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import bean.Customer;
import bean.Order;
import util.JDBCutil;

/**
 * ʹ��	preparedstatementʵ����Բ�ͬ���ͨ�ò�ѯ����
 * @author yf
 *
 */
public class PreparedStatementQueryTest {
	@Test
	public void testGetForList() {
		String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id < ?";
		List<Order> list = getForList(Order.class, sql, 3);
		list.forEach(System.out::println);
	}
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCutil.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// ִ�У���ȡ�����
			rs = ps.executeQuery();
			// ��ȡ�����Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			// ��ȡ����
			int columnCount = rsmd.getColumnCount();
			//�������϶���
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				@SuppressWarnings("deprecation")
				T t = clazz.newInstance();
				//��������һ�������е�ÿһ�У���t����ָ��һ�����Ը�ֵ
				for (int i = 0; i < columnCount; i++) {
					// ��ȡÿ���е���ֵ:ͨ��ResultSet
					Object columnValue = rs.getObject(i + 1);
					// ��ȡÿ���е�������ͨ��ResultSetMetaData
					// String columnName = rsmd.getColumnName(i + 1);
					// ��ȡ�еı���:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// ͨ������,������ָ����columnName�����Ը�ֵΪָ����columnValue
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCutil.closeResource(conn, ps, rs);
		}
		return null;
	}
	@Test
	public void testGetInstance() {
		String sql = "select id,name,email,birth from customers where id = ?";
		Customer customer = getInstance(Customer.class, sql, 2);
		System.out.println(customer);
	}
	/**
	 * ʵ����Բ�ͬ���ͨ�õ�һ����ѯ����
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCutil.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			// ִ�У���ȡ�����
			rs = ps.executeQuery();
			// ��ȡ�����Ԫ����
			ResultSetMetaData rsmd = rs.getMetaData();
			// ��ȡ����
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// ��ȡÿ���е���ֵ:ͨ��ResultSet
					Object columnValue = rs.getObject(i + 1);
					// ��ȡÿ���е�������ͨ��ResultSetMetaData
					// String columnName = rsmd.getColumnName(i + 1);
					// ��ȡ�еı���:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// ͨ������,������ָ����columnName�����Ը�ֵΪָ����columnValue
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columnValue);
				}
				return t;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			JDBCutil.closeResource(conn, ps, rs);
		}
		return null;
	}
}
