package com.codeagles.vo;

import lombok.Data;

import java.util.*;

/**
 * 二级分类VO
 *
 * @author hcn
 * @create 2020-01-09 9:30
 **/
@Data
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private String fatherId;

    //三级分类VO类 list
    private List<SubCategoryVO> subCatList;
}
