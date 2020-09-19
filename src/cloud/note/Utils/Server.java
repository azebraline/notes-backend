package cloud.note.Utils;

import cloud.note.dao.ArticleDao;
import cloud.note.dao.CategoryDao;
import cloud.note.dao.UserDao;
import com.mysql.cj.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import static cloud.note.config.Constants.DELIMITER;
import static cloud.note.config.Constants.END;

/**
 * 该类为多线程类，用于服务端
 */
public class Server implements Runnable {

    private final Socket client;
    private final UserDao userDao = new UserDao();
    private final CategoryDao CategoryDao = new CategoryDao();

    public Server(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            //获取Socket的输出流，用来向客户端发送数据
            PrintStream out = new PrintStream(client.getOutputStream());
            //获取Socket的输入流，用来接收从客户端发送过来的数据
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (true) {
                //接收从客户端发送过来的数据
                String str = buf.readLine();
                if (str.isEmpty()) {
                    break;
                }
                System.out.println(str);
                String[] parsedStr = str.split(DELIMITER);
                String ids;
                StringBuilder res;
                int num, id;
                switch (parsedStr[0]) {
                    case "login":
                        out.println(this.userDao.login(parsedStr[1], parsedStr[2]));
                        break;
                    case "reg":
                        out.println(this.userDao.sign(parsedStr[1], parsedStr[2]));
                        break;
                    case "ArticleSelect":
                        num = 1;
                        StringBuilder artData = new StringBuilder();
                        ResultSet article = new ArticleDao().select();
                        Map<Integer, String> map = this.CategoryDao.getMap();
                        while (article.next()) {
                            String artId = article.getString("articleId");
                            String userId = article.getString("userId");
                            String catId = article.getString("categoryId");
                            String index = String.valueOf(num);
                            String artName = article.getString("titleName");
                            String catName = map.get(Integer.parseInt(catId));
                            String content = article.getString("articleContent");
                            String time = article.getString("createTime");

                            if (userId.equals(parsedStr[1])) {
                                artData.append(artId).append(DELIMITER).append(userId).append(DELIMITER).append(catId).append(DELIMITER).append(index).append(DELIMITER).append(artName).append(DELIMITER).append(catName).append(DELIMITER).append(content).append(DELIMITER).append(time).append(END);
                                num++;
                            }
                        }
                        System.out.println(artData);
                        out.println(artData);
                        article.close();
                        break;
                    case "category":
                        res = new StringBuilder();
                        num = 1;
                        ResultSet rs = this.CategoryDao.select();
                        while (rs.next()) {
                            ids = String.valueOf(rs.getInt("id"));
                            String userId = String.valueOf(rs.getInt("userId"));
                            String index = String.valueOf(num);
                            String name = rs.getString("categoryName");
                            String time = rs.getString("categoryCreateTime");
                            if (userId.equals(str.split(DELIMITER)[1])) {
                                res.append(ids).append(DELIMITER).append(userId).append(DELIMITER).append(index).append(DELIMITER).append(name).append(DELIMITER).append(time).append(END);
                                num++;
                            }
                        }
                        rs.close();
                        out.println(res);
                        break;
                    case "getMap":
                        res = new StringBuilder();
                        Map<Integer, String> catMap = new CategoryDao().getMap();
                        for (Integer key : catMap.keySet()) {
                            String value = catMap.get(key);
                            res.append(key).append(DELIMITER).append(value).append(END);
                        }
                        out.println(res);
                        break;
                    case "ArticleInsert":
                        out.println(new ArticleDao().insert(str));
                        break;
                    case "ArticleUpdate":
                        out.println(new ArticleDao().update(str));
                        break;
                    case "ArticleDelete":
                        out.println(new ArticleDao().delete(parsedStr[1]));
                        break;
                    case "CatInsert":
                        out.println(new CategoryDao().insert(str));
                        break;
                    case "CatUpdate":
                        out.println(new CategoryDao().update(str));
                        break;
                    case "CatDelete":
                        out.println(new CategoryDao().delete(str));
                        break;
                    default:
                        break;
                }
            }
            out.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}