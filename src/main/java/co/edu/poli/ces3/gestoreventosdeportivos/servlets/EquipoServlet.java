package co.edu.poli.ces3.gestoreventosdeportivos.servlets;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EquiposRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EquiposServices;
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

import co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta;

@WebServlet(name="EquipoServlet", value = "/equipos")
public class EquipoServlet extends HttpServlet {

    private EquiposServices equiposServices;

    private Gson gsonRepuesta;

    public void init() {
        this.gsonRepuesta = new Gson();
        ServletContext context = getServletContext();
        this.equiposServices = (EquiposServices) context.getAttribute("equiposService");
        System.out.println("Equipos inicializados");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");

        int page = 1;
        int size = 5;

        if(pageParam != null && !pageParam.isEmpty()){
            page = Integer.parseInt(pageParam);
        }

        if(sizeParam != null && !sizeParam.isEmpty()){
            size = Integer.parseInt(sizeParam);
        }

        PrintWriter out = response.getWriter();

        ArrayList<EquipoDAO> equiposPaginados = equiposServices.obtenerEquiposPorRango(page, size);

        Respuesta respuesta = new Respuesta("", equiposServices.obtenerInformacionJugadores(equiposPaginados));

        String jsonRespuesta = gsonRepuesta.toJson(respuesta);

        out.print(jsonRespuesta);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter out = response.getWriter();
        BufferedReader reader = request.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        EquipoDAO equipoBody = gsonRepuesta.fromJson(jsonBody.toString(), EquipoDAO.class);

        String logo = equipoBody.getLogo();
        String nombre = equipoBody.getNombre();
        String ciudad = equipoBody.getCiudad();
        String deporte = equipoBody.getDeporte();
        String fechaFundacion = equipoBody.getFechaFundacion();
        ArrayList<Integer> jugadores = equipoBody.getJugadores();

        if (logo == null || nombre == null || ciudad == null || deporte == null || fechaFundacion == null || jugadores.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son obligatorios.");
            return;
        }

        int equiposSize = this.equiposServices.obtenerEquipos().size();

        EquipoDAO nuevoEquipo = new EquipoDAO(equiposSize, nombre, deporte, ciudad, fechaFundacion, logo, jugadores);

        Map<String, Object> resultado = this.equiposServices.guardarEquipo(nuevoEquipo);
        boolean success = (Boolean) resultado.get("success");
        String message = (String) resultado.get("message");

        if (success) {
            response.setStatus(HttpServletResponse.SC_CREATED); // 201
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
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

        String equipoIdParam = request.getParameter("equipoId");

        if (equipoIdParam == null || equipoIdParam.isEmpty()) {
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

        EquipoDAO equipoBody = gsonRepuesta.fromJson(jsonBody.toString(), EquipoDAO.class);

        String logo = equipoBody.getLogo();
        String nombre = equipoBody.getNombre();
        String ciudad = equipoBody.getCiudad();
        String deporte = equipoBody.getDeporte();
        String fechaFundacion = equipoBody.getFechaFundacion();
        ArrayList<Integer> jugadores = equipoBody.getJugadores();

        if (logo == null || nombre == null || ciudad == null || deporte == null || fechaFundacion == null || jugadores.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Respuesta resp = new Respuesta("Todos los campos obligatorios deben enviarse y ser válidos.", null);
            out.print(gsonRepuesta.toJson(resp));
            out.flush();
            return;
        }

        Map<String, Object> result = this.equiposServices.actualizarEquipo(equipoBody, Integer.parseInt(equipoIdParam));
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
