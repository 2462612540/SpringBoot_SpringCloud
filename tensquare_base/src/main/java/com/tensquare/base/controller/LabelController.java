package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CrossOrigin用于实现的跨域的访问的一个权限
 */
@RestController
@CrossOrigin
@RequestMapping("/label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 标签全部列表
     *
     * @return
     */
    @GetMapping()
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", labelService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param labelId
     * @return
     */
    @GetMapping("/{labelId}")
    public Result findById(@PathVariable("labelId") String labelId) {
        System.out.println(labelId);
        return new Result(true, StatusCode.OK, "查询成功", labelService.findById(labelId));
    }

    /**
     * 增加标签
     *
     * @param label
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Label label) {
        labelService.save(label);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    /**
     * 修改标签
     *
     * @param labelId
     * @param label
     * @return
     */
    @PutMapping("/{labelId}")
    public Result update(@PathVariable("labelId") String labelId, @RequestBody Label label) {
        label.setId(labelId);
        labelService.update(label);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 根据ID删除
     *
     * @param labelId
     * @return
     */
    @DeleteMapping("/{labelId}")
    public Result deleteById(@PathVariable("labelId") String labelId) {
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 标签分页
     *
     * @param label
     * @return
     */
    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label) {
        List<Label> labelList = labelService.findSearch(label);
        return new Result(true, StatusCode.OK, "查看分页成功", labelList);
    }

    /**
     * 标签分页
     *
     * @param label
     * @return
     */
    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@PathVariable int page,
                            @PathVariable int size,
                            @RequestBody Label label) {
        Page<Label> pageData = labelService.pageQuery(label, page, size);
        PageResult<Label> labelPageResult = new PageResult<>(pageData.getTotalElements(), pageData.getContent());
        return new Result(true, StatusCode.OK, "查看分页成功", labelPageResult);
    }
}
