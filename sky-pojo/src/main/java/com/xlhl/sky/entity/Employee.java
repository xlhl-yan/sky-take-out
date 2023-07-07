package com.xlhl.sky.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @TableField("username")
    private String username;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("phone")
    private String phone;

    @TableField("sex")
    private String sex;

    @TableField("id_number")
    private String idNumber;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField("update_time")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField("create_user")
    private Long createUser;

    @TableField("update_user")
    private Long updateUser;

}
