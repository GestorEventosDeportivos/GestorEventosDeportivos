package co.edu.poli.ces3.gestoreventosdeportivos.dao;

import java.util.ArrayList;

public class EventoDAO {

    private int id;

    private String nombre;

    private String fecha;

    private String lugar;

    private String deporte;

    private ArrayList<Integer> equiposParticipantes;

    private int capacidad;

    private int entradasVendidas;

    private String estado;

    public EventoDAO(int id, String nombre, String fecha, String lugar, String deporte, ArrayList<Integer> equiposParticipantes, int capacidad, int entradasVendidas, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.deporte = deporte;
        this.equiposParticipantes = equiposParticipantes;
        this.capacidad = capacidad;
        this.entradasVendidas = entradasVendidas;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public ArrayList<Integer> getEquiposParticipantes() {
        return equiposParticipantes;
    }

    public void setEquiposParticipantes(ArrayList<Integer> equiposParticipantes) {
        this.equiposParticipantes = equiposParticipantes;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getEntradasVendidas() {
        return entradasVendidas;
    }

    public void setEntradasVendidas(int entradasVendidas) {
        this.entradasVendidas = entradasVendidas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
