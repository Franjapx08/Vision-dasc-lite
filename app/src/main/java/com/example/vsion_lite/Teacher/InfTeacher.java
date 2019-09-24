package com.example.vsion_lite.Teacher;

public class InfTeacher {
    private String tname;
    private String temail;
    private String location;
    private String availability;
    private String id;
    private int img, sexo;

    public InfTeacher(String tname, String temail, String id, int img, int sexo, String availability){
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


    public String getId(){
        return id;
    }

    public int getImg(){
        return img;
    }

    public int getSexo(){
        return sexo;
    }
}
