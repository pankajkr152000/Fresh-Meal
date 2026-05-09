package com.foodies.freshmeal.common.ruleContext.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.foodies.freshmeal.common.ruleContext.IRuleContext;





public class RuleContext implements IRuleContext {

    private static final long serialVersionUID = 7839875592101779383L;

	Map<String, Serializable> parameters = new HashMap<>();

    Set<String> outputParameters = new HashSet<>();

    List<Serializable> contextAttributes = new ArrayList<>();

    public RuleContext() {
    }

    public RuleContext(RuleContext ruleContext) {
        if (ruleContext != null) {
            this.parameters = ruleContext.getParameters();
            this.contextAttributes = ruleContext.getContextAttributes();
        }
    }

    @Override
    public Object get(String key) {
        return parameters.get(key);
    }

    @Override
    public void set(String key, Object value) {
        parameters.put(key, (Serializable) value);
    }

    @Override
    public Object remove(String theName) {
        return parameters.remove(theName);
    }

    public List<Serializable> getContextAttributes() {
        return contextAttributes;
    }

    @Override
    public void setContextAttributes(String[] contextAttributes) {

        if (contextAttributes != null) {
            this.contextAttributes.addAll(Arrays.asList(contextAttributes));
        }
    }

    @Override
    public void setRunDate(Date runDate) {
        parameters.put("runDate", runDate);        
    }

    @Override
    public Map<String, Serializable> getParameters() {
        return parameters;
    }

    @Override
    public boolean contains(String name) {
        return parameters.containsKey(name);
    }

    @Override
    public Collection<String> getOutputParameters() {
        return outputParameters;
    }
    

	

}
