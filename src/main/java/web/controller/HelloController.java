package web.controller;

import framework.servlet.handler.mvc.Controller;
import framework.servlet.handler.mvc.ModelAndView;
import lombok.AllArgsConstructor;
import web.service.HelloService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class HelloController implements Controller {
    private final HelloService helloService;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String helloMessage = helloService.getHelloMessage();

        ModelAndView mv = new ModelAndView();
        return mv.setViewName("hello")
                .addAttribute("helloMessage", helloMessage);
    }
}
