package com.codeagles.bo;

import lombok.Data;

/**
 * 地址BO类
 *
 * @author hcn
 * @create 2020-01-16 9:17
 **/
@Data
public class AddressBO {

    private String addressId;
    private String userId;
    private String receiver;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String detail;

}
