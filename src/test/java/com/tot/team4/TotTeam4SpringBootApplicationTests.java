package com.tot.team4;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TotTeam4SpringBootApplicationTests {

    @Test
    void contextLoads() {
    }
    // 写一段测试代码，输出Hello World
    @Test
    public void testHelloWorld() {
        System.out.println("Hello World");
    }

    // write a test method to generate a random number
    @Test
    public void testRandomNumber() {
        int number = (int) (Math.random() * 100);
        System.out.println(number);
    }



}
