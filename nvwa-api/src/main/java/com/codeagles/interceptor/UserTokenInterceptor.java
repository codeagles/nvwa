package com.codeagles.interceptor;

import com.codeagles.controller.BaseController;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.JsonUtils;
import com.codeagles.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/6/16
 * Time: 9:35 下午
 * <p>
 * Description: 用户权限拦截器
 */
public class UserTokenInterceptor extends BaseController implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 拦截请求 在访问controller调用之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * false：请求被拦截、被驳回，验证出现问题
         * true：请求在经过校验之后，是OK的，可以被放行
         */
        String userToken = request.getHeader("headerUserToken");
        String userId = request.getHeader("headerUserId");

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {

            String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(uniqueToken)) {
                returErrorResponse(response, JSONResult.errorMsg("请登录！"));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    returErrorResponse(response, JSONResult.errorMsg("账号可能在异地登录！！"));
                    return false;
                }
            }
        } else {
            returErrorResponse(response, JSONResult.errorMsg("请登录！"));
            return false;
        }
        return true;
    }

    public void returErrorResponse(HttpServletResponse response, JSONResult jsonResult) {
        OutputStream outputStream = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(jsonResult).getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 请求访问controller之后渲染视图之前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问controller之后，渲染视图之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
