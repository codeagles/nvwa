package com.codeagles.mapper;

import com.codeagles.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface OrderMapperCustom {

    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> paramsMap);
}
