package zw.co.ema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Intervention.
 */
@Entity
@Table(name = "intervention")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Intervention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "target_area", nullable = false)
    private Integer targetArea;

    @NotNull
    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @NotNull
    @Column(name = "achieved_area", nullable = false)
    private Integer achievedArea;

    @NotNull
    @Column(name = "cost_of_intervention", precision = 21, scale = 2, nullable = false)
    private BigDecimal costOfIntervention;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private InterventionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "province" }, allowSetters = true)
    private District location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "district" }, allowSetters = true)
    private Ward ward;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Intervention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Intervention startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getTargetArea() {
        return this.targetArea;
    }

    public Intervention targetArea(Integer targetArea) {
        this.setTargetArea(targetArea);
        return this;
    }

    public void setTargetArea(Integer targetArea) {
        this.targetArea = targetArea;
    }

    public LocalDate getTargetDate() {
        return this.targetDate;
    }

    public Intervention targetDate(LocalDate targetDate) {
        this.setTargetDate(targetDate);
        return this;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public Integer getAchievedArea() {
        return this.achievedArea;
    }

    public Intervention achievedArea(Integer achievedArea) {
        this.setAchievedArea(achievedArea);
        return this;
    }

    public void setAchievedArea(Integer achievedArea) {
        this.achievedArea = achievedArea;
    }

    public BigDecimal getCostOfIntervention() {
        return this.costOfIntervention;
    }

    public Intervention costOfIntervention(BigDecimal costOfIntervention) {
        this.setCostOfIntervention(costOfIntervention);
        return this;
    }

    public void setCostOfIntervention(BigDecimal costOfIntervention) {
        this.costOfIntervention = costOfIntervention;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Intervention createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Intervention lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public InterventionType getType() {
        return this.type;
    }

    public void setType(InterventionType interventionType) {
        this.type = interventionType;
    }

    public Intervention type(InterventionType interventionType) {
        this.setType(interventionType);
        return this;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Intervention project(Project project) {
        this.setProject(project);
        return this;
    }

    public District getLocation() {
        return this.location;
    }

    public void setLocation(District district) {
        this.location = district;
    }

    public Intervention location(District district) {
        this.setLocation(district);
        return this;
    }

    public Ward getWard() {
        return this.ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Intervention ward(Ward ward) {
        this.setWard(ward);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intervention)) {
            return false;
        }
        return id != null && id.equals(((Intervention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intervention{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", targetArea=" + getTargetArea() +
            ", targetDate='" + getTargetDate() + "'" +
            ", achievedArea=" + getAchievedArea() +
            ", costOfIntervention=" + getCostOfIntervention() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
