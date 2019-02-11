package com.main.manage.filter;

import com.main.manage.modules.service.RedisService;
import com.main.manage.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.main.manage.filter.FilterKey.URL_WHITE_LIST;

@Slf4j
public class LoginFilter implements Filter {

    @Autowired
    RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUri = request.getRequestURI();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        log.info("接口地址：" + requestUri);
        //过滤白名单
        if ( ! StringUtils.isEmpty(requestUri) && URL_WHITE_LIST.contains(requestUri)){
            log.info("进入白名单："+requestUri);
            filterChain.doFilter(request, response);
        }else{
            String token = request.getHeader("token");
            String uid = request.getHeader("uid");
            log.info("token:{}", token);
            if (token == null || StringUtils.isEmpty(token)){
                log.warn("token值为空-----用户未登录");
                PrintWriter out = response.getWriter();
                out.print(FastJsonUtil.parseToJSON(ResultUtils.warn(ResultCode.USER_NOT_LOGIN)));
                out.flush();
                out.close();
                return;
            }
             String userId = (String) redisService.get(RedisKeys.LOGIN_TOKEN + uid + "_" + token);
            if(userId == null){
                log.info("用户token不匹配，需重新登录");
                PrintWriter out = response.getWriter();
                out.print(FastJsonUtil.parseToJSON(ResultUtils.warn(ResultCode.USER_NOT_LOGIN)));
                out.flush();
                out.close();
                return;
            } else{
                // 更新缓存ttl
                redisService.set(RedisKeys.LOGIN_TOKEN + uid + "_" + token, uid, RedisKeys.TOKEN_TTL);
                log.info("用户已经登录 userid:{} ", userId);
                filterChain.doFilter(request,response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
