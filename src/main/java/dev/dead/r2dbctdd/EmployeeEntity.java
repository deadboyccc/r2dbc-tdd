package dev.dead.r2dbctdd;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Ensure this matches the quoted table name in your schema exactly
@Table("employee")
public class EmployeeEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("status")
    private Status status;

    public enum Status {
        REMOTE, ONSITE, HYBRID
    }
}