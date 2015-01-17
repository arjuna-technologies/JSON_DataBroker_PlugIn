/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json.test.jsonobjectfieldpassdataprocessor;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.connector.ObservableDataProvider;
import com.arjuna.databroker.data.connector.ObserverDataConsumer;
import com.arjuna.databroker.data.jee.DataFlowNodeLifeCycleControl;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSink;
import com.arjuna.dbutilities.testsupport.dataflownodes.dummy.DummyDataSource;
import com.arjuna.dplugins.json.JSONObjectFieldPassDataProcessor;

public class SimpleTest
{
    @Test
    public void simpleInvocation()
    {
        String              name       = "JSON Object Field Pass Data Processor";
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(JSONObjectFieldPassDataProcessor.FIELDSPASSED_PROPERTYNAME, "a,b,c");

        DummyDataSource dummyDataSource                  = new DummyDataSource("Dummy Data Source", Collections.<String, String>emptyMap());
        DataProcessor   jsonObjectFieldPassDataProcessor = new JSONObjectFieldPassDataProcessor(name, properties);
        DummyDataSink   dummyDataSink                    = new DummyDataSink("Dummy Data Sink", Collections.<String, String>emptyMap());

        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(dummyDataSource, null);
        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(jsonObjectFieldPassDataProcessor, null);
        DataFlowNodeLifeCycleControl.processCreatedDataFlowNode(dummyDataSink, null);

        ((ObservableDataProvider<String>) dummyDataSource.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) jsonObjectFieldPassDataProcessor.getDataConsumer(String.class));
        ((ObservableDataProvider<String>) jsonObjectFieldPassDataProcessor.getDataProvider(String.class)).addDataConsumer((ObserverDataConsumer<String>) dummyDataSink.getDataConsumer(String.class));

        dummyDataSource.sendData("{ \"a\": \"1\", \"b\": \"2\", \"c\": \"3\", \"d\": \"4\" }");

        DataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSource);
        DataFlowNodeLifeCycleControl.removeDataFlowNode(jsonObjectFieldPassDataProcessor);
        DataFlowNodeLifeCycleControl.removeDataFlowNode(dummyDataSink);

        assertNotNull("Unexpected 'null' data set", dummyDataSink.receivedData());
        assertEquals("Unexpected number of data items", 1, dummyDataSink.receivedData().size());
        JSONObject outputJSONObject = new JSONObject((String) dummyDataSink.receivedData().get(0));
        assertNotNull("Unexpected null output JSONObject", outputJSONObject);
        assertEquals("Unexpected number of keys", 3, outputJSONObject.keySet().size());
        assertEquals("Unexpected 'a' value", "1", outputJSONObject.getString("a"));
        assertEquals("Unexpected 'b' value", "2", outputJSONObject.getString("b"));
        assertEquals("Unexpected 'c' value", "3", outputJSONObject.getString("c"));
    }
}
