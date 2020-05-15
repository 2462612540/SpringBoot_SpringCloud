package com.tensquare.article.dao;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ArticleDao extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tb_article SET state=1 WHERE id=?1", nativeQuery = true)
    public void updateState(String id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE tb_article SET thumbup = thumbup+1 WHERE id=?1", nativeQuery = true)
    public void updateThumbup(String id);
}
