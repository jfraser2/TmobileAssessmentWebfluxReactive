package springboot.enums;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public enum ZonedDateTimeEnum {

	INSTANCE;
	private final ZoneId defaultZoneId = ZoneId.of("America/Chicago");
	private final ZoneId UTC_ZONE_ID = ZoneOffset.UTC; // Zulu or London Time +00:00
	
	public final String DATE_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
	public final String DATE_FORMAT2 = "yyyy-MM-dd'T'HH:mm:ss[xx][XX]";
	public final String DATE_FORMAT3 = "MM-dd-yyyy HH:mm:ss z (XXX)";
	
	private ZoneId zoneId = null;

	private ZonedDateTimeEnum() {
	    // Perform any configuration here.
	    this.zoneId = this.defaultZoneId;
	}

	public void resetDefaultZoneId() {
	    this.zoneId = this.defaultZoneId;
	}
	
	private boolean validString(String aString) {
		boolean retVar = false;
		
		if (null != aString && aString.length() > 0) {
			retVar = true;
		}
		 return retVar;	
	}
	
	private ZoneId getZoneId(String newZoneId) {
		
		ZoneId tempZoneId = null;
		if (validString(newZoneId)) {
			// If the zone ID equals 'Z', the result is ZoneOffset.UTC
			try {
				tempZoneId = ZoneId.of(newZoneId);
			} catch (DateTimeException dte) { // if the zone ID has an invalid format
				tempZoneId = null;
			}
		}
		
		return tempZoneId;
	}
	
	public ZoneId getZoneId() {
		return this.zoneId;
	}
	
	public void setZoneId(String newZoneId) {
		if (validString(newZoneId)) {
			// If the zone ID equals 'Z', the result is ZoneOffset.UTC
			ZoneId tempZoneId = getZoneId(newZoneId);
			if (null != tempZoneId) {
				this.zoneId = tempZoneId;
			}
		}
	}
	
	public ZonedDateTime now() {
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
		return  ZonedDateTime.ofInstant(instant, this.zoneId);
	}
	
	public ZonedDateTime now(ZoneId aZoneId) {
		ZonedDateTime retVar = null;
		
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
	    if (null != aZoneId) {
	    	retVar = ZonedDateTime.ofInstant(instant, aZoneId);
	    }
	    
		return  retVar;
	}
	
	public ZonedDateTime now(String aZoneId) {
		ZonedDateTime retVar = null;
		
	    Instant instant = Instant.now(); // Current instant from London(Greenwich)
	    
	    if (validString(aZoneId)) {
	    	ZoneId tempZoneId = getZoneId(aZoneId);
	    	if (null != tempZoneId) {
	    		retVar = ZonedDateTime.ofInstant(instant, tempZoneId);
	    	}
	    }
	    
		return  retVar;
	}
	
	public ZonedDateTime createZonedDateTime(String date, String dateFormat) {
		ZonedDateTime retVar = null;
		
		if (validString(date) && validString(dateFormat)) {
			try {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withZone(this.zoneId);
				retVar = ZonedDateTime.parse(date, dtf);
			} catch(Exception e) {
				retVar = null;
			}
		}
		
		return retVar;
	}
	
	public ZonedDateTime createZonedDateTime(String date, String dateFormat, String theZone) {
		ZonedDateTime retVar = null;
		
		if (validString(date) && validString(dateFormat) && validString(theZone)) {
			ZoneId tempZoneId = getZoneId(theZone);
			try {
				if (null != tempZoneId) {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withZone(tempZoneId);
					retVar = ZonedDateTime.parse(date, dtf);
				}	
			} catch(Exception e) {
				retVar = null;
			}
		}
		
		return retVar;
	}
	
	public Long convertZonedDateToMilliseconds(ZonedDateTime orig) {
		
		Long retVar = null;
		
		if (null != orig) {
			retVar = Long.valueOf(orig.toInstant().toEpochMilli());
		}	
		
		return retVar;
	}

	public Long convertDateStringToMilliseconds(String date, String dateFormat) {
		Long retVar = null;
		
		if (validString(date) && validString(dateFormat)) {
			ZonedDateTime orig = createZonedDateTime(date, dateFormat);
			if (null != orig) {
				ZonedDateTime newDateTime = orig.withZoneSameInstant(UTC_ZONE_ID);				
				retVar = convertZonedDateToMilliseconds(newDateTime);
			}
		}	
		
		return retVar;
	}
	
	public Long convertDateStringToMilliseconds(String date, String dateFormat, String theZone) {
		Long retVar = null;
		
		if (validString(date) && validString(dateFormat) && validString(theZone))  {
			ZonedDateTime orig = createZonedDateTime(date, dateFormat, theZone);
			if (null != orig) {
				ZonedDateTime newDateTime = orig.withZoneSameInstant(UTC_ZONE_ID);				
				retVar = convertZonedDateToMilliseconds(newDateTime);
			}
		}	
		
		return retVar;
	}
	
	public String writeDateString(ZonedDateTime theTime, String dateFormat) {
		String retVar = null;
		
		if (null != theTime && validString(dateFormat)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(this.zoneId);			
			retVar = theTime.format(formatter);
		}
		
		return retVar;
	}
	
	public String writeDateString(ZonedDateTime theTime, String dateFormat, String theZone) {
		String retVar = null;
		
		if (null != theTime && validString(dateFormat) && validString(theZone)) {
			ZoneId tempZoneId = getZoneId(theZone);
			if (null != tempZoneId) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(tempZoneId);			
				retVar = theTime.format(formatter);
			}	
		}
		
		return retVar;
	}
	
	public String convertZuluMillisecondsToDateString(Long milliSeconds, String dateFormat) {
		String retVar = null;
		
		if (null != milliSeconds && validString(dateFormat)) {
			Instant i = Instant.ofEpochSecond(milliSeconds);
			ZonedDateTime zulu = ZonedDateTime.ofInstant(i, UTC_ZONE_ID);
			
			ZoneId usingZone = this.zoneId;
			if (null != usingZone) {
				ZonedDateTime newDateTime = zulu.withZoneSameInstant(usingZone);
				retVar = writeDateString(newDateTime, dateFormat);
			}	
		}
		
		return retVar;
	}
	
	public String convertZuluMillisecondsToDateString(Long milliSeconds, String dateFormat, String timeZoneId) {
		String retVar = null;
		
		if (null != milliSeconds && validString(dateFormat) && validString(timeZoneId)) {
			Instant i = Instant.ofEpochSecond(milliSeconds);
			ZonedDateTime zulu = ZonedDateTime.ofInstant(i, UTC_ZONE_ID);
			
			ZoneId usingZone = getZoneId(timeZoneId);
			if (null != usingZone) {
				ZonedDateTime newDateTime = zulu.withZoneSameInstant(usingZone);
				retVar = writeDateString(newDateTime, dateFormat, timeZoneId);
			}	
		}
		
		return retVar;
	}
	
}
