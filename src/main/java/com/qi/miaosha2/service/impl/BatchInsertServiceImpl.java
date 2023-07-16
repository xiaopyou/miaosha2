package com.qi.miaosha2.service.impl;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qi.miaosha2.entrty.TUser;
import com.qi.miaosha2.mapper.OrderMapper;
import com.qi.miaosha2.service.BatchInsertService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class BatchInsertServiceImpl extends ServiceImpl<OrderMapper, Order> implements BatchInsertService {
    @Override
    public boolean saveBatch(Collection<Order> entityList) {
        return super.saveBatch(entityList);
    }

//    @Override
//    public boolean saveBatch(List<TUser> users) {
//        return super.saveBatch(entityList);
//    }
}
