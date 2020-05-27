package com.tensquare.article.service;

import com.tensquare.article.dao.ArticleDao;
import com.tensquare.article.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 如果是涉及到到数据的增删和该的操作，在spring中的都是需要时的添加事务的
 *
 * @author Administrator
 */
@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新状态
     *
     * @param id
     */
    public void updateState(String id) {
        articleDao.updateState(id);
    }

    public void updateThumbup(String id) {
        articleDao.updateThumbup(id);
    }

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<Article> findAll() {
        //应该也是先做查询的缓冲的中的数据，在去查询到到数据的库，
        return articleDao.findAll();
    }

    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<Article> findSearch(Map whereMap, int page, int size) {
        Specification<Article> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return articleDao.findAll(specification, pageRequest);
    }

    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<Article> findSearch(Map whereMap) {
        Specification<Article> specification = createSpecification(whereMap);
        return articleDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     * 注意这里的redis的是没有起作用的的。请关注一下这个redis的问题
     *
     * @param id
     * @return
     */
    //为甚有的时候redis的不起作用的是因为是的redis中的设置了过期的时间的设置
    public Article findById(String id) {
        //先从缓存中查询数据
        Article article = (Article) redisTemplate.opsForValue().get("article_" + id);
        if (article == null) {
            //缓存中的数据为空的话那就在数据mysql中的查询
            article = articleDao.findById(id).get();
            //在数据查询后放入redis缓存中  并设置过期的时间的timeUnit这样能的一个效果
            redisTemplate.opsForValue().set("article_" + article.getId(), article, 10, TimeUnit.HOURS);
            return article;
        }
        return article;
    }

    /**
     * 增加 然后在添加到缓存中
     *
     * @param article
     */
    public void add(Article article) {
        //先插入到数据库中
        article.setId(idWorker.nextId() + "");
        articleDao.save(article);
        //然后在出插入redis中的 并设置过期的时间是的1小时
        redisTemplate.opsForValue().set("article_" + article.getId(), article, 10, TimeUnit.HOURS);
    }

    /**
     * 修改
     *
     * @param article
     */
    public void update(Article article) {
        //删除原来的缓存信息
        redisTemplate.delete("article_" + article.getId());
        //在更新新的数据的到数据库中
        articleDao.save(article);
        //在将新的数据放入缓存的数据库中
        redisTemplate.opsForValue().set("article_" + article.getId(), article, 10, TimeUnit.HOURS);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        //先删除缓存中的数据
        redisTemplate.delete("article_" + id);
        //然后在删除数据库中的数据
        articleDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<Article> createSpecification(Map searchMap) {
        return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 专栏ID
                if (searchMap.get("columnid") != null && !"".equals(searchMap.get("columnid"))) {
                    predicateList.add(cb.like(root.get("columnid").as(String.class), "%" + (String) searchMap.get("columnid") + "%"));
                }
                // 用户ID
                if (searchMap.get("userid") != null && !"".equals(searchMap.get("userid"))) {
                    predicateList.add(cb.like(root.get("userid").as(String.class), "%" + (String) searchMap.get("userid") + "%"));
                }
                // 标题
                if (searchMap.get("title") != null && !"".equals(searchMap.get("title"))) {
                    predicateList.add(cb.like(root.get("title").as(String.class), "%" + (String) searchMap.get("title") + "%"));
                }
                // 文章正文
                if (searchMap.get("content") != null && !"".equals(searchMap.get("content"))) {
                    predicateList.add(cb.like(root.get("content").as(String.class), "%" + (String) searchMap.get("content") + "%"));
                }
                // 文章封面
                if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                    predicateList.add(cb.like(root.get("image").as(String.class), "%" + (String) searchMap.get("image") + "%"));
                }
                // 是否公开
                if (searchMap.get("ispublic") != null && !"".equals(searchMap.get("ispublic"))) {
                    predicateList.add(cb.like(root.get("ispublic").as(String.class), "%" + (String) searchMap.get("ispublic") + "%"));
                }
                // 是否置顶
                if (searchMap.get("istop") != null && !"".equals(searchMap.get("istop"))) {
                    predicateList.add(cb.like(root.get("istop").as(String.class), "%" + (String) searchMap.get("istop") + "%"));
                }
                // 审核状态
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }
                // 所属频道
                if (searchMap.get("channelid") != null && !"".equals(searchMap.get("channelid"))) {
                    predicateList.add(cb.like(root.get("channelid").as(String.class), "%" + (String) searchMap.get("channelid") + "%"));
                }
                // URL
                if (searchMap.get("url") != null && !"".equals(searchMap.get("url"))) {
                    predicateList.add(cb.like(root.get("url").as(String.class), "%" + (String) searchMap.get("url") + "%"));
                }
                // 类型
                if (searchMap.get("type") != null && !"".equals(searchMap.get("type"))) {
                    predicateList.add(cb.like(root.get("type").as(String.class), "%" + (String) searchMap.get("type") + "%"));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
}
