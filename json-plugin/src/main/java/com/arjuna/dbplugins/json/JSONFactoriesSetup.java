/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.arjuna.databroker.data.DataFlowNodeFactoryInventory;

@Startup
@Singleton
public class JSONFactoriesSetup
{
    @PostConstruct
    public void setup()
    {
        Map<String, String> jsonObjectFieldPassDataProcessorFactoryProperties = new HashMap<String, String>();
        jsonObjectFieldPassDataProcessorFactoryProperties.put("Description", JSONObjectFieldPassDataProcessorFactory.DESCRIPTION);
        Map<String, String> jsonObjectFieldBlockDataProcessorFactoryProperties = new HashMap<String, String>();
        jsonObjectFieldBlockDataProcessorFactoryProperties.put("Description", JSONObjectFieldBlockDataProcessorFactory.DESCRIPTION);
        Map<String, String> jsonArrayFieldPassDataProcessorFactoryProperties = new HashMap<String, String>();
        jsonArrayFieldPassDataProcessorFactoryProperties.put("Description", JSONArrayFieldPassDataProcessorFactory.DESCRIPTION);
        Map<String, String> jsonArrayFieldBlockDataProcessorFactoryProperties = new HashMap<String, String>();
        jsonArrayFieldBlockDataProcessorFactoryProperties.put("Description", JSONArrayFieldBlockDataProcessorFactory.DESCRIPTION);

        JSONObjectFieldPassDataProcessorFactory  jsonObjectFieldPassDataProcessorFactory  = new JSONObjectFieldPassDataProcessorFactory("JSON Object Field Pass Data Processor Factory", jsonObjectFieldPassDataProcessorFactoryProperties);
        JSONObjectFieldBlockDataProcessorFactory jsonObjectFieldBlockDataProcessorFactory = new JSONObjectFieldBlockDataProcessorFactory("JSON Object Field Block Data Processor Factory", jsonObjectFieldBlockDataProcessorFactoryProperties);
        JSONArrayFieldPassDataProcessorFactory   jsonArrayFieldPassDataProcessorFactory   = new JSONArrayFieldPassDataProcessorFactory("JSON Array Field Pass Data Processor Factory", jsonArrayFieldPassDataProcessorFactoryProperties);
        JSONArrayFieldBlockDataProcessorFactory  jsonArrayFieldBlockDataProcessorFactory  = new JSONArrayFieldBlockDataProcessorFactory("JSON Array Field Block Data Processor Factory", jsonArrayFieldBlockDataProcessorFactoryProperties);

        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(jsonObjectFieldPassDataProcessorFactory);
        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(jsonObjectFieldBlockDataProcessorFactory);
        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(jsonArrayFieldPassDataProcessorFactory);
        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(jsonArrayFieldBlockDataProcessorFactory);
    }

    @PreDestroy
    public void cleanup()
    {
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("JSON Object Field Pass Data Processor Factory");
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("JSON Object Field Block Data Processor Factory");
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("JSON Array Field Pass Data Processor Factory");
        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("JSON Array Field Block Data Processor Factory");
    }

    @EJB(lookup="java:global/databroker/data-core-jee/DataFlowNodeFactoryInventory")
    private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
