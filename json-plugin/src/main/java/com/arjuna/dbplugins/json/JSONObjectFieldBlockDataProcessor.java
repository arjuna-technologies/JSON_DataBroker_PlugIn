/*
 * Copyright (c) 2014-2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.json;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataFlow;
import com.arjuna.databroker.data.DataProcessor;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.jee.annotation.DataConsumerInjection;
import com.arjuna.databroker.data.jee.annotation.DataProviderInjection;
import com.arjuna.databroker.data.jee.annotation.PostConfig;
import com.arjuna.databroker.data.jee.annotation.PostCreated;
import com.arjuna.databroker.data.jee.annotation.PostRecovery;

public class JSONObjectFieldBlockDataProcessor implements DataProcessor
{
    private static final Logger logger = Logger.getLogger(JSONObjectFieldBlockDataProcessor.class.getName());

    public static final String FIELDSBLOCKED_PROPERTYNAME = "Fields Blocked";

    public JSONObjectFieldBlockDataProcessor()
    {
        logger.log(Level.FINE, "JSONObjectFieldBlockDataProcessor");

        _fieldsBlocked = new HashSet<String>();
    }

    public JSONObjectFieldBlockDataProcessor(String name, Map<String, String> properties)
    {
        logger.log(Level.FINE, "JSONObjectFieldBlockDataProcessor: " + name + ", " + properties);

        _fieldsBlocked = new HashSet<String>();

        _name       = name;
        _properties = properties;
        _dataFlow   = null;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public void setName(String name)
    {
        _name = name;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return Collections.unmodifiableMap(_properties);
    }

    @Override
    public void setProperties(Map<String, String> properties)
    {
        _properties = properties;
    }

    @Override
    public DataFlow getDataFlow()
    {
        return _dataFlow;
    }

    @Override
    public void setDataFlow(DataFlow dataFlow)
    {
        _dataFlow = dataFlow;
    }

    @PostCreated
    @PostRecovery
    @PostConfig
    public void config()
    {
        String fieldsBlockedProperty = _properties.get(FIELDSBLOCKED_PROPERTYNAME);

        _fieldsBlocked.clear();
        if (fieldsBlockedProperty != null)
            for (String fieldBlocked: fieldsBlockedProperty.split(","))
                _fieldsBlocked.add(fieldBlocked.trim());
    }

    @SuppressWarnings("unchecked")
    public void filter(String data)
    {
        try
        {
            JSONObject inputJSONObject  = new JSONObject(data);
            JSONObject outputJSONObject = new JSONObject();

            for (String field: (Set<String>) inputJSONObject.keySet())
            {
                try
                {
                    if (! _fieldsBlocked.contains(field))
                    {
                        Object fieldObjectValue = inputJSONObject.get(field);

                        outputJSONObject.put(field, fieldObjectValue);
                    }
                }
                catch (JSONException jsonException)
                {
                    logger.log(Level.WARNING, "Failed to transfer field");
                }
            }

            _dataProvider.produce(outputJSONObject.toString());
        }
        catch (Throwable throwable)
        {
            logger.log(Level.WARNING, "Failed to process");
        }
    }

    @Override
    public Collection<Class<?>> getDataConsumerDataClasses()
    {
        Set<Class<?>> dataConsumerDataClasses = new HashSet<Class<?>>();

        dataConsumerDataClasses.add(String.class);

        return dataConsumerDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataConsumer<T> getDataConsumer(Class<T> dataClass)
    {
        if (String.class.isAssignableFrom(dataClass))
            return (DataConsumer<T>) _dataConsumer;
        else
            return null;
    }

    @Override
    public Collection<Class<?>> getDataProviderDataClasses()
    {
        Set<Class<?>> dataProviderDataClasses = new HashSet<Class<?>>();

        dataProviderDataClasses.add(String.class);

        return dataProviderDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataProvider<T> getDataProvider(Class<T> dataClass)
    {
        if (String.class.isAssignableFrom(dataClass))
            return (DataProvider<T>) _dataProvider;
        else
            return null;
    }

    private Set<String> _fieldsBlocked;

    private String               _name;
    private Map<String, String>  _properties;
    private DataFlow             _dataFlow;
    @DataConsumerInjection(methodName="filter")
    private DataConsumer<String> _dataConsumer;
    @DataProviderInjection
    private DataProvider<String> _dataProvider;
}
