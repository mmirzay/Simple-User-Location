package com.project.my.userlocation.entity.identifier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static com.project.my.userlocation.configuration.Constant.DB_ID_MAX_SIZE;

/**
 * It is base entity which holds an ID for storing new entities. The ID will be initialized using {@code UUID} code.
 * Any entities, extending this class, will have a UUID-based ID.
 * @see UuidIdentifierGenerator
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class UuidIdentifiedEntity {

    @Id
    @GeneratedValue(generator = UuidIdentifierGenerator.NAME)
    @GenericGenerator(name = UuidIdentifierGenerator.NAME, strategy = "com.project.my.userlocation.entity.identifier.UuidIdentifierGenerator")
    @Column(length = DB_ID_MAX_SIZE)
    protected String id;
}