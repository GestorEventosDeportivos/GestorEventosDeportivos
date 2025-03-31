package co.edu.poli.ces3.gestoreventosdeportivos.services;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EquiposRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.JugadoresRepositorios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JugadoresServices {

    private EquiposServices equiposServices;
    private JugadoresRepositorios jugadoresRepositorios;

    // Constructor sin EquiposServices
    public JugadoresServices(JugadoresRepositorios jugadoresRepositorios) {
        this.jugadoresRepositorios = jugadoresRepositorios;
    }

    public void setEquiposServices(EquiposServices equiposServices) {
        this.equiposServices = equiposServices;
    }

    public ArrayList<JugadorDAO> obtenerJugadores(){
        return this.jugadoresRepositorios.obtenerJugadores();
    }

    public JugadorDAO obtenerJugadorPorId(int id){
        return this.jugadoresRepositorios.obtenerJugadorPorId(id);
    }

    public Map<String, Object> guardarJugador(JugadorDAO jugador){
        return this.jugadoresRepositorios.guardarJugador(jugador);
    }

    public Map<String, Object> transferirJugador(int jugadorId, int equipoId){
        return this.jugadoresRepositorios.transferirJugador(jugadorId, equipoId);
    }

    public Map<String, Object> actualizarJugador(JugadorDAO jugadorActualizar, int idJugadorAct){
        return this.jugadoresRepositorios.actualizarJugador(jugadorActualizar, idJugadorAct);
    }

}
