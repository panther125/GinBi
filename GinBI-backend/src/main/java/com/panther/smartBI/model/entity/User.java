package com.panther.smartBI.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户(User)表实体类
 *
 * @author makejava
 * @since 2023-07-29 21:30:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> {
    //id
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //账号
    private String userAccount;
    //密码
    private String userPassword;
    //用户昵称
    private String userName;
    //用户头像
    private String userAvatar;
    //用户角色：user/admin
    private String userRole;
    /**
     * 电话
     */
    private String phoneNum;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 积分
     */
    private Integer leftCount;

    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //是否删除
    @TableLogic
    private Integer isDelete;
}

