package com.bank.interceptor;

import org.slf4j.Logger;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yubraj on 1/5/17.
 */
public class PerformanceInterceptor implements HandlerInterceptor {

    private ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();
    Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch = new StopWatch(handler.toString());
        stopWatch.start(handler.toString());
        stopWatchThreadLocal.set(stopWatch);
        logger.info("-----------------------------------------------------------------------------");
        logger.info("Accessing URL path : " + getUrlPath(request));
        logger.info("Request processing started on : " + getCurrentTime());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Request Processing ended on : " +getCurrentTime());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();
        logger.info("Total time taken for processing : " + stopWatch.getTotalTimeMillis() + "ms");
        stopWatchThreadLocal.set(null);
        logger.info("-----------------------------------------------------------------------------");
    }

    private String getUrlPath(HttpServletRequest request){
        String currentPath = request.getRequestURI();
        String queryString = request.getQueryString();
        queryString = queryString == null ? "" : "?" +queryString;
        return currentPath+queryString;
    }

    private String getCurrentTime() {
        DateFormat dateFormatter = new SimpleDateFormat("dd/mm/yy 'at' hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return dateFormatter.format(calendar.getTime());
    }

}
