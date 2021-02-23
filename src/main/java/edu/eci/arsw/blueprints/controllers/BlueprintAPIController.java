/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {
    
    @Autowired
    BlueprintsServices bpp;
    
    /*GET REQUEST*/
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> todosLosPlanos(){
        try {
            //Traer los datos desde la API
            Set<Blueprint> bp = bpp.getAllBlueprints(); 
            return new ResponseEntity<>(bpp.multiFilter(bp),HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(ResourceNotFoundException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error",HttpStatus.NOT_FOUND);
        }        
    }
    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> obtenerPorAutor(@PathVariable("author") String author){
        try {
            //Traer los datos desde la API
            return new ResponseEntity<>(bpp.getBlueprintsByAuthor(author),HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(ResourceNotFoundException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El autor "+author+" no se encontró",HttpStatus.NOT_FOUND);
        }        
    }
    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.GET)
    public ResponseEntity<?> obtenerPorAutoryNombre(@PathVariable("author") String author, @PathVariable("bpname") String bpname ){
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(bpp.getBlueprint(author,bpname),HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            Logger.getLogger(ResourceNotFoundException.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("El elemento no se encontró",HttpStatus.NOT_FOUND);
        }        
    }
    
    /*POST REQUEST*/
        @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPostRecursoPlanos(@RequestBody JSONObject blueprint) {
        try {
            Blueprint temp = new Blueprint(blueprint.get("Autor").toString(), blueprint.get("Nombre").toString());
            String[] list = blueprint.get("Puntos").toString().split("-");
            for (String str : list) {
                List<String> listaSplit = Arrays.asList(str.split(","));
                String posX = listaSplit.get(0).substring(2);
                String posY = listaSplit.get(1).substring(2, listaSplit.get(0).length());
                Point punto = new Point(Integer.parseInt(posX), Integer.parseInt(posY));
                temp.addPoint(punto);
            }
            bpp.addNewBlueprint(temp);
            return new ResponseEntity<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN);
        }
    }
    /*PUT REQUEST*/
    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.PUT)
    public ResponseEntity<?> putBlueprintsByAuthor(@PathVariable String author, @PathVariable String bpname, @RequestBody JSONObject data) {
        try {
            Blueprint blueprint = bpp.filterBlueprint(bpp.getBlueprint(author, bpname));
            String[] lista = data.get("Puntos").toString().split("-");
            blueprint.setAuthor(data.get("Autor").toString());
            blueprint.setName(data.get("Nombre").toString());
            for (String string : lista) {
                List<String> listaSplit = Arrays.asList(string.split(","));
                String posX = listaSplit.get(0).substring(2);
                String posY = listaSplit.get(1).substring(2, listaSplit.get(0).length());
                Point punto = new Point(Integer.parseInt(posX), Integer.parseInt(posY));
                blueprint.addPoint(punto);
            }
            Set<Blueprint> blueprintSet = new HashSet<Blueprint>();
            blueprintSet.add(blueprint);
            return new ResponseEntity<>(blueprintSet, HttpStatus.CREATED);
        } catch (BlueprintNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
        }
    }
}

