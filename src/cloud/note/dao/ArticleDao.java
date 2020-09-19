package cloud.note.dao;

import java.sql.Connection;
import java.sql.ResultSet;


public class ArticleDao {

    private DBUtil dbUtil;
    private Connection conn;
    private String sql;
    private int count;
    private ResultSet rs;

    public ArticleDao() {
        this.dbUtil = new DBUtil();
        this.count = 0;
        try {
            this.conn = dbUtil.myGetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet select() {
        this.sql = "select * from article";
        this.rs = this.dbUtil.myExecuteQuery(sql, null);
        return rs;
    }

    public String insert(String art) {
        this.sql = "insert into article (userId,categoryId,titleName,articleContent,createTime) values(?,?,?,?,?)";// 添加
        String userId = art.split(";")[1];
        String categoryId = art.split(";")[2];
        String titleName = art.split(";")[3];
        String articleContent = art.split(";")[4];
        String createTime = art.split(";")[5];

        String[] parameters = new String[]{userId, categoryId, titleName, articleContent, createTime};
        count = dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();

        return count == 0 ? "false" : "true";
    }

    public String update(String art) {

        this.sql = "update article set userId=?,categoryId=?,titleName=?,articleContent=?,createTime=? where articleId=?";// 添加
        String userId = art.split(";")[1];
        String categoryId = art.split(";")[2];
        String titleName = art.split(";")[3];
        String articleContent = art.split(";")[4];
        String createTime = art.split(";")[5];
        String id = art.split(";")[6];

        String[] parameters = new String[]{userId, categoryId, titleName, articleContent, createTime, id};
        this.count = this.dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }

    public String delete(String id) {
        String sql = "delete from article WHERE articleId=?";
        this.count = dbUtil.myExecuteUpdate(sql, new String[]{id});
        dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }
}
