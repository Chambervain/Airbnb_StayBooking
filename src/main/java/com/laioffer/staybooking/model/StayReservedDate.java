package com.laioffer.staybooking.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stay_reserved_date")
public class StayReservedDate implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private StayReservedDateKey id;

    @ManyToOne
    @MapsId("stay_id")
    private Stay stay;

    public StayReservedDate() {}

    public StayReservedDate(StayReservedDateKey id, Stay stay) {
        this.id = id;
        this.stay = stay;
    }

    public StayReservedDateKey getId() {
        return id;
    }

    public void setId(StayReservedDateKey id) {
        this.id = id;
    }

    public Stay getStay() {
        return stay;
    }

    public void setStay(Stay stay) {
        this.stay = stay;
    }

}
