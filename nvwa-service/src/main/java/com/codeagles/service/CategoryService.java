package com.codeagles.service;

import com.codeagles.pojo.Category;
import java.util.*;
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
}
