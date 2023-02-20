package com.project.my.userlocation.entity.identifier;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * It is a generator strategy for {@code UUID} identifiers. It uses {@link UUID} java util class.
 *
 * @see UuidIdentifiedEntity
 */
public class UuidIdentifierGenerator implements IdentifierGenerator {

	protected static final String NAME = "uuid-identifier-generator";

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return UUID.randomUUID().toString();
	}
}