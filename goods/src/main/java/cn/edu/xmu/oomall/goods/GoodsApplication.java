//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Ming Qiu
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core",
		"cn.edu.xmu.oomall.goods"})
@EnableJpaRepositories(basePackages = "cn.edu.xmu.oomall.goods.mapper")
@EnableMongoRepositories(basePackages = "cn.edu.xmu.oomall.goods.mapper")
public class GoodsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoodsApplication.class, args);
	}

}
