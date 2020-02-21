package com.codeagles.mapper;

import com.codeagles.my.mapper.MyMapper;
import com.codeagles.pojo.ItemsComments;
import com.codeagles.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    public void saveComments(Map<String , Object> map);

    public List<MyCommentVO> queryComments(@Param("paramsMap") Map<String, Object> map);
}