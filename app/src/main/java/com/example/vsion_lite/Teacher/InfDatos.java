package com.example.vsion_lite.Teacher;

public class InfDatos {

    public String  materia, grupo, semestre, carrera , hora, ubicacion;
    public String  id_materia, id_maestros;
    int horaInicio, horaFinal, dia;


    public InfDatos(String id_materia, String materia, String hora ,int horaInicio, int horaFinal, int dia,  String carrera, String id_maestros, String ubicacion){
        this.id_materia = id_materia;
        this.materia = materia;
        this.hora = hora;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
        this.dia = dia;
        this.carrera = carrera;
        this.id_maestros = id_maestros;
        this.ubicacion = ubicacion;
    }

    public String getIdMateria(){
        return id_materia;
    }

    public String getMateria(){
        return materia;
    }

    public String getHora(){
        return hora;
    }

    public int getHorainicio(){
        return horaInicio;
    }

    public int getHorafinal(){
        return horaFinal;
    }

    public int getDia(){
        return dia;
    }


    public String getCarrera(){
        return carrera;
    }

    public String getIdMaestro(){
        return id_maestros;
    }

    public String getUbicacion(){
        return ubicacion;
    }



}
