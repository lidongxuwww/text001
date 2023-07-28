package cn.itcast.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

public class JdbcUtil {
    private static String user;
    private static String password;
    private static String url;
    private static String driver;

    /**
     * 文件的读取，只需要读取一次即可拿这些值、用静态代码块实现
     */

    static {
        try {
            Properties pro = new Properties();

            //类加载器
            ClassLoader cls = JdbcUtil.class.getClassLoader();
            URL res = cls.getResource("jdbc.properties");
            String path = res.getPath();
            pro.load(new FileReader(path));

            url = pro.getProperty("url");
            user = pro.getProperty("user");
            password = pro.getProperty("password");
            driver = pro.getProperty("driver");

            //注册驱动
            Class.forName(driver);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void close(Statement stat, Connection conn){
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
    public static void close(ResultSet rs, Statement stat, Connection conn){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
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
