package com.ajin.shiro;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ajin
 * @create 2023-05-13 1:05
 */
@Data
public class AccountProfile implements Serializable {
    private Long id;

    private String username;

    private String avatar;

    private String email;

    private Integer status;

    private LocalDateTime created;

    private LocalDateTime lastLogin;
}
