package co.edu.poli.ces3.gestoreventosdeportivos.services;

import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EventosRepositorios;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventoServices {

    private EventosRepositorios eventosRepositorios;

    public EventoServices(EventosRepositorios eventosRepositorios) {
        this.eventosRepositorios = eventosRepositorios;
    }

    public ArrayList<EventoDAO> obtenerEventos(){
        return this.eventosRepositorios.obtenerEventos();
    }

    public List<EventoDAO> obtenerEventosFiltrados(HttpServletRequest request, HttpServletResponse response){
        return this.eventosRepositorios.obtenerEventosFiltrados(request, response);
    }

    public EventoDAO obtenerEventoPorId(int id){
        return this.eventosRepositorios.obtenerEventoPorId(id);
    }

    public List<Map<String, Object>> obtenerEquiposEventos (ArrayList<EventoDAO> eventos){
        return this.eventosRepositorios.obtenerEquiposEventos(eventos);
    }

    public Map<String, Object> guardarEvento(EventoDAO evento){
        return this.eventosRepositorios.guardarEvento(evento);
    }

    public Map<String, Object> venderEntradas(int eventoId, int cantidad){
        EventoDAO evento = this.obtenerEventoPorId(eventoId);

        Map<String, Object> resultado = new HashMap<>();

        if (evento == null) {
            resultado.put("success", false);
            resultado.put("message", "No existe el evento con ID: " + eventoId);
            return resultado;
        }

        int entradasDisponibles = evento.getCapacidad() - evento.getEntradasVendidas();

        if (entradasDisponibles < cantidad) {
            resultado.put("success", false);
            resultado.put("message", "No hay entradas suficientes.");
            return resultado;
        }

        evento.setEntradasVendidas(evento.getEntradasVendidas()+cantidad);
        resultado.put("success", true);
        resultado.put("message", "Entradas vendidas.");
        return resultado;
    }

    public Map<String, Object> actualizarEstado(int eventoId, String estado){
        EventoDAO evento = this.obtenerEventoPorId(eventoId);

        Map<String, Object> resultado = new HashMap<>();

        if (evento == null) {
            resultado.put("success", false);
            resultado.put("message", "No existe el evento con ID: " + eventoId);
            return resultado;
        }

        ArrayList<String> estadosValidos = new ArrayList<>();
        estadosValidos.add("Programado");
        estadosValidos.add("En curso");
        estadosValidos.add("Finalizado");
        estadosValidos.add("Cancelado");

        if (!estadosValidos.contains(estado)) {
            resultado.put("success", false);
            resultado.put("message", "Ingrese un estado válido (Programado, En curso, Finalizado, Cancelado).");
            return resultado;
        }

        evento.setEstado(estado);
        resultado.put("success", true);
        resultado.put("message", "Estado actualizado.");
        return resultado;
    }

    public Map<String, Object> actualizarEvento(EventoDAO eventoActualizar, int idEvento) {

        return this.eventosRepositorios.actualizarEvento(eventoActualizar, idEvento);
    }
}
