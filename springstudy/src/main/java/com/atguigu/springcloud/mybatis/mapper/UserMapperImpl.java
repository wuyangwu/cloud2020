package com.atguigu.springcloud.mybatis.mapper;

import com.atguigu.springcloud.entities.Payment;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/25 9:55
 */
public class UserMapperImpl implements PaymentDao {

    private SqlSessionTemplate sqlSession;

    public void setSqlSession(SqlSessionTemplate sqlSession){
        this.sqlSession = sqlSession;
    }

    @Override
    public int create(Payment payment) {
        PaymentDao mapper = sqlSession.getMapper(PaymentDao.class);
       return mapper.create(new Payment());
    }

    @Override
    public Payment getPaymentById(long id) {
        return null;
    }
}
