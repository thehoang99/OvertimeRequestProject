package com.orp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "claim")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "working_id")
    private Integer workingId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "from_time")
    private LocalDate fromTime;

    @Column(name = "to_time")
    private LocalTime toTime;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "audit_trail")
    private String auditTrail;

    @ManyToOne
    @JoinColumn(name = "working_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Working working;

}
