package co.edu.poli.ces3.gestoreventosdeportivos.services;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EquiposRepositorios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquiposServices {

    private EquiposRepositorios equiposRepositorios;

    public EquiposServices(EquiposRepositorios equiposRepositorios) {
        this.equiposRepositorios = equiposRepositorios;
    }

    public ArrayList<EquipoDAO> obtenerEquipos(){
        return this.equiposRepositorios.obtenerEquipos();
    }

    public EquipoDAO obtenerEquipoPorId(int id){
        return this.equiposRepositorios.obtenerEquipoPorId(id);
    }

    public ArrayList<EquipoDAO> obtenerEquiposPorRango(int page, int size){
        return this.equiposRepositorios.obtenerEquiposPorRango(page, size);
    }

    public List<Map<String, Object>> obtenerInformacionJugadores (ArrayList<EquipoDAO> equiposPaginados){
        return this.equiposRepositorios.obtenerInformacionJugadores(equiposPaginados);
    }

    public Map<String, Object> guardarEquipo(EquipoDAO equipo){
        return this.equiposRepositorios.guardarEquipo(equipo);
    }

    public void guardarJugador(JugadorDAO jugador){
        this.equiposRepositorios.guardarJugador(jugador);
    }

    public void guardarJugadorEnNuevoEquipo(JugadorDAO jugador, int equipoId){
            this.equiposRepositorios.guardarJugadorEnNuevoEquipo(jugador, equipoId);
    }

    public Map<String, Object> actualizarEquipo(EquipoDAO equipoActualizar, int idEquipoAct) {
        return this.equiposRepositorios.actualizarEquipo(equipoActualizar, idEquipoAct);
    }

    public void eliminarJugador(JugadorDAO jugador){
        this.equiposRepositorios.eliminarJugador(jugador);
    }
}
