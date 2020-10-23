package com.ecommerce.microcommerce.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
//@JsonFilter("monFiltreDynamique")
public class Product {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "Product ID", name = "id", required = true, value = "1")
    private int id;

    @Length(min=3, max=20, message = "Nom trop long ou trop court. Et oui messages sont plus stylés que ceux de Spring")
    @ApiModelProperty(notes = "Product name", name = "nom", required = true, value = "Product Name")
    private String nom;

    @Min(value = 1, message = "Le prix doit être supérieur à 0")
    @ApiModelProperty(notes = "Product Price", name = "prix", required = true, value = "250")
    private int prix;

    //information que nous ne souhaitons pas exposer
    @Min(value = 1, message = "ProduitGratuitException")
    @ApiModelProperty(notes = "Product Resell Price", name = "prixAchat", required = true, value = "125")
    private int prixAchat;

    //constructeur par défaut
    public Product() {
    }

    //constructeur pour nos tests
    public Product(int id, String nom, int prix, int prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(int prixAchat) {
        this.prixAchat = prixAchat;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
