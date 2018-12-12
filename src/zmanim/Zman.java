package zmanim;

import net.sourceforge.zmanim.*;
import net.sourceforge.zmanim.util.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

public class Zman{
  
    public static void main(String [] args) {
        Scanner kyb = new Scanner(System.in);
	    do{    
	        System.out.println("Enter the location name, latitude, longitude, elevation, and timezone");
	        String locationName = kyb.next();
	        double latitude = kyb.nextDouble();
	        double longitude = kyb.nextDouble();
	        double elevation = kyb.nextDouble();
	        //use a Valid Olson Database timezone listed in java.util.TimeZone.getAvailableIDs()
	        TimeZone timeZone = TimeZone.getTimeZone(kyb.next());
	        //create the location object
	        GeoLocation location = new GeoLocation(locationName, latitude, longitude, elevation, timeZone);
	        //create the ZmanimCalendar
	        ZmanimCalendar zc = new ZmanimCalendar(location);
	        
	        
	        System.out.println("Enter the year and month# you want the zmanim for");
	        int year = kyb.nextInt();
	        int month;
	        do {
	            month = kyb.nextInt() - 1;
	            if(month < 0 || month >= 12)
	                System.out.println("Invalid month, try again.");
	        }while(month < 0 || month >= 12);
	        int day = 1;
	       
	     
	        System.out.println("How many Zmanim out of 12 do you want on the calendar?");
	        int numOfZmanim;
	        do {
	            numOfZmanim = kyb.nextInt();
	            if(numOfZmanim > 12)
	                System.out.println("Invalid number, try again.");
	        }while(numOfZmanim > 12);
	        
	        
	        System.out.println("Which "+ numOfZmanim +" zmanim do you want?\n(1) Alos Hashachar \n(2) Chatzos \n(3) Minchah Gedolah"
	                + " \n(4) Minchah kKetana \n(5) Plag Hamincha \n(6) Sof Zman Shma Gra \n(7) Sof Zman Shma Mga "
	                + "\n(8) Sof Zman Tefila Gra\n(9) Sof Zman Tefila Mga \n(10) Tzais\n(11) Sunrise \n(12) Sunset\n");
	        //gets zmanim choices
	        int[] zmanim = new int[numOfZmanim + 1];
	        zmanim[0] = 12;//this is for the non-optional day of week and date of month
	        for(int i = 1; i <= numOfZmanim; i++) {
	            do {
	                zmanim[i] = kyb.nextInt() - 1;//the -1 is to fit with the array of zmanim
	                if(!(zmanim[i] >= 0 && zmanim[i] <= 11))
	                    System.out.println("Invalid choice, try again.");
	            }while(!(zmanim[i] >= 0 && zmanim[i] <= 11));
	        }
	
	        System.out.println("The calendar for " + locationName +" " + getMonthName(month) + " " + year + " is...");
	        printMonth(zc, year, month, day, zmanim);
	        
	        System.out.println("Do you want to print another calendar? (Y/N)");
	    }while(kyb.next().toUpperCase().charAt(0) == 'Y');
        kyb.close();
    }
  
    // this method prints the whole month
    public static void printMonth(ZmanimCalendar zc, int year, int month, int day, int[] zmanim){
        System.out.println("Sunday \t\t\t\tMonday \t\t\t\tTuesday \t\t\tWednesday \t\t\tThursday \t\t\tFriday \t\t\t\tSaturday");
        printHorizontalLines();
        printWeek(zc, year, month, day, zmanim);//this method prints the first week with the appropiate  null days
        zc.getCalendar().set(year, month, day);
        day = 9 - zc.getCalendar().get(Calendar.DAY_OF_WEEK);//this sets the day number at Sunday of the second week
        for(int i = day; i < getDaysInMonth(year, month); i += 7)//this cycles through the rest of the days of the month
            printWeek(zc, year, month, i, zmanim);
    }
   
    // this method prints 1 week
    public static void printWeek(ZmanimCalendar zc, int year, int month, int day, int[] zmanim){
        for(int zmanChoice: zmanim)
            printGenericDate(zc, year, month, day, zmanChoice);  
        printHorizontalLines();
    }

    //this method prints all the zmanim based on the chosen zman
    public static void printGenericDate(ZmanimCalendar zc, int year, int month, int day, int zmanChoice) {
        printNullCalendarDays(zc, year, month, day);//this prints the first days of the first week, which are not in the specified month, as white space
        for(int i = zc.getCalendar().get(Calendar.DAY_OF_WEEK); i <= 7 ; i++){
            zc.getCalendar().set(year, month, day);
            if (day <= getDaysInMonth(year, month))//if this day is in the month
                System.out.print(getZmanChoice(zc, zmanChoice));
            else//if this day is not in the month
                printOneNullDay();
            day++;
        }
        System.out.println();
   }
  
    //this method contains the zmanim with their appropriate spaces
    public static String getZmanChoice(ZmanimCalendar zc, int zmanChoice){
        SimpleDateFormat HMS = new SimpleDateFormat("HH:mm:SS");
        SimpleDateFormat date = new SimpleDateFormat("E-dd-MM-yyyy");
        String[] zmanim = {
                String.format("Alos Hashachar: %-15s|", HMS.format(zc.getAlosHashachar())),
                String.format("Chatzos: %-22s|", HMS.format(zc.getChatzos())),
                String.format("Mincha Gedola: %-16s|", HMS.format(zc.getMinchaGedola())),
                String.format("Mincha Ketana: %-16s|", HMS.format(zc.getMinchaKetana())),
                String.format("Plag Hamincha: %-16s|", HMS.format(zc.getPlagHamincha())),
                String.format("Sof Zman Shema GRA: %-11s|", HMS.format(zc.getSofZmanShmaGRA())),
                String.format("Sof Zman Shma MGA: %-12s|", HMS.format(zc.getSofZmanShmaMGA())),
                String.format("Sof Zman Tefilla GRA: %-9s|", HMS.format(zc.getSofZmanTfilaGRA())),
                String.format("Sof Zman Tefilla MGA: %-9s|", HMS.format(zc.getSofZmanTfilaMGA())),
                String.format("Tzais: %-24s|", HMS.format(zc.getTzais())),
                String.format("Sunrise: %-22s|", HMS.format(zc.getSunrise())),
                String.format("Sunset: %-23s|", HMS.format(zc.getSunset())),
                String.format("%-31s|", date.format(zc.getSunrise()))
                };
        return zmanim[zmanChoice];
    }
   
    // this method prints the days of the week which are not in the specified month, as white space
    public static void printNullCalendarDays(ZmanimCalendar zc, int year, int month, int day){
        zc.getCalendar().set(year, month, day);
        for(int i = 1; i < zc.getCalendar().get(Calendar.DAY_OF_WEEK); i++)
            printOneNullDay();
    }
   
	// this method prints one null days worth of spaces
	public static void printOneNullDay() {
		System.out.printf("%-31s|", "    ");
	}
   
    //this method prints the horizontal lines of the calendar
    public static void printHorizontalLines(){
        System.out.println("------------------------------------------------------------------"
                + "----------------------------------------------------------------------------"
                + "-----------------------------------------------------------------------------"
                + "----");
    }
    
	// this method returns the name of the month as a String based on the month # passed in
	public static String getMonthName(int month) {
		String[] months = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		return months[month];
	}
	
    // this method returns the days in the entered month, taking leap years into account
    public static int getDaysInMonth(int year, int month){
        int[] daysInEachMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 1 && isLeapYear(year))
            return 29;
        return daysInEachMonth[month];
    }

	// this method tests if the year entered is a leap year
	public static boolean isLeapYear(int year) {
		if (year % 4 != 0)
			return false;
		if (year % 100 != 0)
			return true;
		if (year % 400 != 0)
			return false;
		return true;
	}
}