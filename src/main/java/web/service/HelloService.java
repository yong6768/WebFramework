package web.service;

import framework.bean.Component;

@Component
public class HelloService {
    public String getHelloMessage() {
        return "Hello";
    }
}
