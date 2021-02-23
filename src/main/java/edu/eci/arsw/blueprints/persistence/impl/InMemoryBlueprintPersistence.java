/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Component
@Qualifier("inMemoryPersistence")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts=new Point[]{new Point(100, 100),new Point(130, 130)};
        Blueprint bp=new Blueprint("Jesus", "Salvador",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        
        Blueprint bp2=new Blueprint("Jesus", "Vino",pts);
        blueprints.put(new Tuple<>(bp2.getAuthor(),bp2.getName()), bp2);
        
        Blueprint bp3=new Blueprint("Santipato", "Dinosaurio",pts);
        blueprints.put(new Tuple<>(bp3.getAuthor(),bp3.getName()), bp3);
        
        Blueprint bp4=new Blueprint("Fuque", "Peruano",pts);
        blueprints.put(new Tuple<>(bp4.getAuthor(),bp4.getName()), bp4);
        
    }    
    
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        if((blueprints.get(new Tuple<>(author, bprintname)) != null)){
            return blueprints.get(new Tuple<>(author, bprintname));
        }
        throw new BlueprintNotFoundException("Blueprint no se pudo encontrar");
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> bluePrinthash = new HashSet<Blueprint>();
        for(Tuple<String,String> blueprint:blueprints.keySet()){
            if(blueprint.getElem1().equals(author)){
                bluePrinthash.add(blueprints.get(blueprint));
            }
        }
        if(bluePrinthash.size()!=0){
            return bluePrinthash;
        }
        throw new BlueprintNotFoundException("No se pudieron encontrar blueprints"); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        Set<Blueprint> bluePrinthash = new HashSet<Blueprint>();
        for (Tuple<String, String> blueprint : blueprints.keySet()) {
            if (!blueprint.getElem1().equals("_authorname_")) {
                bluePrinthash.add(blueprints.get(blueprint));
            }
        }
        return bluePrinthash;
    }
    
}
