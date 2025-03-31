package co.edu.poli.ces3.gestoreventosdeportivos.servlets;

import co.edu.poli.ces3.gestoreventosdeportivos.dao.EquipoDAO;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EquiposRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.EventosRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.repositories.JugadoresRepositorios;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EquiposServices;
import co.edu.poli.ces3.gestoreventosdeportivos.services.EventoServices;
import co.edu.poli.ces3.gestoreventosdeportivos.services.JugadoresServices;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;

@WebListener
public class ServletInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent contextEvent){
        // Crear repositorios
        EquiposRepositorios equiposRepo = new EquiposRepositorios();
        JugadoresRepositorios jugadoresRepo = new JugadoresRepositorios();
        EventosRepositorios eventosRepo = new EventosRepositorios();

        // Crear Servicios
        JugadoresServices jugadoresService = new JugadoresServices(jugadoresRepo);
        EquiposServices equiposService = new EquiposServices(equiposRepo);
        EventoServices eventosService = new EventoServices(eventosRepo);

        //Inyectar dependencias cruzadas
        jugadoresRepo.setEquiposServices(equiposService);
        equiposRepo.setJugadoresServices(jugadoresService);
        eventosRepo.setEquiposServices(equiposService);

        // Almacenar en el contexto
        ServletContext context = contextEvent.getServletContext();
        context.setAttribute("equiposRepo", equiposRepo);
        context.setAttribute("equiposService", equiposService);

        context.setAttribute("jugadoresRepo", jugadoresRepo);
        context.setAttribute("jugadoresService", jugadoresService);

        context.setAttribute("eventosRepo", eventosRepo);
        context.setAttribute("eventosService", eventosService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent){
        System.out.println("Contexto desconectado");
    }
}
