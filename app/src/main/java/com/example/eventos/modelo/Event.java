package com.example.eventos.modelo;


public class Event {

    private String id;
    private String name;
    private String direccion;
    private String fecha;
    private String hora;
    private boolean status;

    public Event() {
    }

    public Event(String id, String name, String direccion, String fecha, String hora, boolean status) {
        this.id = id;
        this.name = name;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora = hora;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
