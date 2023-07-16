package com.qi.miaosha2.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qi.miaosha2.entrty.TSeckillGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TSeckillGoodsmapper extends BaseMapper<TSeckillGoods> {
    public TSeckillGoods cxmsspbiao (Long id);

}
