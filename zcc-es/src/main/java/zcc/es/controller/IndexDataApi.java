package zcc.es.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import zcc.es.Bean.Results;
import zcc.es.common.BusContent;
import zcc.es.common.ResultType;
import zcc.es.utils.DateUtils;
import zcc.es.utils.EsUtils;
import zcc.es.utils.SnowIdUtils;
import zcc.es.utils.Tools;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
@Api(value = "搜索API",produces =  "搜索API")
@Slf4j
public class IndexDataApi {


    @ApiOperation(value = "增加数据", notes = "增加数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Results<String> insert(@RequestParam("indexName") String indexName, @RequestParam("idField") String idField, @RequestBody Map<String,Object> map ){

        if(Tools.isEmpty(map)){
            return Results.error(ResultType.BAD_REQUEST);
        }
        String id = "";
        if(map.get(idField)==null){
            id = String.valueOf(SnowIdUtils.generateId());
        }else{
            id = map.get(idField).toString();
        }
        EsUtils.save(indexName,id,map);
        return Results.success();

    }
    @ApiOperation(value = "增加数据", notes = "增加数据")
    @RequestMapping(value = "/insertm", method = RequestMethod.POST)
    public Results<String> insertm(@RequestParam("indexName") String indexName, @RequestParam("idField") String idField, @RequestBody Map<String,Object> map, @RequestParam("mustField") String[] mustFiled ){

        if(Tools.isEmpty(map)){
            return Results.error(ResultType.BAD_REQUEST);
        }
        for(String f: mustFiled){
            if(null==map.get(f)){
                log.error(f+"是必填字段！");
                return Results.error(f+"是必填字段！");
            }
        }
        Object d = map.get("crTime");
        if(null != d){
            map.put("crTime", DateUtils.dateToStr(new Date(d.toString())));
        }
        EsUtils.save(indexName,map.get(idField).toString(),map);
        return Results.success();

    }
    @ApiOperation(value = "批量增加数据", notes = "批量增加数据")
    @RequestMapping(value = "/insertBatch", method = RequestMethod.POST)
    public Results<Map<String,Object>> insertBetch(@RequestParam("indexName") String indexName, @RequestParam("idField") String idField, @RequestBody List<Map<String,Object>> record ){

        if(record.size()==0){
            log.error("数据为空");
            return Results.error("数据为空");
        }
        Map<String,Object> map = EsUtils.saveBatch(indexName,idField,record);
        //SnowIdUtils.generateId();
        return Results.success(map);

    }
    @ApiOperation(value = "批量增加数据", notes = "批量增加数据")
    @RequestMapping(value = "/insertBatchm", method = RequestMethod.POST)
    public Results<Map<String,Object>> insertBetchm(@RequestParam("indexName") String indexName, @RequestParam("idField") String idField, @RequestBody List<Map<String,Object>> record , @RequestParam("mustField") String[] mustFiled){
        if(record.size()==0){
            log.error("数据为空");
            return Results.error("数据为空");
        }
        StringBuffer sb = new StringBuffer();

        Iterator<Map<String,Object>> it=record.iterator();
        while(it.hasNext()){
            boolean flag = false;
            Map<String,Object> map = it.next();
            for(String f:mustFiled){
                if(null == map.get(f)){
                    log.error(f+"是必填字段！");
                    sb.append(f+"是必填字段！").append("\n");
                    flag = true;
                }
                Object d = map.get("crTime");
                if(null != d){
                    map.put("crTime", DateUtils.dateToStr(new Date(d.toString())));
                }
            }
            if(flag){
                it.remove();
            }
        }


        Map<String,Object> map = EsUtils.saveBatch(indexName,idField,record);
        if(StringUtils.isEmpty(sb.toString())){
            map.put("error1",sb.toString());

        }
        //SnowIdUtils.generateId();
        return Results.success(map);

    }
    @ApiOperation(value = "修改数据", notes = "修改数据")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public Results<String> update(@RequestParam("indexName") String indexName, @RequestParam("id") String id, @RequestBody Map<String,Object> record ){

        boolean flag =  EsUtils.update(indexName,id, record,false);
        if(flag){
            return Results.success();
        }
        return Results.error(ResultType.UPDATE_FAILURE);

    }
    @ApiOperation(value = "修改新增数据", notes = "修改新增数据")
    @RequestMapping(value = "/modifyById", method = RequestMethod.POST)
    public Results<String> modify(@RequestParam("indexName") String indexName, @RequestParam("id") String id, @RequestBody Map<String,Object> record ){

        boolean flag =  EsUtils.update(indexName,id, record,true);
        if(flag){
            return Results.success();
        }
        return Results.error(ResultType.UPDATE_FAILURE);

    }
    @ApiOperation(value = "根据ID删除数据", notes = "根据ID删除数据")
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    public Results<Integer> deleteById(@RequestParam("indexName") String indexName, @RequestParam("id") String id ){

        boolean flag =    EsUtils.delete(indexName,id);
        if(flag){
            return Results.success();
        }
        return Results.error(ResultType.DEL_FAILURE);


    }
    @ApiOperation(value = "根据ID批量删除数据", notes = "根据ID批量删除数据")
    @RequestMapping(value = "/deletesByIds", method = RequestMethod.GET)
    public Results<Integer> deletesByIds(@RequestParam("indexName") String indexName, @RequestParam("ids") String ids ){

        try{
            String[] arrId = ids.split(BusContent.DICT_SIGN);
            EsUtils.deleteBatch(indexName,arrId);
            return Results.success();
        }catch (Exception e){
            return Results.error(e.getMessage());
        }

    }

    @ApiOperation(value = "清空记录", notes = "清空记录")
    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public Results<Integer> clear(@RequestParam("indexName") String indexName ){

        try{
            EsUtils.clear(indexName);
            return Results.success();
        }catch (Exception e){
            return Results.error(e.getMessage());
        }

    }
}
