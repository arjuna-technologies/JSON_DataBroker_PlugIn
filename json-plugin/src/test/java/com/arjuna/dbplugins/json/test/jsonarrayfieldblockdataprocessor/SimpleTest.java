/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonarrayfieldblockdataprocessor;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONArray;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.connector.ObservableDataProvider;
import com.arjuna.databroker.data.connector.ObserverDataConsumer;
import com.arjuna.databroker.data.core.DataFlowNodeLifeCycleControl;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSink;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSource;
import com.arjuna.dbutilities.testsupport.dataflownodes.lifecycle.TestJEEDataFlowNodeLifeCycleControl;
import com.arjuna.dplugins.json.JSONArrayFieldBlockDataProcessor;

public class SimpleTest
{
    @Test
    public void simpleInvocation()
    {
        DataFlowNodeLifeCycleControl dataFlowNodeLifeCycleControl = new TestJEEDataFlowNodeLifeCycleControl();

        String              name       = "JSON Array Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONArrayFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DummyDataSource dummyDataSource                 = new DummyDataSource("Dummy Data Source", Collections.<String, String>emptyMap());
        DataProcessor   jsonArrayFieldPassDataProcessor = new JSONArrayFieldBlockDataProcessor(name, properties);
        DummyDataSink   dummyDataSink                   = new DummyDataSink("Dummy Data Sink", Collections.<String, String>emptyMap());

        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), dummyDataSource, null);
        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), jsonArrayFieldPassDataProcessor, null);
        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), dummyDataSink, null);

        ((ObservableDataProvider<String>) dummyDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) jsonArrayFieldPassDataProcessor.getDataConsumer(String.class));
        ((ObservableDataProvider<String>) jsonArrayFieldPassDataProcessor.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) dummyDataSink.getDataConsumer(String.class));

        dummyDataSource.sendData("[ { \"a\": \"1\", \"b\": \"2\" }, { \"c\": \"3\", \"d\": \"4\" }, { \"b\": \"2\", \"d\": \"4\" }]");

        dataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSource);
        dataFlowNodeLifeCycleControl.removeDataFlowNode(jsonArrayFieldPassDataProcessor);
        dataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSink);

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
