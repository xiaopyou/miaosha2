package com.qi.miaosha2.pojo.DTO;

import com.qi.miaosha2.entrty.TUser;
import com.qi.miaosha2.entrty.tgoodss;

public class diandancha {

    private tgoodss tgoodss;
    private String userid;//用户
    private String goodsid;//商品ID
    public com.qi.miaosha2.entrty.tgoodss getTgoodss() {
        return tgoodss;
    }

    public void setTgoodss(com.qi.miaosha2.entrty.tgoodss tgoodss) {
        this.tgoodss = tgoodss;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    @Override
    public String toString() {
        return "diandancha{" +
                "tgoodss=" + tgoodss +
                ", userid=" + userid +
                ", goodsid=" + goodsid +
                '}';
    }
}
