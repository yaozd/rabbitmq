package com.space.rbq.store;

import com.space.rbq.store.config.EventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StoreApplication.class);
        app.addListeners(new EventListener());
        app.run(args);
    }
}
