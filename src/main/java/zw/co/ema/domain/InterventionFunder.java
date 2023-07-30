package zw.co.ema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InterventionFunder.
 */
@Entity
@Table(name = "intervention_funder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterventionFunder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "type", "project", "location", "ward" }, allowSetters = true)
    private Intervention intervention;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sector" }, allowSetters = true)
    private Funder funder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InterventionFunder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Intervention getIntervention() {
        return this.intervention;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public InterventionFunder intervention(Intervention intervention) {
        this.setIntervention(intervention);
        return this;
    }

    public Funder getFunder() {
        return this.funder;
    }

    public void setFunder(Funder funder) {
        this.funder = funder;
    }

    public InterventionFunder funder(Funder funder) {
        this.setFunder(funder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterventionFunder)) {
            return false;
        }
        return id != null && id.equals(((InterventionFunder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterventionFunder{" +
            "id=" + getId() +
            "}";
    }
}
