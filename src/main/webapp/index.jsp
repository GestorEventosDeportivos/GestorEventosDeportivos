<%@ page import="java.net.URL" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO" %>
<%@ page import="co.edu.poli.ces3.gestoreventosdeportivos.middleware.Respuesta" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.gson.reflect.TypeToken" %>
<%@ page import="java.lang.reflect.Type" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%
    Gson gson = new Gson();

    Type responseType = new TypeToken<Respuesta>() {}.getType();
    JsonArray eventos = new JsonArray();
    JsonArray equipos = new JsonArray();

    String servletEventosURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/eventos";
    String servletEquiposURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/equipos";

    URL eventosUrl = new URL(servletEventosURL);
    URL equiposUrl = new URL(servletEquiposURL);

    HttpURLConnection eventosCon = (HttpURLConnection) eventosUrl.openConnection();
    HttpURLConnection equiposCon = (HttpURLConnection) equiposUrl.openConnection();

    eventosCon.setRequestMethod("GET");
    equiposCon.setRequestMethod("GET");

    BufferedReader eventosIn = new BufferedReader(new InputStreamReader(eventosCon.getInputStream()));
    BufferedReader equiposIn = new BufferedReader(new InputStreamReader(equiposCon.getInputStream()));


    String eventosInputLine;
    String equiposInputLine;

    StringBuffer eventosResponse = new StringBuffer();
    StringBuffer equiposResponse = new StringBuffer();

    while ((eventosInputLine = eventosIn.readLine()) != null) {
        eventosResponse.append(eventosInputLine);
    }
    eventosIn.close();

    while ((equiposInputLine = equiposIn.readLine()) != null) {
        equiposResponse.append(equiposInputLine);
    }
    equiposIn.close();

    Respuesta eventosRespuesta = gson.fromJson(eventosResponse.toString(), responseType);
    JsonElement dataElementEvento = gson.toJsonTree(eventosRespuesta.getData());
    eventos = dataElementEvento.getAsJsonArray();

    Respuesta equiposRespuesta = gson.fromJson(equiposResponse.toString(), responseType);
    JsonElement dataElementEquipo = gson.toJsonTree(equiposRespuesta.getData());
    equipos = dataElementEquipo.getAsJsonArray();
    //System.out.println(equipos);

    /*
    Type responseType = new TypeToken<Respuesta>() {}.getType();
    Type eventosType = new TypeToken<ArrayList<EventoDAO>>(){}.getType();
    Type equiposType = new TypeToken<ArrayList<EquipoDAO>>(){}.getType();
    Type eventMapListType = new TypeToken<List<Map<String, Object>>>() {}.getType();

    //ArrayList<EventoDAO> eventos = new ArrayList<>();
    ArrayList<EquipoDAO> equipos = new ArrayList<>();
    ArrayList<Integer> equiposSeleccionados = new ArrayList<>();

   // String servletEventosURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/eventos";
    String servletEquiposURL = "http://localhost:8080/GestorEventosDeportivos_war_exploded/equipos";

    //URL eventosUrl = new URL(servletEventosURL);
    URL equiposUrl = new URL(servletEquiposURL);

    //HttpURLConnection eventosCon = (HttpURLConnection) eventosUrl.openConnection();
    HttpURLConnection equiposCon = (HttpURLConnection) equiposUrl.openConnection();

    //eventosCon.setRequestMethod("GET");
    equiposCon.setRequestMethod("GET");

    //BufferedReader eventosIn = new BufferedReader(new InputStreamReader(eventosCon.getInputStream()));
    BufferedReader equiposIn = new BufferedReader(new InputStreamReader(equiposCon.getInputStream()));

    String eventosInputLine;
    String equiposInputLine;
    StringBuffer eventosResponse = new StringBuffer();
    StringBuffer equiposResponse = new StringBuffer();

    while ((eventosInputLine = eventosIn.readLine()) != null) {
        eventosResponse.append(eventosInputLine);
    }
    eventosIn.close();

    while ((equiposInputLine = equiposIn.readLine()) != null) {
        equiposResponse.append(equiposInputLine);
    }
    equiposIn.close();


    Respuesta eventosRespuesta = gson.fromJson(eventosResponse.toString(), responseType);
    List<Map<String, Object>> eventosMapList = gson.fromJson(gson.toJson(eventosRespuesta.getData()), eventMapListType);
    Respuesta equiposRespuesta = gson.fromJson(equiposResponse.toString(), responseType);

    eventos = gson.fromJson(gson.toJson(eventosRespuesta.getData()), eventosType);
    System.out.println("eventos: " + eventos);
    equipos = gson.fromJson(gson.toJson(equiposRespuesta.getData()), equiposType);

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
        }

        * {
            margin: 0;
        }

        #body {
            display: flex;
            flex-direction: column;
            height: 100vh;
        }

        .equipo {
            background-color: #f3f3f3;
            transition: background-color 0.3s;
        }
        .equipo.selected {
            background-color: #a3d3f5; /* Cambia este color según tu preferencia */
        }
    </style>
    <script src=""></script>
</head>
<body id="body">

    <div id="modal" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
        <div class="bg-white p-6 rounded-lg shadow-lg w-96">
            <h2 id="modalTitulo" class="text-xl font-semibold mb-4">Crear evento</h2>
            <form id="eventoForm" class="flex flex-col gap-3" onsubmit="enviarEvento(event)">
                <input type="hidden" name="eventoId" />
                <input type="text" name="nombre" placeholder="Nombre"
                       class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none" required/>
                <input type="text" name="fecha" placeholder="fecha"
                       class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none" required/>
                <input type="text" name="lugar" placeholder="Lugar"
                       class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none" required/>
                <input type="text" name="deporte" placeholder="Deporte"
                       class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " required/>
                <input type="text" name="capacidad" placeholder="Capacidad"
                       class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " required/>
                <input type="text" id="entradasVendidas" name="entradasVendidas" placeholder="Entradas vendidas"
                       class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " required/>
                <select id="estado" name="estado" class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " required>
                    <option value="" disabled selected>Selecciona un estado</option>
                    <option value="Programado">Programado</option>
                    <option value="En curso">En curso</option>
                    <option value="Finalizado">Finalizado</option>
                    <option value="Cancelado">Cancelado</option>
                </select>
                <%--<input type="text" name="estado" placeholder="Estado"  class="w-full px-3 py-1 border border-gray-300 rounded-lg text-sm text-gray-500 outline-none " />--%>
                <div class="flex flex-col gap-3">
                    <h3 class="text-sm text-gray-500 font-semibold">Equipos participantes</h3>
                    <div class="p-3 bg-gray-300 rounded-lg">
                        <%
                            if(equipos == null || equipos.isEmpty()){
                        %>
                            <div>
                                No hay equipos disponibles
                            </div>
                        <%
                            } else {

                        %>
                        <div id="equiposContainer" class="flex flex-wrap gap-2">
                            <%
                                for(JsonElement equipo: equipos){
                                    JsonObject equipoActual = equipo.getAsJsonObject();
                            %>
                            <div class="w-full equipo p-2 border rounded cursor-pointer" data-id="<%= equipoActual.get("id").getAsInt() %>">
                                <%= equipoActual.get("nombre").getAsString() %>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                    </div>
                </div>
                <div class="mt-4 flex gap-3 justify-end">
                    <button type="button" onclick="cerrarModal()" class="px-3 py-1 bg-gray-300 text-gray-500 rounded-lg">Cerrar</button>
                    <button id="titleButtonModal" type="submit" class="px-3 py-1 bg-green-300 text-white rounded-md">Crear</button>
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
        <h1 class="text-lg font-semibold">Eventos</h1>
        <div>
            <button class="w-8 h-8 flex items-center justify-center text-lg text-gray-600 bg-gray-300 rounded-full" onclick="abrirModalCrear()">+</button>
        </div>
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
                            Fecha
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Lugar
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Deporte
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Equipos participantes
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Capacidad
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Entradas vendidas
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Estado
                        </div>
                    </td>
                    <td>
                        <div class="flex items-center justify-center p-3 text-gray-500 bg-gray-200 text-lg font-semibold">
                            Acciones
                        </div>
                    </td>
                </tr>
            </thead>
            <tbody id="bodyTablaEquipos">
                <%
                    if (eventos == null || eventos.isEmpty()) {
                %>
                <tr class="border-b border-gray-200">
                    <td colspan="8">
                        <div class="flex items-center justify-center py-3">
                            <span class="py-1 px-3 bg-gray-300 text-lg text-gray-500 font-semibold rounded-full">
                                No hay eventos disponibles
                            </span>
                        </div>
                    </td>
                </tr>
                <%
                    } else {
                %>
                <%

                            for (JsonElement evento : eventos) {
                                JsonObject eventoMap = evento.getAsJsonObject();
                                int idEvento = eventoMap.get("id").getAsInt();
                                String nombre = eventoMap.get("nombre").getAsString();
                                String fecha = eventoMap.get("fecha").getAsString();;
                                String lugar = eventoMap.get("lugar").getAsString();;
                                String deporte = eventoMap.get("deporte").getAsString();;
                                Number capacidad = eventoMap.get("capacidad").getAsInt();
                                Number entradasVendidas = eventoMap.get("entradasVendidas").getAsInt();
                                String estado = eventoMap.get("estado").getAsString();;
                                JsonArray equiposParticipantes = eventoMap.get("equiposParticipantes").getAsJsonArray();

                %>
                <tr class="border-b border-gray-200">
                    <td><%= nombre %></td>
                    <td><%= fecha %></td>
                    <td><%= lugar %></td>
                    <td><%= deporte %></td>
                    <td>
                            <ul class="list-disc list-inside">
                                <%
                                    for (JsonElement eq : equiposParticipantes) {
                                %>
                                <li><%= eq.getAsString() %></li>
                                <%
                                    }
                                %>
                            </ul>
                    </td>
                    <td><%= capacidad %></td>
                    <td><%= entradasVendidas %></td>
                    <td><%= estado %></td>
                    <td class="flex justify-center items-center cursor-pointer" onclick="abrirModalEditar(<%= idEvento  %>)">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
                            <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
                        </svg>
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
        let modo = "crear";

        const equiposSeleccionados = [];
        const equipos = document.querySelectorAll('#equiposContainer .equipo');
        equipos.forEach(equipo => {
            equipo.addEventListener('click', function() {
                const equipoId = parseInt(this.getAttribute('data-id'));

                this.classList.toggle('selected');

                if(this.classList.contains('selected')){
                    if (!equiposSeleccionados.includes(equipoId)) {
                        equiposSeleccionados.push(equipoId);
                    }
                } else {
                    const index = equiposSeleccionados.indexOf(equipoId);
                    if(index !== -1){
                        equiposSeleccionados.splice(index, 1);
                    }
                }

                //console.log("Equipos seleccionados:", equiposSeleccionados);
            });
        });

        function abrirModalCrear() {
            modo = "crear";
            document.getElementById("modalTitulo").innerText = "Crear evento";
            document.getElementById("titleButtonModal").innerText = "Crear";

            document.getElementById("eventoForm").reset();

            document.querySelector('input[name="eventoId"]').value = "";

            document.getElementById("entradasVendidas").classList.remove("hidden");
            document.getElementById("estado").classList.remove("hidden");
            document.querySelector('input[name="entradasVendidas"]').setAttribute("required", "true");
            document.querySelector('select[name="estado"]').setAttribute("required", "true");

            document.querySelectorAll('#equiposContainer .equipo').forEach(equipoDiv => {
                equipoDiv.classList.remove('selected');
            });

            document.getElementById("modal").classList.remove("hidden");
        }

        function abrirModalEditar(idEvento) {

            modo="editar";
            document.getElementById("modalTitulo").innerText = "Editar evento";
            document.getElementById("titleButtonModal").innerText = "Editar";

            const dataEventos = <%= eventos %>;

            const eventoEditar = dataEventos.find(ev => ev.id === idEvento);

            document.querySelector('input[name="eventoId"]').value = eventoEditar.id;
            document.querySelector('input[name="nombre"]').value = eventoEditar.nombre;
            document.querySelector('input[name="fecha"]').value = eventoEditar.fecha;
            document.querySelector('input[name="lugar"]').value = eventoEditar.lugar;
            document.querySelector('input[name="deporte"]').value = eventoEditar.deporte;
            document.querySelector('input[name="capacidad"]').value = eventoEditar.capacidad;

            document.getElementById("entradasVendidas").classList.add("hidden");
            document.getElementById("estado").classList.add("hidden");
            document.querySelector('input[name="entradasVendidas"]').removeAttribute("required");
            document.querySelector('select[name="estado"]').removeAttribute("required");


            equiposSeleccionados.length = 0; // Vacía el array global
            document.querySelectorAll('#equiposContainer .equipo').forEach(equipoDiv => {
                equipoDiv.classList.remove('selected');
            });
            eventoEditar.equiposParticipantes.forEach(equipoNombre => {
                const equipoDiv = Array.from(document.querySelectorAll('#equiposContainer .equipo'))
                    .find(div => div.textContent.trim() === equipoNombre);
                if (equipoDiv) {
                    equipoDiv.classList.add('selected');
                    equiposSeleccionados.push(parseInt(equipoDiv.getAttribute('data-id')));
                }
            });

            document.getElementById("modal").classList.remove("hidden");
        }

        function cerrarModal() {
            document.getElementById("modal").classList.add("hidden");
        }


        function enviarEvento(e) {
            console.log(modo);
            e.preventDefault();

            const nombre = document.querySelector('input[name="nombre"]').value;
            const fecha = document.querySelector('input[name="fecha"]').value;
            const lugar = document.querySelector('input[name="lugar"]').value;
            const deporte = document.querySelector('input[name="deporte"]').value;
            const capacidad = document.querySelector('input[name="capacidad"]').value;

            const dataEquipos = <%= equipos %>;

            const equiposValidos = equiposSeleccionados.filter(id => {
                const equipoSeleccionado = dataEquipos.find(eq => eq.id === id);
                return equipoSeleccionado && equipoSeleccionado.deporte === deporte;
            });

            if (equiposValidos.length < 2) {
                alert("Debes seleccionar al menos 2 equipos que practiquen " + deporte + ".");
                return;
            }

            let data = {};

            let method = "";
            let url = "";
            if (modo==="crear") {
                url = "http://localhost:8080/GestorEventosDeportivos_war_exploded/eventos";
                const entradasVendidas = document.querySelector('input[name="entradasVendidas"]').value;
                const estado = document.querySelector('select[name="estado"]').value;
                method="POST";
                data = {
                    nombre: nombre,
                    fecha: fecha,
                    lugar: lugar,
                    deporte: deporte,
                    capacidad: capacidad,
                    equiposParticipantes: equiposSeleccionados,
                    entradasVendidas: entradasVendidas,
                    estado: estado
                }
            } else{
                let idEvento = document.querySelector('input[name="eventoId"]').value;
                console.log('idEvento: es', String(idEvento));
                url = "http://localhost:8080/GestorEventosDeportivos_war_exploded/eventos?eventoId=" + idEvento;
                method="PUT";
                data = {
                    nombre: nombre,
                    fecha: fecha,
                    lugar: lugar,
                    deporte: deporte,
                    capacidad: capacidad,
                    equiposParticipantes: equiposSeleccionados
                }
            }

            fetch(url, {
                method:method,
                headers:  {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
                .then(response => response.json())
                .then(result => {
                    //console.log("Respuesta del servidor:", result);
                    alert(result.message);
                    //console.log(result.statusCode);
                    window.location.href = "http://localhost:8080/GestorEventosDeportivos_war_exploded/";
                })
                .catch(error => console.error("Error:", error));

        }
    </script>

</body>
</html>