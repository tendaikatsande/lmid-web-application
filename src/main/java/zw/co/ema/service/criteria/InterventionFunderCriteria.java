package zw.co.ema.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link zw.co.ema.domain.InterventionFunder} entity. This class is used
 * in {@link zw.co.ema.web.rest.InterventionFunderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /intervention-funders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InterventionFunderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter interventionId;

    private LongFilter funderId;

    private Boolean distinct;

    public InterventionFunderCriteria() {}

    public InterventionFunderCriteria(InterventionFunderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.interventionId = other.interventionId == null ? null : other.interventionId.copy();
        this.funderId = other.funderId == null ? null : other.funderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public InterventionFunderCriteria copy() {
        return new InterventionFunderCriteria(this);
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

    public LongFilter getInterventionId() {
        return interventionId;
    }

    public LongFilter interventionId() {
        if (interventionId == null) {
            interventionId = new LongFilter();
        }
        return interventionId;
    }

    public void setInterventionId(LongFilter interventionId) {
        this.interventionId = interventionId;
    }

    public LongFilter getFunderId() {
        return funderId;
    }

    public LongFilter funderId() {
        if (funderId == null) {
            funderId = new LongFilter();
        }
        return funderId;
    }

    public void setFunderId(LongFilter funderId) {
        this.funderId = funderId;
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
        final InterventionFunderCriteria that = (InterventionFunderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(interventionId, that.interventionId) &&
            Objects.equals(funderId, that.funderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, interventionId, funderId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterventionFunderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (interventionId != null ? "interventionId=" + interventionId + ", " : "") +
            (funderId != null ? "funderId=" + funderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
