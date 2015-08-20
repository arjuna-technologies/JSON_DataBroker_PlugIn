/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonobjectfieldblockdataprocessor;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.connector.ObservableDataProvider;
import com.arjuna.databroker.data.connector.ObserverDataConsumer;
import com.arjuna.databroker.data.core.DataFlowNodeLifeCycleControl;
import com.arjuna.dbplugins.json.JSONObjectFieldBlockDataProcessor;
import com.arjuna.dbutils.testsupport.dataflownodes.dummy.DummyDataSink;
import com.arjuna.dbutils.testsupport.dataflownodes.dummy.DummyDataSource;
import com.arjuna.dbutils.testsupport.dataflownodes.lifecycle.TestJEEDataFlowNodeLifeCycleControl;

public class SimpleTest
{
    @Test
    public void simpleInvocation()
    {
        DataFlowNodeLifeCycleControl dataFlowNodeLifeCycleControl = new TestJEEDataFlowNodeLifeCycleControl();

        String              name       = "JSON Object Field Block Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONObjectFieldBlockDataProcessor.FIELDSBLOCKED_PROPERTYNAME, "b,d");

        DummyDataSource dummyDataSource                  = new DummyDataSource("Dummy Data Source", Collections.<String, String>emptyMap());
        DataProcessor   jsonObjectFieldPassDataProcessor = new JSONObjectFieldBlockDataProcessor(name, properties);
        DummyDataSink   dummyDataSink                    = new DummyDataSink("Dummy Data Sink", Collections.<String, String>emptyMap());

        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), dummyDataSource, null);
        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), jsonObjectFieldPassDataProcessor, null);
        dataFlowNodeLifeCycleControl.completeCreationAndActivateDataFlowNode(UUID.randomUUID().toString(), dummyDataSink, null);

        ((ObservableDataProvider<String>) dummyDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) jsonObjectFieldPassDataProcessor.getDataConsumer(String.class));
        ((ObservableDataProvider<String>) jsonObjectFieldPassDataProcessor.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) dummyDataSink.getDataConsumer(String.class));

        dummyDataSource.sendData("{ \"a\": \"1\", \"b\": \"2\", \"c\": \"3\", \"d\": \"4\" }");

        dataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSource);
        dataFlowNodeLifeCycleControl.removeDataFlowNode(jsonObjectFieldPassDataProcessor);
        dataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSink);

        assertNotNull("Unexpected 'null' data set", dummyDataSink.receivedData());
        assertEquals("Unexpected number of data items", 1, dummyDataSink.receivedData().size());
        JSONObject outputJSONObject = new JSONObject((String) dummyDataSink.receivedData().get(0));
        assertNotNull("Unexpected null output JSONObject", outputJSONObject);
        assertEquals("Unexpected number of keys", 2, outputJSONObject.keySet().size());
        assertEquals("Unexpected 'a' value", "1", outputJSONObject.getString("a"));
        assertEquals("Unexpected 'c' value", "3", outputJSONObject.getString("c"));
    }
}
