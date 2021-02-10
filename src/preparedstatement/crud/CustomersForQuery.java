package preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import bean.Customer;
import util.JDBCutil;

/**
 * 针对于Customers表的查询操作
 * 
 * @author yf
 *
 */
public class CustomersForQuery {
	@Test
	public void testQueryForCustomers() {
		String sql = "select id,name,email,birth from customers where id = ?";
		Customer customer = queryForCustomers(sql, 2);
		System.out.println(customer);
	}

	/**
	 * 针对于customers表的通用查询操作
	 * 
	 * @throws Exception
	 */
	public Customer queryForCustomers(String sql, Object... args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCutil.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			// 获取结果集的元数据：ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// 通过ResultSetMetaData获取结果集列数
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				Customer cust = new Customer();
				// 处理一行结果集的每一列
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columnValue = rs.getObject(i + 1);
					// 获取每个列的列名
					String columnName = rsmd.getColumnName(i + 1);
					// 给cust 对象指定的某个属性赋值为columnValue
					Field field = Customer.class.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(cust, columnValue);
				}
				return cust;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutil.closeResource(conn, ps, rs);
		}
		return null;
	}

	@Test
	public void testQuery1() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		try {
			conn = JDBCutil.getConnection();
			String sql = "select id,name,email,birth from customers where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 2);
			// 执行,并返回结果集
			resultSet = ps.executeQuery();
			// 处理结果集
			if (resultSet.next()) {// 判断结果的下一条是否有数据，如果有返回true，并指针下移；否则不下移
				// 获取当前这条数据的各个字段值
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);
				// 方式一
				// System.out.println(....);
				// 方式二
				// Object[] data = new Object[] {id, name, email, birth};
				// 方式三:将数据封装为对象
				Customer customer = new Customer(id, name, email, birth);
				System.out.println(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 关闭资源
			JDBCutil.closeResource(conn, ps, resultSet);
		}
	}
}
