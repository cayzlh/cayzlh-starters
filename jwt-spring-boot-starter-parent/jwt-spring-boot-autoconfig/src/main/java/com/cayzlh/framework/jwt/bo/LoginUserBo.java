package com.cayzlh.framework.jwt.bo;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

/**
 * @author Ant丶
 * @link https://github.com/cayzlh
 * @date 2020-04-21.
 */
@Data
public class LoginUserBo implements Serializable {

    private Long id;

    private String username;

    private String nickname;

    private Integer gender;

    private Integer state;

    private Long departmentId;

    private String departmentName;

    private Long roleId;

    private String roleName;

    private String roleCode;

    private Set<String> permissionCodes;

}
