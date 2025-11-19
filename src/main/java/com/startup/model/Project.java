package com.startup.model;

import java.util.Date;

public class Project {
    private Long id;
    private Long clientId;
    private String title;
    private String description;
    private Double costMin;
    private Double costMax;
    private Date deadline;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getCostMin() { return costMin; }
    public void setCostMin(Double costMin) { this.costMin = costMin; }
    public Double getCostMax() { return costMax; }
    public void setCostMax(Double costMax) { this.costMax = costMax; }
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
