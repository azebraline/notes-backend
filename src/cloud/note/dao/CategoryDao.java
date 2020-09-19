package cloud.note.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import static cloud.note.config.Constants.DELIMITER;

public class CategoryDao {

    private final DBUtil dbUtil;
    private String sql;
    private ResultSet rs;
    private int count;

    public CategoryDao() {
        this.dbUtil = new DBUtil();
        try {
            dbUtil.myGetConnection();
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
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
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

    public boolean insert(String category) {
        sql = "insert into category (userId,categoryName,categoryCreateTime) values(?,?,?)";
        String userId = category.split(DELIMITER)[1];
        String categoryName = category.split(DELIMITER)[2];
        String categoryCreateTime = category.split(DELIMITER)[3];

        String[] parameters = new String[]{userId, categoryName, categoryCreateTime};
        count = dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count != 0;
    }


    public boolean update(String category) {
        this.sql = "update category set categoryName=?,categoryCreateTime=? where id=?";
        String categoryName = category.split(DELIMITER)[1];
        String categoryCreateTime = category.split(DELIMITER)[2];
        String id = category.split(DELIMITER)[3];

        String[] parameters = new String[]{categoryName, categoryCreateTime, id};
        this.count = this.dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count != 0;
    }

    public boolean delete(String id) {
        String sql = "delete from category where id=?";
        this.count = dbUtil.myExecuteUpdate(sql, new String[]{id.split(DELIMITER)[1]});
        dbUtil.closeAll();
        return count != 0;
    }
}
