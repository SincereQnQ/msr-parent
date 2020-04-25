package com.msr.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msr.commonutils.R;
import com.msr.eduservice.entity.EduTeacher;
import com.msr.eduservice.query.TeacherQuery;
import com.msr.eduservice.service.EduTeacherService;
import com.msr.servicebase.exception.MSRException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author msr
 * @since 2020-04-13
 */
@CrossOrigin // 跨域
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    /**
     *  查询所有讲师
     * @return
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public R list(){

        // 自定义异常测试
//        int a = 10/0;
//        System.out.println(a);
//        try {
//            int a = 10/0;
//        }catch(Exception e) {
//            throw new MSRException(20002,"出现自定义异常");
//        }

        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }
    /**
     *  根据ID删除讲师
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){
        teacherService.removeById(id);
        return R.ok();
    }

    /**
     *  分页和条件查询讲师
     * @param page
     * @param limit
     * @param teacherQuery
     * @return
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "teacherQuery", value = "查询对象", required = false)
                    TeacherQuery teacherQuery){

        Page<EduTeacher> pageParam = new Page<>(page, limit);

        teacherService.pageQuery(pageParam, teacherQuery);
        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }

    /**
     *  添加讲师信息
     * @param teacher
     * @return
     */
    @ApiOperation(value = "新增讲师")
    @PostMapping("save")
    public R save(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){

        teacherService.save(teacher);
        return R.ok();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){

        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("item", teacher);
    }
    /**
     * 根据ID修改
     * @param id
     * @return
     */
    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id,

            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher){

        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok();
    }
}

