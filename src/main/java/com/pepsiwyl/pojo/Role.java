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
 * @date 2022-04-16 14:09
 */

// lombok注解
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

// 别名
@Alias("role")
// 数据库名称 表名称
@TableName(schema = "shiro", value = "t_role")

// 注册组件
@Component("role")
public class Role {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String name;

}
