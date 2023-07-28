package cn.itcast.jdbctemplate;

import cn.itcast.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("all")
public class JdbcTemplateDemo1 {
    public static void main(String[] args) {
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        template.update("delete from stu where id = ?",5);
        template.update("insert into stu (id,name,sex) values (?,?,?)",11,"zhangmin","女");


    }
}
