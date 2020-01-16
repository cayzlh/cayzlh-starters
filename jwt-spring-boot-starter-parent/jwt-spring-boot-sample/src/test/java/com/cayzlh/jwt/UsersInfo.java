package com.cayzlh.jwt;

import com.cayzlh.framework.jwt.bo.JwtUser;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersInfo implements JwtUser {

    private String username;

    private String mobile;

    private String nickname;

    private String email;

    private String password;

    private List<String> roles;

    private List<String> authority;

    @Override
    public Date getLastPasswordReset() {
        return new Date(System.currentTimeMillis() - (24*60*60*1000));
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public List<String> getAuthority() {
        return authority;
    }
}
