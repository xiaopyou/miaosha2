package com.qi.miaosha2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qi.miaosha2.entrty.TGoods;
import com.qi.miaosha2.entrty.tgoodss;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TGoodsmapper extends BaseMapper<TGoods> {

    public List<tgoodss> splbiao();
    public tgoodss spmiaos(String id);

}
