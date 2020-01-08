package com.codeagles.impl;

import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.mapper.CarouselMapper;
import com.codeagles.pojo.Carousel;
import com.codeagles.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.List;

/**
 * 轮播图实现接口
 *
 * @author hcn
 * @create 2020-01-08 19:20
 **/
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<Carousel> queryAll(Integer isShow) {

        Example example = new Example(Carousel.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isShow", isShow);
        example.orderBy("sort").desc();
        List<Carousel> carousels = carouselMapper.selectByExample(example);
        return carousels;
    }
}
