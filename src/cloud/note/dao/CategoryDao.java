package cloud.note.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CategoryDao {

    private DBUtil dbUtil;
    private String sql;
    private Connection conn;
    private ResultSet rs;
    private int count;

    public CategoryDao() {
        this.dbUtil = new DBUtil();
        int count = 0;
        try {
            Connection conn = dbUtil.myGetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet select() {
        String sql = "select * from category";
        this.rs = this.dbUtil.myExecuteQuery(sql, null);
        return rs;
    }

    public Map<Integer, String> getMap() {
        Map<Integer, String> map = new LinkedHashMap();
        this.rs = new CategoryDao().select();
        try {
            while (this.rs.next()) {
                int id = this.rs.getInt("id");
                String name = this.rs.getString("categoryName");
                map.put(id, name);
            }
            this.rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    public String insert(String category) {
        sql = "insert into category (userId,categoryName,categoryCreateTime) values(?,?,?)";
        String userId = category.split(";")[1];
        String categoryName = category.split(";")[2];
        String categoryCreateTime = category.split(";")[3];

        String[] parameters = new String[]{userId, categoryName, categoryCreateTime};
        count = dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }



    public String update(String catetory) {
        this.sql = "update category set categoryName=?,categoryCreateTime=? where id=?";
        String categoryName = catetory.split(";")[1];
        String categoryCreateTime = catetory.split(";")[2];
        String id = catetory.split(";")[3];

        String[] parameters = new String[]{categoryName, categoryCreateTime, id};
        this.count = this.dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }

    public String delete(String id) {
        String sql = "delete from category where id=?";
        this.count = dbUtil.myExecuteUpdate(sql, new String[]{id.split(";")[1]});
        dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }
}
