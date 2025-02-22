package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {
    @GetMapping("/product")
    public String getItemPage() {
        return "/client/product/detail";
    }

    @GetMapping("/product/{id}")
    public String getItemDetailPage() {
        return "/client/product/detail";
    }
}
