package com.mastertek.navigator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Street.
 */
@Entity
@Table(name = "street")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Street implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @Column(name = "completed")
    private Boolean completed;

    @ManyToOne
    private Town town;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Street name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public Street lat(String lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public Street lng(String lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public Street completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Town getTown() {
        return town;
    }

    public Street town(Town town) {
        this.town = town;
        return this;
    }

    public void setTown(Town town) {
        this.town = town;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Street street = (Street) o;
        if (street.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), street.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Street{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lat='" + getLat() + "'" +
            ", lng='" + getLng() + "'" +
            ", completed='" + isCompleted() + "'" +
            "}";
    }
}
