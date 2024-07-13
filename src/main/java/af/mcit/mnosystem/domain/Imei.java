package af.mcit.mnosystem.domain;

import af.mcit.mnosystem.domain.enumeration.ImeiStatus;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Imei.
 */
@Entity
@Table(name = "imei")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Imei implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "imei_number")
    private String imeiNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImeiStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Imei id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImeiNumber() {
        return this.imeiNumber;
    }

    public Imei imeiNumber(String imeiNumber) {
        this.setImeiNumber(imeiNumber);
        return this;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public ImeiStatus getStatus() {
        return this.status;
    }

    public Imei status(ImeiStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ImeiStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Imei)) {
            return false;
        }
        return id != null && id.equals(((Imei) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Imei{" +
            "id=" + getId() +
            ", imeiNumber='" + getImeiNumber() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
