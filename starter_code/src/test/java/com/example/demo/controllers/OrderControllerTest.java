package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController(userRepository, orderRepository);
    }
    private List<Item> create_items(){
        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Round Widget");
        item1.setPrice(new BigDecimal(2.99));
        item1.setDescription("A widget that is round");
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Square Widget");
        item2.setPrice(new BigDecimal(1.99));
        item2.setDescription("A widget that is square");
        items.add(item1);
        items.add(item2);
        return items;
    }
    private User createUserWithCart(){
        User newUser = new User();
        newUser.setUsername("arwa");
        newUser.setPassword("1234567");
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(newUser);
        cart.setTotal(new BigDecimal(4.98));
        cart.setItems(create_items());
        newUser.setCart(cart);
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(newUser);
        return newUser;
    }
    @Test
    public void submit_order_test(){
        User user = createUserWithCart();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user.getCart().getUser());
        userOrder.setItems(user.getCart().getItems());
        userOrder.setTotal(user.getCart().getTotal());
        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userOrder.getTotal(), response.getBody().getTotal());
    }
    @Test
    public void get_order_test(){
        User user = createUserWithCart();
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user.getCart().getUser());
        userOrder.setItems(user.getCart().getItems());
        userOrder.setTotal(user.getCart().getTotal());
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userOrder.getUser().getCart(), response.getBody().get(0).getUser().getCart());
    }
}
