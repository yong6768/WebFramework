package web.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestHelloService {

    public TestHelloService() {
        log.info("TestHelloService bean instantiated");
    }

    public void hello(){
        log.info("Hello");
    }
}
