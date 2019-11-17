package com.mastertek.navigator.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "insert_date")
    private Instant insertDate;

    @Column(name = "complated")
    private Boolean complated;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private District district;

    @ManyToOne
    private Town town;

    @ManyToOne
    private Street street;

    @ManyToOne
    private Building building;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Task description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getInsertDate() {
        return insertDate;
    }

    public Task insertDate(Instant insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
    }

    public Boolean isComplated() {
        return complated;
    }

    public Task complated(Boolean complated) {
        this.complated = complated;
        return this;
    }

    public void setComplated(Boolean complated) {
        this.complated = complated;
    }

    public String getLat() {
        return lat;
    }

    public Task lat(String lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public Task lng(String lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Task vehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public District getDistrict() {
        return district;
    }

    public Task district(District district) {
        this.district = district;
        return this;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Town getTown() {
        return town;
    }

    public Task town(Town town) {
        this.town = town;
        return this;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public Street getStreet() {
        return street;
    }

    public Task street(Street street) {
        this.street = street;
        return this;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public Building getBuilding() {
        return building;
    }

    public Task building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
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
        Task task = (Task) o;
        if (task.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", insertDate='" + getInsertDate() + "'" +
            ", complated='" + isComplated() + "'" +
            ", lat='" + getLat() + "'" +
            ", lng='" + getLng() + "'" +
            "}";
    }
}
