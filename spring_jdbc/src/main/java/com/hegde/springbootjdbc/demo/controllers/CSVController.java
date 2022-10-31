package com.hegde.springbootjdbc.demo.controllers;


import com.hegde.springbootjdbc.demo.services.CSVHelper;
import com.hegde.springbootjdbc.demo.services.CSVService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CSVController {

    private static final Logger logger = LogManager.getLogger(CSVController.class);

    @Autowired
    CSVService csvService; //service class that interacts with repository class to save data to the DB

    private boolean isMetaDataCorrect;
    private boolean isFileDataCorrect;

    @PostMapping(value = "/uploadcsv",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String uploadCSV( @RequestPart("file") MultipartFile file,@RequestPart String tableInfo) {//,

        String message;

        //metadata information about tables incorrect ---- throw the error message and stop
        isMetaDataCorrect = CSVHelper.isMetaDataCorrect(tableInfo);

        if(!isMetaDataCorrect){
            logger.error("CSV file table metadata information incorrect");
            return "CSV file table metadata information incorrect";
            /*throw new InvalidBusinessException("CSV file table metadata information not found",
                    ResponseStatusEnum.INVALID_DATA);*/
        }
        else {

            //csv data issues ----- throw the error message and stop
            isFileDataCorrect = CSVHelper.isFileDataValid(file,tableInfo);

            if(!isFileDataCorrect){
                logger.error("CSV file data incorrect "+file.getOriginalFilename());
                return "CSV file data incorrect "+file.getOriginalFilename();

            /*throw new InvalidBusinessException("CSV file data incorrect",
                    ResponseStatusEnum.INVALID_DATA);*/
            } else {
                if (CSVHelper.hasCSVFormat(file)) {
                    try {
                        // csvService.save(file,);

                        message = "Uploaded the file successfully: " + file.getOriginalFilename();

                /*responseObj = new ResponseObj(true,
                        message, message,
                        ResponseStatusEnum.FILE_UPLOADED);*/

                        return message;

                    } catch (Exception ex) {
                        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                        logger.error(message);
               /* throw new InvalidBusinessException(ex.getMessage() + " File upload Error. "+message,
                        ResponseStatusEnum.FILE_NOT_FOUND);*/

                    }
                }

                message = "Please upload a csv file!";
                logger.error(message);
      /*  throw new InvalidBusinessException(HttpStatus.BAD_REQUEST + " File upload issues. "+message,
                ResponseStatusEnum.FILE_NOT_FOUND);*/

                return message;
            }
            }
    }
}
