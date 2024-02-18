package com.momo.taskManagement.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "libraryEntityManagerFactory",
        transactionManagerRef = "libraryTransactionManager",
        basePackages = { "com.momo.taskManagement.libraryFolder.repository" })
public class LibraryDatasourceConfiguration {

    @Bean(name="libraryProperties")
    @ConfigurationProperties("spring.library.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name="libraryDatasource")
    @ConfigurationProperties(prefix = "spring.library.datasource")
    public DataSource datasource(@Qualifier("libraryProperties") DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }
    @Bean(name="libraryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBeanx
            (EntityManagerFactoryBuilder builder,
             @Qualifier("libraryDatasource") DataSource dataSource){

        return builder.dataSource(dataSource)
                .packages("com.momo.taskManagement.libraryFolder.model")
                .persistenceUnit("library").build();
    }

    @Bean(name = "libraryTransactionManager")
    @ConfigurationProperties(prefix="spring.jpa")
    public PlatformTransactionManager transactionManager(
            @Qualifier("libraryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}
