package web.controller;

import framework.bean.Bean;
import framework.servlet.handler.mvc.annotation.Controller;
import framework.servlet.handler.mvc.annotation.RequestBody;
import framework.servlet.handler.mvc.annotation.RequestMapping;
import framework.servlet.handler.mvc.annotation.RequestMethod;
import lombok.AllArgsConstructor;
import web.dao.HelloRequest;
import web.service.HelloService;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Controller
@RequestMapping("/hello")
public class HelloController {
    private final HelloService helloService;

    @Bean
    public HelloService helloService() {
        return new HelloService();
    }

    @RequestMapping(value = "/world", method = RequestMethod.POST)
    public String helloWorld(
            HttpServletRequest request,
            @RequestBody HelloRequest helloRequest
    ) {
        System.out.println("request = " + request);
        System.out.println("helloRequest = " + helloRequest);
        return "hello";
    }
}
