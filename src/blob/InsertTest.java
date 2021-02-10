package blob;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import util.JDBCutil;

/**
 * 使用preparedStratement	实现批量数据操作
 * updata，delete本身就具有批量操作的效果
 * 主要为插入操作
 * @author yf
 *
 */
public class InsertTest {
	//使用preparedStatement
	@Test
	public void testInsert1(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Long start = System.currentTimeMillis();
			conn = JDBCutil.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < 20000; i++) {
				ps.setObject(1, "name_" + i);
				ps.execute();
			}
			Long end = System.currentTimeMillis();
			System.out.println("花费的时间为：" + (end - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(conn, ps);
		}
	}
	/**
	 * 批量操作方法二：
	 * 1.addBatch( )、executeBatch().clearBatch()
	 * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
	 * ?rewriteBatchedStatements=true 写在配置文件的url后面
	 * 3.使用更新的mysql 驱动:mysql-connector-java-5.1.37-bin.jarl
	 */
	@Test
	public void testInsert2() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Long start = System.currentTimeMillis();
			conn = JDBCutil.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 1; i <= 20000; i++) {
				ps.setObject(1, "name_" + i);
				//"攒"sql
				ps.addBatch();
				if(i % 500 == 0) {
					//执行
					ps.executeBatch();
					//清空
					ps.clearBatch();
				}
			}
			Long end = System.currentTimeMillis();
			System.out.println("花费的时间为：" + (end - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(conn, ps);
		}
	}
	/**
	 * 设置连接不允许自动提交
	 */
	@Test
	public void testInsert3() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutil.getConnection();
			//设置不允许自动提交数据
			conn.setAutoCommit(false);
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 1; i <= 1000000; i++) {
				ps.setObject(1, "name_" + i);
				//"攒"sql
				ps.addBatch();
				if(i % 500 == 0) {
					//执行
					ps.executeBatch();
					//清空
					ps.clearBatch();
				}
			}
			conn.commit();
			long end = System.currentTimeMillis();
			System.out.println("花费的时间为：" + (end - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(conn, ps);
		}
	}
}
