package ru.dazzled.ecb.ui;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ru.dazzled.ecb.jpa.CurrencyRate;

import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;

/**
 * Session Bean implementation class JPAEntityProvider
 */
@Stateless
@LocalBean
@TransactionManagement
public class CurrencyRateEntityProvider extends MutableLocalEntityProvider<CurrencyRate> {
	
	@PersistenceContext
	private EntityManager entityManager;

	public CurrencyRateEntityProvider() {
		super(CurrencyRate.class);
		setTransactionsHandledByProvider(false);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	protected void runInTransaction(Runnable operation) {
		super.runInTransaction(operation);
	}

	@PostConstruct
	public void postConstruct() {
		setEntityManager(entityManager);
	}

}
