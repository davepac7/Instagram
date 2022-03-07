package com.ableto.models;

public class Claim {

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDateOfService() {
        return dateOfService;
    }

    public void setDateOfService(String dateOfService) {
        this.dateOfService = dateOfService;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getAmountBilled() {
        return amountBilled;
    }

    public void setAmountBilled(String amountBilled) {
        this.amountBilled = amountBilled;
    }

    public String getDateToBeSubmitted() {
        return dateToBeSubmitted;
    }

    public void setDateToBeSubmitted(String dateToBeSubmitted) {
        this.dateToBeSubmitted = dateToBeSubmitted;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    private String product;
    private String memberId;
    private String participantId;
    private String dateOfBirth;
    private String client;
    private String dateOfService;
    private String protocolName;
    private String amountBilled;
    private String dateToBeSubmitted;
    private String claimId;
    private String claimStatus;


}
