package cn.tanlw.flashbuy.util;

import cn.tanlw.flashbuy.domain.FlashbuyUser;
import cn.tanlw.flashbuy.redis.FlashbuyUserKey;
import cn.tanlw.flashbuy.redis.RedisService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserUtil {

    private static final boolean CREATE_USERS = false;
    private static final boolean INSERT_DB = false;
    private static final boolean INSERT_REIDS = true;
    private static final boolean INSERT_REDIS_BY_HTTP = false;

    private static void createUser(int count) throws Exception {
        List<FlashbuyUser> users = new ArrayList<>();

        File file = new File("C:/a/tokens.txt");
        if (CREATE_USERS) {
            users = createFlashbuyUsers(count);
        } else {
            queryUsers(users);
        }
        if (INSERT_DB) {
            insertDB(users);
        }
        if (INSERT_REIDS) {
            Jedis writeJedisObject = RedisUtil.getWriteJedisObject();


            StringBuilder tokensContent = new StringBuilder();
            for (int i = 0; i < users.size(); i++) {
                String uuid = UUIDUtil.uuid();
                writeJedisObject.set(FlashbuyUserKey.AUTH_TOKEN.getPrefix() + uuid, RedisService.beanToString(users.get(i)));
                tokensContent.append(users.get(i).getId() + "," + uuid);
                tokensContent.append("\r\n");
            }
            if (file.exists()) {
                file.delete();
            }
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(file, "rw");
                file.createNewFile();
                raf.seek(0);
                raf.seek(raf.length());
                raf.write(tokensContent.toString().getBytes());
                raf.write("\r\n".getBytes());
            } finally {
                if (raf != null) {
                    raf.close();
                }
            }
        }
        if (INSERT_REDIS_BY_HTTP) {
            insertRedisByHttp(users, file);
        }


        System.out.println("over");
    }

    private static void queryUsers(List<FlashbuyUser> users) throws Exception {
        Connection conn = null;
        try {
            conn = DBUtil.getConn();
            String sql = "select * from flashbuy_user";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String nickname = resultSet.getString("nickname");
                FlashbuyUser user = new FlashbuyUser();
                user.setId(id);
                user.setNickname(nickname);
                users.add(user);
            }
            preparedStatement.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static void insertRedisByHttp(List<FlashbuyUser> users, File file) throws IOException {
        //登录，生成token
        String urlString = "http://localhost:8077/login/do_login";
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for (int i = 0; i < users.size(); i++) {
            FlashbuyUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getId());

            String row = user.getId() + "," + token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();
    }

    private static void insertDB(List<FlashbuyUser> users) throws Exception {
        //		//插入数据库
        Connection conn = DBUtil.getConn();
        String sql = "insert into flashbuy_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            FlashbuyUser user = users.get(i);
            pstmt.setInt(1, user.getLoginCount());
            pstmt.setString(2, user.getNickname());
            pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setString(4, user.getSalt());
            pstmt.setString(5, user.getPassword());
            pstmt.setLong(6, user.getId());
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db");
    }

    private static List<FlashbuyUser> createFlashbuyUsers(int count) {
        List<FlashbuyUser> users = new ArrayList<FlashbuyUser>(count);
        //生成用户
        for (int i = 0; i < count; i++) {
            FlashbuyUser user = new FlashbuyUser();
            user.setId(13000000000L + i);
            user.setLoginCount(1);
            user.setNickname("user" + i);
            user.setRegisterDate(new Date());
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDbPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.println("create user");
        return users;
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
