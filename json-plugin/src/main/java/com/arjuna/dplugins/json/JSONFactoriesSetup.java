/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dplugins.json;

import java.util.Collections;
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
        JSONObjectFieldPassDataProcessorFactory  jsonObjectFieldPassDataProcessorFactory  = new JSONObjectFieldPassDataProcessorFactory("JSON Object Field Pass Data Processor Factory", Collections.<String, String>emptyMap());
        JSONObjectFieldBlockDataProcessorFactory jsonObjectFieldBlockDataProcessorFactory = new JSONObjectFieldBlockDataProcessorFactory("JSON Object Field Block Data Processor Factory", Collections.<String, String>emptyMap());
        JSONArrayFieldPassDataProcessorFactory   jsonArrayFieldPassDataProcessorFactory   = new JSONArrayFieldPassDataProcessorFactory("JSON Array Field Pass Data Processor Factory", Collections.<String, String>emptyMap());
        JSONArrayFieldBlockDataProcessorFactory  jsonArrayFieldBlockDataProcessorFactory  = new JSONArrayFieldBlockDataProcessorFactory("JSON Array Field Block Data Processor Factory", Collections.<String, String>emptyMap());

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

    @EJB(lookup="java:global/databroker/control-core/DataFlowNodeFactoryInventory")
    private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
