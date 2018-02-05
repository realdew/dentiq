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

@SpringBootApplication
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
		
		Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
		sessionFactoryBean.setMapperLocations(res);
		
		sessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
				
		return sessionFactoryBean.getObject();
	}
	
}