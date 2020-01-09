package com.codeagles.vo;

import lombok.Data;

import java.util.List;

/**
 * 首页商品图VO
 *
 * @author hcn
 * @create 2020-01-09 9:30
 **/
@Data
public class NewItemsVO {


    private Integer id;
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;

    //三级分类VO类 list
    private List<SimpleItemVO> simpleItemList;
}
