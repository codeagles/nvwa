package com.codeagles.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用于商品评价的VO
 *
 * @author hcn
 * @create 2020-01-10 9:25
 **/
@Data
public class ItemCommentVO {

    private Integer commentLevel;

    private String content;

    private String specName;

    private String userFace;

    private String nickname;

    private Date createdTime;


}
