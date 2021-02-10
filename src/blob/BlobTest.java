package blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import bean.Customer;
import util.JDBCutil;

/**
 * 测试使用PreparedStatement操作Blob类型
 * @author yf
 *
 */
public class BlobTest {
	//向customers表中添加Blob类型数据
	@Test
	public void testInsert() throws Exception {
		Connection conn = JDBCutil.getConnection();
		String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setObject(1, "张宇豪");
		ps.setObject(2,"zhang@qq.com");
		ps.setObject(3, "1999-2-3");
		FileInputStream is = new FileInputStream(new File("1.jpg"));
		ps.setBlob(4, is);
		ps.execute();
		JDBCutil.closeResource(conn, ps);
	}
	//向customers表中查询Blob类型数据
	@Test
	public void testQuery(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			conn = JDBCutil.getConnection();
			//String sql = "select id,name,email,birth,photo from customers where id = ?";
			String sql = "";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, 3);
			rs = ps.executeQuery(sql);
			if(rs.next()) {
				//方式一：索引
//			int id = rs.getInt(1);
//			String name = rs.getString(2);
//			String email = rs.getString(3);
//			Date brith = rs.getDate(4);
				//方式二：别名
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date birth = rs.getDate("birth");
				Customer cust = new Customer(id, name, email, birth);
				System.out.println(cust);
				//将Blob文件下载下来并保存在本地
				Blob photo = rs.getBlob("photo");
				is = photo.getBinaryStream();
				fos = new FileOutputStream(new File("2.jpg"));
				byte[] buffer = new byte[1024];
				int len;
				while((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fos != null)
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(is != null)
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JDBCutil.closeResource(conn, ps, rs);
		}
		
	}
}
