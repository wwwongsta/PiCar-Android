package com.example.picar.retrofit.model;

public class StatusUpdateResponse {
    /**
     *             "n": 1,
     *             "nModified": 1,
     *             "opTime": {
     *               "ts": "6683205425971593217",
     *               "t": 3
     *             },
     *             "electionId": "7fffffff0000000000000003",
     *             "ok": 1,
     *             "operationTime": "6683205425971593217",
     *             "$clusterTime": {
     *               "clusterTime": "6683205425971593217",
     *               "signature": {
     *                 "hash": "zOj7M437B61C0HMOkrTwThJCIcg=",
     *                 "keyId": "6675285197040123905"
     *               }
     *             }*/
    private int ok;

    public int getOk() {
        return ok;
    }

}
