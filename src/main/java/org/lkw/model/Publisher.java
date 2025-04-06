package org.lkw.model;

public class Publisher {
    private int publisherId;
    private String publisherName;
    private String contactInfo;

    public Publisher() {
    }

    public Publisher(int publisherId, String publisherName, String contactInfo) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.contactInfo = contactInfo;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getName() {
        return publisherName;
    }

    public void setName(String name) {
        this.publisherName = name;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return publisherName;
    }
} 