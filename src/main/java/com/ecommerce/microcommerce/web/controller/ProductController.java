package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Api(value="API des produits")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    private static Map<Integer, Product> productDB = new HashMap<Integer, Product>(){
        {
            put(1, new Product(1, "Ordinateur Portable", 350, 230));
            put(2, new Product(2, "Aspirateur Robot", 500, 300));
            put(3, new Product(3, "Table de Ping Pong", 750, 350));
        }
    };


    //Récupérer la liste des produits
    @ApiOperation(value = "Get all products", response = List<Product>, tags = "listeProduits")
    @GetMapping(value = "/Produits")
    public Map<Integer, Product> listeProduits() {
        return productDB;
    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "Get a product with its ID", response = Product.class, tags = "afficherUnProduit")
    @GetMapping(value = "/getProduct/{productId}")
    public Product afficherUnProduit(@PathVariable int productId) {
        System.out.println("Getting product with ID " + productId);

        Product product = productDB.get(productId);

        if (product == null){
            product = new Product(0, "Votre produit n'existe pas. Ou n'est plus en stock. Ou vous vous êtes trompé d'ID. Mais peut importe, le truc, c'est qu'IL EXISTE PAS!!!!", 0, 0);
        }
        return product;
    }




    //ajouter un produit
    @ApiOperation(value = "Add a product", tags = "ajouterProduit")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // supprimer un produit
    @ApiOperation(value = "Delete a product", tags = "supprimerProduit")
    @DeleteMapping(value = "/deleteProduct/{productId}")
    public ResponseEntity<String> supprimerProduit(@PathVariable int productId) {
        System.out.println("Deleting product with ID " + productId);
        if (productDB.get(productId) == null){
            return new ResponseEntity<>("Le produit " + productId + " n'existe pas", HttpStatus.NOT_FOUND);
        }
        productDB.remove(productId);
        return new ResponseEntity<>("Le produit " + productId + " a été supprimé", HttpStatus.OK);
    }

    // Mettre à jour un produit
    @ApiOperation(value = "Update entirely a product", tags = "updateProduit")
    @PutMapping(value = "/patchProduct/{productid}")
    public ResponseEntity<String> updateProduit(@RequestBody Product product, @PathVariable int productId) {
        System.out.println("Updating product with ID " + productId);
        Product productUpdated = productDB.get(productId);
        if (productUpdated == null){
            return new ResponseEntity<>("Le produit " + productId + " n'existe pas", HttpStatus.NOT_FOUND);
        }
        product.setId(productId);
        productDB.update(product);

    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }



}
