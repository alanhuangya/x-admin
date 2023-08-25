package com.alan;

import com.alan.common.utils.JwtUtil;
import com.alan.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt() {
        User user = new User();
        user.setUsername("rrxx");
        user.setPhone("123456789");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    /**
     * 从token中解析出用户信息
     */
    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2OWZkY2E4Ny1jZjdmLTRmNjAtOTJlYy0wOWZhMTA5NGYyY2MiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDU2Nzg5XCIsXCJ1c2VybmFtZVwiOlwicnJ4eFwifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY5MjkyODA4MCwiZXhwIjoxNjkyOTI5ODgwfQ.UbmSlEOmtf1zM5YhIJOkZL-BBRf7iyIGOXSWLLLfFws";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }

    /**
     * 从token中解析出用户信息（user）
     */
    @Test
    public void testParseJwt2() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2OWZkY2E4Ny1jZjdmLTRmNjAtOTJlYy0wOWZhMTA5NGYyY2MiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDU2Nzg5XCIsXCJ1c2VybmFtZVwiOlwicnJ4eFwifSIsImlzcyI6InN5c3RlbSIsImlhdCI6MTY5MjkyODA4MCwiZXhwIjoxNjkyOTI5ODgwfQ.UbmSlEOmtf1zM5YhIJOkZL-BBRf7iyIGOXSWLLLfFws";
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
