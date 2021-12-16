package maburhan.mamunassessment.controllers;

import maburhan.mamunassessment.services.UploadService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    private UploadService uploadService;
    private String csv;

    public UploadController(UploadService uploadProcessor) {
        this.uploadService = uploadProcessor;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @PostMapping("/upload")
    public String processUpload(@RequestParam("file1") MultipartFile file1,
                                @RequestParam("file2") MultipartFile file2,
                                @RequestParam("collation_policy") String CollationPolicy,
                                @RequestParam("no_lines") Integer n,
                                @RequestParam(name = "download", required = false) String download,
                                Model model){

        String[][] collation = uploadService.processUpload(file1, file2, CollationPolicy, n);

        List<String> list = Arrays.stream(collation).map(row -> String.join(",", row)).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for(String value : list){
            sb.append(value + '\n');
        }
        this.csv = sb.toString();

       if(download != null && download.equals("true")){
           return "redirect:download";
       } else{
           model.addAttribute("collation", this.csv);
           return "index";
       }
    }

    @GetMapping("/download")
    public void downLoadCSV(HttpServletResponse response) throws Exception{

        String filename = "collation.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        response.getWriter().print(this.csv);
    }


}
