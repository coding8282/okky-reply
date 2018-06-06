package org.okky.reply.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class InfoResource {
    @Value("${app.name}")
    String appName;

    @GetMapping("/")
    String index() {
        return appName + " service";
    }
}
