package com.alan.interceptor;

import com.alan.common.utils.JwtUtil;
import com.alan.common.vo.Result;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtValidateInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）请求到这里的时候来决定是否要拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求中的token
        String token = request.getHeader("X-Token");
        log.debug(request.getRequestURI() + "需要验证：" + token);
        // 2. 判断token是否存在
        if (token != null) {
            // 3. 解析token
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证通过");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败,禁止访问");

        // 返回错误信息
        response.setContentType("application/json;charset=utf-8");
        Result<Object> objectResult = Result.fail(20003, "jwt无效，请重新登录");
        response.getWriter().write(JSON.toJSONString(objectResult));
        // 拦截
        return false;
    }
}
