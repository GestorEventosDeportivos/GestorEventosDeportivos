package co.edu.poli.ces3.gestoreventosdeportivos.servlets;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EquiposServices;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EventoServices;
import co.edu.poli.ces3.gestoreventosdeportivos.services.JugadoresServices;
import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "EstadisticaServlet", value = "/estadisticas")
public class EstadisticaServlet extends HttpServlet {

    private EventoServices eventoServices;
    private EquiposServices equiposServices;

    private Gson gsonRepuesta;

    @Override
    public void init() throws ServletException {
        this.gsonRepuesta = new Gson();
        ServletContext context = getServletContext();
        eventoServices = (EventoServices) context.getAttribute("eventosService");
        equiposServices = (EquiposServices) context.getAttribute("equiposService");
        System.out.println("Estadisticas inicializadas");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<EventoDAO> eventos = eventoServices.obtenerEventos();
        List<EquipoDAO> equipos = equiposServices.obtenerEquipos();

        Map<String, Integer> eventosPorDeporte = new HashMap<>();
        for (EventoDAO evento : eventos) {
            String deporte = evento.getDeporte();
            eventosPorDeporte.put(deporte, eventosPorDeporte.getOrDefault(deporte, 0) + 1);
        }

        double promedioJugadoresPorEquipo = 0.0;
        if (!equipos.isEmpty()) {
            int totalJugadores = 0;
            for (EquipoDAO equipo : equipos) {
                if (equipo.getJugadores() != null) {
                    totalJugadores += equipo.getJugadores().size();
                }
            }
            promedioJugadoresPorEquipo = (double) totalJugadores / equipos.size();
        }

        Map<Integer, Integer> eventosProgramadosPorEquipo = new HashMap<>();
        for (EventoDAO evento : eventos) {
            if ("Programado".equalsIgnoreCase(evento.getEstado())) {
                List<Integer> equiposParticipantes = evento.getEquiposParticipantes();
                if (equiposParticipantes != null) {
                    for (Integer equipoId : equiposParticipantes) {
                        eventosProgramadosPorEquipo.put(equipoId,
                                eventosProgramadosPorEquipo.getOrDefault(equipoId, 0) + 1);
                    }
                }
            }
        }
        int maxEventos = 0;
        for (Integer count : eventosProgramadosPorEquipo.values()) {
            if (count > maxEventos) {
                maxEventos = count;
            }
        }
        List<Map<String, Object>> equiposConMasEventos = new ArrayList<>();
        for (EquipoDAO equipo : equipos) {
            int count = eventosProgramadosPorEquipo.getOrDefault(equipo.getId(), 0);
            if (count == maxEventos && count > 0) {
                Map<String, Object> equipoInfo = new HashMap<>();
                equipoInfo.put("id", equipo.getId());
                equipoInfo.put("nombre", equipo.getNombre());
                equipoInfo.put("eventosProgramados", count);
                equiposConMasEventos.add(equipoInfo);
            }
        }

        List<Map<String, Object>> porcentajeOcupacionEventos = new ArrayList<>();
        for (EventoDAO evento : eventos) {
            int capacidad = evento.getCapacidad();
            int vendidas = evento.getEntradasVendidas();
            double porcentaje = 0;
            if (capacidad > 0) {
                porcentaje = ((double) vendidas / capacidad) * 100;
            }
            Map<String, Object> eventoInfo = new HashMap<>();
            eventoInfo.put("id", evento.getId());
            eventoInfo.put("nombre", evento.getNombre());
            eventoInfo.put("porcentajeOcupacion", porcentaje);
            porcentajeOcupacionEventos.add(eventoInfo);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("cantidadEventosPorDeporte", eventosPorDeporte);
        result.put("promedioJugadoresPorEquipo", promedioJugadoresPorEquipo);
        result.put("equiposConMasEventosProgramados", equiposConMasEventos);
        result.put("porcentajeOcupacionEventos", porcentajeOcupacionEventos);

        Respuesta respuesta = new Respuesta("", result);
        String jsonRespuesta = gsonRepuesta.toJson(result);
        PrintWriter out = response.getWriter();

        out.print(jsonRespuesta);
        out.flush();
    }

}
