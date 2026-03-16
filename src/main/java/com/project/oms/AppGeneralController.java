package com.project.oms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AppGeneralController {

    @Value("${APP_VERSION:unknown}")
    private String version;

    @GetMapping("/load")
    public String load() {
        int count = 1000000000;
        log.info("Increasing load : {}",count);
        for (int i = 0; i < count; i++) {
            Math.sqrt(i);
        }
        return "done";
    }

    @GetMapping("/version")
    public String version() {
        return "OMS App Version: " + version;
    }
}
