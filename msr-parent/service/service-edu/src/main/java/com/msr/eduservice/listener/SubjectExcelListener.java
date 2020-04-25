package com.msr.eduservice.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.msr.eduservice.entity.EduSubject;
import com.msr.eduservice.entity.excel.SubjectData;
import com.msr.eduservice.service.EduSubjectService;
import com.msr.servicebase.exception.MSRException;

/**
 *  excel读取监听器
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    // 因为SubjectExcelListener不交给spring进行管理,需要自己new不能注入其他对象
    // 不能实现数据库操作
    public EduSubjectService subjectService;
    // 空参构造器
    public SubjectExcelListener(){ }
    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService = subjectService;
    }
    // 对一级分类进行判断,若重复,去除,不进行重复添加.
    private EduSubject existOneSubject(EduSubjectService subjectService, String name){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject oneSubject = subjectService.getOne(wrapper);
        return oneSubject;
    }
    // 对二级分类进行判断,若重复,去除,不进行重复添加.
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject twoSubject = subjectService.getOne(wrapper);
        return twoSubject;
    }

    /**
     *  读取Excel内容 一行一行进行读取
     * @param subjectData
     * @param analysisContext
     */
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null){
            throw new MSRException(20001,"文件数据为空");
        }
        // 一行一行进行读取,每次读取两个值,第一个值为一级分类,第二个值为耳机分类
        // 判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(subjectService,subjectData.getOneSubjectName());
        if (existOneSubject == null){
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName()); // 一级分类名称
            subjectService.save(existOneSubject);
        }
        // 获取一级分类id值
        String pid = existOneSubject.getId();
        // 添加二级分类
        // 判断耳机分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectService,subjectData.getTwoSubjectName(),pid);
        if (existTwoSubject == null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName()); // 二级分类名称
            subjectService.save(existTwoSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
