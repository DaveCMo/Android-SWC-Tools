package com.swctools.swc_server_interactions.legacy;

import javax.json.JsonArray;

public class DisneySingleCommandGuildGetPublicResponse extends DisneySingleCommandResponse {
    private JsonArray members;
    public DisneySingleCommandGuildGetPublicResponse(String response) {
        super(response);
        members = data.result().getJsonArray("members");
    }

    public DisneySingleCommandGuildGetPublicResponse(Exception e) {
        super(e);
    }

    public JsonArray getMembers() {
        return members;
    }
}
