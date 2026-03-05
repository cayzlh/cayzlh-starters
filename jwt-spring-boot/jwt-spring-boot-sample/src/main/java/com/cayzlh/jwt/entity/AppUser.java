/*
 * Copyright (c) 2020.  All rights reserved.
 *
 * BLOG:  https://cayzlh.com
 * GITHUB:  https://github.com/cayzlh
 */

package com.cayzlh.jwt.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ant丶
 * @date 2020-04-22.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements Serializable {

    private Long id;

    private String username;

    private String nickname;

    private String salt;

}
