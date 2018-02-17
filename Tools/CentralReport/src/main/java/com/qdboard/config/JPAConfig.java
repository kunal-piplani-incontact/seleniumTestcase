package com.qdboard.config;

import java.sql.Driver;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

	@Configuration
	@EnableJpaRepositories("com.qdboard.repository")
	@EnableTransactionManagement
	@EnableCaching
	public class JPAConfig {
	
	  @Autowired
	    private Environment env;

	@Value("${spring.datasource.url}")
	private String jdbcURL;

	@Value("${spring.datasource.username}")
	private String user;

	@Value("${spring.datasource.password}")
	private String password;

	@SuppressWarnings("Duplicates")
	@Bean
	public DataSource dataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(Driver.class.getName());
	    dataSource.setUrl(jdbcURL);
	    dataSource.setInitialSize(2);
	    dataSource.setMaxActive(5);
	    return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(){
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    vendorAdapter.setDatabase(Database.DEFAULT);
	    vendorAdapter.setGenerateDdl(false);
	    vendorAdapter.setShowSql(true);

	    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

	    factory.setJpaVendorAdapter(vendorAdapter);
	    
	    factory.setPackagesToScan("com.qdboard");
	    factory.setJpaProperties(additionalProperties());
	    factory.setDataSource(dataSource());

	 //   factory.afterPropertiesSet();
	    return factory;
	}

	@Bean
	@Primary
	public EntityManagerFactory entityManagerFactory(){
	    return localContainerEntityManagerFactoryBean().getObject();
	}



	@Bean
	public EntityManager entityManager(){
	    return localContainerEntityManagerFactoryBean().getObject().createEntityManager();
	}

	@Bean
	PlatformTransactionManager transactionManager(){
	    JpaTransactionManager manager = new JpaTransactionManager();
	    manager.setEntityManagerFactory(localContainerEntityManagerFactoryBean().getObject());
	    return manager;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator(){
	    return  new HibernateExceptionTranslator();
	}
	
	  final Properties additionalProperties() {
	      Properties hibernateProperties = new Properties();
	      hibernateProperties.put("hibernate.dialect", com.qdboard.config.SQLiteDialect.class);
	      return hibernateProperties;
	   }



}
