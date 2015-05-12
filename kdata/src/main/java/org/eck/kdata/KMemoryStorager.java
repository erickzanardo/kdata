package org.eck.kdata;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class KMemoryStorager extends KStorager {

    @Override
    public KEntity save(KEntity entity, String idField) {

        String kind = entity.kind();
        if (KMemoryDB.db().get(kind) == null) {
            KMemoryDB.db().put(kind, new HashMap<Long, KEntity>());
        }

        JsonObject json = entity.toJson();
        if (json.get(idField) != null && !json.get(idField).isJsonNull()) {
            KMemoryDB.db().get(kind).put(json.get(idField).getAsLong(), entity);
        } else {
            json.addProperty(idField, System.currentTimeMillis());
            entity.fromJson(json);
            KMemoryDB.db().get(kind).put(json.get(idField).getAsLong(), entity);
        }

        return entity;
    }

    @Override
    public void delete(KEntity entity, String idField) {
        if (entity != null) {
            String kind = entity.kind();
            Map<Long, KEntity> kinds = KMemoryDB.db().get(kind);
            if (kinds != null) {
                Long id = entity.toJson().get(idField).getAsLong();
                kinds.remove(id);
            }
        }
    }

}
