package com.jobportal.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "job_applications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "job_id"})
    }
)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(nullable = false)
    private String status = "APPLIED";

    // ---------- GETTERS ----------
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Job getJob() {
        return job;
    }

    public String getStatus() {
        return status;
    }

    // ---------- SETTERS ----------
    public void setUser(User user) {
        this.user = user;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
