package mate.academy.spring.controller;

import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.mapper.DtoResponseMapper;
import mate.academy.spring.model.Order;
import mate.academy.spring.model.ShoppingCart;
import mate.academy.spring.model.dto.response.OrderResponseDto;
import mate.academy.spring.service.OrderService;
import mate.academy.spring.service.ShoppingCartService;
import mate.academy.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final DtoResponseMapper<OrderResponseDto, Order> orderMapper;
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(DtoResponseMapper<OrderResponseDto, Order> orderMapper,
                           ShoppingCartService shoppingCartService,
                           OrderService orderService,
                           UserService userService) {
        this.orderMapper = orderMapper;
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public List<OrderResponseDto> getOrdersHistory(@RequestParam Long userId) {
        return orderService.getOrdersHistory(userService.get(userId))
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/complete")
    public OrderResponseDto completeOrder(@RequestParam Long userId) {
        ShoppingCart cart = shoppingCartService.getByUser(userService.get(userId));
        return orderMapper.toDto(orderService.completeOrder(cart));
    }
}