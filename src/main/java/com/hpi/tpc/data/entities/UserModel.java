package com.hpi.tpc.data.entities;

import java.io.*;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode @ToString
@Builder
@Entity
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public UserModel() {
    }

//    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull@Id private Integer id;
    @Column(nullable = false, unique = true)
    @NonNull private String name;
    @Column(nullable = false, unique = true)
    @NonNull private String userName;
    @Column(nullable = false, unique = true)
    @NonNull private String email;
    @NonNull private String password;
}