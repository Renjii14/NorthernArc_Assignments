package org.northernarc.librarymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fine_id")
    private Long fineId;

    private double amount;
    private String paymentStatus;

    @OneToOne
    @JoinColumn(name="issue_id")
    private BookIssue bookIssue;

}
