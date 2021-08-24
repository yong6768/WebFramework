package web.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestWorldService {

    public TestWorldService() {
        log.info("TestWorldService bean instantiated");
    }

    public void world(){
        log.info("World");
    }
}
