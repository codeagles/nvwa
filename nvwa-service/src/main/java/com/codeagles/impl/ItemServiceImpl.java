package com.codeagles.impl;

import com.codeagles.enums.EnumCommentLevel;
import com.codeagles.mapper.*;
import com.codeagles.pojo.*;
import com.codeagles.service.ItemService;
import com.codeagles.utils.DesensitizationUtil;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.CommentLevelCountVO;
import com.codeagles.vo.ItemCommentVO;
import com.codeagles.vo.SearchItemVO;
import com.codeagles.vo.ShopcartVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 分类实现接口
 *
 * @author hcn
 * @create 2020-01-08 19:20
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpec(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountVO queryCommentCounts(String itemId) {

        Integer goodCounts  = getCommentsCounts(itemId, EnumCommentLevel.GOOD.type);
        Integer normalCounts  = getCommentsCounts(itemId, EnumCommentLevel.NORMAL.type);
        Integer badCounts  = getCommentsCounts(itemId, EnumCommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        CommentLevelCountVO commentLevelCountVO = new CommentLevelCountVO();
        commentLevelCountVO.setGoodCounts(goodCounts);
        commentLevelCountVO.setNormalCounts(normalCounts);
        commentLevelCountVO.setBadCounts(badCounts);
        commentLevelCountVO.setTotalCounts(totalCounts);

        return commentLevelCountVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentsCounts(String itemId, Integer level){
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if(level != null){
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedItemComments(String itemId, Integer commentLevel, Integer page, Integer pageSize) {
        Map<String , Object> paramsMap = new HashMap<>();
        paramsMap.put("itemId",itemId);
        paramsMap.put("level", commentLevel);


        PageHelper.startPage(page,pageSize);
        List<ItemCommentVO> itemCommentVOS = itemsMapperCustom.queryItemComments(paramsMap);
        PagedGridResult pagedGridResult = this.setterPagedGrid(itemCommentVOS,page);

        for (ItemCommentVO itemCommentVO : itemCommentVOS) {
            itemCommentVO.setNickname(DesensitizationUtil.commonDisplay(itemCommentVO.getNickname()));
        }

        return pagedGridResult;

    }

    private PagedGridResult setterPagedGrid( List<?> list,int page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {

        Map<String , Object> paramsMap = new HashMap<>();
        paramsMap.put("keywords",keywords);
        paramsMap.put("sort", sort);


        PageHelper.startPage(page,pageSize);
        List<SearchItemVO> searchItemVOS = itemsMapperCustom.searchItems(paramsMap);

        return this.setterPagedGrid(searchItemVOS,page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {

        Map<String , Object> paramsMap = new HashMap<>();
        paramsMap.put("catId",catId);
        paramsMap.put("sort", sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemVO> searchItemVOS = itemsMapperCustom.searchItemsByThirdCat(paramsMap);

        return this.setterPagedGrid(searchItemVOS,page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList,ids);
        return itemsMapperCustom.queryItemsBySpecIds(specIdsList);
    }
}
