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
 * 使用定时任务关闭超期未支付订单，会存在的弊端
 * 1. 会有时间差，程序不严谨
 *     10：30分下单，11：00检查订单不足一小时，12:00检查满足要求，会超时1小时多余半个小时的时间差
 * 2. 不支持集群
 *     单机没问题，使用集群后，就会有多个定时任务
 *     解决方案：只使用一台计算机节点，单独用来运行所有的定时任务
 * 3. 会全表扫描数据，对数据库压力产生很大的影响
 *
 * 定时任务仅仅适用于比较小型、轻量级项目，或者传统项目。
 * 解决方案有很多，比如延迟队列
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
