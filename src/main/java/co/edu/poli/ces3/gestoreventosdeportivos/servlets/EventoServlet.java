package co.edu.poli.ces3.gestoreventosdeportivos.servlets;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EventoServices;

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
import java.util.List;
import java.util.Map;

@WebServlet(name="EventoServlet",  value = "/eventos/*") //urlPatterns = { "/eventos" })
public class EventoServlet extends HttpServlet {

    private EventoServices eventoServices;

    private Gson gsonRepuesta;

    public void init() {
        this.gsonRepuesta = new Gson();

        ServletContext context = getServletContext();
        this.eventoServices = (EventoServices) context.getAttribute("eventosService");

        System.out.println("Eventos inicializados");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        List<EventoDAO> eventosFiltrados = eventoServices.obtenerEventosFiltrados(request, response);

        Respuesta respuesta = new Respuesta("", eventoServices.obtenerEquiposEventos(new ArrayList<>(eventosFiltrados)));

        String jsonRespuesta = gsonRepuesta.toJson(respuesta);

        out.print(jsonRespuesta);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        BufferedReader reader = request.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        EventoDAO eventoBody = gsonRepuesta.fromJson(jsonBody.toString(), EventoDAO.class);

        if (eventoBody.getNombre() == null || eventoBody.getFecha() == null ||
                eventoBody.getLugar() == null || eventoBody.getDeporte() == null || eventoBody.getEquiposParticipantes().isEmpty() ||
                eventoBody.getCapacidad() <= 0 || eventoBody.getEntradasVendidas() <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Respuesta respuesta = new Respuesta("Todos los campos son obligatorios.", null);
            String jsonRespuesta = gsonRepuesta.toJson(respuesta);

            out.print(jsonRespuesta);
            out.flush();
            return;
        }

        String nombre = eventoBody.getNombre();
        String fecha = eventoBody.getFecha();
        String lugar = eventoBody.getLugar();
        String deporte = eventoBody.getDeporte();
        int capacidad = eventoBody.getCapacidad();
        int entradasVendidas = eventoBody.getEntradasVendidas();
        String estado = eventoBody.getEstado();
        ArrayList<Integer> equiposParticipantes = eventoBody.getEquiposParticipantes();

        int eventosSize = this.eventoServices.obtenerEventos().size();

        EventoDAO nuevoEvento = new EventoDAO(
                eventosSize,
                nombre,
                fecha,
                lugar,
                deporte,
                equiposParticipantes,
                capacidad,
                entradasVendidas,
                estado
        );

        Map<String, Object> result = this.eventoServices.guardarEvento(nuevoEvento);
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

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();

        String eventoIdParam = request.getParameter("eventoId");

        if (eventoIdParam == null || eventoIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Respuesta resp = new Respuesta("Falta parámetro id", null);
            out.print(gsonRepuesta.toJson(resp));
            out.flush();
            return;
        }

        if (pathInfo != null && pathInfo.equals("/vender-entradas")) {

            String eventoCantidadParam = request.getParameter("cantidad");

            if (eventoCantidadParam == null || eventoCantidadParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Respuesta respuesta = new Respuesta("Falta el parámetro cantidad.", null);
                String jsonRespuesta = gsonRepuesta.toJson(respuesta);

                out.print(jsonRespuesta);
                out.flush();
                return;
            }

            Map<String, Object> result = this.eventoServices.venderEntradas(Integer.parseInt(eventoIdParam), Integer.parseInt(eventoCantidadParam));
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
            return;
        }

        if (pathInfo != null && pathInfo.equals("/actualizar-estado")) {
            String eventoEstadoParam = request.getParameter("estado");

            if (eventoEstadoParam == null || eventoEstadoParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Respuesta respuesta = new Respuesta("Falta el parámetro estado.", null);
                String jsonRespuesta = gsonRepuesta.toJson(respuesta);

                out.print(jsonRespuesta);
                out.flush();
                return;
            }

            Map<String, Object> result = this.eventoServices.actualizarEstado(Integer.parseInt(eventoIdParam), eventoEstadoParam);
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
            return;
        }

        BufferedReader reader = request.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        EventoDAO eventoBody = gsonRepuesta.fromJson(jsonBody.toString(), EventoDAO.class);

        if (eventoBody.getNombre() == null || eventoBody.getFecha() == null ||
                eventoBody.getLugar() == null || eventoBody.getDeporte() == null ||
                eventoBody.getEquiposParticipantes() == null ||
                eventoBody.getEquiposParticipantes().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Respuesta resp = new Respuesta("Todos los campos obligatorios deben enviarse y ser válidos.", null);
            out.print(gsonRepuesta.toJson(resp));
            out.flush();
            return;
        }

        Map<String, Object> result = this.eventoServices.actualizarEvento(eventoBody, Integer.parseInt(eventoIdParam));
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
