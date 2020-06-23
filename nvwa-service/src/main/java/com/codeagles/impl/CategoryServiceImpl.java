package com.codeagles.impl;

import com.codeagles.enums.EnumCategoryLevel;
import com.codeagles.mapper.CategoryMapper;
import com.codeagles.mapper.CategoryMapperCustom;
import com.codeagles.pojo.Category;
import com.codeagles.service.CategoryService;
import com.codeagles.vo.CategoryVO;
import com.codeagles.vo.NewItemsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", EnumCategoryLevel.ONE.type);
        List<Category> categories = categoryMapper.selectByExample(example);
        return categories;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {

        return categoryMapperCustom.getSubCatList(rootCatId);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String, Object> map = new HashMap<>();
        map.put("rootCatId", rootCatId);


        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}
