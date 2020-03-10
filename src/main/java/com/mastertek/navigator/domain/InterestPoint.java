package com.mastertek.navigator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A InterestPoint.
 */
@Entity
@Table(name = "interest_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InterestPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

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

    public InterestPoint name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public InterestPoint lat(String lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public InterestPoint lng(String lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(String lng) {
        this.lng = lng;
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
        InterestPoint interestPoint = (InterestPoint) o;
        if (interestPoint.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interestPoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InterestPoint{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lat='" + getLat() + "'" +
            ", lng='" + getLng() + "'" +
            "}";
    }
}
