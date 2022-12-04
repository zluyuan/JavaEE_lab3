package cn.edu.xmu.oomall.goods.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.goods.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 商品控制器
 * @author Ming Qiu
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class UnAuthorizedController {

    private final Logger logger = LoggerFactory.getLogger(UnAuthorizedController.class);


    private ProductService productService;

    @Autowired
    public UnAuthorizedController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ReturnObject findProductById(@PathVariable("id") Long id) {
        return new ReturnObject(productService.findProductById(id));
    }
}
