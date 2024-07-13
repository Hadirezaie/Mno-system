package af.mcit.mnosystem.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A SimPairs.
 */
@Entity
@Table(name = "sim_pairs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SimPairs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "imsi")
    private String imsi;

    @Column(name = "imei_number")
    private Long imeiNumber;

    @Column(name = "sent")
    private Boolean sent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SimPairs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return this.msisdn;
    }

    public SimPairs msisdn(String msisdn) {
        this.setMsisdn(msisdn);
        return this;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImsi() {
        return this.imsi;
    }

    public SimPairs imsi(String imsi) {
        this.setImsi(imsi);
        return this;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public Long getImeiNumber() {
        return this.imeiNumber;
    }

    public SimPairs imeiNumber(Long imeiNumber) {
        this.setImeiNumber(imeiNumber);
        return this;
    }

    public void setImeiNumber(Long imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public Boolean getSent() {
        return this.sent;
    }

    public SimPairs sent(Boolean sent) {
        this.setSent(sent);
        return this;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimPairs)) {
            return false;
        }
        return id != null && id.equals(((SimPairs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SimPairs{" +
            "id=" + getId() +
            ", msisdn='" + getMsisdn() + "'" +
            ", imsi='" + getImsi() + "'" +
            ", imeiNumber=" + getImeiNumber() +
            ", sent='" + getSent() + "'" +
            "}";
    }
}
