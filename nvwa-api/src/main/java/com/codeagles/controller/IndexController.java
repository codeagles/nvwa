package com.codeagles.controller;

import com.codeagles.bo.UserBO;
import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.pojo.Carousel;
import com.codeagles.pojo.Users;
import com.codeagles.service.CarouselService;
import com.codeagles.service.UserSerivce;
import com.codeagles.utils.CookieUtils;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.JsonUtils;
import com.codeagles.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "首页", tags = {"用于首页展示的相关接口"})
@RestController
@RequestMapping(("/index"))
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {

        List<Carousel> carousels = carouselService.queryAll(EnumYesOrNo.YES.type);
        return JSONResult.ok(carousels);
    }


}