package br.com.bcp.currencyexchangeservice;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ExchangeValue {

    @Id
    private Long id;
    //from is a SQL reserved word
    @Column(name = "currency_from")
    private String from;
    @Column(name = "currency_to")
    private String to;
    private BigDecimal conversionMultiple;
    private int port;
    private String hostName;
    private String hostAddress;

    protected ExchangeValue() {
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public ExchangeValue(Long id, String from, String to, BigDecimal converstionMultiple) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.conversionMultiple = converstionMultiple;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return this.to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getConversionMultiple() {
        return this.conversionMultiple;
    }

    public void setConversionMultiple(BigDecimal converstionMultiple) {
        this.conversionMultiple = converstionMultiple;
             
    }
    
    @Override
    public String toString() {
        return "{" +
        " id='" + getId() + "'" +
        ", from='" + getFrom() + "'" +
        ", to='" + getTo() + "'" +
            ", converstionMultiple='" + getConversionMultiple() + "'" +
            "}";
    }
    
    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}