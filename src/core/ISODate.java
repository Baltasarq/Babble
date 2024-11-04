// Babble (c) 2014/24 Baltasar MIT License <baltasarq@gmail.com>


package core;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/** Holds a date that will be represented and interpreted as ISO.
  * Format: YYY-MM-DD.
  */
public class ISODate {
    public static final String FmtDate = "yyyy/MM/dd";

    public ISODate(int year, int month, int day)
    {
        final Calendar DATE = Calendar.getInstance();

        DATE.set( Calendar.YEAR, year );
        DATE.set( Calendar.MONTH, month );
        DATE.set( Calendar.DAY_OF_MONTH, day );

        this.date = DATE;
    }

    public ISODate(Calendar date)
    {
        this.date = date;
    }

    public ISODate()
    {
        this.date = Calendar.getInstance();
    }

    public Calendar getCalendarDate()
    {
        return this.date;
    }

    @Override
    public String toString()
    {
        return new SimpleDateFormat( FmtDate ).format( date.getTime() );
    }

    public static ISODate fromString(String strDate) throws ParseException
    {
        final Calendar DATE = Calendar.getInstance();

        DATE.setTime( new SimpleDateFormat( FmtDate ).parse( strDate ) );
        return new ISODate( DATE );
    }

    private final Calendar date;
}
