package com.orp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "working")
public class Working {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "job_rank_id")
    private Integer jobRankId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "job_rank_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private JobRank jobRank;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Project project;

    @OneToMany(mappedBy = "working", cascade = CascadeType.REMOVE)
    private List<Claim> claimList = new ArrayList<>();

}
