package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController(itemRepository);
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

    @Test
    public void get_Items_test(){
        List<Item> items = create_items();
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> itemsResponse = itemController.getItems();
        assertNotNull(itemsResponse.getBody());
        assertEquals(200, itemsResponse.getStatusCodeValue());
        assertEquals(items.size(), itemsResponse.getBody().size());
    }
    @Test
    public void get_Item_byId_test(){
        List<Item> items = create_items();
        when(itemRepository.findById(items.get(0).getId())).thenReturn(Optional.ofNullable(items.get(0)));
        ResponseEntity<Item> itemsResponse = itemController.getItemById(items.get(0).getId());
        assertNotNull(itemsResponse.getBody());
        assertEquals(200, itemsResponse.getStatusCodeValue());
        assertEquals(items.get(0).getId(), itemsResponse.getBody().getId());
        assertEquals(items.get(0).getPrice(), itemsResponse.getBody().getPrice());
    }
    @Test
    public void get_Items_byName_test(){
        List<Item> items = create_items();
        when(itemRepository.findByName(items.get(0).getName())).thenReturn(items);
        ResponseEntity<List<Item>> itemsResponse = itemController.getItemsByName(items.get(0).getName());
        assertNotNull(itemsResponse.getBody());
        assertEquals(200, itemsResponse.getStatusCodeValue());
        assertEquals(items.size(), itemsResponse.getBody().size());
    }
}
