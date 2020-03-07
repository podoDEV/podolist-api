package com.podo.podolist.controller;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class MaintenanceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testL7check() {
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity("/monitor/l7check", Object.class);
        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
