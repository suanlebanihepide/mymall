package com.example.mymmal.dao;

import com.example.mymmal.dataobject.OrderInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderInfoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Jun 18 01:07:08 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Jun 18 01:07:08 CST 2020
     */
    int insert(OrderInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Jun 18 01:07:08 CST 2020
     */
    int insertSelective(OrderInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Jun 18 01:07:08 CST 2020
     */
    OrderInfoDO selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Jun 18 01:07:08 CST 2020
     */
    int updateByPrimaryKeySelective(OrderInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Thu Jun 18 01:07:08 CST 2020
     */
    int updateByPrimaryKey(OrderInfoDO record);

    //定时关单

    List<OrderInfoDO> selectOrderStatusByCreateTime(@Param("status")Integer status , @Param("date") String date);

    //关单
    int closeOrderByOrderId(String id);

}