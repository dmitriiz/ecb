package ru.dazzled.ecb;

import java.net.MalformedURLException;
import java.net.URL;

import java.sql.Date;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Schedule;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.dazzled.ecb.jaxb.CubeType;
import ru.dazzled.ecb.jpa.CurrencyRate;

@Singleton
@Startup
public class EuroRateLoader {
	private static final Logger log = LoggerFactory.getLogger(EuroRateLoader.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@PostConstruct
	public void postConstruct() {
		log.info("Loader started");
	}

	@SuppressWarnings("unchecked")
	@Schedule(second="0", minute="0", hour="1", persistent=false)
    private void scheduledTimeout(final Timer t) throws MalformedURLException {
		log.info("Loader activated");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND));
		Date yesterday = new Date(cal.getTime().getTime());
		URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
		CubeType root = JAXB.unmarshal(url, CubeType.class);
		log.info("File loaded, yesterday={}/{}", yesterday, yesterday.getTime());
		for (JAXBElement<CubeType> c1 : root.getContent()) {
			for (JAXBElement<CubeType> c2 : c1.getValue().getContent()) {
				CubeType cube1 = c2.getValue();
				Date date = new Date(cube1.getTime().toGregorianCalendar().getTime().getTime());
				log.info("Rates for {}/{}", date, date.getTime());
				if (!yesterday.equals(date)) {
					log.error("Date mismatch, skipping");
					continue;
				}
				for (JAXBElement<CubeType> c3 : c2.getValue().getContent()) {
					CubeType cube2 = c3.getValue();
					log.info("{} {}", cube2.getCurrency(), cube2.getRate());
					CurrencyRate currencyRate = new CurrencyRate();
					currencyRate.setDate(date);
					currencyRate.setCurrency(cube2.getCurrency());
					currencyRate.setRate(cube2.getRate());
					entityManager.persist(currencyRate);
				}
			}
		}
    }
	
}
