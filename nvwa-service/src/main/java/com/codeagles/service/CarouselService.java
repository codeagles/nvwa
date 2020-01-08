package com.codeagles.service;

import com.codeagles.pojo.Carousel;
import java.util.*;
/**
 *  轮播图接口
 * @author codeagles
 *
 */
public interface CarouselService {

    public List<Carousel> queryAll(Integer isShow);
}
