package co.edu.poli.ces3.gestoreventosdeportivos.servlets;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EquiposRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.JugadoresRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EquiposServices;
import co.edu.poli.ces3.gestoreventosdeportivos.services.JugadoresServices;

import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name="JugadorServlet", value = "/jugadores/*" )//urlPatterns = { "/jugadores/*" })
public class JugadorServlet extends HttpServlet {

    private JugadoresServices jugadoresServices;
    private EquiposServices equiposServices;

    private Gson gsonRepuesta;

    public void init() {
        this.gsonRepuesta = new Gson();
        ServletContext context = getServletContext();

        this.jugadoresServices = (JugadoresServices) context.getAttribute("jugadoresService");
        this.equiposServices = (EquiposServices) context.getAttribute("equiposService");

        System.out.println("Jugadores inicializados");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/transferir")) {
            String jugadorIdParam = request.getParameter("jugadorId");
            String equipoDestino = request.getParameter("equipoDestino");

            if (jugadorIdParam == null || equipoDestino == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son obligatorios.");
            }

            Map<String, Object> result = this.jugadoresServices.transferirJugador(Integer.parseInt(jugadorIdParam), Integer.parseInt(equipoDestino));
            boolean success = (Boolean) result.get("success");
            String message = (String) result.get("message");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if(success){
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            Respuesta respuesta = new Respuesta(message, null);
            String jsonRespuesta = gsonRepuesta.toJson(respuesta);

            out.print(jsonRespuesta);
            out.flush();
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ArrayList<JugadorDAO> jugadores = jugadoresServices.obtenerJugadores();

            List<Map<String, Object>> listaRespuesta = new ArrayList<>();

            for (JugadorDAO jugador : jugadores) {
                Map<String, Object> jugadorMap = new LinkedHashMap<>();
                jugadorMap.put("id", jugador.getId());
                jugadorMap.put("nombre", jugador.getNombre());
                jugadorMap.put("apellido", jugador.getApellido());
                jugadorMap.put("fechaNacimiento", jugador.getFechaNacimiento());
                jugadorMap.put("nacionalidad", jugador.getNacionalidad());
                jugadorMap.put("posicion", jugador.getPosicion());
                jugadorMap.put("numero", jugador.getNumero());
                jugadorMap.put("equipoId", jugador.getEquipoId());
                jugadorMap.put("estadoActivo", jugador.isEstadoActivo());
                EquipoDAO equipo = equiposServices.obtenerEquipoPorId(jugador.getEquipoId());
                String nombreEquipo = (equipo != null) ? equipo.getNombre() : null;
                jugadorMap.put("nombre_equipo", nombreEquipo);
                listaRespuesta.add(jugadorMap);
            }

            Respuesta respuesta = new Respuesta("", listaRespuesta);

            String jsonRespuesta = gsonRepuesta.toJson(respuesta);

            out.print(jsonRespuesta);
            out.flush();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        BufferedReader reader = request.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

        JugadorDAO jugadorBody = gsonRepuesta.fromJson(jsonBody.toString(), JugadorDAO.class);

        if (jugadorBody.getNombre() == null || jugadorBody.getApellido() == null ||
                jugadorBody.getFechaNacimiento() == null || jugadorBody.getNacionalidad() == null ||
                    jugadorBody.getPosicion() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son obligatorios.");
            return;
        }

        String nombre = jugadorBody.getNombre();
        String apellido = jugadorBody.getApellido();
        String fechaNacimiento = jugadorBody.getFechaNacimiento();
        String nacionalidad = jugadorBody.getNacionalidad();
        String posicion = jugadorBody.getPosicion();
        int numero = jugadorBody.getNumero();
        int equipoId = jugadorBody.getEquipoId();

        int jugadoresSize = this.jugadoresServices.obtenerJugadores().size();

        JugadorDAO nuevoJugador = new JugadorDAO(
                jugadoresSize,
                nombre,
                apellido,
                fechaNacimiento,
                nacionalidad,
                posicion,
                numero,
                equipoId,
                true
        );

        Map<String, Object> result = this.jugadoresServices.guardarJugador(nuevoJugador);
        boolean success = (Boolean) result.get("success");
        String message = (String) result.get("message");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if(success){
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        Respuesta respuesta = new Respuesta(message, null);
        String jsonRespuesta = gsonRepuesta.toJson(respuesta);

        out.print(jsonRespuesta);
        out.flush();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String jugadorIdParam = request.getParameter("jugadorId");

        if (jugadorIdParam == null || jugadorIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Respuesta resp = new Respuesta("Falta parámetro id", null);
            out.print(gsonRepuesta.toJson(resp));
            out.flush();
            return;
        }

        BufferedReader reader = request.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        JugadorDAO eventoBody = gsonRepuesta.fromJson(jsonBody.toString(), JugadorDAO.class);

        if (eventoBody.getNombre() == null || eventoBody.getApellido() == null ||
                eventoBody.getFechaNacimiento() == null || eventoBody.getNacionalidad() == null ||
                eventoBody.getPosicion() == null) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Respuesta resp = new Respuesta("Todos los campos obligatorios deben enviarse y ser válidos.", null);
            out.print(gsonRepuesta.toJson(resp));
            out.flush();
            return;
        }

        Map<String, Object> result = this.jugadoresServices.actualizarJugador(eventoBody, Integer.parseInt(jugadorIdParam));
        boolean success = (Boolean) result.get("success");
        String message = (String) result.get("message");

        if(success){
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        Respuesta respuesta = new Respuesta(message, null);
        String jsonRespuesta = gsonRepuesta.toJson(respuesta);

        out.print(jsonRespuesta);
        out.flush();
    }

}
