package com.qi.miaosha2.controller;

import com.qi.miaosha2.entrty.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order/{id}")
    public User getUser(@PathVariable String id){
        System.out.println("ss");
        return  restTemplate.getForObject("http://localhost:8081/user/"+id,User.class);
    }


}
