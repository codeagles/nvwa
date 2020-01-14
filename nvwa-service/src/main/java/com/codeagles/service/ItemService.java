package com.codeagles.service;

import com.codeagles.pojo.Items;
import com.codeagles.pojo.ItemsImg;
import com.codeagles.pojo.ItemsParam;
import com.codeagles.pojo.ItemsSpec;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.CommentLevelCountVO;

import java.util.List;

/**
 * 分类 服务层
 *
 * @author hcn
 * @create 2020-01-08 21:07
 **/
public interface ItemService {
    /**
    * 根据商品id查询
    * @param itemId
    * @return
    */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id 查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemList(String itemId);

    /**
     * 根据商品id 查询商品规格
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpec(String itemId);

    /**
     * 根据商品id 查询商品参数
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);


    /**
     * 根据商品id 查询商品的评价等级数量
     * @param itemId
     */
    public CommentLevelCountVO queryCommentCounts(String itemId);


    public PagedGridResult queryPagedItemComments(String itemId, Integer commentLevel,
                                                  Integer page, Integer pageSize);


    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);
}