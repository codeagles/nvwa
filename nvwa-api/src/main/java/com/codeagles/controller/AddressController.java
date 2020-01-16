package com.codeagles.controller;

import com.codeagles.bo.AddressBO;
import com.codeagles.pojo.UserAddress;
import com.codeagles.service.AddressSerivce;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "地址相关", tags = {"用于地址相关接口"})
@RestController
@RequestMapping(("/address"))
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收获地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */
    @Autowired
    private AddressSerivce addressSerivce;


    @ApiOperation(value = "根据userId查询地址列表", notes = "根据userId查询地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public JSONResult list(
            @ApiParam(name = "userId", value = "用户id", required = true, example = "xx-1001")
            @RequestParam(defaultValue = "userId", required = true) String userId
    ) {

        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }
        List<UserAddress> userAddresses = addressSerivce.queryAll(userId);
        return JSONResult.ok(userAddresses);
    }


    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @ApiParam(name = "addressBO", value = "地址相关JSON串")
            @RequestBody AddressBO addressBO
    ) {
        JSONResult jsonResult = checkAddress(addressBO);
        if (jsonResult.getStatus() != 200) {
            return jsonResult;
        }

        addressSerivce.addNewUserAddress(addressBO);
        return JSONResult.ok();
    }

    private JSONResult checkAddress(AddressBO addressBO){
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JSONResult.errorMsg("收货人手机号码不能为空");
        }
        if (mobile.length() != 11) {
            return JSONResult.errorMsg("收货人手机号码长度不正确");
        }

        boolean mobileIsOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!mobileIsOk) {
            return JSONResult.errorMsg("收货人手机号码格式不正确");
        }
        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province)||
                StringUtils.isBlank(city)||
                StringUtils.isBlank(district)||
                StringUtils.isBlank(detail)) {
            return JSONResult.errorMsg("收货地址信息不能为空");
        }

        return JSONResult.ok();

    }


    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(
            @ApiParam(name = "addressBO", value = "地址相关JSON串")
            @RequestBody AddressBO addressBO
    ) {

        String addressId = addressBO.getAddressId();
        if (StringUtils.isBlank(addressId)) {
            JSONResult.errorMsg("修改地址错误：addressId不能为空");
        }

        JSONResult jsonResult = checkAddress(addressBO);
        if (jsonResult.getStatus() != 200) {
            return jsonResult;
        }

        addressSerivce.updateUserAddress(addressBO);
        return JSONResult.ok();
    }
}