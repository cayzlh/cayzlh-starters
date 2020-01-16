package com.cayzlh.framewrok.base.sample.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cayzlh
 * @link https://github.com/cayzlh
 * @date 2019/11/20.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestVo {

    private Integer id;

    private String name;

}
