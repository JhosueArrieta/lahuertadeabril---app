package com.example.lahuertadeabril;

public class Product2Response {
    private String nombre;
    private String imagen_url;
    private String origen;
    private float precio;

    public String getName() {
        return nombre;
    }
    public void setName(String name) {
        this.nombre = name;
    }

    public String getImage() {
        return imagen_url;
    }

    public void setImage(String image) {
        this.imagen_url = image;
    }

    public String getOriginProduct() {
        return origen;
    }

    public void setOriginProduct(String originProduct) {
        this.origen = originProduct;
    }

    public float getPriceProduct() {
        return precio;
    }

    public void setPriceProduct(float priceProduct) {
        this.precio = priceProduct;
    }






}
