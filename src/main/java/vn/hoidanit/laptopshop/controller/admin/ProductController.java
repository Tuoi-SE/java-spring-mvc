package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    // Display Product
    @GetMapping("/admin/product")
    public String productsPage(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("displayProducts", products);
        return "admin/product/home";
    }

    // Detail Product
    @GetMapping("/admin/product/{id}")
    public String getDetailProduct(Model model, @PathVariable long id) {
        Product product = this.productService.getProductDetail(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    // Create Product
    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("createProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String createProductPage(Model model, @ModelAttribute("createProduct") @Valid Product create,
            BindingResult newProductBindingResult,
            @RequestParam("imageProduct") MultipartFile file) {
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String imagePr = this.uploadService.savaUploadFile(file, "product");
        create.setImage(imagePr);
        this.productService.saveProduct(create);
        return "redirect:/admin/product";
    }

    // Update Data
    @RequestMapping("/admin/product/update/{id}")
    public String getUpdateProduct(Model model, @PathVariable long id) {
        Optional<Product> product = this.productService.getProductDetail(id);
        model.addAttribute("updateProduct", product.get());
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String updateUpdateProduct(Model model, @ModelAttribute("updateProduct") @Valid Product product,
            BindingResult newUserBindingResult,
            @RequestParam("imageProduct") MultipartFile file) {
        List<FieldError> errors = newUserBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(error.getField() + " - " + error.getDefaultMessage());
        }
        // validate
        if (newUserBindingResult.hasErrors()) {
            return "admin/product/update";
        }
        Product update = this.productService.getProductDetail(product.getId()).get();
        if (update != null) {
            update.setName(product.getName());
            update.setPrice(product.getPrice());
            update.setDetailDesc(product.getDetailDesc());
            update.setShortDesc(product.getShortDesc());
            update.setQuantity(product.getQuantity());
            update.setFactory(product.getFactory());
            update.setTarget(product.getTarget());
            if (!file.isEmpty()) {
                String img = this.uploadService.savaUploadFile(file, "product");
                update.setImage(img);
            }
        }
        this.productService.saveProduct(update);
        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/delete/{id}")
    public String getDeleteProduct(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("deleteProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String deleteAProduct(Model model, @ModelAttribute("deleteProduct") Product product) {
        this.productService.deleteProduct(product.getId());
        return "redirect:/admin/product";
    }
}
