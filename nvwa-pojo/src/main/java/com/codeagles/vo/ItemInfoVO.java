package com.codeagles.vo;

import com.codeagles.pojo.Items;
import com.codeagles.pojo.ItemsImg;
import com.codeagles.pojo.ItemsParam;
import com.codeagles.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/1/9
 * Time: 10:33 PM
 * <p>
 * Description: 商品品的详情VO
 */
@Data
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
