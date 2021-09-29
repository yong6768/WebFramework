package web.service;

import framework.servlet.handler.mvc.annotation.Service;

@Service
public class HelloService {
    public String getHelloMessage() {
        return "Hello";
    }
}
