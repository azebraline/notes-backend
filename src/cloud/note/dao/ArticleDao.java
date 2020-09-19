package cloud.note.dao;

import java.sql.ResultSet;

import static cloud.note.config.Constants.DELIMITER;


public class ArticleDao {

    private final DBUtil dbUtil;
    private String sql;
    private int count;

    public ArticleDao() {
        this.dbUtil = new DBUtil();
        this.count = 0;
        try {
            dbUtil.myGetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet select() {
        this.sql = "select * from article";
        return this.dbUtil.myExecuteQuery(sql, null);
    }

    public String insert(String art) {
        this.sql = "insert into article (userId,categoryId,titleName,articleContent,createTime) values(?,?,?,?,?)";// 添加
        String userId = art.split(DELIMITER)[1];
        String categoryId = art.split(DELIMITER)[2];
        String titleName = art.split(DELIMITER)[3];
        String articleContent = art.split(DELIMITER)[4];
        String createTime = art.split(DELIMITER)[5];

        String[] parameters = new String[]{userId, categoryId, titleName, articleContent, createTime};
        count = dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count == 0 ? "false" : "true";
    }

    public boolean update(String art) {
        this.sql = "update article set userId=?,categoryId=?,titleName=?,articleContent=?,createTime=? where articleId=?";// 添加
        String userId = art.split(DELIMITER)[1];
        String categoryId = art.split(DELIMITER)[2];
        String titleName = art.split(DELIMITER)[3];
        String articleContent = art.split(DELIMITER)[4];
        String createTime = art.split(DELIMITER)[5];
        String id = art.split(DELIMITER)[6];

        String[] parameters = new String[]{userId, categoryId, titleName, articleContent, createTime, id};
        this.count = this.dbUtil.myExecuteUpdate(this.sql, parameters);
        dbUtil.closeAll();
        return count != 0;
    }

    public boolean delete(String id) {
        String sql = "delete from article WHERE articleId=?";
        this.count = dbUtil.myExecuteUpdate(sql, new String[]{id});
        dbUtil.closeAll();
        return count != 0;
    }
}
