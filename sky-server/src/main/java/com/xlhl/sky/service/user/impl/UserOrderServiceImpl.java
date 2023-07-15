package com.xlhl.sky.service.user.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlhl.sky.constant.MessageConstant;
import com.xlhl.sky.context.BaseContext;
import com.xlhl.sky.dto.OrdersPaymentDTO;
import com.xlhl.sky.dto.OrdersSubmitDTO;
import com.xlhl.sky.entity.*;
import com.xlhl.sky.exception.AddressBookBusinessException;
import com.xlhl.sky.exception.OrderBusinessException;
import com.xlhl.sky.exception.ShoppingCartBusinessException;
import com.xlhl.sky.mapper.user.*;
import com.xlhl.sky.service.user.UserOrderService;
import com.xlhl.sky.utils.WeChatPayUtil;
import com.xlhl.sky.vo.OrderPaymentVO;
import com.xlhl.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class UserOrderServiceImpl implements UserOrderService {

    @Resource
    private UserOrderMapper orderMapper;

    @Resource
    private UserOrderDetailMapper orderDetailMapper;

    @Resource
    private UserShoppingCartMapper shoppingCartMapper;

    @Resource
    private UserAddressBookMapper addressBookMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private WeChatPayUtil weChatPayUtil;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        assert ordersSubmitDTO != null;
        //1. ==>处理业务异常
        //==>地址簿为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //==>购物车信息为空
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        Long uid = BaseContext.getCurrentId();
        wrapper.eq("user_id", uid);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(wrapper);
        if (shoppingCartList == null || shoppingCartList.size() <= 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2. ==>插入订单信息
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        LocalDateTime orderTime = LocalDateTime.now();
        //下单时间
        orders.setOrderTime(orderTime);
        //未付款
        orders.setPayStatus(Orders.UN_PAID);
        //等待付款状态
        orders.setStatus(Orders.PENDING_PAYMENT);
        //订单号
        String orderCode = String.valueOf(System.currentTimeMillis());
        orders.setNumber(orderCode);
        //手机号码
        orders.setPhone(addressBook.getPhone());
        //收货人
        orders.setConsignee(addressBook.getConsignee());
        //设置订单归属者
        orders.setUserId(uid);
        //插入订单数据 使用 MybatisPlus一键插入
        orderMapper.insert(orders);

        //3. ==>插入订单插入多条详情信息
        List<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCartList.forEach(shoppingCart -> {
            //订单明细
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            //设置详情关联的订单id
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        });
        //批量插入
        orderDetailMapper.insertBatch(orderDetailList);

        //4. ==>清空购物车信息
        shoppingCartMapper.clean(uid);

        //5. ==>封装VO信息
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orderTime)
                .orderNumber(orderCode)
                .orderAmount(orders.getAmount())
                .build();
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.selectById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal("0.01"), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }
}
