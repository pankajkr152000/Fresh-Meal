package com.foodies.freshmeal.common.exception.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.date.DateConverter;
import com.foodies.freshmeal.common.ruleContext.IRuleContext;
import com.foodies.freshmeal.common.ruleContext.impl.RuleContext;

public class DataExceptionUtils {

    public static Set<String> getBypassErrorByConcessionCd(String ruleCd, Map<String, Serializable> objectMap,
            String consessionCode) {

        if (!StringUtils.hasText(ruleCd) || !StringUtils.hasText(consessionCode))
            return null;

        IRuleContext ruleContext = new RuleContext();
        ruleContext.setRunDate(DateConverter.toDate(AppCalendar.getBusinessLocalDateTime()));
        ruleContext.set("concessionCode", consessionCode);
        if (objectMap != null && !objectMap.isEmpty()) {
            for (Map.Entry<String, Serializable> entry : objectMap.entrySet()) {
                ruleContext.set(entry.getKey(), entry.getValue());
            }
        }

        Set<String> errorSet = null;

        if (ruleContext.contains("concessionCode")) {
            String errorCodes = (String) ruleContext.get("concessionCode");
            String[] errorCodeArray = errorCodes.split(",");
            if (errorCodeArray != null && errorCodeArray.length != 0) {
                errorSet = new java.util.HashSet<>();
                for (String code : errorCodeArray) {
                    if (StringUtils.hasText(code))
                        errorSet.add(code.trim());
                }
                return errorSet;
            }
        }
        return errorSet;

    }

}
