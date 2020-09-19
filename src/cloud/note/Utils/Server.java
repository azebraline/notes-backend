package cloud.note.Utils;

import cloud.note.dao.ArticleDao;
import cloud.note.dao.CategoryDao;
import cloud.note.dao.UserDao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 该类为多线程类，用于服务端
 */
public class Server implements Runnable {

    private Socket client = null;

    public Server(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            //获取Socket的输出流，用来向客户端发送数据
            boolean f = true;
            PrintStream out = new PrintStream(client.getOutputStream());
            //获取Socket的输入流，用来接收从客户端发送过来的数据
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean flag = true;
            while (flag) {
                //接收从客户端发送过来的数据
                String str = buf.readLine();
                if (str == null || "".equals(str)) {
                    flag = false;
                } else {
                    System.out.println(str);
                    if (str.startsWith("login")) {
                        String username = str.split(";")[1];
                        String password = str.split(";")[2];
                        int id = new UserDao().login(username, password);
                        out.println(id);
                    } else if (str.startsWith("reg")) {
                            String res = new UserDao().sign(str.split(";")[1], str.split(";")[2]);
                            out.println(res);
                    } else if (str.startsWith("ArticleSelect")) {
                        Map<Integer, String> map = new LinkedHashMap();
                        int num = 1;
                        String artData = "";
                        ResultSet article = new ArticleDao().select();
                        map = new CategoryDao().getMap();
                        while (article.next()) {
                            String artId = article.getString("articleId");
                            String userId = article.getString("userId");
                            String catId = article.getString("categoryId");
                            String index = String.valueOf(num);
                            String artName = article.getString("titleName");
                            String catName = map.get(Integer.parseInt(catId));
                            String content = article.getString("articleContent");
                            String time = article.getString("createTime");
                            System.out.println("#######文章列表######\n"+str);
                            System.out.println(userId.equals(str.split(";")[1]));
                            if (userId.equals(str.split(";")[1])) {
                                artData += artId + ";" + userId + ";" + catId + ";" + index + ";" + artName + ";" + catName + ";" + content + ";" + time + "10010";
                                num++;
                            }
                        }

                        System.out.println(artData);
                        out.println(artData);
                        article.close();
                    } else if (str.startsWith("category")) {
                        String res = "";
                        int num = 1;
                        ResultSet rs = new CategoryDao().select();
                        while (rs.next()) {
                            String id = String.valueOf(rs.getInt("id"));
                            String userId = String.valueOf(rs.getInt("userId"));
                            String index = String.valueOf(num);
                            String name = rs.getString("categoryName");
                            String time = rs.getString("categoryCreateTime");
                            if (userId.equals(str.split(";")[1])) {
                                res += id + ";" + userId + ";" + index + ";" + name + ";" + time + "10010";
                                num++;
                            }
                        }
                        rs.close();
                        out.println(res);

                    } else if (str.startsWith("getmap")) {
                        String res = "";
                        Map<Integer, String> map = new LinkedHashMap();
                        map = new CategoryDao().getMap();
                        for(Integer key : map.keySet()){
                            String value = map.get(key);
                            res += key+";"+value+"10010";
                        }
                        out.println(res);
                    } else if (str.startsWith("ArticleInsert")){
                        out.println(new ArticleDao().insert(str));
                    } else if (str.startsWith("ArticleUpdate")){
                        out.println(new ArticleDao().update(str));
                    } else if (str.startsWith("ArticleDelete")){
                        out.println(new ArticleDao().delete(str.split(";")[1]));
                    } else if (str.startsWith("CatInsert")){
                        out.println(new CategoryDao().insert(str));
                    } else if (str.startsWith("CatUpdate")){
                        out.println(new CategoryDao().update(str));
                    } else if (str.startsWith("CatDelete")){
                        out.println(new CategoryDao().delete(str));
                    }
                }
            }
            out.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}