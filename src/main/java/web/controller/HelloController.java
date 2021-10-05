package web.controller;

import framework.bean.Bean;
import framework.servlet.handler.mvc.annotation.*;
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
    @ResponseBody
    public Integer helloWorld(
            HttpServletRequest request,
            @RequestBody HelloRequest helloRequest
    ) {
        System.out.println("request = " + request);
        System.out.println("helloRequest = " + helloRequest);
        return 2000;
//        return "hello";
    }

    @RequestMapping(value = "/world2", method = RequestMethod.GET)
    @ResponseBody
    public String helloWorld2 (
            @RequestParam("hello") String hello,
            @RequestParam("age") int age
    ) {
        System.out.println("hello = " + hello);
        System.out.println("age = " + age);
        return hello+"-"+age;
    }
}
