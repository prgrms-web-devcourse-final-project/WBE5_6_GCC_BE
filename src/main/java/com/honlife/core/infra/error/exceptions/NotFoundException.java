package com.honlife.core.infra.error.exceptions;

import com.honlife.core.infra.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends CommonException {

    public NotFoundException(ResponseCode code) {super(code);}

    public NotFoundException(ResponseCode code, Exception e) {
        super(code, e);
        log.error(e.getMessage(), e);
    }

}
