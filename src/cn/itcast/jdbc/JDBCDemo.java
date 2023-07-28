package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCDemo {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stat = null;
        try {
            //1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.创建连接对象
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db2", "root", "root");
            //3.定义SQL语句
            String sql = "insert into employee values (null,'季沧海',38)";
            //4.获取执行SQL对象
            stat = conn.createStatement();
            //5.执行SQL
            int count = stat.executeUpdate(sql);
            //6.处理结果
            System.out.println(count);
            if(count > 0){
                System.out.println("添加成功！");
            }else{
                System.out.println("添加失败！");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally{
            if(stat != null){
                try {
                    stat.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
