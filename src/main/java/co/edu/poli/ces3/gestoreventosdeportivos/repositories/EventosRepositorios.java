package co.edu.poli.ces3.gestoreventosdeportivos.repositories;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EquiposServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.*;

public class EventosRepositorios {

    private ArrayList<EventoDAO> eventos;
    private EquiposServices equiposServices;

    public EventosRepositorios() {
        eventos = new ArrayList<>();

        ArrayList<Integer> equiposParticipantes = new ArrayList<>();
        equiposParticipantes.add(0);
        equiposParticipantes.add(1);
        EventoDAO evento1 = new EventoDAO(
                0,
                "Final de la Copa",
                "2025-03-29",
                "Estadio Atanasio Girardot",
                "Futbol",
                equiposParticipantes,
                3000,
                2500,
                "Programado"
        );
        eventos.add(evento1);

        ArrayList<Integer> equiposParticipantes2 = new ArrayList<>();
        equiposParticipantes2.add(2);
        equiposParticipantes2.add(3);
        EventoDAO evento2 = new EventoDAO(
                1,
                "Final de la Copa Basket",
                "2025-04-10",
                "Estadio Atanasio Girardot",
                "Basket",
                equiposParticipantes2,
                500,
                100,
                "Cancelado"
        );
        eventos.add(evento2);
    }

    public void setEquiposServices(EquiposServices equiposServices) {
        this.equiposServices = equiposServices;
    }

    public ArrayList<EventoDAO> obtenerEventos(){
        return this.eventos;
    }

    public List<EventoDAO> obtenerEventosFiltrados(HttpServletRequest request, HttpServletResponse response){
        List<EventoDAO> eventosFiltrados = new ArrayList<>(this.eventos);

        String deporteParam = request.getParameter("deporte");
        String estadoParam = request.getParameter("estado");
        String fechaInicioParam = request.getParameter("fechaInicio");
        String fechaFinParam = request.getParameter("fechaFin");

        if (deporteParam != null && !deporteParam.isEmpty()) {
            eventosFiltrados.removeIf(e -> !e.getDeporte().equalsIgnoreCase(deporteParam));
        }

        if (estadoParam != null && !estadoParam.isEmpty()) {
            eventosFiltrados.removeIf(e ->  !e.getEstado().equalsIgnoreCase(estadoParam));
        }

        LocalDate fechaInicioTmp = null;
        LocalDate fechaFinTmp = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            if (fechaInicioParam != null && !fechaInicioParam.isEmpty()) {
                fechaInicioTmp = LocalDate.parse(fechaInicioParam, formatter);
            }
            if (fechaFinParam != null && !fechaFinParam.isEmpty()) {
                fechaFinTmp = LocalDate.parse(fechaFinParam, formatter);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
        }

        final LocalDate fechaInicio = fechaInicioTmp;
        final LocalDate fechaFin = fechaFinTmp;

        if (fechaInicio != null) {
            eventosFiltrados.removeIf(e -> {
                LocalDate fechaEvento;
                try {
                    fechaEvento = LocalDate.parse(e.getFecha(), formatter);
                } catch (Exception ex) {
                    return true;
                }
                return fechaEvento.isBefore(fechaInicio);
            });
        }

        if (fechaFin != null) {
            eventosFiltrados.removeIf(e -> {
                LocalDate fechaEvento;
                try {
                    fechaEvento = LocalDate.parse(e.getFecha(), formatter);
                } catch (Exception ex) {
                    return true;
                }
                return fechaEvento.isAfter(fechaFin);
            });
        }

        return eventosFiltrados;
    }

    public EventoDAO obtenerEventoPorId(int id){
        for(int i = 0; i < eventos.size(); i++){
            EventoDAO evento = eventos.get(i);
            if(evento.getId() == id){
                return evento;
            }
        }
        return null;
    }

    public List<Map<String, Object>> obtenerEquiposEventos(ArrayList<EventoDAO> eventos){
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (EventoDAO evento : eventos) {
            Map<String, Object> eventoMap = new LinkedHashMap<>();
            eventoMap.put("id", evento.getId());
            eventoMap.put("nombre", evento.getNombre());
            eventoMap.put("fecha", evento.getFecha());
            eventoMap.put("lugar", evento.getLugar());
            eventoMap.put("deporte", evento.getDeporte());
            eventoMap.put("capacidad", evento.getCapacidad());
            eventoMap.put("entradasVendidas", evento.getEntradasVendidas());
            eventoMap.put("estado", evento.getEstado());

            List<String> nombresEquipos = new ArrayList<>();
            for (Integer idequipo : evento.getEquiposParticipantes()) {
                EquipoDAO equipo = equiposServices.obtenerEquipoPorId(idequipo);
                if (equipo != null) {
                    nombresEquipos.add(equipo.getNombre());
                }
            }

            eventoMap.put("equiposParticipantes", nombresEquipos);

            respuesta.add(eventoMap);
        }
        return respuesta;
    }

    public String validarEquiposParticipantes(ArrayList<Integer> equiposParticipantes, String deporteEvento) {

        int contadorEquiposValidos = 0;

        if (equiposParticipantes == null || equiposParticipantes.size() < 2) {
            return "Debes ingresar al menos 2 equipos participantes.";
        }

        for (Integer idequipo : equiposParticipantes) {
            EquipoDAO equipo = equiposServices.obtenerEquipoPorId(idequipo);

            if (equipo == null) {
                return "No existe el equipo con ID: " + idequipo;
            }

            if (equipo.getDeporte().equalsIgnoreCase(deporteEvento)) {
                contadorEquiposValidos++;
            }
        }

        if (contadorEquiposValidos < 2) {
            return "El evento debe tener al menos 2 equipos participantes del mismo deporte";
        }

        return "";
    }

    public Map<String, Object> guardarEvento(EventoDAO evento){
        Map<String, Object> resultado = new HashMap<>();

        if (evento.getEntradasVendidas() > evento.getCapacidad()) {
            resultado.put("success", false);
            resultado.put("message", "Las entradas vendidas no pueden ser mayor que la capacidad");
            return resultado;
        }

        String validacion = validarEquiposParticipantes(evento.getEquiposParticipantes(), evento.getDeporte());

        if (!validacion.isEmpty()) {
            resultado.put("success", false);
            resultado.put("message", validacion);
            return resultado;
        }

        this.eventos.add(evento);
        resultado.put("success", true);
        resultado.put("message", "Evento guardado.");

        return resultado;
    }

    public Map<String, Object> actualizarEvento(EventoDAO eventoActualizar, int idEventoAct){

        Map<String, Object> resultado = new HashMap<>();

        EventoDAO eventoExistente = this.obtenerEventoPorId(idEventoAct);

        if (eventoExistente == null) {
            resultado.put("success", false);
            resultado.put("message", "No existe el evento con ID: " + idEventoAct);
            return resultado;
        }

        String validacion = validarEquiposParticipantes(eventoActualizar.getEquiposParticipantes(), eventoActualizar.getDeporte());

        if (!validacion.isEmpty()) {
            resultado.put("success", false);
            resultado.put("message", validacion);
            return resultado;
        }

        if (eventoActualizar.getCapacidad() < eventoExistente.getEntradasVendidas()) {
            resultado.put("success", false);
            resultado.put("message", "Ya hay " + eventoExistente.getEntradasVendidas() + " entradas vendidas, la capacidad a modificar no puede ser menor.");
            return resultado;
        }

        // Reemplazar o asignar en la misma referencia
        //eventos.set(i, eventoActualizar);
        eventoExistente.setNombre(eventoActualizar.getNombre());
        eventoExistente.setFecha(eventoActualizar.getFecha());
        eventoExistente.setLugar(eventoActualizar.getLugar());
        eventoExistente.setDeporte(eventoActualizar.getDeporte());
        eventoExistente.setCapacidad(eventoActualizar.getCapacidad());
        eventoExistente.setEquiposParticipantes(eventoActualizar.getEquiposParticipantes());

        resultado.put("success", true);
        resultado.put("message", "Evento actualizado correctamente.");
        return resultado;
    }

}
