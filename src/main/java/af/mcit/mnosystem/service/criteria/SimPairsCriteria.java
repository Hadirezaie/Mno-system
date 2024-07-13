package af.mcit.mnosystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link af.mcit.mnosystem.domain.SimPairs} entity. This class is used
 * in {@link af.mcit.mnosystem.web.rest.SimPairsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sim-pairs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimPairsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter msisdn;

    private StringFilter imsi;

    private LongFilter imeiNumber;

    private BooleanFilter sent;

    private Boolean distinct;

    public SimPairsCriteria() {}

    public SimPairsCriteria(SimPairsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.msisdn = other.msisdn == null ? null : other.msisdn.copy();
        this.imsi = other.imsi == null ? null : other.imsi.copy();
        this.imeiNumber = other.imeiNumber == null ? null : other.imeiNumber.copy();
        this.sent = other.sent == null ? null : other.sent.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SimPairsCriteria copy() {
        return new SimPairsCriteria(this);
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

    public StringFilter getMsisdn() {
        return msisdn;
    }

    public StringFilter msisdn() {
        if (msisdn == null) {
            msisdn = new StringFilter();
        }
        return msisdn;
    }

    public void setMsisdn(StringFilter msisdn) {
        this.msisdn = msisdn;
    }

    public StringFilter getImsi() {
        return imsi;
    }

    public StringFilter imsi() {
        if (imsi == null) {
            imsi = new StringFilter();
        }
        return imsi;
    }

    public void setImsi(StringFilter imsi) {
        this.imsi = imsi;
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

    public BooleanFilter getSent() {
        return sent;
    }

    public BooleanFilter sent() {
        if (sent == null) {
            sent = new BooleanFilter();
        }
        return sent;
    }

    public void setSent(BooleanFilter sent) {
        this.sent = sent;
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
        final SimPairsCriteria that = (SimPairsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(msisdn, that.msisdn) &&
            Objects.equals(imsi, that.imsi) &&
            Objects.equals(imeiNumber, that.imeiNumber) &&
            Objects.equals(sent, that.sent) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, msisdn, imsi, imeiNumber, sent, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimPairsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (msisdn != null ? "msisdn=" + msisdn + ", " : "") +
            (imsi != null ? "imsi=" + imsi + ", " : "") +
            (imeiNumber != null ? "imeiNumber=" + imeiNumber + ", " : "") +
            (sent != null ? "sent=" + sent + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
