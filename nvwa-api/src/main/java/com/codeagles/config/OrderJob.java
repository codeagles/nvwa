package com.codeagles.config;

import com.codeagles.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/17
 * Time: 3:23 PM
 * <p>
 * Description:
 */
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder() {
        System.out.println("执行关闭超期订单，当前时间为:"+new Date());
        orderService.closeOrder();
    }
}
