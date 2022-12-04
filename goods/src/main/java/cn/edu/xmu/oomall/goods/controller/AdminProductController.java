package cn.edu.xmu.oomall.goods.controller;


import cn.edu.xmu.oomall.goods.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;
import static cn.edu.xmu.javaee.core.util.Common.createPageObj;

/**
 * 商品控制器
 * @author Ming Qiu
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class AdminProductController{

    private final Logger logger = LoggerFactory.getLogger(AdminProductController.class);


    private ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

}
