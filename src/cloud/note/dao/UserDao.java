package cloud.note.dao;

import java.sql.ResultSet;

public class UserDao {
    private final DBUtil dbUtil;
    private int count;

    public UserDao() {
        this.dbUtil = new DBUtil();
        this.count = 0;
        try {
            dbUtil.myGetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int login(String username, String password) {

        String sql = "select id from user  where username=? and password=?";
        try {
            ResultSet rs = this.dbUtil.myExecuteQuery(sql, new String[]{username, password});
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean sign(String username, String password) {
        String sql = "insert into user (username,password) values(?,?)";// 添加
        String[] parameters = new String[]{username, password};
        this.count = dbUtil.myExecuteUpdate(sql, parameters);
        this.dbUtil.closeAll();
        return count != 0;
    }
}
