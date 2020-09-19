package cloud.note.dao;

import java.sql.Connection;
import java.sql.ResultSet;

public class UserDao {
    private DBUtil dbUtil;
    private Connection conn;
    private String sql;
    private int count;
    private ResultSet rs;

    public UserDao(){
        this.dbUtil = new DBUtil();
        this.count = 0;
        try {
            this.conn = dbUtil.myGetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int login(String username, String password) {

        String sql = "select id from user  where username=? and password=?";
        try {
            this.rs = this.dbUtil.myExecuteQuery(sql, new String[]{username, password});
            if (this.rs.next())
                return this.rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String sign(String username, String password){
        this.sql = "insert into user (username,password) values(?,?)";// 添加
        String[] parameters = new String[]{username,password};
        this.count = dbUtil.myExecuteUpdate(sql, parameters);
        this.dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }
}
