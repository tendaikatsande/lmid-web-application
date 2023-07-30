package zw.co.ema.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link zw.co.ema.domain.Intervention} entity. This class is used
 * in {@link zw.co.ema.web.rest.InterventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterventionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startDate;

    private IntegerFilter targetArea;

    private LocalDateFilter targetDate;

    private IntegerFilter achievedArea;

    private BigDecimalFilter costOfIntervention;

    private InstantFilter createdDate;

    private InstantFilter lastModifiedDate;

    private LongFilter typeId;

    private LongFilter projectId;

    private LongFilter locationId;

    private LongFilter wardId;

    private Boolean distinct;

    public InterventionCriteria() {}

    public InterventionCriteria(InterventionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.targetArea = other.targetArea == null ? null : other.targetArea.copy();
        this.targetDate = other.targetDate == null ? null : other.targetDate.copy();
        this.achievedArea = other.achievedArea == null ? null : other.achievedArea.copy();
        this.costOfIntervention = other.costOfIntervention == null ? null : other.costOfIntervention.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.locationId = other.locationId == null ? null : other.locationId.copy();
        this.wardId = other.wardId == null ? null : other.wardId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InterventionCriteria copy() {
        return new InterventionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            startDate = new LocalDateFilter();
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public IntegerFilter getTargetArea() {
        return targetArea;
    }

    public IntegerFilter targetArea() {
        if (targetArea == null) {
            targetArea = new IntegerFilter();
        }
        return targetArea;
    }

    public void setTargetArea(IntegerFilter targetArea) {
        this.targetArea = targetArea;
    }

    public LocalDateFilter getTargetDate() {
        return targetDate;
    }

    public LocalDateFilter targetDate() {
        if (targetDate == null) {
            targetDate = new LocalDateFilter();
        }
        return targetDate;
    }

    public void setTargetDate(LocalDateFilter targetDate) {
        this.targetDate = targetDate;
    }

    public IntegerFilter getAchievedArea() {
        return achievedArea;
    }

    public IntegerFilter achievedArea() {
        if (achievedArea == null) {
            achievedArea = new IntegerFilter();
        }
        return achievedArea;
    }

    public void setAchievedArea(IntegerFilter achievedArea) {
        this.achievedArea = achievedArea;
    }

    public BigDecimalFilter getCostOfIntervention() {
        return costOfIntervention;
    }

    public BigDecimalFilter costOfIntervention() {
        if (costOfIntervention == null) {
            costOfIntervention = new BigDecimalFilter();
        }
        return costOfIntervention;
    }

    public void setCostOfIntervention(BigDecimalFilter costOfIntervention) {
        this.costOfIntervention = costOfIntervention;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            createdDate = new InstantFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new InstantFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public LongFilter projectId() {
        if (projectId == null) {
            projectId = new LongFilter();
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public LongFilter locationId() {
        if (locationId == null) {
            locationId = new LongFilter();
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getWardId() {
        return wardId;
    }

    public LongFilter wardId() {
        if (wardId == null) {
            wardId = new LongFilter();
        }
        return wardId;
    }

    public void setWardId(LongFilter wardId) {
        this.wardId = wardId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterventionCriteria that = (InterventionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(targetArea, that.targetArea) &&
            Objects.equals(targetDate, that.targetDate) &&
            Objects.equals(achievedArea, that.achievedArea) &&
            Objects.equals(costOfIntervention, that.costOfIntervention) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(wardId, that.wardId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startDate,
            targetArea,
            targetDate,
            achievedArea,
            costOfIntervention,
            createdDate,
            lastModifiedDate,
            typeId,
            projectId,
            locationId,
            wardId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterventionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startDate != null ? "startDate=" + startDate + ", " : "") +
            (targetArea != null ? "targetArea=" + targetArea + ", " : "") +
            (targetDate != null ? "targetDate=" + targetDate + ", " : "") +
            (achievedArea != null ? "achievedArea=" + achievedArea + ", " : "") +
            (costOfIntervention != null ? "costOfIntervention=" + costOfIntervention + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (projectId != null ? "projectId=" + projectId + ", " : "") +
            (locationId != null ? "locationId=" + locationId + ", " : "") +
            (wardId != null ? "wardId=" + wardId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
