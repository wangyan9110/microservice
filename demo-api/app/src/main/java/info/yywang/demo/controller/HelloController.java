package info.yywang.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shengguo
 * @version 1.0
 * @date 2017-03-08 21:37
 */
@RestController
public class HelloController {

    @RequestMapping(method = RequestMethod.GET, path = "/hello", name = "HelloService")
    public String hello() {
        return "Hello, this is from hello service.";
    }

}
