package com.codeagles.controller;

import com.codeagles.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/6/4
 * Time: 7:22 上午
 * <p>
 * Description:
 */
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("set")
    public Object set(String key, String value) {
//        redisTemplate.opsForValue().set(key,value);
        redisOperator.set(key, value);
        return "success";

    }


    @GetMapping("get")
    public Object get(String key) {
//        Object o = redisTemplate.opsForValue().get(key);

        return redisOperator.get(key);

    }

    @GetMapping("delete")
    public Object delete(String key) {
//        redisTemplate.delete(key);
        redisOperator.del(key);
        return "delete";
    }

    /**
     * 大量的KEY查询-对比写法
     *
     * @param keys
     * @return
     */
    @GetMapping("getAlot")
    public Object getAlot(String... keys) {
        //循环写法
        List<String> result = new ArrayList<>();
        for (String key : keys) {
            result.add(redisOperator.get(key));
        }
        //批量查询
        List<String> redisKeys = Arrays.asList(keys);
        result = redisOperator.mget(redisKeys);
        return result;
    }


    @GetMapping("set/session")
    public String setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new User");
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        return "ok";

    }

}
