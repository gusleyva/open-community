package com.opencommunity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Initiative entity.\n@author c4cydonia team.
 */
@ApiModel(description = "Initiative entity.\n@author c4cydonia team.")
@Entity
@Table(name = "initiative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "initiative")
public class Initiative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToMany(mappedBy = "initiatives")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Initiative title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Initiative projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Initiative addProject(Project project) {
        this.projects.add(project);
        project.getInitiatives().add(this);
        return this;
    }

    public Initiative removeProject(Project project) {
        this.projects.remove(project);
        project.getInitiatives().remove(this);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Initiative)) {
            return false;
        }
        return id != null && id.equals(((Initiative) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Initiative{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
