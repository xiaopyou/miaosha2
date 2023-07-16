package com.qi.miaosha2.entrty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qi.miaosha2.service.impl.TSeckillGoodsimpl;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.io.Serializable;

/**
 * 秒杀商品表(TSeckillGoods)实体类
 *
 * @author makejava
 * @since 2023-06-04 17:56:30
 */
@TableName("t_seckill_goods")
public class TSeckillGoods  implements Serializable {
    private static final long serialVersionUID = 519175275348459797L;
    /**
     * 秒杀商品ID
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 商品ID
     */
    @TableField(value ="goods_id",exist = true)
    private Long goodsId;
    /**
     * 秒杀价
     */
    @TableField(value ="seckill_price",exist = true)
    private Double seckillPrice;
    /**
     * 库存数量
     */
    @TableField(value ="stock_count",exist = true)
    private Integer stockCount;
    /**
     * 秒杀开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss" )
    private Date startDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Double getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Double seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 秒杀结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss" )
    private Date endDate;

    @Override
    public String toString() {
        return "TSeckillGoods{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", seckillPrice=" + seckillPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

