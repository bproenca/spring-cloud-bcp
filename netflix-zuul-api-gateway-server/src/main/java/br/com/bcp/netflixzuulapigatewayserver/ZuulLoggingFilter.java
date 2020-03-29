package br.com.bcp.netflixzuulapigatewayserver;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ZuulLoggingFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(ZuulLoggingFilter.class);

    /*
     * you can check the request and see if you want
     * to skip this filter or not
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        logger.info("request -> {} uri -> {}", request, request.getRequestURI());
        return null;
    }

    /*
     * pre - post - error
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /*
     * if you have many filters (this is the order)
     */
    @Override
    public int filterOrder() {
        return 1;
    }

}