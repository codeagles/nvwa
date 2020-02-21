package com.codeagles.impl.center;

import com.codeagles.utils.PagedGridResult;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 6:35 PM
 * <p>
 * Description:通用Service
 */

public class BaseService {

    public PagedGridResult setterPagedGrid(List<?> list, int page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
