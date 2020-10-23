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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

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
    @ApiOperation(value = "Retourne une Map de tous les produits", response = Iterable.class, tags = "listeProduits")
    @GetMapping(value = "/Produits")
    public Map<Integer, Product> listeProduits() {
        return productDB;
    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "Retourne un produit grâce à son ID", response = Product.class, tags = "afficherUnProduit")
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
    @ApiOperation(value = "Ajoute un produit", tags = "ajouterProduit")
    @PostMapping(value = "/Produits", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ajouterProduit(@Valid @RequestBody Product product) {
        if(productDB.containsKey(product.getId())) {
            return new ResponseEntity<>("Le produit ne peux pas être ajouté car la clé " + product.getId() + " existe déjà", HttpStatus.CONFLICT);
        }
        productDB.put(product.getId(),product);
        return new ResponseEntity<>("Le produit a été ajouté avec succès", HttpStatus.OK);
    }

    // supprimer un produit
    @ApiOperation(value = "Supprime un produit", tags = "supprimerProduit")
    @DeleteMapping(value = "/deleteProduct/{productId}")
    public ResponseEntity<String> supprimerProduit(@PathVariable int productId) {
        System.out.println("Deleting product with ID " + productId);
        if (productDB.get(productId) == null){
            return new ResponseEntity<>("Le produit " + productId + " n'existe pas", HttpStatus.NOT_FOUND);
        }
        productDB.remove(productId);
        return new ResponseEntity<>("Le produit " + productId + " a été supprimé", HttpStatus.OK);
    }
    /*
    **************************************
    TODO LA MISE A JOUR!!!!!!!!!!!!!!
    **************************************
    */
    // Mettre à jour un produit
    @ApiOperation(value = "Mets à jour un produit", tags = "updateProduit")
    @PutMapping(value = "/updateProduct/{productid}")
    public ResponseEntity<String> updateProduit(@RequestBody Product product, @PathVariable int productId) {
        System.out.println("Updating product with ID " + productId);
        Product productUpdated = productDB.get(productId);
        if (productUpdated == null){
            return new ResponseEntity<>("Le produit " + productId + " n'existe pas", HttpStatus.NOT_FOUND);
        }
        product.setId(productId);
        return null;
    }

    @ApiOperation(value = "Retourne la différence entre le prix et le prixAchat d'un produit à partir de son ID", tags = "calculerMargeProduit")
    @GetMapping(value = "/calculerMargeProduit/{productId}")
    public ResponseEntity<Integer> calculerMargeProduit(@PathVariable int productId){
        System.out.println("Getting price difference of product with ID " + productId);
        Product product = productDB.get(productId);

        if (product == null){
            return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
        }

        int priceDifference = product.getPrix() - product.getPrixAchat();
        return new ResponseEntity<>(priceDifference, HttpStatus.OK);
    }

    @ApiOperation(value = "Retourne une Map de tous les produits ranger alphabétiquement", tags = "trierProduitsParOrdreAlphabetique")
    @GetMapping(value = "/ProduitsABC")
    public List<Product> trierProduitsParOrdreAlphabetique() {
        System.out.println("Getting all products and sort them alphabetically");
        List<Product> productList = new ArrayList(productDB.values());
        productList.sort(Comparator.comparing(Product::getNom));

        return productList;
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }



}
