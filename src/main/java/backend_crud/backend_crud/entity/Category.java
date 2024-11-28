package backend_crud.backend_crud.entity;

import jakarta.persistence.*;
import lombok.Data;

@NamedQuery(name = "Category.getAllCategory", query = " select c from Category as c where c.createdBy=:username")

@Data
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String desc;

    @Column(name = "Created By")
    private String createdBy;
}
