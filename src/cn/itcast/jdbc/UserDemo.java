package cn.itcast.jdbc;

import cn.itcast.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDemo {
    public static void main(String[] args) {
        List<User> users = new UserDemo().userTables();
        for (User user : users) {
            int id = user.getId();
            String username = user.getUsername();
            String password = user.getPassword();
            System.out.println(id + " , " + username + " , " + password);
        }
    }

    public List<User> userTables() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        List<User> list = null;
        try {
            conn = JdbcUtil.getConnection();
            String sql = "select * from user";
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            User user = null;
            list = new ArrayList<User>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");

                user = new User();
                user.setId(id);
                user.setUsername(username);
                user.setPassword(password);

                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtil.close(rs, stat, conn);
        }

        return list;
    }
}
