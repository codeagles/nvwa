package com.codeagles.mapper;


import com.codeagles.vo.ItemCommentVO;
import com.codeagles.vo.SearchItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.*;

public interface ItemsMapperCustom {

    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> paramsMap);
    public List<SearchItemVO> searchItems(@Param("paramsMap") Map<String, Object> paramsMap);

}