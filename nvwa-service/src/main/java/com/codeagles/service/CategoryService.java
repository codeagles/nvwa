package com.codeagles.service;

import com.codeagles.pojo.Category;
import com.codeagles.vo.CategoryVO;
import com.codeagles.vo.NewItemsVO;

import java.util.List;
/**
 * 分类 服务层
 *
 * @author hcn
 * @create 2020-01-08 21:07
 **/
public interface CategoryService {

    /**
     * 查询所有一级分类
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 查询二级分类
     *
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询每个分类下的六个商品
     * *
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
