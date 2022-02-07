package bloomfilters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NaiveDifferential {

	String diff_file_path = "src/Files/DiffFile.txt";
	String grams_file_path = "src/Files/grams.txt";
	

	String RetrieveRecord(String key) {
		return RetrieveRecordFromDatabase(key, diff_file_path);
	}
	
	String RetrieveRecordFromDatabase(String s, String fileName) {

        String line = null;
        boolean isFound = false;

        try {
        	
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	if (line.equals(s)) {
            		isFound = true;
            		break;
            	}
            }
            
            bufferedReader.close();
            fileReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        
        if (isFound) {
        	return s;
        }
        return "THE QUERY WAS NOT FOUND";
	}

}
