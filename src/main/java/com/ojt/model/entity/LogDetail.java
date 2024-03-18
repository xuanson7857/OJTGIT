package com.ojt.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LogDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameError;
    private Integer lineError;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "log_id", referencedColumnName = "id")
    private LogImport logImport;
}