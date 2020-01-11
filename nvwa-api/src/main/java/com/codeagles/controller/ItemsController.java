package com.codeagles.controller;

import com.codeagles.pojo.Items;
import com.codeagles.pojo.ItemsImg;
import com.codeagles.pojo.ItemsParam;
import com.codeagles.pojo.ItemsSpec;
import com.codeagles.service.ItemService;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.CommentLevelCountVO;
import com.codeagles.vo.ItemInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping(("/items"))
public class ItemsController extends  BaseController {

    @Autowired
    private ItemService itemService;


    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {


        if(StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("分类id不能为空");
        }

        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpec(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);

        return JSONResult.ok(itemInfoVO);
    }



    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            String itemId) {


        if(StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("分类id不能为空");
        }
        CommentLevelCountVO commentLevelCountVO = itemService.queryCommentCounts(itemId);
        return JSONResult.ok(commentLevelCountVO);
    }


    @ApiOperation(value = "查询商品评论", notes = "查询商品评论", httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
                    String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
                    Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                    Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
                    Integer pageSize) {


        if(StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("分类id不能为空");
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = itemService.queryPagedItemComments(itemId, level, page, pageSize);
        return JSONResult.ok(pagedGridResult);
    }
}