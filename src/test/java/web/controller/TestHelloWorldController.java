package web.controller;

import lombok.extern.slf4j.Slf4j;
import web.service.TestHelloService;
import web.service.TestWorldService;

@Slf4j
public class TestHelloWorldController {

    TestHelloService testHelloService;
    TestWorldService testWorldService;

    public TestHelloWorldController(TestHelloService testHelloService, TestWorldService testWorldService) {
        this.testHelloService = testHelloService;
        this.testWorldService = testWorldService;
        log.info("TestHelloWorldController bean instantiated");
    }

    public void helloWorld() {
        testHelloService.hello();
        testWorldService.world();
    }

}
