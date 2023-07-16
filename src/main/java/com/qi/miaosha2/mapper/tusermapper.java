package com.qi.miaosha2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qi.miaosha2.entrty.TUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface tusermapper extends BaseMapper<TUser> {
    public int pilianzhul(List<TUser> list);

}
