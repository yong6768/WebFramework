package testmvc.jsonBeanContainer.controller;

import framework.bean.Autowired;
import lombok.extern.slf4j.Slf4j;
import testmvc.jsonBeanContainer.service.TestHelloService;
import testmvc.jsonBeanContainer.service.TestWorldService;

@Slf4j
public class AutowiredCheckController {
    TestHelloService testHelloService;
    TestWorldService testWorldService;

    @Autowired
    public AutowiredCheckController(TestHelloService testHelloService, TestWorldService testWorldService) {
        this.testHelloService = testHelloService;
        this.testWorldService = testWorldService;
        log.info("AutowiredCheckController bean instantiated");
    }

    public AutowiredCheckController() {

    }

    public void helloWorld() {
        testHelloService.hello();
        testWorldService.world();
    }
}
