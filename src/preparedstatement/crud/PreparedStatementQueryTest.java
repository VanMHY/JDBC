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
 * 使用	preparedstatement实现针对不同表的通用查询操作
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
			// 执行，获取结果集
			rs = ps.executeQuery();
			// 获取结果集元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			// 获取列数
			int columnCount = rsmd.getColumnCount();
			//创建集合对象
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				@SuppressWarnings("deprecation")
				T t = clazz.newInstance();
				//处理结果集一行数据中的每一列：给t对象指定一个属性赋值
				for (int i = 0; i < columnCount; i++) {
					// 获取每个列的列值:通过ResultSet
					Object columnValue = rs.getObject(i + 1);
					// 获取每个列的列名：通过ResultSetMetaData
					// String columnName = rsmd.getColumnName(i + 1);
					// 获取列的别名:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 通过反射,将对象指定名columnName的属性赋值为指定的columnValue
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
	 * 实现针对不同表的通用的一条查询操作
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
			// 执行，获取结果集
			rs = ps.executeQuery();
			// 获取结果集元数据
			ResultSetMetaData rsmd = rs.getMetaData();
			// 获取列数
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// 获取每个列的列值:通过ResultSet
					Object columnValue = rs.getObject(i + 1);
					// 获取每个列的列名：通过ResultSetMetaData
					// String columnName = rsmd.getColumnName(i + 1);
					// 获取列的别名:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 通过反射,将对象指定名columnName的属性赋值为指定的columnValue
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
