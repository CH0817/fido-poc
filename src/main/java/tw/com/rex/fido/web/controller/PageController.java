package tw.com.rex.fido.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class PageController {

    @RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }

}
