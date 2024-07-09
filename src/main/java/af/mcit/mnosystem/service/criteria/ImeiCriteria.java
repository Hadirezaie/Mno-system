package af.mcit.mnosystem.service.criteria;

import af.mcit.mnosystem.domain.enumeration.ImeiStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.mnosystem.domain.Imei} entity. This class is used
 * in {@link af.mcit.mnosystem.web.rest.ImeiResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /imeis?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImeiCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ImeiStatus
     */
    public static class ImeiStatusFilter extends Filter<ImeiStatus> {

        public ImeiStatusFilter() {}

        public ImeiStatusFilter(ImeiStatusFilter filter) {
            super(filter);
        }

        @Override
        public ImeiStatusFilter copy() {
            return new ImeiStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter imeiNumber;

    private ImeiStatusFilter status;

    private Boolean distinct;

    public ImeiCriteria() {}

    public ImeiCriteria(ImeiCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imeiNumber = other.imeiNumber == null ? null : other.imeiNumber.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImeiCriteria copy() {
        return new ImeiCriteria(this);
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

    public LongFilter getImeiNumber() {
        return imeiNumber;
    }

    public LongFilter imeiNumber() {
        if (imeiNumber == null) {
            imeiNumber = new LongFilter();
        }
        return imeiNumber;
    }

    public void setImeiNumber(LongFilter imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public ImeiStatusFilter getStatus() {
        return status;
    }

    public ImeiStatusFilter status() {
        if (status == null) {
            status = new ImeiStatusFilter();
        }
        return status;
    }

    public void setStatus(ImeiStatusFilter status) {
        this.status = status;
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
        final ImeiCriteria that = (ImeiCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imeiNumber, that.imeiNumber) &&
            Objects.equals(status, that.status) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imeiNumber, status, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImeiCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (imeiNumber != null ? "imeiNumber=" + imeiNumber + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
