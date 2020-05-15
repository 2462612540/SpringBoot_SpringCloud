package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

@Document(indexName = "tensquare_article",type = "article")
public class Article implements Serializable {

    @Id
    private String id;
    //是否索引 就是在看这个东西能不能被搜索到
    //是否分词 就是在看搜索的时候是整体匹配还是单词匹配
    //是否存储 就是查看页面上面是否显示
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;

    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String content;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
