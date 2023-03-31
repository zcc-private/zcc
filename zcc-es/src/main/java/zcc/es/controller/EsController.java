package zcc.es.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zcc.es.utils.EsUtils;

@RestController
public class EsController {


    @RequestMapping(value = "/getEs",method = RequestMethod.GET)
    private void getEs(@RequestParam String indexName,@RequestParam String id){
        String s = EsUtils.queryDataById(indexName, id);
    }
}
