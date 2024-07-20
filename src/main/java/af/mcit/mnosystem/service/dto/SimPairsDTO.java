package af.mcit.mnosystem.service.dto;

public class SimPairsDTO {

    private String msisdn;

    private String imsi;
    private String imei;

    private java.time.LocalDate datetime;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public java.time.LocalDate getDatetime() {
        return datetime;
    }

    public void setDatetime(java.time.LocalDate datetime) {
        this.datetime = datetime;
    }
}
