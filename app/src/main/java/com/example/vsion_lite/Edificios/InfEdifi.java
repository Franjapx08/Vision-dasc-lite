package com.example.vsion_lite.Edificios;

public class InfEdifi {
    private String tname;
    private String ubicacion;
    private int id;
    private int img;

    public InfEdifi(String tname, String ubicacion, int id, int img){
        this.tname = tname;
        this.ubicacion = ubicacion;
        this.id = id;
        this.img = img;
    }

    public String getName(){
        return tname;
    }

    public String getUbicacion(){
        return ubicacion;
    }

    public int getId(){
        return id;
    }

    public int getImg(){
        return img;
    }

}
