package com.example.lahuertadeabril;

public class Product1Item {
    private String id;
    private String nombre;
    private String imagenUrl;

    public Product1Item(String id, String nombre, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }
}
