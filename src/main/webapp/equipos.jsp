<%@ page import="java.net.URL" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.lang.reflect.Type" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta" %>
<%@ page import="com.google.gson.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%

  Gson gson = new Gson();

  Type responseType = new TypeToken<Respuesta>() {}.getType();
  JsonArray equipos = new JsonArray();

  String servletEquiposURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/equipos";

  URL equiposUrl = new URL(servletEquiposURL);

  HttpURLConnection equiposCon = (HttpURLConnection) equiposUrl.openConnection();

  equiposCon.setRequestMethod("GET");

  BufferedReader equiposIn = new BufferedReader(new InputStreamReader(equiposCon.getInputStream()));

  String equiposInputLine;

  StringBuffer equiposResponse = new StringBuffer();

  while ((equiposInputLine = equiposIn.readLine()) != null) {
    equiposResponse.append(equiposInputLine);
  }
  equiposIn.close();

  Respuesta equiposRespuesta = gson.fromJson(equiposResponse.toString(), responseType);
  JsonElement dataElementEquipo = gson.toJsonTree(equiposRespuesta.getData());
  equipos = dataElementEquipo.getAsJsonArray();
  System.out.println(equipos);

  /*
  ArrayList<EquipoDAO> equipos = new ArrayList<>();
  String servletURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/equipos";
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
  Type equiposType = new TypeToken<ArrayList<EquipoDAO>>(){}.getType();
  equipos = gson.fromJson(gson.toJson(respuesta.getData()), equiposType);

   */
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
      <form action="http://localhost:8080/GestorEventosDeportivos_war_exploded/equipos" method="POST" class="flex flex-col gap-3">
        <input type="text" name="logo" placeholder="Url logo"
               class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none" />
        <input type="text" name="nombre" placeholder="Nombre"
               class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none"/>
        <input type="text" name="ciudad" placeholder="Ciudad"
               class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none"/>
        <input type="text" name="deporte" placeholder="Deporte"
                class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />
        <input type="text" name="fechaFundacion" placeholder="Fecha fundación"
               class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />
        <div class="mt-4 flex gap-3 justify-end">
          <button onclick="cerrarModal()" class="px-3 py-1 bg-gray-300 text-gray-500 rounded-lg">Cerrar</button>
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
              Logo
            </div>
          </td>
          <td>
            <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
              Nombre
            </div>
          </td>
          <td>
            <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
              Ciudad
            </div>
          </td>
          <td>
            <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
              Deporte
            </div>
          </td>
          <td>
            <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
              Fecha fundación
            </div>
          </td>
          <td>
            <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
              Jugadores
            </div>
          </td>
        </tr>
      </thead>
      <tbody id="bodyTablaEquipos">
      <%
        if (equipos == null || equipos.isEmpty()) {
      %>
      <tr>
        <td colspan="8">
          <div class="flex items-center justify-center py-3">
            <span class="py-1 px-3 bg-gray-300 text-lg text-gray-500 font-semibold rounded-full">
              No hay equipos disponibles
            </span>
          </div>
        </td>
      </tr>
      <%
      } else {
      %>
      <%
          for(JsonElement equipo : equipos){
            JsonObject equipoActual = equipo.getAsJsonObject();
            JsonArray jugadores = equipoActual.get("jugadores").getAsJsonArray();
      %>
      <tr class="border-b border-gray-200">
        <td class="flex justify-center items-center content-center"><img style="width:30px; align-items: center;" src="<%= equipoActual.get("logo").getAsString() %>" ></td>
        <td><%= equipoActual.get("nombre").getAsString() %></td>
        <td><%= equipoActual.get("ciudad").getAsString() %></td>
        <td><%= equipoActual.get("deporte").getAsString() %></td>
        <td><%= equipoActual.get("fechaFundacion").getAsString() %></td>
        <td class="flex flex-col gap-1 items-center px-3 py-2">
          <%
            for (JsonElement jugadorArr : jugadores) {
              JsonObject jugador = jugadorArr.getAsJsonObject();
          %>
          <div class="w-full bg-gray-50 p-1 rounded-lg">
            <p class="font-bold text-gray-700"><%= jugador.get("nombre").getAsString() + " " + jugador.get("apellido").getAsString() %> </p>
            <p>Numero:
              <span class="text-gray-700"><%= jugador.get("numero").getAsInt() %></span>
            </p>
            <p>Posicion:
              <span class="text-gray-700"><%= jugador.get("posicion").getAsString() %></span>
            </p>
          </div>
          <%
            }
          %>
        </td>
      </tr>
      <%
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