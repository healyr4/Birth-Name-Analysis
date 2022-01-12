
/**
 * Write a description of BabyBirths here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;

public class BabyBirths {
    public void printNames () {
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser(false);
        
        for(CSVRecord record : parser){
            int numBorn = Integer.parseInt(record.get(2));
            if (numBorn <= 100){
		System.out.println("Name " + record.get(0) +
				   " Gender " + record.get(1) +
				   " Num Born " + record.get(2));                
            }
        }
    }
    
    public void totalBirths(FileResource fr){
        int numBirths = 0;
        int numBoys = 0;
        int numGirls = 0;
        
        for(CSVRecord record : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(record.get(2));
            numBirths += 1;
            if (record.get(1).equals("F")){
               numGirls += 1;
            }
            else{
                numBoys += 1;
            }
        }
        System.out.println("Num Total names: " + numBirths +
                            " Num girls names: " + numGirls +
                            " Num boys names: " + numBoys);
    }   
    
    public void testTotalBirths() {
        // "C:\\Users\healy\\OneDrive\\JavaCourse1\\Birth-Data\\us_babynames_test\\yob2014short.csv"
        FileResource fr = new FileResource("us_babynames_by_year/yob1905.csv");
        totalBirths(fr);
    }
    
    public int getRank(String year, String name, String gender){
        int rank = 0;
        
        FileResource fr = new FileResource("us_babynames_by_year/yob"+ year + ".csv");
        for(CSVRecord record : fr.getCSVParser(false)){
            if (record.get(1).equals(gender)){
                rank += 1;
                if(record.get(0).equals(name)){
                    return rank;
                }
            }
        
        }
        return -1;
    
    }
    
    public String getName(String year, String name, String newYear, String gender){
        int rank = getRank(year,name,gender);
        int counter = 0;
        FileResource fr = new FileResource("us_babynames_by_year/yob"+ newYear + ".csv");
        
        for(CSVRecord record : fr.getCSVParser(false)){
            if (record.get(1).equals(gender)){
                counter += 1;
                if(counter == rank){
                    return record.get(0);
                }
            }
        
        }
        return "NO NAME";
    }
    

    public int yearOfHighestRank(String name, String gender){
        int highestYear = 0;
        DirectoryResource dr = new DirectoryResource();
        String year = "";
        int currHighestRank = Integer.MAX_VALUE;
        //String highestYear = "";
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            
            String currFile = f.getName();

            // Ex: yob2011.csv
            int startIndex = currFile.indexOf("b") + 1;
            int endIndex = currFile.indexOf(".",startIndex);
            String yearName = currFile.substring(startIndex,endIndex);
            int yearNumber = Integer.parseInt(yearName);
            
            int rank = getRank(yearName,name,gender);
            if (rank <0 && currHighestRank <0){   
            }
            
            else if(rank!= -1 && rank <  currHighestRank){
                currHighestRank = rank;
                highestYear = yearNumber;
            }
        }
        // Hasn't been updated so name hasn't been ranked
        if(currHighestRank == Integer.MAX_VALUE){
            return -1;
        }
        return highestYear;
    }
    
    public double  getAverageRank (String name, String gender){
        int totalRank = 0;
        int numFiles = 0;
        DirectoryResource dr = new DirectoryResource();
        int currHighestRank = Integer.MAX_VALUE;
        for(File f : dr.selectedFiles()){
            numFiles++;
            FileResource fr = new FileResource(f);
            String currFile = f.getName();

            // Ex: yob2011.csv
            int startIndex = currFile.indexOf("b") + 1;
            int endIndex = currFile.indexOf(".",startIndex);
            String yearName = currFile.substring(startIndex,endIndex);
            int yearNumber = Integer.parseInt(yearName);
            
            int rank = getRank(yearName,name,gender);
            
            if(rank > 0){
                totalRank += rank;
            }    
        }    
        double avgRank = (double) totalRank/numFiles;    
        return avgRank;
    }
    
    
    public int  getTotalBirthsRankedHigher (String year, String name, String gender){
        int numHigher = 0;
        FileResource fr = new FileResource("us_babynames_by_year/yob"+ year + ".csv");
        boolean flag = false;
        for(CSVRecord record : fr.getCSVParser(false)){
            if (record.get(1).equals(gender)) {
                if (record.get(0).equals(name)) {
                    flag = true;
                    break;
                }
            
                numHigher += Integer.parseInt(record.get(2));
            }
        }   
        if(flag){
            return numHigher;
        }
        
        return -1;
    }
    
    
    public void tester(){
        //Test Rank
        int rank = getRank("1996","Robert", "M");
        System.out.println("Rank is: " + rank);
        
        //Test what Ranking would be in another year
        String birthYear = "1990";
        String birthName = "Emily";
        String newYear = "2014";
        String sex = "F";
        String altName = getName(birthYear, birthName, newYear, sex);
        System.out.println(birthName +" born in  " + birthYear
                            + " would be " + altName + " if born in "
                            + newYear);
                            
        //Getting year of highest rank
        int yearOfHR = yearOfHighestRank(birthName,sex);
        System.out.println("Year of highest rank is: " + yearOfHR);
        
        //Getting average rank
        double avgRank = getAverageRank (birthName, sex);
        System.out.println("Average rank is: " + avgRank);
        
        //Getting num births ranked higher
        int numHigher = getTotalBirthsRankedHigher(birthYear, birthName, sex);
        System.out.println("Num ranked higher: " + numHigher);
        
        
        
    }
    
}