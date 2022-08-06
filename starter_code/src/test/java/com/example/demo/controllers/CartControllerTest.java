package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init(){
        cartController = new CartController(userRepository, cartRepository, itemRepository);
    }
    @Test
    public void create_cart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        User user = createUser("arwa", "1234567");
        Item item = createItem();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(1);
        ResponseEntity<Cart> newCart = cartController.addTocart(cartRequest);
        assertNotNull(newCart.getBody());
        assertEquals(200, newCart.getStatusCodeValue());

    }
    @Test
    public void create_cart_with_nonExistingUser(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        User user = new User();
        user.setUsername("none");
        user.setPassword("1234567");
        Item item = createItem();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(1);
        ResponseEntity<Cart> newCart = cartController.addTocart(cartRequest);
        assertEquals(404, newCart.getStatusCodeValue());

    }
    private User createUser(String name, String password){
        User newUser = new User();
        newUser.setUsername(name);
        newUser.setPassword(password);
        newUser.setCart(new Cart());
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(newUser);
        return newUser;
    }
    private Item createItem(){
        Item newItem = new Item();
        newItem.setId(1L);
        newItem.setName("Square Widget");
        newItem.setPrice(new BigDecimal(1.99));
        newItem.setDescription("A widget that is square");
        when(itemRepository.findById(newItem.getId())).thenReturn(Optional.of(newItem));
        return newItem;
    }

    @Test
    public void remove_cart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        User user = createUser("arwa", "1234567");
        Item item = createItem();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(1);
        ResponseEntity<Cart> newCart = cartController.removeFromcart(cartRequest);
        assertNotNull(newCart.getBody());
        assertEquals(200, newCart.getStatusCodeValue());
        assertEquals(0, newCart.getBody().getItems().size());

    }
}
