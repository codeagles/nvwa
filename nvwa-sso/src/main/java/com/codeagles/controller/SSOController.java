package com.codeagles.controller;

import com.codeagles.pojo.Users;
import com.codeagles.service.UserSerivce;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.JsonUtils;
import com.codeagles.utils.MD5Utils;
import com.codeagles.utils.RedisOperator;
import com.codeagles.vo.UsersVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/6/4
 * Time: 7:22 上午
 * <p>
 * Description:
 */
@Controller
public class SSOController {

    @Autowired
    private UserSerivce userSerivce;
    @Autowired
    private RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    public static final String REDIS_TEMP_TICKET = "redis_temp_ticket";
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";



    @GetMapping("/login")
    public String login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {


        model.addAttribute("returnUrl", returnUrl);
        // 1. 获取userTicket门票,如果cookie中能够获取到，证明登陆过，此时签发一个一次性的临时门票
        String userTicket = getCookie(request,COOKIE_USER_TICKET);
        boolean isVerifyUserTicket = verifyUserTicket(userTicket);
        if(isVerifyUserTicket){
            String tmpTicket = createTmpTicket();
            return "redirect:"+returnUrl+"?tmpTicket="+tmpTicket;
        }

        //2. 用户从未登录过，第一次进入则跳转到CAS的统一登录页面
        return "login";
    }

    private boolean verifyUserTicket(String userTicket){
        // 0. 验证CAS门票不能为空
        if(StringUtils.isBlank(userTicket)){
            return false;
        }
        // 1. 验证CAS门票是否有效
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if(StringUtils.isBlank(userId)){
            return false;
        }

        //2. 验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN+":"+userId);
        if(StringUtils.isBlank(userRedis)){
            return false;
        }
        return true;
    }

    /**
     * CAS 统一登录接口
     *      目的：
     *      1. 登录后创建用户的全局会话                         -> uniqueToken
     *      2. 创建用户全局门票 ,用以表示在CAS端是否登录        -> userTicket
     *      3. 创建用户的临时票据，用于回跳回传
     * @param username
     * @param password
     * @param returnUrl
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                        String password,
                        String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {


        model.addAttribute("returnUrl", returnUrl);
        // TODO 后续版本完善校验是否登录


        //0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            model.addAttribute("errorMsg", "用户名或者密码不能为空");
            return "login";
        }

        //1. 实现登录
        Users users = userSerivce.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (users == null) {
            model.addAttribute("errorMsg", "用户名或者密码不正确");
            return "login";
        }

        //用户从未登录过，第一次进入则跳转到CAS的统一登录页面

        //实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(), uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        redisOperator.set(REDIS_USER_TOKEN+":"+users.getId(), JsonUtils.objectToJson(usersVO));

        //3 生成ticket门票，全局门票，代表用户在CAS端登录过
        String userTicket  = UUID.randomUUID().toString().trim();

        //3.1 用户全局门票需要放入CAS端的Cookie中
        setCookie(COOKIE_USER_TICKET,userTicket, response);
        //4. userTicket 关联用户id，并且放入到redis中，代表这个用户有门票了，可以再各个景区游玩
        redisOperator.set(REDIS_USER_TICKET+":"+userTicket, users.getId());
        //5.生成临时票据，回调到调用端网址，是由CAS端所签发的一个一次性的临时票据
        String tmpTicket = this.createTmpTicket();

        /**
         * userTicket: 用于表示用户在CAS端的一个登录状态： 已经登录
         * tempTicket: 用于颁发给用户进行一次性的验证的票据，有时效性
         *
         * 例如：
         *    去动物园，大门口买了一张统一的门票，这个就是CAS系统的全局门票和用户全局会话
         *    动物园中有一些小的景点，需要凭借你的门票领取一次性的票据，有了这张票据就可以去一些小的景点玩了
         *    这样的一个个小景点就是我们这里对应的一个个站点
         *    当我们使用完毕这张临时票据以后，就要销毁
         */
        return "redirect:"+returnUrl+"?tmpTicket="+tmpTicket;
//        return "login";

    }

    @PostMapping("verifyTmpTicket")
    @ResponseBody
    private JSONResult verifyTmpTicket(String tmpTicket, HttpServletRequest request) throws Exception {
        // 使用一次性临时票据来验证用户是否登录，如果登录过，把用户会话信息返回给站点
        //使用完毕后，需要销毁临时票据
        String tmpTicketValue = redisOperator.get(REDIS_TEMP_TICKET+":"+tmpTicket);
        if(StringUtils.isBlank(tmpTicketValue)){
            return JSONResult.errorUserTicket("用户票据异常");
        }
        //0. 如果临时票据OK，则需要销毁，并且拿到CAS端Cookie中的全局userTicket 以此在获取用户会话
        if(!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))){
            return JSONResult.errorUserTicket("用户票据异常");
        }else{
            //销毁临时票据
            redisOperator.del(REDIS_TEMP_TICKET+":"+tmpTicket);
        }
        // 1. 验证并且获取用户的userTicket
        String userTicket = getCookie(request,COOKIE_USER_TICKET);
        String userId = redisOperator.get(REDIS_USER_TICKET+":"+userTicket);
        if(StringUtils.isBlank(userId)){
            return JSONResult.errorUserTicket("用户票据异常");
        }

        //2. 验证门票对应的user会话是否存在
        String userRedis = redisOperator.get(REDIS_USER_TOKEN+":"+userId);
        if(StringUtils.isBlank(userRedis)){
            return JSONResult.errorUserTicket("用户票据异常");
        }
        //验证成功，返回OK，携带用户会话
        return JSONResult.ok(JsonUtils.jsonToPojo(userRedis, UsersVO.class));
    }

    /**
     * 创建临时票据
     * @return
     */
    private String createTmpTicket() throws Exception {
        String tmpTicket  = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_TEMP_TICKET+":"+tmpTicket, MD5Utils.getMD5Str(tmpTicket),600);
        return tmpTicket;
    }


    private void setCookie(String key, String value, HttpServletResponse response){
        Cookie cookie = new Cookie(key,value);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    private String getCookie(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if(cookies == null || StringUtils.isBlank(key)){
            return null;
        }
        String cookieValue = null;
        for (int i = 0; i < cookies.length; i++) {
            if(cookies[i].getName().equals(key)){
                cookieValue = cookies[i].getValue();
                break;
            }
        }

        return cookieValue;
    }
}
