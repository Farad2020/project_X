package com.example.projectX.controllers;

import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class PageReloader {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public PageReloader(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }

/*
    // Request Mapping
    @RequestMapping(value = "company_students", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> processReloadData(Model model,
                                                    @AuthenticationPrincipal ManagementStaff manager,
                                                    @RequestBody String body) {


        // Get your request
        JSONObject request = new JSONObject(body);
        String id = request.getString("id"); // Here the value is 'some id'

        // Get the new data in a JSONObject
        JSONObject response = new JSONObject();
        // build the response...

        // Send the response back to your client
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(response.toString(),
                headers, HttpStatus.OK);
    }
      */

}
