/*
 * Copyright (c) 2014, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonarrayfieldpassdataprocessor;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.connector.ObservableDataProvider;
import com.arjuna.databroker.data.connector.ObserverDataConsumer;
import com.arjuna.databroker.data.jee.DataFlowNodeLifeCycleControl;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSink;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSource;
import com.arjuna.dplugins.json.JSONArrayFieldPassDataProcessor;

public class SimpleTest
{
    @Test
    public void simpleInvocation()
    {
        String              name       = "JSON Array Field Pass Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "a,c");

        DummyDataSource dummyDataSource                 = new DummyDataSource("Dummy Data Source", Collections.<String, String>emptyMap());
        DataProcessor   jsonArrayFieldPassDataProcessor = new JSONArrayFieldPassDataProcessor(name, properties);
        DummyDataSink   dummyDataSink                   = new DummyDataSink("Dummy Data Sink", Collections.<String, String>emptyMap());

        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(dummyDataSource, null);
        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(jsonArrayFieldPassDataProcessor, null);
        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(dummyDataSink, null);

        ((ObservableDataProvider<String>) dummyDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) jsonArrayFieldPassDataProcessor.getDataConsumer(String.class));
        ((ObservableDataProvider<String>) jsonArrayFieldPassDataProcessor.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) dummyDataSink.getDataConsumer(String.class));

        dummyDataSource.sendData("[ { \"a\": \"1\", \"b\": \"2\" }, { \"c\": \"3\", \"d\": \"4\" }, { \"b\": \"2\", \"d\": \"4\" }]");

        DataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSource);
        DataFlowNodeLifeCycleControl.removeDataFlowNode(jsonArrayFieldPassDataProcessor);
        DataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSink);

        assertNotNull("Unexpected 'null' data set", dummyDataSink.receivedData());
        assertEquals("Unexpected number of data items", 1, dummyDataSink.receivedData().size());
        JSONArray outputJSONArray = new JSONArray((String) dummyDataSink.receivedData().get(0));
        assertNotNull("Unexpected null output JSONArray", outputJSONArray);
        assertEquals("Unexpected array length", 3, outputJSONArray.length());
        assertNotNull("Unexpected null array element 0", outputJSONArray.getJSONObject(0));
        assertEquals("Unexpected array element 0 length", 1, outputJSONArray.getJSONObject(0).length());
        assertEquals("Unexpected 'a' value, in array element 0", "1", outputJSONArray.getJSONObject(0).getString("a"));
        assertEquals("Unexpected array element 1 length", 1, outputJSONArray.getJSONObject(1).length());
        assertEquals("Unexpected 'c' value, in array element 1", "3", outputJSONArray.getJSONObject(1).getString("c"));
        assertEquals("Unexpected array element 2 length", 0, outputJSONArray.getJSONObject(2).length());
    }
}