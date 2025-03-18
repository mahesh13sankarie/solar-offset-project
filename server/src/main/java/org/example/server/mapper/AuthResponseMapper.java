package org.example.server.mapper;

import org.hibernate.mapping.Any;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: astidhiyaa
 * @date: 18/03/25
 */
public class AuthResponseMapper {
    //TODO: return Json

    Map<String, String> responseLogin(){
        Map<String, String> map = new HashMap<>();
        return map;
    }

    //TODO:
    //base response


    //build error etc

    //success
    private Map<String, String> baseResponse(Map<String, String> response){
        Map<String, String> map = new HashMap<>();
        map.put("status", "200");
        map.put("mesage", "success");
//        map.put("data", response);
        return map;
    }
}
