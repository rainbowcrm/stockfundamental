package com.primus.query.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:20452", maxAge = 3600)
@RequestMapping("/query")
public class QueryController {

    @RequestMapping(value = "/runQuery", method = RequestMethod.POST)
    public ResponseEntity<List<Map>> applyQuery(       @RequestBody Map<String,Object> queryContent )
    {
        return null;
    }

}
