package com.example.filestore.controllers;

import com.example.filestore.entities.AjaxResponse;
import com.example.filestore.entities.Bucket;
import com.example.filestore.entities.ValidationError;
import com.example.filestore.entities.ValidationErrorBuilder;
import com.example.filestore.services.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class APIController {

    @Autowired
    BucketService bucketService;

    @ResponseBody
    @RequestMapping(value = "/api/folder/create", method = RequestMethod.POST)
    public AjaxResponse createNewFolder(@RequestBody Bucket bucket){


        System.out.println(bucket.getName());

        AjaxResponse ajaxResponse = new AjaxResponse(true, "ddd");

        bucketService.createFolder(bucket);


        return ajaxResponse;

    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(MethodArgumentNotValidException exception) {
        return createValidationError(exception);
    }

    private ValidationError createValidationError(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }

//    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public DataTableOutput getForms(){
//
//    }
}
