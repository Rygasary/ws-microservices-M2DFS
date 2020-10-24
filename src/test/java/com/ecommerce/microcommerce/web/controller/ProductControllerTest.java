package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class ProductControllerTest {
    @Mock
    ProductDao productDao;
    @Mock
    Map<Integer, Product> productDB;
    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAjouterProduit() {
        ResponseEntity<String> result = productController.ajouterProduit(new Product(33, "Un truc", 3, 2));
        Assertions.assertEquals(new ResponseEntity<>("Le produit a été ajouté avec succès", HttpStatus.OK), result);
    }

    @Test
    void testListeProduits() {
        Map<Integer, Product> result = productController.listeProduits();
        Assertions.assertEquals("Aspirateur Robot", result.get(2).getNom());
        Assertions.assertEquals("Nouveau Item", result.get(3).getNom());
    }

    @Test
    void testAfficherUnProduit() {
        ResponseEntity<Product> result = productController.afficherUnProduit(2);
        Assertions.assertEquals(2, result.getBody().getId());
        Assertions.assertEquals("Aspirateur Robot", result.getBody().getNom());
        Assertions.assertEquals(500, result.getBody().getPrix());
    }

    @Test
    void testSupprimerProduit() {
        ResponseEntity<String> result = productController.supprimerProduit(1);
        Assertions.assertEquals(new ResponseEntity<>("Le produit 1 a été supprimé", HttpStatus.OK), result);
    }

    @Test
    void testUpdateProduit() {
        ResponseEntity<String> result = productController.updateProduit(3, new Product(3, "Nouveau Item", 400, 250));
        Assertions.assertEquals(new ResponseEntity<>("Le produit a été mis à jour", HttpStatus.OK), result);
    }

    @Test
    void testCalculerMargeProduit() {
        ResponseEntity<Integer> result = productController.calculerMargeProduit(2);
        Assertions.assertEquals(new ResponseEntity<>(200, HttpStatus.OK), result);
    }

    @Test
    void testTrierProduitsParOrdreAlphabetique() {
        List<Product> result = productController.trierProduitsParOrdreAlphabetique();
        Assertions.assertEquals("Aspirateur Robot", result.get(0).getNom());
        Assertions.assertEquals("Nouveau Item", result.get(1).getNom());
        Assertions.assertEquals("Un truc", result.get(2).getNom());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme