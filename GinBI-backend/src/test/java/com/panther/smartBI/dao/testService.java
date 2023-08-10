package com.panther.smartBI.dao;

import com.panther.smartBI.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Gin 琴酒
 * @data 2023/8/6 15:19
 */
@SpringBootTest
@Slf4j
public class testService {

    @Resource
    private ChartService chartService;

    @Test
    void testGetFailed(){
        List<Long> failedChart = chartService.getFailedChart();
        log.info("{}\n",failedChart.size());
        failedChart.forEach(Object::toString);
    }

}
