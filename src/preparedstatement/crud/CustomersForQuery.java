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
 * �����Customers��Ĳ�ѯ����
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
	 * �����customers���ͨ�ò�ѯ����
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
			// ��ȡ�������Ԫ���ݣ�ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// ͨ��ResultSetMetaData��ȡ���������
			int columnCount = rsmd.getColumnCount();
			if (rs.next()) {
				Customer cust = new Customer();
				// ����һ�н������ÿһ��
				for (int i = 0; i < columnCount; i++) {
					// ��ȡ��ֵ
					Object columnValue = rs.getObject(i + 1);
					// ��ȡÿ���е�����
					String columnName = rsmd.getColumnName(i + 1);
					// ��cust ����ָ����ĳ�����Ը�ֵΪcolumnValue
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
			// ִ��,�����ؽ����
			resultSet = ps.executeQuery();
			// ��������
			if (resultSet.next()) {// �жϽ������һ���Ƿ������ݣ�����з���true����ָ�����ƣ���������
				// ��ȡ��ǰ�������ݵĸ����ֶ�ֵ
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String email = resultSet.getString(3);
				Date birth = resultSet.getDate(4);
				// ��ʽһ
				// System.out.println(....);
				// ��ʽ��
				// Object[] data = new Object[] {id, name, email, birth};
				// ��ʽ��:�����ݷ�װΪ����
				Customer customer = new Customer(id, name, email, birth);
				System.out.println(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// �ر���Դ
			JDBCutil.closeResource(conn, ps, resultSet);
		}
	}
}
