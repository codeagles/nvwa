package com.codeagles.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 6:04 PM
 * <p>
 * Description:
 */
@Data
public class MyCommentVO {
    private String commentId;
    private String content;
    private Date createdTime;
    private String itemId;
    private String itemName;
    private String specName;
    private String itemImg;
}
