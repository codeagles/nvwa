package com.codeagles.service;

import com.codeagles.pojo.Items;
import com.codeagles.pojo.ItemsImg;
import com.codeagles.pojo.ItemsParam;
import com.codeagles.pojo.ItemsSpec;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.CommentLevelCountVO;
import com.codeagles.vo.ShopcartVO;

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

    /**
     * 根据商品id查询评论
     * @param itemId
     * @param commentLevel
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryPagedItemComments(String itemId, Integer commentLevel,
                                                  Integer page, Integer pageSize);

    /**
     * 根据关键词搜索商品，可排序
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
    /**
     * 根据三级分类id搜索商品，可排序
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车中商品的数据(用于刷新渲染购物车中的数据)
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据规格id获取规格对象
     * @param specId
     * @return
     */
    public ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id 获取商品主图地址
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    public void decreaseItemSpecStock(String specId, int buyCounts);
}