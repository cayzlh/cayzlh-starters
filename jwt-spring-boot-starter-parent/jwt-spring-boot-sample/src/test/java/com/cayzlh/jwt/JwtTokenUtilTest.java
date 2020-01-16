package com.cayzlh.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.cayzlh.framework.jwt.JwtTokenUtil;
import com.cayzlh.framework.jwt.bo.JwtUser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JwtTestApplicationTests.class)
@Rollback
@Slf4j
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void generateToken() {

        String token = getToken();
        log.info(token);

    }

    private String getToken() {
        JwtUser user = getJwtUser();

        return jwtTokenUtil.generateToken(user);
    }

    private String getToken(JwtUser jwtUser) {
        return jwtTokenUtil.generateToken(jwtUser);
    }


    private JwtUser getJwtUser() {
        List<String> roles = Lists.newArrayList("admin","vip");
        List<String> authority = Lists.newArrayList("view","add","delete");
        return UsersInfo.builder().username("cayzlh").email("cayzlh@qq.com")
                .nickname("Ant丶").mobile("13800138000").authority(authority).roles(roles).build();
    }

    @Test
    public void getUsernameFromToken() {
        String token = getToken();
        String username = jwtTokenUtil.getSubjectFromToken(token);
        Assert.assertEquals(username, "cayzlh");
        List<String> roles = jwtTokenUtil.getRolesFromToken(token);
        System.out.println(roles);
        List<String> authoritys = jwtTokenUtil.getAuthorityFromToken(token);
        System.out.println(authoritys);
    }

    @Test
    public void testRefreshToken() {
        JwtUser user = getJwtUser();
        String token = getToken(user);
        assertTrue(jwtTokenUtil.isTokenCanRefreshed(token, user.getLastPasswordReset()));
        String newToken = jwtTokenUtil.refreshToken(token);
        assertTrue(jwtTokenUtil.isTokenCanRefreshed(token, user.getLastPasswordReset()));
        assertEquals("cayzlh", jwtTokenUtil.getSubjectFromToken(newToken));
        assertEquals(token, newToken);
        assertTrue(jwtTokenUtil.validateToken(token,user));
    }
}