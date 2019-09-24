package com.example.vsion_lite.Teacher;

public class InfCard {
    private String tname;
    private String temail;
    private String location;
    private String availability;
    private int id;
    private int img, sexo;

    public InfCard(){

    }

    public InfCard(String tname, String temail, int id, int img, int sexo, String availability){
        this.tname = tname;
        this.temail = temail;
        this.location = location;
        this.availability = availability;
        this.id = id;
        this.img = img;
        this.sexo = sexo;
    }

    public String getName(){
        return tname;
    }

    public String getEmail(){
        return temail;
    }

    public String getDispon(){
        return availability;
    }


    public int getId(){
        return id;
    }

    public int getImg(){
        return img;
    }

    public int getSexo(){
        return sexo;
    }
}
