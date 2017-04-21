package filter;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


/**
 * Created by liqi1 on 2017/4/21.
 */
@WebFilter(urlPatterns = "/*",initParams = @WebInitParam(name = "param",value = "<;>"))
public class EncodeingFilter implements Filter {
    private String param;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        param = filterConfig.getInitParameter("param");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String charset = "utf-8";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding(charset);//设置编码格式
        String[] filterParam = param.split(";");
        String name = request.getParameter("name");
        Enumeration<String> paramterName = request.getParameterNames();
        if(paramterName.hasMoreElements()){
            StringEscapeUtils.escapeHtml3(request.getParameter(paramterName.nextElement()));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
