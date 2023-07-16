package com.qi.miaosha2.pojo.DTO;

import com.qi.miaosha2.entrty.TGoods;

import java.io.Serializable;

public class SeckillMessage implements Serializable {//拿到消息去生成订单

    @Override
    public String toString() {
        return "SeckillMessage{" +
                "tGoods=" + tGoods +
                ", Userid='" + Userid + '\'' +
                ", goodId='" + goodId + '\'' +
                '}';
    }

    public SeckillMessage() {
    }

    public SeckillMessage(TGoods tGoods, String userid, String goodId) {
        this.tGoods = tGoods;
        Userid = userid;
        this.goodId = goodId;
    }

    public SeckillMessage(String userid, String goodId) {
        Userid = userid;
        this.goodId = goodId;
    }

    private TGoods tGoods;

    public TGoods gettGoods() {
        return tGoods;
    }

    public void settGoods(TGoods tGoods) {
        this.tGoods = tGoods;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    private String Userid;
    private String goodId;

}
