package com.codeagles.bo.center;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 11:25 AM
 * <p>
 * Description:
 */
@Data
public class OrderItemsCommentsBO {
    private String commentId;
    private String itemId;
    private String itemName;
    private String itemSpecId;
    private String itemSpecName;
    private String commentLevel;
    private String content;

    @Override
    public String toString() {
        return "OrderItemsCommentsBO{" +
                "commentId='" + commentId + '\'' +
                ", commentLevel='" + commentLevel + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
