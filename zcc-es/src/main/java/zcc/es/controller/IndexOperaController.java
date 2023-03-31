package zcc.es.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import zcc.es.Bean.IndexInfo;
import zcc.es.Bean.Results;
import zcc.es.common.ResultType;
import zcc.es.utils.EsUtils;


/*
*
 * @auther: liuxianling
 * @date: 2020/2/17 23:28
 * @description:

*/

@RestController
@RequestMapping("/opera")
@Api(value = "ES操作全集",produces =  "ES操作全集")
public class IndexOperaController {

    @ApiOperation(value = "判断索引是否存在", notes = "判断索引是否存在")
    @RequestMapping(value = "/ifIndex", method = RequestMethod.GET)
    public Results<String> ifIndex(@RequestParam("indexName") String indexName ){
        boolean flag = EsUtils.isIndexExists(indexName);
        return Results.success(flag?"已存在":"不存在");
    }
    @ApiOperation(value = "创建索引，front调用", notes = "创建索引，front调用，通过标签及Clazz方式")
    @RequestMapping(value = "/createIndex", method = RequestMethod.POST)
    public Results<String> createIndex(@RequestBody IndexInfo indexs){

        if(indexs.getClazz()==null){
            return Results.error(ResultType.BAD_REQUEST);
        }
        boolean flag = EsUtils.createIndex(indexs);
        if (flag) {
            return Results.success();
        }
        return Results.error(ResultType.BAD_REQUEST.getCode());

    }
    @ApiOperation(value = "创建索引", notes = "创建索引")
    @RequestMapping(value = "/createIndexByJson", method = RequestMethod.POST)
    public Results<String> createIndexByJson(@RequestParam("indexName") String indexName, @RequestParam("indexInfoJson") String indexInfoJson){
        boolean flag = EsUtils.createIndexInfoJson(indexName,indexInfoJson);
        if (flag) {
            return Results.success();
        }
        return Results.error(ResultType.BAD_REQUEST.getCode());

    }
    @ApiOperation(value = "删除索引", notes = "删除索引")
    @RequestMapping(value = "/deleteIndex", method = RequestMethod.GET)
    public Results<Integer> deleteIndex(@RequestParam("indexName") String indexName ){
        EsUtils.delete(indexName);
        return Results.success();

    }


}
