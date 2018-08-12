package com.mycompany.webapp_config;

import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConf
{
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException 
    {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan("com.mycompany.model");
        em.setDataSource(dataSource());
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.setProperty("hibernate.show_sql", "false");
        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");

        em.setJpaProperties(jpaProperties);
        
        return em;
    }
    
    @Bean
    public DataSource dataSource()
    {
    	try
		{
			return (DataSource) new JndiTemplate().lookup("java:comp/env/jdbc/mycompanyDS");
		} catch (NamingException e)
		{
			throw new RuntimeException("Couldn't obtain Data Source from JNDI", e);
		}
    }

	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory)
	{

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);

		return txManager;
	}
	
}