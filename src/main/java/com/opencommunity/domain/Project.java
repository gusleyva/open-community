package com.opencommunity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "project_title", nullable = false)
    private String projectTitle;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @NotNull
    @Column(name = "registration_dead_line", nullable = false)
    private Instant registrationDeadLine;

    @Column(name = "description")
    private String description;

    @Column(name = "volunteer_task")
    private String volunteerTask;

    @Column(name = "max_volunteer")
    private Integer maxVolunteer;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_guest")
    private Integer maxGuest;

    @Column(name = "volunteer_instructions")
    private String volunteerInstructions;

    @Column(name = "additional_project_details")
    private String additionalProjectDetails;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    @OneToOne
    @JoinColumn(unique = true)
    private Photo photo;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "project_initiative",
               joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "initiative_id", referencedColumnName = "id"))
    private Set<Initiative> initiatives = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "projects", allowSetters = true)
    private Volunteer volunteer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public Project projectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
        return this;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Project startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Project endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Instant getRegistrationDeadLine() {
        return registrationDeadLine;
    }

    public Project registrationDeadLine(Instant registrationDeadLine) {
        this.registrationDeadLine = registrationDeadLine;
        return this;
    }

    public void setRegistrationDeadLine(Instant registrationDeadLine) {
        this.registrationDeadLine = registrationDeadLine;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVolunteerTask() {
        return volunteerTask;
    }

    public Project volunteerTask(String volunteerTask) {
        this.volunteerTask = volunteerTask;
        return this;
    }

    public void setVolunteerTask(String volunteerTask) {
        this.volunteerTask = volunteerTask;
    }

    public Integer getMaxVolunteer() {
        return maxVolunteer;
    }

    public Project maxVolunteer(Integer maxVolunteer) {
        this.maxVolunteer = maxVolunteer;
        return this;
    }

    public void setMaxVolunteer(Integer maxVolunteer) {
        this.maxVolunteer = maxVolunteer;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public Project minAge(Integer minAge) {
        this.minAge = minAge;
        return this;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxGuest() {
        return maxGuest;
    }

    public Project maxGuest(Integer maxGuest) {
        this.maxGuest = maxGuest;
        return this;
    }

    public void setMaxGuest(Integer maxGuest) {
        this.maxGuest = maxGuest;
    }

    public String getVolunteerInstructions() {
        return volunteerInstructions;
    }

    public Project volunteerInstructions(String volunteerInstructions) {
        this.volunteerInstructions = volunteerInstructions;
        return this;
    }

    public void setVolunteerInstructions(String volunteerInstructions) {
        this.volunteerInstructions = volunteerInstructions;
    }

    public String getAdditionalProjectDetails() {
        return additionalProjectDetails;
    }

    public Project additionalProjectDetails(String additionalProjectDetails) {
        this.additionalProjectDetails = additionalProjectDetails;
        return this;
    }

    public void setAdditionalProjectDetails(String additionalProjectDetails) {
        this.additionalProjectDetails = additionalProjectDetails;
    }

    public Location getLocation() {
        return location;
    }

    public Project location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Project photo(Photo photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Set<Initiative> getInitiatives() {
        return initiatives;
    }

    public Project initiatives(Set<Initiative> initiatives) {
        this.initiatives = initiatives;
        return this;
    }

    public Project addInitiative(Initiative initiative) {
        this.initiatives.add(initiative);
        initiative.getProjects().add(this);
        return this;
    }

    public Project removeInitiative(Initiative initiative) {
        this.initiatives.remove(initiative);
        initiative.getProjects().remove(this);
        return this;
    }

    public void setInitiatives(Set<Initiative> initiatives) {
        this.initiatives = initiatives;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public Project volunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
        return this;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", projectTitle='" + getProjectTitle() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", registrationDeadLine='" + getRegistrationDeadLine() + "'" +
            ", description='" + getDescription() + "'" +
            ", volunteerTask='" + getVolunteerTask() + "'" +
            ", maxVolunteer=" + getMaxVolunteer() +
            ", minAge=" + getMinAge() +
            ", maxGuest=" + getMaxGuest() +
            ", volunteerInstructions='" + getVolunteerInstructions() + "'" +
            ", additionalProjectDetails='" + getAdditionalProjectDetails() + "'" +
            "}";
    }
}
