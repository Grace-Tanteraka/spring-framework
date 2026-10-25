package mg.itu.grace.web;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private Map<String, Object> attributes = new HashMap<>();

    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    public Map<String, Object> getAllAttributes() {
        return attributes;
    }

    public void reset(){
        this.attributes.clear();
    }

    public void removeSession(String key){
        this.attributes.remove(key);
    }
}

// Donc on a un petit probleme c'est que il ne faut plus me derrander quand je suis concentre car tout a l'heure j'ai attendu trop longtemps et maintenant je ne veux plus etre deranger
// Je ferai ma priere tout seul apres mais maintenant je dois me concentre
