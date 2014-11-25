/*
 * Copyright (c) 2014, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.connector.ObservableDataProvider;
import com.arjuna.databroker.data.connector.ObserverDataConsumer;
import com.arjuna.databroker.data.jee.DataFlowNodeLifeCycleControl;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSink;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSource;
import com.arjuna.dplugins.json.JSONFieldFilterDataProcessor;

public class SimpleTest
{
    @Test
    public void simpleInvocation()
    {
        String              name       = "JSON Field Filter Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONFieldFilterDataProcessor.JSON_FIELDSPASSED_PROPERTYNAME, "a,b,c");

        DummyDataSource dummyDataSource           = new DummyDataSource("Dummy Data Source", Collections.<String, String>emptyMap());
        DataProcessor   jsonFieldFilterDataSource = new JSONFieldFilterDataProcessor(name, properties);
        DummyDataSink   dummyDataSink             = new DummyDataSink("Dummy Data Sink", Collections.<String, String>emptyMap());

        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(dummyDataSource, null);
        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(jsonFieldFilterDataSource, null);
        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(dummyDataSink, null);

        ((ObservableDataProvider<String>) dummyDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) jsonFieldFilterDataSource.getDataConsumer(String.class));
        ((ObservableDataProvider<String>) jsonFieldFilterDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) dummyDataSink.getDataConsumer(String.class));

        DataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSource);
        DataFlowNodeLifeCycleControl.removeDataFlowNode(jsonFieldFilterDataSource);
        DataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSink);

        assertEquals("Didn't receive any tweets", 4, dummyDataSink.receivedData().size());
    }
}
