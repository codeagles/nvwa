package com.codeagles.impl;

import com.codeagles.enums.EnumCategoryLevel;
import com.codeagles.mapper.CarouselMapper;
import com.codeagles.mapper.CategoryMapper;
import com.codeagles.pojo.Carousel;
import com.codeagles.pojo.Category;
import com.codeagles.service.CarouselService;
import com.codeagles.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 分类实现接口
 *
 * @author hcn
 * @create 2020-01-08 19:20
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", EnumCategoryLevel.ONE.type);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }
}
