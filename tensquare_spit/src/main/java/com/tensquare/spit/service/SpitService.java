package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //如果当前添加的吐槽，有父节点，那么父节点的吐槽回复数要加一
        if (!StringUtils.isEmpty(spit.getParentid())){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "com/tensquare/spit");
        }
        spitDao.save(spit);
    }

    public void update(Spit spit){
        spitDao.save(spit);
    }

    public void delete(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentId(String parentid, int page, int size){
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return spitDao.findByParentid(parentid, pageRequest);
    }

    public void thumbup(String spitId) {
        //方式一：效率有问题
        //Spit com.tensquare.spit.spit = spitDao.findById(spitId).get();
        //Integer thumbup = com.tensquare.spit.spit.getThumbup() == null?0:com.tensquare.spit.spit.getThumbup();
        //com.tensquare.spit.spit.setThumbup(thumbup+1);
        //spitDao.save(com.tensquare.spit.spit);

        //方式二：使用原生mongo命令来实现自增 db.com.tensquare.spit.spit.updata({"_id":"1"},{$inc:{thumbup:NumberInt(1)}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("1"));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "com/tensquare/spit");
    }
}
