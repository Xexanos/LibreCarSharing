package de.librecarsharing;

import javax.persistence.Basic;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by fred on 30.06.17.
 */
public class CommunityNoReferences {
     private DBCommunity dbCommunity;
    public CommunityNoReferences(DBCommunity dbCommunity)
    {
        this.dbCommunity=dbCommunity;
    }

    public String getName() {
        return dbCommunity.getName();
    }
    public long getId(){
        return this.dbCommunity.getId();
    }

}
