package com.mycompany.application.product_review.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConf
{

    @Bean
    @Scope("singleton")
    public LocalSessionFactoryBean sessionFactory()
    {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.mycompany.model");
        sessionFactory.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));

        return sessionFactory;

    }

    @Bean
    public DataSource dataSource()
    {
        try
        {
            DataSource dataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/mycompanyDS");
            return dataSource;
        } catch (NamingException e)
        {
            throw new RuntimeException("Could not get Data Source from context", e);
        }
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager()
    {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

}