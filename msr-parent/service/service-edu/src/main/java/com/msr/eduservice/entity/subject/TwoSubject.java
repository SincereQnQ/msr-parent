package com.msr.eduservice.entity.subject;

import lombok.Data;


@Data
public class TwoSubject {
    private String id;
    private String title;
    // 一个一级分类对应多个二级分类

}
