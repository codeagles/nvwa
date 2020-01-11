package com.codeagles.vo;

import lombok.Data;

/**
 * 用于商品评价数量的VO
 *
 * @author hcn
 * @create 2020-01-10 9:25
 **/
@Data
public class CommentLevelCountVO {

    public Integer totalCounts;
    public Integer goodCounts;
    public Integer normalCounts;
    public Integer badCounts;
}
