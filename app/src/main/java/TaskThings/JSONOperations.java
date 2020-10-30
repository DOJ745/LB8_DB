package TaskThings;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONOperations {
    public static class Operations {

        public static void saveString(ArrayList<String> arr, String fileName, File filePath) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(filePath, fileName), arr);
            Log.d("Log_file", "File has been saved to: " + new File(fileName).getPath());
        }

        public static ArrayList<String> readString(File file){
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<String> readArr = new ArrayList<>();
            try{
                readArr = mapper.readValue(file, ArrayList.class);
            }
            catch (IOException e){
                Log.e("Log_e", "Failed to read from file\n" + e.getMessage());
                return readArr;
            }
            return readArr;
        }

        public static void createCategory(String fileName, File filePath) throws IOException {
            ArrayList<String> arr = new ArrayList<>();
            arr.add("Study");
            saveString(arr, fileName, filePath);
        }
    }
}
