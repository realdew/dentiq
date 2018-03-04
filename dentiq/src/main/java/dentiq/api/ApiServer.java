package dentiq.api;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value= {"dentiq.api.repository"})
public class ApiServer {

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApiServer.class, args);
		
		//Object cl = Class.forName("com.mysql.jdbc.Driver");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++    ");
	}
	
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		
		Resource[] configLocation = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis-config.xml");
		sessionFactoryBean.setConfigLocation(configLocation[0]);
		
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
		sessionFactoryBean.setMapperLocations(res);
		
		sessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
				
		return sessionFactoryBean.getObject();
	}
	
//	@Bean
//	public PlatformTransactionManager transactionManager() throws Exception {
//    //public PlatformTransactionManager transactionManager() throws URISyntaxException, GeneralSecurityException, ParseException, IOException {
//        return new DataSourceTransactionManager(dataSource());
//    }
	
}

//
// class PersistenceConfiguration {
//    @Bean
//    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws IOException {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setConfigurationProperties(mybatisProperties());
//
//        return factoryBean;
//    }
//
//    private Properties mybatisProperties() {
//        Properties properties = new Properties();
//        properties.put("lazyLoadingEnabled", "true");
//
//        return properties;
//    }
//
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}