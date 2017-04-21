package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by liqi1 on 2017/4/21.
 */
@WebFilter(urlPatterns = "/*")
public class EncodeingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String charset = "utf-8";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding(charset);//设置编码格式
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
