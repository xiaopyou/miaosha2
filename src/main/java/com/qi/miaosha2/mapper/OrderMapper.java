package com.qi.miaosha2.mapper;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


// 使用mybatis-plus增强接口
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
