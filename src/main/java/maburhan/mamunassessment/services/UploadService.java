package maburhan.mamunassessment.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UploadService {

    public String[][] processUpload(MultipartFile file1, MultipartFile file2, String collationPolicy, Integer n){

        List<String> list1 = extractList(file1, n);
        List<String> list2 = extractList(file2, n);
        Collections.reverse(list2);

        String[][] collation;

        if(collationPolicy.equals("full")){
            collation = new String[n][2];

            addToCollation(collation, list1, 1, list1.size());
            addToCollation(collation, list2, 0, list2.size());

        } else if(collationPolicy.equals("normal")){
            int minSize = Math.min(list1.size(), list2.size());
            collation = new String[minSize][2];

            addToCollation(collation, list1, 1, minSize);
            addToCollation(collation, list2, 0, minSize);

        } else {
            throw new IllegalArgumentException("Invalid collation policy");
        }

        return collation;
    }

    private void addToCollation(String[][] collation, List<String> list, int column, int lastIndex){
        for (int i = 0; i < lastIndex ; i++) {
            collation[i][column] = list.get(i);
        }
    }

    private List<String> extractList(MultipartFile file, Integer n){

        List<String> list = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String row;
            int counter = 0;
            while ((row = reader.readLine()) != null && ++counter <= n) {
                String[] data = row.split(",");
                list.add(data[0]);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return list;
    }

}
