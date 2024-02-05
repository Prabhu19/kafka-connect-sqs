package com.nordstrom.kafka.connect.sqs;


import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.lang.reflect.InvocationTargetException;

public class DynamicProtoParser {

    public static Message.Builder getBuilderForClassName(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        try {
            return (Message.Builder) clazz.getMethod("newBuilder").invoke(clazz);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error getting builder for class: " + className, e);
        }
    }

    public static Descriptors.Descriptor getDescriptorForClassName(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        try {
            return (Descriptors.Descriptor) clazz.getMethod("getDescriptor").invoke(clazz);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error getting descriptor for class: " + className, e);
        }
    }

    public static Message parseJsonToProtobuf(String json, Message.Builder builder) {
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Error parsing JSON to Protobuf", e);
        }
    }
//
//    public static void main(String[] args) {
//        String className = "com.nordstrom.kafka.connect.eventbus.RiderLocation";
//
//        // Get the builder for the specified class name
//        Message.Builder builder;
//        try {
//            builder = getBuilderForClassName(className);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("Class not found: " + className, e);
//        }
//
//        // Example JSON string
//        String jsonString = "{\"type\":\"automotive\",\"data\":{\"courier\":{\"id\":1,\"user_id\":2},\"location\":{\"latitude\":123.456,\"longitude\":789.012},\"metadata\":{\"speed\":10.5,\"heading\":90.0,\"accuracy\":0.8,\"mock_location\":false,\"device_id\":\"device123\",\"platform\":\"android\",\"roadrunner_version\":\"1.0\"}},\"country_code\":\"US\",\"timestamp\":\"2024-02-05T12:00:00Z\",\"version\":1}";
//
//        // Parse JSON to Protobuf
//        Message message = parseJsonToProtobuf(jsonString, builder);
//
//        System.out.println("Parsed Protobuf message:\n" + message);
//    }
}
