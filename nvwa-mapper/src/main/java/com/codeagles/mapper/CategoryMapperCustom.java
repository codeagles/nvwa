package com.codeagles.mapper;

import com.codeagles.my.mapper.MyMapper;
import com.codeagles.pojo.Category;
import java.util.*;
public interface CategoryMapperCustom {

    public List getSubCatList(Integer rootCatId);
}