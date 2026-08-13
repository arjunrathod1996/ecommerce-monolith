package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping
    public String index(){
        return "index";
    }

    // Users UI
    @GetMapping("/users")
    public String users(Model model){
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        model.addAttribute("user", new UserDTO());
        return "user_form";
    }

    @PostMapping("/users")
    public String createUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult br, RedirectAttributes ra){
        if(br.hasErrors()){
            return "user_form";
        }
        userService.createUser(userDTO);
        ra.addFlashAttribute("success", "User created");
        return "redirect:/ui/users";
    }

    // Products UI
    @GetMapping("/products")
    public String products(Model model){
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model){
        model.addAttribute("product", new ProductDTO());
        return "product_form";
    }

    @PostMapping("/products")
    public String createProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult br, RedirectAttributes ra){
        if(br.hasErrors()){
            return "product_form";
        }
        productService.createProduct(productDTO);
        ra.addFlashAttribute("success", "Product created");
        return "redirect:/ui/products";
    }

    // Orders UI (simple single-item order form)
    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("orderRequest", new CreateOrderRequest());
        return "orders";
    }

    @PostMapping("/orders")
    public String createOrder(@ModelAttribute("orderRequest") CreateOrderRequest orderRequest, RedirectAttributes ra){
        // The form will bind userId and a single order item (productId + quantity).
        // Ensure we transform single item into the request if needed.
        if(orderRequest.getItems() == null || orderRequest.getItems().isEmpty()){
            ra.addFlashAttribute("error", "Please provide at least one order item");
            return "redirect:/ui/orders";
        }
        try{
            orderService.createOrder(orderRequest);
            ra.addFlashAttribute("success", "Order created");
        }catch (Exception e){
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/ui/orders";
    }
}
