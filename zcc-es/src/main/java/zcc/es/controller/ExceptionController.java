
package zcc.es.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import zcc.es.Bean.Results;
import zcc.es.common.ResultType;
import zcc.es.common.BusException;
import zcc.es.utils.Tools;

import javax.servlet.http.HttpServletResponse;


/**
 * controller 增强器
 */

@ControllerAdvice
public class ExceptionController {
    private final Logger log = LoggerFactory.getLogger(getClass());

/**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Results<Void> errorHandler(HttpServletResponse response, Exception ex) {
        int status = response.getStatus();
        log.error("Response status: {}", response.getStatus());
        log.error("error msg:{}", Tools.getException(ex));
        ResultType resultType = null;
        if (HttpStatus.BAD_REQUEST.value() == status) {
            resultType = ResultType.BAD_REQUEST;
        }
        else if (HttpStatus.NOT_FOUND.value() == status) {
            resultType = ResultType.NOT_FOUND;
        }
        else if (HttpStatus.INTERNAL_SERVER_ERROR.value() == status) {
            resultType = ResultType.INTERNAL_ERROR;
            log.error("内部错误", ex);
        } else {
            resultType = ResultType.FAILURE;
        }


        Results<Void> result = Results.error(resultType.getCode(),Tools.getException(ex));
        return result;
    }
    

/**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */

    @ResponseBody
    @ExceptionHandler(value = BusException.class)
    public Results<Void> busException(BusException ex) {
        Results<Void> result = new Results<Void>();
        result.setCode(ResultType.FAILURE.getCode());
        result.setMessage("业务异常");
        return result;
    }




}
