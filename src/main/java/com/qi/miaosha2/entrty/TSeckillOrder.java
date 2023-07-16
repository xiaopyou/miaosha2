package com.qi.miaosha2.entrty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 秒杀订单表(TSeckillOrder)实体类
 *
 * @author makejava
 * @since 2023-06-04 17:56:30
 */
@TableName("t_seckill_order")
public class TSeckillOrder implements Serializable {
    private static final long serialVersionUID = 480804654536310556L;

    public TSeckillOrder(Long id, Long userId, Long orderId, Long goodsId) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.goodsId = goodsId;
    }

    /**
     * 秒杀订单ID
     */
    @TableId(value = "id",type = IdType.AUTO)

    private Long id;
    /**
     * 用户ID
     */
    @TableField(value ="user_id",exist = true)
    private Long userId;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 商品ID
     */
    private Long goodsId;

    public TSeckillOrder() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "TSeckillOrder{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", goodsId=" + goodsId +
                '}';
    }
}

