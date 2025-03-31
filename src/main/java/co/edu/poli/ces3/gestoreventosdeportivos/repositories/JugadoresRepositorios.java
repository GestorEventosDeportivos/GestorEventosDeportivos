package co.edu.poli.ces3.gestoreventosdeportivos.repositories;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.EventoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.dao.JugadorDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EquiposServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JugadoresRepositorios {
    private ArrayList<JugadorDAO> jugadores;
    private EquiposServices equiposServices;

    public JugadoresRepositorios() {
        jugadores = new ArrayList<>();

        JugadorDAO jacobo = new JugadorDAO(
                0,
                "Jacobo",
                "Uribe",
                "2003-10-04",
                "Colombiano",
                "Delantero",
                11,
                0,
                true
        );
        jugadores.add(jacobo);

        JugadorDAO paola = new JugadorDAO(
                1,
                "Paola",
                "Dominguez",
                "2010-04-04",
                "Colombiana",
                "Delantero",
                12,
                1,
                true
        );
        jugadores.add(paola);

        JugadorDAO thomas = new JugadorDAO(
                2,
                "Thomas",
                "Torres",
                "1990-01-14",
                "Colombiano",
                "Delantero",
                10,
                2,
                true
        );
        jugadores.add(thomas);


        JugadorDAO santiago = new JugadorDAO(
                3,
                "Santiago",
                "Vargas",
                "2000-02-02",
                "Colombiana",
                "Delantero",
                5,
                3,
                true
        );
        jugadores.add(santiago);
    }

    public void setEquiposServices(EquiposServices equiposServices) {
        this.equiposServices = equiposServices;
    }

    public ArrayList<JugadorDAO> obtenerJugadores(){
        return this.jugadores;
    }

    public JugadorDAO obtenerJugadorPorId(int id){
        for(int i = 0; i < jugadores.size(); i++){
            JugadorDAO jugador = jugadores.get(i);
            if(jugador.getId() == id){
                return jugador;
            }
        }
        return null;
    }

    public boolean buscarNumeroJugadorEnEquipo(int numero, int equipoId){
        EquipoDAO equipo = this.equiposServices.obtenerEquipoPorId(equipoId);
        if(equipo == null){
            return false;
        }
        ArrayList<Integer> jugadoresEquipo = equipo.getJugadores();
        for(int i = 0; i < jugadoresEquipo.size(); i++){
            JugadorDAO jugadorEquipo = obtenerJugadorPorId(jugadoresEquipo.get(i));
            if(jugadorEquipo.getNumero() == numero){
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> guardarJugador(JugadorDAO jugador) {
        Map<String, Object> resultado = new HashMap<>();

        EquipoDAO equipo = this.equiposServices.obtenerEquipoPorId(jugador.getEquipoId());
        if (equipo == null) {
            resultado.put("success", false);
            resultado.put("message", "No se encontró un equipo con id: " + jugador.getEquipoId());
            return resultado;
        }

        boolean existe = buscarNumeroJugadorEnEquipo(jugador.getNumero(), jugador.getEquipoId());
        if (existe) {
            resultado.put("success", false);
            resultado.put("message", "El jugador con ese número ya existe en el equipo.");
            return resultado;
        }

        this.jugadores.add(jugador);
        this.equiposServices.guardarJugador(jugador);

        resultado.put("success", true);
        resultado.put("message", "Guardado.");
        return resultado;
    }

    public Map<String, Object> transferirJugador(int jugadorId, int equipoId){
        JugadorDAO jugadorATransferir = this.obtenerJugadorPorId(jugadorId);

        Map<String, Object> resultado = new HashMap<>();

        if (jugadorATransferir == null) {
            resultado.put("success", false);
            resultado.put("message", "No existe el jugador con ID: " + jugadorId);
            return resultado;
        }

        boolean existe = buscarNumeroJugadorEnEquipo(jugadorATransferir.getNumero(), equipoId);
        if(existe) {
            resultado.put("success", false);
            resultado.put("message", "El jugador con ese numero ya existe en el equipo.");
            return resultado;
        }
        this.equiposServices.eliminarJugador(jugadorATransferir);
        this.equiposServices.guardarJugadorEnNuevoEquipo(jugadorATransferir, equipoId);
        jugadorATransferir.setEquipoId(equipoId);
        resultado.put("success", true);
        resultado.put("message", "Jugador transferido.");
        return resultado;
    }

    public Map<String, Object> actualizarJugador(JugadorDAO jugadorActualizar, int idJugadorAct) {

        Map<String, Object> resultado = new HashMap<>();

        JugadorDAO jugadorExistente = this.obtenerJugadorPorId(idJugadorAct);

        if (jugadorExistente == null) {
            resultado.put("success", false);
            resultado.put("message", "No existe el jugador con ID: " + idJugadorAct);
            return resultado;
        }

        boolean existe = buscarNumeroJugadorEnEquipo(jugadorActualizar.getNumero(), jugadorActualizar.getEquipoId());
        if (existe) {
            resultado.put("success", false);
            resultado.put("message", "El jugador con ese número ya existe en el equipo.");
            return resultado;
        }

        jugadorExistente.setNombre(jugadorActualizar.getNombre());
        jugadorExistente.setApellido(jugadorActualizar.getApellido());
        jugadorExistente.setFechaNacimiento(jugadorActualizar.getFechaNacimiento());
        jugadorExistente.setNacionalidad(jugadorActualizar.getNacionalidad());
        jugadorExistente.setPosicion(jugadorActualizar.getPosicion());
        jugadorExistente.setNumero(jugadorActualizar.getNumero());
        jugadorExistente.setEstadoActivo(jugadorActualizar.isEstadoActivo());

        resultado.put("success", true);
        resultado.put("message", "Jugador actualizado correctamente.");
        return resultado;
    }

}
