package com.example.ai.nospring;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.example.ai.nospring.entity.Person;
import com.example.ai.nospring.mapper.PersonMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;


/**
 * @author miemie
 * @since 2020-03-11
 */
public class NoSpringApplication {

    private static SqlSessionFactory sqlSessionFactory = initSqlSessionFactory();

    public static void main(String[] args) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            PersonMapper mapper = session.getMapper(PersonMapper.class);
            Person person = new Person();
            person.setName("小张");
//            person.setId(3L);
            person.setAge(19);
            mapper.insert(person);
            System.out.println("结果: " + mapper.selectById(person.getId()));
        }
    }

    public static SqlSessionFactory initSqlSessionFactory() {
        DataSource dataSource = dataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("Production", transactionFactory, dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration(environment);
        configuration.addMapper(PersonMapper.class);
        configuration.setLogImpl(StdOutImpl.class);
        return new MybatisSqlSessionFactoryBuilder().build(configuration);
    }

//    public static DataSource dataSource() {
//        PooledDataSource dataSource = new PooledDataSource();
//        dataSource.setDriver(org.h2.Driver.class.getName());
//        dataSource.setUrl("jdbc:h2:mem:test");
//        dataSource.setUsername("root");
//        dataSource.setPassword("test");
//        try {
//            Connection connection = dataSource.getConnection();
//            Statement statement = connection.createStatement();
//            statement.execute("create table person (" +
//                    "id BIGINT NOT NULL," +
//                    "name VARCHAR(30) NULL," +
//                    "age INT NULL," +
//                    "PRIMARY KEY (id)" +
//                    ")");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dataSource;
//    }

    public static DataSource dataSource() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(com.mysql.cj.jdbc.Driver.class.getName());
        dataSource.setUrl("jdbc:mysql://localhost:3316/iask?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
//            statement.execute("create table person (" +
//                    "id BIGINT NOT NULL," +
//                    "name VARCHAR(30) NULL," +
//                    "age INT NULL," +
//                    "PRIMARY KEY (id)" +
//                    ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
