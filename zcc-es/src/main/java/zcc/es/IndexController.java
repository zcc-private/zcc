package zcc.es;

/**
 * @Description: 默认controller
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class IndexController
{

    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String appName;



    @RequestMapping(value = {"/", "/info"}, method = RequestMethod.GET)
    public String toLoginPage(HttpServletRequest request){
        return "项目名称："+appName+"<br/>端口："+port+"<br/>当前环境：";
    }
}

