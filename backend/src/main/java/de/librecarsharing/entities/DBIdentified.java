package de.librecarsharing.entities;

/**
 * Created by Admin on 16.06.2017.
 */
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;

@MappedSuperclass
public abstract class DBIdentified {

    private long id;

    @Id
    @GeneratedValue
    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}

