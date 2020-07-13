package com.thoseyears.curriculum.config;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.thoseyears.curriculum.entity.User;
import com.thoseyears.curriculum.util.JwtUtil;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 *
 * @Package: com.*.*.interceptor
 * @ClassName: AdminInterceptor
 * @Description:拦截器
 * @author: zk
 * @date: 2019年9月19日 下午2:20:57
 */
public class InterceptorConfig implements  HandlerInterceptor {

    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        //统一拦截（查询当前TKOEn是否存在或者过期）
        String TOKEN  = request.getHeader("TOKEN");
        if(TOKEN!=null&& JwtUtil.checkToken(TOKEN)){
            return true;
        }else{
            System.out.println("拦截成功");

            returnJson(response);
            return false;
        }
    }
    private void returnJson(HttpServletResponse response){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            Map<String, Object> result = new HashMap<>();

            result.put("msg", "未登录/登录过期");
            result.put("code", "0");
            result.put("count", 0);
            result.put("data", null);
            JSONObject js = JSONObject.fromObject(result);
            writer.print(js.toString());
        } catch (IOException e){
            System.out.println("拦截器输出异常");
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//         System.out.println("执行了TestInterceptor的postHandle方法");
    }

    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        System.out.println("执行了TestInterceptor的afterCompletion方法");
    }

}