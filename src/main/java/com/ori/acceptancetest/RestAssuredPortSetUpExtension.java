package com.ori.acceptancetest;

import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

class RestAssuredPortSetUpExtension implements BeforeEachCallback {

    private static final String LOCAL_SERVER_PORT_PATH = "local.server.port";

    @Override
    public void beforeEach(final ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        String port = applicationContext.getEnvironment()
                .getProperty(LOCAL_SERVER_PORT_PATH);
        RestAssured.port = parseToIntegr(port);
    }

    private int parseToIntegr(final String port) {
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new RuntimeException("port parsing error");
        }
    }
}
