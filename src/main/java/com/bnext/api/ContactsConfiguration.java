package com.bnext.api;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("contacts")
public class ContactsConfiguration {

    private String databaseName = "TestBnextDB";
    private String collectionName = "Contact";

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

}
