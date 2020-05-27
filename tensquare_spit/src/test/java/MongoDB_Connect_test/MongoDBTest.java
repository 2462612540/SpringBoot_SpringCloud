/**
 * Copyright (C), 2018-2020
 * FileName: MongoDBTest
 * Author:   xjl
 * Date:     2020/5/27 12:57
 * Description: 测试连接
 */
package MongoDB_Connect_test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBTest {
    public static void main(String[] args) {
        //连接数据服务器
        MongoClient mongoClient = new MongoClient("192.168.25.132");
        //连接凑在哦的数据库
        MongoDatabase spitdb = mongoClient.getDatabase("spitdb");
        //得到操作的集合
        MongoCollection<Document> spit = spitdb.getCollection("spit");
        //得到集合中的文档数据
        FindIterable<Document> documents = spit.find();
        //遍历数据
        for (Document document : documents) {
            System.out.println("内容" + document.getString("content"));
            System.out.println("ID" + document.getString("userid"));
        }
        //关闭连接
        mongoClient.close();
    }
}
