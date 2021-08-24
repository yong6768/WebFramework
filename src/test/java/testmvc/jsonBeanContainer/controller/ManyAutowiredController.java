package testmvc.jsonBeanContainer.controller;

import framework.bean.Autowired;
import testmvc.jsonBeanContainer.service.TestHelloService;
import testmvc.jsonBeanContainer.service.TestWorldService;

public class ManyAutowiredController {
    TestHelloService testHelloService;
    TestWorldService testWorldService;

    @Autowired
    public ManyAutowiredController(TestHelloService testHelloService, TestWorldService testWorldService) {
        this.testHelloService = testHelloService;
        this.testWorldService = testWorldService;
    }

    @Autowired
    public ManyAutowiredController(TestHelloService testHelloService) {
        this.testHelloService = testHelloService;
    }

    @Autowired
    public ManyAutowiredController(TestWorldService testWorldService) {
        this.testWorldService = testWorldService;
    }
}
