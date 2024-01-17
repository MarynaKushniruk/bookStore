package com.example.bookstore.service;

import com.example.bookstore.dto.order.OrderRequestDto;
import com.example.bookstore.dto.order.OrderRequestUpdateDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderItemsMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.Status;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.orderitem.OrderItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.repository.user.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper mapper;
    private final OrderItemsMapper orderItemsMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto saveOrder(String email, OrderRequestDto requestDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(
                "Can't find user by email " + email));
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUser_Id(user.getId()).orElseThrow(() ->
                        new EntityNotFoundException(
                                "Can't find shopping cart by userId " + user.getId()));
        Order order = createOrder(user, requestDto, shoppingCart);
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setPrice(cartItem.getBook().getPrice()
                            .multiply(new BigDecimal(cartItem.getQuantity())));
                    return orderItemRepository.save(orderItem);
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        return mapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> getAll(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can't find user by email " + email));
        return orderRepository.findAllByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find entity by id " + orderId));
        return order.getOrderItems()
                .stream()
                .map(orderItemsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItemByIdFromOrderById(Long orderItemId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id " + orderId));
        return order.getOrderItems().stream().filter(item ->
                        item.getId().equals(orderItemId)).map(orderItemsMapper::toDto)
                .findFirst().orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order item by id " + orderItemId));
    }

    @Override
    public OrderResponseDto updateStatus(Long orderId,
                                         OrderRequestUpdateDto requestUpdateDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Can't find order by id " + orderId));
        order.setStatus(Status.valueOf(requestUpdateDto.getStatus()));
        Set<OrderItemResponseDto> orderItemsResponse = order.getOrderItems()
                .stream()
                .map(orderItemsMapper::toDto)
                .collect(Collectors.toSet());
        OrderResponseDto orderResponseDto = mapper.toDto(order);
        orderResponseDto.setOrderItems(orderItemsResponse);
        return orderResponseDto;
    }

    private Order createOrder(User user, OrderRequestDto requestDto, ShoppingCart shoppingCart) {
        double totalPrice = shoppingCart.getCartItems()
                .stream()
                .mapToDouble(cartItem ->
                        (double) cartItem.getQuantity()
                                * cartItem.getBook().getPrice().doubleValue())
                .sum();
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(BigDecimal.valueOf(totalPrice));
        order.setStatus(Status.NEW);
        return orderRepository.save(order);
    }
}
