package preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import bean.Order;
import util.JDBCutil;

/**
 * 针对于order表的查询操作
 * @author yf
 *
 */
public class OrderForQuery {
	@Test
	public void testOrderForQuery() {
		String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
		Order order = orderForQuery(sql,1 );
		System.out.println(order);
	}
	public Order orderForQuery(String sql, Object... args) {
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
				Order order = new Order();
				for (int i = 0; i < columnCount; i++) {
					// 获取每个列的列值:通过ResultSet
					Object columnValue = rs.getObject(i + 1);
					// 获取每个列的列名：通过ResultSetMetaData
					//String columnName = rsmd.getColumnName(i + 1);
					//获取列的别名:getColumnLabel
					String columnLabel = rsmd.getColumnLabel(i + 1);
					
					// 通过反射,将对象指定名columnName的属性赋值为指定的columnValue
					Field field = Order.class.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(order, columnValue);
				}
				return order;
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
	public void testQuery1(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCutil.getConnection();
			String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			rs = ps.executeQuery();
			if(rs.next()) {
				int id = (int)rs.getObject(1);
				String name = (String)rs.getObject(2);
				Date date = (Date)rs.getObject(3);
				Order order = new Order(id, name, date);
				System.out.println(order);
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCutil.closeResource(conn, ps, rs);
		}
	}
}
