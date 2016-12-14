package util;



import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


public class DateUtil 
{
	
	public static Date Date( final String date, final String dateFormat ){
        if( date == null) return null;
        
        final String dateTrim = date.trim();
        if( dateTrim.length() == 0 ) return null;		
		
		final SimpleDateFormat df = new SimpleDateFormat( dateFormat );
		Date result = null;
		
		try{
			result = df.parse( dateTrim );
		}catch( ParseException x ){ 
			x.printStackTrace();
		}	
		
		return result;
	}	
	
	public static String getString(Date date, String dateFormat ){
        SimpleDateFormat df = new SimpleDateFormat( dateFormat );
		String result = null;
		result = df.format(date);		
		return result;
	}
	
	public static Date stdDateLiteralToDate(String ds){
        if(ds==null) return null;
        
        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: StdDate: Invalid Date Format: ["+ds+"]");
		}	
		
		return d;
	}	
	
	public static Date stdDateFromStringNoFormat(String ds){
        if(ds==null) return null;
        
        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("yyyyMMdd");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: StdDate: Invalid Date Format: ["+ds+"]");
		}	
		
		return d;
	}
	
	public static Date stdStampLiteralToDate(String ds){
        if(ds==null) return null;
        

        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: StdStamp: Invalid Date Format: ["+ds+"]");

		}	
		
		return d;
		
		
	}	
			
		
	
	
	public static Date sodTimeLiteralToDate(String ds){
        if(ds==null) return null;
        
        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("HH:mm");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: Parse: Invalid Time Format: ["+ds+"]");

		}	
		
		return d;
		
		
	}	
	
	public static Date sodDateLiteralToDate(String ds){
        if(ds==null) return null;
        
        
        
        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: SOD: Invalid Date Format: ["+ds+"]");

		}	
		
		return d;
		
		
	}	
	
	public static Date sodFileStampLiteralToDate(String ds){
        if(ds==null) return null;
        
        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("yyyyMMddHHmmss");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: SOD File Stamp: Invalid Date Format: ["+ds+"]");

		}	
		
		return d;
		
		
	}	
	
	public static String dateToStdDateLiteral(Date d){
		if(d==null) return "null";
		
		SimpleDateFormat fmtDate=new SimpleDateFormat("yyyy-MM-dd");
		
		return fmtDate.format(d);
		
		
	}		
	
	public static String dateToStdStampLiteral(Date d){
		if(d==null) return "null";
		
		SimpleDateFormat fmtDate=new SimpleDateFormat("dd-MM-yy HH:mm:ss a");
		
		return fmtDate.format(d);
		
		
	}		
	
	public static void printStampLiteral(Date d){

		SimpleDateFormat fmtDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		System.out.println(fmtDate.format(d));
		
		
	}
	
	public static Date getNow() {
		return new Date();
	}		
	
	public static String getNowNotSeparator(Date d){
		if(d==null) return "null";
		
		SimpleDateFormat fmtDate=new SimpleDateFormat("yyyyMMddHHmmssSS");
		
		return fmtDate.format(d);
	}
	
	public static String getDateNotSeparator(Date d){
		if(d==null) return "null";
		
		SimpleDateFormat fmtDate=new SimpleDateFormat("yyMMdd");
		
		return fmtDate.format(d);
	}
	
	public static Date getNowAsDateOnly() {

		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
		
	}
	
	public static Date hourMinuteTimeLiteralToDate(String ds){
        if(ds==null) return null;
        
        String dsTrim=ds.trim();
        if(dsTrim.length()==0) return null;		
		
		Date d=null;
		SimpleDateFormat fmtDate=new SimpleDateFormat("HH:mm");
		
		try {
			d=fmtDate.parse(dsTrim);
		} catch (ParseException e) {
			System.out.println("Warning: Parse: Invalid Time Format: ["+ds+"]");

		}	
		
		return d;
		
		
	}
	
	public static Date getNowHourMinuteOnly() {
		//Date d=new Date();
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.SECOND,0);
		
		return cal.getTime();
		
	}
	
	public static void main(String [] args){
		DateUtil date = new DateUtil();
		System.out.println(date.getNowAsDateOnly());
		
	}
	
	@SuppressWarnings("deprecation")
	public static Date normalizeToHourMinuteOnly(Date d) {
		//Date d=new Date();
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, 1970);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.HOUR, d.getHours());
		cal.set(Calendar.MINUTE, d.getMinutes());
		
		return cal.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public static Date normalizeToDateTimeOnly(Date dt,Date tm) {
		//Date d=new Date();
		if(tm==null){
			tm = new Date();
			tm.setSeconds(0);
			tm.setHours(0);
			tm.setMinutes(0);
		}
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.YEAR, dt.getYear());
		cal.set(Calendar.MONTH, dt.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, dt.getDay());
		cal.set(Calendar.SECOND,tm.getSeconds());
		cal.set(Calendar.HOUR, tm.getHours());
		cal.set(Calendar.MINUTE, tm.getMinutes());
		
		return cal.getTime();
	}
	
	public static String getStdDateTime(Date d){
		Format f = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		String result = f.format(d)+" WIB";
		return result;
	}
	
	public static String getStdTimeDisplay(Date d){
		Format f = new SimpleDateFormat("HH:mm");
		String result = f.format(d);
		return result;
	}
	
	public static String getStdDateTimeDisplayWIB(Date d){
		String result="-";
		
		if(d!=null){
			Format f = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			result = f.format(d)+" WIB";			
		}
		

		return result;
	}
	
	public static String getStdDateTimeDisplay(Date d){
		String result="-";
		
		if(d!=null){
			Format f = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			result = f.format(d);			
		}
		

		return result;
	}
	
	public static String getStdDateDisplay(Date d){
		String result="-";
		
		if(d!=null){
			Format f = new SimpleDateFormat("dd MMM yyyy");
			//result = f.format(d)+" WIB";
			result = f.format(d);
		}
		

		return result;
	}	
	public static String getStdDateDisplay2(Date d){
		if(d!=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal.get(Calendar.YEAR) + "-" + String.format("%02d", (cal.get(Calendar.MONTH)+1)) + "-" + String.format("%02d",cal.get(Calendar.DAY_OF_MONTH));
		}else{
			return "";
		}
	}	
	
	public static String getStdDateIndOnly(Date d){
		
		Format f = new SimpleDateFormat("dd-MMM-yyyy");
		String result = f.format(d);
		result = result.replace("Jan", "Januari");
		result = result.replace("Feb", "Februari");
		result = result.replace("Mar", "Maret");
		result = result.replace("Apr", "April");
		result = result.replace("May", "Mei");
		result = result.replace("Jun", "Juni");
		result = result.replace("Jul", "Juli");
		result = result.replace("Aug", "Agustus");
		result = result.replace("Sep", "September");
		result = result.replace("Oct", "Oktober");
		result = result.replace("Nov", "November");
		result = result.replace("Dec", "Desember");
                result = result.replace("-", " ");       
		return result;
	}
	
	public static Date getTodayPlus( final int nDays ) {
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );
		cal.add( Calendar.DATE, nDays );
		
		final Date theDay = cal.getTime();
		//log.trace("getTodayPlus("+nDays+") = " + getStdDateTime(theDay) );
		return theDay;		
	}			
	
	
	public static String getTodayPlusAsConstraint( final int nDays ) {
		return new SimpleDateFormat("yyyyMMdd").format( getTodayPlus(nDays) );
	}
	
	public static Date normalizeToDateOnly(Date d){
		Calendar nd=Calendar.getInstance();
		
		nd.setTime(d);
		nd.set(Calendar.HOUR_OF_DAY, 0);
		nd.set(Calendar.MINUTE, 0);
		nd.set(Calendar.SECOND, 0);
		nd.set(Calendar.MILLISECOND, 0);		
		
		return nd.getTime();
				
	}
	
	@SuppressWarnings("deprecation")
	public static Date setDateAndTime(Date d,Date t){
		Calendar nd=Calendar.getInstance();
		
		nd.setTime(d);
		nd.set(Calendar.HOUR_OF_DAY, t.getHours());
		nd.set(Calendar.MINUTE, t.getMinutes());
		nd.set(Calendar.SECOND, 0);
		nd.set(Calendar.MILLISECOND, 0);		
		
		return nd.getTime();
				
	}
	
	public static Boolean isEqualsDateOnly(Date d1, Date d2){
		Boolean res = false;
		if(d1==null && d2==null){
			res = true;
		}else if(d1==null || d2==null){
			res = false;
		}else{
			Date dOne = DateUtil.normalizeToDateOnly(d1);
			Date dTwo = DateUtil.normalizeToDateOnly(d2);
			if(dOne.compareTo(dTwo)==0){
				res = true;
			}
		}
		return res;
	}
	
	//Date dMin = minimal date, Date d2 parameter
	public static Boolean isGreaterThanOrEqualsDateOnly(Date dMin, Date dPar){
		Boolean res = false;
		if(dMin==null && dPar==null){
			res = true;
		}else if(dMin==null || dPar==null){
			res = false;
		}else{
			Date dOne = DateUtil.normalizeToDateOnly(dMin);
			Date dTwo = DateUtil.normalizeToDateOnly(dPar);
			if(dTwo.compareTo(dOne)==0 || dTwo.after(dOne)){
				res = true;
			}
		}
		return res;
	}
	
	//Date dMin = minimal date, Date d2 parameter
	public static Boolean isGreaterThanDateOnly(Date dMin, Date dPar){
		Boolean res = false;
		if(dMin==null && dPar==null){
			res = true;
		}else if(dMin==null || dPar==null){
			res = false;
		}else{
			Date dOne = DateUtil.normalizeToDateOnly(dMin);
			Date dTwo = DateUtil.normalizeToDateOnly(dPar);
			if(dTwo.after(dOne)){
				res = true;
			}
		}
		return res;
	}
	
	//Date d1 = dMax, Date dParam date parameter
	public static Boolean isLessThanOrEqualsDateOnly(Date dMax, Date dParam){
		Boolean res = false;
		if(dMax==null && dParam==null){
			res = true;
		}else if(dMax==null || dParam==null){
			res = false;
		}else{
			Date dOne = DateUtil.normalizeToDateOnly(dMax);
			Date dTwo = DateUtil.normalizeToDateOnly(dParam);
			//if(dTwo.compareTo(dOne)==0 || dTwo.before(dOne)){
			if(dTwo.compareTo(dOne)==0 || dOne.before(dTwo)){
				res = true;
			}
		}
		return res;
	}
	
	//Date d1 = dMax, Date dParam date parameter
	public static Boolean isLessThanDateOnly(Date dMax, Date dParam){
		Boolean res = false;
		if(dMax==null && dParam==null){
			res = true;
		}else if(dMax==null || dParam==null){
			res = false;
		}else{
			Date dOne = DateUtil.normalizeToDateOnly(dMax);
			Date dTwo = DateUtil.normalizeToDateOnly(dParam);
			//if(dTwo.compareTo(dOne)==0 || dTwo.before(dOne)){
			if(dOne.before(dTwo)){
				res = true;
			}
		}
		return res;
	}
	
	//Date minDate = minimal date, Date maxDate = maximal date, Date dParam date date parameter
	public static Boolean isInRangeDateOnly(Date minDate,Date maxDate, Date dParam){
		Boolean res = false;
		if(minDate==null || maxDate==null || dParam==null){
			res = false;
		}else{
			Date dMin = DateUtil.normalizeToDateOnly(minDate);
			Date dMax = DateUtil.normalizeToDateOnly(maxDate);
			Date dPar = DateUtil.normalizeToDateOnly(dParam);
			if((dPar.after(dMin) &&	dPar.before(dMax)) || 
					dPar.compareTo(dMin)==0 ||
					dPar.compareTo(dMax)==0){
				
				res = true;
			}
		}
		return res;
	}
	
	public static String getRangeHourMinute(Date dStart, Date dEnd){		
		Long ldtEnd = dEnd.getTime();
		Long ldtStart = dStart.getTime();
		//Long intMsLdt = ldtCutOff - ldtNow;
		Long intScLdt = (ldtEnd - ldtStart)/1000;
		
		Long houDt = intScLdt / (60 * 60);
		Long minDt = (intScLdt - (houDt *(60 * 60)))/60;

		Integer zero = 0;
		if(houDt<0){
			houDt = zero.longValue();
		}
		if(minDt<0){
			minDt = zero.longValue();
		}
		//jam dan menit
		String range = houDt +"h "+ minDt + "m";
		return range;
	}
	
	public static Integer getLongIntervalDateInSecond(Date dStart, Date dEnd){		
		Long ldtEnd = dEnd.getTime();
		Long ldtStart = dStart.getTime();
		//Long intMsLdt = ldtCutOff - ldtNow;
		Long intScLdt = (ldtEnd - ldtStart)/1000;
		if(intScLdt < 0){
			return 0;
		}else{
			return intScLdt.intValue();
		}
	}
	
	public static int getDaysInterval(Date start, Date end) {
		int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;
		if (end.before(start)) {
			throw new IllegalArgumentException("The end date must be later than the start date");
		}
		 
		//reset all hours mins and secs to zero on start date
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(start);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		long startTime = (int) startCal.getTimeInMillis();
		 
		//reset all hours mins and secs to zero on end date
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		long endTime = endCal.getTimeInMillis();
		 
		return (int)(endTime - startTime) / MILLISECONDS_IN_DAY;
	}
	
	public static Date getDate6MonthBefore(Date dt) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        cal.add(Calendar.DATE, -1);
        cal.add(Calendar.MONTH, -6);        
        
		return cal.getTime();
	}
	public static Date getDate6MonthAfter(Date dt) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        cal.add(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 6);        
        
		return cal.getTime();
	}
	public static Date getDateYesterday(Date dt) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        cal.add(Calendar.DATE, -1);      
        
        
		return cal.getTime();
	}
	
	public static Date getDateTomorow(Date dt) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        cal.add(Calendar.DATE, 1);      
        
        
		return cal.getTime();
	}
	
	// chris
	public static String getYearOnly(Date dt){
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        Integer year = cal.get(Calendar.YEAR);
        return year.toString();
	}
	
	// chris
	public static String getMonthOnly(Date dt){
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        Integer month = cal.get(Calendar.MONTH);
        month++;
        String idMonth = String.format("%02d", month);
        return idMonth;
	}
	
	public static java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
		return java.sql.Date.valueOf(entityValue);
	}
	
	public static Date convertToDateColumn(LocalDate entityValue) {
		return Date.from(entityValue.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static String getDateRipTime(Date entityValue) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(entityValue);
		Integer day = cal.get(Calendar.DATE);
		Integer month = cal.get(Calendar.MONTH) + 1;
		Integer year = cal.get(Calendar.YEAR);
		System.out.println("--> month : " + month);
		return day + "-" + month + "-" + year;
	}

	// chris
	public static String getDayOnly(Date dt) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        Integer day = cal.get(Calendar.DAY_OF_MONTH);

        String idDay = String.format("%02d", day);
        return idDay;
	}

	public static String getDayOnlyTommorow(Date dt) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        Integer day = cal.get(Calendar.DATE);
        day++;
        String idDay = String.format("%02d", day);
        return idDay;
	}

	// chris
	public static Date fotoTimbangDateGenerateRule(Date dt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		if(hour>=0&&hour<11){
			Integer dayBefore = day - 1;
			calendar.set(Calendar.DAY_OF_MONTH, dayBefore);	
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE,59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 0);
		}
		
		return calendar.getTime();
	}
	// chris
	public static Integer getNomorHariDalamSeminggu(Date dt){
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	// chris
	public static String getDateRipTimeForSQL(Date currDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		Integer day = cal.get(Calendar.DATE);
		Integer month = cal.get(Calendar.MONTH) + 1;
		Integer year = cal.get(Calendar.YEAR);
		return year.toString() + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
	}
}
