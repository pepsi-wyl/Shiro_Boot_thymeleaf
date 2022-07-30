package com.pepsiwyl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Component;

/**
 * @author by pepsi-wyl
 * @date 2022-04-16 14:08
 */

// lombok注解
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

// 别名
@Alias("user")
// 数据库名称 表名称
@TableName(schema = "shiro", value = "t_user")

// 注册组件
@Component("user")
public class User {

    // 主键 雪花算法
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    // MD5 + salt + hash 加密
    private String password;

    /**
     * salt 盐字段 加密
     */
    private String salt;

}