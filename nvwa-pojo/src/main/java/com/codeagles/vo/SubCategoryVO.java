package com.codeagles.vo;

import lombok.Data;

/**
 * 三级分类VO类
 *
 * @author hcn
 * @create 2020-01-09 9:32
 **/
@Data
public class SubCategoryVO {

    private Integer subId;
    private String subName;
    private String subType;
    private String subFatherId;
}
