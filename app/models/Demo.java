package models;

import org.jongo.marshall.jackson.oid.MongoObjectId;

public class Demo {

    @MongoObjectId
    public String _id;
    public String data;

}
