package com.qi.miaosha2.service;

import com.qi.miaosha2.entrty.TGoods;
import com.qi.miaosha2.entrty.TOrder;
import com.qi.miaosha2.util.RespEntity;

public interface TSeckillGoodsservice {

    public RespEntity scdindan(String user_id, TGoods tGoods);//生成秒杀订单
//    public RespEntity scdindanyouhua(String user_id, TGoods tGoods);//优化生成秒杀订单

}
