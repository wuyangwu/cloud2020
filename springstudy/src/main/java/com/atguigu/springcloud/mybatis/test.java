package com.atguigu.springcloud.mybatis;

import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.mybatis.mapper.PaymentDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wuyang
 * @version 1.0
 * @date 2021/4/23 23:42
 */
public class test {
    @Test
    public void test() throws IOException {
        String resourcesMybatis = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(resourcesMybatis);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(in);
        SqlSession sqlSession = sessionFactory.openSession(true);
//        sqlSession.getMapper()
    }

    @Test
    public void test1() throws IOException{
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-dao.xml");
        PaymentDao paymentDao =context.getBean("paymentMapper",PaymentDao.class);
        paymentDao.create(new Payment());
    }

}
