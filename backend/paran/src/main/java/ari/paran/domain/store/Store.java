package ari.paran.domain.store;

import ari.paran.domain.Event;
import ari.paran.domain.Partnership;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Store implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "store_name")
    private String name;

    @Column
    private String owner_name;

    @Column
    private String address;

    @Column(name = "phone")
    private String phoneNumber;

    @Embedded
    @Column(name = "img")
    private ImgFiles imgFiles;

    @Column
    private boolean private_event;

    @Column
    private boolean stamp;

    @JsonManagedReference
    @OneToMany(mappedBy = "store")
    private List<Partnership> partnershipList;

    @JsonManagedReference
    @OneToMany(mappedBy = "name")
    private List<Event> eventList;

    public boolean getPrivateevent(){
        return this.private_event;
    }

    public boolean getStamp(){
        return this.stamp;
    }


}