package com.codeagles.mapper;


import com.codeagles.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.*;

public interface ItemsMapperCustom {

    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> paramsMap);

}