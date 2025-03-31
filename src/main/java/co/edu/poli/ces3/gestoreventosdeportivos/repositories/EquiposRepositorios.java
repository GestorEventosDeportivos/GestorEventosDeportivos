package co.edu.poli.ces3.gestoreventosdeportivos.repositories;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.services.JugadoresServices;

import java.util.*;

public class EquiposRepositorios {

    private ArrayList<EquipoDAO> equipos;
    private JugadoresServices jugadoresServices;

    public EquiposRepositorios() {

        equipos = new ArrayList<>(); //inicializar lista de equipos

        ArrayList<Integer> jugadoresIds1 = new ArrayList<>();
        jugadoresIds1.add(0);
        EquipoDAO nacional = new EquipoDAO(
                0,
                "Atletico Nacional",
                "Futbol",
                "Medellín",
                "1947-04-07T00:00:00",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTbT_C2sZb9w4zTK2CyAuYX6Y47nvSzNtfbg&s",
                jugadoresIds1
        );
        equipos.add(nacional);

        ArrayList<Integer> jugadoresIds2 = new ArrayList<>();
        jugadoresIds2.add(1);
        EquipoDAO medellin = new EquipoDAO(
                1,
                "Deportivo Independiente Medellín",
                "Futbol",
                "Medellín",
                "1913-11-14T00:00:00",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/Escudo_del_Deportivo_Independiente_Medell%C3%ADn.png/190px-Escudo_del_Deportivo_Independiente_Medell%C3%ADn.png",
                jugadoresIds2
        );
        equipos.add(medellin);

        ArrayList<Integer> jugadoresIds3 = new ArrayList<>();
        jugadoresIds3.add(2);
        jugadoresIds3.add(3);
        EquipoDAO medellinBasket = new EquipoDAO(
                2,
                "Deportivo Independiente Medellín Basket",
                "Basket",
                "Medellín",
                "1963-07-28T00:00:00",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/Escudo_del_Deportivo_Independiente_Medell%C3%ADn.png/190px-Escudo_del_Deportivo_Independiente_Medell%C3%ADn.png",
                jugadoresIds3
        );
        equipos.add(medellinBasket);

        ArrayList<Integer> jugadoresIds4 = new ArrayList<>();
        jugadoresIds4.add(2);
        jugadoresIds4.add(3);
        EquipoDAO sabanetaBasket = new EquipoDAO(
                3,
                "Deportivo Basket",
                "Basket",
                "Sabaneta",
                "1949-06-12T00:00:00",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/Escudo_del_Deportivo_Independiente_Medell%C3%ADn.png/190px-Escudo_del_Deportivo_Independiente_Medell%C3%ADn.png",
                jugadoresIds4
        );
        equipos.add(sabanetaBasket);
    }

    public void setJugadoresServices(JugadoresServices jugadoresServices) {
        this.jugadoresServices = jugadoresServices;
    }

    public ArrayList<EquipoDAO> obtenerEquipos(){
        return this.equipos;
    }

    public EquipoDAO obtenerEquipoPorId(int id){
        for(int i = 0; i < equipos.size(); i++){
            EquipoDAO equipo = equipos.get(i);
            if(equipo.getId() == id){
                return equipo;
            }
        }
        return null;
    }

    public List<Map<String, Object>> obtenerInformacionJugadores(ArrayList<EquipoDAO> equiposPaginados){
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (EquipoDAO equipo : equiposPaginados) {
            Map<String, Object> equipoMap = new LinkedHashMap<>();
            equipoMap.put("id", equipo.getId());
            equipoMap.put("nombre", equipo.getNombre());
            equipoMap.put("deporte", equipo.getDeporte());
            equipoMap.put("ciudad", equipo.getCiudad());
            equipoMap.put("fechaFundacion", equipo.getFechaFundacion());
            equipoMap.put("logo", equipo.getLogo());

            List<Map<String, Object>> jugadoresDetallados = new ArrayList<>();
            for (Integer idJugador : equipo.getJugadores()) {
                JugadorDAO jugador = jugadoresServices.obtenerJugadorPorId(idJugador);
                if (jugador != null) {
                    Map<String, Object> jugMap = new LinkedHashMap<>();
                    jugMap.put("nombre", jugador.getNombre());
                    jugMap.put("apellido", jugador.getApellido());
                    jugMap.put("numero", jugador.getNumero());
                    jugMap.put("posicion", jugador.getPosicion());
                    jugadoresDetallados.add(jugMap);
                }
            }

            equipoMap.put("jugadores", jugadoresDetallados);

            respuesta.add(equipoMap);
        }
        return respuesta;
    }

    public ArrayList<EquipoDAO> obtenerEquiposPorRango(int page, int size){
        if (page < 1) page = 1;

        int start = (page - 1) * size;

        if (start >= this.equipos.size()) {
            return new ArrayList<>();
        }

        int end = Math.min(start + size, this.equipos.size());

        return new ArrayList<>(this.equipos.subList(start, end));

    }

    public boolean existeEquipo(EquipoDAO equipo){
        for (EquipoDAO e : this.equipos) {
            if (e.getNombre().equalsIgnoreCase(equipo.getNombre()) &&
                    e.getDeporte().equalsIgnoreCase(equipo.getDeporte())) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> guardarEquipo(EquipoDAO equipo){
        Map<String, Object> resultado = new HashMap<>();

        for (Integer jugadorId : equipo.getJugadores()) {
            JugadorDAO jugadorExistente = jugadoresServices.obtenerJugadorPorId(jugadorId);
            if (jugadorExistente == null) {
                resultado.put("success", false);
                resultado.put("message", "El jugador con ID " + jugadorId + " no existe.");
                return resultado;
            }
        }

        if(existeEquipo(equipo)){
            resultado.put("success", false);
            resultado.put("message", "El nombre del equipo ya existe en el deporte.");
            return resultado;
        }

        this.equipos.add(equipo);
        resultado.put("success", true);
        resultado.put("message", "Equipo creado.");
        return resultado;
    }

    public void guardarJugador(JugadorDAO jugador){
        EquipoDAO equipo = this.obtenerEquipoPorId(jugador.getEquipoId());
        equipo.getJugadores().add(jugador.getId());
    }

    public void guardarJugadorEnNuevoEquipo(JugadorDAO jugador, int equipoId){
        EquipoDAO equipo = this.obtenerEquipoPorId(equipoId);
        equipo.getJugadores().add(jugador.getId());
    }

    public Map<String, Object> actualizarEquipo(EquipoDAO equipoActualizar, int idEquipoAct) {
        Map<String, Object> resultado = new HashMap<>();

        EquipoDAO equipoExistente = this.obtenerEquipoPorId(idEquipoAct);
        if (equipoExistente == null) {
            resultado.put("success", false);
            resultado.put("message", "No existe el equipo con ID: " + idEquipoAct);
            return resultado;
        }

        for (Integer jugadorId : equipoActualizar.getJugadores()) {
            JugadorDAO jugadorExistente = jugadoresServices.obtenerJugadorPorId(jugadorId);
            if (jugadorExistente == null) {
                resultado.put("success", false);
                resultado.put("message", "El jugador con ID " + jugadorId + " no existe.");
                return resultado;
            }
        }

        if(existeEquipo(equipoActualizar)){
            resultado.put("success", false);
            resultado.put("message", "El nombre del equipo ya existe en el deporte.");
            return resultado;
        }

        equipoExistente.setLogo(equipoActualizar.getLogo());
        equipoExistente.setNombre(equipoActualizar.getNombre());
        equipoExistente.setCiudad(equipoActualizar.getCiudad());
        equipoExistente.setDeporte(equipoActualizar.getDeporte());
        equipoExistente.setFechaFundacion(equipoActualizar.getFechaFundacion());
        equipoExistente.setJugadores(equipoActualizar.getJugadores());

        resultado.put("success", true);
        resultado.put("message", "Equipo actualizado.");
        return resultado;
    }

    public void eliminarJugador(JugadorDAO jugador){
        EquipoDAO equipo = this.obtenerEquipoPorId(jugador.getEquipoId());
        equipo.getJugadores().remove(Integer.valueOf(jugador.getId()));
    }

}
