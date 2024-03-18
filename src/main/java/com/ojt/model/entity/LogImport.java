package com.ojt.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class LogImport {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private  Long id;
    private Timestamp createDate;
    private String fileName;

    @OneToMany(mappedBy = "logImport")
    List<LogDetail> logDetails;
}
