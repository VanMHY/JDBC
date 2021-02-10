package blob;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import util.JDBCutil;

/**
 * ʹ��preparedStratement	ʵ���������ݲ���
 * updata��delete����;�������������Ч��
 * ��ҪΪ�������
 * @author yf
 *
 */
public class InsertTest {
	//ʹ��preparedStatement
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
			System.out.println("���ѵ�ʱ��Ϊ��" + (end - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(conn, ps);
		}
	}
	/**
	 * ����������������
	 * 1.addBatch( )��executeBatch().clearBatch()
	 * 2.mysql������Ĭ���ǹر�������ģ�������Ҫͨ��һ����������mysql�����������֧�֡�
	 * ?rewriteBatchedStatements=true д�������ļ���url����
	 * 3.ʹ�ø��µ�mysql ����:mysql-connector-java-5.1.37-bin.jarl
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
				//"��"sql
				ps.addBatch();
				if(i % 500 == 0) {
					//ִ��
					ps.executeBatch();
					//���
					ps.clearBatch();
				}
			}
			Long end = System.currentTimeMillis();
			System.out.println("���ѵ�ʱ��Ϊ��" + (end - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(conn, ps);
		}
	}
	/**
	 * �������Ӳ������Զ��ύ
	 */
	@Test
	public void testInsert3() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			long start = System.currentTimeMillis();
			conn = JDBCutil.getConnection();
			//���ò������Զ��ύ����
			conn.setAutoCommit(false);
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 1; i <= 1000000; i++) {
				ps.setObject(1, "name_" + i);
				//"��"sql
				ps.addBatch();
				if(i % 500 == 0) {
					//ִ��
					ps.executeBatch();
					//���
					ps.clearBatch();
				}
			}
			conn.commit();
			long end = System.currentTimeMillis();
			System.out.println("���ѵ�ʱ��Ϊ��" + (end - start));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			JDBCutil.closeResource(conn, ps);
		}
	}
}
