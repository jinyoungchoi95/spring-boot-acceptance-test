package com.ori.acceptancetest;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootAcceptanceTest
@TestMethodOrder(OrderAnnotation.class)
class SpringBootAcceptanceTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void restAssuredPortIsPositive() {
        assertThat(RestAssured.port).isPositive();
    }

    @Test
    @Order(1)
    @DisplayName("데이터를 삽입한다.")
    void setUpTableAndInsertData() {

        jdbcTemplate.update("insert into station (name) values ('name')");
        jdbcTemplate.update("insert into section (station_id) values (1)");

        boolean isExistsFeild = jdbcTemplate.queryForObject("select exists (select * from station)", Boolean.class);
        assertThat(isExistsFeild).isTrue();
    }

    @Test
    @DisplayName("truncate 실행으로 테이블 데이터가 존재하지 않는다.")
    void tableIsCleared() {
        boolean isExistsFeild = jdbcTemplate.queryForObject("select exists (select * from station)", Boolean.class);
        assertThat(isExistsFeild).isFalse();
    }
}
