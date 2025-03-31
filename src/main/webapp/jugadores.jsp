<%@ page import="java.net.URL" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.gson.Gson, com.google.gson.reflect.TypeToken" %>
<%@ page import="java.lang.reflect.Type" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    ArrayList<JugadorDAO> jugadores = new ArrayList<>();
    String servletURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/jugadores";
    URL url = new URL(servletURL);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer responseData = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        responseData.append(inputLine);
    }
    in.close();

    Gson gson = new Gson();
    Type responseType = new TypeToken<Respuesta>() {}.getType();
    Respuesta respuesta = gson.fromJson(responseData.toString(), responseType);
    Type jugadoresType = new TypeToken<ArrayList<JugadorDAO>>(){}.getType();
    jugadores = gson.fromJson(gson.toJson(respuesta.getData()), jugadoresType);

    System.out.println(jugadores);
%>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>

    <style>

        :root {
            --color-gray-100: #D5DBDB;
            --color-gray-200: #B3B2AE;
            --color-gray-300: #91918F;
            --color-gray-400: #5D6363;
            --color-gray-500: #293737;

            --color-green-100: #6dc35a;
            --color-green-200: #5ab249;
            --color-green-300: #47a139;
            --color-green-400: #359028;
            --color-green-500: #227e17;
        }

        * {
            margin: 0;
        }

        #body {
            display: flex;
            flex-direction: column;
            height: 100vh;
        }
    </style>
    <script src=""></script>
</head>
<body id="body">
<div id="modal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
    <div class="bg-white p-6 rounded-lg shadow-lg w-96">
        <h2 class="text-xl font-semibold mb-4">Crear equipo</h2>
        <form action="http://localhost:8080/GestorEventosDeportivos_war_exploded/jugadores" method="POST" class="flex flex-col gap-3">
            <input type="text" name="nombre" placeholder="Nombre"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none"/>
            <input type="text" name="apellido" placeholder="Apellido"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none"/>
            <input type="text" name="fechaNacimiento" placeholder="Fecha nacimiento"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none"/>
            <input type="text" name="nacionalidad" placeholder="Nacionalidad"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />
            <input type="text" name="posicion" placeholder="Posición"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />
            <input type="text" name="numero" placeholder="Número"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />
            <input type="text" name="equipoId" placeholder="Equipo Id"
                   class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />
            <div class="mt-4 flex gap-3 justify-end">
                <button onclick="cerrarModal()" type="button" class="px-3 py-1 bg-gray-300 text-gray-500 rounded-lg">Cerrar</button>
                <button type="submit" class="px-3 py-1 bg-green-300 text-white rounded-md">Crear</button>
            </div>
        </form>
    </div>
</div>
<div class="w-full flex justify-between">
    <div class="flex p-3 gap-3">
        <a href="index.jsp">
            <p class="px-3 py-1 bg-gray-300 rounded-full text-sm text-gray-600">Eventos</p>
        </a>
        <a href="equipos.jsp">
            <p class="px-3 py-1 bg-gray-300 rounded-full text-sm text-gray-600">Equipos</p>
        </a>
        <a href="jugadores.jsp">
            <p class="px-3 py-1 bg-gray-300 rounded-full text-sm text-gray-600">Jugadores</p>
        </a>
    </div>
</div>
<div class="w-full flex justify-between p-3 border-b border-gray-400">
    <h1 class="text-lg font-semibold">Equipos</h1>
</div>
<div style="flex: 1;">
    <table style="width:100%;">
        <thead>
        <tr>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Nombre
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Apellido
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Fecha Nacimiento
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Nacionalidad
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Posición
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Numero
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Equipo
                </div>
            </td>
            <td>
                <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                    Estado
                </div>
            </td>
        </tr>
        </thead>
        <tbody id="bodyTablaEquipos">
        <%
            if (jugadores == null || jugadores.isEmpty()) {
        %>
        <tr>
            <td colspan="8">
                <div class="flex items-center justify-center py-3">
                        <span class="py-1 px-3 bg-gray-300 text-lg text-gray-500 font-semibold rounded-full">
                            No hay jugadores disponibles
                        </span>
                </div>
            </td>
        </tr>
        <%
        } else {
        %>
        <%
            if(jugadores != null && !jugadores.isEmpty()) {
                for(int i = 0; i < jugadores.size(); i++){
                    JugadorDAO jugadorActual = jugadores.get(i);
        %>
        <tr>
            <td><%= jugadorActual.getNombre() %></td>
            <td><%= jugadorActual.getApellido() %></td>
            <td><%= jugadorActual.getFechaNacimiento() %></td>
            <td><%= jugadorActual.getNacionalidad() %></td>
            <td><%= jugadorActual.getPosicion() %></td>
            <td><%= jugadorActual.getNumero() %></td>
            <td><%= jugadorActual.getEquipoId() %></td>
            <td><%= jugadorActual.isEstadoActivo() ? "Activo" : "Inactivo" %></td>
        </tr>
        <%
                    }
                }
            }
        %>
        </tbody>
    </table>
</div>

<script>
    function abrirModal() {
        document.getElementById("modal").classList.remove("hidden");
    }

    function cerrarModal() {
        document.getElementById("modal").classList.add("hidden");
    }
</script>

</body>
</html>